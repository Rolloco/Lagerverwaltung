package test;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Validation {

	private static String errorTextBefore;
	private static String errorTextAfter;

	public Validation() {
	}

	public void validate(Artikel person) throws Exception {
		errorTextBefore = "Das Feld ";
		errorTextAfter = " darf nicht leer sein!";
		if (person.getBarcode().isEmpty()) {
			showError(errorTextBefore + "Barcode" + errorTextAfter);
		} else if (person.getBezeichnung().isEmpty()) {
			showError(errorTextBefore + "Bezeichnung" + errorTextAfter);
		} else if (person.getPreis().isEmpty()) {
			showError(errorTextBefore + "Preis" + errorTextAfter);
		} else if (person.getKundennummer().isEmpty()) {
			showError(errorTextBefore + "Kundennummer" + errorTextAfter);
		} else if (person.getAblaufDatum().isEmpty()) {
			showError(errorTextBefore + "Ablaufdatum" + errorTextAfter);
		}
	}

	private void showError(String error) throws Exception {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error Dialog");
		alert.setHeaderText("Es ist ein Fehler aufgetreten!");
		alert.setContentText(error);

		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(this.getClass().getResource("icons/alert.png").toString()));

		alert.showAndWait();

		throw new Exception(error);
	}

	/**
	 * Asks the user if he wants to add the described Supplier
	 * 
	 * @return 0 = OK
	 * @return 1 = No
	 * Initializes the table columns and sets up sorting and filtering.
	 */
	public Optional<String> showInfoLieferant(Artikel artikel) throws Exception {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText("Der Lieferant " + artikel.getLieferant() + //
				" mit der Kundennumemr " + artikel.getKundennummer() + //
				" existiert nicht!");
		alert.setContentText("Möchten Sie Ihn anlegen?");
		
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(this.getClass().getResource("icons/information.png").toString()));

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			TextInputDialog dialog = new TextInputDialog("GmBH");
			dialog.setTitle("Text Input Dialog");
			dialog.setHeaderText("Vervollständigung der Angbaben");
			dialog.setContentText("Bitte geben Sie den Namen des Lieferanten ein:");

			stage = (Stage) dialog.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("icons/information.png").toString()));
			
			return dialog.showAndWait();
		}
		throw new Exception("Der Lieferant " + artikel.getLieferant() + //
				" mit der Kundennumemr " + artikel.getKundennummer() + //
				" existiert nicht.");
	}
}
