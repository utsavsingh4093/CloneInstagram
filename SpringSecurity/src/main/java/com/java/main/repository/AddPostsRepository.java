package com.java.main.repository;

import com.java.main.entity.AddPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddPostsRepository extends JpaRepository<AddPost,Integer> {
}
