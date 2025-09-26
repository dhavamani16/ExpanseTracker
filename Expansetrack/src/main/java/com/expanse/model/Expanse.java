package com.expanse.model;

import java.time.LocalDateTime;

public class Expanse {
    private int id;
    private int categoryId;
    private double amount;
    private LocalDateTime date;
    private String description;
    
    public Expanse(int categoryId, double amount, LocalDateTime date, String description) {
        this.categoryId = categoryId;
        this.amount = amount;
        this.date = date;
        this.description = description;
    }
    
    public Expanse(int id, int categoryId, double amount, LocalDateTime date, String description) {
        this.id = id;
        this.categoryId = categoryId;
        this.amount = amount;
        this.date = date;
        this.description = description;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}