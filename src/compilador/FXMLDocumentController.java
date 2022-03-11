/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import compilador.models.AnaliseLexica;
import compilador.models.AnaliseSintatica;
import compilador.models.Singleton;
import java.io.File;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
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

   
    private File file_selecionado=null;
    @FXML
    private MenuItem miFechar;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lbLinha.setText(" 1"+"\n"+" ");
        miFechar.setDisable(true);
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
            as.receberPrograma(txCodigo);
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

    @FXML
    private void evtAbrir(ActionEvent event) {
        
        
        FileChooser fc=new FileChooser();
        fc.setTitle("Abrir arquivo");
        File file=fc.showOpenDialog(txCodigo.getScene().getWindow());
        file_selecionado=file;
        String codigo="";
        try
        {
            lbLinha.setText("");
            lbErro.setText("");
            txCodigo.setText("");
            RandomAccessFile arq=new RandomAccessFile(file,"r" );
            byte[] conteudo=new byte[(int)arq.length()];
            arq.readFully(conteudo);
            codigo=new String(conteudo);
            txCodigo.setText(codigo);
            corrigirLabel(codigo);
            miFechar.setDisable(false);
            
            arq.close();
        }
        catch(Exception e)
        {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Erro ao abrir o arquivo");
            alert.setContentText(e.getMessage());
            lbLinha.setText(" 1"+"\n"+" ");
        }
        
    }
    private void corrigirLabel(String codigo) {
        linha=2;
        lbLinha.setText(" 1"+"\n"+" ");
        for(int i=0;i<codigo.length();i++) {
            if(codigo.charAt(i)== '\n') {
                lbLinha.setText(lbLinha.getText()+linha+"\n"+" ");
                linha=linha+1;
                
            }
        }
   
                
    }
    @FXML
    private void evtSalvar(ActionEvent event) {
        if(file_selecionado==null){
            FileChooser c=new FileChooser();
            c.setTitle("Salvar arquivo");
            File file=c.showSaveDialog(txCodigo.getScene().getWindow());
            RandomAccessFile arq;
            try
            {
                if(file!=null)
                {
                    arq=new RandomAccessFile(file,"rw");
                    arq.setLength(0);
                    arq.writeBytes(txCodigo.getText());
                    arq.close();
                    file_selecionado=null;
                }

            }
            catch(Exception e)
            {
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Erro ao salvar");
                alert.setContentText(e.getMessage());
            }
        }
        else {
            salvar();
            
        }
    }
    public void salvar() {
        try
        {
            RandomAccessFile arq=new RandomAccessFile(file_selecionado,"rw");
            byte[] conteudo=new byte[(int)arq.length()];
            arq.readFully(conteudo);
            if(!txCodigo.getText().equals(new String(conteudo)))
            {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmar para salvar");
                alert.setHeaderText("Autorização para salvar");
                alert.setContentText("Salvar as alterações?");

                Optional<ButtonType> result = alert.showAndWait();
                if(result.get()==ButtonType.OK)
                {
                    arq.setLength(0);
                    arq.writeBytes(txCodigo.getText());
                }
            }
            arq.close();
        }
        catch(Exception e)
        {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Erro ao abrir o arquivo");
            alert.setContentText(e.getMessage());
        }

        file_selecionado=null;
    }
    @FXML
    private void evtFechar(ActionEvent event) {
        if(file_selecionado!=null) {
            lbLinha.setText(" 1"+"\n"+" ");
            lbErro.setText("");
            txCodigo.setText("");
            miFechar.setDisable(true);
            file_selecionado=null;
        }
        else {
            salvar();
        }
    }

    @FXML
    private void evtAumentar(ActionEvent event) {
        Font fonte = txCodigo.getFont();
        txCodigo.setFont(new Font(fonte.getSize()+1));
        
        Font fonte2 = lbLinha.getFont();
        lbLinha.setFont(new Font(fonte2.getSize()+1));
    }

    @FXML
    private void evtDiminuir(ActionEvent event) {
        Font fonte = txCodigo.getFont();
        txCodigo.setFont(new Font(fonte.getSize()-1));
        
        Font fonte2 = lbLinha.getFont();
        lbLinha.setFont(new Font(fonte2.getSize()-1));
    }
    
}
