package application;

import java.sql.Connection;

import database.Database;

public class Main {

	public static void main(String[] args) {
		Connection c = Database.getConnection();
		Database.closeConnection();
	}
}
