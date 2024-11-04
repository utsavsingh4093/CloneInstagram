package com.java.main.controller;

import com.java.main.entity.PostComment;
import com.java.main.service.AddCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Optional;

@Controller
public class AddCommentController {

    @Autowired
    AddCommentService addCommentService;

//    @PostMapping("/comment")
//    public String addComment(@RequestParam Integer userId, @RequestParam Integer postId, @RequestParam String UserComment,@RequestParam String userName, Model model) {
//
//        System.out.println(userId+ " : "+ postId + " : Here i am getting id's" + " : : "+userName);
//            PostComment postComment = new PostComment();
//            postComment.setComment(UserComment);
//            postComment.setUserName(userName);
//
//            String message = addCommentService.addComment(userId, postId , postComment);
//
//           // return ResponseEntity.ok("Comment added: " + message);
//          //return ResponseEntity.badRequest().body("Error: " + e.getMessage());
//        return "redirect:/homepage";
//    }

//    @PostMapping("/commentOnUserProfile")
//    public String addCommentOnUserProfile(@RequestParam Integer userId,@RequestParam Integer postId,@RequestParam String comment,@RequestParam String userName,PostComment postComment)
//    {
//        postComment.setComment(comment);
//        postComment.setUserName(userName);
//        addCommentService.addComment(userId,postId,postComment);
//
//        return "redirect:/viewPosts";
//    }
//
//    @GetMapping("/getListOfComment")
//    public String getListComment(@RequestParam Integer postId)
//    {
//        Optional<PostComment> postComment=addCommentService.findPostComment(postId);
//        return "redirect:/homepage";
//    }
//
//    @GetMapping("/getListOfComments")
//    public String getListCommentOnUserProfile(@RequestParam Integer postId)
//    {
//        Optional<PostComment> postComment=addCommentService.findPostComment(postId);
//        return "redirect:/viewPosts";
//    }

}