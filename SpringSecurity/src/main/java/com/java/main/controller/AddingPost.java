package com.java.main.controller;

import com.java.main.entity.AddPost;
import com.java.main.entity.User;
import com.java.main.service.AddPostService;
import com.java.main.service.UserServiceImp;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.repository.Repository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@SessionAttributes("userdata")//for getting user id at from usercontroller page
public class AddingPost {
    @Autowired
    private AddPostService addPostService;

    @Autowired
    UserServiceImp userServiceImp;

    @GetMapping("/addPost")
    public String getPost(Model model) {
        model.addAttribute("addPost", new AddPost());
        return "addPost";
    }

    @PostMapping("/addPostForm")
    public String addingDataOnAddPost(@ModelAttribute("addPost") AddPost addPost, @RequestParam("profileImage") MultipartFile imageFile, @RequestParam("userId") Integer userId, Model model) throws IOException {
        if (!imageFile.isEmpty()) {
            //There i,am getting the data and setting the data in it user
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
    public String getAllPosts(Model model) {
        List<AddPost> addPostList = addPostService.findListOfPost();
        System.out.println(addPostList.size());
        for (AddPost post : addPostList) {
            System.out.println("Post ID: " + post.getPost_id() + ", Name: " + post.getPost_name());
        }
        model.addAttribute("addPost", addPostList);
        return "viewPosts";
    }

    @GetMapping("/addPost/{id}/image")
    public ResponseEntity<ByteArrayResource> getPostImages(@PathVariable int id) {
        Optional<AddPost> addPost = addPostService.getUserById(id);
        System.out.println(addPost.get().getPost_id()+" asjdn akjsa ;jad s");
        if (addPost.isPresent() && addPost.get().getImage_data() != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(addPost.get().getImage_type()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + addPost.get().getImage_name() + "\"")
                    .body(new ByteArrayResource(addPost.get().getImage_data()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
