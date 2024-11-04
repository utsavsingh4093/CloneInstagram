package com.java.main.controller;

import com.java.main.dto.AddFollowersWrapper;
import com.java.main.entity.User;
import com.java.main.dto.UserWrapper;
import com.java.main.service.AddFollowService;
import com.java.main.service.UserServiceImp;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller

public class AddFollowController {
    @Autowired
    AddFollowService addFollowService;

    @Autowired
    UserServiceImp userServiceImp;

//    @PostMapping("/addFollow")
//    public String addFollowers(@RequestParam int userId, @RequestParam int followedId) {
//        System.out.println("Your User ID : " + userId);
//        System.out.println("Your Followed ID : " + followedId);
//        addFollowService.followers(userId, followedId);
//        System.out.println("Data");
//        return "redirect:/homepage";
//    }

//    @PostMapping("/listFollowing")
//    public String addListOfFollowing(@RequestParam Integer userId, @RequestParam Integer followId, Model model) {
//        System.out.println("Your User ID : " + userId);
//        System.out.println("Your Followed ID : " + followId);
//        addFollowService.followers(userId, followId);
//        List<AddFollowersWrapper> followers = addFollowService.getAllFollowingByUserIdAndFollowId(userId, followId);
//        return "redirect:/listFollow";
//    }

//    @GetMapping("/follower")
//    public ResponseEntity<List<AddFollowersWrapper>> getFollowers(@RequestParam int userId, Model model) {
//        List<AddFollowersWrapper> followers = addFollowService.getAllFollowersByUserId(userId);
//        model.addAttribute("getAllFollowers", followers);
//        return ResponseEntity.ok(followers);
//    }

//    @GetMapping("/listFollow")
//    public String getAllFollowAvailable(HttpSession session, Model model) {
//        User user = (User) session.getAttribute("user");
//        int userId = user.getId();
//        System.out.println(userId + " That is my USerID");
//        List<UserWrapper> following = userServiceImp.getUserList();
//        for (UserWrapper userWrapper : following) {
//            if (addFollowService.getByUserAndFollowerId(userId, userWrapper.getId()) != null) {
//                userWrapper.setFollowType(addFollowService.getByUserAndFollowerId(userId, userWrapper.getId()).getType().toString());
//            }
//        }
////        List<User> users = userServiceImp.findListOfUser();
////        List<AddFollowersWrapper> followers = addFollowService.getAllFollowersByUserId(userId);
//
//        model.addAttribute("getAllUserForFollow", following);
////        model.addAttribute("getAllFollowType",followers);
//        model.addAttribute("username", user.getFirst_name() + " " + user.getLast_name());
//        model.addAttribute("userId", user.getId());
//        return "listFollow";
//    }
//
//   // I want to Do Display List Of Followers

//    @PostMapping("/ListOFFollowers")
//    public String addListOfFollowers(@RequestParam int userId, @RequestParam int followedId, Model model) {
//        System.out.println("Your User ID : " + userId);
//        System.out.println("Your Followed ID : " + followedId);
//        addFollowService.followBack(userId, followedId);
//        return "redirect:/followers";
//    }

//    @GetMapping("/followers")
//    public String getFollowingData(Model model, HttpSession session) {
//
//        User user = (User) session.getAttribute("user");
//        int userId = user.getId();
//
//        List<AddFollowersWrapper> follow = addFollowService.getAllFollowersByUserId(userId);
//        System.out.println("Here is the size of followers : " + follow.size());
//
//        List<UserWrapper> followers = addFollowService.getAllFollowersByUserIds(userId);
//        model.addAttribute("getAllFollowers", followers);
//        model.addAttribute("username", user.getFirst_name() + " " + user.getLast_name());
//        model.addAttribute("userId", userId);
//        return "followers"; // Thymeleaf template
//    }
}