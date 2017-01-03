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
import java.util.Locale;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class Schnittstelle {
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/lagerverwaltung?autoReconnect=true&useSSL=false";

	// Database credentials
	static final String USER = "root";
	static final String PASS = "Watt3r";

	static Validation validation = new Validation();

	static ArrayList<Artikel> artikelen = new ArrayList<>();

	public static ArrayList<Artikel> datenbankverbindungSelect() {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			// STEP 2: Register JDBC driver
			Class.forName(JDBC_DRIVER);

			// STEP 3: Open a connection
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// STEP 4: Execute a query
			String sqlArtikel = "SELECT * FROM ARTIKEL";

			stmt = conn.prepareStatement(sqlArtikel);

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String bar = rs.getString("BARCODE");
				String bez = rs.getString("BEZEICHNUNG");
				int stueck = rs.getInt("STUECKZAHL");
				Date datum = rs.getDate("DATUM");
				Date aDatum = rs.getDate("ABLAUFDATUM");
				int preis = rs.getInt("PREIS");
				String kundennr = rs.getString("KUNDENNUMMER");

				DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
				String date = formatter.format(datum);
				String aDate = formatter.format(aDatum);

				artikelen.add(new Artikel(bar, bez, stueck, date, aDate, preis, kundennr, ""));
			}
			for (int i = 0; i < artikelen.size(); i++) {
				stmt = conn.prepareStatement("SELECT * FROM LIEFERANT WHERE KUNDENNUMMER = ?");
				stmt.setString(1, artikelen.get(i).getKundennummer());
				rs = stmt.executeQuery();
				if (rs.next()) {
					artikelen.get(i).setLieferant(rs.getString("NAME"));
				}
			}
			return artikelen;
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

	public static Artikel datenbankverbindungInsert(Artikel artikel) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			// STEP 2: Register JDBC driver
			Class.forName(JDBC_DRIVER);

			// STEP 3: Open a connection
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// STEP 4: Execute a query
			ps = conn.prepareStatement("SELECT * FROM LIEFERANT WHERE KUNDENNUMMER = ?");
			ps.setString(1, artikel.getKundennummer());
			ResultSet rs = ps.executeQuery();
			if (!rs.next()) {
				artikel = validation.showInfoLieferant(artikel);

				ps = conn.prepareStatement("INSERT INTO LIEFERANT ( KUNDENNUMMER, NAME ) VALUES ( ?, ?)");
				ps.setString(1, artikel.getKundennummer());
				ps.setString(2, artikel.getLieferant());
				ps.executeUpdate();
			} else {
				artikel.setLieferant(rs.getString("NAME"));
			}
			ps = conn.prepareStatement(
					"INSERT INTO ARTIKEL ( BARCODE, BEZEICHNUNG, STUECKZAHL, DATUM, ABLAUFDATUM, PREIS, KUNDENNUMMER ) VALUES ( ?, ?, ?, ?, ?, ?, ?)");
			ps.setString(1, artikel.getBarcode());
			ps.setString(2, artikel.getBezeichnung());
			ps.setInt(3, artikel.getStueckzahl());
			ps.setString(4, artikel.getDatum());
			ps.setString(5, artikel.getAblaufDatum());
			ps.setInt(6, artikel.getPreis());
			ps.setString(7, artikel.getKundennummer());

			ps.executeUpdate();
			return artikel;
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
		return artikel;
	}

	public static void datenbankverbindungInsertOnEdit(Artikel artikel) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
			java.util.Date date = formatter.parse(artikel.getAblaufDatum());
			formatter = new SimpleDateFormat("yyyy.MM.dd");
			String datum = formatter.format(date);
			
			// STEP 2: Register JDBC driver
			Class.forName(JDBC_DRIVER);

			// STEP 3: Open a connection
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// STEP 4: Execute a query
			ps = conn.prepareStatement(
					"UPDATE ARTIKEL SET BEZEICHNUNG = ?, STUECKZAHL = ?, ABLAUFDATUM = ?, PREIS = ?, KUNDENNUMMER = ? WHERE BARCODE = ?");
			ps.setString(1, artikel.getBezeichnung());
			ps.setInt(2, artikel.getStueckzahl());
			ps.setString(3, datum);
			ps.setInt(4, artikel.getPreis());
			ps.setString(5, artikel.getKundennummer());
			ps.setString(6, artikel.getBarcode());

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

	public static void datenbankverbindungInsertOnEditPrimary(Artikel artikel, String neu, String attribut) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			// STEP 2: Register JDBC driver
			Class.forName(JDBC_DRIVER);

			// STEP 3: Open a connection
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// STEP 4: Execute a query
			if (attribut.equalsIgnoreCase("BARCODE")) {
				ps = conn.prepareStatement("UPDATE ARTIKEL SET BARCODE = ? WHERE BARCODE = ?");
				ps.setString(1, neu);
				ps.setString(2, artikel.getBarcode());
			}
			// else {
			// ps = conn.prepareStatement(
			// "ALTER TABLE ARTIKEL DROP FOREIGN KEY KUNDENNUMMER;"
			// + "UPDATE ARTIKEL SET KUNDENNUMMER = ? WHERE KUNDENNUMMER = ?;"
			// + "UPDATE LIEFERANT SET KUNDENNUMMER = ? WHERE KUNDENNUMMER = ?;"
			// + "ALTER TABLE ARTIKEL ADD CONSTRAINT KUNDENNUMMER FOREIGN KEY
			// (KUNDENNUMMER) REFERENCES Lieferant(KUNDENNUMMER);");
			// ps.setString(1, neu);
			// ps.setString(2, artikel.getKundennummer());
			// ps.setString(3, neu);
			// ps.setString(4, artikel.getKundennummer());
			// }

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

	public static void datenbankverbindungDelete(Artikel artikel) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			// STEP 2: Register JDBC driver
			Class.forName(JDBC_DRIVER);

			// STEP 3: Open a connection
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// STEP 4: Execute a query
			ps = conn.prepareStatement("DELETE FROM ARTIKEL WHERE KUNDENNUMMER = ?");
			ps.setString(1, artikel.getKundennummer());
			ps.executeUpdate();

		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
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

	public static ArrayList<Artikel> datenbankverbindungSelectExpired() {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			ArrayList<Artikel> expired = new ArrayList<Artikel>();
			
			// STEP 2: Register JDBC driver
			Class.forName(JDBC_DRIVER);

			// STEP 3: Open a connection
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// STEP 4: Execute a query
			String sqlArtikel = "SELECT * FROM artikel WHERE ABLAUFDATUM > NOW() && ABLAUFDATUM < DATE_SUB(NOW(), INTERVAL -10 DAY)";
			stmt = conn.prepareStatement(sqlArtikel);

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String bar = rs.getString("BARCODE");
				String bez = rs.getString("BEZEICHNUNG");
				String kundennr = rs.getString("KUNDENNUMMER");


				expired.add(new Artikel(bar, bez, 0, "", "", 0, kundennr, ""));
			}
			for (int i = 0; i < expired.size(); i++) {
				stmt = conn.prepareStatement("SELECT * FROM LIEFERANT WHERE KUNDENNUMMER = ?");
				stmt.setString(1, artikelen.get(i).getKundennummer());
				rs = stmt.executeQuery();
				if (rs.next()) {
					expired.get(i).setLieferant(rs.getString("NAME"));
				}
			}
			return expired;
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
	
	public static Boolean datenbankverbindungSelectExpiredSingle(Artikel artikel) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			// STEP 2: Register JDBC driver
			Class.forName(JDBC_DRIVER);

			// STEP 3: Open a connection
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// STEP 4: Execute a query
			ps = conn.prepareStatement(
					"SELECT * FROM ARTIKEL WHERE ABLAUFDATUM > NOW() && ABLAUFDATUM < DATE_SUB(NOW(), INTERVAL -10 DAY) && BARCODE = ?;");
			ps.setString(1, artikel.getBarcode());

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				return true;
			}
			return false;
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
		return null;
		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		SINGLE EDIT KUNDENNUMMER NOCH NICHT MÖGLICH
//		
//		
//		
//		
//		
	}
}