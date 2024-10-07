package com.java.main.service;

import com.java.main.entity.AddPost;
import com.java.main.entity.PostComment;
import com.java.main.entity.User;
import com.java.main.repository.AddCommentRepo;
import com.java.main.repository.AddPostsRepository;
import com.java.main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddCommentService {
    @Autowired
    private AddPostsRepository addPostsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddCommentRepo addCommentRepo;

    public String addComment(int userId, int postId, PostComment postComment) {
        AddPost post = addPostsRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        postComment.setAddPost(post);
        postComment.setUser(user);

        addCommentRepo.save(postComment);

        return "Comment saved";
    }
}
