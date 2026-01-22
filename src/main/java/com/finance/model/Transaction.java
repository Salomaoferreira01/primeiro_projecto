package com.finance.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Transaction {
    private String description;
    private BigDecimal amount;
    private String category;
    private LocalDate date;
    private TransactionType type;
    private String observations;

    public Transaction() {}

    public Transaction(String description, BigDecimal amount, String category, LocalDate date, TransactionType type, String observations) {
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.type = type;
        this.observations = observations;
    }

    // Getters and Setters
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }

    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }
}
