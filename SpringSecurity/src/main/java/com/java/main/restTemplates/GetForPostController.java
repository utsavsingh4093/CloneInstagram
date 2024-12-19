package com.java.main.restTemplates;

import com.java.main.dto.UserWrapper;
import com.java.main.entity.User;
import com.java.main.service.AddFollowService;
import com.java.main.service.UserServiceImp;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.IContext;

import java.io.IOException;
import java.util.*;

@RestController
public class GetForPostController {
    @Autowired
    UserServiceImp userServiceImp;
    @Autowired
    AddFollowService addFollowService;
    @Autowired
    private TemplateEngine templateEngine;

    @GetMapping(value = "/updateProfile/{id}", produces = {"application/json", "text/html"})
    public ResponseEntity<Object> showUpdateData(@PathVariable String id,
            @RequestHeader(value = HttpHeaders.ACCEPT, defaultValue = "text/html") String acceptHeader) {

        Optional<User> optionalUser = userServiceImp.getUserById(Integer.parseInt(id));
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("alert", "User not found."));
        }
        User user = optionalUser.get();
        UserWrapper userWrapper = new UserWrapper(user.getId(),user.getFirst_name(),user.getLast_name(),user.getEmail(),user.getCity(),user.getState(),user.getPassword(),user.getStringImageFile(),user.getFollowType(),user.getUserName());
        Map<String, Object> variables = new HashMap<>();
        variables.put("user", userWrapper);
        variables.put("alert", "User is Fetched.");
        variables.put("id", user.getId());

        if (acceptHeader.contains("application/json")) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(variables);
        } else {
            String html = renderTemplate("updateProfile", variables);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(html);
        }
    }

    @PostMapping(value = "/updateProfile", produces = {"application/json", "text/html"})
    public ResponseEntity<Object> updateProfileData(@RequestParam int userId, @RequestPart(required = false) MultipartFile profileImage, @RequestParam(required = false) String first_name, @RequestParam(required = false) String last_name, @RequestParam(required = false) String email, @RequestParam(required = false) String city, @RequestParam(required = false) String state, @RequestParam(required = false) String password,
                                                    @RequestHeader(value = HttpHeaders.ACCEPT, defaultValue = "text/html") String acceptHeader) throws IOException {
        Optional<User> optionalUser = userServiceImp.getUserById(userId);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        User existingUser = optionalUser.get();
            existingUser.setCity(city);
            existingUser.setFirst_name(first_name);
            existingUser.setLast_name(last_name);
            existingUser.setEmail(email);
            existingUser.setState(state);

        if (profileImage != null && !profileImage.isEmpty()) {
            existingUser.setImage_type(profileImage.getContentType());
            existingUser.setImage_name(profileImage.getOriginalFilename());
            existingUser.setImage_data(profileImage.getBytes());
        }
        if (password != null && !password.isEmpty()) {
            existingUser.setPassword(password);
        }

        userServiceImp.updateUser(existingUser);

        Map<String, Object> map = new HashMap<>();
        map.put("user", existingUser);
        map.put("getUserId", existingUser.getId());
        map.put("username", existingUser.getFirst_name() + " " + existingUser.getLast_name());

        if (acceptHeader.contains("application/json")) {
            map.clear();
            map.put("Message", "User data updated successfully");
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(map);
        } else {
            String html = renderTemplate("updateProfile", map);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(html);
        }
    }

    @GetMapping(value = "/updateProfile" , produces = {"application/json", "text/html"})
    public ResponseEntity<Object> getUpdateProfileWithId(HttpSession httpSession , @RequestHeader(value = HttpHeaders.ACCEPT, defaultValue = "text/html") String acceptHeader) {
        User user = (User) httpSession.getAttribute("user");
        Map<String, Object> map = new HashMap<>();
        System.out.println(user.getId()+" jah ");
        map.put("user",user);
        if (acceptHeader.contains("application/json")) {
            map.put("message", "Rdirecct to Application Page");
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(map);
        }
        else {
            String redirectUrl = "/updateProfile/" + user.getId();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.LOCATION, redirectUrl);

            return ResponseEntity.status(HttpStatus.FOUND)
                    .headers(headers)
                    .build();
        }
    }
    @GetMapping(value = "/view" , produces = {"application/json","text/html"})
    public ResponseEntity<Object> getViewPage(HttpSession httpSession,@RequestHeader(value = HttpHeaders.ACCEPT, defaultValue = "text/html") String acceptHeader) {
        User user=(User)httpSession.getAttribute("user");
        Map<String, Object> map = new HashMap<>();
        map.put("user",user);
        map.put("getUserId",user.getId());
        map.put("username",user.getFirst_name()+" "+user.getLast_name());
        if (acceptHeader.contains("application/json")) {
            map.clear();
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(map);
        } else {
            String redirectUrl = "/view/" + user.getId();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.LOCATION, redirectUrl);

            return ResponseEntity.status(HttpStatus.FOUND)
                    .headers(headers)
                    .build();
        }
    }
    @GetMapping(value = "/view/{id}" , produces = {"application/json","text/html"})
    public ResponseEntity<Object> getUserData(@PathVariable String id, HttpSession httpSession,@RequestHeader(value = HttpHeaders.ACCEPT,defaultValue = "text/html") String acceptHeader)
    {
        Optional<User> user = userServiceImp.getUserById(Integer.parseInt(id));
        Map<String,Object> map=new HashMap<>();
        ModelAndView modelAndView=new ModelAndView("view");
        List<UserWrapper> followers = addFollowService.getAllFollowersByUserIds(user.get().getId());

        List<UserWrapper> following=userServiceImp.getUserList();
        System.out.println("Number of users fetched: " + following);

        int followingCount = 0;//Both count following and unfollow

        for(UserWrapper userWrapper : following){
            if(addFollowService.getByUserAndFollowerId(Integer.parseInt(id),userWrapper.getId())!=null){
                userWrapper.setFollowType(addFollowService.getByUserAndFollowerId(Integer.parseInt(id),userWrapper.getId()).getType().toString());
                if (userWrapper.getFollowType().equals(User.FollowType.FOLLOWING.toString()) || userWrapper.getFollowType().equals((User.FollowType.UNFOLLOW.toString()))) {
                    followingCount++;
                }
            }else{
                userWrapper.setFollowType(User.FollowType.FOLLOW.toString());
            }
        }
        System.out.println(followingCount +" This is my Following count");
        map.put("followingCount", followingCount);
        map.put("user",user.get());
        map.put("followersCount", followers.size());
        map.put("getUserId",id);
        map.put("username",user.get().getFirst_name()+" "+user.get().getLast_name());
        httpSession.setAttribute("user",user.get());
        if (acceptHeader.contains("application/json")) {
            map.clear();
            map.put("Message","Welcome To View Page.");
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(map);
        } else {
            String html = renderTemplate("view", map);
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