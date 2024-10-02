package com.java.main.controller;

import com.java.main.entity.User;
import com.java.main.service.UserServiceImp;
import jakarta.servlet.http.HttpServletRequest;
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

import java.awt.*;
import java.io.IOException;
import java.util.Optional;

@Controller//it use to handel http request
public class UserController {

    @Autowired
    private UserServiceImp  userServiceImp;

    @GetMapping("/register")
    public String openRegsitrationPage(Model model) {
        model.addAttribute("user", new User());//it will use to transfer the value from one page to other page
        return "register";
    }
    @GetMapping("/home")
    public String openHome() {
        return "home";
    }


    @GetMapping("/homepage")
    public String openHomePage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user"); // Retrieve user from session
        if (user != null) {
            model.addAttribute("user", user);
            return "homepage";
        } else {
            return "redirect:/login"; // Redirect to login if session expired
        }
    }



    @GetMapping("/login")
    public String openLoginPage(Model model) {
        model.addAttribute("user", new User());//it will use to transfer the value from one page to other page
        return "login";
    }


    @PostMapping("/regForm")
    @ResponseBody
    public String getRegisterForm(@ModelAttribute("user") User user, @RequestParam("profileImage") MultipartFile file, Model model) throws IOException {
        if (!file.isEmpty()){
            user.setImage_type(file.getContentType());
            user.setImage_name(file.getOriginalFilename());
            user.setImage_data(file.getBytes());
        }
        User student1 = userServiceImp.registerUser(user);
        System.out.println(student1.getFirst_name());
        model.addAttribute("name",student1.getFirst_name()+" "+student1.getLast_name());
        return "home";
    }

    @PostMapping("/logForm")
    public String submitLoginForm(@ModelAttribute("user") User user, Model model) {
        User validUser = userServiceImp.loginUser(user.getEmail(), user.getPassword());
        if (validUser != null) {
            return "redirect:/homepage/" + validUser.getId(); // Redirect to getdata with user ID
        } else {
            model.addAttribute("errorMsg", "Wrong email and password");
            return "login";
        }
    }


    @GetMapping("/logout")
        public String logoutForm(HttpServletRequest request)
        {
            HttpSession session=request.getSession(false);
            if(session!=null)
            {
                session.invalidate();
            }
            return "redirect:/login";
        }

    @GetMapping("/users/{id}/image")
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

    // Redirect to getdata page after login
    @GetMapping("/homepage/{id}")
    public String getUserData(@PathVariable int id, Model model,HttpSession httpSession) {
        Optional<User> user = userServiceImp.getUserById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            httpSession.setAttribute("user",user.get());//set user in session after reload the page
            return "homepage";
        } else {
            return "404";
        }
    }



}
