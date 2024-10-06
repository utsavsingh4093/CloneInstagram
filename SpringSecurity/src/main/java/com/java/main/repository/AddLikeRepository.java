package com.java.main.repository;

import com.java.main.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface AddLikeRepository extends JpaRepository<PostLike, Integer> {
    Optional<PostLike> findByUser_IdAndAddPost_PostId(int userId, int postId);
}









