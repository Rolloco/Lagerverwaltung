package test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class Schnittstelle {
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/lagerverwaltung?";

	// Database credentials
	static final String USER = "root";
	static final String PASS = "Watt3r";

	static ArrayList<Artikel> personen = new ArrayList<>();

	public static ArrayList<Artikel> datenbankverbindungSelect() {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			// STEP 2: Register JDBC driver
			Class.forName(JDBC_DRIVER);

			// STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// STEP 4: Execute a query
			System.out.println("Creating database...");
			String sqlArtikel = "SELECT * FROM ARTIKEL";
			
			
			stmt = conn.prepareStatement(sqlArtikel);

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String bar = rs.getString("BARCODE");
				String bez = rs.getString("BEZEICHNUNG");
				String stueck = rs.getString("STUECKZAHL");
				Date datum = rs.getDate("DATUM");
				Date aDatum = rs.getDate("ABLAUFDATUM");
				String preis = rs.getString("PREIS");
				String kundennr = rs.getString("KUNDENNUMMER");

				DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
				String date = formatter.format(datum);
				String aDate = formatter.format(aDatum);

				personen.add(new Artikel(bar, bez, stueck, date, aDate, preis, kundennr, ""));
			}
			for (int i = 0; i < personen.size(); i++){
				stmt = conn.prepareStatement("SELECT * FROM LIEFERANT WHERE KUNDENNUMMER = ?");
				stmt.setString(1, personen.get(i).getKundennummer());
				rs = stmt.executeQuery();
				if (rs.next()) {
					personen.get(i).setLieferant(rs.getString("NAME"));
				}
			}
			return personen;
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return null;
	}

	public static Artikel datenbankverbindungInsert(Artikel person) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			// STEP 2: Register JDBC driver
			Class.forName(JDBC_DRIVER);

			// STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// STEP 4: Execute a query
			System.out.println("Creating database...");
			ps = conn.prepareStatement("SELECT * FROM LIEFERANT WHERE KUNDENNUMMER = ?");
			ps.setString(1, person.getKundennummer());
			ResultSet rs = ps.executeQuery();
			if (!rs.next()) {
				Validation validation = new Validation();
				Optional<String> lieferant = validation.showInfoLieferant(person);
				if (lieferant.get().length() != 0){
					person.setLieferant(lieferant.get());
					
					ps = conn.prepareStatement("INSERT INTO LIEFERANT ( KUNDENNUMMER, NAME ) VALUES ( ?, ?)");
					ps.setString(1, person.getKundennummer());
					ps.setString(2, lieferant.get());
					ps.executeUpdate();
					
				}
			} else {
				person.setLieferant(rs.getString("NAME"));
			}
			ps = conn.prepareStatement("INSERT INTO ARTIKEL ( BARCODE, BEZEICHNUNG, STUECKZAHL, DATUM, ABLAUFDATUM, PREIS, KUNDENNUMMER ) VALUES ( ?, ?, ?, ?, ?, ?, ?)");
			ps.setString(1, person.getBarcode());
			ps.setString(2, person.getBezeichnung());
			ps.setString(3, person.getStueckzahl());
			ps.setString(4, person.getDatum());
			ps.setString(5, person.getAblaufDatum());
			ps.setString(6, person.getPreis());
			ps.setString(7, person.getKundennummer());
			
			ps.executeUpdate();
			return person;
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException se2) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return person;
	}

	public static void datenbankverbindungInsertOnEdit(Artikel person) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			// STEP 2: Register JDBC driver
			Class.forName(JDBC_DRIVER);

			// STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// STEP 4: Execute a query
			System.out.println("Creating database...");
			ps = conn.prepareStatement("UPDATE ARTIKEL SET BARCODE = ?, BEZEICHNUNG = ?, STUECKZAHL = ?, ABLAUFDATUM = ?, PREIS = ?, KUNDENNUMMER = ? WHERE BARCODE = ?");
			ps.setString(1, person.getBarcode());
			ps.setString(2, person.getBezeichnung());
			ps.setString(3, person.getStueckzahl());
			ps.setString(4, person.getAblaufDatum());
			ps.setString(5, person.getPreis());
			ps.setString(6, person.getKundennummer());
			ps.setString(7, person.getBarcode());

			ps.executeUpdate();
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException se2) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}
}