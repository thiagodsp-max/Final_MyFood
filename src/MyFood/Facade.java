package MyFood;

import MyFood.Exceptions.NaoCadastrado;
import MyFood.Exceptions.Invalido;
import MyFood.Exceptions.NaoEncontrado;
import MyFood.Models.*;
import java.util.*;
import java.time.*;

public class Facade {
    private final Map<Integer, Usuario> users = new LinkedHashMap<>();
    //Criar espaços para armazenar junto de Restaurantes: Farmacias e Mercados
    private final Map<Integer, Empresa> lugar = new LinkedHashMap<>();
    private final Map<Integer, Produto> prod = new LinkedHashMap<>();
    private final Map<Integer, Pedido> orders = new LinkedHashMap<>();
    private int k=0;//Gerador de ids únicos
    Filtro logico = new Filtro(users,lugar);

    public void zerarSistema(){ //TODO O SISTEMA É RESETADO
        users.clear();
        lugar.clear();
        prod.clear();
        orders.clear();
        k=0;
    }
    public void encerrarSistema(){}

    public void criarUsuario(String name, String email, String senha, String ender)
            throws Invalido {
        logico.validauser(name,email,senha,ender);
        int id=k++;
        Usuario neo = new Cliente(id,name,email,senha,ender);
        users.put(id,neo);
    }
    public void criarUsuario(String name, String email, String senha, String ender, String cpf)
            throws Invalido {
        logico.validadono(name,email,senha,ender,cpf);
        int id=k++;
        Usuario neo = new Dono(id,name,email,senha,ender,cpf);
        users.put(id,neo);
    }

