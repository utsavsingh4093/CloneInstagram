package com.java.main.repository;

import com.java.main.entity.AddFollowers;
import com.java.main.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddFollowRepo  extends JpaRepository<AddFollowers , Integer> {

    List<AddFollowers> findByUser_IdAndFollowedId(int userId, int followedId);
   // Integer deleteByUser_IdAndFollowedId(int userId, int followedId);

    AddFollowers findByUser_IdAndFollowedId(Integer userId, int followedId);

    List<AddFollowers> findByUser_Id(int userId);
    List<AddFollowers> findByFollowedId(int followedId);
    List<User> findById(int userID);
}
