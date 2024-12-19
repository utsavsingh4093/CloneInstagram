package com.java.main.restTemplates;

import com.java.main.dto.CommentWrapper;
import com.java.main.dto.LikeWrapper;
import com.java.main.dto.PostWrapper2;
import com.java.main.dto.UserWrapper;
import com.java.main.entity.AddPost;
import com.java.main.entity.User;
import com.java.main.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.IContext;

import java.util.*;

@RestController
public class GetForLikeController {

    @Autowired
    private PostLikeService postLikeService;
    @Autowired
    private UserServiceImp userServiceImp;
    @Autowired
    private AddPostService addPostService;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    AddFollowService addFollowService;

    @PostMapping(value = "/addLikes", produces = {"text/html","application/json"})
    public ResponseEntity<Object> addLike(@RequestPart AddPost post,
                                          @RequestHeader(value = HttpHeaders.ACCEPT, defaultValue = "text/html") String acceptHeader) {
        int userId = post.getUser().getId();
        int postId = post.getPostId();
        postLikeService.addLike(userId, postId);
        Map<String, Object> response = new HashMap<>();
        Optional<User> user = userServiceImp.getUserById(userId);
        List<AddPost> posts = addPostService.findListOfPostData(userId);
        for (AddPost getPost : posts) {
            String img = Base64.getEncoder().encodeToString(getPost.getImage_data());
            getPost.setImage_string_data("data:image/png;base64," + img);
        }
        response.put("getUserId", userId);
        response.put("getPost", posts);
        response.put("user", user.orElse(null));
        response.put("postId", postId);
        System.out.println(posts.get(0).getPostId());
        System.out.println(posts.get(0).getLikesCount());
        response.put("username", user.get().getFirst_name()+" "+user.get().getLast_name());
        if (acceptHeader.contains("application/json")) {
            response.clear();
            response.put("Message", "Like Is Pressed");
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        }
        else {
            String html = renderTemplate("homepage", response);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(html);
        }
    }

    @PostMapping(value = "/addLikesOnUserPost", produces = {"application/json", "text/html"})
    public ResponseEntity<Object> addLikeOnUserPost(@RequestPart AddPost post,
                                                    @RequestHeader(value = HttpHeaders.ACCEPT, defaultValue = "text/html") String acceptHeader) {

        int userId = post.getUser().getId();
        int postId = post.getPostId();
        System.out.println(userId + " This is UserID");
        System.out.println(postId + " This is Post Id");
        Map<String, Object> map = new HashMap<>();
        postLikeService.addLike(userId, postId);
        Optional<User> user = userServiceImp.getUserById(userId);
        List<AddPost> posts = addPostService.findListOfPost(userId);
        System.out.println("This is User ID : " + userId + " :  : ");
        System.out.println(userId + " Here i am Getting my id");
        map.put("getUserId", userId);
        map.put("getPost", posts);
        map.put("user", user.orElse(null));
        map.put("postId", postId);
        map.put("username", user.get().getFirst_name() + " " + user.get().getLast_name());

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

        for (AddPost post1 : posts) {
            String img = Base64.getEncoder().encodeToString(post1.getImage_data());
            post1.setImage_string_data("data:image/png;base64," + img);
            System.out.println("Post ID: " + post1.getPostId() + ", Name: " + post1.getPost_name());
        }
        map.put("addPost", posts);

        if (acceptHeader.contains("application/json")) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(map);
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
