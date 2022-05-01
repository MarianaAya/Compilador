
package compilador.models;

import java.util.ArrayList;
import java.util.List;


public class GeracaoCodigoObjeto {
    private int auxCodComando = 0;
    private List<Tripla> lista;
    private List<Registrador> registradores;
    private int indexReg;
    private int enderecoAux;
    private List<Endereco> enderecos;
    public void gerar() {
        indexReg = 1;
        lista = Singleton.getTriplasOtimizadas();
        registradores = new ArrayList<>();
        enderecoAux=255;
        enderecos= new ArrayList<>();
        inicializarRegistradores();
        construirCodigo();
    }
 
    public void construirCodigo() {
        int pos = 0;
        int posRegistrador;
        while(pos<lista.size()) {
            if(lista.get(pos).getOperador().equals("=")) {
                posRegistrador = getPosVariavel(lista.get(pos).getOperando1());
                if(posRegistrador==-1) { //não tem um registrador associado a variavel
                    posRegistrador = indexReg;
                    registradores.get(indexReg).setVariavel(lista.get(pos).getOperando1());
                    indexReg++;
                }
                if(lista.get(pos).getOperando2().charAt(0)=='(') {
                   int posEndereco = getPosEndereco(lista.get(pos).getOperando2());
                   Singleton.addComando(new ComandoMaquina("a"+lista.get(pos).getCodigo(),"load",registradores.get(posRegistrador).getRegistrador(),"["+enderecos.get(posEndereco).getEndereco()+"]"));
                   
                }
                else {
                    if(lista.get(pos).getOperando2().charAt(0)>=48 && lista.get(pos).getOperando2().charAt(0)<=57){//se for um número
                        Singleton.addComando(new ComandoMaquina("a"+lista.get(pos).getCodigo(),"load",registradores.get(posRegistrador).getRegistrador(),lista.get(pos).getOperando2()));
                    }
                    else {
                        int posRegistrador2 = getPosVariavel(lista.get(pos).getOperando2());
                        Singleton.addComando(new ComandoMaquina("a"+lista.get(pos).getCodigo(),"move",registradores.get(posRegistrador).getRegistrador(),registradores.get(posRegistrador2).getRegistrador()));
                    }
                    
                }
            }
            if(SinalOperacao(lista.get(pos).getOperador())) {
                if(lista.get(pos).getOperador().equals("*")) {
                    Multiplicacao(pos);
                }
                if(lista.get(pos).getOperador().equals("/")) {
                    Divisao(pos);
                }
                
                if(lista.get(pos).getOperador().equals("+") || lista.get(pos).getOperador().equals("-")) {
                    SomaSubtracao(pos);
                }
                
            }
            
            if(isJMP(lista.get(pos).getOperador())) {
                Singleton.addComando(new ComandoMaquina("a"+lista.get(pos).getCodigo(),"jmp","a"+lista.get(pos).getOperando1()));
            }
            
            if(SinalComparacao(lista.get(pos).getOperador())) {
               
                Comparacao(pos);
                pos++; //porque para onde deve ir está no próximo comando
            }
            
            pos++;
        }
        Singleton.addComando(new ComandoMaquina("a"+(lista.get(lista.size()-1).getCodigo()+1),"halt"));
    }
    
