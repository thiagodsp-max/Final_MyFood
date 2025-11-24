package MyFood.Models;

import MyFood.Exceptions.*;
import java.util.*;

public class Pedido {
    int numero;
    int idcliente;
    int idempresa;
    String cliente;
    String empresa;
    String status; //Aberto ou fechado
    //Lista de Produtos adicionados
    private List<Produto> buyed;

    public Pedido(int num, Usuario cliente,Empresa empresa){
        this.numero=num;
        this.idcliente=cliente.getId();
        this.idempresa=empresa.getId();
        this.cliente=cliente.getNome();
        this.empresa=empresa.getNome();
        this.status="aberto";
        this.buyed=new ArrayList<>();
    }

    public int getNumero(){
        return this.numero;
    }
    public int getIdcliente(){
        return this.idcliente;
    }
    public int getIdempresa(){
        return this.idempresa;
    }
    public String getCliente(){
        return this.cliente;
    }
    public String getEmpresa(){
        return this.empresa;
    }
    public String getEstado(){
        return this.status;
    }
    public List<Produto> getProduto(){
        return this.buyed;
    }
    public void close(){
        this.status="preparando";
    }
    public float calcular(){
        float total=0;
        for(Produto z: buyed){
            total+=z.getValor();
        }
        return total;
    }
    //Novo Método de Status para as Entregas
    public void setStatus(String estado){
        this.status=estado;
    }

}