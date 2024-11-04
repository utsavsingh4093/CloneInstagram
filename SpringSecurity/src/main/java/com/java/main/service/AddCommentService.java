package com.java.main.service;

import com.java.main.dto.CommentWrapper;
import com.java.main.entity.AddPost;
import com.java.main.entity.PostComment;
import com.java.main.entity.User;
import com.java.main.repository.AddCommentRepo;
import com.java.main.repository.AddPostsRepository;
import com.java.main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
//    public List<CommentWrapper> getCommentList()
//    {
//        List<CommentWrapper> list = new ArrayList<>();
//        for (PostComment postComment : addCommentRepo.findAll()) {
//            CommentWrapper wrapper = new CommentWrapper(postComment.getComment_id(),postComment.getUserName(),postComment.getComment(),postComment.getUser().getId(),postComment.getAddPost().getPostId());
//            list.add(wrapper);
//        }
//        return list;
//    }

    public List<PostComment> findPostComment(Integer id)
    {
        return addCommentRepo.findByAddPost_PostId(id);
    }

    public  PostComment getCommentById(Integer commentId){
        return addCommentRepo.findById(commentId).orElse(null);
    }

    public List<CommentWrapper> getCommentsByPostId(Integer postId) {
        List<CommentWrapper> list = new ArrayList<>();
        for (PostComment postComment : addCommentRepo.findByAddPost_PostId(postId)) {
            CommentWrapper wrapper = new CommentWrapper(postComment.getComment_id(),postComment.getUserName(),postComment.getComment(),postComment.getUser().getId(),postComment.getAddPost().getPostId());
            list.add(wrapper);
        }
        return list;
    }

    public void deleteComment(int commentId) {
        System.out.println(commentId);
        addCommentRepo.deleteById(commentId);
    }

    public PostComment updateComment(PostComment postComment) {
        try {
            return addCommentRepo.save(postComment);
        } catch (Exception e) {
            throw new RuntimeException("Data is not updating: ");
        }
    }
}
