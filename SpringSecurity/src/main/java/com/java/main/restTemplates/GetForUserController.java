package com.java.main.restTemplates;

import com.java.main.dto.CommentWrapper;
import com.java.main.dto.LikeWrapper;
import com.java.main.dto.PostWrapper2;
import com.java.main.entity.AddPost;
import com.java.main.entity.ReCaptchResponseType;
import com.java.main.entity.User;
import com.java.main.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.IContext;

import java.io.IOException;
import java.util.*;

@RestController
public class GetForUserController {

    @Autowired
    UserServiceImp userServiceImp;
    @Autowired
    AddCommentService addCommentService;
    @Autowired
    PostLikeService postLikeService;
    @Autowired
    AddPostService addPostService;
    @Autowired
    TemplateEngine templateEngine;
    @Autowired
    private ReCaptchaValidationService reCaptchaValidationService;
    @GetMapping(value = "/homepage", produces = {"application/json", "text/html"})
    public ResponseEntity<Object> openHomePage1(HttpSession session, @RequestHeader(value = HttpHeaders.ACCEPT, defaultValue = "text/html") String acceptHeader) {
        User user = (User) session.getAttribute("user");
        System.out.println(user.getId() + " afn afa adfqa" );
        System.out.println(user.getFirst_name() + " : " + user.getLast_name());
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("getUserId", user.getId());
        map.put("username",user.getUserName());
        map.put("usernames", user.getFirst_name() + " " + user.getLast_name());
        List<AddPost> posts = addPostService.findListOfPostData(user.getId());
        List<PostWrapper2> postWrappers2 = new ArrayList<>();
        for (AddPost getPost : posts) {
            PostWrapper2 addPostData = new PostWrapper2();
            addPostData.setPostId(getPost.getPostId());
            addPostData.setPost_name(getPost.getPost_name());
            addPostData.setLikesCount(getPost.getLikesCount());
            addPostData.setUserName(getPost.getUser().getFirst_name());
            addPostData.setImage_name(getPost.getImage_name());
            addPostData.setCaption(getPost.getCaption());
            if (getPost.getImage_data() != null) {
                String img = Base64.getEncoder().encodeToString(getPost.getImage_data());
                getPost.setImage_string_data("data:image/png;base64," + img);
                System.out.println("Post ID: " + getPost.getPostId() + ", Name: " + getPost.getPost_name());
            }
            List<CommentWrapper> commentWrappers = addCommentService.getCommentsByPostId(getPost.getPostId());
            addPostData.setComments(commentWrappers);
            System.out.println("This is My UserId " + user.getId() + "This is My Post Id " + getPost.getPostId() + "getting all comment " + commentWrappers.size());
            List<LikeWrapper> likeWrappers = postLikeService.getLikesByPostId(getPost.getPostId());
            addPostData.setLikes(likeWrappers);
            postWrappers2.add(addPostData);
            map.put("getPost", posts);
        }

            if (acceptHeader.contains("application/json")) {
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

    @PostMapping(value = "/regForm", produces = {"application/json", "text/html"})
    public ResponseEntity<Object> getRegisterForm(@RequestParam("first_name") String firstName, @RequestParam("last_name") String lastName, @RequestParam("email") String email, @RequestParam("city") String city, @RequestParam("state") String state, @RequestParam("password") String password, @RequestParam("profileImage") MultipartFile file,
                                                  @RequestHeader(value = HttpHeaders.ACCEPT, defaultValue = "text/html") String acceptHeader) throws IOException {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encryptedPassword = passwordEncoder.encode(password);
        System.out.println(state + " Here is the state");
        Map<String, Object> map = new HashMap<>();
        System.out.println(email);
        String upperCaseFirstName = firstName.toUpperCase();
        Random random = new Random();
        int threeDigitNumber = 100 + random.nextInt(900);
        String userName = upperCaseFirstName + threeDigitNumber;
        if (!userServiceImp.isEmailUnique(email)) {
            System.out.println("This Email is Already in use");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Email already exists"));
        }
        User user = new User();
        user.setFirst_name(firstName.trim());
        user.setLast_name(lastName.trim());
        user.setEmail(email.trim());
        user.setCity(city.trim());
        user.setState(state.trim());
        user.setPassword(encryptedPassword.trim());
        user.setUserName(userName.trim());
        if (!file.isEmpty()) {
            user.setImage_type(file.getContentType());
            user.setImage_name(file.getOriginalFilename());
            user.setImage_data(file.getBytes());
        }

        User registeredUser = userServiceImp.registerUser(user);
        System.out.println(registeredUser.getFirst_name());
        map.put("user", new User());
        if (acceptHeader.contains("application/json")) {
            map.clear();
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(map);
        } else {
            String html = renderTemplate("home", map);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(html);
        }
    }

    @GetMapping(value = "/homeData/{id}", produces = {"application/json", "text/html"})
    public ResponseEntity<Object> getUserData(
            @PathVariable int id,
            HttpSession httpSession,
            @RequestHeader(value = HttpHeaders.ACCEPT, defaultValue = "text/html") String acceptHeader) {
        Map<String, Object> map=new HashMap<>();
        Optional<User> user = userServiceImp.getUserById(id);
            List<AddPost> posts = addPostService.findListOfPostData(id);
            List<PostWrapper2> postWrappers2 = new ArrayList<>();
            for (AddPost getPost : posts) {
                PostWrapper2 addPostData = new PostWrapper2();
                addPostData.setPostId(getPost.getPostId());
                addPostData.setPost_name(getPost.getPost_name());
                addPostData.setLikesCount(getPost.getLikesCount());
                addPostData.setUserName(getPost.getUser().getFirst_name());
                addPostData.setImage_name(getPost.getImage_name());
                addPostData.setCaption(getPost.getCaption());
                if (getPost.getImage_data() != null) {
                    String img = Base64.getEncoder().encodeToString(getPost.getImage_data());
                    getPost.setImage_string_data("data:image/png;base64," + img);
                    System.out.println("Post ID: " + getPost.getPostId() + ", Name: " + getPost.getPost_name());
                }

                List<CommentWrapper> commentWrappers = addCommentService.getCommentsByPostId(getPost.getPostId());
                addPostData.setComments(commentWrappers);

                List<LikeWrapper> likeWrappers=postLikeService.getLikesByPostId(getPost.getPostId());
                addPostData.setLikes(likeWrappers);

                postWrappers2.add(addPostData);
                map.put("getPost", posts);
                map.put("getUserId", id);
                map.put("user", user.get());
                map.put("username", user.get().getFirst_name() + " " + user.get().getLast_name());
            }
        Map<String,Object> mapForPostWrapper=new HashMap<>();
        mapForPostWrapper.put("getdataPost",postWrappers2);
        httpSession.setAttribute("user", user.get());
        httpSession.setAttribute("username", user.get());
        if (acceptHeader.contains("application/json")) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapForPostWrapper);
        } else {
            String html = renderTemplate("homepage", map);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(html);
        }
    }

