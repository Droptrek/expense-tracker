package com.example.expense_tracker.controller;

import com.example.expense_tracker.model.Expense;
import com.example.expense_tracker.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
public class ExpenseController {

    private final ExpenseService expenseService;

    @Value("${app.currency-symbol:₽}")
    private String currencySymbol;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping({"/", "/expenses"})
    public String listExpenses(
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            Model model) {

        List<Expense> expenses = expenseService.filterExpenses(category, from, to);
        BigDecimal total = expenseService.calculateTotal(expenses);

        model.addAttribute("expenses", expenses);
        model.addAttribute("total", total);
        model.addAttribute("currencySymbol", currencySymbol); // Передаем в HTML

        model.addAttribute("category", category);
        model.addAttribute("from", from);
        model.addAttribute("to", to);

        return "expenses/list";
    }

    @GetMapping("/expenses/new")
    public String showAddForm(Model model) {
        model.addAttribute("expense", new Expense()); // Пустой объект для формы
        return "expenses/addForm";
    }

    @PostMapping("/expenses/create")
    public String createExpense(@Valid @ModelAttribute("expense") Expense expense,
                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "expenses/addForm";
        }
        expenseService.createExpense(expense.getDescription(), expense.getAmount(), expense.getCategory(), expense.getDate());
        return "redirect:/expenses";
    }

    @GetMapping("/expenses/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("expense", expenseService.getExpenseById(id));
        return "expenses/editForm";
    }

    @PostMapping("/expenses/update/{id}")
    public String updateExpense(@PathVariable Long id,
                                @Valid @ModelAttribute("expense") Expense expense,
                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "expenses/editForm";
        }
        expenseService.updateExpense(id, expense.getDescription(), expense.getAmount(), expense.getCategory(), expense.getDate());
        return "redirect:/expenses";
    }

    @PostMapping("/expenses/delete/{id}")
    public String deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return "redirect:/expenses";
    }
}