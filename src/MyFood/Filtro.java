package MyFood;

import MyFood.Exceptions.Invalido;
import MyFood.Exceptions.NaoCadastrado;
import MyFood.Models.Restaurante;
import MyFood.Models.Usuario;

import java.util.Map;

public class Filtro {
    private Map<Integer, Usuario> users;
    private Map<Integer, Restaurante> lugar;
    //private Map<Integer, Produto> prod = new LinkedHashMap<>();
    //private Map<Integer, Pedido> pedidos = new LinkedHashMap<>();

    public Filtro(Map<Integer, Usuario> users, Map<Integer,Restaurante> cozinha) {
        this.users = users;
        this.lugar=cozinha;
    }

    //Métodos de Filtragem
    /*
    public int presente(int emp) throws Invalido,NaoCadastrado {
        //Checar se o User, Restaurante, etc. Existe dentro do sistema!!
        int id;
        try { //Precisamos checar se o emp enviado de fato existe, ou é inválido
            //id = Integer.parseInt(String.valueOf(emp));
        } catch (NumberFormatException e) {
            if (emp.isBlank()) throw new NaoCadastrado("emp");
            else throw new IllegalArgumentException();
        }
        return id;
    }
     */

    //Funções Complementares
    private boolean emailValido(String email) {
        if (email == null) return false;
        email = email.trim();
        return email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    }
    private boolean cpfValido(String cpf) {
        if (cpf == null) return false;
        cpf = cpf.trim();
        return cpf.length()==14;
    }


    //Verificar se os valores são válidos ou não
    protected void validauser(String nome, String email, String senha,String ender)
            throws Invalido{
        if (nome==null || nome.isBlank()) {
            throw new Invalido("Nome");
        }
        if (senha==null||senha.isBlank()) {
            throw new Invalido("Senha");
        }
        if (ender==null||ender.isBlank()) {
            throw new Invalido("Endereco");
        }
        if (email==null||email.isBlank()||!emailValido(email)) {
            throw new Invalido("Email");
        }
        for(Usuario x: users.values()){
            if(x.getMail().equalsIgnoreCase(email)){
                throw new IllegalArgumentException("Conta com esse email ja existe");
            }
        }
    }
    protected void validadono(String nome, String email, String senha,String ender, String cpf)
            throws Invalido{
        if (cpf == null || cpf.isBlank()||cpf.length()!=14) {
            throw new Invalido("CPF");
        }
        validauser(nome,email,senha,ender);
    }
    void checkproduct(String nome, float valor, String cat) throws Invalido{
        if(nome == null||nome.isBlank()){
            throw new Invalido("Nome");
        }
        if(valor <0){
            throw new Invalido("Valor");
        }
        if(cat == null||cat.isBlank()){
            throw new Invalido("Categoria");
        };
    }
    //
    //
}