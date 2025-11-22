package MyFood.Models;

import MyFood.Exceptions.*;

public class Restaurante extends Empresa{
    String tipoCozinha;

    //Agora como subclasse de Empresa, Restaurante ficou muito mais simples
    public Restaurante(int ru, String name, String ender, String type, Usuario user){
        super(ru,name,ender,user);
        this.tipoCozinha=type;
        this.dono=user;
    }

    public String getTipo(){
        return this.tipoCozinha;
    }

}