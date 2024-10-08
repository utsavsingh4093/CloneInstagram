package com.java.main.service;

import com.java.main.entity.AddFollowers;
import com.java.main.entity.PostLike;
import com.java.main.entity.User;
import com.java.main.repository.AddFollowRepo;
import com.java.main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AddFollowService {

    @Autowired
    private AddFollowRepo addFollowRepo;

    @Autowired
    UserRepository userRepository;

    @Transactional//all database operations within the method are executed within a transaction context
    public String followers(int userId, int followedId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        System.out.println("Searching for user with ID: " + userId);

        Optional<AddFollowers> existingFollowers=addFollowRepo.findByUser_IdAndFollowedId(userId,followedId);

        if(existingFollowers.isPresent())
        {
            addFollowRepo.delete(existingFollowers.get());
            return "Follow removed successfully.";
        }

        else {
            String type="Follow";
            AddFollowers addFollowers=new AddFollowers();
            addFollowers.setFollowedId(followedId);
            addFollowers.setUser(user);
            addFollowers.setType(type);
            addFollowRepo.save(addFollowers);
            return "Like added successfully.";
        }
    }
}