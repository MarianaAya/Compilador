
package compilador.models;

import java.util.ArrayList;
import java.util.List;

public class GeracaoCodigoIntermediario {
    private List<Token> tokens;
    private int pos = 0;
    private int posToken = 0;
    public void gerar(){
        tokens = Singleton.getTokensResultado();
        criarTriplas();
    }
    public void proxToken() {
        posToken++;
        if(posToken<tokens.size()) {
            if(tokens.get(posToken).getToken().equals("t_pontovirgula")) 
                posToken++;
            if(tokens.get(posToken).getToken().equals("t_virgula")) 
                posToken++;
            if(tokens.get(posToken).getToken().equals("t_abre_parenteses")) 
                posToken++;
            if(tokens.get(posToken).getToken().equals("t_fecha_parenteses")) 
                posToken++;
            if(tokens.get(posToken).getToken().equals("t_fecha_chaves")) 
                posToken++;
            if(tokens.get(posToken).getToken().equals("t_abre_chaves")) 
                posToken++;
            
            
            
        }
    }
    public void criarTriplas() {
        Token token=null;
        while(posToken<tokens.size()) {
            
            if(TipoVariavel(tokens.get(posToken).getToken())) {
                token = tokens.get(posToken);
                proxToken();
                Singleton.addTripla(new Tripla(pos,token.getToken(),tokens.get(posToken).getCadeia()));
                pos++;
                proxToken();
            }
            else {
                if(tokens.get(posToken).getToken().equals("t_identificador")) {
                    identificador();
                }
                else {
                    if(tokens.get(posToken).getToken().equals("t_while")) {
                        While();
                    }
                    else {
                        if(tokens.get(posToken).getToken().equals("t_if")) {
                            If();
                        }
                        else {
                            if(tokens.get(posToken).getToken().equals("t_else")) {
                                Else();
                            }
                            else {
                                if(tokens.get(posToken).getToken().equals("t_else")){
                                    For();
                                }else {
                                    proxToken();
                                }
                         
                            }
                        }
                    }
                }
                
            }

        }
    }
    public void logico() {
        
        List<Integer> aux = new ArrayList<>();
        List<String> auxLogico = new ArrayList<>();
        int index = 0;//index da lista aux
        while(posToken<tokens.size() && !tokens.get(posToken).getCadeia().equals(")")) {
            if(SinalComparacao(tokens.get(posToken).getToken())) {
                Singleton.addTripla(new Tripla(pos,tokens.get(posToken).getCadeia(),tokens.get(posToken-1).getCadeia(),tokens.get(posToken+1).getCadeia()));
                aux.add(pos);
                pos++;
                index++;
            }
            if(SinalLogica(tokens.get(posToken).getToken())) {
                auxLogico.add(tokens.get(posToken).getCadeia());
            }
            posToken++;
        }
        List<Tripla> triplas = Singleton.getTriplas();
        int qtde = triplas.size();
        if(auxLogico.size()>0) {
            for(int i=0;i<auxLogico.size();i++) {
                if(i == 0 ){
                    Singleton.addTripla(new Tripla(pos,auxLogico.get(i),
                            "("+triplas.get(qtde-2*index-2).getCodigo()+")",
                            "("+triplas.get(qtde-2*index-1).getCodigo()+")"));
                }
                else {
                    Singleton.addTripla(new Tripla(pos,auxLogico.get(i),
                            "("+triplas.get(Singleton.getTriplas().size()-1).getCodigo()+")",
                            "("+triplas.get(qtde-2*index-1).getCodigo()+")"));
                }
                index--;
            }
        }
    }
    public void For() {
        
    }
    public void Else() {
        posToken++;
        criarTriplas();
    }
    public void While() {
        proxToken();
        int posWhile = pos; //pego onde começa o while
        proxToken();
        logico();
        int posAlt = pos;
        Singleton.addTripla(new Tripla(pos,"if",
                            ""+pos));
        pos++;
        proxToken();
        criarTriplas();
        Singleton.getTriplas().add(new Tripla(pos,"goto",""+posWhile)); //retornar para o while
        Singleton.getTriplas().get(posAlt).setOperando1(""+(pos)); //comandos depois do while
        
    }
    public void If() {
        proxToken();
        logico();
        System.out.println("sai do logico");
        int posAlt = pos;
        Singleton.addTripla(new Tripla(pos,"if",
                            ""+pos));
        pos++;
        proxToken();
        criarTriplas();
        Singleton.getTriplas().get(posAlt).setOperando1(""+(pos)); //comandos depois do if
        
    }
    public void identificador() {

        int i = posToken;
        int fim;
        List<Tripla> aux = new ArrayList<>();
        while(i<tokens.size() && (Termo(tokens.get(i).getToken()) || tokens.get(i).getToken().equals("t_sinal_definicao") 
                || tokens.get(i).getToken().equals("t_string"))) {
            i++;
        }
        fim = i;
        i--;
        if(i - posToken == 2) { //x = 1 ou é palavra
   
            Singleton.addTripla(new Tripla(pos,"=", tokens.get(i-2).getCadeia(),tokens.get(i).getCadeia()));
            pos++;
        }
        else { //é uma expressao
            String auxT = "";
            String operacao  = "";
            String operacaoInter = "";
            while(i>posToken) {
                auxT = tokens.get(i).getCadeia();
                i--;
                operacao = tokens.get(i).getCadeia();
                i--;
                if(operacao.equals("/") || operacao.equals("*"))
                Singleton.addTripla(new Tripla(0,operacao,tokens.get(i).getCadeia(),auxT));
                i--;
                
                
                
            }
        }
        posToken = fim;
        proxToken();
    }
    
    public boolean SinalOperacao(String token) {
        if(token.equals("t_positivo") || token.equals("t_negativo") || token.equals("t_divisao") || token.equals("t_multiplicacao"))
            return true;
        return false;
    }
    public boolean SinalComparacao(String token) {
        if(token.equals("t_maior") || token.equals("t_menor") || token.equals("t_maior_igual") || token.equals("t_menor_igual")
           || token.equals("t_igual") || token.equals("t_diferente")) {
            return true;
        }
        return false;
    }
    
    public boolean SinalLogica(String token) {
        if(token.equals("t_e") || token.equals("t_ou"))
            return true;
        return false;
    }
    public boolean Termo(String token) {
        if(token.equals("t_identificador") || token.equals("t_numero_int") || token.equals("t_numero_float") || token.equals("t_numero_cientifico")){
            return true;
        }
        return false;
    }
    public boolean Numero(String token) {
        if(token.equals("t_numero_int") || token.equals("t_numero_float") || token.equals("t_numero_cientifico"))
            return true;
        return false;
    }
    public boolean TipoVariavel(String token) {
        if(token.equals("t_tipo_int") || token.equals("t_tipo_float") || token.equals("t_tipo_string")
        || token.equals("t_tipo_cientifico")) {
            return true;
        }
        return false;
    }
}
