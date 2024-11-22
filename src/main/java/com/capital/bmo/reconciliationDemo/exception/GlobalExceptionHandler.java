package com.capital.bmo.reconciliationDemo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // Show friendly error page
    public String handleExceptions(Exception ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error"; // Map to an error.html in templates
    }
}
