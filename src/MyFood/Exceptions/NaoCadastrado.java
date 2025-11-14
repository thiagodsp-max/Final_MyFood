package MyFood.Exception;

public class NaoCadastrado extends Exception{
    public NaoCadastrado(int sign) {
        super(caso(sign));
    }
    public static String caso(int atr){
            if(atr==0){
                return "Usuario nao cadastrado.";
            }
            else if(atr==1){
                return "Empresa nao cadastrada";
            }
            else if(atr==2){
                return "Produto nao cadastrado";
            }
        }
    }
