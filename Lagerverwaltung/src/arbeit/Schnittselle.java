package arbeit;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Schnittselle {
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "oracle.jdbc.OracleDriver";
	static final String DB_URL = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=vhvxora001.hv.devk.de)(PORT=1521))(CONNECT_DATA=(SERVICE_NAME=centsp_t.hv.devk.de)))";

	// Database credentials
	static final String USER = "TTCENTSP";
	static final String PASS = "kLjO0mBmmC";

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
			String sql = "SELECT * FROM LAGER";
			stmt = conn.prepareStatement(sql);

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String bar = rs.getString("BARCODE");
				String bez = rs.getString("BEZEICHNUNG");
				String stueck = rs.getString("STUECKZAHL");
				Date datum = rs.getDate("DATUM");
				Date aDatum = rs.getDate("ABLAUFDATUM");

				DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
				String date = formatter.format(datum);
				String aDate = formatter.format(aDatum);

				personen.add(new Person(bar, bez, stueck, date, aDate));
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
			}// nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}// end finally try
		}// end try
		System.out.println("Goodbye!");
		return null;
	}// end main

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
			ps = conn.prepareStatement("INSERT INTO LAGER ( BARCODE, BEZEICHNUNG, STUECKZAHL, DATUM, ABLAUFDATUM ) VALUES ( ?, ? , ?, ?, ?)");
			ps.setString(1, person.getBarcode());
			ps.setString(2, person.getBezeichnung());
			ps.setString(3, person.getStueckzahl());
			ps.setString(4, person.getDatum());
			ps.setString(5, person.getAblaufdatum());

			ps.executeQuery();
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException se2) {
			}// nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}// end finally try
		}// end try
		System.out.println("Goodbye!");
	}// end main

	public static ArrayList<Person> datenbankverbindungSearch(String filter, String kategorie) {
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
			String sql = "SELECT * FROM LAGER WHERE ? LIKE ?'%'";
			ps = conn.prepareStatement(sql);
			ps.setString(1, kategorie);
			ps.setString(2, filter);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String bar = rs.getString("BARCODE");
				String bez = rs.getString("BEZEICHNUNG");
				String stueck = rs.getString("STUECKZAHL");
				Date datum = rs.getDate("DATUM");
				Date aDatum = rs.getDate("ABLAUFDATUM");

				DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
				String date = formatter.format(datum);
				String aDate = formatter.format(aDatum);

				personen.add(new Person(bar, bez, stueck, date, aDate));
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
				if (ps != null)
					ps.close();
			} catch (SQLException se2) {
			}// nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}// end finally try
		}// end try
		System.out.println("Goodbye!");
		return null;
	}// end main
}// end JDBCExample

