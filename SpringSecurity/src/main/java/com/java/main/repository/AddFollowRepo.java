package com.java.main.repository;

import com.java.main.entity.AddFollowers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddFollowRepo  extends JpaRepository<AddFollowers , Integer> {

    Optional<AddFollowers> findByUser_IdAndFollowedId(int userId, int followedId);
    Integer deleteByUser_IdAndFollowedId(int userId, int followedId);
}
