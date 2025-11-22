package MyFood.Models;

public class Farmacia extends Empresa{
    boolean abre24;
    int funcionarios;

    public Farmacia(int id, String name, String ender, boolean abre24, int funcionarios, Usuario dono){
        super(id,name,ender,dono);
        this.abre24=abre24;
        this.funcionarios=funcionarios;
    }

    public boolean isaberto24(){
        return abre24;
    }
    public int getFuncionarios(){
        return this.funcionarios;
    }

    public String getTipo(){
        //Farmácia tem somente um tipo fixo
        return "farmacia";
    }
}
