package org.expensetracker.service;

import org.expensetracker.dto.ResponseStructure;
import org.expensetracker.exception.NoResultFoundException;
import org.expensetracker.model.User;
import org.expensetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public ResponseEntity<ResponseStructure<List<User>>> getAllUsers() {
        List<User> users=userRepository.findAll();
        if(!users.isEmpty()){
            ResponseStructure<List<User>> res=new ResponseStructure<>();
            res.setStatusCode(HttpStatus.FOUND.value());
            res.setMessage("Users retrieved Successfully...");
            res.setData(users);
            return new ResponseEntity<>(res,HttpStatus.FOUND);
        }
        throw new NoResultFoundException("No users found !!..");
    }

    public ResponseEntity<ResponseStructure<User>> getUserById(Integer id) {
        User user=userRepository.findById(id)
                .orElseThrow(()->new NoResultFoundException("User not found on given id "+id));
        ResponseStructure<User> res=new ResponseStructure<>();
        res.setStatusCode(HttpStatus.FOUND.value());
        res.setMessage("User retrieved Successfully based on given id "+id);
        res.setData(user);
        return new ResponseEntity<>(res,HttpStatus.FOUND);
    }

    public ResponseEntity<ResponseStructure<User>> updateUserById(Integer id,User user) {
        User existingUser=userRepository.findById(id)
                .orElseThrow(()->new NoResultFoundException("User not found to update the given id "+id));
        if (user.getName() != null) {
            existingUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }
        if (user.getPassword() != null) {

            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));

        }
        ResponseStructure<User> res=new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("User registered Successfully...");
        res.setData(userRepository.save(existingUser));
        return new ResponseEntity<>(res,HttpStatus.OK);
    }
}
