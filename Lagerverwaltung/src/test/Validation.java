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
		} else if (!person.getPreis().matches("([0-9]*[.,])?[0-9]+")) {
			showError("Der Preis muss eine Zahl sein!");
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
	 * @return Name of the supplier
	 * Initializes the table columns and sets up sorting and filtering.
	 */
	public String showInfoLieferant(Artikel artikel) throws Exception {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Bestätigungsdialog");
		alert.setHeaderText("Die Kundennummer " + artikel.getKundennummer() + //
				" existiert nicht im Zusammenhang mit einem Lieferanten!");
		alert.setContentText("Möchten Sie einen neuen Lieferanten anlegen?");
		
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(this.getClass().getResource("icons/information.png").toString()));

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			showLieferantEingabe(artikel);
		}
		throw new Exception("Der Lieferant " + artikel.getLieferant() + //
				" mit der Kundennumemr " + artikel.getKundennummer() + //
				" existiert nicht.");
	}
	
	public String showLieferantEingabe(Artikel artikel) { 
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Name des Lieferanten");
		dialog.setHeaderText("Vervollständigung der Angbaben");
		dialog.setContentText("Bitte geben Sie den Namen des Lieferanten ein:");

		Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(this.getClass().getResource("icons/information.png").toString()));
		
		String lieferant = dialog.showAndWait().get();
		if (lieferant.length() > 0) {
			return lieferant;
		} else {
			showLieferantEingabe(artikel);
		}
		return null;
	}
}
