package com.java.main.repository;

import com.java.main.entity.User;
import com.java.main.entity.UserWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

 User findByEmail(String email);
}
