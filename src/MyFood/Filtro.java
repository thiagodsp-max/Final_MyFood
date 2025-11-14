package MyFood;

import MyFood.Exceptions.Invalido;
import MyFood.Models.*;
import java.util.*;

public class Filter {
    private Map<Integer, Usuario> users;
    //private Map<Integer, Restaurante> lugar = new LinkedHashMap<>();
    //private Map<Integer, Produto> prod = new LinkedHashMap<>();
    //private Map<Integer, Pedido> pedidos = new LinkedHashMap<>();

    public Filter(Map<Integer, Usuario> users) {
        this.users = users;
    }

    //Métodos de Filtragem
    //Checar se o User, Restaurante, etc. Existe dentro do sistema!!
    public int presente(int emp) throws Invalido,NaoCadastrado {
        int id;
        try { //Precisamos checar se o emp enviado de fato existe, ou é inválido
            //id = Integer.parseInt(emp);
        } catch (NumberFormatException e) {
            if (emp.isBlank()) {
                throw new NaoCadastrado("emp");
            } else throw new IllegalArgumentException();
        }
        return id;
    }

    //Verificar se os valores são válidos ou não
    protected validauser(String nome, String email, String senha,String ender, String cpf)
            throws Invalido{
        if (nome.isBlank()) {
            throw new Invalido("Nome");
        }
        if (email.isBlank()) {
            throw new Invalido("Email");
        }
        else{
            //Procurar pelo email em questão na lista
            //Se já existir esse email, então throw Exception
        }
        if (senha.isBlank()) {
            throw new Invalido("Senha");
        }
        if (ender.isBlank()) {
            throw new Invalido("Endereco");
        }
        if (cpf != null) {
            if (cpf.isBlank()) {
                throw new Invalido("CPF");
            }
        }
    }

    //protected valida

    //S
    //
}