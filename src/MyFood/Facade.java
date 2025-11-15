package MyFood;

import MyFood.Exceptions.NaoCadastrado;
import MyFood.Exceptions.Invalido;
import MyFood.Exceptions.NaoEncontrado;
import MyFood.Models.*;
import java.util.*;
import java.time.*;

public class Facade {
    private Map<Integer, Usuario> users = new LinkedHashMap<>();
    private Map<Integer, Restaurante> lugar = new LinkedHashMap<>();
    private Map<Integer, Produto> prod = new LinkedHashMap<>();
    private Map<Integer, Pedido> orders = new LinkedHashMap<>();
    private int k1=0;
    private int k2=0;
    private int k3=0;
    private int k4=0;//Gerador de ids únicos
    Filtro logico = new Filtro(users,lugar);

    public void zerarSistema(){
        users.clear();
        k1=0;
    }
    public void encerrarSistema(){}

    public void criarUsuario(String name, String email, String senha, String ender)
            throws Invalido {
        logico.validauser(name,email,senha,ender);
        int id=k1++;
        Usuario neo = new Cliente(id,name,email,senha,ender);
        users.put(id,neo);
    }
    public void criarUsuario(String name, String email, String senha, String ender, String cpf)
            throws Invalido {
        logico.validadono(name,email,senha,ender,cpf);
        int id=k1++;
        Usuario neo = new Dono(id,name,email,senha,ender,cpf);
        users.put(id,neo);
    }

    public String getAtributoUsuario(int id, String atr)
            throws NaoCadastrado, Invalido {
        //Procurar pelo Id do Usuário em questão
        Usuario dude = users.get(id);
        if(dude == null){
            throw new NaoCadastrado(0);
        }
        if(atr==null || atr.isBlank()){
            throw new Invalido("Atributo");
        }
        //Filtrar se existe ou não o Atributo
        if(atr.equalsIgnoreCase("nome")){
            return dude.getNome();
        }
        else if(atr.equalsIgnoreCase("email")){
            return dude.getMail();
        }else if(atr.equalsIgnoreCase("senha")){
            return dude.getSenha();
        }else if(atr.equalsIgnoreCase("endereco")){
            return dude.getEndereco();
        }else if (atr.equals("cpf")) {
            String cpf = dude.getCpf();
            if (cpf == null) {
                throw new Invalido("CPF");
            }
            return cpf;
        }
        //Não é nenhum atributo conhecido
        throw new Invalido("Atributo");
    }

    public int login(String email, String pass){
        //Conferir se o Email é válido antes de procurar
        if(email==null ||pass==null ||email.isBlank() ||pass.isBlank() ){
            throw new IllegalArgumentException("Login ou senha invalidos");
        }
        //Procurar por toda o Map para ver se o email existe
        for(Map.Entry<Integer,Usuario> entry : users.entrySet() ){
            Usuario z = entry.getValue();
            if(z.getMail().equalsIgnoreCase(email)){
                //Checar se a senha está de acordo
                if(z.getSenha().equals(pass)){
                    return entry.getKey(); //Retornar Id desse User
                }
                else{
                    throw new IllegalArgumentException("Login ou senha invalidos");
                }
            }
        }
        throw new IllegalArgumentException("Login ou senha invalidos");
    }

    public int criarEmpresa(String tipoEmpresa, int dono, String nome, String endereco, String tipoCozinha)
    throws Invalido{
        //Achar a pessoa do Id dono
        Usuario chef=users.get(dono);
        if(chef==null || !(chef instanceof Dono)){
            throw new IllegalArgumentException("Usuario nao pode criar uma empresa");
        }
        if(nome == null||nome.isBlank()){
            throw new Invalido("Nome");
        }
        //REGRAS DE EMPRESA -- logico.regra(us);
        for(Restaurante r:lugar.values()){
            if(r.getNome().equalsIgnoreCase(nome) && r.getDono().getId()!=dono){
                throw new IllegalArgumentException("Empresa com esse nome ja existe");
            }
        }
        for(Restaurante r:lugar.values()){
            if(r.getNome().equalsIgnoreCase(nome) && r.getEnder().equalsIgnoreCase(endereco) && r.getDono().getId()==dono){
                throw new IllegalArgumentException("Proibido cadastrar duas empresas com o mesmo nome e local");
            }
        }
        //Criação da Empresa
        int id=k2++;
        Restaurante cozinha = new Restaurante(id,nome,endereco,tipoCozinha,chef);
        lugar.put(id,cozinha);
        return id;
    }

