package com.java.main.service;

import com.java.main.entity.AddPost;
import com.java.main.entity.User;
import com.java.main.repository.AddPostsRepository;
import jakarta.persistence.Lob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class AddPostService {

    @Autowired
    AddPostsRepository addPostsRepository;

    public AddPost addPostData(AddPost addPost)
    {
        addPostsRepository.findAll();
        return addPostsRepository.save(addPost);
    }

    public List<AddPost> findListOfPost() {
        List<AddPost> posts = addPostsRepository.findAll();
        for (AddPost post : posts) { 
            String img = Base64.getEncoder().encodeToString(post.getImage_data());
            post.setImage_string_data("data:image/png;base64,"+img);
            System.out.println("Post ID: " + post.getPost_id() + ", Name: " + post.getPost_name());
        }
        return posts;
    }

    public Optional<AddPost> getUserById(int id) {
        return addPostsRepository.findById(id); // Assuming you're using JpaRepository
    }


}
