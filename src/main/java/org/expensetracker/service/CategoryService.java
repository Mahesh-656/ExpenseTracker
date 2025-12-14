package org.expensetracker.service;


import org.expensetracker.dto.ResponseStructure;
import org.expensetracker.exception.NoResultFoundException;
import org.expensetracker.model.Category;
import org.expensetracker.model.User;
import org.expensetracker.repository.CategoryRepository;
import org.expensetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<ResponseStructure<List<Category>>> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if (!categories.isEmpty()) {
            ResponseStructure<List<Category>> res = new ResponseStructure<>();
            res.setStatusCode(HttpStatus.FOUND.value());
            res.setMessage("Categories retrieved successfully...");
            res.setData(categories);
            return new ResponseEntity<>(res, HttpStatus.FOUND);
        }
        throw new NoResultFoundException("No categories found !!..");
    }
    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // subject from JWT
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NoResultFoundException("User not found for email: " + email));
    }
    public ResponseEntity<ResponseStructure<Category>> addCategory(Category category) {
        User currentUser = getCurrentUser(); // derive from JWT

        category.setCreatedBy(currentUser);

        Category saved = categoryRepository.save(category);

        ResponseStructure<Category> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.CREATED.value());
        res.setMessage("Category added successfully...");
        res.setData(saved);

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }


    public ResponseEntity<ResponseStructure<Category>> updateCategory(Integer id, Category category) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new NoResultFoundException("Category not found with id " + id));
        if (category.getName() != null) {
            existing.setName(category.getName());
        }
        ResponseStructure<Category> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Category updated successfully...");
        res.setData(categoryRepository.save(existing));
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    public ResponseEntity<ResponseStructure<String>> deleteCategory(Integer id) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new NoResultFoundException("Category not found with id " + id));
        categoryRepository.delete(existing);
        ResponseStructure<String> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Category deleted successfully...");
        res.setData("Deleted category id: " + id);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
