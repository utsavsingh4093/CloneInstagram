package com.java.main.controller;

import com.java.main.entity.AddFollowers;
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
    public String addFollowers(@RequestParam Integer userId,@RequestParam Integer followedId, AddFollowers addFollowers) {
        String addType = "Following";
        try {
            addFollowers.setType(addType);
            addFollowers.setFollowedId(followedId);
            addFollowService.addFollower(userId, addType, addFollowers);
            System.out.println("Data");
            // Redirect to a success page or back to the original page
            return "redirect:/homepage"; // Adjust the redirect URL as needed
        } catch (Exception e) {
            System.out.println(e.getMessage());
            // Handle the error case, maybe return to the form with an error message
            return "redirect:/login"; // Adjust as necessary
        }
    }

}
