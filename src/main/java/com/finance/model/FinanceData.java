package com.finance.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FinanceData {
    private List<Transaction> transactions = new ArrayList<>();
    private Map<String, BigDecimal> categoryBudgets = new HashMap<>();

    public FinanceData() {}

    public List<Transaction> getTransactions() { return transactions; }
    public void setTransactions(List<Transaction> transactions) { this.transactions = transactions; }

    public Map<String, BigDecimal> getCategoryBudgets() { return categoryBudgets; }
    public void setCategoryBudgets(Map<String, BigDecimal> categoryBudgets) { this.categoryBudgets = categoryBudgets; }
}
