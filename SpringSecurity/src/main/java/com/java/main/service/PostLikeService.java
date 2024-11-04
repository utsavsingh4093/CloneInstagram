package com.java.main.service;

import com.java.main.dto.LikeWrapper;
import com.java.main.entity.AddPost;
import com.java.main.entity.PostLike;
import com.java.main.entity.User;
import com.java.main.repository.AddLikeRepository;
import com.java.main.repository.AddPostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class PostLikeService {

    @Autowired
    private AddPostsRepository addPostRepository;

    @Autowired
    private AddLikeRepository postLikeRepository;

    @Transactional
    public int addLike(int userId, int postId) {
        AddPost post = addPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Optional<PostLike> existingLike = postLikeRepository.findByUser_IdAndAddPost_PostId(userId, postId);

        if (existingLike.isPresent()) {
            postLikeRepository.delete(existingLike.get());
            post.setLikesCount(post.getLikesCount() - 1);
        } else {
            PostLike postLike = new PostLike();
            postLike.setUser(new User(userId));
            postLike.setAddPost(post);
            postLikeRepository.save(postLike);

            post.setLikesCount(post.getLikesCount() + 1);
        }
        addPostRepository.save(post);
        return post.getLikesCount();
    }


//    public List<LikeWrapper> getLikeList()
//    {
//        List<LikeWrapper> list = new ArrayList<>();
//        for (PostLike postLike : postLikeRepository.findAll()) {
//            LikeWrapper likeWrapper = new LikeWrapper(postLike.getLikeId(),postLike.getUser().getId(),postLike.getAddPost().getPostId());
//            list.add(likeWrapper);
//        }
//        return list;
//    }


    public List<LikeWrapper> getLikesByPostId(int postId) {
        List<LikeWrapper> list = new ArrayList<>();
        for (PostLike postLike : postLikeRepository.findByAddPost_PostId(postId)) {
            LikeWrapper likeWrapper = new LikeWrapper(postLike.getLikeId(),postLike.getUser().getId(),postLike.getAddPost().getPostId());
            list.add(likeWrapper);
        }
        return list;
    }
}