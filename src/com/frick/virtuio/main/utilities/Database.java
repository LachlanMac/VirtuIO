package com.frick.virtuio.main.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

	public static final String DATABASE_PATH = "jdbc:sqlite:/home/frick/application/prime_unity.db";

	Connection conn = null;

	public Database() {

	}

	public void connect() {

		try {
			conn = DriverManager.getConnection(DATABASE_PATH);
			System.out.println("Connected to database");
		} catch (SQLException e) {
			System.out.println("Could not connect to database");
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getLanguage(int id) {
	
		String language = "null";

		String sql = "SELECT english FROM languages WHERE language_id=?";

		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				language = rs.getString("english");
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}


		return language;
	}

}
