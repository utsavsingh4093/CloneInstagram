package com.java.main.service;

import com.java.main.entity.*;
import com.java.main.repository.AddFollowRepo;
import com.java.main.repository.AddPostsRepository;
import com.java.main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AddPostService {

    @Autowired
    AddPostsRepository addPostsRepository;

    @Autowired
    AddFollowRepo addFollowRepo;

    @Autowired
    UserRepository userRepository;

    public AddPost addPostData(AddPost addPost)
    {
        addPostsRepository.findAll();
        return addPostsRepository.save(addPost);
    }
    //For Display the List Of Post On Home Page
    public List<AddPost> findListOfPost() {
        return addPostsRepository.findAll();
    }

    public List<AddPost> findListOfPostData(Integer userId) {
        System.out.println("This is My User Id : "+userId);
        UserWrapper addFollowersWrapper = new UserWrapper();
        List<AddPost> posts = new ArrayList<>();
        for (AddFollowers followers : addFollowRepo.findByUser_Id(userId)) {
            System.out.println(followers.getUser().getId());
            System.out.println(followers.getType().toString());
            if(followers.getType().toString().equals("UNFOLLOW")) {
                posts.addAll(addPostsRepository.findAllByUser_Id(followers.getFollowedId()));
                for(AddPost addPost: posts ) {
                    System.out.println(addPost.getPostId());
                }
            }
        }
        posts.addAll(addPostsRepository.findAllByUser_Id(userId));
        return posts;
    }

    public Optional<AddPost> getUserById(int id) {
        return addPostsRepository.findById(id); // Assuming you're using JpaRepository
    }
}