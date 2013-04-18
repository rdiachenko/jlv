package com.rdiachenko.jlv.log4j.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Test;

public class DbCreationTest {

	@Test
	public void testDb() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Class.forName("org.h2.Driver").newInstance();
		Connection conn = DriverManager.getConnection("jdbc:h2:src/main/resources/jlv", "jlv",  "jlv");
		Statement statement = conn.createStatement();
		ResultSet rs = statement.executeQuery("select * from logs");

		while (rs.next()) {
			System.out.println(rs.getBigDecimal(1));

			for (int i = 2; i < 13; i++) {
				System.out.println(rs.getString(i));
			}
		}
		conn.close();
	}

}
