package com.expanse.gui;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ExpanseGUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JTable expanseTable;

    private JButton categoryButton, expanseButton;
    private JButton addButton, editButton, deleteButton, backButton1, backButton2;

    public ExpanseGUI() {
        setTitle("Expanse Tracker");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // ---------------- HOME PAGE ----------------
        JPanel homePanel = new JPanel();
        homePanel.setLayout(new BoxLayout(homePanel, BoxLayout.Y_AXIS));
        categoryButton = new JButton("Categories");
        expanseButton = new JButton("Expanses");
        categoryButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        expanseButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        homePanel.add(Box.createVerticalStrut(100));
        homePanel.add(categoryButton);
        homePanel.add(Box.createVerticalStrut(20));
        homePanel.add(expanseButton);

        // ---------------- CATEGORIES PAGE ----------------
        JPanel categoryPanel = new JPanel(new BorderLayout());
        expanseTable = new JTable();
        categoryPanel.add(new JScrollPane(expanseTable), BorderLayout.CENTER);
        backButton1 = new JButton("Back");
        categoryPanel.add(backButton1, BorderLayout.SOUTH);

        // ---------------- EXPANSES PAGE ----------------
        JPanel expansePanel = new JPanel(new BorderLayout());
        JTable expanseTable2 = new JTable();
        expansePanel.add(new JScrollPane(expanseTable2), BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        addButton = new JButton("Add Expanse");
        editButton = new JButton("Edit Expanse");
        deleteButton = new JButton("Delete Expanse");
        backButton2 = new JButton("Back");

        controlPanel.add(addButton);
        controlPanel.add(editButton);
        controlPanel.add(deleteButton);
        controlPanel.add(backButton2);

        expansePanel.add(controlPanel, BorderLayout.SOUTH);

        // ---------------- Add to CardLayout ----------------
        mainPanel.add(homePanel, "HOME");
        mainPanel.add(categoryPanel, "CATEGORIES");
        mainPanel.add(expansePanel, "EXPANSES");

        add(mainPanel);

        // ---------------- Events ----------------
        categoryButton.addActionListener(e -> {
            showCategories();
            cardLayout.show(mainPanel, "CATEGORIES");
        });

        expanseButton.addActionListener(e -> {
            showExpanses(expanseTable2);
            cardLayout.show(mainPanel, "EXPANSES");
        });

        backButton1.addActionListener(e -> cardLayout.show(mainPanel, "HOME"));
        backButton2.addActionListener(e -> cardLayout.show(mainPanel, "HOME"));

        // addButton / editButton / deleteButton events can be added later

        setVisible(true);
    }

    private void showCategories() {
        try {
            String[] columnNames = {"Category ID", "Category Name"};
            Object[][] data = {
                {1, "Food"},
                {2, "Transport"},
                {3, "Utilities"}
            };
            expanseTable.setModel(new DefaultTableModel(data, columnNames));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading categories: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showExpanses(JTable table) {
        try {
            String[] columnNames = {"Expanse ID", "Category ID", "Date", "Amount"};
            Object[][] data = {
                {1, 1, "2023-10-01 12:00", 50},
                
                
            };
            table.setModel(new DefaultTableModel(data, columnNames));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading expanses: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new ExpanseGUI();
    }
}