    public void Multiplicacao(int pos) {
        
        int posRegistrador=0;
        int qtde = 0;
        String reg1="",reg2="";
        Singleton.addComando(new ComandoMaquina("a"+lista.get(pos).getCodigo(),"load","R0","0"));
        //vou careegar os termos em outros dois registradores
        //primeiro termo
        if(lista.get(pos).getOperando1().charAt(0)=='(') {
            int posEndereco = getPosEndereco(lista.get(pos).getOperando1());
            Singleton.addComando(new ComandoMaquina("","load","RE","["+enderecos.get(posEndereco).getEndereco()+"]"));

        }
        else {
            
            if(lista.get(pos).getOperando1().charAt(0)>=48 && lista.get(pos).getOperando1().charAt(0)<=57){//se for um número
               Singleton.addComando(new ComandoMaquina("","load","RE",lista.get(pos).getOperando1()));
            }
            else {
                System.out.println("entrei");
                posRegistrador = getPosVariavel(lista.get(pos).getOperando1());
                Singleton.addComando(new ComandoMaquina("","move","RE",registradores.get(posRegistrador).getRegistrador()));
            }

        }

        //segundo termo
        if(lista.get(pos).getOperando2().charAt(0)=='(') {
            int posEndereco = getPosEndereco(lista.get(pos).getOperando2());
            Singleton.addComando(new ComandoMaquina("","load","RF","["+enderecos.get(posEndereco).getEndereco()+"]"));

        }
        else {

            if(lista.get(pos).getOperando2().charAt(0)>=48 && lista.get(pos).getOperando2().charAt(0)<=57){//se for um número
               Singleton.addComando(new ComandoMaquina("","load","RF",lista.get(pos).getOperando2()));
            }
            else {
                posRegistrador = getPosVariavel(lista.get(pos).getOperando2());
                Singleton.addComando(new ComandoMaquina("","move","RF",registradores.get(posRegistrador).getRegistrador()));
            }

        }
     
        //inicializando
        Singleton.addComando(new ComandoMaquina("","move","RC","RE"));
        Singleton.addComando(new ComandoMaquina("","load","RD","-1"));     
        Singleton.addComando(new ComandoMaquina("","store","RE","["+enderecoAux+"]"));
        
        //iteração
        Singleton.addComando(new ComandoMaquina("mult"+auxCodComando,"addi","RF","RF","RD")); //estou decrementando o contador
        Singleton.addComando(new ComandoMaquina("","jmpEQ","RF=R0","a"+(lista.get(pos).getCodigo()+1)));//vejo se o contador é igual a zero
        Singleton.addComando(new ComandoMaquina("","addi","RE","RE","RC")); //estou somando
        Singleton.addComando(new ComandoMaquina("","store","RE","["+enderecoAux+"]"));
        Singleton.addComando(new ComandoMaquina("","jmp","mult"+auxCodComando));
        
        enderecos.add(new Endereco(""+enderecoAux,"("+lista.get(pos).getCodigo()+")"));
        enderecoAux--;
        auxCodComando++;
      
    }
    
    public void Divisao(int pos) {
        int posRegistrador=0;
        int qtde = 0;
        String reg1="",reg2="";
      
        //vou careegar os termos em outros dois registradores
        //primeiro termo
        if(lista.get(pos).getOperando1().charAt(0)>=48 && lista.get(pos).getOperando1().charAt(0)<=57){//se for um número
            Singleton.addComando(new ComandoMaquina("","load","RE",lista.get(pos).getOperando1()));
        }
        else {
            posRegistrador = getPosVariavel(lista.get(pos).getOperando1());
            Singleton.addComando(new ComandoMaquina("","move","RE",registradores.get(posRegistrador).getRegistrador()));
        }
        //segundo termo
        if(lista.get(pos).getOperando2().charAt(0)>=48 && lista.get(pos).getOperando2().charAt(0)<=57){//se for um número
            Singleton.addComando(new ComandoMaquina("","load","RF",lista.get(pos).getOperando2()));
        }
        else {
            posRegistrador = getPosVariavel(lista.get(pos).getOperando2());
            Singleton.addComando(new ComandoMaquina("","move","RF",registradores.get(posRegistrador).getRegistrador()));
        }
     
        //inicializando
    
    
        enderecoAux++;
        auxCodComando++;
    
    }
    
