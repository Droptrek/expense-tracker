package com.example.expense_tracker.service;

import com.example.expense_tracker.model.Expense;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseService {

    List<Expense> getAllExpenses();

    List<Expense> filterExpenses(String category, LocalDate from, LocalDate to);

    Expense getExpenseById(Long id);

    Expense createExpense(String description, BigDecimal amount, String category, LocalDate date);

    Expense updateExpense(Long id, String description, BigDecimal amount, String category, LocalDate date);

    void deleteExpense(Long id);

    BigDecimal calculateTotal(List<Expense> expenses);
}