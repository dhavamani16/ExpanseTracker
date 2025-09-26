package com.expanse.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CategoryDAO {
    private Connection conn;

    public CategoryDAO(Connection conn) {
        this.conn = conn;
    }

    public CategoryDAO(String string) {
        //TODO Auto-generated constructor stub
    }

    public void addCategory(CategoryDAO category) throws SQLException {
        String sql = "INSERT INTO categories (name) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category.getName());
            stmt.executeUpdate();
        }
    }

    public List<CategoryDAO> getAllCategories() throws SQLException {
        List<CategoryDAO> list = new ArrayList<>();
        String sql = "SELECT * FROM categories";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new CategoryDAO(rs.getString("name")));
            }
        }
        return list;
    }
    private String getName() {
        // TODO Auto-generated method stub
        return null;
    }
}
