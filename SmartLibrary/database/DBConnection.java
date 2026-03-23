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
    private static final String PASSWORD = "root123";

    // ✅ ADD THIS BLOCK (VERY IMPORTANT)
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL Driver Loaded ✅");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver NOT found ❌");
            e.printStackTrace();
        }
    }

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        System.out.println("Database Connected ✅");
        return conn;
    }
}
