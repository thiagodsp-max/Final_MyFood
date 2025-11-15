package MyFood.Models;

import MyFood.Exceptions.*;

public class Restaurante {
    int id;
    String nome;
    String endereco;
    String tipoCozinha;
    Usuario dono;

    public Restaurante(int ru, String name, String ender, String type, Usuario user){
        this.id=ru;
        this.endereco=ender;
        this.nome=name;
        this.tipoCozinha=type;
        this.dono=user;
    }

    public String getNome(){
        return this.nome;
    }
    public String getEnder(){
        return this.endereco;
    }
    public String getTipo(){
        return this.tipoCozinha;
    }
    public int getId() {
        return this.id;
    }
    public Usuario getDono(){
        return this.dono;
    }
}