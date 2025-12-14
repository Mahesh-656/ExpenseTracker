package org.expensetracker.controller;

import org.expensetracker.dto.AuthRequest;
import org.expensetracker.dto.AuthResponse;
import org.expensetracker.dto.ResponseStructure;
import org.expensetracker.model.User;
import org.expensetracker.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final BCryptPasswordEncoder passwordEncoder;


    public AuthController(AuthService authService, BCryptPasswordEncoder passwordEncoder) {
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signup")
    public ResponseEntity<ResponseStructure<User>> createUser(@RequestBody User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return authService.createUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseStructure<AuthResponse>> login(@RequestBody AuthRequest authRequest){
        return authService.login(authRequest);
    }
}
