package com.java.main.service;

import com.java.main.entity.AddFollowers;
import com.java.main.entity.User;
import com.java.main.repository.AddFollowRepo;
import com.java.main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddFollowService {

    @Autowired
    private AddFollowRepo addFollowRepo;

    @Autowired
    UserRepository userRepository;

    public void addFollower(int userId, String type, AddFollowers addFollowers) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        System.out.println("Searching for user with ID: " + userId);

        addFollowers.setUser(user);
        addFollowers.setType(type);
        addFollowRepo.save(addFollowers);
    }
}