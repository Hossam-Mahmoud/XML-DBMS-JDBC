package TestPackage;

import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import org.junit.Before;
import org.junit.Test;

public class TestMyResultSet {

	@Before
	public void start() throws SQLException {
		TestMain.TestJDBC();
	}

	@Test
	public void aTestReturn() throws SQLException {
		assertEquals(
				TestMain.execute("create table JunitTest (name varchar(20),id integer)"),
				false);
		assertEquals(
				TestMain.executeUpdate("insert into JunitTest values ('islam',10)"),
				1);
		assertEquals(
				TestMain.executeUpdate("insert into JunitTest values ('Radwan',10)"),
				1);
		assertEquals(TestMain.execute("select * from JunitTest"), true);
		assertEquals(
				TestMain.execute("select * from JunitTest where name='islam'"),
				true);
		assertEquals(
				TestMain.executeUpdate("delete from JunitTest where name='islam'"),
				1);
		assertEquals(
				TestMain.executeUpdate("insert into JunitTest values ('hazem',10)"),
				1);
		assertEquals(
				TestMain.executeUpdate("update JunitTest set id=2 where id=10"),
				2);
		assertEquals(TestMain.executeUpdate("drop JunitTest"), 2);

	}

	@Test
	public void cursorTest() throws SQLException {

		TestMain.execute("insert into JunitTest values ('islam',29)");
		TestMain.execute("insert into JunitTest values ('Alex',232)");
		ResultSet rs = TestMain.executeQuery("select * from JunitTest");

		assertEquals(rs.next(), true);
		assertEquals(rs.isFirst(), true);
		assertEquals(rs.isLast(), false);
		rs.next();
		assertEquals(rs.isLast(), true);
		rs.next();
		assertEquals(rs.isAfterLast(), true);
		assertEquals(rs.previous(), true);
	}

	@Test(expected = SQLException.class)
	public void StatementTest() throws SQLException {
		TestMain.execute("drop G|Egypt");
		TestMain.execute("insert into JunitTest values ('Faculty')");
	}

	@Test(expected = SQLException.class)
	public void ReaderTest() throws SQLException {
		TestMain.execute("select * from Google");
	}

	@Test(expected = SQLException.class)
	public void ValidationTest() throws SQLException {
		TestMain.execute("insert into JunitTest values ('FacultyOfEngineeringAlex',200)");
	}

	@Test
	public void ResultTest() throws SQLException {

		TestMain.execute("create table student ( name varchar(100) , age integer , pass boolean)");
		TestMain.execute("create table teacher ( name varchar(100) , age integer , work boolean)");

		TestMain.execute("create table class ( className varchar(100) , capacity integer , empty boolean)");

		TestMain.execute("insert into student values ( 'ahmed Mahmoud' , 23 , true )");
		TestMain.execute("insert into student values ( 'ahmed Mahmoud' , 78 ,false )");
		TestMain.execute("insert into student values ( 'ahmed Mohamed' , 78 ,false )");
		ResultSet rs = TestMain
				.executeQuery("select * from student where name = 'ahmed Mahmoud'");

		ResultSetMetaData rm = rs.getMetaData();
		int nCols = rm.getColumnCount();
		String[][] fullResult = new String[2][];
		fullResult[0] = new String[] { "'ahmed Mahmoud'".toLowerCase(),
				"23".toLowerCase(), "true".toLowerCase() };
		fullResult[1] = new String[] { "'ahmed Mahmoud'".toLowerCase(),
				"23".toLowerCase(), "true".toLowerCase() };
		int k = 0;
		while (rs.next()) {
			String[] re = new String[nCols];
			for (int i = 0; i < nCols; i++) {
				re[i] = rs.getObject(i + 1) + "";
			}
			assertArrayEquals(re, fullResult[k]);
			k++;
		}

		TestMain.execute("insert into student values ( 'hamydoinho' , 2213 , false )");
		rs = TestMain.executeQuery("select * from student where age < 45");
		k = 0;
		while (rs.next()) {
			String[] re = new String[nCols];
			for (int i = 0; i < nCols; i++) {
				re[i] = rs.getObject(i + 1) + "";
			}
			assertArrayEquals(re, fullResult[k]);
			k++;
		}

	}
}
