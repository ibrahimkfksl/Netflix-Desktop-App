
package netflix.dinamik;

import java.io.File;
import java.io.IOException;
import DatabaseConenction.Baglanti;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import netflix.FXMLDocumentController;

public class Dinamik extends HBox {

	@FXML
	private ImageView resim;

	@FXML
	private Label puan;

	@FXML
	private Label filmAdi;

	@FXML
	private Label sure;

	@FXML
	private Label bolumSayisi;

	@FXML
	private Label filmTur;

	private Image image;
	private int pid;

        public static String film_adi;
        public static Image resimImage;
        
	public Dinamik(int pid) {
                film_adi="";
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Dinamik.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}

		  

		
		Baglanti database = new Baglanti();
		
		this.pid=pid;

		String tmp = String.valueOf(database.programPuaniniBul(pid));
		puan.setText("Puan:" + tmp);

		filmAdi.setText(database.programIsmıBul(pid));

		tmp = String.valueOf(database.programSuresiBul(pid));
		sure.setText(tmp + " dakika");

		tmp = String.valueOf(database.programBolumSayisiBul(pid));
		bolumSayisi.setText(tmp + " Bolum");

		File file = new File(database.programResimVeriYoluBul(pid));
		image = new Image(file.toURI().toString());
		resim.setImage(image);

		filmTur.setText(database.programTuruBul(pid));

		  
		this.setOnMouseClicked((event) -> {

			VBox list = (VBox) this.getParent();
			list.getChildren().forEach((t) -> {
				Dinamik dinamik = (Dinamik) t;
				dinamik.getStyleClass().clear();
				dinamik.getStyleClass().add("hboxNonSelection");
			});

			this.getStyleClass().add("hboxSelection");     
			FXMLDocumentController.pid=this.pid;
                        film_adi=this.filmAdi.getText();
                        this.resimImage=this.image;
			
		});

	}

	public String getFilmAdi() {
		return filmAdi.getText();
	}

	public void setFilmAdi(Label filmAdi) {
		this.filmAdi = filmAdi;
	}

        public String getSure() {
            return sure.getText();
        }

        
	@FXML
	private void initialize() {
	}

}
