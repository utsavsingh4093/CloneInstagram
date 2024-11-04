package com.java.main.restTemplates;

import com.java.main.dto.AddFollowersWrapper;
import com.java.main.dto.UserWrapper;
import com.java.main.entity.AddPost;
import com.java.main.entity.User;
import com.java.main.service.AddFollowService;
import com.java.main.service.AddPostService;
import com.java.main.service.UserServiceImp;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.IContext;

import java.util.*;

@RestController
public class GetAddFollowController {
    @Autowired
    AddFollowService addFollowService;
    @Autowired
    TemplateEngine templateEngine;
    @Autowired
    UserServiceImp userServiceImp;
    @Autowired
    AddPostService addPostService;

    @PostMapping(value = "/addFollow" , produces = {"application/json","text/html"})
    public ResponseEntity<Object> addFollowers(@RequestParam int userId, @RequestParam int followedId,
                               @RequestHeader(value = HttpHeaders.ACCEPT, defaultValue = "text/html") String acceptHeader) {
        System.out.println("Your User ID : " + userId);
        System.out.println("Your Followed ID : " + followedId);
        Map<String,Object> map=new HashMap<>();
        addFollowService.followers(userId, followedId);
        Optional<User> user = userServiceImp.getUserById(followedId);
        if (user.isPresent()) {
            List<AddPost> posts = addPostService.findListOfPostData(followedId);
            for (AddPost getPost : posts) {
                String img = Base64.getEncoder().encodeToString(getPost.getImage_data());
                getPost.setImage_string_data("data:image/png;base64," + img);
                System.out.println("Post ID: " + getPost.getPostId() + ", Name: " + getPost.getPost_name());
            }
            map.put("getUserId", userId);
            map.put("getPost", posts);
            map.put("user", user.get());
            map.put("usernames", user.get().getFirst_name() + " " + user.get().getLast_name());
        }
        if (acceptHeader.contains("application/json")) {
            map.clear();
            map.put("Message","Done");
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(map);
        } else {
            String html = renderTemplate("homepage", map);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(html);
        }
    }

    @PostMapping(value = "/listFollowing",produces = {"application/json","text/html"})
    public ResponseEntity<Object> addListOfFollowing(@RequestParam Integer userId, @RequestParam Integer followId,
                                     @RequestHeader(value = HttpHeaders.ACCEPT, defaultValue = "text/html") String acceptHeader ) {
        System.out.println("Your User ID : " + userId);
        System.out.println("Your Followed ID : " + followId);
        User user=userServiceImp.getById(userId);
        addFollowService.followers(userId, followId);
        List<AddFollowersWrapper> followers = addFollowService.getAllFollowingByUserIdAndFollowId(userId, followId);
        Map<String,Object> map=new HashMap<>();
        List<UserWrapper> following = userServiceImp.getUserList();
        for (UserWrapper userWrapper : following) {
            if (addFollowService.getByUserAndFollowerId(userId, userWrapper.getId()) != null) {
                userWrapper.setFollowType(addFollowService.getByUserAndFollowerId(userId, userWrapper.getId()).getType().toString());
            }
        }
        map.put("getAllUserForFollow", following);
        map.put("username", user.getFirst_name() + " " + user.getLast_name());
        map.put("userId", user.getId());
        if (acceptHeader.contains("application/json")) {
            map.clear();
            map.put("Message","Your Request send");
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(map);
        } else {
            String html = renderTemplate("listFollow", map);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(html);
        }
    }

//    @GetMapping(value = "/follower" , produces = {"application/json","text/html"})
//    public ResponseEntity<Object> getFollowers(@RequestParam int userId,@RequestHeader(value = HttpHeaders.ACCEPT, defaultValue = "text/html") String acceptHeader) {
//        List<AddFollowersWrapper> followers = addFollowService.getAllFollowersByUserId(userId);
//        User user=userServiceImp.getById(userId);
//        Map<String,Object> map=new HashMap<>();
//
//        List<AddFollowersWrapper> follow = addFollowService.getAllFollowersByUserId(userId);
//        System.out.println("Here is the size of followers : " + follow.size());
//        map.put("getAllFollowers", followers);
//        map.put("username", user.getFirst_name() + " " + user.getLast_name());
//        map.put("userId", userId);
//
//        if (acceptHeader.contains("application/json")) {
//            return ResponseEntity.ok()
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .body(map);
//        } else {
//            String html = renderTemplate("followers", map);
//            return ResponseEntity.ok()
//                    .contentType(MediaType.TEXT_HTML)
//                    .body(html);
//        }
//    }

    @GetMapping(value = "/listFollow" , produces = {"application/json","text/html"})
    public ResponseEntity<Object> getAllFollowAvailable(@RequestParam int userId,HttpSession session,@RequestHeader(value = HttpHeaders.ACCEPT, defaultValue = "text/html") String acceptHeader) {
        Map<String,Object> map =new HashMap<>();
        User user=userServiceImp.getById(userId);
        System.out.println(userId + " That is my USerID");
        List<UserWrapper> following=userServiceImp.getUserList();
        System.out.println("Number of users fetched: " + following);
        for(UserWrapper userWrapper : following){
            if(addFollowService.getByUserAndFollowerId(userId,userWrapper.getId())!=null){
                userWrapper.setFollowType(addFollowService.getByUserAndFollowerId(userId,userWrapper.getId()).getType().toString());
            }else{
                userWrapper.setFollowType(User.FollowType.FOLLOW.toString());
            }
        }
        map.put("getAllUserForFollow", following);
        map.put("username", user.getFirst_name() + " " + user.getLast_name());
        map.put("userId", user.getId());
        if (acceptHeader.contains("application/json")) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(map);
        } else {
            String html = renderTemplate("listFollow", map);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(html);
        }
    }

    @PostMapping(value = "/ListOFFollowers", produces = {"application/json", "text/html"})
    public ResponseEntity<Object> addListOfFollowers(@RequestParam int userId,@RequestParam int followedId,
                                                     @RequestHeader(value = HttpHeaders.ACCEPT, defaultValue = "text/html") String acceptHeader) {
        System.out.println("Your User ID : " + userId);
        System.out.println("Your Followed ID : " + followedId);
        Map<String,Object> map=new HashMap<>();
        addFollowService.followBack(userId, followedId);
        User user = userServiceImp.getById(userId);
        List<UserWrapper> followers = addFollowService.getAllFollowersByUserIds(followedId);
        map.put("getAllFollowers", followers);
        map.put("username", user.getFirst_name() + " " + user.getLast_name());
        map.put("userId", followedId);
        if (acceptHeader.contains("application/json")) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(map);
        } else {
            String html = renderTemplate("followers", map);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(html);
        }
    }

    @GetMapping(value = "/followers", produces = {"application/json", "text/html"})
    public ResponseEntity<Object> getFollowingData(@RequestParam int userId,
                                                   @RequestHeader(value = HttpHeaders.ACCEPT, defaultValue = "text/html") String acceptHeader) {
        System.out.println("asd ja ij ao"+userId);
        List<AddFollowersWrapper> follow = addFollowService.getAllFollowersByUserId(userId);
        List<UserWrapper> followers = addFollowService.getAllFollowersByUserIds(userId);
        Map<String,Object> map=new HashMap<>();
        map.put("getAllFollowers", followers);
        map.put("userId", userId);
        if (acceptHeader.contains("application/json")) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(map);
        } else {
            String html = renderTemplate("followers", map);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(html);
        }
    }

    private String renderTemplate(String viewName, Map<String, Object> model) {
        IContext context = new IContextImpl(model);
        return templateEngine.process(viewName, context);
    }
}