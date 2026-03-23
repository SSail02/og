package SmartLibrary.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Centralized JDBC helper for MySQL connectivity.
 */
public final class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/smart_library";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
