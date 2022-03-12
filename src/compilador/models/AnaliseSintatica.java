
package compilador.models;

import java.util.List;
import javafx.scene.control.TextArea;


public class AnaliseSintatica {
    private int pos = 0;
    private TextArea txCodigo;
    public void receberPrograma(TextArea txCodigo) {
        List<Token> lista = Singleton.getTokensResultado();
        this.txCodigo=txCodigo;
        pos=0;
        if(!lista.isEmpty())
            Programa(lista);
        
        
    }
    public void Programa(List<Token> lista){
        
        
        if(lista.get(pos).getToken().equals("t_go")) { //Se o primeiro token é GO
            pos++;
            if(pos<lista.size()){ //Se o primeiro token é GO e continua
                if(lista.get(pos).getToken().equals("t_finish")) {
                    pos++;
                    if(pos<lista.size()) { //O programa continua depois do FINISH
                        Singleton.addErro("Erro sintático: programa não deveria continuar depois do FINISH");
                    }
                }
                else { //Próximo não é FINISH
                    Comandos(lista,false);
                    if(Singleton.getErros().size()==0){
                   
                        if(!(lista.get(lista.size()-1).getToken().equals("t_finish"))){ //Se o programa não terminou com finish
                            Singleton.addErro("Erro sintático: programa não tem FINISH");
                    }
                    }
                }
            }
            else { //Se o primeiro token é GO e não tem o FINISH
                Singleton.addErro("Erro sintático: programa não tem o FINISH");
            }
        }
        else {
            Singleton.addErro("Erro sintático: programa não começa com GO");
        }
    }
    
