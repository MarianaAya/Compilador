package compilador;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import compilador.models.ComandoMaquina;
import compilador.models.GeracaoCodigoObjeto;
import compilador.models.Singleton;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class FXMLCodigoObjetoController implements Initializable {

    @FXML
    private Label lbResultado;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         
        List<ComandoMaquina> listaComandos = Singleton.getComandos();
        String texto="";
        for(int i=0;i<listaComandos.size();i++) {
            texto+=listaComandos.get(i)+"\n";
            
        }
        lbResultado.setText(texto);
    }    

    @FXML
    private void evtFechar(ActionEvent event) {
        lbResultado.getScene().getWindow().hide();
    }
    
}
