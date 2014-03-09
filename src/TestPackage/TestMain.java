package TestPackage;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import JDBC.MyDriver;

public class TestMain {

	private static Driver dv;
	private static Connection con;

	public static void TestJDBC() throws SQLException {
		dv = new MyDriver();
		Properties info = new Properties();
		info.put("username", "");
		info.put("password", "");
		con = dv.connect("jdbc:odbc:Mydriver", info);
	}

	public static int executeUpdate(String input) throws SQLException {
		Statement st = con.createStatement();
		return st.executeUpdate(input);
	}

	public static boolean execute(String input) throws SQLException {
		Statement st = con.createStatement();
		return st.execute(input);
	}

	public static ResultSet executeQuery(String input) throws SQLException {
		Statement st = con.createStatement();
		return st.executeQuery(input);
	}
}
