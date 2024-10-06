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
public class AddPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postId; // This must match the method name

    private String post_name;
    private String caption;

    private String image_type;
    private String image_name;

    @Lob
    private byte[] image_data;

    @Lob
    private String image_string_data;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_user_Id", referencedColumnName = "user_Id")
    private User user;

    private int likesCount;

    @OneToMany(mappedBy = "addPost", cascade = CascadeType.ALL)
    private List<PostComment> comments;

    @OneToMany(mappedBy = "addPost", cascade = CascadeType.ALL)
    private List<PostLike> likes;
}