    public void Comandos(List<Token> lista, boolean flag) { //a flag é para dizer que está dentro de um if, while ...
        while(pos+1<lista.size()){

            if(lista.get(pos).getToken().equals("t_identificador")){ //nesse caso ele pode ser uma definicao ou uma expressao
                Definicao(lista,false);
            }
            else {
                if(lista.get(pos).getToken().equals("t_while")) { //é o while
                    pos++;
                    While(lista);
                }
                else{
                    if(lista.get(pos).getToken().equals("t_for")) { //é o for
                        pos++;
                        For(lista);
                    }
                    else {
                        if(lista.get(pos).getToken().equals("t_if")) { // é o if
                            pos++;
                            If(lista);
                        }
                        else {
                            if(lista.get(pos).getToken().equals("t_tipo_int") || lista.get(pos).getToken().equals("t_tipo_float")
                               || lista.get(pos).getToken().equals("t_tipo_string") || lista.get(pos).getToken().equals("t_tipo_cientifico")){
                                pos++;
                                Declaracao(lista);
                            }
                            else{
                                if(lista.get(pos).getToken().equals("t_fecha_chaves") && flag){
                                    flag=false;
                                    break;
                                }
                                else{
                                    if(lista.get(pos).getToken().equals("t_finish") && flag){
                                        Singleton.addErro("Erro sintático na linha "+lista.get(pos).getLinha()+": finish não pode ser colocado aqui");
                                    }
                                    else{
                                        if(lista.get(pos).getToken().equals("t_go")){
                                            Singleton.addErro("Erro sintático na linha "+lista.get(pos).getLinha()+": go só pode ser colocado no começo");
                                        }
                                        else{
                                            if(pos< lista.size()-1 && lista.get(pos).getToken().equals("t_finish")){
                                                Singleton.addErro("Erro sintático na linha "+lista.get(pos).getLinha()+": finish deve ser colocado somente no final do programa");
                                            }
                                            else{
                                                if(lista.get(pos).getToken().equals("t_go")) {
                                                    Singleton.addErro("Erro sintático na linha "+lista.get(pos).getLinha()+": go deve ser colocado somente no inicio do programa");
                                                }
                                                else
                                                    Singleton.addErro("Erro sintático na linha "+lista.get(pos).getLinha()+": não reconhecida com um comando "+lista.get(pos).getToken()+" da cadeia = "+lista.get(pos).getCadeia());
                                            
                                            }
                                        }
                                    }
                                    
                                }
                            }
                        }
                    }
                }
            }
            //pos++;
           
        }
    }
    public void Definicao(List<Token> lista, boolean flagFor) { //precisei dessa flag, pois definicao aparece em comandos e for
        int qtdeErros = Singleton.getErros().size();
        if(pos<lista.size() && lista.get(pos).getToken().equals("t_identificador")){
            pos++;
            if(pos<lista.size() && lista.get(pos).getToken().equals("t_sinal_definicao")) {
                pos++;
                if(pos<lista.size() && (Numero(lista.get(pos).getToken()) || lista.get(pos).getToken().equals("t_string") 
                         || lista.get(pos).getToken().equals("t_identificador") )){
                    
                    if(Numero(lista.get(pos).getToken()) || lista.get(pos).getToken().equals("t_identificador")) {
                        pos++;
                        if(pos<lista.size()) {
                            if(SinalOperacao(lista.get(pos).getToken())) {
                    
                                pos--;
                                
                                Expressao(lista);
                            }
                           
                        }
                        if(!(pos<lista.size() && lista.get(pos).getToken().equals("t_pontovirgula")) && !flagFor) {
                            Singleton.addErro("Erro sintático na linha "+lista.get(pos-1).getLinha()+": era esperado ;");
                        }
                        else {
                            if(!flagFor)
                                pos++;
                        }
                    }
                    else {
                        pos++;
                        if(!(pos<lista.size() && lista.get(pos).getToken().equals("t_pontovirgula"))) {
                            Singleton.addErro("Erro sintático na linha "+lista.get(pos-1).getLinha()+": era esperado ;");
                        }
                        else
                            pos++;
                    }
                    

                }
                else {
                    Singleton.addErro("Erro sintático na linha "+lista.get(pos-1).getLinha()+": erro no comando DEFINICAO, era esperado uma string, número ou expressão");
                }
            }
            else {
                Singleton.addErro("Erro sintático na linha "+lista.get(pos-1).getLinha()+": erro no comando DEFINICAO, era esperado =");
            }
        }
        else {
            Singleton.addErro("Erro sintático na linha "+lista.get(pos-1).getLinha()+": erro no comando DEFINICAO");
        }
        

        if(Singleton.getErros().size()-qtdeErros>0){ //Se houve erro na Definicao
            continuarFimSentenca(lista);
        }
        
     
     
    }
    public void analisarInteriorFor(List<Token> lista) {
        
    }
    public void For(List<Token> lista) {
        int qtdeErros=0;
        if(pos<lista.size()){
            if(lista.get(pos).getToken().equals("t_abre_parenteses")) {
                pos++;
                qtdeErros = Singleton.getErros().size();
                Definicao(lista,true);
                if(pos<lista.size() && lista.get(pos).getToken().equals("t_virgula")){
                    pos++;
                    
                    Comparacao(lista);

                    pos++;

                    if(pos<lista.size() && lista.get(pos).getToken().equals("t_virgula")) {
                        pos++;
                        if(pos<lista.size() && lista.get(pos).getToken().equals("t_identificador")) {
                            pos++;
                            if(pos<lista.size() && lista.get(pos).getToken().equals("t_sinal_definicao")){
                                pos++;
                                Expressao(lista);
                                //pos++;
                                if(pos<lista.size() && lista.get(pos).getToken().equals("t_fecha_parenteses")) {
                                    pos++;
                                    if(pos<lista.size() && lista.get(pos).getToken().equals("t_abre_chaves")) {
                                        pos++;
                                        Comandos(lista,true);
                                        
                                        if(pos<lista.size() && lista.get(pos).getToken().equals("t_fecha_chaves")) {
                                            pos++;
                                        }
                                        else {
                                            Singleton.addErro("Erro sintático na linha "+lista.get(pos-1).getLinha()+": erro no comando FOR, era esperado }");
                                        }


                                    }
                                    else {
                                        Singleton.addErro("Erro sintático na linha "+lista.get(pos-1).getLinha()+": erro no comando FOR, era esperado {");
                                    }
                                }
                                else {
                                    Singleton.addErro("Erro sintático na linha "+lista.get(pos-1).getLinha()+": erro no comando FOR, era esperado )");
                                }

                            }
                            else {
                                Singleton.addErro("Erro sintático na linha "+lista.get(pos-1).getLinha()+": erro no comando EXPRESSAO, era esperado =");
                            }
                        }
                        else {
                            Singleton.addErro("Erro sintático na linha "+lista.get(pos-1).getLinha()+": erro no comando EXPRESSAO, era esperado identificador");

                        }
                    }
                    else {
                        Singleton.addErro("Erro sintático na linha "+lista.get(pos-1).getLinha()+": erro no comando FOR, era esperado , (segundo)");
                    }
                        
                        
                }
                else {
                    Singleton.addErro("Erro sintático na linha "+lista.get(pos-1).getLinha()+": erro no comando FOR, era esperado , (primeiro)");
                }
                
            }
            else {
                Singleton.addErro("Erro sintático na linha "+lista.get(pos-1).getLinha()+": erro no comando FOR, era esperado (");
            }
        }
        else {
            Singleton.addErro("Erro sintático na linha "+lista.get(pos-1).getLinha()+": erro no comando FOR");
        }
        
    }
    public void While(List<Token> lista) {
        
        if(pos<lista.size()) {
            if(lista.get(pos).getToken().equals("t_abre_parenteses")) {
                pos++;
                Comparacao(lista);
                if(pos<lista.size()) {
                    pos++;
                    if(lista.get(pos).getToken().equals("t_fecha_parenteses")){
                        pos++;
                        if(pos<lista.size() && lista.get(pos).getToken().equals("t_abre_chaves")){
                            pos++;
                            Comandos(lista,true);

                            if(!(pos<lista.size() && lista.get(pos).getToken().equals("t_fecha_chaves"))){
                                Singleton.addErro("Erro sintático na linha "+lista.get(pos-1).getLinha()+": erro no comando WHILE, era esperado }");
                            }
                            else
                                pos++;
                            

                        }
                        else {
                            Singleton.addErro("Erro sintático na linha "+lista.get(pos-1).getLinha()+": erro no comando WHILE, era esperado {");
                        }
                    }
                    else {
                        Singleton.addErro("Erro sintático na linha "+lista.get(pos-1).getLinha()+": erro no comando WHILE, era esperado )");
                    }
                }
                else {
                    Singleton.addErro("Erro sintático na linha "+lista.get(pos-1).getLinha()+": erro no comando WHILE, era esperado )");
                }
                
               
            }
            else {
                Singleton.addErro("Erro sintático na linha "+lista.get(pos-1).getLinha()+": erro no comando WHILE, era esperado (");
            }
        }
        else {
            Singleton.addErro("Erro sintático na linha "+lista.get(pos-1).getLinha()+": erro no comando WHILE");
        }
        
    }
    
