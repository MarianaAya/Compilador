
package compilador;

import compilador.models.Singleton;
import compilador.models.Token;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


public class FXMLSaidaController implements Initializable {

    @FXML
    private Button btFechar;
    @FXML
    private Label lbSaida;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<Token> resultado=Singleton.getTokensResultado();
        for(int i=0;i<resultado.size();i++){
            lbSaida.setText(lbSaida.getText()+"\n"+resultado.get(i).getCadeia()+" é "+resultado.get(i).getToken());
        }
    }    

    @FXML
    private void evtFechar(ActionEvent event) {
        btFechar.getScene().getWindow().hide();
    }
    
}
