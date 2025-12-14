package org.expensetracker.controller;

import org.expensetracker.dto.ExpenseResponse;
import org.expensetracker.dto.ResponseStructure;
import org.expensetracker.model.Expense;
import org.expensetracker.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;


    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ResponseStructure<Page<Expense>>> getExpenses(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return expenseService.getExpenses(startDate, endDate, categoryId, page, size);
    }


    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ResponseStructure<ExpenseResponse>> addExpense(@RequestBody Expense expense) {
        return expenseService.addExpense(expense);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ResponseStructure<Expense>> updateExpense(@PathVariable Integer id,
                                                                    @RequestBody Expense expense) {
        return expenseService.updateExpense(id, expense);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ResponseStructure<String>> deleteExpense(@PathVariable Integer id) {
        return expenseService.deleteExpense(id);
    }
}