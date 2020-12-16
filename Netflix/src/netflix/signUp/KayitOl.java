
package netflix.signUp;

import java.io.IOException;
import java.time.LocalDate;

import DatabaseConenction.Baglanti;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import netflix.FXMLDocumentController;
import netflix.sevilenTurler.SevilenTur;

public class KayitOl extends AnchorPane {

	@FXML
	private TextField Name;

	@FXML
	private TextField Email;

	@FXML
	private PasswordField password;

	@FXML
	private DatePicker dogumTarihi;

	@FXML
	private Label uyari;

	public KayitOl() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("KayitOl.fxml"));
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
	void signUp(ActionEvent event) {
		if (isControl()) {
			uyari.setText("");
			LocalDate date = dogumTarihi.getValue();
			boolean kontrol = true;

			
			Baglanti database = new Baglanti();
			kontrol = database.kayitEmailKontrol(Email.getText());
			

			if (kontrol) {

				
				
				
				database.kayitEt(Email.getText(), Name.getText(), password.getText(), date.toString());
				FXMLDocumentController.email=Email.getText();
				
				
				uyari.setVisible(false);
				SevilenTur tur = new SevilenTur();
				getScene().setRoot(tur);
				
				
			} else {
				uyari.setText("Böyle Bir Hesap Zaten Var!");
				uyari.setVisible(true);
			}

		} else {
			uyari.setText("Lütfen Boş Alan Bırakmayınız!");
			uyari.setVisible(true);
		}
	}

	private boolean isControl() {
		return Name.getText() != null && !"".equals(Name.getText()) && Email.getText() != null
				&& !"".equals(Email.getText()) && password.getText() != null && !"".equals(password.getText())
				&& dogumTarihi.getValue() != null;
	}
}
