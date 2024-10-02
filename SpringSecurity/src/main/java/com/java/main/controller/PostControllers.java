package com.java.main.controller;

import com.java.main.entity.AddPost;
import com.java.main.entity.User;
import com.java.main.service.UserServiceImp;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class PostControllers{

    @Autowired
    UserServiceImp userServiceImp;
    @GetMapping("/addPost")
    public String getPost(Model model) {
        model.addAttribute("user", new AddPost());
        return "addPost"; // Ensure there is a corresponding addPost.html template
    }

    @GetMapping("/view")
    public String getViewPage(Model model, HttpSession httpSession) {
        User user=(User)httpSession.getAttribute("user");
        if(user!=null)
        {
            model.addAttribute("user",new User());
        }
        else{
            return "redirect:/login";
        }
        return "redirect:/view/"+user.getId();
    }

    @GetMapping("/view/{id}")
    public String getUserData(@PathVariable int id, Model model, HttpSession httpSession)
    {
        Optional<User> user = userServiceImp.getUserById(id);
        if(user.isPresent()){
            model.addAttribute("user",user.get());
            httpSession.setAttribute("user",user.get());
            return "view";
        }
        else{
            return "404";
        }
    }

    @GetMapping("/user/{id}/image")
    public ResponseEntity<ByteArrayResource> getUserImage(@PathVariable int id) {
        Optional<User> user = userServiceImp.getUserById(id);
        if (user.isPresent() && user.get().getImage_data() != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(user.get().getImage_type()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + user.get().getImage_name() + "\"")
                    .body(new ByteArrayResource(user.get().getImage_data()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