    public String getEmpresasDoUsuario(int dono){
        Usuario z = users.get(dono);
        if(z==null || !(z instanceof Dono)){
            throw new IllegalArgumentException("Usuario nao pode criar uma empresa");
        }
        List<String> empresas=new ArrayList<>();
        for(Restaurante x: lugar.values()){
            if(x.getDono().getId() == dono){
                empresas.add("["+x.getNome()+", "+x.getEnder()+"]");
            }
        }
        return "{"+empresas.toString()+"}";
    }

    public int getIdEmpresa(int idDono, String nome, int index) throws Invalido{
        if(nome==null || nome.isBlank()){
            throw new Invalido("Nome");
        }
        if(index<0){
            throw new Invalido("Indice");
        }
        Usuario y=users.get(idDono);
        if(y==null | !(y instanceof Dono)){
            throw new IllegalArgumentException("Usuario nao pode criar uma empresa");
        }
        List<Restaurante> empresas=new ArrayList<>();
        for(Restaurante x: lugar.values()){
            if(x.getDono().getId()==idDono && x.getNome().equalsIgnoreCase(nome)){
                empresas.add(x);
            }
        }
        //
        if(empresas.isEmpty()){
            throw new IllegalArgumentException("Nao existe empresa com esse nome");
        }
        if(index >= empresas.size()){
            throw new IllegalArgumentException("Indice maior que o esperado");
        }
        return empresas.get(index).getId();
    }

    public String getAtributoEmpresa(int emp, String atr)throws NaoCadastrado,Invalido{
        Restaurante x = lugar.get(emp);

        if(x == null){
            throw new NaoCadastrado(1);
        }
        if(atr==null || atr.isBlank()){
            throw new Invalido("Atributo");
        } else if (atr.equalsIgnoreCase("nome")) {
            return x.getNome();
        } else if (atr.equalsIgnoreCase("endereco")) {
            return x.getEnder();
        } else if (atr.equalsIgnoreCase("tipoCozinha")) {
            return x.getTipo();
        } else if (atr.equalsIgnoreCase("dono")) {
            return x.getDono().getNome();
        }
        //
        throw new Invalido("Atributo");
    }

    public int criarProduto(int emp, String nome, float valor, String cat)
    throws Invalido,NaoEncontrado{
        Restaurante x = lugar.get(emp);
        if(x == null){
            throw new NaoEncontrado(1);
        }
        logico.checkproduct(nome,valor,cat);
        for(Produto z: prod.values()){
            if(z.getEmp()==emp && z.getNome().equalsIgnoreCase(nome)){
                throw new IllegalArgumentException("Ja existe um produto com esse nome para essa empresa");
            }
        }
        //
        int id=k3++;
        Produto novo=new Produto(id,nome,valor,cat,emp);
        prod.put(id,novo);
        return id;
    }
    public void editarProduto(int prods, String nome, float valor, String cat)
    throws NaoCadastrado,Invalido{
        Produto x=prod.get(prods);
        if(x==null){
            throw new NaoCadastrado(2);
        }
        logico.checkproduct(nome,valor,cat);
        //Após a checagem hora de atualizar
        x.setNome(nome);
        x.setCateg(cat);
        x.setValor(valor);
    }
    public String getProduto(String nome, int emp, String atr)
    throws NaoEncontrado{
        Produto target=null;
        for(Produto y:prod.values()){
            if(y.getEmp()==emp && y.getNome().equalsIgnoreCase(nome)){
                target=y;
                break;
            }
        }
        if(target == null){
            throw new NaoEncontrado(0);
        }else if(atr.equalsIgnoreCase("valor")){
            return String.format(Locale.US,"%.2f",target.getValor());
        }else if(atr.equalsIgnoreCase("categoria")){
            return target.getCateg();
        }else if(atr.equalsIgnoreCase("empresa")){
            return lugar.get(target.getEmp()).getNome();
        }
        throw new IllegalArgumentException("Atributo nao existe");
    }
    public String listarProdutos(int emp) throws NaoEncontrado{
        Restaurante y = lugar.get(emp);
        if(y==null){
            throw new NaoEncontrado(1);
        }
        List<String> estoque=new ArrayList<>();
        for(Produto x:prod.values()){
            if(x.getEmp()==emp){
                estoque.add(x.getNome());
            }
        }
        return "{["+String.join(", ",estoque)+"]}";
    }

