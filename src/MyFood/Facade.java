package MyFood;

import MyFood.Exceptions.*;
import MyFood.Models.*;
import java.util.*;
import java.time.*;

public class Facade {
    private Map<Integer, Usuario> users = new LinkedHashMap<>();
    private int k1=0;
    private int k2=0;
    private int k3=0;
    private int k4=0;//Gerador de ids únicos
    Filtro logico = new Filtro(users);

    public void zerarSistema(){
        users.clear();
    }
    public void encerrarSistema(){}

    public void criarUsuario(String name, String email, String senha, String ender){
        //logico.validar(name,email,senha,ender,NULL);
        int id=k1++;
        //Construtor do Cliente
        Usuario neo = new Usuario(id,name,email,senha,ender);
        users.put(id,neo);
        //Sem retorno
    }
    public void criarUsuario(String name, String email, String senha, String ender, String cpf){
        //logico.validar(name,email,senha,ender,cpf);
        int id=k1++;
        //Construtor do Dono
        Usuario neo = new Usuario(id,name,email,senha,ender,cpf);
        users.put(id,neo);
        // Sem retorno
    }

    public String getAtributoUsuario(int id, String atr){
        //Checar se o Id digitado é válido ou não
        try { //Precisamos checar se o emp enviado de fato existe, ou é inválido
            id = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            if (id.isBlank()) {
                //throw new ValorNuloException("emp");
            } else //throw new EmpregadoNaoExisteException();
        }
        //Procurar pelo Id do Usuário em questão
        Usuario dude = users.get(id);
        if(dude == null){
            //Exception
            //throw new ();
        }
        //Filtrar se existe ou não o Atributo
        if(atr=="nome"){
            return dude.getNome();
        }
        else if(atr=="email"){
            return dude.getMail();
        }
    }

    public login(String email, String pass){
        //Checar se o email existe
        for(email == ){
            ;
        }
        //Checar se a senha é a que consta

        //Retorna o id desse user
    }

    //public criarEmpresa(String tipoEmpresa, int dono, String nome, String endereco, String tipoCozinha){}
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