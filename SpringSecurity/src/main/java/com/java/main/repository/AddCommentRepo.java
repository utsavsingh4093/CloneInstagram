package com.java.main.repository;

import com.java.main.dto.CommentWrapper;
import com.java.main.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddCommentRepo extends JpaRepository<PostComment,Integer> {
    List<PostComment> findByAddPost_PostId(int postId);
}
