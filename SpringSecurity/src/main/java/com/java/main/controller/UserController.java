package com.java.main.controller;

import com.java.main.dto.PostWrapper;
import com.java.main.entity.AddPost;
import com.java.main.entity.User;
import com.java.main.service.AddPostService;
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
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Controller//it use to handel http request
@SessionAttributes("userdata")
public class UserController {

    @Autowired
    private UserServiceImp userServiceImp;

    @Autowired
    AddPostService addPostService;
//
//    @GetMapping("/register")
//    public String openRegsitrationPage(Model model) {
//        model.addAttribute("user", new User());//it is use to transfer the value from one page to other page
//        return "register";
//    }
//
//    @GetMapping("/home")
//    public String openHome() {
//        return "home";
//    }
//
//    @GetMapping("/homepage")
//    public String openHomePage(Model model, HttpSession session) {
//        User user = (User) session.getAttribute("user");
//        System.out.println(user.getId()+" afn afa adfqa");
//        if (user != null) {
//            model.addAttribute("user", user);
//            List<AddPost> posts=addPostService.findListOfPostData(user.getId());
//
//            for(AddPost getPost : posts)
//            {
//                String img= Base64.getEncoder().encodeToString(getPost.getImage_data());
//                getPost.setImage_string_data("data:image/png;base64,"+img);
//                System.out.println("Post ID: " + getPost.getPostId() + ", Name: " + getPost.getPost_name());
//            }
//             model.addAttribute("username",user.getFirst_name()+" "+user.getLast_name());
//            model.addAttribute("getPost",posts);
//            model.addAttribute("getUserId",user.getId());
//            return "homepage";
//        } else {
//            return "redirect:/login"; // Redirect to login if session expired
//        }
//    }
//
//    @GetMapping("/login")
//    public String openLoginPage(Model model) {
//        model.addAttribute("user", new User());//it will use to transfer the value from one page to other page
//        return "login";
//    }
//
//    @PostMapping("/regForm")
//    public String getRegisterForm(@ModelAttribute("user") User user, @RequestParam("profileImage") MultipartFile file, Model model) throws IOException {
//        if (!file.isEmpty()) {
//            user.setImage_type(file.getContentType());
//            user.setImage_name(file.getOriginalFilename());
//            user.setImage_data(file.getBytes());
//        }
//        User student1 = userServiceImp.registerUser(user);
//        System.out.println(student1.getFirst_name());
//        //  model.addAttribute("name",student1.getFirst_name()+" "+student1.getLast_name());
//        return "login";
//    }

//    @PostMapping("/logForm")
//    public String submitLoginForm(@ModelAttribute("user") User user, Model model) {
//        User validUser = userServiceImp.loginUser(user.getEmail(), user.getPassword());
//        if (validUser != null) {
//            model.addAttribute("username", validUser.getFirst_name() + " " + validUser.getLast_name());
//            model.addAttribute("userdata", validUser.getId());
//            model.addAttribute("getUserId",validUser.getId());
//            System.out.println(validUser.getId() + " here i am getting user id");
//            return "redirect:/homeData/" + validUser.getId();
//
//        } else {
//            model.addAttribute("errorMsg", "Wrong email and password");
//            return "login";
//        }
//    }


<<<<<<< HEAD
//    @GetMapping("/logout")
//    public String logoutForm(HttpServletRequest request) {
//        HttpSession session = request.getSession(false);
//        if (session != null) {
//            session.invalidate();
//        }
//        return "redirect:/login";
//    }
=======
    @GetMapping("/homepage")
    public String openHomePage(@SessionAttribute("userdata") Integer userId,Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
            List<AddPost> posts=addPostService.findListOfPostData(userId);

            for(AddPost getPost : posts)
            {
                String img= Base64.getEncoder().encodeToString(getPost.getImage_data());
                getPost.setImage_string_data("data:image/png;base64,"+img);
                System.out.println("Post ID: " + getPost.getPostId() + ", Name: " + getPost.getPost_name());
            }
             model.addAttribute("username",user.getFirst_name()+" "+user.getLast_name());
            model.addAttribute("getPost",posts);
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
    public String getRegisterForm(@ModelAttribute("user") User user, @RequestParam("profileImage") MultipartFile file, Model model) throws IOException {
        if (!file.isEmpty()) {
            user.setImage_type(file.getContentType());
            user.setImage_name(file.getOriginalFilename());
            user.setImage_data(file.getBytes());
        }
        User student1 = userServiceImp.registerUser(user);
        System.out.println(student1.getFirst_name());
        //  model.addAttribute("name",student1.getFirst_name()+" "+student1.getLast_name());
        return "login";
    }

    @PostMapping("/logForm")
    public String submitLoginForm(@ModelAttribute("user") User user, Model model) {
        User validUser = userServiceImp.loginUser(user.getEmail(), user.getPassword());
        if (validUser != null) {
            model.addAttribute("username", validUser.getFirst_name() + " " + validUser.getLast_name());
            model.addAttribute("userdata", validUser.getId());
            System.out.println(validUser.getId() + " here i am getting user id");
            // Redirect to getdata with user ID
            return "redirect:/homeData/" + validUser.getId();

        } else {
            model.addAttribute("errorMsg", "Wrong email and password");
            return "login";
        }
    }


    @GetMapping("/logout")
    public String logoutForm(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login";
    }
>>>>>>> origin/main

    @GetMapping("/users/{id}/image")
    public ResponseEntity<ByteArrayResource> getUserImage(@PathVariable int id) {
        Optional<User> user = userServiceImp.getUserById(id);
        if (user.isPresent() && user.get().getImage_data() != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(user.get().getImage_type()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + user.get().getImage_name() + "\"")
                    .body(new ByteArrayResource(user.get().getImage_data()));
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    // it will send you to get data page after login
//        @GetMapping("/homeData/{id}")
//        public ModelAndView getUserData(@PathVariable int id, HttpSession httpSession) {
//            ModelAndView modelAndView = new ModelAndView("homepage");
//
//            Optional<User> user = userServiceImp.getUserById(id);
//            if (user.isPresent()) {
//                List<AddPost> posts = addPostService.findListOfPostData(id);
//                for (AddPost getPost : posts) {
//                    String img = Base64.getEncoder().encodeToString(getPost.getImage_data());
//                    getPost.setImage_string_data("data:image/png;base64," + img);
//                    System.out.println("Post ID: " + getPost.getPostId() + ", Name: " + getPost.getPost_name());
//                }
//
//                modelAndView.addObject("getPost", posts);
//                modelAndView.addObject("getUserId",id);
//                modelAndView.addObject("user", user.get());
//                modelAndView.addObject("username", user.get().getFirst_name() + " " + user.get().getLast_name());
//                httpSession.setAttribute("user", user.get());
//                httpSession.setAttribute("userusername", user.get());
//            } else {
//                modelAndView.setViewName("404");
//            }
//            return modelAndView;
//        }
}