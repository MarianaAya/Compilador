
package compilador;

import compilador.models.GeracaoCodigoIntermediario;
import compilador.models.Singleton;
import compilador.models.Token;
import compilador.models.Tripla;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


public class FXMLSaidaController implements Initializable {

    @FXML
    private Button btFechar;
    @FXML
    private Label lbSaida;
    @FXML
    private TableView<Token> tvToken;
    @FXML
    private TableColumn<Token, String> colToken;
    @FXML
    private TableColumn<Token, String> colCadeia;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        colToken.setCellValueFactory(new PropertyValueFactory<>("Token"));
        colCadeia.setCellValueFactory(new PropertyValueFactory<>("Cadeia"));

        List<Token> resultado=Singleton.getTokensResultado();
        tvToken.setItems(FXCollections.observableArrayList(resultado));
        
 
         GeracaoCodigoIntermediario g = new GeracaoCodigoIntermediario();
        g.gerar();
        List<Tripla> triplas = Singleton.getTriplas();
        for(int i=0;i<triplas.size();i++) {
            String texto = "Código: "+triplas.get(i).getCodigo()+ " Operador: "+triplas.get(i).getOperador()+" Operando1: "+triplas.get(i).getOperando1()+
                    " Operando2: "+triplas.get(i).getOperando2()+"\n";
            System.out.println(""+texto);
        }
                
        
    }    

    @FXML
    private void evtFechar(ActionEvent event) {
        btFechar.getScene().getWindow().hide();
    }
    
}