    public void Comparacao(int pos) {
        String reg1="",reg2="R0";
        int posRegistrador;
        String sinal = lista.get(pos).getOperador();
        
        //primeiro cologo os valores em registradores
        //primeiro termo
        if(lista.get(pos).getOperando1().charAt(0)>=48 && lista.get(pos).getOperando1().charAt(0)<=57){//se for um número
            Singleton.addComando(new ComandoMaquina("a"+lista.get(pos).getCodigo(),"load","RF",lista.get(pos).getOperando1()));
        }
        else {
            posRegistrador = getPosVariavel(lista.get(pos).getOperando1());
            reg1 = registradores.get(posRegistrador).getRegistrador();
        }
        //segundo termo
        if(lista.get(pos).getOperando2().charAt(0)>=48 && lista.get(pos).getOperando2().charAt(0)<=57){//se for um número
            Singleton.addComando(new ComandoMaquina("a"+lista.get(pos).getCodigo(),"load","R0",lista.get(pos).getOperando2()));
        }
        else {
            posRegistrador = getPosVariavel(lista.get(pos).getOperando2());
            Singleton.addComando(new ComandoMaquina("","move","R0",registradores.get(posRegistrador).getRegistrador()));
        }
        pos++;
        if(sinal.equals("==")) {
            Singleton.addComando(new ComandoMaquina("","jmpEQ",reg1+"="+reg2,"a"+lista.get(pos).getOperando2()));
            Singleton.addComando(new ComandoMaquina("","jmp","a"+lista.get(pos).getOperando1()));
        }
        if(sinal.equals("!=")) {
            Singleton.addComando(new ComandoMaquina("","jmpEQ",reg1+"="+reg2,"a"+lista.get(pos).getOperando1()));
            Singleton.addComando(new ComandoMaquina("","jmp","a"+lista.get(pos).getOperando2()));
        }
        if(sinal.equals("<=")) {
            Singleton.addComando(new ComandoMaquina("","jmpLE",reg1+"<="+reg2,"a"+lista.get(pos).getOperando2()));
            Singleton.addComando(new ComandoMaquina("","jmp","a"+lista.get(pos).getOperando1()));
        }
        if(sinal.equals(">=")) {
           Singleton.addComando(new ComandoMaquina("","jmpEQ",reg1+"="+reg2,"a"+lista.get(pos).getOperando2()));
           Singleton.addComando(new ComandoMaquina("","jmpLE",reg1+"<="+reg2,"a"+lista.get(pos).getOperando1()));
           Singleton.addComando(new ComandoMaquina("","jmp","a"+lista.get(pos).getOperando2()));
        }
        if(sinal.equals("<")) {
            Singleton.addComando(new ComandoMaquina("","jmpLE",reg1+"<="+reg2,"aux"+auxCodComando));
            Singleton.addComando(new ComandoMaquina("aux"+auxCodComando,"jmpEQ",reg1+"="+reg2,"a"+lista.get(pos).getOperando1()));
            Singleton.addComando(new ComandoMaquina("","jmp","a"+lista.get(pos).getOperando2()));
            auxCodComando++;
        }
        if(sinal.equals(">")) {
            Singleton.addComando(new ComandoMaquina("","jmpLE",reg1+"<="+reg2,"a"+lista.get(pos).getOperando1()));
            Singleton.addComando(new ComandoMaquina("","jmp","a"+lista.get(pos).getOperando2()));
            
        }
    }
    
