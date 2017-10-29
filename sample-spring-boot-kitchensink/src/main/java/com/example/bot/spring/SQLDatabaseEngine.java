package com.example.bot.spring;

import lombok.extern.slf4j.Slf4j;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.net.URISyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

// @Slf4j
// public class SQLDatabaseEngine {
// 	String search(String text) throws Exception {
// 		String tvoya_mamka = "Fuck_yeah";
// 		return tvoya_mamka;
// 	}
// }


@Slf4j
public class SQLDatabaseEngine {
	String search(String text) throws Exception {
		String result = null;

		Connection connection = getConnection();
		
		PreparedStatement stmt = connection.prepareStatement(
			"SELECT userId, name, weight FROM Users where name like concat('%', ?, '%')");
		stmt.setString(1, text);
		ResultSet rs = stmt.executeQuery();

		while(rs.next()) {
			result = rs.getString(3);
			break;
		}
		rs.close();
		stmt.close();
		connection.close();

		if(result == null)
			return "NOT FOUND";
		else
			return result;
	}

	boolean existUser(String userId) throws Exception {
		Connection connection = getConnection();

		PreparedStatement stmt = connection.prepareStatement(
			"SELECT * FROM Users WHERE userId=?");
		stmt.setString(1, userId);
		ResultSet rs = stmt.executeQuery();

		int count = 0;
		while(rs.next())
			count++;

		return count != 0;
	}

	void createUser(String userId, String name) throws Exception {
		Connection connection = getConnection();

		PreparedStatement stmt = connection.prepareStatement(
			"INSERT INTO Users (userId, name) VALUES (?, ?)");
		stmt.setString(1, userId);
		stmt.setString(2, name);

		stmt.executeUpdate();
	}	
	
	private Connection getConnection() throws URISyntaxException, SQLException {
		Connection connection;
		URI dbUri = new URI(System.getenv("DATABASE_URL"));

		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];
		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() +  "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

		log.info("Username: {} Password: {}", username, password);
		log.info ("dbUrl: {}", dbUrl);
		
		connection = DriverManager.getConnection(dbUrl, username, password);

		return connection;
	}
}

