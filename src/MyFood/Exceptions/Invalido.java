package MyFood.Exception;

public class Invalido extends Exception{
    public Invalido(String atr){
        super(mensagem(atr));



    public static String mensagem(String atr){
        if(atr.equalsIgnoreCase("nome")){
            return "Nome invalido";
        }
        else if(atr.equalsIgnoreCase("email")){
            return "Email invalido";
        }
        else if(atr.equalsIgnoreCase("endereço")){
            return "Endereco invalido";
        }
        else if(atr.equalsIgnoreCase("senha")){
            return "Senha invalido";
        }
        else if(atr.equalsIgnoreCase("cpf")){
            return "CPF invalido";
        }
        return null;
    }
}