    public void SomaSubtracao(int pos) {
        int posRegistrador=0;
        String reg1 = "RF";
        String reg2 = "RE";
        boolean rotulou=false;
        String rotulo="";
        //Analisar o operando1
        if(lista.get(pos).getOperando1().charAt(0)=='(') { //se for (2)
            rotulou=true;
           int posEndereco = getPosEndereco(lista.get(pos).getOperando1());
           Singleton.addComando(new ComandoMaquina("a"+lista.get(pos).getCodigo(),"load","RF","["+enderecos.get(posEndereco).getEndereco()+"]"));
        }
        else {
            if(lista.get(pos).getOperando1().charAt(0)>=48 && lista.get(pos).getOperando1().charAt(0)<=57){ //se for um numero
                rotulou=true;
                Singleton.addComando(new ComandoMaquina("a"+lista.get(pos).getCodigo(),"load","RF",lista.get(pos).getOperando1()));
            }
            else {//se for uma variavel, procuro o registrador
                posRegistrador = getPosVariavel(lista.get(pos).getOperando1());
                reg1=registradores.get(posRegistrador).getRegistrador();
            }
        }
        //analisar o operando2
        if(lista.get(pos).getOperando2().charAt(0)=='(') {//se for (2)
           int posEndereco = getPosEndereco(lista.get(pos).getOperando2());
           if(!rotulou)
               rotulo= "a"+lista.get(pos).getCodigo();
           Singleton.addComando(new ComandoMaquina(rotulo,"load","RE","["+enderecos.get(posEndereco).getEndereco()+"]"));
        }
        else {
            if(lista.get(pos).getOperando2().charAt(0)>=48 && lista.get(pos).getOperando2().charAt(0)<=57){ //se for um numero
                if(!rotulou)
                    rotulo= "a"+lista.get(pos).getCodigo();
                Singleton.addComando(new ComandoMaquina(rotulo,"load","RE",lista.get(pos).getOperando2()));
            }
            else {//se for uma variavel, procuro o registrador
                posRegistrador = getPosVariavel(lista.get(pos).getOperando2());

                reg2=registradores.get(posRegistrador).getRegistrador();
            }
        }

        if(lista.get(pos).getOperador().equals("-")) {
            Singleton.addComando(new ComandoMaquina("a"+lista.get(pos).getCodigo(),"load","RD",""+255));
            Singleton.addComando(new ComandoMaquina("","xor",reg2,reg2,"RD"));
            Singleton.addComando(new ComandoMaquina("","load","RD",""+1));
            Singleton.addComando(new ComandoMaquina("","addi",reg2,reg2,"RD"));

        }

        enderecos.add(new Endereco(""+enderecoAux,"("+lista.get(pos).getCodigo()+")"));

        Singleton.addComando(new ComandoMaquina("","addi","RD",reg1,reg2));
        Singleton.addComando(new ComandoMaquina("","store","RD","["+enderecoAux+"]"));

        enderecoAux--;
    }
    
    public boolean SinalComparacao(String sinal) {
        if(sinal.equals("<=") || sinal.equals(">=") || sinal.equals(">") || sinal.equals("<") || sinal.equals("==") || sinal.equals("!="))
            return true;
        return false;
    
    }
    
    public boolean SinalOperacao(String sinal) {
        if(sinal.equals("+") || sinal.equals("-") || sinal.equals("*") || sinal.equals("/"))
            return true;
        return false;
    }
    
    public boolean isJMP(String simbolo) {
        if(simbolo.equals("for") || simbolo.equals("if") || simbolo.equals("while") || simbolo.equals("goto"))
            return true;
        return false;
    }
    
    public int getPosEndereco(String variavel) {
        int pos = 0;
        while(pos<enderecos.size() && !enderecos.get(pos).getCodigo().equals(variavel)) {
            pos++;
        }
        
        if(pos == registradores.size())
            return -1;
        return pos;
    }
    
    public int getPosVariavel(String variavel) {
        int pos = 0;
        while(pos<registradores.size() && !registradores.get(pos).getVariavel().equals(variavel)) {
            pos++;
        }
        
        if(pos == registradores.size())
            return -1;
        return pos;
    }
    
    
    
    public void inicializarRegistradores() {
        registradores.add(new Registrador("R0", ""));
        registradores.add(new Registrador("R1", ""));
        registradores.add(new Registrador("R2", ""));
        registradores.add(new Registrador("R3", ""));
        registradores.add(new Registrador("R4", ""));
        registradores.add(new Registrador("R5", ""));
        registradores.add(new Registrador("R6", ""));
        registradores.add(new Registrador("R7", ""));
        registradores.add(new Registrador("R8", ""));
        registradores.add(new Registrador("R9", ""));
        registradores.add(new Registrador("RA", ""));
        registradores.add(new Registrador("RB", ""));
        registradores.add(new Registrador("RC", ""));
        registradores.add(new Registrador("RD", ""));
        registradores.add(new Registrador("RE", ""));
        registradores.add(new Registrador("RF", ""));
    }
}
