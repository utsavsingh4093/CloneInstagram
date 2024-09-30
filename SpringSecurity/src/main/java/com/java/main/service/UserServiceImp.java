package com.java.main.service;

import com.java.main.entity.User;
import com.java.main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class UserServiceImp implements UserService{

    @Autowired
    private UserRepository userRepository;


    @Override
    public boolean registerUser(User user) {
        try {
            userRepository.save(user);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User loginUser(String email, String password) {
          User validUser=userRepository.findByEmail(email);
          if(validUser!=null && validUser.getPassword().equals(password)){
              return validUser;
          }
          return null;
    }


}
