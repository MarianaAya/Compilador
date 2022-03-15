
package compilador.models;

import java.util.ArrayList;
import java.util.List;

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
                        if(token==null){
                           List<Token> novos=new ArrayList<>();
                           novos = tab.tentativaToken(cadeias[j]);
                           
                           for(int t=0;t<novos.size();t++) {
                                Singleton.addResultadoToken(new Token(novos.get(t).getToken(),novos.get(t).getCadeia(),i+1,posCadeia));
                                if(novos.get(t).getToken().isEmpty())
                                    Singleton.addErro("Erro Léxico na Linha "+(i+1)+": Não foi possivel descobrir token da cadeia "+novos.get(t).getCadeia(), i+1);
  
                               
                           }
                           
                        } else{
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
