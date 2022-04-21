/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador.models;

import java.util.List;

/**
 *
 * @author USER
 */
public class GeracaoCodigoOtimizado {
    private List<Tripla> lista;
    public void otimizar() {
        lista = Singleton.getTriplas();
        Singleton.removeAllTriplasOtimizadas();
        percorrerLista();
    }
    public void percorrerLista() {
        tirarGoProxLinha();
        lista = Singleton.getTriplasOtimizadas();
        
    }
    public void tirarGoProxLinha() {
        int pos = 0;
        int goExcluidos = 0;
        for(int i = 0;i<lista.size();i++) {
            if(comandoGo(lista.get(i).getOperador())) {
                String operando1 = lista.get(i).getOperando1();
                if(i+1<lista.size() && Integer.parseInt(operando1) == lista.get(i+1).getCodigo()) {
                    goExcluidos++;
                    for(int j = 0;j<lista.size();j++) { //percorro vendo se alguém aponta para ele
                        if(comandoGo(lista.get(j).getOperador())) {
                            String aux = lista.get(j).getOperando1();
                            if(Integer.parseInt(aux)==lista.get(i).getCodigo()) {
                                lista.get(j).setOperando1(""+(lista.get(i).getCodigo()+1));
                            } 
                        } 
                    }
                }
                else {
                    Singleton.addTriplaOtimizada(lista.get(i)); 
                }
            }
            else {
                Singleton.addTriplaOtimizada(lista.get(i)); 
            }
        }
    }
    
    public boolean comandoGo(String comando) {
        if(comando.equals("goto") || comando.equals("if") || comando.equals("while") || comando.equals("for"))
          return true;
        return false;
    }
}
