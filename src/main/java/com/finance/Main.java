package com.finance;

import com.finance.model.FinanceData;
import com.finance.model.Transaction;
import com.finance.model.TransactionType;
import com.finance.repository.FinanceRepository;
import com.finance.service.FinanceService;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final FinanceRepository repository = new FinanceRepository();
    private static FinanceService service;
    private static FinanceData data;

    public static void main(String[] args) {
        data = repository.load();
        service = new FinanceService(data);

        boolean running = true;
        while (running) {
            showMenu();
            int choice = readInt();

            switch (choice) {
                case 1 -> showDashboard();
                case 2 -> registerTransaction();
                case 3 -> editTransactionMenu();
                case 4 -> showReports();
                case 5 -> configureBudgets();
                case 6 -> importExportMenu();
                case 7 -> saveData();
                case 0 -> running = false;
                default -> System.out.println("Opção inválida!");
            }
        }
        saveData();
        System.out.println("Saindo...");
    }

    private static void showMenu() {
        System.out.println("\n=== Sistema de Finanças Pessoais ===");
        System.out.println("1. Dashboard (Resumo)");
        System.out.println("2. Registrar Transação");
        System.out.println("3. Editar/Excluir Transação");
        System.out.println("4. Relatórios");
        System.out.println("5. Configurações de Orçamento");
        System.out.println("6. Importar/Exportar Dados");
        System.out.println("7. Salvar Dados");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static void showDashboard() {
        System.out.println("\n--- Dashboard ---");
        System.out.println("Saldo Atual: R$ " + service.calculateBalance());
        System.out.println("Total Receitas: R$ " + service.calculateTotalIncome());
        System.out.println("Total Despesas: R$ " + service.calculateTotalExpenses());

        List<String> alerts = service.checkBudgetAlerts();
        if (!alerts.isEmpty()) {
            System.out.println("\n--- ALERTAS ---");
            alerts.forEach(System.out::println);
        }
    }

    private static void registerTransaction() {
        System.out.println("\n--- Registrar Transação ---");
        Transaction t = inputTransaction(null);
        service.addTransaction(t);
        System.out.println("Transação registrada com sucesso!");
    }

    private static Transaction inputTransaction(Transaction existing) {
        System.out.print("Descrição [" + (existing != null ? existing.getDescription() : "") + "]: ");
        String desc = scanner.nextLine();
        if (desc.isEmpty() && existing != null) desc = existing.getDescription();

        BigDecimal amount = null;
        while (amount == null) {
            System.out.print("Valor [" + (existing != null ? existing.getAmount() : "") + "]: ");
            String valStr = scanner.nextLine();
            if (valStr.isEmpty() && existing != null) {
                amount = existing.getAmount();
            } else {
                amount = parseBigDecimal(valStr);
                if (amount == null) System.out.println("Valor inválido! Tente novamente.");
            }
        }

        TransactionType type = null;
        while (type == null) {
            System.out.print("Tipo (1 para Receita, 2 para Despesa) [" + (existing != null ? existing.getType() : "") + "]: ");
            String typeStr = scanner.nextLine();
            if (typeStr.isEmpty() && existing != null) {
                type = existing.getType();
            } else {
                int typeChoice = -1;
                try { typeChoice = Integer.parseInt(typeStr); } catch (Exception e) {}
                if (typeChoice == 1) type = TransactionType.INCOME;
                else if (typeChoice == 2) type = TransactionType.EXPENSE;
                else System.out.println("Opção inválida!");
            }
        }

        System.out.print("Categoria [" + (existing != null ? existing.getCategory() : "") + "]: ");
        String category = scanner.nextLine();
        if (category.isEmpty() && existing != null) category = existing.getCategory();

        LocalDate date = null;
        while (date == null) {
            System.out.print("Data (dd/MM/yyyy) [Vazio para hoje" + (existing != null ? ", Atual: " + existing.getDate() : "") + "]: ");
            String dateStr = scanner.nextLine();
            if (dateStr.isEmpty()) {
                date = (existing != null) ? existing.getDate() : LocalDate.now();
            } else {
                try {
                    date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                } catch (Exception e) {
                    System.out.println("Data inválida! Use o formato dd/MM/yyyy.");
                }
            }
        }

        System.out.print("Observações [" + (existing != null ? existing.getObservations() : "") + "]: ");
        String obs = scanner.nextLine();
        if (obs.isEmpty() && existing != null) obs = existing.getObservations();

        return new Transaction(desc, amount, category, date, type, obs);
    }

    private static void editTransactionMenu() {
        System.out.println("\n--- Editar/Excluir Transação ---");
        List<Transaction> list = service.getTransactions();
        if (list.isEmpty()) {
            System.out.println("Nenhuma transação encontrada.");
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            Transaction t = list.get(i);
            System.out.printf("[%d] %s | %s | R$ %s | %s\n", i, t.getDate(), t.getType(), t.getAmount(), t.getDescription());
        }
        System.out.print("Escolha o índice para editar/excluir (ou -1 para cancelar): ");
        int index = readInt();
        if (index >= 0 && index < list.size()) {
            System.out.println("1. Editar");
            System.out.println("2. Excluir");
            int subChoice = readInt();
            if (subChoice == 1) {
                Transaction updated = inputTransaction(list.get(index));
                service.updateTransaction(index, updated);
                System.out.println("Transação atualizada!");
            } else if (subChoice == 2) {
                service.deleteTransaction(index);
                System.out.println("Transação excluída!");
            }
        }
    }

    private static void showReports() {
        System.out.println("\n--- Relatórios ---");
        System.out.println("1. Gastos por Categoria");
        System.out.println("2. Relatório Mensal (Saldo por Mês)");
        System.out.println("3. Listar todas as Transações");
        int choice = readInt();

        switch (choice) {
            case 1 -> {
                System.out.println("\n--- Gastos por Categoria ---");
                service.getExpensesByCategory().forEach((cat, val) -> System.out.println(cat + ": R$ " + val));
            }
            case 2 -> {
                System.out.println("\n--- Relatório Mensal ---");
                service.getMonthlyReport().forEach((month, balance) -> System.out.println(month + ": R$ " + balance));
            }
            case 3 -> {
                System.out.println("\n--- Transações ---");
                service.getTransactions().forEach(t ->
                        System.out.printf("%s | %-10s | %-15s | R$ %10.2f | %s\n",
                                t.getDate(), t.getType(), t.getCategory(), t.getAmount(), t.getDescription()));
            }
        }
    }

    private static void importExportMenu() {
        System.out.println("\n--- Importar/Exportar ---");
        System.out.println("1. Exportar para arquivo");
        System.out.println("2. Importar de arquivo");
        int choice = readInt();

        if (choice == 1) {
            System.out.print("Digite o caminho do arquivo para exportação: ");
            String path = scanner.nextLine();
            try {
                repository.exportData(path, data);
                System.out.println("Dados exportados com sucesso!");
            } catch (IOException e) {
                System.err.println("Erro ao exportar: " + e.getMessage());
            }
        } else if (choice == 2) {
            System.out.print("Digite o caminho do arquivo para importação: ");
            String path = scanner.nextLine();
            try {
                data = repository.importData(path);
                service = new FinanceService(data);
                System.out.println("Dados importados com sucesso!");
            } catch (IOException e) {
                System.err.println("Erro ao importar: " + e.getMessage());
            }
        }
    }

    private static void configureBudgets() {
        System.out.println("\n--- Configurações de Orçamento ---");
        System.out.print("Categoria: ");
        String category = scanner.nextLine();
        System.out.print("Limite mensal: ");
        BigDecimal limit = readBigDecimal();
        service.setBudget(category, limit);
        System.out.println("Orçamento configurado!");
    }

    private static void saveData() {
        try {
            repository.save(data);
            System.out.println("Dados salvos com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados: " + e.getMessage());
        }
    }

    private static int readInt() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static BigDecimal readBigDecimal() {
        return parseBigDecimal(scanner.nextLine());
    }

    private static BigDecimal parseBigDecimal(String input) {
        try {
            return new BigDecimal(input.replace(",", "."));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
