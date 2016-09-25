package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
				String datum = rs.getString("DATUM");
				String aDatum = rs.getString("ABLAUFDATUM");
				String preis = rs.getString("PREIS");
				String kundennr = rs.getString("KUNDENNUMMER");
				// Person person = new Person(pnr, aa, datum);
				personen.add(new Person(bar, bez, stueck, datum, aDatum, preis, kundennr));
				// data.add(person);
			}
			return personen;
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			} // nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			} // end finally try
		} // end try
		System.out.println("Goodbye!");
		return null;
	}// end main
}
//
// public static Person datenbankverbindungInsert(String pnr, String aa, String
// datum) {
// Connection conn = null;
// PreparedStatement ps = null;
// try {
// // STEP 2: Register JDBC driver
// Class.forName(JDBC_DRIVER);
//
// // STEP 3: Open a connection
// System.out.println("Connecting to database...");
// conn = DriverManager.getConnection(DB_URL, USER, PASS);
//
// // STEP 4: Execute a query
// System.out.println("Creating database...");
// ps = conn.prepareStatement("INSERT INTO CENTSPENDE ( PERSONALNUMMER,
// ARBEITSANTEIL, DATUM ) VALUES ( ?, ? , ?)");
// ps.setString(1, pnr);
// ps.setString(2, aa);
// ps.setString(3, datum);
//
// ps.executeQuery();
// return new Person(pnr, aa, datum);
// } catch (SQLException se) {
// // Handle errors for JDBC
// se.printStackTrace();
// } catch (Exception e) {
// // Handle errors for Class.forName
// e.printStackTrace();
// } finally {
// // finally block used to close resources
// try {
// if (ps != null)
// ps.close();
// } catch (SQLException se2) {
// }// nothing we can do
// try {
// if (conn != null)
// conn.close();
// } catch (SQLException se) {
// se.printStackTrace();
// }// end finally try
// }// end try
// System.out.println("Goodbye!");
// return null;
// }// end main
//
// public static ArrayList<Person> datenbankverbindungSearch(String filter,
// String kategorie) {
// Connection conn = null;
// PreparedStatement ps = null;
// try {
// // STEP 2: Register JDBC driver
// Class.forName(JDBC_DRIVER);
//
// // STEP 3: Open a connection
// System.out.println("Connecting to database...");
// conn = DriverManager.getConnection(DB_URL, USER, PASS);
//
// // STEP 4: Execute a query
// System.out.println("Creating database...");
// String sql = "SELECT * FROM CENTSPENDE WHERE ? LIKE ?'%'";
// ps = conn.prepareStatement(sql);
// ps.setString(1, kategorie);
// ps.setString(2, filter);
//
// ResultSet rs = ps.executeQuery();
// while (rs.next()) {
// String pnr = rs.getString("PERSONALNUMMER");
// String aa = rs.getString("ARBEITSANTEIL");
// String datum = rs.getString("DATUM");
// // Person person = new Person(pnr, aa, datum);
// personen.add(new Person(pnr, aa, datum));
// // data.add(person);
// System.out.println("Personalnummer: " + pnr);
// System.out.println("Arbeitsanteil: " + aa);
// System.out.println("Datum: " + datum);
// }
// return personen;
// } catch (SQLException se) {
// // Handle errors for JDBC
// se.printStackTrace();
// } catch (Exception e) {
// // Handle errors for Class.forName
// e.printStackTrace();
// } finally {
// // finally block used to close resources
// try {
// if (ps != null)
// ps.close();
// } catch (SQLException se2) {
// }// nothing we can do
// try {
// if (conn != null)
// conn.close();
// } catch (SQLException se) {
// se.printStackTrace();
// }// end finally try
// }// end try
// System.out.println("Goodbye!");
// return null;
// }// end main
// }// end JDBCExample
