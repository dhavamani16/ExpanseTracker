package com.expanse.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.expanse.dao.ExpanseDAO;
import com.expanse.model.Expanse;
import com.expanse.dao.CategoryDAO;

import java.util.List;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ExpanseGUI extends JFrame {
    private ExpanseDAO expanseDAO;
    private JTable expanseTable;
    private DefaultTableModel expansetableModel;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JTable categoryTable;
    private DefaultTableModel categoryTableModel;
    private JButton addCategoryButton, editCategoryButton, deleteCategoryButton,back;
    private JButton addExpanseButton, editExpanseButton, deleteExpanseButton;


    public ExpanseGUI() {
        this.expanseDAO = new ExpanseDAO();
        setTitle("Expanse Tracker");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // HOME PANEL 
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

        //  CATEGORY PANEL 
        JPanel categoryPanel = new JPanel(new BorderLayout());
        categoryTable = new JTable();
        categoryPanel.add(new JScrollPane(categoryTable), BorderLayout.CENTER);
        JButton back1 = new JButton("Back");
        categoryPanel.add(back1, BorderLayout.SOUTH);

        JPanel categoryButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton editCategory = new JButton("Edit Category");
        JButton deleteCategory = new JButton("Delete Category");
        JButton back = new JButton("Back"); // your existing back button
        JButton addCategory = new JButton("Add Category");

        categoryButtonPanel.add(addCategory);
        categoryButtonPanel.add(editCategory);
        categoryButtonPanel.add(deleteCategory);
        categoryButtonPanel.add(back1);

        categoryPanel.add(categoryButtonPanel, BorderLayout.SOUTH);


        // EXPANSE PANEL 
        JPanel expansePanel = new JPanel(new BorderLayout());
        String[] columnNames = {"Expanse ID", "Category ID", "Date", "Amount", "Description"};
        expansetableModel = new DefaultTableModel(columnNames, 0);
        expanseTable = new JTable(expansetableModel);
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

        // ADD TO MAIN PANEL 
        mainPanel.add(homePanel, "HOME");
        mainPanel.add(categoryPanel, "CATEGORIES");
        mainPanel.add(expansePanel, "EXPANSES");

        add(mainPanel);

        //  ACTION LISTENERS
        categoryButton.addActionListener(e -> {
            showCategories();
            cardLayout.show(mainPanel, "CATEGORIES");
        });

        expanseButton.addActionListener(e -> {
            loadExpanses();
            cardLayout.show(mainPanel, "EXPANSES");
        });
        

        back1.addActionListener(e -> cardLayout.show(mainPanel, "HOME"));
        back2.addActionListener(e -> cardLayout.show(mainPanel, "HOME"));

        addExpanse.addActionListener(e -> openAddExpanseDialog());
        editExpanse.addActionListener(e -> openEditExpanseDialog());
        deleteExpanse.addActionListener(e -> deleteExpanse());

        addCategory.addActionListener(e -> {
        String name = JOptionPane.showInputDialog(this, "Enter category name:");
        if (name != null && !name.trim().isEmpty()) {
            try {
                CategoryDAO categoryDAO = null;
                categoryDAO.createCategory(name.trim());
                showCategories(); 
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error adding category: " + ex.getMessage());
            }
        }
    });
    addCategory.addActionListener(e -> {
    String name = JOptionPane.showInputDialog(this, "Enter category name:");
    if (name != null && !name.trim().isEmpty()) {
        try {
            CategoryDAO categoryDAO = null;
            categoryDAO.createCategory(name.trim());
            loadCategories(); // refresh table
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error adding category: " + ex.getMessage());
        }
    }
    });



        setVisible(true);
    }

    private void loadCategories() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loadCategories'");
    }

    // CATEGORY DISPLAY 
    private void showCategories() {
        String[] columnNames = {"Category ID", "Category Name"};
        Object[][] data = {
                {1, "Food"},
                {2, "Transport"},
                {3, "Utilities"}
        };
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        categoryTable.setModel(model);
    }

    //  EXPANSE ADD 
    private void openAddExpanseDialog() {
        JTextField categoryField = new JTextField();
        JTextField amountField = new JTextField();
        JTextField descriptionField = new JTextField();
        JTextField dateField = new JTextField(LocalDateTime.now().toString().substring(0, 19));

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Category ID:"));
        panel.add(categoryField);
        panel.add(new JLabel("Amount:"));
        panel.add(amountField);
        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);
        panel.add(new JLabel("Date (YYYY-MM-DDTHH:MM:SS):"));
        panel.add(dateField);

        int result = JOptionPane.showConfirmDialog(
                this, panel, "Add Expanse", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int categoryId = Integer.parseInt(categoryField.getText());
                double amount = Double.parseDouble(amountField.getText());
                String description = descriptionField.getText();
                LocalDateTime date = LocalDateTime.parse(dateField.getText());

                Expanse expanse = new Expanse(categoryId, amount, date, description);
                int newId = expanseDAO.createExpanse(expanse);

                JOptionPane.showMessageDialog(this, "Expanse added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadExpanses();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage(),
                        "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // EXPANSE EDIT 
    private void openEditExpanseDialog() {
        int selectedRow = expanseTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select an expanse to update", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) expansetableModel.getValueAt(selectedRow, 0);
        int categoryId = (int) expansetableModel.getValueAt(selectedRow, 1);
        String dateStr = (String) expansetableModel.getValueAt(selectedRow, 2);
        double amount = (double) expansetableModel.getValueAt(selectedRow, 3);
        String description = (String) expansetableModel.getValueAt(selectedRow, 4);

        JTextField categoryField = new JTextField(String.valueOf(categoryId));
        JTextField amountField = new JTextField(String.valueOf(amount));
        JTextField descriptionField = new JTextField(description);
        JTextField dateField = new JTextField(dateStr);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Category ID:"));
        panel.add(categoryField);
        panel.add(new JLabel("Amount:"));
        panel.add(amountField);
        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);
        panel.add(new JLabel("Date (YYYY-MM-DDTHH:MM:SS):"));
        panel.add(dateField);

        int result = JOptionPane.showConfirmDialog(
                this, panel, "Edit Expanse", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int newCategoryId = Integer.parseInt(categoryField.getText());
                double newAmount = Double.parseDouble(amountField.getText());
                String newDescription = descriptionField.getText();
                LocalDateTime newDate = LocalDateTime.parse(dateField.getText());

                Expanse expanse = new Expanse(id, newCategoryId, newAmount, newDate, newDescription);
                if (expanseDAO.updateExpanse(expanse)) {
                    JOptionPane.showMessageDialog(this, "Expanse updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadExpanses();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update expanse", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage(),
                        "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // EXPANSE DELETE 
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

    //  LOAD EXPANSES FROM DB 
    private void loadExpanses() {
        expansetableModel.setRowCount(0);
        try {
            List<Expanse> expanses = expanseDAO.getAllExpanses();
            for (Expanse exp : expanses) {
                Object[] row = new Object[]{
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

    // MAIN 
    public static void main(String[] args) {
        new ExpanseGUI();
    }
}
