package com.expanse.dao;

import com.expanse.Category;
import com.expanse.model.Expanse;
import com.expanse.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    private static final String SELECT_ALL = "SELECT * FROM categories ORDER BY id";
    private static final String INSERT_CATEGORY = "INSERT INTO categories (name) VALUES (?)";
    private static final String UPDATE_CATEGORY = "UPDATE categories SET name=? WHERE id=?";
    private static final String DELETE_CATEGORY = "DELETE FROM categories WHERE id=?";

    public List<Category> getAllCategories() throws SQLException {
        List<Category> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Category(rs.getInt("id"), rs.getString("name")));
            }
        }
        return list;
    }

    public int createCategory(String name) throws SQLException {
        try (Connection conn = DatabaseConnection.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_CATEGORY, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
                else return -1;
            }
        }
    }

    public boolean updateCategory(int id, String name) throws SQLException {
        try (Connection conn = DatabaseConnection.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_CATEGORY)) {
            stmt.setString(1, name);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteCategory(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_CATEGORY)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
}
