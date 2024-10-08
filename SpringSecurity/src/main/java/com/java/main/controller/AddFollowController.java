package com.java.main.controller;

import com.java.main.entity.AddFollowers;
import com.java.main.entity.AddFollowersWrapper;
import com.java.main.service.AddFollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

@Controller

public class AddFollowController {
    @Autowired
    AddFollowService addFollowService;
    private static final Logger logger = LoggerFactory.getLogger(AddFollowController.class);

    @PostMapping("/addFollow")
    public String addFollowers(@RequestParam int userId,@RequestParam int followedId) {
        System.out.println("Your User ID : "+userId);
        System.out.println("Your Followed ID : "+followedId);
            addFollowService.followers(userId, followedId);
            System.out.println("Data");
            return "redirect:/homepage";
    }

    @GetMapping("/follower")
    public ResponseEntity<List<AddFollowersWrapper>> getFollowers(@RequestParam int userId) {
        List<AddFollowersWrapper> followers = addFollowService.getAllFollowersByUserId(userId);
        return ResponseEntity.ok(followers);
    }


}
