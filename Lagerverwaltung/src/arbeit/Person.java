package arbeit;

public class Person {

	private String barcode;
	private String bezeichnung;
	private String stueckzahl;
	private String datum;
	private String ablaufdatum;

	public String getBarcode() {
		return barcode;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}

	public String getStueckzahl() {
		return stueckzahl;
	}

	public String getDatum() {
		return datum;
	}

	public String getAblaufdatum() {
		return ablaufdatum;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	public void setStueckzahl(String stueckzahl) {
		this.stueckzahl = stueckzahl;
	}

	public void setDatum(String datum) {
		this.datum = datum;
	}

	public void setAblaufdatum(String ablaufdatum) {
		this.ablaufdatum = ablaufdatum;
	}

	Person(String barcode, String bezeichnung, String stueckzahl, String datum, String ablaufdatum) {
		this.barcode = barcode;
		this.bezeichnung = bezeichnung;
		this.stueckzahl = stueckzahl;
		this.datum = datum;
		this.ablaufdatum = ablaufdatum;
	}

	public Person(String string, String string2) {
		// TODO Auto-generated constructor stub
	}

}