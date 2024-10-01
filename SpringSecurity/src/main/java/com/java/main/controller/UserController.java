package com.java.main.controller;

import com.java.main.entity.User;
import com.java.main.service.UserServiceImp;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    public String openHomePage() {
        return "home";
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
        return "redirect:/login";
    }




    @PostMapping("/logForm")
    public String submitLoginForm(@ModelAttribute("user") User user, Model model) {
        User validUser = userServiceImp.loginUser(user.getEmail(), user.getPassword());
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
