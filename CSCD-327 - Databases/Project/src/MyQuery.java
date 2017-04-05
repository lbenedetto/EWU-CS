/*****************************
 Query the University Database
 *****************************/

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MyQuery {

	private Connection conn = null;
	private Statement statement = null;
	private ResultSet resultSet = null;

	public MyQuery(Connection c) throws SQLException {
		conn = c;
		// Statements allow to issue SQL queries to the database
		statement = conn.createStatement();
	}

	public void findFall2009Students() throws SQLException {
		String query = "SELECT DISTINCT name FROM student NATURAL JOIN takes WHERE semester = \'Fall\' AND year = 2009;";

		resultSet = statement.executeQuery(query);
	}

	public void printFall2009Students() throws IOException, SQLException {
		System.out.println("******** Query 0 ********");
		System.out.println("name");
		while (resultSet.next()) {
			// It is possible to get the columns via name
			// also possible to get the columns via the column number which starts at 1
			String name = resultSet.getString(1);
			System.out.println(name);
		}
	}

	public void findGPAInfo() throws SQLException {
		String query = "SELECT DISTINCT id,name, (SUM(numerical_grade * credits) / SUM(credits)) AS 'GPA'";
		resultSet = statement.executeQuery(query);
	}

	public void printGPAInfo() throws IOException, SQLException {
		System.out.println("******** Query 1 ********");
		System.out.println("id  name    GPA");
		while (resultSet.next()) {
			// It is possible to get the columns via name
			// also possible to get the columns via the column number which starts at 1
			String id = resultSet.getString(1);
			String name = resultSet.getString(2);
			String GPA = resultSet.getString(3);
			System.out.println(id + " " + name + "    " + GPA);
		}
	}

	public void findMorningCourses() throws SQLException {
		String query = "";
		resultSet = statement.executeQuery(query);
	}

	public void printMorningCourses() throws IOException, SQLException {
		System.out.println("******** Query 2 ********");
	}

	public void findBusyInstructor() throws SQLException {
		String query = "";
		resultSet = statement.executeQuery(query);
	}

	public void printBusyInstructor() throws IOException, SQLException {
		System.out.println("******** Query 3 ********");
	}

	public void findPrereq() throws SQLException {
		String query = "";
		resultSet = statement.executeQuery(query);
	}

	public void printPrereq() throws IOException, SQLException {
		System.out.println("******** Query 4 ********");
	}

	public void updateTable() throws SQLException {

	}

	public void printUpdatedTable() throws IOException, SQLException {
		System.out.println("******** Query 5 ********");
	}

	public void findFirstLastSemester() throws SQLException {

	}

	public void printFirstLastSemester() throws IOException, SQLException {
		System.out.println("******** Query 6 ********");
	}

	public void findHeadCounts() throws SQLException {
		System.out.println("******** Query 7 ********");
	}
}