    public void Declaracao(List<Token> lista) {
        int qtdeErros = Singleton.getErros().size();
        if(pos<lista.size() && lista.get(pos).getToken().equals("t_identificador")) {
           pos++;
           if(pos<lista.size() && lista.get(pos).getToken().equals("t_sinal_definicao")){
               Singleton.addErro("Erro sintático na linha "+lista.get(pos-1).getLinha()+": erro no comando DECLARACAO");
           } 
           else {
               if(!(pos<lista.size() && lista.get(pos).getToken().equals("t_pontovirgula"))) {
                   Singleton.addErro("Erro sintático na linha "+lista.get(pos-1).getLinha()+": era esperado ;");
               }
               else
                   pos++;
           }
        }
        else
            Singleton.addErro("Erro sintático na linha "+lista.get(pos-1).getLinha()+": erro no comando DECLARACAO");
        if(Singleton.getErros().size()>qtdeErros) {
            continuarFimSentencaIdentificador(lista);
            pos++;
        }
    }
    
    public void continuarFimSentencaIdentificador(List<Token> lista) {
        boolean flag=false;
        while(pos<lista.size() && !flag) {
            if((lista.get(pos).getToken().equals("t_fecha_parenteses") || lista.get(pos).getToken().equals("t_fecha_chaves") 
                || TipoVariavel(lista.get(pos).getToken()) || lista.get(pos).getToken().equals("t_abre_chaves")
                || lista.get(pos).getToken().equals("t_identificador"))) {
                flag=true;
            }
            else    
                pos++;
        }
        pos--;
    }
    
