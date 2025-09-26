package com.expanse.dao;

import com.expanse.model.Expanse;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.expanse.util.DatabaseConnection;
import com.expanse.model.Expanse;;

public class ExpanseDAO {
    
    public Expanse getExpanseByID(int id) throws SQLException {
        if (id == 1) {
           
        }
        return null;
    }

    public void createExpanse(Expanse expanse) throws SQLException {
        System.out.println("DAO: Inserting new Expanse: " + expanse.getDescription());
        expanse.setId((int)(Math.random() * 1000) + 100); 
    }

    public boolean updateExpanse(Expanse expanse) throws SQLException {
        System.out.println("DAO: Updating Expanse ID " + expanse.getId() + " to " + expanse.getDescription());
        return true; 
    }

    public boolean deleteExpanse(int expanseId) throws SQLException {
        System.out.println("DAO: Deleting Expanse ID: " + expanseId);
        return true; 
    }
    
    public List<Expanse> getAllExpanses() throws SQLException {
        List<Expanse> list = new ArrayList<>();
        list.add(new Expanse(1, 1, 50.00, LocalDateTime.now().minusDays(1), "Initial Load: Lunch"));
        list.add(new Expanse(2, 2, 20.00, LocalDateTime.now().minusDays(2), "Initial Load: Transport"));
        return list;
    }
}