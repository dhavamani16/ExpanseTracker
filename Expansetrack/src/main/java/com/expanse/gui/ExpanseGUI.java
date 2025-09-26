package com.expanse.gui;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.expanse.dao.ExpanseDAO;
import com.expanse.model.Expanse;
import java.util.List;

import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ExpanseGUI extends JFrame {
    private ExpanseDAO expanseDAO = new ExpanseDAO();

    private JTable expanseTable;
    private DefaultTableModel expansetableModel;
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JCheckBox completedCheckBox;
    private JButton addButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JButton refreshButton;
    private JComboBox<String> filterComboBox;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    private JTable table;

    public ExpanseGUI() {
        this.expanseDAO = new ExpanseDAO();
        setTitle("Expanse Tracker");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        JPanel homePanel = new JPanel();
        homePanel.setLayout(new BoxLayout(homePanel, BoxLayout.Y_AXIS));

        JButton categoryButton = new JButton("Categories");
        JButton expanseButton = new JButton("Expanses");

        categoryButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        expanseButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        homePanel.add(Box.createVerticalStrut(200));
        homePanel.add(categoryButton);
        homePanel.add(Box.createVerticalStrut(50));
        homePanel.add(expanseButton);
        categoryButton.setPreferredSize(new Dimension(250, 120));
        expanseButton.setPreferredSize(new Dimension(250, 120));
        categoryButton.setFont(new Font("Arial", Font.BOLD, 20));
        expanseButton.setFont(new Font("Arial", Font.BOLD, 20));

        


        JPanel categoryPanel = new JPanel(new BorderLayout());
        table = new JTable();
        categoryPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        JButton back1 = new JButton("Back");
        categoryPanel.add(back1, BorderLayout.SOUTH);

        JPanel expansePanel = new JPanel(new BorderLayout());
        JTable expanseTable = new JTable();
        expansePanel.add(new JScrollPane(expanseTable), BorderLayout.CENTER);

        JPanel expanseButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton addExpanse = new JButton("Add Expanse");
        JButton editExpanse = new JButton("Edit Expanse");
        JButton deleteExpanse = new JButton("Delete Expanse");
        JButton back2 = new JButton("Back");

        expanseButtonPanel.add(addExpanse);
        expanseButtonPanel.add(editExpanse);
        expanseButtonPanel.add(deleteExpanse);
        expanseButtonPanel.add(back2);

        expansePanel.add(expanseButtonPanel, BorderLayout.SOUTH);

        mainPanel.add(homePanel, "HOME");
        mainPanel.add(categoryPanel, "CATEGORIES");
        mainPanel.add(expansePanel, "EXPANSES");

        add(mainPanel);

        categoryButton.addActionListener(e -> {
            showCategories();
            cardLayout.show(mainPanel, "CATEGORIES");
        });

        expanseButton.addActionListener(e -> {
            showExpanses(expanseTable);
            cardLayout.show(mainPanel, "EXPANSES");
        });

        back1.addActionListener(e -> cardLayout.show(mainPanel, "HOME"));
        back2.addActionListener(e -> cardLayout.show(mainPanel, "HOME"));

        // Extra: action buttons in Expanses page
        addExpanse.addActionListener(e -> addExpanse(new Object[] {0, 1, LocalDateTime.now().toString().substring(0, 19), 0.0, "New Expanse"}));
        editExpanse.addActionListener(e -> editExpanseExpanse());
        deleteExpanse.addActionListener(e -> deleteExpanse());

        setVisible(true);
    }

    private Object editExpanseExpanse() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'editExpanseExpanse'");
    }

    private void showCategories() {
        try {
            String[] columnNames = {"Category ID", "Category Name"};
            Object[][] data = {
                {1, "Food"},
                {2, "Transport"},
                {3, "Utilities"}
            };
            DefaultTableModel model = new DefaultTableModel(data, columnNames);
            table.setModel(model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading categories: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showExpanses(JTable expanseTable) {
        try {
            String[] columnNames = {"Expanse ID", "Category ID", "Date", "Amount"};
            Object[][] data = {
                {1, 1, "2023-10-01 12:00", 50},
                {2, 2, "2023-10-02 15:30", 20},
                {3, 3, "2023-10-03 09:45", 100}
            };
            DefaultTableModel model = new DefaultTableModel(data, columnNames);
            expanseTable.setModel(model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading expanses: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void addExpanse(Object[] data) {
    int categoryId = (int) data[1];
    double amount = (double) data[3];
    LocalDateTime date = LocalDateTime.parse((String) data[2]);
    String description = (String) data[4];

    if (description.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Description cannot be empty", "Validation Error", JOptionPane.WARNING_MESSAGE);
        return;
    }

    try {
        Expanse expanse = new Expanse(categoryId, amount, date, description);
        
        expanseDAO.createExpanse(expanse);

        JOptionPane.showMessageDialog(this, "Expanse added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        loadExpanses();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error adding expanse: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void editExpanse(Object[] data) {
    int selectedRow = expanseTable.getSelectedRow();
    if (selectedRow < 0) {
        JOptionPane.showMessageDialog(this, "Please select an expanse to update", "No Selection", JOptionPane.WARNING_MESSAGE);
        return;
    }

    int id = (int) expansetableModel.getValueAt(selectedRow, 0); 
    
    int categoryId = (int) data[1];
    double amount = (double) data[3];
    LocalDateTime date = LocalDateTime.parse((String) data[2]);
    String description = (String) data[4];

    if (description.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Description cannot be empty", "Validation Error", JOptionPane.WARNING_MESSAGE);
        return;
    }

    try {
        Expanse expanse = expanseDAO.getExpanseByID(id); 
        
        if (expanse != null) {
            expanse.setCategoryId(categoryId);
            expanse.setAmount(amount);
            expanse.setDate(date);
            expanse.setDescription(description);

            if (expanseDAO.updateExpanse(expanse)) {
                JOptionPane.showMessageDialog(this, "Expanse updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadExpanses();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update expanse", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selected expanse not found in database", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error updating expanse: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void deleteExpanse() {
    int row = expanseTable.getSelectedRow();
    if (row < 0) {
        JOptionPane.showMessageDialog(this, "Please select an expanse to delete", "No Selection", JOptionPane.WARNING_MESSAGE);
        return;
    }

    int expanseId = (int) expansetableModel.getValueAt(row, 0);
    int confirm = JOptionPane.showConfirmDialog(this, 
        "Are you sure you want to delete Expanse ID " + expanseId + "?", 
        "Confirm Deletion", JOptionPane.YES_NO_OPTION);

    if (confirm == JOptionPane.YES_OPTION) {
        try {
            if (expanseDAO.deleteExpanse(expanseId)) {
                JOptionPane.showMessageDialog(this, "Expanse deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadExpanses();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete expanse", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error deleting expanse: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

private void loadExpanses() {
    expansetableModel.setRowCount(0);

    try {
        List<Expanse> expanses = expanseDAO.getAllExpanses();
        
        for (Expanse exp : expanses) {
            Object[] row = new Object[] {
                exp.getId(),
                exp.getCategoryId(),
                exp.getDate().toString().substring(0, 19),
                exp.getAmount(),
                exp.getDescription()
            };
            expansetableModel.addRow(row);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error loading expanses: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}
    public static void main(String[] args) {
        new ExpanseGUI();
    }
}