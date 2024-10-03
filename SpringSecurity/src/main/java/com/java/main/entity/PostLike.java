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
    private List<Integer> likes;

    // Getting the data of user_id
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_user_Id")
    private User user;

    // Getting the data of post_id
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_post_id")
    private AddPost addPost;
}
