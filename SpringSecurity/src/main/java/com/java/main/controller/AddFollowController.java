package com.java.main.controller;

import com.java.main.service.AddFollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AddFollowController {
    @Autowired
    AddFollowService addFollowService;

    @PostMapping("/addFollow")
    public String addFollowers(@RequestParam int userId,@RequestParam int followedId) {
        System.out.println("Your User ID : "+userId);
        System.out.println("Your Followed ID : "+followedId);
            addFollowService.followers(userId, followedId);
            System.out.println("Data");
            return "redirect:/homepage";
    }
}
