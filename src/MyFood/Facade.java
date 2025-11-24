package MyFood;

import MyFood.Exceptions.IndiceMaior;
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
    //Adicionar registros para Entregadores e suas Empresas
    private final Map<Integer, List<Integer>> porEmpresa = new LinkedHashMap<>();
    private final Map<Integer, List<Integer>> porEntregador = new LinkedHashMap<>();

    //Map para guardar as Entregas dos Pedidos
    private Map<Integer,Entrega> entregas= new LinkedHashMap<>();
    private Map<Integer, Integer> PedidoEntrega = new HashMap<>();
    private Set<Integer> Ocupados = new HashSet<>();
    private int k = 0;//Gerador de ids únicos
    Filtro logico = new Filtro(users, lugar);

    public void zerarSistema() { //TODO O SISTEMA É RESETADO
        users.clear();
        lugar.clear();
        prod.clear();
        orders.clear();
        k = 0;
    }

    public void encerrarSistema() {
    }

    public void criarUsuario(String name, String email, String senha, String ender)
            throws Invalido {
        logico.validauser(name, email, senha, ender);
        int id = k++;
        Usuario neo = new Cliente(id, name, email, senha, ender);
        users.put(id, neo);
    }

    public void criarUsuario(String name, String email, String senha, String ender, String cpf)
            throws Invalido {
        logico.validadono(name, email, senha, ender, cpf);
        int id = k++;
        Usuario neo = new Dono(id, name, email, senha, ender, cpf);
        users.put(id, neo);
    }

    public void criarUsuario(String name, String email, String senha, String endereco, String veiculo, String placa) throws Invalido {
        //Validação dos Campos de Entregador
        logico.validaentregador(name, email, senha, endereco, veiculo, placa);
        int id = k++;
        Usuario neo = new Entregador(id, name, email, senha, endereco, veiculo, placa);
        users.put(id, neo);
    }

    public String getAtributoUsuario(int id, String atr)
            throws NaoCadastrado, Invalido {
        Usuario dude = users.get(id);
        if (dude == null) {
            throw new NaoCadastrado(0);
        }
        if (atr == null || atr.isBlank()) {
            throw new Invalido("Atributo");
        }
        //Filtrar se existe ou não o Atributo
        if (atr.equalsIgnoreCase("nome")) {
            return dude.getNome();
        } else if (atr.equalsIgnoreCase("email")) {
            return dude.getMail();
        } else if (atr.equalsIgnoreCase("senha")) {
            return dude.getSenha();
        } else if (atr.equalsIgnoreCase("endereco")) {
            return dude.getEndereco();
        } else if (atr.equals("cpf")) {
            String cpf = dude.getCpf();
            if (cpf == null) {
                throw new Invalido("CPF");
            }
            return cpf;
        }
        if (dude instanceof Entregador z) {
            if (atr.equalsIgnoreCase("veiculo")) {
                return ((Entregador) dude).getVeiculo();
            } else if (atr.equalsIgnoreCase("placa")) {
                return ((Entregador) dude).getPlaca();
            }
        }
        //Não é nenhum atributo conhecido
        throw new Invalido("Atributo");
    }

    public int login(String email, String pass) {
        //Conferir se os Parâmetros não estão vazios, antes de fazer a procura
        if (email == null || pass == null || email.isBlank() || pass.isBlank()) {
            throw new IllegalArgumentException("Login ou senha invalidos");
        }
        //Procurar por toda o Map para ver se o email existe
        for (Map.Entry<Integer, Usuario> entry : users.entrySet()) {
            Usuario z = entry.getValue();
            //Checar se o email está de acordo com o alvo
            if (z.getMail().equalsIgnoreCase(email)) {
                //Checar se a senha está de acordo com o alvo
                if (z.getSenha().equals(pass)) {
                    return entry.getKey(); //Retornar Id desse User
                } else {
                    throw new IllegalArgumentException("Login ou senha invalidos");
                }
            }
        }
        throw new IllegalArgumentException("Login ou senha invalidos");
    }

    //Criando uma Empresa do Tipo Restaurante
    public int criarEmpresa(String tipoEmpresa, int dono, String nome, String endereco, String tipoCozinha)
            throws Invalido, NaoCadastrado {
        //Achar a pessoa do Id dono
        Usuario chef = users.get(dono);
        logico.permitido(chef);

        logico.validaempresa(chef, nome, endereco);
        //Regras da Empresa sobre Nome e Local e Busca nos Registros
        logico.cadastroempresa(dono, nome, endereco);
        //Criação da Empresa-Restaurante
        int id = k++;
        if (tipoCozinha == null || tipoCozinha.isBlank()) {
            throw new Invalido("Tipo de cozinha");
        }
        Restaurante cozinha = new Restaurante(id, nome, endereco, tipoCozinha, chef);
        lugar.put(id, cozinha);
        return id;
    }

    //Criando Empresa do Tipo Mercado
    public int criarEmpresa(String tipoEmpresa, int dono, String nome, String endereco, String open, String close, String tipoMercado)
            throws Invalido, NaoCadastrado {
        if (tipoEmpresa == null || tipoEmpresa.isBlank()) {
            throw new Invalido("Tipo de empresa");
        }
        Usuario chefe = users.get(dono);
        //Somente Donos podem criar Empresas
        logico.permitido(chefe);
        if (chefe == null) {
            throw new NaoCadastrado(0);
        }
        logico.validaempresa(chefe, nome, endereco);
        //Validação do Tempo de Abrir e de Fechar
        logico.periodo(open, close);
        //Validação dos Tipos de Mercado
        if (tipoMercado == null || tipoMercado.isBlank()) {
            throw new Invalido("Tipo de mercado");
        }
        if (!tipoMercado.equalsIgnoreCase("supermercado") &&
                !tipoMercado.equalsIgnoreCase("minimercado") &&
                !tipoMercado.equalsIgnoreCase("atacadista")) {
            throw new Invalido("Tipo de mercado");
        }
        //Verificação das Regras da Empresa
        logico.cadastroempresa(dono, nome, endereco);
        //Criação Própria para o Mercado
        int emp = k++;
        Mercado m = new Mercado(emp, nome, endereco, tipoMercado, chefe, open, close);
        lugar.put(emp, m);
        return emp;
    }

    //Criando Empresa do Tipo Farmacia
    public int criarEmpresa(String tipoEmpresa, int dono, String nome, String ender, boolean hora24, int numero)
            throws Invalido, NaoCadastrado {
        if (tipoEmpresa == null || tipoEmpresa.isBlank() || !tipoEmpresa.equalsIgnoreCase("farmacia")) {
            throw new Invalido("Tipo de empresa");
        }
        Usuario chefe = users.get(dono);
        //Verifica se o Usuario pode criar Empresa
        logico.permitido(chefe);
        //Abreviando o Loop que analisa nome, endereço e dono
        logico.validaempresa(chefe, nome, ender);
        //Busca para checar as Regras da Empresa
        logico.cadastroempresa(dono, nome, ender);

        int emp = k++;
        Farmacia z = new Farmacia(emp, nome, ender, hora24, numero, chefe);
        lugar.put(emp, z);
        return emp;
    }

    public String getEmpresasDoUsuario(int dono) {
        Usuario z = users.get(dono);
        logico.permitido(z);
        List<String> empresas = new ArrayList<>();
        for (Empresa x : lugar.values()) {
            if (x.getDono().getId() == dono) {
                empresas.add("[" + x.getNome() + ", " + x.getEnder() + "]");
            }
        }
        return "{" + empresas.toString() + "}";
    }

    public int getIdEmpresa(int idDono, String nome, int index) throws Invalido, IndiceMaior {
        if (nome == null || nome.isBlank()) {
            throw new Invalido("Nome");
        }
        if (index < 0) {
            throw new Invalido("Indice");
        }
        Usuario y = users.get(idDono);
        logico.permitido(y);
        List<Empresa> empresas = new ArrayList<>();
        //Adicionar no Array, as Empresas com o id do mesmo Dono
        for (Empresa x : lugar.values()) {
            if (x.getDono().getId() == idDono && x.getNome().equalsIgnoreCase(nome)) {
                empresas.add(x);
            }
        }
        if (empresas.isEmpty()) {
            throw new IllegalArgumentException("Nao existe empresa com esse nome");
        }
        if (index >= empresas.size()) {
            throw new IndiceMaior();
        }
        return empresas.get(index).getId();
    }

    public String getAtributoEmpresa(int emp, String atr) throws NaoCadastrado, Invalido {
        Empresa x = lugar.get(emp);
        if (x == null) { //Analisar se
            throw new NaoCadastrado(1);
        }
        if (atr == null || atr.isBlank()) {
            throw new Invalido("Atributo");
        } else if (atr.equalsIgnoreCase("nome")) {
            return x.getNome();
        } else if (atr.equalsIgnoreCase("endereco")) {
            return x.getEnder();
        } else if (atr.equalsIgnoreCase("dono")) {
            return x.getDono().getNome();
        }
        if (x instanceof Restaurante y) {
            if (atr.equalsIgnoreCase("tipoCozinha")) {
                return x.getTipo();
            }
        }
        if (x instanceof Mercado y) {
            if (atr.equalsIgnoreCase("tipoMercado")) {
                return y.getTipo();
            } else if (atr.equalsIgnoreCase("abre")) {
                return y.getOpen();
            } else if (atr.equalsIgnoreCase("fecha")) {
                return y.getClose();
            }
        }
        if (x instanceof Farmacia y) {
            if (atr.equalsIgnoreCase("aberto24horas")) {
                return String.valueOf(y.isaberto24());
            } else if (atr.equalsIgnoreCase("numeroFuncionarios")) {
                return String.valueOf(y.getFuncionarios());
            }
        }
        //Se o atr não bater com nenhum dos Atributos
        throw new Invalido("Atributo");
    }

    //Método Introduzido nos Testes 5 - Horário de Funcionamento do Mercado
    public void alterarFuncionamento(int emp, String abrir, String fechar)
            throws NaoCadastrado, Invalido {
        Empresa z = lugar.get(emp);
        if (z == null) {
            throw new NaoCadastrado(1);
        }
        if (!(z instanceof Mercado w)) {
            throw new IllegalArgumentException("Nao e um mercado valido");
        }
        //
        logico.periodo(abrir, fechar);
        w.setOpen(abrir);
        w.setClose(fechar);
    }

    public int criarProduto(int emp, String nome, float valor, String cat)
            throws Invalido, NaoEncontrado {
        Empresa x = lugar.get(emp);
        if (x == null) {
            throw new NaoEncontrado(1);
        }
        logico.checkproduct(nome, valor, cat);
        for (Produto z : prod.values()) {
            if (z.getEmp() == emp && z.getNome().equalsIgnoreCase(nome)) {
                throw new IllegalArgumentException("Ja existe um produto com esse nome para essa empresa");
            }
        }
        //Após a Validação, a criação do produto é feita
        int id = k++;
        Produto novo = new Produto(id, nome, valor, cat, emp);
        prod.put(id, novo);
        return id;
    }

    public void editarProduto(int prods, String nome, float valor, String cat)
            throws NaoCadastrado, Invalido {
        Produto x = prod.get(prods);
        if (x == null) {
            throw new NaoCadastrado(2);
        }
        logico.checkproduct(nome, valor, cat);
        //Após a checagem hora de atualizar
        x.setNome(nome);
        x.setCateg(cat);
        x.setValor(valor);
    }

    public String getProduto(String nome, int emp, String atr)
            throws NaoEncontrado {
        Produto target = null;
        for (Produto y : prod.values()) {
            if (y.getEmp() == emp && y.getNome().equalsIgnoreCase(nome)) {
                target = y;
                break;
            }
        }
        if (target == null) {
            throw new NaoEncontrado(0);
        } else if (atr.equalsIgnoreCase("valor")) {
            return String.format(Locale.US, "%.2f", target.getValor());
        } else if (atr.equalsIgnoreCase("categoria")) {
            return target.getCateg();
        } else if (atr.equalsIgnoreCase("empresa")) {
            return lugar.get(target.getEmp()).getNome();
        }
        throw new IllegalArgumentException("Atributo nao existe");
    }

    public String listarProdutos(int emp) throws NaoEncontrado {
        Empresa y = lugar.get(emp);
        if (y == null) {
            throw new NaoEncontrado(1);
        }
        List<String> estoque = new ArrayList<>();
        for (Produto x : prod.values()) {
            if (x.getEmp() == emp) {
                estoque.add(x.getNome());
            }
        }
        return "{[" + String.join(", ", estoque) + "]}";
    }

    public int criarPedido(int client, int emp) throws NaoEncontrado {
        Usuario x = users.get(client);
        if (x == null) {
            throw new NaoEncontrado(3);
        }
        if (!(x instanceof Cliente)) {
            throw new IllegalArgumentException("Dono de empresa nao pode fazer um pedido");
        }
        Empresa y = lugar.get(emp);
        if (y == null) {
            throw new NaoEncontrado(1);
        }
        for (Pedido z : orders.values()) {
            if (z.getIdcliente() == client && z.getIdempresa() == emp && z.getEstado().equals("aberto")) {
                throw new IllegalArgumentException("Nao e permitido ter dois pedidos em aberto para a mesma empresa");
            }
        }
        //CRIAR UM PEDIDO
        int number = k++;
        Pedido novo = new Pedido(number, x, y);
        orders.put(number, novo);
        return number;
    }

    public int getNumeroPedido(int client, int emp, int index) throws Invalido, IndiceMaior {
        if (index < 0) {
            throw new Invalido("Index");
        }
        List<Pedido> ord = new ArrayList<>();
        for (Pedido z : orders.values()) {
            if (z.getIdcliente() == client && z.getIdempresa() == emp) {
                ord.add(z);
            }
        }
        if (index >= ord.size()) {
            throw new IndiceMaior();
        }
        return ord.get(index).getNumero();
    }

    public void adicionarProduto(int num, int obj) {
        Pedido pedido = orders.get(num);
        if (pedido == null) {
            throw new IllegalArgumentException("Nao existe pedido em aberto");
        }
        if (!pedido.getEstado().equals("aberto")) {
            throw new IllegalArgumentException("Nao e possivel adcionar produtos a um pedido fechado");
        }
        Produto z = prod.get(obj);
        if (z == null || z.getEmp() != pedido.getIdempresa()) {
            throw new IllegalArgumentException("O produto nao pertence a essa empresa");
        }
        pedido.getProduto().add(z);
    }

    public void fecharPedido(int num) throws NaoEncontrado {
        Pedido i = orders.get(num);
        if (i == null) {
            throw new NaoEncontrado(2);
        }
        i.close();
    }

    public void removerProduto(int num, String nprod) throws NaoEncontrado, Invalido {
        Pedido v = orders.get(num);
        if (v == null) {
            throw new NaoEncontrado(2);
        }
        if (nprod == null || nprod.isBlank()) {
            throw new Invalido("Produto");
        }
        if (!v.getEstado().equals("aberto")) {
            throw new IllegalArgumentException("Nao e possivel remover produtos de um pedido fechado");
        }
        List<Produto> estoque = v.getProduto();
        boolean removed = false;
        for (int i = 0; i < estoque.size(); i++) {
            if ((estoque.get(i).getNome()).equalsIgnoreCase(nprod)) {
                estoque.remove(i);
                removed = true;
                break;
            }
        }
        if (!removed) {
            throw new NaoEncontrado(0);
        }
    }

    public String getPedidos(int num, String atr) throws NaoEncontrado, Invalido {
        Pedido z = orders.get(num);
        if (z == null) {
            throw new NaoEncontrado(2);
        }
        if (atr == null || atr.isBlank()) {
            throw new Invalido("Atributo");
        } else if (atr.equalsIgnoreCase("cliente")) {
            return z.getCliente();
        } else if (atr.equalsIgnoreCase("empresa")) {
            return z.getEmpresa();
        } else if (atr.equalsIgnoreCase("estado")) {
            return z.getEstado();
        } else if (atr.equalsIgnoreCase("produtos")) {
            List<String> nomes = new ArrayList<>();
            for (Produto w : z.getProduto()) {
                nomes.add(w.getNome());
            }
            return "{[" + String.join(", ", nomes) + "]}";
        } else if (atr.equalsIgnoreCase("valor")) {
            return String.format(Locale.US, "%.2f", z.calcular());
        }
        throw new IllegalArgumentException("Atributo nao existe");
    }

    //Métodos para Entregadores
    public void cadastrarEntregador(int emp, int us) throws NaoCadastrado, NaoEncontrado, Invalido {
        Empresa local = lugar.get(emp);
        if (local == null) {
            throw new NaoEncontrado(1);
        }
        Usuario boy = users.get(us);
        if (boy == null) {
            throw new NaoCadastrado(0);
        }
        if (!(boy instanceof Entregador)) {
            throw new IllegalArgumentException("Usuario nao e um entregador");
        }
        /*Precisaremos utilizar o método PutIfAbsent, para que caso o valor de
        associado a emp não esteja pronto, então seja colocado um valor provisorio
        que retorna null, isso ajudará no futuro a imprimir vários elementos da lista*/
        porEmpresa.putIfAbsent(emp, new ArrayList<>());
        if (porEmpresa.get(emp).contains(us)) {
            throw new IllegalArgumentException("Entregador ja cadastrado");
        }
        porEmpresa.get(emp).add(us);
        porEntregador.putIfAbsent(us, new ArrayList<>());
        porEntregador.get(us).add(emp);
    }

    public String getEntregadores(int empresa) throws NaoEncontrado {
        Empresa emp = lugar.get(empresa);
        if (emp == null) {
            throw new NaoEncontrado(1);
        }
        /*Precisaremos usar o Método GetOrDefault para conseguir inserir no Entregador
        os multiplos Valores que presentes no Id Empresa, e caso não haja nada, ele
        inserirá um Array vazio, permitindo que os resultados possam ser impressos*/
        List<Integer> listado = porEmpresa.getOrDefault(empresa, new ArrayList<>());
        List<String> emails = new ArrayList<>();
        //Inserir na Lista
        for (int id : listado) {
            Entregador z = (Entregador) users.get(id);
            emails.add(z.getMail());
        }
        return "{[" + String.join(", ", emails) + "]}";
    }

    public String getEmpresas(int entregador) throws NaoCadastrado {
        Usuario emp = users.get(entregador);
        if (emp == null) {
            throw new NaoCadastrado(0);
        }
        if (!(emp instanceof Entregador)) {
            throw new IllegalArgumentException("Usuario nao e um entregador");
        }
        List<Integer> listado = porEntregador.getOrDefault(entregador, new ArrayList<>());
        List<String> emails = new ArrayList<>();
        //
        for (int id : listado) {
            Empresa z = lugar.get(id);
            emails.add("[" + z.getNome() + ", " + z.getEnder() + "]");
        }
        return "{[" + String.join(", ", emails) + "]}";
    }

    //Métodos para Entrega
    public int criarEntrega(int pedido, int entregador, String destino) throws NaoCadastrado{
        Pedido p = orders.get(pedido);
        if (p == null) {
            throw new NaoCadastrado(2);
        }
        if (!"pronto".equalsIgnoreCase(p.getEstado())) {
            throw new IllegalArgumentException("Pedido nao esta pronto para entrega");
        }
        Usuario u = users.get(entregador);
        if (!(u instanceof Entregador)) {
            throw new IllegalArgumentException("Nao e um entregador valido");
        }
        //Vínculo do entregador com a empresa
        List<Integer> empresas = porEntregador.get(entregador);
        if (empresas == null || !empresas.contains(p.getIdempresa())) {
            throw new IllegalArgumentException("Nao e um entregador valido");
        }
        if (Ocupados.contains(entregador)) {
            throw new IllegalArgumentException("Entregador ainda em entrega");
        }
        //Criar a entrega
        int id = k++;
        Entrega e = new Entrega(id, p, entregador, destino);
        entregas.put(id, e);
        PedidoEntrega.put(pedido, id);
        p.setStatus("entregando");
        Ocupados.add(entregador);
        return id;
    }


    public String getEntrega(int id, String atr) throws Invalido{
        Entrega z=entregas.get(id);
        if(z==null){
            throw new IllegalArgumentException("Nao existe entrega com esse id");
        }
        if(atr==null|| atr.isBlank()){
            throw new Invalido("Atributo");
        }
        //Fazendo o Get dos Valores de coisa
        if (atr.equalsIgnoreCase("cliente")) {
            return z.getCliente(); //String
        }
        if (atr.equalsIgnoreCase("empresa")) {
            return z.getEmpresa(); //String
        }
        if (atr.equalsIgnoreCase("pedido")) {
            return String.valueOf(z.getIdPedido()); //Id
        }
        if (atr.equalsIgnoreCase("entregador")) {
            Usuario boy=users.get(z.getIdEntregador());
            return boy.getNome();  //Tipo Usuario
        }
        if (atr.equalsIgnoreCase("destino")) {
            String dest = z.getDestino();
            if (dest != null) return dest;
            Pedido p = orders.get(z.getIdPedido());
            if (p != null) {
                Usuario cliente = users.get(p.getIdcliente());
                if (cliente != null) return cliente.getEndereco();
            }
        }
        if (atr.equalsIgnoreCase("produtos")) {
            return "{[" + String.join(", ", z.getProduto()) + "]}";
        }
        throw new IllegalArgumentException("Atributo nao existe");
    }

    public int getIdEntrega(int pedido){
        Integer id=PedidoEntrega.get(pedido);
        if(id==null){
            throw new IllegalArgumentException("Nao existe entrega com esse id");
        }
        return id;
    }

    public int obterPedido(int entregador){
        Usuario x=users.get(entregador);
        if (!(x instanceof Entregador)) {
            throw new IllegalArgumentException("Usuario nao e um entregador");
        }
        List<Integer> empresas = porEntregador.get(entregador);
        if (empresas==null || empresas.isEmpty()) {
            throw new IllegalArgumentException("Entregador nao estar em nenhuma empresa.");
        }
        List<Pedido> farmacia = new ArrayList<>();
        List<Pedido> normal = new ArrayList<>();
        //Criar lista de Empresas para procurar pelo pedido requisitado no loop
        for (Pedido z : orders.values()) {
            if(!z.getEstado().equals("pronto")) continue;
            int empId=z.getIdempresa();
            if(!empresas.contains(empId)) continue;
            if (lugar.get(empId) instanceof Farmacia){
                farmacia.add(z);
            }
            else{
                normal.add(z);
            }
        }
        if(!farmacia.isEmpty()){
            return farmacia.get(0).getNumero();
        }
        if(!normal.isEmpty()){
            return normal.get(0).getNumero();
        }
        throw new IllegalArgumentException("Nao existe pedido para entrega");
    }

    public void liberarPedido(int numero) throws NaoEncontrado{
        Pedido z=orders.get(numero);
        if(z==null){
            throw new NaoEncontrado(2);
        }
        if ("pronto".equalsIgnoreCase(z.getEstado())) {
            throw new IllegalArgumentException("Pedido ja liberado");
        }
        if (!"preparando".equalsIgnoreCase(z.getEstado())) {
            throw new IllegalArgumentException("Nao e possivel liberar um produto que nao esta sendo preparado");
        }
        z.setStatus("pronto");
    }

    public void entregar(int entrega){
        Entrega x=entregas.get(entrega);
        if(x==null){
            throw new IllegalArgumentException("Nao existe nada para ser entregue com esse id");
        }
        Pedido y=orders.get(x.getIdPedido());
        if(y!=null){
            y.setStatus("entregue");
        }
        Ocupados.remove(x.getIdEntregador());
        PedidoEntrega.remove(x.getIdEntregador());
    }
}