    public void If(List<Token> lista) {
        
        if(pos<lista.size()) {
            if(lista.get(pos).getToken().equals("t_abre_parenteses")) {
                pos++;
                
                Comparacao(lista);
            
                if(pos<lista.size()){
                    pos++;
                    if(lista.get(pos).getToken().equals("t_fecha_parenteses")) {
                        pos++;
                        if(pos<lista.size() && lista.get(pos).getToken().equals("t_abre_chaves")) {

                            pos++;
                            Comandos(lista,true);
                            if(!(pos<lista.size() && lista.get(pos).getToken().equals("t_fecha_chaves"))) {
                                Singleton.addErro("Erro sintático na linha "+lista.get(pos-1).getLinha()+": erro no comando IF, era esperado }");
                            }
                            else {
                                pos++;
                                System.out.println("token 333 = "+lista.get(pos).getToken());
                                if(pos<lista.size() && lista.get(pos).getToken().equals("t_else")) {
                                    pos++;
                                    if(pos<lista.size() && lista.get(pos).getToken().equals("t_abre_chaves")) {
                                        pos++;
                                        Comandos(lista,true);
                                        if(Singleton.getErros().size()==0) {
                                            if(!(pos<lista.size() && lista.get(pos).getToken().equals("t_fecha_chaves"))) {
                                                Singleton.addErro("Erro sintático na linha "+lista.get(pos-1).getLinha()+": erro no comando ELSE, era esperado }");

                                            }
                                            else
                                                pos++;
                                        }
                                    }
                                    else {
                                        Singleton.addErro("Erro sintático na linha "+lista.get(pos-1).getLinha()+": erro no comando ELSE, era esperado {");
                                    }

                                }
                            }
                            
                        }
                        else {
                            Singleton.addErro("Erro sintático na linha "+lista.get(pos-1).getLinha()+": erro no comando IF, era esperado {");
                        }
                    }
                    else {
                        Singleton.addErro("Erro sintático na linha "+lista.get(pos-1).getLinha()+": erro no comando IF, era esperado )");
                    }
                }
                else {
                    Singleton.addErro("Erro sintático na linha "+lista.get(pos-1).getLinha()+": erro no comando IF, era esperado (");
                }
                
            }
            else {
                Singleton.addErro("Erro sintático na linha "+lista.get(pos-1).getLinha()+": erro no comando IF");
            }
        }
        else {
            Singleton.addErro("Erro sintático na linha "+lista.get(pos-1).getLinha()+": erro no comando IF");
        }
        
    }
    public void Expressao(List<Token> lista) {
        int qtdeErros = Singleton.getErros().size();
        if(pos<lista.size()) {
            String token = lista.get(pos).getToken();
            if(Termo(token)) { // x
                
                pos++;
                if(pos<lista.size()) {
                    token = lista.get(pos).getToken();
                    if(SinalOperacao(token)) { // x +
                        if(pos<lista.size()) {
                            pos++;
                            if(pos<lista.size()) { // x + x
                                if(Termo(lista.get(pos).getToken())) {
                                    if(pos<lista.size()) {
                                        pos++;
                                        TermoOperacao(lista);
                                             
                                        
                                    }
                                    pos++;  
                                    
                                }
                                else {
                                    Singleton.addErro("Erro sintático na linha "+lista.get(pos-1).getLinha()+": erro na EXPRESSAO, era esperado um identificador ou um número");
                                }
                            }
                            else{
                                Singleton.addErro("Erro sintático na linha "+lista.get(pos-1).getLinha()+": erro na EXPRESSAO, era esperado um identificador ou um núumero");
                            }
                            
                        }
                        else {
                            Singleton.addErro("Erro sintático na linha "+lista.get(pos-1).getLinha()+": erro na EXPRESSAO, era esperado um sinal de comparação");
                        }
                    }
                    else {
                        Singleton.addErro("Erro sintático na linha "+lista.get(pos-1).getLinha()+": erro na EXPRESSAO, era esperado um sinal de operação");
                    }
                    
                } 
                else{
                    Singleton.addErro("Erro sintático na linha "+lista.get(pos-1).getLinha()+": erro na EXPRESSAO, era esperado um sinal de operação");
                }
                
            }
            else {
                Singleton.addErro("Erro sintático na linha "+lista.get(pos-1).getLinha()+": erro na EXPRESSAO, era esperado um identificador ou um número");
            }
        }
        else {
            Singleton.addErro("Erro sintático na linha "+lista.get(pos-1).getLinha()+": erro na EXPRESSAO");
        }
        
        if(qtdeErros<Singleton.getErros().size()) {
            continuarFimSentenca(lista);
        }
    }
    public void Comparacao(List<Token> lista) {
        
        int qtdeErros = Singleton.getErros().size();
        int qtdeErrosProximo=0;
        if(pos<lista.size()) {
            String token = lista.get(pos).getToken();
            if(Termo(token)) { // x
                pos++;
                if(pos<lista.size()) {
                    token = lista.get(pos).getToken();
                    if(SinalComparacao(token)) { // x <
                        if(pos<lista.size()) {
                            pos++;
                            if(pos<lista.size()) { // x < x
                                if(Termo(lista.get(pos).getToken())) {
                                    
                                    if(pos<lista.size()) {
                                        pos++;
                                        TermoComparacao(lista);
                                        qtdeErrosProximo = Singleton.getErros().size()-qtdeErros;
                                        
                                    }
                                    else {
                                        Singleton.addErro("Erro sintático na linha "+lista.get(pos-1).getLinha()+": erro na COMPARACAO, era espera um identificador ou um número");
                                    }
                                }
                                else {
                                    Singleton.addErro("Erro sintático na linha "+lista.get(pos-1).getLinha()+": erro na COMPARACAO, era espera um identificador ou um número");
                                }
                            }
                            else{
                                Singleton.addErro("Erro sintático na linha "+lista.get(pos-1).getLinha()+": erro na COMPARACAO, era esperado um identificador ou um núumero");
                            }
                            
                        }
                        else {
                            Singleton.addErro("Erro sintático na linha "+lista.get(pos-1).getLinha()+": erro na COMPARACAO, era esperado um sinal de comparação");
                        }
                    }
                    else {
                        Singleton.addErro("Erro sintático na linha "+lista.get(pos-1).getLinha()+": erro na COMPARACAO, era esperado um sinal de comparação");
                    }
                    
                } 
                else{
                    Singleton.addErro("Erro sintático na linha "+lista.get(pos-1).getLinha()+": erro na COMPARACAO, era esperado um sinal de comparação");
                }
                
            }
            else {
                Singleton.addErro("Erro sintático na linha "+lista.get(pos-1).getLinha()+": erro na COMPARACAO, era esperado um identificador ou um número");
            }
        }
        else {
            Singleton.addErro("Erro sintático na linha "+lista.get(pos-1).getLinha()+": erro na COMPARACAO");
        }
        
        if(qtdeErrosProximo==0){ //Se não houve erro na Termo Comparacao
            if(Singleton.getErros().size()-qtdeErros>0){ //Se houve erro na Comparacao
                continuarFimSentenca(lista);
            }
        }
       
    }
    public void TermoComparacao(List<Token> lista) {
        if(Singleton.getErros().size()==0) {
         
            if(pos<lista.size() && SinalLogica(lista.get(pos).getToken())) {
                pos++;
                Comparacao(lista);

            }
            else {
                pos--;
            }
        }
    }
    public void TermoOperacao(List<Token> lista) {
        if(Singleton.getErros().size()==0) {
            if(pos<lista.size() && SinalOperacao(lista.get(pos).getToken())) {
                pos++;
             
                if(pos<lista.size() && Termo(lista.get(pos).getToken())) {
                    pos++;
                    TermoOperacao(lista);
                }
                else {
                    Singleton.addErro("Erro sintático na linha "+lista.get(pos-1).getLinha()+": erro no TERMOOPERACAO, era esperado um identificaor ou um número");
                }

            }
            else{
                pos--;
            }
        }
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
        if(token.equals("t_tipo_int") || token.equals("t_tipo_float") || token.equals("t_tipo_string")) {
            return true;
        }
        return false;
    }
    public void continuarFimSentenca(List<Token> lista) {
        boolean flag=false;
        while(pos<lista.size() && !flag) {
            if((lista.get(pos).getToken().equals("t_fecha_parenteses") || lista.get(pos).getToken().equals("t_fecha_chaves") 
                || TipoVariavel(lista.get(pos).getToken()) || lista.get(pos).getToken().equals("t_abre_chaves"))) {
                flag=true;
            }
            else    
                pos++;
        }
        pos--;
    }
   
}
