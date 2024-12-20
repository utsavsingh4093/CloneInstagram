package com.java.main.controller;

import com.java.main.dto.UserWrapper;
import com.java.main.entity.User;
import com.java.main.service.UserServiceImp;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import java.io.IOException;
import java.util.Optional;

@Controller
public class PostControllers{

    @Autowired
    UserServiceImp userServiceImp;


//    @GetMapping("/view")
//    public String getViewPage(HttpSession httpSession) {
//        User user=(User)httpSession.getAttribute("user");
//        ModelAndView modelAndView=new ModelAndView();
//        System.out.println(user.getId()+"  jah ja iusa o ah ");
//        if(user!=null)
//        {
//            modelAndView.addObject("user",new User());
//            modelAndView.addObject("getUserId",user.getId());
//            modelAndView.addObject("username",user.getFirst_name()+" "+user.getLast_name());
//        }
//        return "redirect:/view/"+user.getId();
//    }

//    @GetMapping("/view/{id}")
//    public ModelAndView getUserData(@PathVariable int id, Model model, HttpSession httpSession)
//    {
//        Optional<User> user = userServiceImp.getUserById(id);
//        ModelAndView modelAndView=new ModelAndView("view");
//            modelAndView.addObject("user",user.get());
//        modelAndView.addObject("getUserId",id);
//        modelAndView.addObject("username",user.get().getFirst_name()+" "+user.get().getLast_name());
//            httpSession.setAttribute("user",user.get());
//            return modelAndView;
//    }


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

//    @GetMapping("/updateProfile")
//    public String getUpdateProfileWithId(Model model, HttpSession httpSession) {
//        User user = (User) httpSession.getAttribute("user");
//        if (user != null) {
//            return "redirect:/updateProfile/" + user.getId();
//        } else {
//            return "redirect:/login";
//        }
//    }

//    @GetMapping("/updateProfile/{id}")
//    public ModelAndView getDataOnUpdateProfile(@PathVariable int id, HttpSession httpSession) {
//        ModelAndView modelAndView=new ModelAndView("updateProfile");
//        Optional<User> user = userServiceImp.getUserById(id);
//        if (user.isPresent()) {
//            modelAndView.addObject("user", user.get());
//        }
//        return modelAndView;
//    }

//     FOR View The Data At JSON Format
//@GetMapping(value = "/updateProfile/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
//public ResponseEntity<Object> getDataOnUpdateProfile(@PathVariable int id) {
//    Optional<User> user = userServiceImp.getUserById(id);
//
//    if (user.isPresent()) {
//        UserWrapper userDTO = new UserWrapper(
//                user.get().getId(),
//                user.get().getFirst_name(),
//                user.get().getLast_name(),
//                user.get().getEmail(),
//                user.get().getCity(),
//                user.get().getState(),
//                user.get().getPassword(),
//                user.get().getStringImageFile(),
//                user.get().getFollowType()
//        );
//        return ResponseEntity.ok(userDTO);
//    } else {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                .body(Collections.singletonMap("alert", "User not found."));
//    }
//}




//    @PostMapping("/updateProfile")
//    public String updateProfile(@ModelAttribute("user") User user,@RequestParam int userId, @RequestParam("profileImage") MultipartFile file, Model model) throws IOException {
//        System.out.println(userId + " jasdh kjasndk n akjsfdk lasndjb aieh a");
//        Optional<User> optionalUser = userServiceImp.getUserById(userId);
//        if (!optionalUser.isPresent()) {
//            throw new RuntimeException("User not found with id: " + userId);
//        }
//        User existingUser = optionalUser.get();
//        // Update fields
//        existingUser.setFirst_name(user.getFirst_name());
//        existingUser.setLast_name(user.getLast_name());
//        existingUser.setEmail(user.getEmail());
//        existingUser.setCity(user.getCity());
//        existingUser.setState(user.getState());
//        existingUser.setPassword(user.getPassword()); // Ensure to hash it
//
//        // Handle image upload
//        if (!file.isEmpty()) {
//            existingUser.setImage_type(file.getContentType());
//            existingUser.setImage_name(file.getOriginalFilename());
//            existingUser.setImage_data(file.getBytes());
//        }
//
//        // Update user in the database
//        try {
//            userServiceImp.updateUser(existingUser);
//        } catch (Exception e) {
//            throw new RuntimeException("Error updating user in database: " + e.getMessage());
//        }
//
//        model.addAttribute("name", existingUser.getFirst_name() + " " + existingUser.getLast_name());
//        return "redirect:/view";
//    }

}
