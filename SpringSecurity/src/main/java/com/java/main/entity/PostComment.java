package com.java.main.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int comment_id;

    @Column(columnDefinition = "TEXT")
    private String comment;

    // This is from User Class
    @ManyToOne(cascade = CascadeType.ALL) // Change here
    @JoinColumn(name = "fk_user_Id") // This id is from user id
    private User user;

    //This is from Add Post Class
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_postId")
    private AddPost addPost;


}
