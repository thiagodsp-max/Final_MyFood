package MyFood;

import MyFood.Exceptions.Invalido;
import MyFood.Exceptions.NaoCadastrado;
import MyFood.Exceptions.NaoEncontrado;
import MyFood.Models.Dono;
import MyFood.Models.Empresa;
import MyFood.Models.Usuario;

import java.util.Map;

public class Filtro {
    /*Nessa classe ficará SOMENTE métodos que lidem com a lógica de validação e que são
    recorrentes, para otimizar a Facade e garantir o direcionamento a Exception correta*/
    private Map<Integer, Usuario> users;
    private Map<Integer, Empresa> lugar;

    public Filtro(Map<Integer, Usuario> users, Map<Integer, Empresa> cozinha) {
        this.users = users;
        this.lugar=cozinha;
    }

    //Funções Complementares - Email
    private boolean emailValido(String email) {
        if (email == null) return false;
        email = email.trim();
        return email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    }
    //Funções Complementares - Horarios do Mercado
    boolean formatohora(String time){
        return time.matches("^[0-9]{2}:[0-9]{2}");
        //validar se Horas está entre 0 e 23 e Minutos entre 0 e 59
        //É importante que a hora de abrir seja menor que a hora de fechar
    }
    boolean valorhora(String time){
        if(time==null) return false;
        try{
            //Definir Hora e Minuto como partes da mesma string
            int hour=Integer.parseInt(time.substring(0,2));
            int min=Integer.parseInt(time.substring(3,5));
            return  (hour>=0 && hour<24) && (min >=0 && min <=59);
        } catch(Exception e){
            return false;
        }
    }

    //Métodos de Filtragem

    //Validação dos Parâmetros do Construtor
    protected void validauser(String nome, String email, String senha,String ender)
            throws Invalido{
        //Validação de todos os valores de Cliente
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
        //Verifica se já existe algum email igual ao do Parâmetro
        for(Usuario x: users.values()){
            if(x.getMail().equalsIgnoreCase(email)){
                throw new IllegalArgumentException("Conta com esse email ja existe");
            }
        }
    }
    protected void validadono(String nome, String email, String senha,String ender, String cpf)
            throws Invalido{
        //Validação Única para Usuários do Tipo Dono
        if (cpf == null || cpf.isBlank()||cpf.length()!=14) {
            throw new Invalido("CPF");
        }
        //Chama a Validação de Cliente para complementar o restante dos valores
        validauser(nome,email,senha,ender);
    }

    protected void validaempresa(Usuario chefe, String nome, String ender) throws Invalido, NaoCadastrado {
        if(chefe==null){
            throw new NaoCadastrado(0);
        }
        if(nome == null||nome.isBlank()){
            throw new Invalido("Nome");
        }
        if(ender == null||ender.isBlank()){
            throw new Invalido("Endereco da empresa");
        }
    }

    //Validação do Construtor de Produto
    void checkproduct(String nome, float valor, String cat) throws Invalido{
        if(nome == null||nome.isBlank()){
            throw new Invalido("Nome");
        }
        if(valor <0){
            throw new Invalido("Valor");
        }
        if(cat == null||cat.isBlank()){
            throw new Invalido("Categoria");
        }
    }

    //O Usuario é autorizado a criar tal Entidade??
    void permitido(Usuario w){
        //Somente Donos podem criar Restaurantes Mercados e Farmacias
        if(w==null || !(w instanceof Dono)){
            throw new IllegalArgumentException("Usuario nao pode criar uma empresa");
        }
    }

    //Simplificando as Regras de criar Empresas
    public void cadastroempresa(int dono, String nome, String endereco){
        for(Empresa r:lugar.values()){
            if(r.getNome().equalsIgnoreCase(nome)){
                if(r.getDono().getId()==dono && r.getEnder().equalsIgnoreCase(endereco)){
                    throw new IllegalArgumentException("Proibido cadastrar duas empresas com o mesmo nome e local");
                }
                else if(r.getDono().getId()!=dono){
                    throw new IllegalArgumentException("Empresa com esse nome ja existe");
                }
            }
        }
    }
    //Validação de Horário
    public void periodo(String abre, String fecha) throws Invalido{
        if(abre==null||fecha==null){
            throw new Invalido("Horario");
        }
        if(!formatohora(abre) || !formatohora(fecha)){
            throw new Invalido("Formato de hora");
        }
        if(!valorhora(abre) || !valorhora(fecha) || abre.compareTo(fecha)>0){
            throw new Invalido("Horario");
        }
    }
}