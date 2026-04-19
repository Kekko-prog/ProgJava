package it.unina.prog;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
    private static final String URL = "jdbc:postgresql://localhost:5432/concessionario_db";
    private static final String USER = "concessionario_admin";
    private static final String PASSWORD = "password123";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
