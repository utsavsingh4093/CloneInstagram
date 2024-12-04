package com.java.main.restTemplates;

import com.java.main.dto.PostWrapper;
import com.java.main.dto.UserWrapper;
import com.java.main.entity.AddPost;
import com.java.main.entity.User;
import com.java.main.service.AddFollowService;
import com.java.main.service.AddPostService;
import com.java.main.service.UserServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.IContext;

import java.io.IOException;
import java.util.*;

@RestController
public class GetForAddingPostController {

    @Autowired
    private AddPostService addPostService;
    @Autowired
    UserServiceImp userServiceImp;
    @Autowired
    TemplateEngine templateEngine;
    @Autowired
    AddFollowService addFollowService;

    @GetMapping(value = "/addPost" , produces = {"application/json","text/html"})
    public ResponseEntity<Object> getPost(@RequestParam("userId") Integer userId,
                          @RequestHeader(value = HttpHeaders.ACCEPT, defaultValue = "text/html") String acceptHeader) {
        System.out.println("jksdba alsdnf " + userId);
        Map<String, Object> map = new HashMap<>();
        map.put("addPost", new AddPost());
        map.put("userId",userId);
        if (acceptHeader.contains("application/json")) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(map);
        } else {
            String html = renderTemplate("addPost", map);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(html);
        }
    }

    @PostMapping(value = "/addPostForm", produces = {"application/json", "text/html"})
    public ResponseEntity<Object> addingDataOnAddPost(@RequestParam("post_name") String postName, @RequestParam("caption") String caption, @RequestParam("userId") Integer userId, @RequestParam("profileImage") MultipartFile imageFile,
            @RequestHeader(value = HttpHeaders.ACCEPT, defaultValue = "text/html") String acceptHeader) throws IOException {

        AddPost addPost = new AddPost();
        addPost.setPost_name(postName);
        addPost.setCaption(caption);

        User user = userServiceImp.getById(userId);
        if (user != null) {
            addPost.setUser(user);
        }

        addPost.setImage_type(imageFile.getContentType());
        addPost.setImage_name(imageFile.getOriginalFilename());
        addPost.setImage_data(imageFile.getBytes());
        AddPost savedPost = addPostService.addPostData(addPost);
        System.out.println(savedPost.getPost_name());
        Map<String, Object> map = new HashMap<>();
        if (acceptHeader.contains("application/json")) {
            map.put("Message", "Post Added Successfully");
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(map);
        } else {
            String html = renderTemplate("addPost", map);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(html);
        }
    }


    @GetMapping(value = "/viewPosts" , produces = {"application/json","text/html"})
    public ResponseEntity<Object> findListOfPost(@RequestParam("userId") Integer userId ,
                                 @RequestHeader(value = HttpHeaders.ACCEPT, defaultValue = "text/html") String acceptHeader) {
        Optional<User> user=userServiceImp.getUserById(userId);
        List<AddPost> posts = addPostService.findListOfPost(userId);
        System.out.println("This is User ID : "+userId +" :  : ");
        System.out.println(userId+ " Here i am Getting my id");
        Map<String,Object> map=new HashMap<>();
        map.put("userId", userId);
        List<UserWrapper> followers = addFollowService.getAllFollowersByUserIds(user.get().getId());

        List<UserWrapper> following=userServiceImp.getUserList();
        System.out.println("Number of users fetched: " + following);

        int followingCount = 0;//Both count following and unfollow

        for(UserWrapper userWrapper : following){
            if(addFollowService.getByUserAndFollowerId(userId,userWrapper.getId())!=null){
                userWrapper.setFollowType(addFollowService.getByUserAndFollowerId(userId,userWrapper.getId()).getType().toString());
                if (userWrapper.getFollowType().equals(User.FollowType.FOLLOWING.toString()) || userWrapper.getFollowType().equals((User.FollowType.UNFOLLOW.toString()))) {
                    followingCount++;
                }
            }else{
                userWrapper.setFollowType(User.FollowType.FOLLOW.toString());
            }
        }
        System.out.println(followingCount +" YTHOsali ioaj a");
        map.put("followingCount", followingCount);
        map.put("postCount",posts.size());
        map.put("user",user.get());
        map.put("followersCount", followers.size());
        map.put("username",user.get().getUserName());
        map.put("usernames",user.get().getFirst_name()+" "+user.get().getLast_name());
        List<PostWrapper> postWrappers=new ArrayList<>();
        for (AddPost post : posts) {
            PostWrapper postWrapperData = new PostWrapper();
            postWrapperData.setPostId(post.getPostId());
            postWrapperData.setPost_name(post.getPost_name());
            postWrapperData.setCaption(post.getCaption());
            postWrapperData.setImage_type(post.getImage_type());
            postWrapperData.setImage_name(post.getImage_name());
            postWrapperData.setLikesCount(post.getLikesCount());
            if (post.getImage_data() != null) {
                String img = Base64.getEncoder().encodeToString(post.getImage_data());
                post.setImage_string_data("data:image/png;base64," + img);
            }
            postWrapperData.setUserId(post.getUser().getId());
            System.out.println("Post ID: " + post.getPostId() + ", Name: " + post.getPost_name());
       postWrappers.add(postWrapperData);
        }
        map.put("addPost",posts);
        Map<String,Object> mapForPostWrapper=new HashMap<>();
        mapForPostWrapper.put("getdataPost",postWrappers);
        if (acceptHeader.contains("application/json")) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapForPostWrapper);
        } else {
            String html = renderTemplate("viewPosts", map);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(html);
        }
    }

    @GetMapping(value = "/viewPost" , produces = {"application/json","text/html"})
    public ResponseEntity<Object> findPostAccordingToUserId(@RequestParam("userId") Integer userId,@RequestParam("userID") Integer userID , @RequestHeader(value = HttpHeaders.ACCEPT, defaultValue = "text/html") String acceptHeader)
    {
        System.out.println("uherk adsjkb : "+ userId);
        Optional<User> user=userServiceImp.getUserById(userId);
        Optional<User> getCurrentUserName=userServiceImp.getUserById(userID);
        List<AddPost> posts = addPostService.findListOfPost(userId);
        System.out.println("This is User ID : "+userId +" :  : " + posts.size());
        System.out.println(userId+ " Here i am Getting my id");
        Map<String,Object> map=new HashMap<>();
        map.put("userId", userId);
        List<UserWrapper> followers = addFollowService.getAllFollowersByUserIds(user.get().getId());

        List<UserWrapper> following=userServiceImp.getUserList();
        System.out.println("Number of users fetched: " + following);

        int followingCount = 0;//Both count following and unfollow

        for(UserWrapper userWrapper : following){
            if(addFollowService.getByUserAndFollowerId(userId,userWrapper.getId())!=null){
                userWrapper.setFollowType(addFollowService.getByUserAndFollowerId(userId,userWrapper.getId()).getType().toString());
                if (userWrapper.getFollowType().equals(User.FollowType.FOLLOWING.toString()) || userWrapper.getFollowType().equals((User.FollowType.UNFOLLOW.toString()))) {
                    followingCount++;
                }
            }else{
                userWrapper.setFollowType(User.FollowType.FOLLOW.toString());
            }
        }
        System.out.println(followingCount +" YTHOsali ioaj a");
        map.put("followingCount", followingCount);
        map.put("postCount",posts.size());
        map.put("user",user.get());
        map.put("followersCount", followers.size());
        map.put("username",getCurrentUserName.get().getUserName());
        map.put("usernames",user.get().getFirst_name()+" "+user.get().getLast_name());
        List<PostWrapper> postWrappers=new ArrayList<>();
        for (AddPost post : posts) {
            PostWrapper postWrapperData = new PostWrapper();
            postWrapperData.setPostId(post.getPostId());
            postWrapperData.setPost_name(post.getPost_name());
            postWrapperData.setCaption(post.getCaption());
            postWrapperData.setImage_type(post.getImage_type());
            postWrapperData.setImage_name(post.getImage_name());
            postWrapperData.setLikesCount(post.getLikesCount());
            if (post.getImage_data() != null) {
                String img = Base64.getEncoder().encodeToString(post.getImage_data());
                post.setImage_string_data("data:image/png;base64," + img);
            }
            postWrapperData.setUserId(post.getUser().getId());
            System.out.println("Post ID: " + post.getPostId() + ", Name: " + post.getPost_name());
            postWrappers.add(postWrapperData);
        }
        map.put("addPost",posts);
        Map<String,Object> mapForPostWrapper=new HashMap<>();
        mapForPostWrapper.put("getdataPost",postWrappers);
        if (acceptHeader.contains("application/json")) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(postWrappers);
        } else {
            String html = renderTemplate("viewPosts", map);
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
