package MyFood.Models;

import MyFood.Exceptions.*;

//Classe Principal dos Usuarios
public class Dono extends Usuario {
    int id;
    String nome;
    String email;
    String senha;
    String endereco;
    String cpf;

    //Construtor de Classe
    public Usuario(int us, String name, String mail, String pass, String adress){
        super(us,name, mail,pass);
        this.endereco=adress;
    }

    //Métodos para GetAtributo
    public String getEndereco() {
        return this.endereco;
    }
    public String getCpf() {
        return this.cpf;
    }

}