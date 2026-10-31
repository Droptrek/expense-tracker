package com.example.expense_tracker.repository;

import com.example.expense_tracker.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    @Query("SELECT e FROM Expense e WHERE " +
            "LOWER(e.category) = LOWER(COALESCE(:category, e.category)) AND " +
            "e.date >= COALESCE(:from, e.date) AND " +
            "e.date <= COALESCE(:to, e.date) " +
            "ORDER BY e.date DESC")
    List<Expense> filterExpenses(
            @Param("category") String category,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );
}