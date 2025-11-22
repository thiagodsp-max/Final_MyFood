package MyFood.Models;

public class Mercado extends Empresa{
    String tipoMercado;
    String aberto;
    String fechado;

    public Mercado(int ru, String name, String ender, String type, Usuario user, String open, String close){
        super(ru,name,ender,user);
        this.tipoMercado=type;
        this.dono=user;
        this.aberto=open;
        this.fechado=close;
    }
    //Retornar os valores únicos de Mercado
    public String getTipo(){
        return this.tipoMercado;
        /*Há 3 tipos de Mercado que estão definidos
         nas validações dos comandos Facade*/
    }
    public String getOpen(){
        return this.aberto;
    }
    public String getClose(){
        return this.fechado;
    }
    //Alterar/Definir a Hora de abrir e de fechar
    public void setOpen(String abrir){
        this.aberto=abrir;
    }
    public void setClose(String fechar){
        this.fechado=fechar;
    }

}
