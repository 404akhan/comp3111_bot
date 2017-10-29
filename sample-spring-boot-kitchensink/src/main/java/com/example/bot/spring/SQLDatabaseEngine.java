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
		PreparedStatement stmt = connection.prepareStatement("SELECT * FROM keyresp2");
		ResultSet rs = stmt.executeQuery();

		while(rs.next()) {
			if (text.toLowerCase().contains(rs.getString(2).toLowerCase())) {
				result = rs.getString(3);
				return result;
			}
		}
		rs.close();
		stmt.close();
		connection.close();

		return "NOT FOUND";
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

