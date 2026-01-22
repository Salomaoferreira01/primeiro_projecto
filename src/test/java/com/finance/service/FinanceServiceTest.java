package com.finance.service;

import com.finance.model.FinanceData;
import com.finance.model.Transaction;
import com.finance.model.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class FinanceServiceTest {
    private FinanceService service;
    private FinanceData data;

    @BeforeEach
    public void setUp() {
        data = new FinanceData();
        service = new FinanceService(data);
    }

    @Test
    public void testCalculateBalance() {
        service.addTransaction(new Transaction("Salário", new BigDecimal("5000"), "Renda", LocalDate.now(), TransactionType.INCOME, ""));
        service.addTransaction(new Transaction("Aluguel", new BigDecimal("1200"), "Moradia", LocalDate.now(), TransactionType.EXPENSE, ""));

        assertEquals(new BigDecimal("5000"), service.calculateTotalIncome());
        assertEquals(new BigDecimal("1200"), service.calculateTotalExpenses());
        assertEquals(new BigDecimal("3800"), service.calculateBalance());
    }

    @Test
    public void testExpensesByCategory() {
        service.addTransaction(new Transaction("Janta", new BigDecimal("50"), "Alimentação", LocalDate.now(), TransactionType.EXPENSE, ""));
        service.addTransaction(new Transaction("Almoço", new BigDecimal("30"), "Alimentação", LocalDate.now(), TransactionType.EXPENSE, ""));
        service.addTransaction(new Transaction("Ônibus", new BigDecimal("10"), "Transporte", LocalDate.now(), TransactionType.EXPENSE, ""));

        Map<String, BigDecimal> expenses = service.getExpensesByCategory();
        assertEquals(new BigDecimal("80"), expenses.get("Alimentação"));
        assertEquals(new BigDecimal("10"), expenses.get("Transporte"));
    }

    @Test
    public void testBudgetAlerts() {
        service.setBudget("Lazer", new BigDecimal("100"));
        service.addTransaction(new Transaction("Cinema", new BigDecimal("60"), "Lazer", LocalDate.now(), TransactionType.EXPENSE, ""));

        assertTrue(service.checkBudgetAlerts().isEmpty());

        service.addTransaction(new Transaction("Show", new BigDecimal("50"), "Lazer", LocalDate.now(), TransactionType.EXPENSE, ""));

        List<String> alerts = service.checkBudgetAlerts();
        assertFalse(alerts.isEmpty());
        assertTrue(alerts.get(0).contains("Lazer"));
    }
}
