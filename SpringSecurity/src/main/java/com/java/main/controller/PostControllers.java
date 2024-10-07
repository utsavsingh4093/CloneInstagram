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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Controller
public class PostControllers{

    @Autowired
    UserServiceImp userServiceImp;


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


    @GetMapping("/updateProfile")
    public String getUpdateProfileWithId(Model model, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        if (user != null) {
            return "redirect:/updateProfile/" + user.getId();
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/updateProfile/{id}")
    public String getDataOnUpdateProfile(@PathVariable int id, Model model, HttpSession httpSession) {
        Optional<User> user = userServiceImp.getUserById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "updateProfile";
        } else {
            return "404"; // Handle user not found
        }
    }

    @PostMapping("/updateProfile")
    public String updateProfile(@ModelAttribute("user") User user, @RequestParam("profileImage") MultipartFile file, Model model) throws IOException {
        if (user.getId() <= 0) {
            throw new IllegalArgumentException("User ID must not be null for updates");
        }
        Optional<User> optionalUser = userServiceImp.getUserById(user.getId());
        if (!optionalUser.isPresent()) {
            throw new RuntimeException("User not found with id: " + user.getId());
        }
        User existingUser = optionalUser.get();
        // Update fields
        existingUser.setFirst_name(user.getFirst_name());
        existingUser.setLast_name(user.getLast_name());
        existingUser.setEmail(user.getEmail());
        existingUser.setCity(user.getCity());
        existingUser.setState(user.getState());
        existingUser.setPassword(user.getPassword()); // Ensure to hash it

        // Handle image upload
        if (!file.isEmpty()) {
            existingUser.setImage_type(file.getContentType());
            existingUser.setImage_name(file.getOriginalFilename());
            existingUser.setImage_data(file.getBytes());
        }

        // Update user in the database
        try {
            userServiceImp.updateUser(existingUser);
        } catch (Exception e) {
            throw new RuntimeException("Error updating user in database: " + e.getMessage());
        }

        model.addAttribute("name", existingUser.getFirst_name() + " " + existingUser.getLast_name());
        return "redirect:/view";
    }

}
