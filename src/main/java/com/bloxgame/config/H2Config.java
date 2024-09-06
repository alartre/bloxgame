package com.bloxgame.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class H2Config {
    private static final String DB_URL = ApplicationPropertyLoader.getProperty("db.url");
    private static final String DB_USER = ApplicationPropertyLoader.getProperty("db.user");
    private static final String DB_PASSWORD = ApplicationPropertyLoader.getProperty("db.password");

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public static void createTables() {
        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {

            String createUserTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                    "id VARCHAR(255) PRIMARY KEY," +
                    " username VARCHAR(255) NOT NULL UNIQUE," +
                    " password VARCHAR(255) NOT NULL," +
                    " seed VARCHAR(255)," +
                    " serverSeed VARCHAR(255)," +
                    " serverSeedHashed VARCHAR(255)," +
                    " nonce INT)";

            statement.execute(createUserTableSQL);
            String createTableSQL = "CREATE TABLE IF NOT EXISTS spin_results (" +
                    "id VARCHAR(255) PRIMARY KEY," +
                    "number INT," +
                    "isPrime BOOLEAN DEFAULT FALSE," +
                    "userId VARCHAR(255) ," +
                    " seed VARCHAR(255) ," +
                    " serverSeed VARCHAR(255)," +
                    " nonce INT)";

            statement.execute(createTableSQL);




        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
