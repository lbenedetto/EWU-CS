/*
 Tester for the project
 By: Dan Li
 */

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestMyQuery {
	public static void main(String[] args) {
		try {
			Connection conn = getConnection();
			MyQuery mquery = new MyQuery(conn);

			// Query 0
			mquery.findFall2009Students();
			mquery.printFall2009Students();

			// Query 1
			mquery.findGPAInfo();
			mquery.printGPAInfo();

			// Query 2
			mquery.findMorningCourses();
			mquery.printMorningCourses();

			// Query 3
			mquery.findBusyInstructor();
			mquery.printBusyInstructor();

			// Query 4
			mquery.findPrereq();
			mquery.printPrereq();

			// Query 5
			mquery.updateTable();
			mquery.printUpdatedTable();

			// Query 6
			mquery.findFirstLastSemester();
			mquery.printFirstLastSemester();

			// Query 7
			mquery.findHeadCounts();

			conn.close();
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() throws SQLException {
		Connection connection;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		//Create a connection to the database

		String serverName = "146.187.134.44:3306";
		String mydatabase = "DBUsername_4"; //change needed
		String url = "jdbc:mysql://" + serverName + "/" + mydatabase; // a JDBC url
		String username = "w17benedetto"; //change needed
		String password = "newpassword"; //change needed
		connection = DriverManager.getConnection(url, username, password);
		return connection;
	}
}
