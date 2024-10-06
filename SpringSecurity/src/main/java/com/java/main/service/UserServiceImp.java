package com.java.main.service;

import com.java.main.entity.User;
import com.java.main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class UserServiceImp{

    @Autowired
    private UserRepository userRepository;

    public  User getById(Integer user_id){
        return userRepository.findById(user_id).orElse(null);
    }

    public User registerUser(User user) throws IOException {
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace(); // Log the error
            throw new RuntimeException("Error saving user to the database.");
        }
    }
    //Update User
    public User updateUser(User user) {
        // Basic validation
        if (user.getFirst_name() == null || user.getFirst_name().isEmpty()) {
            throw new IllegalArgumentException("First name is required.");
        }
        // Add other validation checks as needed

        try {
            return userRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace(); // Log the stack trace for debugging
            throw new RuntimeException("Error saving user to the database: " + e.getMessage());
        }
    }


    public User loginUser(String email, String password) {
        User validUser=userRepository.findByEmail(email);
        if(validUser!=null && validUser.getPassword().equals(password)){
            return validUser;
        }
        return null;
    }

    public Optional<User> getUserById(int id) {
        return userRepository.findById(id); // Assuming you're using JpaRepository
    }

    public User getAllProductById(int id) {
        return null;
    }

    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }
}