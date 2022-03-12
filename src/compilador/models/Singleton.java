
package compilador.models;

import java.util.ArrayList;
import java.util.List;


public class Singleton {
    private static List<Token> tokens=new ArrayList<>();
    private static List<Erro> erros=new ArrayList<>();
    public static int pos;
    public static List<Token> getTokensResultado()
    {
        return tokens;
    }
    public static void addResultadoToken(Token token){
        tokens.add(token);
    }
    
    public static void removeAllResultadoToken(){
        tokens.clear();
    }
    
    
    
    public static List<Erro> getErros()
    {
        return erros;
    }
    public static void addErro(String msg, int linha){
        erros.add(new Erro(msg,linha));
    }
    public static void addErro(String msg){
        erros.add(new Erro(msg,0));
    }
    public static void removeAllErros(){
        erros.clear();
    }
    
    
  
    
    private Singleton()
    {
        //impede a instacia da classe
    }
}
