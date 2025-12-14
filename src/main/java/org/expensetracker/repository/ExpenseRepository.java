package org.expensetracker.repository;


import org.expensetracker.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface ExpenseRepository extends JpaRepository<Expense, Integer> {
    Page<Expense> findByUserId(Integer userId, Pageable pageable);
    Page<Expense> findByUserIdAndDateBetween(Integer userId, LocalDate start, LocalDate end, Pageable pageable);
    Page<Expense> findByUserIdAndCategoryId(Integer userId, Integer categoryId, Pageable pageable);
}
