package test;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Simple model class for the person table.
 * 
 * @author Marco Jakob
 */
public class Artikel {

	private StringProperty barcode;
	private StringProperty bezeichnung;
	private IntegerProperty stueckzahl; 
	private StringProperty datum;
	private StringProperty ablaufDatum;
	private IntegerProperty preis;
	private StringProperty kundennummer;
	private StringProperty lieferant;

	public Artikel(String barcode, String bezeichnung, int stueckzahl, String datum,
			String ablaufDatum, int preis, String kundennummer, String lieferant) {
		this.barcode = new SimpleStringProperty(barcode);
		this.bezeichnung = new SimpleStringProperty(bezeichnung);
		this.stueckzahl = new SimpleIntegerProperty(stueckzahl);
		this.datum = new SimpleStringProperty(datum);
		this.ablaufDatum = new SimpleStringProperty(ablaufDatum);
		this.preis = new SimpleIntegerProperty(preis);
		this.kundennummer = new SimpleStringProperty(kundennummer);
		this.lieferant = new SimpleStringProperty(lieferant);
	}

	public String getBarcode() {
		return barcode.get();
	}

	public void setBarcode(String barcode) {
		this.barcode.set(barcode);
	}
	
	public StringProperty barcodeProperty() {
		return barcode;
	}

	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung.set(bezeichnung);
	}
	
	public StringProperty bezeichnungProperty() {
		return bezeichnung;
	}

	public String getBezeichnung() {
		return bezeichnung.get();
	}

	public int getStueckzahl() {
		return stueckzahl.get();
	}

	public String getDatum() {
		return datum.get();
	}

	public String getAblaufDatum() {
		return ablaufDatum.get();
	}

	public int getPreis() {
		return preis.get();
	}

	public String getKundennummer() {
		return kundennummer.get();
	}

	public void setStueckzahl(int stueckzahl) {
		this.stueckzahl.set(stueckzahl);
	}
	
	public void setDatum(String datum) {
		this.datum.set(datum);
	}
	
	public void setAblaufDatum(String ablaufDatum) {
		this.ablaufDatum.set(ablaufDatum);
	}
	
	public void setPreis(int preis) {
		this.preis.set(preis);
	}
	
	public void setKundennummer(String kundennummer) {
		this.kundennummer.set(kundennummer);
	}
	
	public IntegerProperty stueckzahlProperty() {
		return stueckzahl;
	}
	
	public StringProperty datumProperty() {
		return datum;
	}
	
	public StringProperty ablaufDatumProperty() {
		return ablaufDatum;
	}
	
	public IntegerProperty preisProperty() {
		return preis;
	}
	
	public StringProperty kundennummerProperty() {
		return kundennummer;
	}
	
	public String getLieferant() {
		return lieferant.get();
	}

	public void setLieferant(String lieferant) {
		this.lieferant.set(lieferant);
	}
	
	public StringProperty lieferantProperty() {
		return lieferant;
	}
}