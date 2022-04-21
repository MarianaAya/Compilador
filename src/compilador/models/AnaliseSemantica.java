
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
        System.out.println("iniciei semantica");
        lista = Singleton.getTokensResultado();
        proxToken();
        analisarComandos();
        
        List<Simbolo> simbolos = Singleton.getSimbolos();
        for(int i=0;i<simbolos.size();i++) {
            if(simbolos.get(i).getValor().equals("undefined")) {
                Singleton.addErro("Erro semantico na linha "+simbolos.get(i).getLinha()
                        +" : "+simbolos.get(i).getCadeia()+" não teve atribuição"
                        , simbolos.get(i).getLinha());
            }
        }
        
    }
    public void analisarComandos() {
        while(pos<lista.size() && !lista.get(pos).getToken().equals("t_finish") ) {
            if(pos<lista.size() && lista.get(pos).getToken().equals("t_while")) {
                While();
            }
            if(pos<lista.size() && lista.get(pos).getToken().equals("t_for")) {
                For();
            }
            if(pos<lista.size() && lista.get(pos).getToken().equals("t_if")) {
                If();
            }

            if(pos<lista.size() && TipoVariavel(lista.get(pos).getToken())) {
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
            
            if(pos<lista.size() && lista.get(pos).getToken().equals("t_identificador")) {
                Token token_identificador = lista.get(pos);
                proxToken();
                Token token = lista.get(pos);
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
                        else { //é quando a variavel é um número, sendo assim pode haver uma conta
                            String resultado = Operacao();
                            Singleton.getSimbolos().get(pos).setValor(""+resultado);
                        }
                    }
                    
                }
                proxToken();
                
            }
            
            if( pos<lista.size() && lista.get(pos).getToken().equals("t_fecha_chaves")) { //vou sair de uma estrutura
                break;
            }
            
        }
        
    }
    public void verificarIdentificador() {
        Token token_identificador = lista.get(pos);
        proxToken();
        Token token = lista.get(pos);
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
                else { //é quando a variavel é um número, sendo assim pode haver uma conta
                    System.out.println("129 entrei");
                    String resultado = Operacao();
                    Singleton.getSimbolos().get(pos).setValor(""+resultado);
                }
            }
        }
    }
    public void If() {
        proxToken();
        String primeiro = Operacao();
        Token sinalComparacao = null;
        if(SinalComparacao(lista.get(pos).getToken())) {
            sinalComparacao = lista.get(pos);
            proxToken();
            
            String segundo = Operacao();
            boolean resultado;
            System.out.println("144 "+primeiro);
            if(primeiro.equals("NAN") || segundo.equals("NAN")) {
                
            }
            else {
                if(!primeiro.isEmpty() && !segundo.isEmpty()){
                        if((primeiro.charAt(0) == 34 && segundo.charAt(0)!=34) || (primeiro.charAt(0) != 34 && segundo.charAt(0)==34)){
                            Singleton.addErro("Erro semantico na linha "+lista.get(pos).getLinha()+" : não é possivel fazer comparações com tipos diferentes",lista.get(pos).getLinha());
                        }
                        else {
                            if(primeiro.charAt(0) == 34){
                                resultado = Comparacao(primeiro,segundo,sinalComparacao.getCadeia(),"string");
                            }
                            else {
                                resultado = Comparacao(primeiro,segundo,sinalComparacao.getCadeia(),"numero");
                            }
                        }

                    }
            }
            
            while(SinalLogica(lista.get(pos).getToken())) {
                pos++;
                primeiro = Operacao();
                sinalComparacao = lista.get(pos);
                pos++;
                segundo = Operacao();
                if(primeiro.equals("NAN") || segundo.equals("NAN")) {
                
                }
                else {
                    if(!primeiro.isEmpty() && !segundo.isEmpty()){
                        if((primeiro.charAt(0) == 34 && segundo.charAt(0)!=34) || (primeiro.charAt(0) != 34 && segundo.charAt(0)==34)){
                            Singleton.addErro("Erro semantico na linha "+lista.get(pos).getLinha()+" : não é possivel fazer comparações com tipos diferentes",lista.get(pos).getLinha());
                        }
                        else {
                            if(primeiro.charAt(0) == 34){
                                resultado = Comparacao(primeiro,segundo,sinalComparacao.getCadeia(),"string");
                            }
                            else {
                                resultado = Comparacao(primeiro,segundo,sinalComparacao.getCadeia(),"numero");
                            }
                        }

                    }
                }
               
            }
    
  
            proxToken();
            proxToken();
            analisarComandos();

        }
 
        while(lista.get(pos).getToken().equals("t_fecha_chaves")) {
            pos++;
        }
        System.out.println("164 "+lista.get(pos).getCadeia());
        if(lista.get(pos).getToken().equals("t_else")) {
            pos++;
            proxToken();
            analisarComandos();
        }
    }
    public void For() {
        proxToken();
        verificarIdentificador();
        proxToken();
        String primeiro = Operacao();
        Token sinalComparacao = null;
        if(SinalComparacao(lista.get(pos).getToken())) {
            sinalComparacao = lista.get(pos);
            proxToken();
            
            String segundo = Operacao();
            boolean resultado;
            if(primeiro.equals("NAN") || segundo.equals("NAN")) {
                
            }
            else {
                if(!primeiro.isEmpty() && !segundo.isEmpty()){
                        if((primeiro.charAt(0) == 34 && segundo.charAt(0)!=34) || (primeiro.charAt(0) != 34 && segundo.charAt(0)==34)){
                            Singleton.addErro("Erro semantico na linha "+lista.get(pos).getLinha()+" : não é possivel fazer comparações com tipos diferentes",lista.get(pos).getLinha());
                        }
                        else {
                            if(primeiro.charAt(0) == 34){
                                resultado = Comparacao(primeiro,segundo,sinalComparacao.getCadeia(),"string");
                            }
                            else {
                                resultado = Comparacao(primeiro,segundo,sinalComparacao.getCadeia(),"numero");
                            }
                        }

                    }
            }
        }

        proxToken();

        verificarIdentificador();

        while(pos<lista.size() && !lista.get(pos).getToken().equals("t_fecha_parenteses")) {
            pos++;
        }
   
        proxToken();
        proxToken();
        //System.out.println("254 "+lista.get(pos).getCadeia());
        analisarComandos();
        pos++;
        
        
    }
    public void pularAteFechaChaves() {
        while(pos<lista.size() && !lista.get(pos).getToken().equals("t_fecha_chaves")) {
            pos++;
        }
    }
    public String getValor(Token token) {
        String valor = "";
        if(token.getToken().equals("t_identificador")) {
            int pos = Singleton.getPosSimbolo(token.getCadeia());
            if(pos == -1) {
               Singleton.addErro("Erro semantico na linha "+token.getLinha()+" : "+token.getCadeia()+" não foi declarada",token.getLinha());
            }
            else {
               if(Singleton.getSimbolos().get(pos).getValor().equals("undefined")){
                   Singleton.addErro("Erro semantico na linha "+token.getLinha()+" : "+token.getCadeia()+" não foi inicializada",token.getLinha());
               }
               else {
  
                   valor = Singleton.getSimbolos().get(pos).getValor();
               }
            }
        }
        else {
            valor = token.getCadeia();
        }
        return valor;
    }
    public double Conta(double primeiro, double segundo, String sinal) {
        double resultado = 0;
        switch(sinal) {
            case "+": resultado = primeiro + segundo;
                break;
            case "-": resultado = primeiro - segundo;
                break;
            case "*": resultado = primeiro * segundo;
                break;
            case "/": resultado = primeiro / segundo;
                break;
        }
        
        return resultado;
        
    }
    public String Operacao() {
        boolean bwhile = false;
        boolean erro = false;
        double resultado = 0;
        String termo = "";
        String sinal="";
        String inicial = getValor(lista.get(pos));
        if(inicial.length()>0 && inicial.charAt(0)== 34) {
            termo = inicial;
            erro=true;   
        }
        else {
           
            termo = inicial;
            if(termo.length()>0)
                resultado = Double.parseDouble(termo);
        }
        pos++;
        while(pos<lista.size() && (SinalOperacao(lista.get(pos).getToken()) || Termo(lista.get(pos).getToken()))) {
            bwhile=true;
            if(Termo(lista.get(pos).getToken())) {
                termo = getValor(lista.get(pos));
                if(termo.length()>0 && termo.charAt(0)==34) {
                    erro=true;
                    Singleton.addErro("Erro semantico na linha "+lista.get(pos).getLinha()+" : não é possivel fazer operações com string",lista.get(pos).getLinha());
                }
                else {
                    if(!termo.isEmpty()) {
                        resultado = Conta(resultado,Double.parseDouble(termo),sinal);
                        termo = ""+resultado;
                    }
                }
            }
            if(SinalOperacao(lista.get(pos).getToken())) {
                sinal = lista.get(pos).getCadeia();
            }
            pos++;
        }
        if(!erro)
            return ""+resultado;
        else {
            if(bwhile && erro) {
                return "NAN";
            }
            else{
                    
                return termo;
            }
            
        }
            
    }
    public void While() {
        proxToken();
        boolean resultado;
        String primeiro = Operacao();
        Token sinalComparacao = null;
        if(SinalComparacao(lista.get(pos).getToken())) {
            sinalComparacao = lista.get(pos);
            proxToken();
            
            String segundo = Operacao();
            if(primeiro.equals("NAN") || segundo.equals("NAN")) {
                
            }
            else {
                if(!primeiro.isEmpty() && !segundo.isEmpty()){
                    if((primeiro.charAt(0) == 34 && segundo.charAt(0)!=34) || (primeiro.charAt(0) != 34 && segundo.charAt(0)==34)){
                        Singleton.addErro("Erro semantico na linha "+lista.get(pos).getLinha()+" : não é possivel fazer comparações com tipos diferentes",lista.get(pos).getLinha());
                    }
                    else {
                        if(primeiro.charAt(0) == 34){
                            resultado = Comparacao(primeiro,segundo,sinalComparacao.getCadeia(),"string");
                        }
                        else {
                            resultado = Comparacao(primeiro,segundo,sinalComparacao.getCadeia(),"numero");
                        }
                    }
                    
                }
            }
            
            while(SinalLogica(lista.get(pos).getToken())) {
                pos++;
                primeiro = Operacao();
                sinalComparacao = lista.get(pos);
                pos++;
                segundo = Operacao();
                if(primeiro.equals("NAN") || segundo.equals("NAN")) {
                
                }
                else {
                    if(!primeiro.isEmpty() && !segundo.isEmpty()){
                        if((primeiro.charAt(0) == 34 && segundo.charAt(0)!=34) || (primeiro.charAt(0) != 34 && segundo.charAt(0)==34)){
                            Singleton.addErro("Erro semantico na linha "+lista.get(pos).getLinha()+" : não é possivel fazer comparações com tipos diferentes",lista.get(pos).getLinha());
                        }
                        else {
                            if(primeiro.charAt(0) == 34){
                                resultado = Comparacao(primeiro,segundo,sinalComparacao.getCadeia(),"string");
                            }
                            else {
                                resultado = Comparacao(primeiro,segundo,sinalComparacao.getCadeia(),"numero");
                            }
                        }

                    }
                }
            }
    

            proxToken();
            proxToken();
            analisarComandos();
            pos++;
        }
        
    }
    
    public boolean Comparacao(String primeiro, String segundo, String sinal, String tipo) {
        boolean flag=false;
        if(tipo.equals("string"))
        {
            flag=true;
        }
        else {
            switch(sinal) {
                case ">": if(Double.parseDouble(primeiro)>Double.parseDouble(segundo))
                            flag = true;
                    break;
                case "<": if(Double.parseDouble(primeiro)<=Double.parseDouble(segundo))
                            flag = true;
                    break;
                case ">=": if(Double.parseDouble(primeiro)>=Double.parseDouble(segundo))
                            flag = true;
                    break;
                case "<=": if(Double.parseDouble(primeiro)<=Double.parseDouble(segundo))
                            flag = true;
                    break;
                case "==": if(Double.parseDouble(primeiro)==Double.parseDouble(segundo))
                            flag = true;
                    break;
                case "!=": if(Double.parseDouble(primeiro)!=Double.parseDouble(segundo))
                            flag = true;
                    break;

            }
        }
        return flag;
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
    public boolean Termo(String token) {
        if(token.equals("t_identificador") || token.equals("t_numero_int") || token.equals("t_numero_float") || token.equals("t_numero_cientifico")){
            return true;
        }
        return false;
    }
    public boolean SinalLogica(String token) {
        if(token.equals("t_e") || token.equals("t_ou"))
            return true;
        return false;
    }
}
