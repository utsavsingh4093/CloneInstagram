package com.java.main.dto;

import com.java.main.entity.PostComment;
import com.java.main.entity.PostLike;
import com.java.main.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostWrapper {
    private int postId;
    private String post_name;
    private String caption;
    private String image_type;
    private String image_name;
    private int likesCount;
    private int userId;
//    private List<PostComment> comments;
//    private List<PostLike> likes;


}
