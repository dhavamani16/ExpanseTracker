package com.expanse.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.expanse.model.Expanse;

public class ExpanseDAO {
    private Connection conn;

    public ExpanseDAO(Connection conn) {
        this.conn = conn;
    }

    public List<Expanse> getAllExpanses() throws SQLException {
        List<Expanse> expanses = new ArrayList<>();
        String sql = "SELECT id, category_id, date, amount FROM expanses";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Expanse e = new Expanse(0, 0, null, 0);
                e.setCategoryId(rs.getInt("category_id"));
                e.setDate(rs.getTimestamp("date").toLocalDateTime());
                e.setAmt(rs.getDouble("amount"));
                expanses.add(e);
            }
        }
        return expanses;
    }

    public void addExpanse(Expanse expanse) throws SQLException {
        String sql = "INSERT INTO expanses (category_id, date, amount) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, 0);
            ps.setTimestamp(2, Timestamp.valueOf(expanse.getDate()));
            ps.setDouble(3, expanse.getAmt());
            ps.executeUpdate();
        }
    }

    public boolean updateExpanse(Expanse expanse) throws SQLException {
        String sql = "UPDATE expanses SET category_id=?, date=?, amount=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, (int) expanse.getCategoryId());
            ps.setTimestamp(2, Timestamp.valueOf(expanse.getDate()));
            ps.setDouble(3, expanse.getAmt());
            ps.setInt(4, expanse.getAmt());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteExpanse(int id) throws SQLException {
        String sql = "DELETE FROM expanses WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public Expanse getExpanseById(int id) {
        throw new UnsupportedOperationException("Unimplemented method 'getExpanseById'");
    }
}
