package MyFood.Models;

import MyFood.Exceptions.*;

//Classe Principal dos Usuarios
public class Usuario {
    int id;
    String nome;
    String email;
    String senha;

    //Construtor de Classe
    public Usuario(int us, String name, String mail, String pass){
        this.id=us;
        this.email=mail;
        this.nome=name;
        this.senha=pass;
    }

    //Métodos para GetAtributo
    public String getNome() {
        return this.nome;
    }
    public String getMail() {
        return this.email;
    }
    //public String getEndereco() {
        //return this.nome;
    //}
    //public String getCpf() {
        //return this.nome;
    //}

}