/*****************************
 Query the University Database
 *****************************/

import java.io.*;
import java.sql.*;
import java.util.Date;
import java.lang.String;
import java.util.Scanner;

//@SuppressWarnings("ALL")
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
		String q1 = "CREATE TEMPORARY TABLE takes1 SELECT * FROM takes;";
		String q2 = "ALTER TABLE takes1 ADD COLUMN num_grade VARCHAR(4);";
		String q3 = "UPDATE takes1 SET num_grade = CASE " +
				"WHEN grade = 'A' THEN 4.0 " +
				"WHEN grade = 'A-' THEN 3.67 " +
				"WHEN grade = 'B+' THEN 3.33 " +
				"WHEN grade = 'B' THEN 3 " +
				"WHEN grade = 'B-' THEN 2.67 " +
				"WHEN grade = 'C+' THEN 2.33 " +
				"WHEN grade = 'C' THEN 2 " +
				"WHEN grade = 'C-' THEN 1.67 " +
				"WHEN grade = 'D+' THEN 1.33 " +
				"WHEN grade = 'D' THEN 1 " +
				"WHEN grade = 'D-' THEN .67 " +
				"WHEN grade = 'F' THEN 0 END " +
				"WHERE grade IS NOT NULL;";
		String q4 = "SELECT	id,name,sum(num_grade * credits) / sum(credits) AS GPA " +
				"FROM takes1 JOIN student USING (ID) NATURAL JOIN course GROUP BY id;";
		statement.executeUpdate(q1);
		statement.executeUpdate(q2);
		statement.executeUpdate(q3);
		resultSet = statement.executeQuery(q4);
	}

	public void printGPAInfo() throws IOException, SQLException {
		System.out.println("******** Query 1 ********");
		System.out.format("%-15s%-15s%-15s\n", "id", "name", "GPA");
		while (resultSet.next()) {
			String[] out = new String[3];
			out[0] = resultSet.getString(1);
			out[1] = resultSet.getString(2);
			out[2] = resultSet.getString(3);
			System.out.format("%-15s%-15s%-15s\n", (Object[]) out);
		}
	}

	public void findMorningCourses() throws SQLException {
		String q1 = "SELECT " +
				"  course_id, " +
				"  title, " +
				"  sec_id, " +
				"  semester, " +
				"  year, " +
				"  name, " +
				"  count(DISTINCT takes.id) AS enrollment " +
				"FROM course " +
				"  NATURAL JOIN section " +
				"  NATURAL JOIN time_slot " +
				"  NATURAL JOIN teaches " +
				"  NATURAL JOIN instructor " +
				"  JOIN takes USING (course_id, sec_id, semester, year) " +
				"WHERE start_hr <= 12 " +
				"GROUP BY course_id, sec_id, semester, year, name " +
				"HAVING count(DISTINCT takes.id) > 0;";
		resultSet = statement.executeQuery(q1);
	}

	public void printMorningCourses() throws IOException, SQLException {
		System.out.println("******** Query 2 ********");
		System.out.format("%-30s%-30s%-30s%-30s%-30s%-30s%-30s\n",
				"course_id", "title", "sec_id", "semester", "year", "name", "enrollment");
		while (resultSet.next()) {
			String[] out = new String[7];
			out[0] = resultSet.getString(1);
			out[1] = resultSet.getString(2);
			out[2] = resultSet.getString(3);
			out[3] = resultSet.getString(4);
			out[4] = resultSet.getString(5);
			out[5] = resultSet.getString(6);
			out[6] = resultSet.getString(7);
			System.out.format("%-30s%-30s%-30s%-30s%-30s%-30s%-30s\n", (Object[]) out);
		}
	}

	public void findBusyInstructor() throws SQLException {
		String q1 = "SELECT name " +
				"FROM instructor " +
				"  NATURAL JOIN teaches " +
				"GROUP BY instructor.ID " +
				"HAVING count(instructor.ID) >= ALL (SELECT count(ID) " +
				"                                    FROM teaches " +
				"                                    GROUP BY ID);";
		resultSet = statement.executeQuery(q1);
	}

	public void printBusyInstructor() throws IOException, SQLException {
		System.out.println("******** Query 3 ********");
		System.out.println("name");
		while (resultSet.next())
			System.out.println(resultSet.getString(1));
	}

	public void findPrereq() throws SQLException {
		String q1 = "CREATE TEMPORARY TABLE prereq1 SELECT " +
				"prereq.course_id, prereq_id, title " +
				"FROM prereq, course " +
				"WHERE (course.course_id = prereq.prereq_id);";
		String q2 = "SELECT course.title, prereq1.title " +
				"FROM course LEFT OUTER JOIN prereq1 " +
				"ON (course.course_id = prereq1.course_id);";
		statement.executeUpdate(q1);
		resultSet = statement.executeQuery(q2);
	}

	public void printPrereq() throws IOException, SQLException {
		System.out.println("******** Query 4 ********");
		System.out.format("%-30s%-30s\n", "course", "prereq");
		while (resultSet.next()) {
			String[] out = new String[2];
			out[0] = resultSet.getString(1);
			out[1] = resultSet.getString(2);
			System.out.format("%-30s%-30s\n", (Object[]) out);
		}
	}

	@SuppressWarnings("SqlResolve")
	public void updateTable() throws SQLException {
		String q1 = "CREATE TEMPORARY TABLE student1 SELECT * FROM student;";
		String q2 = "UPDATE student1 AS S1 " +
				"SET tot_cred = (SELECT SUM(credits) AS credits " +
				"                FROM takes " +
				"                  NATURAL JOIN course " +
				"                WHERE grade IS NOT NULL " +
				"                      AND grade <> 'F' " +
				"                      AND ID = S1.ID);";
		String q3 = "SELECT * FROM student1";
		statement.executeUpdate(q1);
		statement.executeUpdate(q2);
		resultSet = statement.executeQuery(q3);
	}

	public void printUpdatedTable() throws IOException, SQLException {
		System.out.println("******** Query 5 ********");
		System.out.format("%-15s%-15s%-15s%-15s\n", "id", "name", "dept_name", "tot_cred");
		while (resultSet.next()) {
			String[] out = new String[4];
			out[0] = resultSet.getString(1);
			out[1] = resultSet.getString(2);
			out[2] = resultSet.getString(3);
			out[3] = resultSet.getString(4);
			if (out[3] == null) out[3] = "0";
			System.out.format("%-15s%-15s%-15s%-15s\n", (Object[]) out);
		}
	}

	public void findFirstLastSemester() throws SQLException {
		String q1 = "DROP TABLE student1";
		String q2 = "CREATE TEMPORARY TABLE student1 SELECT " +
				"                                  takes.ID, " +
				"                                  name, " +
				"                                  year, " +
				"                                  semester " +
				"                                FROM takes " +
				"                                  LEFT OUTER JOIN student " +
				"                                    ON (takes.ID = student.ID);";
		String q3 = "ALTER TABLE student1" +
				"  ADD COLUMN date FLOAT(5);";
		String q4 = "UPDATE student1 " +
				"SET date = CASE" +
				"           WHEN semester = 'Winter'" +
				"             THEN YEAR + .1" +
				"           WHEN semester = 'Spring'" +
				"             THEN YEAR + .2" +
				"           WHEN semester = 'Summer'" +
				"             THEN YEAR + .3" +
				"           WHEN semester = 'Fall'" +
				"             THEN YEAR + .4" +
				"           END;";
		String q5 =
				"ALTER TABLE student1" +
						"  ADD COLUMN semester_name VARCHAR(11);";
		String q6 = "CREATE TEMPORARY TABLE student2 SELECT * FROM student1;";
		String q7 =
				"CREATE TEMPORARY TABLE studentMin SELECT " +
						"s1.ID," +
						"CONCAT(semester, ' ', YEAR) AS First_Semester " +
						"FROM student1 AS s1 INNER JOIN (" +
						"SELECT " +
						"student2.ID, " +
						"min(DATE) AS minDate " +
						"FROM student2 " +
						"GROUP BY student2.ID" +
						") AS s2 ON s1.id = s2.ID AND s1.date = s2.MinDate;";
		String q8 =
				"CREATE TEMPORARY TABLE studentMax SELECT " +
						"s1.ID," +
						"CONCAT(semester, ' ', YEAR) AS Last_Semester " +
						"FROM student1 AS s1 INNER JOIN (" +
						"SELECT " +
						"student2.ID," +
						"max(DATE) AS maxDate " +
						"FROM student2 " +
						"GROUP BY student2.ID" +
						") AS s2 ON s1.ID = s2.ID AND s1.date = s2.MaxDate;";
		String q9 =
				"SELECT" +
						"  DISTINCT id, name, First_Semester, Last_Semester " +
						"FROM student1 NATURAL JOIN studentMin NATURAL JOIN studentMax;";
		statement.executeUpdate(q1);
		statement.executeUpdate(q2);
		statement.executeUpdate(q3);
		statement.executeUpdate(q4);
		statement.executeUpdate(q5);
		statement.executeUpdate(q6);
		statement.executeUpdate(q7);
		statement.executeUpdate(q8);
		resultSet = statement.executeQuery(q9);
	}

	public void printFirstLastSemester() throws IOException, SQLException {
		System.out.println("******** Query 6 ********");
		System.out.format("%-15s%-15s%-15s%-15s\n", "id", "name", "First_Semester", "Last_Semester");
		while (resultSet.next()) {
			String[] out = new String[4];
			out[0] = resultSet.getString(1);
			out[1] = resultSet.getString(2);
			out[2] = resultSet.getString(3);
			out[3] = resultSet.getString(4);
			System.out.format("%-15s%-15s%-15s%-15s\n", (Object[]) out);

		}
	}

	public void findHeadCounts() throws SQLException {
		System.out.println("******** Query 7 ********");
		String q0 = "DROP PROCEDURE getNumbers";
		statement.executeUpdate(q0);
		String q1 = "CREATE PROCEDURE getNumbers(IN dept VARCHAR(50), OUT o1 INT, OUT o2 INT) " +
				"  BEGIN " +
				"    SELECT count(DISTINCT ID) " +
				"    INTO o1 " +
				"    FROM instructor " +
				"    WHERE dept_name = dept; " +
				"    SELECT count(DISTINCT ID) " +
				"    INTO o2 " +
				"    FROM student " +
				"    WHERE dept_name = dept; " +
				"  END;";
		statement.executeUpdate(q1);
		Scanner kb = new Scanner(System.in);
		System.out.print("Please enter the department name for the query: ");
		String s = kb.nextLine();
		CallableStatement cs = conn.prepareCall("{CALL getNumbers(?,?,?)}");
		cs.setString(1, s);
		cs.registerOutParameter(2, java.sql.Types.INTEGER);
		cs.registerOutParameter(3, java.sql.Types.INTEGER);
		cs.executeUpdate();
		int instCnt = cs.getInt(2);
		int stdntCnt = cs.getInt(3);
		System.out.println(s);
		System.out.println(s + " Department has " + instCnt + " instructors");
		System.out.println(s + " Department has " + stdntCnt + " students");
	}
}