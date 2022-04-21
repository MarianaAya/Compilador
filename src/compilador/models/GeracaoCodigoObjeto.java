
package compilador.models;

import java.util.ArrayList;
import java.util.List;


public class GeracaoCodigoObjeto {
       
    private List<Tripla> lista;
    private List<Registrador> registradores;
    private int indexReg;
    private int enderecoAux;
    private List<Endereco> enderecos;
    public void gerar() {
        indexReg = 0;
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
                   Singleton.addComando(new ComandoMaquina("load",registradores.get(posRegistrador).getRegistrador(),"["+enderecos.get(posEndereco).getEndereco()+"]"));
                   
                }
                else {
                    Singleton.addComando(new ComandoMaquina("load",registradores.get(posRegistrador).getRegistrador(),lista.get(pos).getOperando2()));
                }
            }
            if(SinalOperacao(lista.get(pos).getOperador())) {
                enderecos.add(new Endereco(""+enderecoAux,"("+lista.get(pos).getCodigo()+")"));
                
                Singleton.addComando(new ComandoMaquina(""));
                enderecoAux--;
            }
            pos++;
        }
    }
    
    public boolean SinalOperacao(String sinal) {
        if(sinal.equals("+") || sinal.equals("-") || sinal.equals("*") || sinal.equals("/"))
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
