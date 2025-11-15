package MyFood;

import MyFood.Exceptions.NaoCadastrado;
import MyFood.Exceptions.Invalido;
import MyFood.Models.*;
import java.util.*;
import java.time.*;

public class Facade {
    private Map<Integer, Usuario> users = new LinkedHashMap<>();
    private Map<Integer, Restaurante> lugar = new LinkedHashMap<>();
    //private Map<Integer, Produto> prod = new LinkedHashMap<>();
    //private Map<Integer, Pedido> pedidos = new LinkedHashMap<>();
    private int k1=0;
    private int k2=0;
    private int k3=0;
    private int k4=0;//Gerador de ids únicos
    Filtro logico = new Filtro(users);

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

    //public criarEmpresa(String tipoEmpresa, int dono, String nome, String endereco, String tipoCozinha){
        //Achar a pessoa do Id
        //;
    //}
    //public getEmpresasDoUsuario(int idDono){}
    //public getIdEmpresa(int idDono, String nome, int index){}
    //public getAtributoEmpresa(int emp, String atr){}

    //public criarProduto(int emp, String nome, float valor, String cat){}
    //public editarProduto(int prod, String nome, float valor, String cat){}
    //public getProduto(String nome, int emp, String atr){}
    //public listarProdutos(int emp){}

    //public criarPedido(int client, int emp){}
    //public getNumeroPedido(int client, int emp, int index){}
    //public adicionarProduto(int num, int prod){}
    //public getPedidos(int num, String atr){}
    //public fecharPedido(int num){}
    //public removerProduto(int pedido, String prod){}

}