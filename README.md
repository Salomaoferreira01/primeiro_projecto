# Aplicação de Finanças Pessoais em Java

Esta é uma aplicação modular em Java para gerenciamento de finanças pessoais, permitindo o registro de receitas e despesas, controle de orçamento e geração de relatórios.

## Funcionalidades

- **Registro de Transações**: Adicione receitas e despesas com valor, categoria, data e observações.
- **Dashboard**: Visualize saldo atual, total de receitas e total de despesas.
- **Relatórios**:
  - Gastos por categoria.
  - Relatório mensal de saldo (agrupado por ano-mês).
  - Listagem detalhada de transações.
- **Orçamentos**: Defina limites mensais por categoria e receba alertas se os gastos forem ultrapassados.
- **Persistência**: Os dados são salvos automaticamente em um arquivo `finance_data.json`.
- **Importação/Exportação**: Permite importar e exportar dados em formato JSON.

## Pré-requisitos

- Java 17 ou superior.
- Maven 3.x.

## Como Executar

1. **Compilar o projeto**:
   ```bash
   mvn compile
   ```

2. **Executar os testes**:
   ```bash
   mvn test
   ```

3. **Executar a aplicação**:
   ```bash
   mvn exec:java -Dexec.mainClass="com.finance.Main"
   ```

## Estrutura do Projeto

- `com.finance.Main`: Interface de linha de comando (CLI).
- `com.finance.model`: Classes de dados (`Transaction`, `FinanceData`).
- `com.finance.service`: Lógica de negócio e cálculos.
- `com.finance.repository`: Gerenciamento de persistência de dados.
