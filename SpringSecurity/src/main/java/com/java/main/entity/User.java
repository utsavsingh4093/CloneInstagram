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
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "email"))
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
    //For adding the profile
    private String image_name;
    private String image_type;
    @Lob
    private byte[] image_data;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL) // Add this to link to AddPost
    private List<AddPost> posts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<PostComment> comments;
}
