package com.example.expense_tracker.service.implementation;

import com.example.expense_tracker.model.Expense;
import com.example.expense_tracker.repository.ExpenseRepository;
import com.example.expense_tracker.service.ExpenseService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Override
    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    @Override
    public List<Expense> filterExpenses(String category, LocalDate from, LocalDate to) {
        String searchCategory = (category == null || category.isBlank()) ? null : category.trim();
        return expenseRepository.filterExpenses(searchCategory, from, to);
    }

    @Override
    public Expense getExpenseById(Long id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Расход с ID " + id + " не найден"));
    }

    @Override
    public Expense createExpense(String description, BigDecimal amount, String category, LocalDate date) {
        Expense expense = new Expense(description, amount, category, date);
        return expenseRepository.save(expense);
    }

    @Override
    public Expense updateExpense(Long id, String description, BigDecimal amount, String category, LocalDate date) {
        Expense expense = getExpenseById(id);
        expense.setDescription(description);
        expense.setAmount(amount);
        expense.setCategory(category);
        expense.setDate(date);
        return expenseRepository.save(expense);
    }

    @Override
    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }

    @Override
    public BigDecimal calculateTotal(List<Expense> expenses) {
        return expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}