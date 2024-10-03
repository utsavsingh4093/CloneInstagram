package com.java.main.controller;

import com.java.main.entity.AddPost;
import com.java.main.entity.User;
import com.java.main.service.AddPostService;
import com.java.main.service.UserServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@SessionAttributes("userdata")//for getting user id at from usercontroller page
public class AddingPost {
    @Autowired
    private AddPostService addPostService;

    @Autowired
    UserServiceImp userServiceImp;
    @GetMapping("/addPost")
    public String getPost(@ModelAttribute("userdata") int id,Model model) {
        System.out.println(id+" Getting an id");
        model.addAttribute("addPost", new AddPost());
        return "addPost";
    }

    @PostMapping("/addPostForm")
    public String addingDataOnAddPost(@ModelAttribute("addPost") AddPost addPost, @RequestParam("profileImage") MultipartFile imageFile, @ModelAttribute("userdata") Integer userId, Model model) throws IOException {
      if(!imageFile.isEmpty())
      {
          //There i,am getting the data and setting the data in it user

          User user = userServiceImp.getById(userId);
          if(user!=null)
          {
              addPost.setUser(user);
          }
          addPost.setImage_type(imageFile.getContentType());
          addPost.setImage_name(imageFile.getOriginalFilename());
          addPost.setImage_data(imageFile.getBytes());

          AddPost savePost=addPostService.addPostData(addPost);
          System.out.println(savePost.getPost_name());
      }
        return "redirect:/homepage";
    }

}
