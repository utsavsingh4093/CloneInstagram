package com.java.main.controller;

import com.java.main.entity.User;
import com.java.main.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller//it use to handel http request
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String openRegsitrationPage(Model model) {
        model.addAttribute("user", new User());//it will use to transfer the value from one page to other page
        return "register";
    }

    @GetMapping("/login")
    public String openLoginPage(Model model) {
        model.addAttribute("user", new User());//it will use to transfer the value from one page to other page
        return "login";
    }


    @PostMapping("/regForm")
    //ModelAttribute it use to bind the data
    public String addUser(@ModelAttribute("user") User user,Model model) throws IOException {
       boolean status=userService.registerUser(user);
        if (status) {
            model.addAttribute("successMsg", "User is Registered Successfully");
        } else {
            model.addAttribute("errorMsg", "User is Not Registered; something went wrong");
        }
        return "register";
    }

    @PostMapping("/logForm")
    public String submitLoginForm(@ModelAttribute("user") User user, Model model) {
        User validUser = userService.loginUser(user.getEmail(), user.getPassword());
        if (validUser != null) {
            model.addAttribute("email", validUser.getEmail());
            return "home";
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
}
