package com.expanse.dao;

import com.expanse.model.Expanse;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.expanse.util.DatabaseConnection;

public class ExpanseDAO {

    private static final String SELECT_ALL_EXPANSES = "SELECT * FROM expanses ORDER BY date DESC";
    private static final String INSERT_EXPANSE = "INSERT INTO expanses (user_id, amount, date, description) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_EXPANSE = "UPDATE expanses SET user_id=?, amount=?, date=?, description=? WHERE id=?";
    private static final String DELETE_EXPANSE = "DELETE FROM expanses WHERE id=?";
    private static final String SELECT_EXPANSE_BY_ID = "SELECT * FROM expanses WHERE id=?";

    public int createExpanse(Expanse expanse) throws SQLException {
        try (Connection conn = DatabaseConnection.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_EXPANSE, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, expanse.getCategoryId()); 
            stmt.setDouble(2, expanse.getAmount());
            stmt.setTimestamp(3, Timestamp.valueOf(expanse.getDate()));
            stmt.setString(4, expanse.getDescription());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating expanse failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating expanse failed, no ID obtained.");
                }
            }
        }
    }

    public boolean updateExpanse(Expanse expanse) throws SQLException {
        try (Connection conn = DatabaseConnection.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_EXPANSE)) {

            stmt.setInt(1, expanse.getCategoryId());
            stmt.setDouble(2, expanse.getAmount());
            stmt.setTimestamp(3, Timestamp.valueOf(expanse.getDate()));
            stmt.setString(4, expanse.getDescription());
            stmt.setInt(5, expanse.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteExpanse(int expanseId) throws SQLException {
        try (Connection conn = DatabaseConnection.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_EXPANSE)) {

            stmt.setInt(1, expanseId);
            return stmt.executeUpdate() > 0;
        }
    }

    public List<Expanse> getAllExpanses() throws SQLException {
        List<Expanse> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_EXPANSES);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(mapExpanse(rs));
            }
        }
        return list;
    }

    private Expanse mapExpanse(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int userId = rs.getInt("user_id");
        double amount = rs.getDouble("amount");
        LocalDateTime date = rs.getTimestamp("date").toLocalDateTime();
        String description = rs.getString("description");
        return new Expanse(id, userId, amount, date, description);
    }

    public Expanse getExpanseByID(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_EXPANSE_BY_ID)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapExpanse(rs);
                }
            }
        }
        return null;
    }
}
