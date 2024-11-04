package com.java.main.restTemplates;

import com.java.main.entity.AddPost;
import com.java.main.entity.PostComment;
import com.java.main.entity.User;
import com.java.main.service.AddCommentService;
import com.java.main.service.AddPostService;
import com.java.main.service.PostLikeService;
import com.java.main.service.UserServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.IContext;

import java.util.*;

@RestController
public class getForCommentController {

    @Autowired
    AddCommentService addCommentService;
    @Autowired
    UserServiceImp userServiceImp;
    @Autowired
    AddPostService addPostService;
    @Autowired
    TemplateEngine templateEngine;

    @PostMapping(value = "/comment", produces = {"application/json", "text/html"})
    public ResponseEntity<Object> addComment(@RequestParam Integer userId, @RequestParam Integer postId, @RequestParam String UserComment, @RequestParam String userName,
                                             @RequestHeader(value = HttpHeaders.ACCEPT, defaultValue = "text/html") String acceptHeader) {
        if (UserComment == null || UserComment.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Comment cannot be Null"));
        }
        System.out.println(userId + " : " + postId + " : Here i am getting id's" + " : : " + userName);
        Map<String, Object> map = new HashMap<>();
        PostComment postComment = new PostComment();
        postComment.setComment(UserComment);
        postComment.setUserName(userName);
        Optional<User> user = userServiceImp.getUserById(userId);
        if (user.isPresent()) {
            List<AddPost> posts = addPostService.findListOfPostData(userId);
            for (AddPost getPost : posts) {
                String img = Base64.getEncoder().encodeToString(getPost.getImage_data());
                getPost.setImage_string_data("data:image/png;base64," + img);
                System.out.println("Post ID: " + getPost.getPostId() + ", Name: " + getPost.getPost_name());
            }
            map.put("getUserId", userId);
            map.put("getPost", posts);
            map.put("user", user.get());
            map.put("username",user.get().getUserName());
            map.put("usernames", user.get().getFirst_name() + " " + user.get().getLast_name());
        }

        addCommentService.addComment(userId, postId, postComment);
        if (acceptHeader.contains("application/json")) {
            map.put("userId", userId);
            map.put("username", userName);
            map.clear();
            map.put("Message","Comment Added On Post");
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
    @PostMapping(value = "/commentOnUserProfile",produces = {"application/json","text/html"})
    public ResponseEntity<Object> addCommentOnUserProfile(@RequestParam Integer userId,@RequestParam Integer postId,@RequestParam String comment,@RequestParam String userName,PostComment postComment,
                                                          @RequestHeader(value = HttpHeaders.ACCEPT,defaultValue = "text/html")String acceptHeader)
    {
        if (comment == null || comment.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Comment can't be Null"));
        }
        Map<String,Object> map=new HashMap<>();
        postComment.setComment(comment);
        postComment.setUserName(userName);
        Optional<User> user=userServiceImp.getUserById(userId);
        List<AddPost> posts = addPostService.findListOfPost(userId);
        System.out.println("This is User ID : "+userId +" :  : ");
        System.out.println(userId+ " Here i am Getting my id");
        map.put("userId", userId);
        map.put("username",user.get().getUserName());
        map.put("usernames",user.get().getFirst_name()+" "+user.get().getLast_name());
        for (AddPost post : posts) {
            String img = Base64.getEncoder().encodeToString(post.getImage_data());
            post.setImage_string_data("data:image/png;base64,"+img);
            System.out.println("Post ID: " + post.getPostId() + ", Name: " + post.getPost_name());
        }
        map.put("addPost",posts);
        addCommentService.addComment(userId,postId,postComment);
        if (acceptHeader.contains("application/json")) {
            map.put("userId",userId);
            map.put("userName",userName);
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

    @GetMapping(value = "/AllComments" , produces = {"application/json","text/html"})
    public ResponseEntity<Object> getListComment(@RequestParam Integer userId,@RequestParam Integer postId, @RequestHeader(value = HttpHeaders.ACCEPT,defaultValue = "text/html")String acceptHeader)
    {
        List<PostComment> postComment = addCommentService.findPostComment(postId);
        Map<String,Object> map=new HashMap<>();
        Optional<User> user = userServiceImp.getUserById(userId);
        if (user.isPresent()) {
            List<AddPost> posts = addPostService.findListOfPostData(userId);
            for (AddPost getPost : posts) {
                String img = Base64.getEncoder().encodeToString(getPost.getImage_data());
                getPost.setImage_string_data("data:image/png;base64," + img);
                System.out.println("Post ID: " + getPost.getPostId() + ", Name: " + getPost.getPost_name());
            }
            map.put("getUserId", userId);
            map.put("getPostId",postId);
            map.put("getPost", posts);
            map.put("user", user.get());
            map.put("username", user.get().getUserName());
        }
        if (acceptHeader.contains("application/json")) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(map);
        } else {
            String html = renderTemplate("viewallComments", map);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(html);
        }
    }

    @GetMapping(value = "/getListOfComments",produces = {"application/json","text/html"})
    public ResponseEntity<Object> getListCommentOnUserProfile(@RequestParam Integer userId,@RequestParam Integer postId,@RequestHeader(value = HttpHeaders.ACCEPT,defaultValue = "text/html")String acceptHeader)
    {
        List<PostComment> postComment=addCommentService.findPostComment(postId);
        Map<String,Object> map=new HashMap<>();
        Optional<User> user=userServiceImp.getUserById(userId);
        List<AddPost> posts = addPostService.findListOfPost(userId);
        map.put("userId", userId);
        map.put("username",user.get().getFirst_name()+" "+user.get().getLast_name());
        for (AddPost post : posts) {
            String img = Base64.getEncoder().encodeToString(post.getImage_data());
            post.setImage_string_data("data:image/png;base64,"+img);
            System.out.println("Post ID: " + post.getPostId() + ", Name: " + post.getPost_name());
        }
        map.put("addPost",posts);
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

    @PostMapping(value = "/deleteComment",produces = {"text/html","application/json"})
    public ResponseEntity<Object> deleteCommentByCommentId(@RequestParam Integer commentId,@RequestParam Integer userId,@RequestParam Integer postId,@RequestHeader(value = HttpHeaders.ACCEPT,defaultValue = "text/html")String acceptHeader)
    {
        System.out.println(commentId +" This is my CommentID");
        addCommentService.deleteComment(commentId);
        Map<String,Object> map=new HashMap<>();
        Optional<User> user = userServiceImp.getUserById(userId);
        if (user.isPresent()) {
            List<AddPost> posts = addPostService.findListOfPostData(userId);
            for (AddPost getPost : posts) {
                String img = Base64.getEncoder().encodeToString(getPost.getImage_data());
                getPost.setImage_string_data("data:image/png;base64," + img);
                System.out.println("Post ID: " + getPost.getPostId() + ", Name: " + getPost.getPost_name());
            }
            map.put("getUserId", userId);
            map.put("getPostId",postId);
            map.put("getPost", posts);
            map.put("user", user.get());
            map.put("username", user.get().getUserName());
        }
        if (acceptHeader.contains("application/json")) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(map);
        } else {
            String html = renderTemplate("viewallComments", map);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(html);
        }
    }

    @PostMapping(value = "/updateComment",produces = {"text/html","application/json"})
    public ResponseEntity<Object> updateCommentById(@RequestParam Integer userId,@RequestParam Integer commentId, @RequestParam Integer postId, @RequestParam String UserComment, @RequestParam String userName,@RequestHeader(value = HttpHeaders.ACCEPT,defaultValue = "text/html")String acceptHeader)
    {

        PostComment existingComments = addCommentService.getCommentById(commentId);
        Map<String,Object> map=new HashMap<>();
        existingComments.setComment(UserComment);
        addCommentService.updateComment(existingComments);

        Optional<User> user = userServiceImp.getUserById(userId);
        if (user.isPresent()) {
            List<AddPost> posts = addPostService.findListOfPostData(userId);
            for (AddPost getPost : posts) {
                String img = Base64.getEncoder().encodeToString(getPost.getImage_data());
                getPost.setImage_string_data("data:image/png;base64," + img);
                System.out.println("Post ID: " + getPost.getPostId() + ", Name: " + getPost.getPost_name());
            }
            map.put("getUserId", userId);
            map.put("getPostId",postId);
            map.put("getPost", posts);
            map.put("user", user.get());
            map.put("username", user.get().getUserName());
        }
        if (acceptHeader.contains("application/json")) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(map);
        } else {
            String html = renderTemplate("viewallComments", map);
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
