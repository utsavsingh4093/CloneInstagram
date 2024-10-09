package com.java.main.controller;

import com.java.main.entity.AddFollowers;
import com.java.main.entity.AddFollowersWrapper;
import com.java.main.entity.User;
import com.java.main.repository.UserRepository;
import com.java.main.service.AddFollowService;
import com.java.main.service.UserServiceImp;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @Autowired
    UserServiceImp userServiceImp;

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
    public ResponseEntity<List<AddFollowersWrapper>> getFollowers(@RequestParam int userId,Model model) {
        List<AddFollowersWrapper> followers = addFollowService.getAllFollowersByUserId(userId);
        model.addAttribute("getAllFollowers",followers);
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/following")
    public String getFollowingData(Model model, HttpSession session)
    {
        User user=(User) session.getAttribute("user");
        int userId=user.getId();
        System.out.println(userId+" That is my USerID");
        List<AddFollowers> followers = addFollowService.getAllFollowersByUserIds(userId);
        model.addAttribute("getAllFollowers",followers);

        if (user != null) {
            model.addAttribute("username",user.getFirst_name()+" "+user.getLast_name());
            model.addAttribute("userId",user.getId());
        }
        return "following";
    }

}
