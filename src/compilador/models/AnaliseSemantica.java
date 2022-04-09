
package compilador.models;

import java.util.List;


public class AnaliseSemantica {
    private int pos = 0;
    private List<Token> lista;
    public void proxToken() {
        pos++;
        if(pos<lista.size()) {
            if(lista.get(pos).getToken().equals("t_pontovirgula")) 
                pos++;
            if(lista.get(pos).getToken().equals("t_virgula")) 
                pos++;
            if(lista.get(pos).getToken().equals("t_abre_parenteses")) 
                pos++;
            if(lista.get(pos).getToken().equals("t_fecha_parenteses")) 
                pos++;
            if(lista.get(pos).getToken().equals("t_fecha_parenteses")) 
                pos++;
            if(lista.get(pos).getToken().equals("t_sinal_definicao")) 
                pos++;

            
            
        }
    }
    public void analisar() {
        lista = Singleton.getTokensResultado();
        proxToken();
        analisarComandos();
        
    }
    public void analisarComandos() {
        while(pos<lista.size() && !lista.get(pos).getToken().equals("t_finish")) {
            if(lista.get(pos).getToken().equals("t_while")) {
                While();
            }
            if(lista.get(pos).getToken().equals("t_for")) {

            }
            if(lista.get(pos).getToken().equals("t_if")) {

            }

            if(TipoVariavel(lista.get(pos).getToken())) {
                String tipo = definirTipoVariavel(lista.get(pos).getToken());
                pos++;
                Token token = lista.get(pos);
 
                if(!Singleton.simboloNaLista(token.getCadeia())) {
                    Singleton.addSimbolos(new Simbolo(token.getToken(),token.getCadeia(),tipo,"undefined",token.getLinha()));
                }
                else {
                    Singleton.addErro("Erro semantico na linha "+token.getLinha()+" : variavel "+token.getCadeia()+" já foi declarada",token.getLinha());
                }
                proxToken();
            }
            
            if(lista.get(pos).getToken().equals("t_identificador")) {
                Token token_identificador = lista.get(pos);
                proxToken();
                Token token = lista.get(pos);
                
                System.out.println("67 "+token.getCadeia());
                int pos = Singleton.getPosSimbolo(token_identificador.getCadeia());
                if(pos == -1) {
                    Singleton.addErro("Erro semantico na linha "+token.getLinha()+" : "+token_identificador.getCadeia()+" não foi declarada",token.getLinha());
                }
                else {
                    Simbolo simbolo =  Singleton.getSimbolos().get(pos);
                    if(simbolo.getTipo().equals("string")) { //se a variavel for do tipo strinf
                        if(token.getToken().equals("t_string")) {
                            Singleton.getSimbolos().get(pos).setValor(token.getCadeia());
                        }
                        else {
                            Singleton.addErro("Erro semantico na linha "+token.getLinha()+" : "+token_identificador.getCadeia()+" não pode receber números",token.getLinha());
                        }
                    }
                    else {
                        if(token.getToken().equals("t_string")) {
                            Singleton.addErro("Erro semantico na linha "+token.getLinha()+" : "+token_identificador.getCadeia()+" não pode receber string",token.getLinha());
                            
                        }
                        else {
                            Singleton.getSimbolos().get(pos).setValor(token.getCadeia());
                        }
                    }
                    
                }
                proxToken();
                
            }
            
        }
        
    }
    public void While() {
        
    }
    
    public boolean TipoVariavel(String token) {
        if(token.equals("t_tipo_int") || token.equals("t_tipo_float") || token.equals("t_tipo_string")
                || token.equals("t_tipo_cientifico")) {
            return true;
        }
        return false;
    }
    public String definirTipoVariavel(String token) {
        if(token.equals("t_tipo_int"))
            return "inteiro";
        if(token.equals("t_tipo_float"))
            return "float";
        if(token.equals("t_tipo_cientifico"))
            return "cientifico";
        if(token.equals("t_tipo_string"))
            return "string";
        return "";
    }
}
