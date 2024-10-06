package com.java.main.controller;

import com.java.main.entity.AddPost;
import com.java.main.entity.PostLike;
import com.java.main.entity.User;
import com.java.main.service.PostLikeService;
import com.java.main.service.AddPostService;
import com.java.main.service.UserServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Controller
//@SessionAttribute("userdata")//for getting user id at from usercontroller page
public class AddingPost {
    @Autowired
    private AddPostService addPostService;

    @Autowired
    UserServiceImp userServiceImp;

    @GetMapping("/addPost")
    public String getPost(Model model, @SessionAttribute("userdata") Integer userId) {
        System.out.println("jksdba alsdnf "+userId);
        model.addAttribute("addPost", new AddPost());
        return "addPost";
    }

    @PostMapping("/addPostForm")
    public String addingDataOnAddPost(@ModelAttribute("addPost") AddPost addPost, @RequestParam("profileImage") MultipartFile imageFile, @SessionAttribute("userdata") Integer userId, Model model) throws IOException {
        if (!imageFile.isEmpty()) {
            //There i,am getting the data and setting the data in it user

            System.out.println("That : "+userId);
            User user = userServiceImp.getById(userId);
            if (user != null) {
                addPost.setUser(user);
            }
            addPost.setImage_type(imageFile.getContentType());
            addPost.setImage_name(imageFile.getOriginalFilename());
            addPost.setImage_data(imageFile.getBytes());

            AddPost savePost = addPostService.addPostData(addPost);
            System.out.println(savePost.getPost_name());
        }
        return "redirect:/homepage";
    }

    @GetMapping("/viewPosts")
    public String findListOfPost(@SessionAttribute("userdata") Integer userId , Model model) {
        List<AddPost> posts = addPostService.findListOfPostData(userId);
        System.out.println("This is User ID : "+userId +" :  : ");
        System.out.println(userId+ " Here i am Getting my id");
        for (AddPost post : posts) {
            String img = Base64.getEncoder().encodeToString(post.getImage_data());
            post.setImage_string_data("data:image/png;base64,"+img);
            System.out.println("Post ID: " + post.getPostId() + ", Name: " + post.getPost_name());
        }
        model.addAttribute("addPost",posts);
        return "viewPosts";
    }

}
