
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
        while(pos<lista.size() && !lista.get(pos).getToken().equals("t_finish") && Singleton.getErros().size()==0) {
            if(lista.get(pos).getToken().equals("t_while")) {
                While();
            }
            if(lista.get(pos).getToken().equals("t_for")) {
                For();
            }
            if(lista.get(pos).getToken().equals("t_if")) {
                If();
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
                            double resultado = Operacao();
                            Singleton.getSimbolos().get(pos).setValor(""+resultado);
                        }
                    }
                    
                }
                proxToken();
                
            }
            
            if(lista.get(pos).getToken().equals("t_fecha_chaves")) { //vou sair de uma estrutura
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
                    double resultado = Operacao();
                    Singleton.getSimbolos().get(pos).setValor(""+resultado);
                }
            }
        }
    }
    public void If() {
        proxToken();
        double primeiro = Operacao();
        Token sinalComparacao = null;
        if(SinalComparacao(lista.get(pos).getToken())) {
            sinalComparacao = lista.get(pos);
            proxToken();
            
            double segundo = Operacao();
            boolean resultado = Comparacao(primeiro,segundo,sinalComparacao.getCadeia());
            
            while(SinalLogica(lista.get(pos).getToken())) {
                pos++;
                primeiro = Operacao();
                sinalComparacao = lista.get(pos);
                pos++;
                segundo = Operacao();
                resultado = Comparacao(primeiro,segundo,sinalComparacao.getCadeia());
            }
    
            if(resultado) {
                proxToken();
                proxToken();
                analisarComandos();
            }
            else {
                pularAteFechaChaves();
            }
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
        double primeiro = Operacao();
        Token sinalComparacao = null;
        if(SinalComparacao(lista.get(pos).getToken())) {
            sinalComparacao = lista.get(pos);
            proxToken();
            
            double segundo = Operacao();
            boolean resultado = Comparacao(primeiro,segundo,sinalComparacao.getCadeia());
        }
        proxToken();

        verificarIdentificador();
        if(Singleton.getErros().size()>0) {
            proxToken();
            proxToken();

            analisarComandos();
            pos++;
        }
        
    }
    public void pularAteFechaChaves() {
        while(pos<lista.size() && lista.get(pos).getToken().equals("t_fecha_chaves")) {
            pos++;
        }
    }
    public double getValor(Token token) {
        double valor = 0;
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
                   valor = Double.parseDouble(Singleton.getSimbolos().get(pos).getValor());
               }
            }
        }
        else {
            valor = Double.parseDouble(token.getCadeia());
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
    public double Operacao() {
        double resultado = 0;
        double termo = 0;
        String sinal="";
        resultado = getValor(lista.get(pos));
        pos++;
        while(pos<lista.size() && (SinalOperacao(lista.get(pos).getToken()) || Termo(lista.get(pos).getToken()))) {
            if(Termo(lista.get(pos).getToken())) {
                termo = getValor(lista.get(pos));
                resultado = Conta(resultado,termo,sinal);
            }
            if(SinalOperacao(lista.get(pos).getToken())) {
                sinal = lista.get(pos).getCadeia();
            }
            pos++;
        }
        return resultado;
    }
    public void While() {
        proxToken();
        double primeiro = Operacao();
        Token sinalComparacao = null;
        if(SinalComparacao(lista.get(pos).getToken())) {
            sinalComparacao = lista.get(pos);
            proxToken();
            
            double segundo = Operacao();
            boolean resultado = Comparacao(primeiro,segundo,sinalComparacao.getCadeia());
            
            while(SinalLogica(lista.get(pos).getToken())) {
                pos++;
                primeiro = Operacao();
                sinalComparacao = lista.get(pos);
                pos++;
                segundo = Operacao();
                resultado = Comparacao(primeiro,segundo,sinalComparacao.getCadeia());
            }
    
            if(resultado) {
                proxToken();
                proxToken();
                analisarComandos();
                pos++;
            }
            else {
                pularAteFechaChaves();
            }
        }
        
    }
    
    public boolean Comparacao(double primeiro, double segundo, String sinal) {
        boolean flag=false;
        switch(sinal) {
            case ">": if(primeiro>segundo)
                        flag = true;
                break;
            case "<": if(primeiro<=segundo)
                        flag = true;
                break;
            case ">=": if(primeiro>=segundo)
                        flag = true;
                break;
            case "<=": if(primeiro<=segundo)
                        flag = true;
                break;
            case "==": if(primeiro==segundo)
                        flag = true;
                break;
            case "!=": if(primeiro!=segundo)
                        flag = true;
                break;
               
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
