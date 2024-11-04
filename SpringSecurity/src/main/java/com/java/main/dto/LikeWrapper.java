package com.java.main.dto;

import com.java.main.entity.AddPost;
import com.java.main.entity.User;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeWrapper {
    private int likeId;
    private int userId;
    private int addPostId;
}
