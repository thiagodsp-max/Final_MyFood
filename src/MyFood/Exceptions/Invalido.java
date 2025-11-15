package MyFood.Exceptions;

public class Invalido extends Exception {
    public Invalido(String atr) {
        super(atr + " invalido");
    }
}