package com.finance.service;

import com.finance.model.FinanceData;
import com.finance.model.Transaction;
import com.finance.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FinanceService {
    private final FinanceData data;

    public FinanceService(FinanceData data) {
        this.data = data;
    }

    public void addTransaction(Transaction transaction) {
        data.getTransactions().add(transaction);
    }

    public void updateTransaction(int index, Transaction updatedTransaction) {
        if (index >= 0 && index < data.getTransactions().size()) {
            data.getTransactions().set(index, updatedTransaction);
        }
    }

    public void deleteTransaction(int index) {
        if (index >= 0 && index < data.getTransactions().size()) {
            data.getTransactions().remove(index);
        }
    }

    public BigDecimal calculateTotalIncome() {
        return data.getTransactions().stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateTotalExpenses() {
        return data.getTransactions().stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateBalance() {
        return calculateTotalIncome().subtract(calculateTotalExpenses());
    }

    public Map<String, BigDecimal> getExpensesByCategory() {
        return data.getTransactions().stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)
                ));
    }

    public Map<String, BigDecimal> getMonthlyReport() {
        return data.getTransactions().stream()
                .collect(Collectors.groupingBy(
                        t -> t.getDate().getYear() + "-" + String.format("%02d", t.getDate().getMonthValue()),
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                t -> t.getType() == TransactionType.INCOME ? t.getAmount() : t.getAmount().negate(),
                                BigDecimal::add
                        )
                ));
    }

    public List<Transaction> getTransactions() {
        return data.getTransactions();
    }

    public void setBudget(String category, BigDecimal limit) {
        data.getCategoryBudgets().put(category, limit);
    }

    public Map<String, BigDecimal> getBudgets() {
        return data.getCategoryBudgets();
    }

    public List<String> checkBudgetAlerts() {
        Map<String, BigDecimal> expenses = getExpensesByCategory();
        return data.getCategoryBudgets().entrySet().stream()
                .filter(entry -> expenses.getOrDefault(entry.getKey(), BigDecimal.ZERO).compareTo(entry.getValue()) > 0)
                .map(entry -> "ALERTA: Gastos em '" + entry.getKey() + "' excederam o limite de " + entry.getValue())
                .collect(Collectors.toList());
    }
}
