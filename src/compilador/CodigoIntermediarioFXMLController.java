package compilador;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class CodigoIntermediarioFXMLController implements Initializable {

    @FXML
    private TableView<Tripla> tvCodInter1;
    @FXML
    private TableColumn<Tripla, Integer> colCod1;
    @FXML
    private TableColumn<Tripla, String> colOperador1;
    @FXML
    private TableColumn<Tripla, String> colOperando1;
    @FXML
    private TableColumn<Tripla, String> colOperando2;
    @FXML
    private TableView<Tripla> tvCodInter2;
    @FXML
    private TableColumn<Tripla, Integer> colCod2;
    @FXML
    private TableColumn<Tripla, String> colOperador2;
    @FXML
    private TableColumn<Tripla, String> colOperand1;
    @FXML
    private TableColumn<Tripla, String> colOperand2;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colCod1.setCellValueFactory(new PropertyValueFactory<>("Codigo"));
        colOperador1.setCellValueFactory(new PropertyValueFactory<>("Operador"));
        colOperando1.setCellValueFactory(new PropertyValueFactory<>("Operando1"));
        colOperando2.setCellValueFactory(new PropertyValueFactory<>("Operando2"));
        List<Tripla> resultado=Singleton.getTriplas();
        tvCodInter1.setItems(FXCollections.observableArrayList(resultado));
        
        colCod2.setCellValueFactory(new PropertyValueFactory<>("Codigo"));
        colOperador2.setCellValueFactory(new PropertyValueFactory<>("Operador"));
        colOperand1.setCellValueFactory(new PropertyValueFactory<>("Operando1"));
        colOperand2.setCellValueFactory(new PropertyValueFactory<>("Operando2"));
        List<Tripla> resultado2=Singleton.getTriplasOtimizadas();
        tvCodInter2.setItems(FXCollections.observableArrayList(resultado2));
    }    

    @FXML
    private void evtFechar(ActionEvent event) {
        tvCodInter1.getScene().getWindow().hide();
    }
    
}
