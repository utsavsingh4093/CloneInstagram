package com.java.main.service;

import com.java.main.entity.AddPost;
import com.java.main.repository.AddPostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddPostService {

    @Autowired
    AddPostsRepository addPostsRepository;

    public AddPost addPostData(AddPost addPost)
    {
        return addPostsRepository.save(addPost);
    }
}
