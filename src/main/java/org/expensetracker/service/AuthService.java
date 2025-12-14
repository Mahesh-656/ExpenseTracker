package org.expensetracker.service;

import org.expensetracker.dto.AuthRequest;
import org.expensetracker.dto.AuthResponse;
import org.expensetracker.dto.ResponseStructure;
import org.expensetracker.exception.MissingFieldsException;
import org.expensetracker.exception.NoResultFoundException;
import org.expensetracker.model.User;
import org.expensetracker.repository.UserRepository;
import org.expensetracker.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    public AuthService(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<ResponseStructure<User>> createUser(User user) {
        if(user!=null){
            userRepository.save(user);
            ResponseStructure<User> res=new ResponseStructure<>();
            res.setStatusCode(HttpStatus.CREATED.value());
            res.setMessage("User registered Successfully...");
            return new ResponseEntity<>(res,HttpStatus.CREATED);
        }
        throw new MissingFieldsException("Provide sufficient details");
    }

    public ResponseEntity<ResponseStructure<AuthResponse>> login(AuthRequest authRequest) {
        User user=userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(()->new  UsernameNotFoundException("No user found create the account"));
        if(!passwordEncoder.matches(authRequest.getPassword(),user.getPassword())){
            throw new NoResultFoundException("Password mismatched Please provide valid data");
        }
        String token=jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        ResponseStructure<AuthResponse> res=new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("User registered Successfully...");
        res.setData(new AuthResponse(token,user.getRole().name()));
        return new ResponseEntity<>(res,HttpStatus.OK);
    }
}
