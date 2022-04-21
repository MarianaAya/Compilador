
package compilador.models;

import java.util.ArrayList;
import java.util.List;


public class Singleton {
    private static List<Token> tokens=new ArrayList<>();
    private static List<Erro> erros=new ArrayList<>();
    private static List<Simbolo> simbolos=new ArrayList<>();
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
    public static void addErro(String msg, int linha, int pos){
        erros.add(new Erro(msg,linha,pos));
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
    //Analise semantica
    public static List<Simbolo> getSimbolos()
    {
        return simbolos;
    }
    public static void addSimbolos(Simbolo simbolo){
        simbolos.add(simbolo);
    }
    
    public static void removeAllSimbolos(){
        simbolos.clear();
    }
    public static boolean simboloNaLista(String cadeia) {
        int i=0;
        while(i< simbolos.size() && !cadeia.equals(simbolos.get(i).getCadeia())) {
            i++;
        }
        if(i<simbolos.size())
            return true;
        else
            return false;
    }
    
    public static int getPosSimbolo(String cadeia) {
        int i=0;
        while(i< simbolos.size() && !cadeia.equals(simbolos.get(i).getCadeia())) {
            i++;
        }
        if(i<simbolos.size())
            return i;
        else
            return -1;
    }

    //Geracao codigo inetrmediario
    private static List<Tripla> triplas=new ArrayList<>();
     public static List<Tripla> getTriplas()
    {
        return triplas;
    }
    public static void addTripla(Tripla tripla){
        triplas.add(tripla);
    }
    
    public static void removeAllTriplas(){
        triplas.clear();
    }
    //geração do código otimizado
    private static List<Tripla> triplasOtimizadas=new ArrayList<>();
     public static List<Tripla> getTriplasOtimizadas()
    {
        return triplasOtimizadas;
    }
    public static void addTriplaOtimizada(Tripla tripla){
        triplasOtimizadas.add(tripla);
    }
    
    public static void removeAllTriplasOtimizadas(){
        triplasOtimizadas.clear();
    }
    
    //geração do código objeto
    private static List<ComandoObjeto> comandos=new ArrayList<>();
     public static List<ComandoObjeto> getComandos()
    {
        return comandos;
    }
    public static void addComando(ComandoObjeto comando){
        comandos.add(comando);
    }
    
    public static void removeAllComandos(){
        comandos.clear();
    }
    
    private Singleton()
    {
        //impede a instacia da classe
    }
}
