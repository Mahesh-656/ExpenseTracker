package org.expensetracker.service;

import org.expensetracker.dto.ExpenseResponse;
import org.expensetracker.dto.ResponseStructure;
import org.expensetracker.exception.NoResultFoundException;
import org.expensetracker.model.Category;
import org.expensetracker.model.Expense;
import org.expensetracker.model.User;
import org.expensetracker.repository.CategoryRepository;
import org.expensetracker.repository.ExpenseRepository;
import org.expensetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // subject from JWT
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NoResultFoundException("User not found for email: " + email));
    }

    public ResponseEntity<ResponseStructure<Page<Expense>>> getExpenses(
            String startDate, String endDate, Integer categoryId, int page, int size) {

        User currentUser = getCurrentUser();
        PageRequest pageable = PageRequest.of(page, size);
        Page<Expense> expenses;

        if (startDate != null && endDate != null) {
            expenses = expenseRepository.findByUserIdAndDateBetween(
                    currentUser.getId(), LocalDate.parse(startDate), LocalDate.parse(endDate), pageable);
        } else if (categoryId != null) {
            expenses = expenseRepository.findByUserIdAndCategoryId(currentUser.getId(), categoryId, pageable);
        } else {
            expenses = expenseRepository.findByUserId(currentUser.getId(), pageable);
        }

        if (expenses.hasContent()) {
            ResponseStructure<Page<Expense>> res = new ResponseStructure<>();
            res.setStatusCode(HttpStatus.FOUND.value());
            res.setMessage("Expenses retrieved successfully...");
            res.setData(expenses);
            return new ResponseEntity<>(res, HttpStatus.FOUND);
        }
        throw new NoResultFoundException("No expenses found !!..");
    }

    public ResponseEntity<ResponseStructure<ExpenseResponse>> addExpense(Expense expense) {
        User currentUser = getCurrentUser();
        Category category = categoryRepository.findById(
                expense.getCategory().getId()
        ).orElseThrow(() -> new NoResultFoundException("Category not found"));

        expense.setCategory(category);

        expense.setUser(currentUser);
        Expense saved = expenseRepository.save(expense);

        ExpenseResponse response = new ExpenseResponse(
                saved.getId(),
                saved.getAmount(),
                saved.getDescription(),
                saved.getDate(),
                saved.getCategory().getId(),
                saved.getCategory().getName(),
                saved.getUser().getId()
        );


        ResponseStructure<ExpenseResponse> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.CREATED.value());
        res.setMessage("Expense added successfully...");
        res.setData(response);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    public ResponseEntity<ResponseStructure<Expense>> updateExpense(Integer id, Expense expense) {
        Expense existing = expenseRepository.findById(id)
                .orElseThrow(() -> new NoResultFoundException("Expense not found with id " + id));

        User currentUser = getCurrentUser();
        if (!currentUser.getRole().name().equals("ADMIN") &&
                !existing.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not authorized to update this expense");
        }

        if (expense.getAmount() != 0) existing.setAmount(expense.getAmount());
        if (expense.getDescription() != null) existing.setDescription(expense.getDescription());
        if (expense.getDate() != null) existing.setDate(expense.getDate());
        if (expense.getCategory() != null) existing.setCategory(expense.getCategory());

        ResponseStructure<Expense> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Expense updated successfully...");
        res.setData(expenseRepository.save(existing));
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    public ResponseEntity<ResponseStructure<String>> deleteExpense(Integer id) {
        Expense existing = expenseRepository.findById(id)
                .orElseThrow(() -> new NoResultFoundException("Expense not found with id " + id));

        User currentUser = getCurrentUser();
        // ✅ Ownership check
        if (!currentUser.getRole().name().equals("ADMIN") &&
                !existing.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not authorized to delete this expense");
        }

        expenseRepository.delete(existing);

        ResponseStructure<String> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Expense deleted successfully...");
        res.setData("Deleted expense id: " + id);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}