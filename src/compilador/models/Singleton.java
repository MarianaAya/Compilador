
package compilador.models;

import java.util.ArrayList;
import java.util.List;


public class Singleton {
    private static List<Token> tokens=new ArrayList<>();
    private static List<String> erros=new ArrayList<>();
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
    
    
    
    public static List<String> getErros()
    {
        return erros;
    }
    public static void addErro(String resultado){
        erros.add(resultado);
    }
    public static void removeAllErros(){
        erros.clear();
    }
    
    
  
    
    private Singleton()
    {
        //impede a instacia da classe
    }
}
