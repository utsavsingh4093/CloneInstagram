package com.java.main.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int like_id;

    @ManyToOne
    @JoinColumn(name = "fk_user_id", referencedColumnName = "user_Id")
    private User user; // Reference to User entity

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_post_id")
    private AddPost addPost;

}

