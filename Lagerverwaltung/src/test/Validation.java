package test;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

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
	public Artikel showInfoLieferant(Artikel artikel) throws Exception {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Bestätigungsdialog");
		alert.setHeaderText("Die Kundennummer " + artikel.getKundennummer() + //
				" existiert nicht im Zusammenhang mit einem Lieferanten!");
		alert.setContentText("Möchten Sie einen neuen Lieferanten anlegen?");
		
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(this.getClass().getResource("icons/information.png").toString()));

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			return showLieferantEingabe(artikel);
		}
		throw new Exception("Der Lieferant " + artikel.getLieferant() + //
				" mit der Kundennumemr " + artikel.getKundennummer() + //
				" existiert nicht.");
	}
	
	public Artikel showLieferantEingabe(Artikel artikel) { 
		Dialog<Artikel> dialog = new Dialog<>();
		dialog.setTitle("Neuer Lieferant");
		dialog.setHeaderText("Vervollständigung der Angbaben");
		dialog.setContentText("Bitte geben Sie die Angaben des Lieferanten ein:");
		
		Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(this.getClass().getResource("icons/information.png").toString()));

		Label kundennummerLabel = new Label("Kundennummer: ");
		Label nameLabel = new Label("Name des Lieferanten: ");
		TextField kundennummerText = new TextField(artikel.getKundennummer());
		TextField nameText = new TextField(artikel.getLieferant());

		GridPane grid = new GridPane();
		grid.add(kundennummerLabel, 1, 1);
		grid.add(kundennummerText, 2, 1);
		grid.add(nameLabel, 1, 2);
		grid.add(nameText, 2, 2);
		dialog.getDialogPane().setContent(grid);

		ButtonType buttonTypeOk = new ButtonType("Okay", ButtonData.OK_DONE);
		ButtonType buttonTypeCancel = new ButtonType("Abbrechen", ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
		dialog.getDialogPane().getButtonTypes().add(buttonTypeCancel);

		dialog.setResultConverter(new Callback<ButtonType, Artikel>() {
			@Override
			public Artikel call(ButtonType b) {
				if (b == buttonTypeOk) {
					artikel.setKundennummer(kundennummerText.getText());
					artikel.setLieferant(nameText.getText());
				}
				if (b == buttonTypeCancel){
					artikel.setKundennummer(kundennummerText.getText());
					artikel.setLieferant(nameText.getText());
					return null;
				}
				return artikel;
			}
		});

		dialog.showAndWait().get();
		if ((artikel.getLieferant().isEmpty() || artikel.getKundennummer().isEmpty())) {
			showLieferantEingabe(artikel);
		}
		return artikel;
	}
}
