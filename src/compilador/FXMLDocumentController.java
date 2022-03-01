/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import compilador.models.AnaliseLexica;
import compilador.models.AnaliseSintatica;
import compilador.models.Singleton;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class FXMLDocumentController implements Initializable {

    @FXML
    private Button btCompilar;
    @FXML
    private TextArea txCodigo;
    @FXML
    private Label lbLinha;
    
    private int linha = 2;
    @FXML
    private Label lbErro;

   
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lbLinha.setText(" 1"+"\n"+" ");
    }    

    @FXML
    private void evtCompilar(ActionEvent event) {
        System.out.println("Compilei");
        lbErro.setText("");
        Singleton.removeAllResultadoToken();
        Singleton.removeAllErros();
        AnaliseLexica al=new AnaliseLexica();
        al.receberPrograma(txCodigo.getText());
        List<String> erros=Singleton.getErros();
        
        AnaliseSintatica as=new AnaliseSintatica();
        if(erros.size()==0){
            as.receberPrograma();
            if(Singleton.getErros().size()==0){
                try
                {

                    Stage stage = new Stage();
                    Scene scene=new Scene(FXMLLoader.load(getClass().getResource("FXMLSaida.fxml")));
                    stage.setScene(scene);
                    stage.setTitle("Saída");
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.showAndWait();


                }
                catch(Exception e)
                {
                    System.out.println(e);
                }
            }
            else {
                lbErro.setText("");
                for(int i=0;i<erros.size();i++) {
                    lbErro.setText(lbErro.getText()+"\n"+erros.get(i));
                }
            }
        }
        else {
            lbErro.setText("");
            for(int i=0;i<erros.size();i++) {
                lbErro.setText(lbErro.getText()+"\n"+erros.get(i));
            }
        }
        
    }

    @FXML
    private void evtKeyPress(KeyEvent event) {
        if(event.getCode().equals(KeyCode.ENTER)){
            lbLinha.setText(lbLinha.getText()+linha+"\n"+" ");
            linha=linha+1;
        }
       
    }
    
}
