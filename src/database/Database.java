package database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Database {
	private static Connection connection = null;

	public static Connection getConnection() {
		if(connection == null) {
			try {
				Properties properties = loadProperties();
				String url = properties.getProperty("dburl");
				connection = DriverManager.getConnection(url, properties);
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
		}

		return connection;
	}
	
	public static void closeConnection() {
		if(connection != null) {
			try {
				connection.close();
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void closeStatement(Statement statement) {
		if(statement != null) {
			try {
				statement.close();
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void closeResultSet(ResultSet resultSet) {
		if(resultSet != null) {
			try {
				resultSet.close();
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private static Properties loadProperties() {
		Properties properties = null;

		try(FileInputStream fis = new FileInputStream("db.properties")){
			properties = new Properties();
			properties.load(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return properties;
	}
}