    public String getAtributoUsuario(int id, String atr)
            throws NaoCadastrado, Invalido {
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
        //Conferir se os Parâmetros não estão vazios, antes de fazer a procura
        if(email==null ||pass==null ||email.isBlank() ||pass.isBlank() ){
            throw new IllegalArgumentException("Login ou senha invalidos");
        }
        //Procurar por toda o Map para ver se o email existe
        for(Map.Entry<Integer,Usuario> entry : users.entrySet() ){
            Usuario z = entry.getValue();
            //Checar se o email está de acordo com o alvo
            if(z.getMail().equalsIgnoreCase(email)){
                //Checar se a senha está de acordo com o alvo
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

    //Criando uma Empresa do Tipo Restaurante
    public int criarEmpresa(String tipoEmpresa, int dono, String nome, String endereco, String tipoCozinha)
    throws Invalido{
        //Achar a pessoa do Id dono
        Usuario chef=users.get(dono);
        logico.permitido(chef);
        if(nome == null||nome.isBlank()){
            throw new Invalido("Nome");
        }
        //Regras da Empresa sobre Nome e Local e Busca nos Registros
        logico.cadastroempresa(dono,nome,endereco);
        //Criação da Empresa-Restaurante
        int id=k++;
        if(tipoCozinha == null||tipoCozinha.isBlank()){
            throw new Invalido("Tipo de cozinha");
        }
        Restaurante cozinha = new Restaurante(id,nome,endereco,tipoCozinha,chef);
        lugar.put(id,cozinha);
        return id;
    }
    //Criando Empresa do Tipo Mercado
    public int criarEmpresa(String tipoEmpresa, int dono, String nome, String endereco, String open, String close, String tipoMercado)
    throws Invalido,NaoCadastrado{
        if(tipoEmpresa==null||tipoEmpresa.isBlank()){
            throw new Invalido("Tipo de empresa");
        }
        Usuario chefe=users.get(dono);
        //Somente Donos podem criar Empresas
        logico.permitido(chefe);
        if(chefe == null){
            throw new NaoCadastrado(0);
        }
        //Checagem de Invalido do Nome - Endereco - V - etc
        if(nome == null||nome.isBlank()){
            throw new Invalido("Nome");
        }
        if(endereco == null||endereco.isBlank()){
            throw new Invalido("Endereco da empresa");
        }
        logico.periodo(open,close);
        //Validação dos Tipos de Mercado
        if (tipoMercado==null||tipoMercado.isBlank()) {
            throw new Invalido("Tipo de mercado");
        }
        if (!tipoMercado.equalsIgnoreCase("supermercado") &&
                !tipoMercado.equalsIgnoreCase("minimercado") &&
                !tipoMercado.equalsIgnoreCase("atacadista")) {
            throw new Invalido("Tipo de mercado");
        }
        //Verificação das Regras da Empresa
        logico.cadastroempresa(dono,nome,endereco);
        //Criação Própria para o Mercado
        int emp=k++;
        Mercado m=new Mercado(emp,nome,endereco,tipoMercado,chefe,open,close);
        lugar.put(emp,m);
        return emp;
    }

    public String getEmpresasDoUsuario(int dono){
        Usuario z = users.get(dono);
        logico.permitido(z);
        List<String> empresas=new ArrayList<>();
        for(Empresa x: lugar.values()){
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
        logico.permitido(y);
        List<Empresa> empresas=new ArrayList<>();
        //Adicionar no Array, as Empresas com o id do mesmo Dono
        for(Empresa x: lugar.values()){
            if(x.getDono().getId()==idDono && x.getNome().equalsIgnoreCase(nome)){
                empresas.add(x);
            }
        }
        if(empresas.isEmpty()){
            throw new IllegalArgumentException("Nao existe empresa com esse nome");
        }
        if(index >= empresas.size()){
            throw new IllegalArgumentException("Indice maior que o esperado");
        }
        return empresas.get(index).getId();
    }

    public String getAtributoEmpresa(int emp, String atr)throws NaoCadastrado,Invalido{
        Empresa x = lugar.get(emp);
        if(x == null){ //Analisar se
            throw new NaoCadastrado(1);
        }
        if(atr==null || atr.isBlank()){
            throw new Invalido("Atributo");
        } else if (atr.equalsIgnoreCase("nome")) {
            return x.getNome();
        } else if (atr.equalsIgnoreCase("endereco")) {
            return x.getEnder();
        } else if (atr.equalsIgnoreCase("dono")) {
            return x.getDono().getNome();
        }
        if(x instanceof Restaurante y){
            if (atr.equalsIgnoreCase("tipoCozinha")) {
                return x.getTipo();
            }
        }
        if(x instanceof Mercado y){
            if (atr.equalsIgnoreCase("tipoMercado")) {
                return y.getTipo();
            } else if (atr.equalsIgnoreCase("abre")) {
                return y.getOpen();
            } else if (atr.equalsIgnoreCase("fecha")) {
                return y.getClose();
            }
        }
        //Se o atr não bater com nenhum dos Atributos
        throw new Invalido("Atributo");
    }

    //Método Introduzido nos Testes 5 - Horário de Funcionamento do Mercado
    public void alterarFuncionamento(int emp, String abrir, String fechar)
            throws NaoCadastrado,Invalido{
        Empresa z=lugar.get(emp);
        if(z==null){
            throw new NaoCadastrado(1);
        } if(!(z instanceof Mercado w)){
            throw new IllegalArgumentException("Nao e um mercado valido");
        }
        logico.periodo(abrir,fechar);
        w.setOpen(abrir);
        w.setClose(fechar);
    }

    public int criarProduto(int emp, String nome, float valor, String cat)
    throws Invalido,NaoEncontrado{
        Empresa x = lugar.get(emp);
        if(x == null){
            throw new NaoEncontrado(1);
        }
        logico.checkproduct(nome,valor,cat);
        for(Produto z: prod.values()){
            if(z.getEmp()==emp && z.getNome().equalsIgnoreCase(nome)){
                throw new IllegalArgumentException("Ja existe um produto com esse nome para essa empresa");
            }
        }
        //Após a Validação, a criação do produto é feita
        int id=k++;
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
        Empresa y = lugar.get(emp);
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
        Empresa y=lugar.get(emp);
        if(y==null){
            throw new NaoEncontrado(1);
        }
        for(Pedido z:orders.values()){
            if(z.getIdcliente()==client && z.getIdempresa()==emp && z.getEstado().equals("aberto")){
                throw new IllegalArgumentException("Nao e permitido ter dois pedidos em aberto para a mesma empresa");
            }
        }
        //CRIAR UM PEDIDO
        int number=k++;
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