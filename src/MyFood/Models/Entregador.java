package MyFood.Models;

public class Entregador extends Usuario{
    private String endereco;
    private String veiculo;
    private String placa;

    public Entregador(int us, String name, String mail, String pass, String adress, String moto, String placa){
        super(us,name, mail,pass);
        this.endereco=adress;
        this.veiculo=moto;
        this.placa=placa;
    }

    //Métodos para GetAtributo
    public String getEndereco() {
        return this.endereco;
    }
    //Métodos para GetAtributo
    public String getVeiculo() {
        return this.veiculo;
    }
    public String getPlaca() {
        return this.placa;
    }
    //public boolean isEmEntrega(){
        //return false;
    //}
}
