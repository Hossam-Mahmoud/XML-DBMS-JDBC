package TestPackage;

import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class TestMyResultSetMetaData {

	@Before
	public void start() throws SQLException {
		TestMain.TestJDBC();
	}

	@Test
	public void testMetaData() throws SQLException {
		TestMain.executeUpdate("create table testMeta (id integer,type varchar(20))");
		TestMain.execute("insert into testMeta values (10,'xml')");
		TestMain.execute("insert into testMeta values (20,'xml1')");
		ResultSet rs = TestMain.executeQuery("select * from testMeta");
		ResultSetMetaData rm = rs.getMetaData();
		assertEquals(rm.getColumnCount(), 2);
		assertEquals(rm.getColumnName(1), "id");
		assertEquals(rm.isSearchable(2), true);
	}
}
