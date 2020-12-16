package netflix;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import DatabaseConenction.Baglanti;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import netflix.dinamik.Dinamik;

public class FXMLDocumentController implements Initializable {

    @FXML
    private TextField arama;

    @FXML
    private VBox vboxSol;

    @FXML
    private ImageView ortaResim;

    @FXML
    private Label izlenilensure;

    @FXML
    private VBox vboxSag;


    @FXML
    private TextField puan;

    @FXML
    private ComboBox<String> comboList;
    
    @FXML
    private Label filmAdi;
        
    public static String email;

    Baglanti database;
    public static int sure_baslangic;
    public static int sure_bitis;
    public static int tarih;
    public static int pid;
    public static int kid;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ObservableList<String> liste = FXCollections.observableArrayList("Aksiyon ve Macera", "Bilim Kurgu ve Fantastik Yapımlar", "Romantik",
                "Drama", "Çocuk ve Aile", "Belgesel", "Komedi", "Korku", "Bilim ve Doğa", "Gerilim", "Anime", "Reality Program", "Hepsi");

        comboList.setItems(liste);
        database = new Baglanti();
        kid = database.kullaniciIDBul(email);
        int maks_program_sayisi = database.maksProgramSayisi();
        izlenilensure.setText("İzlenilen Sure: "+database.sonKalinanSureBul(kid, pid));

        for (int i = 0; i < maks_program_sayisi; i++) {
            Dinamik dinamik = new Dinamik(i + 1);
            vboxSol.getChildren().add(dinamik);
        }

        int kid = database.kullaniciIDBul(FXMLDocumentController.email);
        int sevilen_turler[] = database.kullaniciSevilenTurTidBul(kid);
        int[] onerilen_programlar = new int[6];
        for (int i = 0; i < 6; i++) {
            onerilen_programlar[i] = 0;
        }
        for (int i = 0; i < 3; i++) {
            onerilen_programlar = database.kullaniciOnerilenFilmBul(onerilen_programlar, sevilen_turler[i]);
        }
        for (int i = 0; i < 6; i++) {
            Dinamik dinamik = new Dinamik(onerilen_programlar[i]);
            vboxSag.getChildren().add(dinamik);
        }
    }

    @FXML
    private void cikisButon(MouseEvent event) throws IOException {
        sure_bitis = (int) System.currentTimeMillis();
        database.kullaniciSureGuncelle(kid, pid);
        System.exit(0);
    }

    @FXML
    private void bastanSeyret(ActionEvent event) {
        sure_baslangic = (int) System.currentTimeMillis();
        database.kullaniciProgramIzle(kid, pid);
        database.kullaniciSureSifirla(kid, pid);
        izlenilensure.setText("Film izleniyor. Süreniz database e kayıt ediliyor");
        filmAdi.setText(Dinamik.film_adi);
        //ortaResim.setImage(Dinamik.resimImage);
    }

    @FXML
    private void oynat(ActionEvent event) {
        sure_baslangic = (int) System.currentTimeMillis();
        database.kullaniciProgramIzle(kid, pid);
        System.out.println("oynat butonuna tıklandı");
        izlenilensure.setText("Film izleniyor. Süreniz database e kayıt ediliyor");
        filmAdi.setText(Dinamik.film_adi);
        //ortaResim.setImage(Dinamik.resimImage);
    }

    @FXML
    private void puanOnay(ActionEvent event) {
        String kayit_et = puan.getText();
        database.kullaniciPuanGuncelle(kid, pid, Integer.parseInt(kayit_et));
        filmAdi.setText(Dinamik.film_adi);
        //ortaResim.setImage(Dinamik.resimImage);


    }

    @FXML
    private void Durdur(ActionEvent event) {
        sure_bitis = (int) System.currentTimeMillis();
        System.out.println("Durdurulma Tusuna Basildi");
        izlenilensure.setText("Sure: "+database.kullaniciSureGuncelle(kid, pid)+" dakika");
        filmAdi.setText(Dinamik.film_adi);
        //ortaResim.setImage(Dinamik.resimImage);
    }

    public void tam_program() {

        Baglanti database = new Baglanti();

        int maks_program_sayisi = database.maksProgramSayisi();

        for (int i = 0; i < maks_program_sayisi; i++) // sol tarafa tüm filmleri koyduk
        {
            Dinamik dinamik = new Dinamik(i+1);
            vboxSol.getChildren().add(dinamik);
        }

    }

    @FXML
    private void dinamikAra(KeyEvent event) {

        arama.setOnKeyPressed((e) -> {

            if (e.getCode().equals(KeyCode.ENTER)) {
                vboxSol.getChildren().forEach((t) -> {

                    Dinamik dinamik = (Dinamik) t;
                    if (dinamik.getFilmAdi().toLowerCase().equals(arama.getText().toLowerCase())) {
                        vboxSol.getChildren().clear();
                        vboxSol.getChildren().add(dinamik);
                    } else if (arama.getText().equals("")) {
                        vboxSol.getChildren().clear();
                        tam_program();
                    }

                });

            }

        });

    }


    private void turFiltreleme(String tur) {
        int[] programlar = new int[40];
        for (int i = 0; i < 40; i++) {
            programlar[i] = 0;
        }

        programlar = database.tureGoreFiltreleme(programlar, tur);

        int maks_program_sayisi = database.maksProgramSayisi();

        for (int i = 0; i < maks_program_sayisi; i++) // sol tarafa tüm filmleri koyduk
        {
            if (programlar[i] > 0) {
                Dinamik dinamik = new Dinamik(programlar[i]);
                vboxSol.getChildren().add(dinamik);
            }
            if (programlar[i] == 0) {
                break;
            }

        }

    }

    @FXML
    private void seciliTur(ActionEvent event) {
        String kullanici_girisi = comboList.getValue();
        if (kullanici_girisi.equals("Hepsi"))//burada tum turleri secmis oluyoruz
        {
            vboxSol.getChildren().clear();
            tam_program();
        } else {//bu kisimda ise filtreleme yapmis oluyoruz
            vboxSol.getChildren().clear();
            turFiltreleme(kullanici_girisi);
        }

    }
    
    public Image getOrtaResim() {
        return ortaResim.getImage();
    }

    public void setOrtaResim(Image ortaResim) {
        this.ortaResim.setImage(ortaResim);
    }

}
