
package compilador.models;

public class AnaliseLexica {
   
    public void receberPrograma(String text) {
        TabelaToken tab=new TabelaToken();
        Token token=null;
        String linhas[]=text.split("\n");
        String cadeias[];
     
        int j=0,i=0;
        while(i<linhas.length){
            if(linhas[i].length()>0) {
               
                cadeias=linhas[i].split(" ");
                j=0;
                while(j<cadeias.length){
                  
                    cadeias[j]=cadeias[j].replace("\t","");
                    if(cadeias[j].length()>0){
                        //pegar o index da cadeia na linha
                        int posCadeia = linhas[i].indexOf(cadeias[j]);

                        token=tab.descobrirToken(cadeias[j]);
                        if(token==null)
                           Singleton.addErro("Erro Léxico na Linha "+(i+1)+": Não foi possivel descobrir token da cadeia "+cadeias[j], i+1);
                        else{
                            Singleton.addResultadoToken(new Token(token.getToken(),cadeias[j],i+1,posCadeia));
                        }
                    }

                    j++;
                }
            }
            
            i++;
        }
        
    }
    
   
}
