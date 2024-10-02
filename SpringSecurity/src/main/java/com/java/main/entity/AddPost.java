package com.java.main.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AddPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int post_id;
    private String post_name;
    private String caption;
    // For Adding Post
    private String image_type;
    private String image_name;
    @Lob
    private byte[] image_data;

    // This is UserId on which I am adding foreign key
    @ManyToOne(cascade = CascadeType.ALL) // Change here
    @JoinColumn(name = "fk_user_Id", referencedColumnName = "user_Id") // Adjusted to refer to single User
    private User user; // Change to a single User reference

     //This is Refernec of post class
    @OneToMany(mappedBy = "addPost", cascade = CascadeType.ALL)
    private List<PostComment> comments;

}
