package com.java.main.service;

import com.java.main.entity.User;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface UserService {

    public boolean registerUser(User user);
    public User loginUser(String email,String password);
}
