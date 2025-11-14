package MyFood.Models;

import MyFood.Exceptions.*;

//Classe Principal dos Usuarios
public class Cliente extends Usuario {
    int id;
    String nome;
    String email;
    String senha;
    String endereco;

    //Construtor de Classe
    public Usuario(int us, String name, String mail, String pass, String adress){
        super(us,name, mail,pass);
        this.endereco=adress;
    }

    //Métodos para GetAtributo
    public String getEndereco() {
        return this.endereco;
    }

}