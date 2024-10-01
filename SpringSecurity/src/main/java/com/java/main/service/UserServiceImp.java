package com.java.main.service;

import com.java.main.entity.User;
import com.java.main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class UserServiceImp{

    @Autowired
    private UserRepository userRepository;


    public User registerUser(User user) throws IOException {
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace(); // Log the error
            throw new RuntimeException("Error saving user to the database.");
        }
    }


    public User loginUser(String email, String password) {
          User validUser=userRepository.findByEmail(email);
          if(validUser!=null && validUser.getPassword().equals(password)){
              return validUser;
          }
          return null;
    }


}
