package SmartLibrary.controller;

import SmartLibrary.database.DBConnection;
import SmartLibrary.util.SecurityUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Handles authentication workflows.
 */
public class AuthController {
    public boolean registerUser(String username, String password, String role) {
        String sql = "INSERT INTO users(username, password_hash, role) VALUES (?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, SecurityUtil.sha256(password));
            statement.setString(3, role);
            return statement.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean loginUser(String username, String password) {
        String sql = "SELECT password_hash FROM users WHERE username = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String storedHash = resultSet.getString("password_hash");
                    return storedHash.equals(SecurityUtil.sha256(password));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
