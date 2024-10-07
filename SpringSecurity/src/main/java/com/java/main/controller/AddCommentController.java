package com.java.main.controller;

import com.java.main.entity.PostComment;
import com.java.main.service.AddCommentService;
import com.java.main.service.AddPostService;
import com.java.main.service.UserServiceImp;
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

    @Autowired
    AddPostService addPostService;

    @Autowired
    UserServiceImp userServiceImp;

    @PostMapping("/comment")
    public String addComment(@RequestParam Integer userId, @RequestParam Integer postId, @RequestParam String UserComment, Model model) {

        System.out.println(userId+ " : "+ postId + " : Here i am getting id's");
            PostComment postComment = new PostComment();
            postComment.setComment(UserComment);
            String message = addCommentService.addComment(userId, postId, postComment);

           // return ResponseEntity.ok("Comment added: " + message);
          //return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        return "redirect:/homepage";
    }

    @PostMapping("/commentOnUserProfile")
    public String addCommentOnUserProfile(@RequestParam Integer userId,@RequestParam Integer postId,@RequestParam String comment,Model model,PostComment postComment)
    {
        postComment.setComment(comment);
        addCommentService.addComment(userId,postId,postComment);

        return "redirect:/viewPosts";
    }

    @GetMapping("/getListOfComment")
    public String getListComment(@RequestParam Integer postId)
    {
        Optional<PostComment> postComment=addCommentService.findPostComment(postId);

        return "redirect:/homepage";
    }

    @GetMapping("/getListOfComments")
    public String getListCommentOnUserProfile(@RequestParam Integer postId)
    {
        Optional<PostComment> postComment=addCommentService.findPostComment(postId);

        return "redirect:/viewPosts";
    }

//    @GetMapping("/getUserNameById")
//    public String getUserNameById(@RequestParam Integer userId)
//    {
//        Optional<User> user=userServiceImp.getUserById(userId);
//        String name=user.get().getFirst_name()+" "+user.get().getLast_name();
//        System.out.println("Here i am getting my User : "+user.get().getFirst_name()+" "+user.get().getLast_name());
//        return name;
//    }
}