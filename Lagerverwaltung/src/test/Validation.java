package test;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Validation {
	
	private static String errorTextBefore;
	private static String errorTextAfter;

	public Validation(Person person) {
	}
	
	public static void validate(Person person) throws Exception{
		errorTextBefore = "Das Feld ";
		errorTextAfter = " darf nicht leer sein!";
		if (person.getBarcode().isEmpty()){
			showError(errorTextBefore + "Barcode" + errorTextAfter);
		} else if (person.getBezeichnung().isEmpty()){
			showError(errorTextBefore + "Bezeichnung" + errorTextAfter);
		} else if (person.getPreis().isEmpty()){
			showError(errorTextBefore + "Preis" + errorTextAfter);
		} else if (person.getKundennummer().isEmpty()){
			showError(errorTextBefore + "Kundennummer" + errorTextAfter);
		} else if (person.getAblaufDatum().isEmpty()){
			showError(errorTextBefore + "Ablaufdatum" + errorTextAfter);
		} 
	}
	
	private static void showError(String error) throws Exception{
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error Dialog");
		alert.setHeaderText("Es ist ein Fehler aufgetreten!");
		alert.setContentText(error);

		alert.showAndWait();
		
		throw new Exception("Das Feld " + error + " ist leer.");
	}
}
