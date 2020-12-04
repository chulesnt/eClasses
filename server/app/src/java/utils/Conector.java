package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conector {
	
	private final static String DRIVER = "org.postgresql.Driver";
	private final static String URL = "jdbc:postgresql://localhost:5432/eclasses";
	private final static String USER = "postgres";
	private final static String PASSWORD = "postgres";

	public static Connection getConnection() throws ClassNotFoundException, SQLException{
		Class.forName(DRIVER);
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}
	
}
