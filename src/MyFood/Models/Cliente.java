package MyFood.Models;

//Classe Principal dos Usuarios
public class Cliente extends Usuario {
    private String endereco;

    //Construtor de Classe
    public Cliente(int us, String name, String mail, String pass, String adress){
        super(us,name, mail,pass);
        this.endereco=adress;
    }

    //Métodos para GetAtributo
    public String getEndereco() {
        return this.endereco;
    }

}
