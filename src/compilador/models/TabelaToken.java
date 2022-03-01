
package compilador.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class TabelaToken {
    private List<Token> tabela;

    public TabelaToken() {
        tabela = new ArrayList<Token>();
        criarTabela();
    }
    
    public void criarTabela() {
        tabela.add(new Token("t_go","go"));
        tabela.add(new Token("t_finish","finish"));
        tabela.add(new Token("t_tipo_int","int"));
        tabela.add(new Token("t_tipo_float","float"));
        tabela.add(new Token("t_tipo_string","string"));
        tabela.add(new Token("t_tipo_cientifico","cientifico"));
        tabela.add(new Token("t_while","while"));
        tabela.add(new Token("t_for","for"));
        tabela.add(new Token("t_if","if"));
        tabela.add(new Token("t_else","else"));
        tabela.add(new Token("t_sinal_definicao","="));
        tabela.add(new Token("t_positivo","+"));
        tabela.add(new Token("t_negativo","-"));
        tabela.add(new Token("t_multiplicacao","*"));
        tabela.add(new Token("t_divisao","/"));
        tabela.add(new Token("t_abre_parenteses","("));
        tabela.add(new Token("t_fecha_parenteses",")"));
        tabela.add(new Token("t_abre_chaves","{"));
        tabela.add(new Token("t_fecha_chaves","}"));
        tabela.add(new Token("t_abre_chaves","{"));
        tabela.add(new Token("t_fecha_chaves","}"));
        tabela.add(new Token("t_ponto","."));
        tabela.add(new Token("t_maior",">"));
        tabela.add(new Token("t_menor","<"));
        tabela.add(new Token("t_maior_igual",">="));
        tabela.add(new Token("t_menor_iguaç","<="));
        tabela.add(new Token("t_igual","=="));
        tabela.add(new Token("t_diferente","!="));
        tabela.add(new Token("t_pontovirgula",";"));
        tabela.add(new Token("t_virgula",","));
        tabela.add(new Token("t_e","&"));
        tabela.add(new Token("t_ou","|"));
   
        
        
    }
    
    public Token descobrirToken(String cadeia) {
        Token escolhido=null;
        int i=0;
        boolean flag=false;
        String regex="";
        //Verificar token na tabela
        while(i<tabela.size() && !flag) {
            if(cadeia.equals(tabela.get(i).getCadeia())){
                flag=true;
                escolhido=tabela.get(i);
            }
            i++;
        }
        
        //Verificar se é número inteiro
        if(!flag) {
            i=0;
            regex="[0-9]+";
            if(cadeia.matches(regex)){
                flag=true;
                escolhido=new Token("t_numero_int",cadeia);
            }  
        }
        //Verificar se é notação cientifica
        if(!flag) {
            i=0;
            regex="[0-9]+.?[0-9]*E[0-9]+";
            if(cadeia.matches(regex)){
                flag=true;
                escolhido=new Token("t_numero_cientifico",cadeia);
            }  
        }
        //Verificar se é número float
        if(!flag) {
            i=0;
            regex="[0-9]+.[0-9]+";
            if(cadeia.matches(regex)){
                flag=true;
                escolhido=new Token("t_numero_float",cadeia);
            }  
        }
        //Verificar se é identificador
        if(!flag) {
            i=0;
            regex="[a-zA-Z0-9]+";
            
            if(cadeia.matches(regex)){
                flag=true;
                escolhido=new Token("t_identificador",cadeia);
            }  
        }
        
        //verificar se é string
        if(!flag) {
            if(cadeia.charAt(0)==34 && cadeia.charAt(cadeia.length()-1)==34) {
                escolhido = new Token("t_string",cadeia);
            }
        }
      
        
        return escolhido;
        
    }
    public List<Token> getTabela() {
        return tabela;
    }

    public void setTabela(List<Token> tabela) {
        this.tabela = tabela;
    }
    
    
}
