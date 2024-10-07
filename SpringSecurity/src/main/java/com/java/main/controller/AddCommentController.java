package com.java.main.controller;

import com.java.main.entity.PostComment;
import com.java.main.service.AddCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;

@Controller
public class AddCommentController {

    @Autowired
    AddCommentService addCommentService;

    @PostMapping("/comment")
    public ResponseEntity<String> addComment(@RequestBody Map<String, Object> request) {

        int userId = (Integer) request.get("userId");
        int postId = (Integer) request.get("postId");

        String commentText = (String) request.get("comment");

        try {
            PostComment postComment = new PostComment();
            postComment.setComment(commentText);
            String message = addCommentService.addComment(userId, postId, postComment);
            return ResponseEntity.ok("Comment added: " + message);
        }
        catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}