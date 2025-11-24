package MyFood.Models;

import MyFood.Models.*;
import java.util.*;

public class Entrega {
    int id;
    String cliente;
    String empresa;
    int pedido;
    int entregador;
    String destino;
    List<String> produtos;

    public Entrega(int id, Pedido pedido, int deliver, String goal){
        this.id=id;
        this.cliente=pedido.getCliente();
        this.empresa=pedido.getEmpresa();
        this.pedido=pedido.getNumero();
        this.destino=goal;
        this.entregador=deliver;
        this.produtos=new ArrayList<>();
        for(Produto x : pedido.getProduto()){
            this.produtos.add(x.getNome());
        }
    }

    //Métodos Get
    public int getId(){ return this.id;}
    public String getCliente(){ return this.cliente;}
    public String getEmpresa(){ return this.empresa;}
    public int getIdPedido(){ return this.pedido;}
    public int getIdEntregador(){ return this.entregador;}
    public String getDestino(){ return this.destino;}
    public List<String> getProduto(){ return this.produtos;}
}
