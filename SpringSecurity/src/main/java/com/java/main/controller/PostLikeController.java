package com.java.main.controller;
import com.java.main.service.PostLikeService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@Controller
public class PostLikeController {

    @Autowired
    private PostLikeService postLikeService;

    @PostMapping("/addLikes")
    //ResposeEntity is Use to give a plane text
    public String addLike(@RequestParam Integer userId, @RequestParam Integer postId) {
        System.out.println(userId + " This is UserID");
        System.out.println(postId + " This is Post Id");

        try {
            String message = postLikeService.addLike(userId, postId);
           // return ResponseEntity.ok("Like added: " + message);
        } catch (RuntimeException e) {
           // return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
        return "redirect:/homepage";
    }
    @PostMapping("/addLikesOnUserPost")
    //ResposeEntity is Use to give a plane text
    public String addLikeOnUserPost(@RequestParam Integer userId, @RequestParam Integer postId) {
        System.out.println(userId + " This is UserID");
        System.out.println(postId + " This is Post Id");

        try {
            String message = postLikeService.addLike(userId, postId);
            // return ResponseEntity.ok("Like added: " + message);
        } catch (RuntimeException e) {
            // return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
        return "redirect:/viewPosts";
    }

}
