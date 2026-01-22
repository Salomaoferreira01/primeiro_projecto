package com.finance.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.finance.model.FinanceData;

import java.io.File;
import java.io.IOException;

public class FinanceRepository {
    private final String filePath = "finance_data.json";
    private final ObjectMapper objectMapper;

    public FinanceRepository() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public void save(FinanceData data) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), data);
    }

    public FinanceData load() {
        return loadFromFile(filePath);
    }

    public void exportData(String path, FinanceData data) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(path), data);
    }

    public FinanceData importData(String path) throws IOException {
        return loadFromFile(path);
    }

    private FinanceData loadFromFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return new FinanceData();
        }
        try {
            return objectMapper.readValue(file, FinanceData.class);
        } catch (IOException e) {
            System.err.println("CRÍTICO: Erro ao carregar dados de " + path + ". O arquivo pode estar corrompido.");
            System.err.println("Detalhes: " + e.getMessage());
            // Retorna um novo objeto para não quebrar a aplicação, mas avisa o usuário
            return new FinanceData();
        }
    }
}
