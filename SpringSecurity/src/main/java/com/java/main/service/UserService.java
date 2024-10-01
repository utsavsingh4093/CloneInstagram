package com.java.main.service;

import com.java.main.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface UserService {

    public User registerUser(User user, MultipartFile image) throws IOException;
    public User loginUser(String email,String password);
}
