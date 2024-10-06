package com.java.main.repository;

import com.java.main.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddCommentRepo extends JpaRepository<PostComment,Integer> {
}
