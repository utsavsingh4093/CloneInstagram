package com.java.main.service;

import com.java.main.entity.AddPost;
import com.java.main.repository.AddPostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return addPostsRepository.findAll();
    }

    public List<AddPost> findListOfPostData(Integer userId) {
        return addPostsRepository.findAllByUser_Id(userId);
    }
    public Optional<AddPost> getUserById(int id) {
        return addPostsRepository.findById(id); // Assuming you're using JpaRepository
    }

}