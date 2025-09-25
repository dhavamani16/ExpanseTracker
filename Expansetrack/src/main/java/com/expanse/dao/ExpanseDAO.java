package com.expanse.dao;
import com.expanse.model.Expanse;
import com.expanse.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import com.expanse.dao.ExpanseDAO; 
import java.sql.Statement;
import java.sql.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
public class ExpanseDAO{
    private static final String INSERT_TODO = "INSERT INTO todos (eid,cid,date,amount) VALUES (?, ?, ?, ? )";
    private static final String SELECT_TODO_BY_ID = "SELECT eid,cid,date,amount FROM todos WHERE eid = ?";
    public int create(Expanse expanse) throws SQLException {
        try (Connection conn = DatabaseConnection.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_TODO, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, expanse.getEid());
            stmt.setInt(2, expanse.getCid());
            stmt.setTimestamp(3, java.sql.Timestamp.valueOf(expanse.getDate()));
             try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                 if (generatedKeys.next()) {
                     return generatedKeys.getInt(1);
                 } else {
                     throw new SQLException("Creating todo failed, no ID obtained.");
                 }
             } catch (SQLException e) {
            stmt.setInt(4, expanse.getAmt());
             }
            }
            return -1;
        }
    }