    public int criarPedido(int client, int emp) throws NaoEncontrado{
        Usuario x=users.get(client);
        if(x==null){
            throw new NaoEncontrado(3);
        }
        if(!(x instanceof Cliente)){
            throw new IllegalArgumentException("Dono de empresa nao pode fazer um pedido");
        }
        Restaurante y=lugar.get(emp);
        if(y==null){
            throw new NaoEncontrado(1);
        }
        for(Pedido z:orders.values()){
            if(z.getIdcliente()==client && z.getIdempresa()==emp && z.getEstado().equals("aberto")){
                throw new IllegalArgumentException("Nao e permitido ter dois pedidos em aberto para a mesma empresa");
            }
        }
        //CRIAR UM PEDIDO
        int number=k4++;
        Pedido novo=new Pedido(number,x,y);
        orders.put(number,novo);
        return number;
    }
    public int getNumeroPedido(int client, int emp, int index) throws Invalido{
        if(index<0){
            throw new Invalido("Index");
        }
        List<Pedido> ord=new ArrayList<>();
        for(Pedido z: orders.values()){
            if(z.getIdcliente()==client && z.getIdempresa()==emp){
                ord.add(z);
            }
        }
        //
        if(index>=ord.size()){
            throw new IllegalArgumentException("Indice maior que o esperado");
        }
        return ord.get(index).getNumero();
    }
    public void adicionarProduto(int num, int obj){
        Pedido pedido=orders.get(num);
        if(pedido==null){
            throw new IllegalArgumentException("Nao existe pedido em aberto");
        }
        if(!pedido.getEstado().equals("aberto")){
            throw new IllegalArgumentException("Nao e possivel adcionar produtos a um pedido fechado");
        }
        Produto z=prod.get(obj);
        if(z==null || z.getEmp()!=pedido.getIdempresa()){
            throw new IllegalArgumentException("O produto nao pertence a essa empresa");
        }
        pedido.getProduto().add(z);
    }
    public void fecharPedido(int num) throws NaoEncontrado{
        Pedido i = orders.get(num);
        if(i==null){
            throw new NaoEncontrado(2);
        }
        i.close();
    }
    public void removerProduto(int num, String nprod) throws NaoEncontrado,Invalido{
        Pedido v= orders.get(num);
        if(v==null){
            throw new NaoEncontrado(2);
        }
        if(nprod==null || nprod.isBlank()){
            throw new Invalido("Produto");
        }
        if(!v.getEstado().equals("aberto")){
            throw new IllegalArgumentException("Nao e possivel remover produtos de um pedido fechado");
        }
        List<Produto> estoque=v.getProduto();
        boolean removed=false;
        for(int i=0;i<estoque.size();i++){
            if((estoque.get(i).getNome()).equalsIgnoreCase(nprod)){
                estoque.remove(i);
                removed=true;
                break;
            }
        }
        if(!removed){
            throw new NaoEncontrado(0);
        }
    }
    public String getPedidos(int num, String atr) throws NaoEncontrado,Invalido{
        Pedido z=orders.get(num);
        if(z==null){
            throw new NaoEncontrado(2);
        }
        if(atr==null || atr.isBlank()){
            throw new Invalido("Atributo");
        }
        else if(atr.equalsIgnoreCase("cliente")){
            return z.getCliente();
        }
        else if(atr.equalsIgnoreCase("empresa")){
            return z.getEmpresa();
        }
        else if(atr.equalsIgnoreCase("estado")){
            return z.getEstado();
        }
        else if(atr.equalsIgnoreCase("produtos")){
            List<String> nomes=new ArrayList<>();
            for(Produto w: z.getProduto()){
                nomes.add(w.getNome());
            }
            return "{["+String.join(", ",nomes)+"]}";
        }
        else if(atr.equalsIgnoreCase("valor")){
            return String.format(Locale.US,"%.2f",z.calcular());
        }
        throw new IllegalArgumentException("Atributo nao existe");
    }

}