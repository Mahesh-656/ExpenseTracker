package org.expensetracker.dto;

import java.time.LocalDate;

public record ExpenseResponse(Integer id,
                              double amount,
                              String description,
                              LocalDate date,
                              Integer categoryId,
                              String categoryName,
                              Integer userId) {
}
