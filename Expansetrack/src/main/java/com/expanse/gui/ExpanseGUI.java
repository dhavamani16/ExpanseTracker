package com.expanse.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.expanse.dao.ExpanseDAO;
import com.expanse.model.Expanse;

import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Vector;
import java.awt.event.ActionEvent;
import java.sql.*;

public class ExpanseGUI extends JFrame {
    private ExpanseDAO expanseDAO;
    private JTable expanseTable;
    private DefaultTableModel tableModel;

    private JTextField categoryIdField;
    private JTextField dateField;
    private JTextField amountField;

    private JButton addButton, updateButton, deleteButton, refreshButton, backButton;

    public ExpanseGUI() throws SQLException {
        this.expanseDAO = new ExpanseDAO(null);
        initializeComponents();
        setupLayout();
        setupEventListeners();
        loadExpanses();
    }

    private void initializeComponents() {
        setTitle("Expanse Manager");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        String[] columnNames = {"ID", "Category ID", "Date", "Amount"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        expanseTable = new JTable(tableModel);
        expanseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        categoryIdField = new JTextField(15);
        dateField = new JTextField(15);
        amountField = new JTextField(15);

        addButton = new JButton("Add Expanse");
        updateButton = new JButton("Update Expanse");
        deleteButton = new JButton("Delete Expanse");
        refreshButton = new JButton("Refresh");
        backButton = new JButton("Back");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        inputPanel.add(new JLabel("Category ID:"));
        inputPanel.add(categoryIdField);
        inputPanel.add(new JLabel("Date (YYYY-MM-DD HH:mm):"));
        inputPanel.add(dateField);
        inputPanel.add(new JLabel("Amount:"));
        inputPanel.add(amountField);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(expanseTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setupEventListeners() {
        addButton.addActionListener(e -> addExpanse());
        updateButton.addActionListener(e -> updateExpanse());
        
        
        backButton.addActionListener(e -> {
            dispose(); // close this window
            try {
            loadExpanses();
            } catch (SQLException ex) {
                ex.printStackTrace(); // You can also show a dialog or log the error
            }
            try {
               new ExpanseGUI().setVisible(true); // go back to main menu
           } catch (SQLException sqlEx) {
               sqlEx.printStackTrace();
               JOptionPane.showMessageDialog(this, "Error opening ExpanseGUI: " + sqlEx.getMessage(),
                       "Error", JOptionPane.ERROR_MESSAGE);
           }
        });
    }

    private void addExpanse() {
        try {
            int categoryId = Integer.parseInt(categoryIdField.getText().trim());
            String date = dateField.getText().trim();
            double amount = Double.parseDouble(amountField.getText().trim());

            Expanse expanse = new Expanse(categoryId, date, amount);
            expanseDAO.addExpanse(expanse);

            JOptionPane.showMessageDialog(this, "Expanse added successfully!");
            loadExpanses();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding expanse: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateExpanse() {
        int row = expanseTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select an expanse to update");
            return;
        }

        try {
            int id = (int) tableModel.getValueAt(row, 0);
            int categoryId = Integer.parseInt(categoryIdField.getText().trim());
            String date = dateField.getText().trim();
            double amount = Double.parseDouble(amountField.getText().trim());

            Expanse expanse = expanseDAO.getExpanseById(id);
            if (expanse != null) {
                expanse.setCategoryId(categoryId);
                expanse.setDate(date);
                expanse.setAmt(amount);
                expanseDAO.addExpanse(expanse);

                JOptionPane.showMessageDialog(this, "Expanse updated successfully!");
                loadExpanses();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating expanse: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteExpanse(Throwable e) throws SQLException {
        int row = expanseTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select an expanse to delete");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this expanse?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            Expanse id = (Expanse) tableModel.getValueAt(row, 0);
            expanseDAO.addExpanse(id);

            JOptionPane.showMessageDialog(this, "Expanse deleted successfully!");
            loadExpanses();
        }
    }

    private void loadExpanses() throws SQLException {
        List<Expanse> expanses = expanseDAO.getAllExpanses();
        tableModel.setRowCount(0);
        for (Expanse e : expanses) {
            Object[] row = {e.getAmt(), e.getCategoryId(), e.getDate(), e.getAmt()};
            tableModel.addRow(row);
        }

    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ExpanseGUI gui = null;
            try {
                gui = new ExpanseGUI();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            gui.setVisible(true);
        });
    }
}
