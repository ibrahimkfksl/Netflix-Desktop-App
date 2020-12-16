
package netflix.Login;

import java.io.IOException;

import DatabaseConenction.Baglanti;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import netflix.FXMLDocumentController;
import netflix.signUp.KayitOl;


public class Login extends AnchorPane {
    
    
    @FXML
    private TextField userName;

    @FXML
    private PasswordField password;

    @FXML
    private Label uyari;
    
    @FXML
    private CheckBox beniHatirla;
    
    public Login() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    @FXML
    private void initialize() {
       
    }
    
    
    @FXML
    void login(ActionEvent event) throws IOException {
        
        if(isControl())
        {
           
            boolean control=true;
            
            
            
            Baglanti database=new Baglanti();
            
            control=database.girisYap(userName.getText(), password.getText());
            

            
            if(control==true)
            {
            	
            	
            	FXMLDocumentController.email=userName.getText();
            	
                boolean hatirla = beniHatirla.isSelected();
                if(hatirla==true)
                {
                    userName.setText(userName.getText());
                    password.setText(password.getText());
                }
                //bir sonraki ekranı çağırma kısmı 
                uyari.setVisible(false);
                Parent root = FXMLLoader.load(netflix.Netflix.class.getResource("FXMLDocument.fxml"));
                Scene scene = new Scene(root);
                netflix.Netflix.stage.setScene(scene);
                
            }
            else
            {
                mesaj("Böyle bir kullanıcı bulunmamaktadır!");
            }
        }
        else
        {
            mesaj("Lütfen tüm alanları doldurunuz!");
        }
    }
    
    private boolean isControl()
    {
        return (userName.getText()!=null) && !"".equals(userName.getText()) && (password.getText()!=null) && !"".equals(password.getText());
    }
    
    private void mesaj(String mesaj)
    {
        uyari.setText(mesaj);
        uyari.setVisible(true);
    }
    
    @FXML
    void kayitOl(MouseEvent event) {
         KayitOl kayit = new KayitOl();
         getScene().setRoot(kayit);
         
    }
    
}
