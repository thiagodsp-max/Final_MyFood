package MyFood.Models;

public abstract class Empresa {
    int id;
    String nome;
    String endereco;
    Usuario dono;

    //Guarda Todos os
    public Empresa(int emp, String name, String mail, Usuario user){
        this.id=emp;
        this.nome=name;
        this.endereco=mail;
        this.dono=user;
    }
    public String getNome(){
        return this.nome;
    }
    public String getEnder(){
        return this.endereco;
    }
    public int getId() {
        return this.id;
    }
    public Usuario getDono(){
        return this.dono;
    }

    //Este método sofre Override de suas Subclasses
    public abstract String getTipo();
}
