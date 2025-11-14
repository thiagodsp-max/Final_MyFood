package MyFood.Exception;

public class NaoEncontrado extends Exception{
    public NaoEncontrado(int sign) {
        super(caso(sign));
    }
    public static String caso(int atr){
        if(atr==0){
            return "Produto nao encontrado";
        }
        else if(atr==1){
            return "Empresa nao encontrada";
        }
        else if(atr==2){
            return "Pedido nao encontrado";
        }
    }
}
