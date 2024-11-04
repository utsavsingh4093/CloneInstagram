package com.java.main.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Component
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_Id")
    private int id;
    private String first_name;
    private String last_name;
    private String email;
    private String city;
    private String state;
    private String password;
    private String image_name;
    private String image_type;
    private String userName;
    @Lob
    private byte[] image_data;
    @Lob
    private String stringImageFile;

    private String followType;

    @Enumerated(EnumType.STRING)
    private FollowType type;

    public enum FollowType {
        FOLLOW,
        FOLLOWING,
        UNFOLLOW
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<PostComment> comments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<AddPost> posts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<AddFollowers> followers;
    private String recaptcha;
    public User(int userId) {
        this.id=userId;
    }
}