    @PostMapping(value = "/logForm", produces = {"application/json", "text/html"})
    public ResponseEntity<Object> submitLoginForm(
            @RequestBody User user,
            HttpSession httpSession,
            @RequestHeader(value = HttpHeaders.ACCEPT, defaultValue = "text/html") String acceptHeader) {


        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User existingUser = userServiceImp.findByEmail(user.getEmail());

        if (existingUser == null || !passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password.");
        }


        boolean isCaptchaValid = reCaptchaValidationService.validateCaptcha(user.getRecaptchaResponse());
        if (!isCaptchaValid) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid reCAPTCHA. Please try again.");
        }


        httpSession.setAttribute("user", existingUser);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("userId", existingUser.getId());
        responseMap.put("username", existingUser.getFirst_name() + " " + existingUser.getLast_name());

        List<AddPost> posts = addPostService.findListOfPostData(existingUser.getId());
        for (AddPost post : posts) {
            String img = Base64.getEncoder().encodeToString(post.getImage_data());
            post.setImage_string_data("data:image/png;base64," + img);
        }
        responseMap.put("posts", posts);


        if (acceptHeader.contains("application/json")) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(responseMap);
        } else {
            String redirectUrl = "/homepage";
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(redirectUrl);
        }
    }



    @GetMapping(value = "/register",produces = {"application/json","text/html"})
    public ResponseEntity<Object> openRegsitrationPage(@RequestHeader(value = HttpHeaders.ACCEPT, defaultValue = "text/html") String acceptHeader) {
        Map<String,Object> map=new HashMap<>();
        map.put("user", new User());
        if (acceptHeader.contains("application/json")) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(map);
        } else {
            String html = renderTemplate("register", map);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(html);
        }
    }

    @GetMapping(value = "/home" , produces = {"application/json","text/html"})
    public ResponseEntity<Object> openHome(@RequestHeader(value = HttpHeaders.ACCEPT, defaultValue = "text/html") String acceptHeader) {
        Map<String,Object> map=new HashMap<>();
        if (acceptHeader.contains("application/json")) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(map);
        } else {
            String html = renderTemplate("home", map);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(html);
        }
    }

    @GetMapping(value = "/login", produces = {"application/json", "text/html"})
    public ResponseEntity<Object> openLoginPage(@RequestHeader(value = HttpHeaders.ACCEPT, defaultValue = "text/html") String acceptHeader) {
        Map<String,Object> map=new HashMap<>();
        map.put("user", new User());
        if (acceptHeader.contains("application/json")) {
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("message", "Welcome to Your Login Page");
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(responseMap);
        } else {
            String url=renderTemplate("login",map);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(url);
        }
    }

    @GetMapping(value = "/logout" , produces = {"application/json","text/html"})
    public ResponseEntity<Object> logoutForm(HttpServletRequest request,@RequestHeader(value = HttpHeaders.ACCEPT, defaultValue = "text/html") String acceptHeader) {
        HttpSession session = request.getSession(false);
        Map<String,Object> map=new HashMap<>();
        if (acceptHeader.contains("application/json")) {
            map.clear();
            map.put("Message","Logout SuccessFully");
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(map);
        } else {
            String html = renderTemplate("home", map);
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