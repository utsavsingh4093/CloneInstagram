package com.java.main.service;

import com.java.main.entity.User;
import com.java.main.dto.UserWrapper;
import com.java.main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp{

    @Autowired
    private UserRepository userRepository;

    public  User getById(Integer user_id){
        return userRepository.findById(user_id).orElse(null);
    }
    public boolean isEmailUnique(String email) {
        return userRepository.findByEmail(email) == null;
    }
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User registerUser(User user) throws IOException {
        try {
            if (forSpaceChecking(user.getFirst_name()) ||
                    forSpaceChecking(user.getLast_name()) ||
                    forSpaceChecking(user.getEmail()) ||
                    forSpaceChecking(user.getCity()) ||
                    forSpaceChecking(user.getState())) {
                throw new RuntimeException("User Register can't contain space.");
            }
            return userRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("User is not registering: ");
        }
    }
    private boolean forSpaceChecking(String value) {
        return value == null || value.trim().isEmpty();
    }


    public User updateUser(User user) {
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Data is not updating: ");
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
        return userRepository.findById(id);
    }


    public List<UserWrapper> getUserList()
    {
        List<UserWrapper> list = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            System.out.println(user.getFirst_name()+" "+user.getLast_name());
            System.out.println(user.getEmail());
            UserWrapper wrapper = new UserWrapper(user.getId(),user.getFirst_name(),user.getLast_name(),user.getEmail(),user.getCity(),user.getState(),user.getPassword(),user.getStringImageFile(),user.getFollowType(),user.getUserName());
            list.add(wrapper);
        }
        return list;
    }
}