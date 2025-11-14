package MyFood.Models;

import MyFood.Exceptions.*;

public class Restaurante {
    int id;
    String nome;
    String endereco;
    String tipoCozinha;

    public Restaurante(int ru, String name, String ender, String type){
        this.id=ru;
        this.endereco=ender;
        this.nome=name;
        this.tipoCozinha=type;
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
}