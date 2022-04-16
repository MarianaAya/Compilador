
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
            if(tokens.get(posToken).getToken().equals("t_abre_chaves")) 
                posToken++;
            
            
            
        }
    }
    public void criarTriplas() {
        Token token=null;
        while(posToken<tokens.size()) {
            
            if(posToken<tokens.size() && TipoVariavel(tokens.get(posToken).getToken())) {
                token = tokens.get(posToken);
                proxToken();
                System.out.println("token declaracao = "+tokens.get(posToken).getCadeia());
                Singleton.addTripla(new Tripla(pos,token.getToken(),tokens.get(posToken).getCadeia()));
                System.out.println(""+Singleton.getTriplas().get(Singleton.getTriplas().size()-1).getOperando1());
                pos++;
                proxToken();
            }
            else {
                if(posToken<tokens.size() &&  tokens.get(posToken).getToken().equals("t_identificador")) {
                    identificador();
                }
                else {
                    if(posToken<tokens.size() &&  tokens.get(posToken).getToken().equals("t_while")) {
                        While();
                    }
                    else {
                        if(posToken<tokens.size() &&  tokens.get(posToken).getToken().equals("t_if")) {
                            If();
                        }
                        else {
                           
                            if(posToken<tokens.size() &&  tokens.get(posToken).getToken().equals("t_for")){
                                For();
                            }else {
                                if(posToken<tokens.size() &&  tokens.get(posToken).getToken().equals("t_fecha_chaves")) {
                                    break;
                                }
                                else {
                                    posToken++;
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
        while(posToken<tokens.size() && !tokens.get(posToken).getCadeia().equals(")") && !tokens.get(posToken).getCadeia().equals(",")) {
            if(SinalComparacao(tokens.get(posToken).getToken())) {
                Singleton.addTripla(new Tripla(pos,tokens.get(posToken).getCadeia(),tokens.get(posToken-1).getCadeia(),tokens.get(posToken+1).getCadeia()));
                aux.add(pos);
                pos++;
                
            }
            if(SinalLogica(tokens.get(posToken).getToken())) {
                auxLogico.add(tokens.get(posToken).getCadeia());
                index++;
            }
            posToken++;
        }
        List<Tripla> triplas = Singleton.getTriplas();
        int qtde = triplas.size();
        if(auxLogico.size()>0) {
            for(int i=0;i<auxLogico.size();i++) {
                if(i == 0 ){
                    Singleton.addTripla(new Tripla(pos,auxLogico.get(i),
                            "("+triplas.get(qtde-2*(index-1)-2).getCodigo()+")",
                            "("+triplas.get(qtde-2*(index-1)-1).getCodigo()+")"));
                    pos++;
                }
                else {
                    Singleton.addTripla(new Tripla(pos,auxLogico.get(i),
                            "("+triplas.get(Singleton.getTriplas().size()-1).getCodigo()+")",
                            "("+triplas.get(qtde-2*(index-1)-1).getCodigo()+")"));
                    pos++;
                }
                index--;
            }
        }
    }
    public void For() {
        int compFor;
        int goFor; //para onde deve ir se for false a comparação
        List<Tripla> auxLista = new ArrayList<>();
        proxToken();
        identificador();
        compFor = pos;
        proxToken();
        logico();
        
        Singleton.addTripla(new Tripla(pos,"for",""+pos));
        pos++;
        goFor = pos;
        proxToken();
        identificador();
        for(int index = pos-1; index>=goFor;index--) {
           auxLista.add(Singleton.getTriplas().get(index));
           Singleton.getTriplas().remove(Singleton.getTriplas().get(index));
        }
        pos=goFor;
 
        criarTriplas();
        for(int index = auxLista.size()-1;index>=0;index--) {
            if(auxLista.get(index).getOperando1().charAt(0)=='(') {
                int diferenca = pos - auxLista.get(index).getCodigo();
                int cod = Integer.parseInt(auxLista.get(index).getOperando1().substring(1, auxLista.get(index).getOperando1().length()-1));
                auxLista.get(index).setOperando1("("+(cod+diferenca)+")");   
            }
            if(auxLista.get(index).getOperando2().charAt(0)=='(') {
                int diferenca = pos - auxLista.get(index).getCodigo();
                int cod = Integer.parseInt(auxLista.get(index).getOperando2().substring(1, auxLista.get(index).getOperando2().length()-1));
                auxLista.get(index).setOperando2("("+(cod+diferenca)+")");   
            }
            Singleton.addTripla(auxLista.get(index));
            Singleton.getTriplas().get(Singleton.getTriplas().size()-1).setCodigo(pos);
            pos++;
        }
        Singleton.addTripla(new Tripla(pos,"goto",""+compFor));
        pos++;
        
        Singleton.getTriplas().get(goFor-1).setOperando1(""+pos);
        posToken++;
        
    }
    public void Else() {
        proxToken();
        criarTriplas();
    }
    public void While() {
        proxToken();
        int posWhile = pos; //pego onde começa o while
        proxToken();
        logico();
        int posAlt = pos;
        Singleton.addTripla(new Tripla(pos,"while",
                            ""+pos));
        pos++;
        proxToken();
        criarTriplas();
        Singleton.getTriplas().add(new Tripla(pos,"goto",""+posWhile)); //retornar para o while
        pos++;
        Singleton.getTriplas().get(posAlt).setOperando1(""+(pos)); //comandos depois do while
        posToken++;
        
    }
    public void If() {
        proxToken();
        logico();
        int posAlt = pos;
        int fimElse = -1;
        Singleton.addTripla(new Tripla(pos,"if",
                            ""+pos));
        pos++;
        proxToken();
        criarTriplas();
        
        int pularElse = pos;
        //quando for true, não passar pelo else
        Singleton.addTripla(new Tripla(pos,"goto",""+fimElse));
        pos++;

        posToken++;
        if(tokens.get(posToken).getToken().equals("t_else")) {
            int inicioElse = pos;
            Else();
            posToken++;
            fimElse = pos;
            Singleton.getTriplas().get(posAlt).setOperando1(""+inicioElse); //quando o if é falso vai para posAlt
        }
        else {
            Singleton.getTriplas().get(posAlt).setOperando1(""+(pos)); //comandos depois do if
        }

      
        if(fimElse != -1) {
            Singleton.getTriplas().get(pularElse).setOperando1(""+fimElse);
        } 
       
        
    }
    public void identificador() {
        int i = posToken;
        int fim;
        List<Tripla> aux = new ArrayList<>();
        while(i<tokens.size() && (Termo(tokens.get(i).getToken()) || tokens.get(i).getToken().equals("t_sinal_definicao") 
                || tokens.get(i).getToken().equals("t_string") || SinalOperacao(tokens.get(i).getToken()))) {
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
            boolean primeiraVez = true;
            while(i>posToken ) {
                auxT = tokens.get(i).getCadeia();
                i--;
                operacao = tokens.get(i).getCadeia();
                i--;
                if(primeiraVez) {
                    Singleton.addTripla(new Tripla(pos,operacao,tokens.get(i).getCadeia(),auxT));
                    pos++;
                    primeiraVez = false;
                }
                else {
                    Singleton.addTripla(new Tripla(pos,operacao,
                                                    tokens.get(i).getCadeia(),
                                                    "("+(pos-1)+")"));
                    pos++;
                }
   
  
                
            }
        }
        posToken = fim;
        System.out.println("fim expressao "+tokens.get(posToken).getCadeia());
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
