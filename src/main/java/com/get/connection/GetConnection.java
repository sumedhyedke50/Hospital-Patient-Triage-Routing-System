package com.get.connection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class GetConnection {
	
	 private GetConnection() {
	    }

	    public static Connection getConnection() throws SQLException {
	        Properties properties = new Properties();

	        try (InputStream input =
	                   GetConnection.class.getClassLoader()
	                             .getResourceAsStream("db_info.properties")) {

	            if (input == null) {
	                throw new SQLException("db_info.properties not found in src/main/resources");
	            }

	            properties.load(input);

	        } catch (IOException e) {
	            throw new SQLException("Unable to read db_info.properties", e);
	        }

	        String driverClass = properties.getProperty("driver_Class");
	        String databaseUrl = properties.getProperty("databaseUrl");
	        String databaseName = properties.getProperty("databaseName");
	        String userName = properties.getProperty("userName");
	        String password = properties.getProperty("password");

	        if (isBlank(driverClass) || isBlank(databaseUrl)
	                || isBlank(databaseName) || isBlank(userName)) {
	            throw new SQLException("Database configuration is incomplete");
	        }

	        try {
	            Class.forName(driverClass);
	        } catch (ClassNotFoundException e) {
	            throw new SQLException("MySQL JDBC Driver not found", e);
	        }

	        return DriverManager.getConnection(
	                databaseUrl + databaseName,
	                userName,
	                password
	        );
	    }

	    private static boolean isBlank(String value) {
	        return value == null || value.trim().isEmpty();
	    }

}
