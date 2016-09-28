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

public class Schnittstelle {
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/lagerverwaltung?";

	// Database credentials
	static final String USER = "root";
	static final String PASS = "Watt3r";

	static ArrayList<Person> personen = new ArrayList<>();

	public static ArrayList<Person> datenbankverbindungSelect() {
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
			String sql = "SELECT * FROM ARTIKEL";
			stmt = conn.prepareStatement(sql);

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

				personen.add(new Person(bar, bez, stueck, date, aDate, preis, kundennr));
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

	public static void datenbankverbindungInsert(Person person) {
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
			ps = conn.prepareStatement("INSERT INTO ARTIKEL ( BARCODE, BEZEICHNUNG, STUECKZAHL, DATUM, ABLAUFDATUM, PREIS, KUNDENNUMMER ) VALUES ( ?, ?, ?, ?, ?, ?, ?)");
			ps.setString(1, person.getBarcode());
			ps.setString(2, person.getBezeichnung());
			ps.setString(3, person.getStueckzahl());
			ps.setString(4, person.getDatum());
			ps.setString(5, person.getAblaufDatum());
			ps.setString(6, person.getPreis());
			ps.setString(7, person.getKundennummer());

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

	public static void datenbankverbindungInsertOnEdit(Person person) {
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
