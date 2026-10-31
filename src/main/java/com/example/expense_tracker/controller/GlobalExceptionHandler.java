package com.example.expense_tracker.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleNotFound(IllegalArgumentException ex, Model model) {
        model.addAttribute("errorTitle", "Запись не найдена");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/custom-error"; // Имя HTML-шаблона ошибки
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralError(Exception ex, Model model) {
        model.addAttribute("errorTitle", "Что-то пошло не так");
        model.addAttribute("errorMessage", "Произошла внутренняя ошибка сервера. Попробуйте позже.");
        return "error/custom-error";
    }
}