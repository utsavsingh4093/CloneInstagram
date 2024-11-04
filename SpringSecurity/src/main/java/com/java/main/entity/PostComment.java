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

    private String comment;

    private String userName;

    @ManyToOne
    @JoinColumn(name = "fk_user_Id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "fk_postId")
    private AddPost addPost;
}

