/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package netflix.sevilenTurler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import DatabaseConenction.Baglanti;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import netflix.FXMLDocumentController;

/**
 *
 * @author İbrahim Kafkaslı
 */
public class SevilenTur extends AnchorPane {
    
    
    @FXML
    private CheckBox aksiyonvemacera;

    @FXML
    private CheckBox bilimkurgu;

    @FXML
    private CheckBox romantik;

    @FXML
    private CheckBox drama;

    @FXML
    private CheckBox cocukveaile;

    @FXML
    private CheckBox belgesel;

    @FXML
    private CheckBox komedi;

    @FXML
    private CheckBox korku;

    @FXML
    private CheckBox bilimvedoga;

    @FXML
    private CheckBox gerilim;

    @FXML
    private CheckBox anime;

    @FXML
    private CheckBox reality;
        
    private List<CheckBox> kontrol_listesi;
            
    @FXML
    private Label uyari;
    
    private String Email;
    
    public SevilenTur() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SevilenTur.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        this.Email = Email;
    }
    
    @FXML
    private void initialize() {
        kontrol_listesi = new ArrayList<>();
        kontrol_listesi.add(aksiyonvemacera);
        kontrol_listesi.add(bilimkurgu);
        kontrol_listesi.add(romantik);
        kontrol_listesi.add(drama);
        kontrol_listesi.add(cocukveaile);
        kontrol_listesi.add(belgesel);
        kontrol_listesi.add(komedi);
        kontrol_listesi.add(korku);
        kontrol_listesi.add(bilimvedoga);
        kontrol_listesi.add(gerilim);
        kontrol_listesi.add(anime);
        kontrol_listesi.add(reality);
              
    }
    
    
    @FXML
    void nextButton(MouseEvent event) throws IOException {
        
            int sayac=0;
            for(int i=0;i<kontrol_listesi.size();i++)
            {
                if(kontrol_listesi.get(i).isSelected()==true)
                {
                    sayac++;
                }
            }
            if(sayac!=3)
            {
                uyari.setVisible(true);
            }
            else
            {
                
            	
            	
            	Baglanti database=new Baglanti();
            	
            	
                
                for(int i=0;i<kontrol_listesi.size();i++)
                {
                    if(kontrol_listesi.get(i).isSelected()==true)
                    {
                    	
                    	
                    	database.kullaniciSevilenTurKayit(FXMLDocumentController.email, i+1);
    
                    	
                    	
                    }
                }
                Parent root = FXMLLoader.load(netflix.Netflix.class.getResource("FXMLDocument.fxml"));
                Scene scene = new Scene(root);
                netflix.Netflix.stage.setScene(scene);
                uyari.setVisible(false);
            }
    }
}
