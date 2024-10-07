package com.java.main.controller;

import com.java.main.entity.PostComment;
import com.java.main.service.AddCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class AddCommentController {

    @Autowired
    AddCommentService addCommentService;

    @PostMapping("/comment")
    public String addComment(@RequestParam Integer userId, @RequestParam Integer postId, @RequestParam String UserComment) {

        System.out.println(userId+ " : "+ postId + " : Here i am getting id's");
            PostComment postComment = new PostComment();
            postComment.setComment(UserComment);
            String message = addCommentService.addComment(userId, postId, postComment);
           // return ResponseEntity.ok("Comment added: " + message);
          //return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        return "redirect:/homepage";
    }
}