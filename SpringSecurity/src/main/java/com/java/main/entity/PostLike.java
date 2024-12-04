package com.java.main.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int likeId;
    @ManyToOne
    @JoinColumn(name = "fk_user_id", referencedColumnName = "user_Id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "fk_post_id", referencedColumnName = "postId")
    private AddPost addPost;
}
