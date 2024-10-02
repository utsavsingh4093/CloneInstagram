package com.java.main.controller;

import com.java.main.entity.AddPost;
import com.java.main.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PostControllers {

    @GetMapping("/addPost")
    public String getPost(Model model)
    {
        model.addAttribute("user",new AddPost());
        return "addPost";
    }
}
