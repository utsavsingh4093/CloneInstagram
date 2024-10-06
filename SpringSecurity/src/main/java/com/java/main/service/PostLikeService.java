package com.java.main.service;

import com.java.main.entity.AddPost;
import com.java.main.entity.PostLike;
import com.java.main.entity.User;
import com.java.main.repository.AddLikeRepository;
import com.java.main.repository.AddPostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class PostLikeService {

    @Autowired
    private AddPostsRepository addPostRepository;

    @Autowired
    private AddLikeRepository postLikeRepository;

    public String addLike(int userId, int postId) {
        AddPost post = addPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // Cheking there for existing like
        Optional<PostLike> existingLike = postLikeRepository.findByUser_IdAndAddPost_PostId(userId, postId);

        if (existingLike.isPresent()) {
            // If the like exists, remove it
            postLikeRepository.delete(existingLike.get());
            post.setLikesCount(post.getLikesCount() - 1);
            addPostRepository.save(post);
            return "Like removed successfully.";
        } else {
            PostLike postLike = new PostLike();
            postLike.setUser(new User(userId));
            postLike.setAddPost(post);
            postLikeRepository.save(postLike);

            post.setLikesCount(post.getLikesCount() + 1);
            addPostRepository.save(post);
            return "Like added successfully.";
        }
    }
}