package com.java.main.service;

import com.java.main.dto.UserWrapper;
import com.java.main.entity.*;
import com.java.main.repository.AddFollowRepo;
import com.java.main.repository.AddPostsRepository;
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

    public AddPost addPostData(AddPost addPost)
    {
        addPostsRepository.findAll();
        return addPostsRepository.save(addPost);
    }

    public List<AddPost> findListOfPost(int userId) {
        return addPostsRepository.findByUserId(userId);

    }

    public List<AddPost> findListOfPostData(Integer userId) {
        System.out.println("This is My User Id : "+userId);

            UserWrapper addFollowersWrapper = new UserWrapper();
            List<AddPost> posts = new ArrayList<>();
        try {
            for (AddFollowers followers : addFollowRepo.findByUser_Id(userId)) {
                System.out.println(followers.getUser().getId());
                System.out.println(followers.getType().toString());
                if (followers.getType().toString().equals("UNFOLLOW")) {
                    posts.addAll(addPostsRepository.findAllByUser_Id(followers.getFollowedId()));
//                for(AddPost addPost: posts ) {
//                    System.out.println(addPost.getPostId());
//                }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        posts.addAll(addPostsRepository.findAllByUser_Id(userId));
        return posts;
    }

    public Optional<AddPost> getUserById(int id) {
        return addPostsRepository.findById(id);
    }
}