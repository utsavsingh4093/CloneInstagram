package com.java.main.dto;

import com.java.main.entity.PostComment;
import com.java.main.entity.PostLike;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostWrapper2 {
    private int postId;
    private String post_name;
    private String caption;
    private String image_name;
    private int likesCount;
    private String userName;
    private List<CommentWrapper> comments;
    private List<LikeWrapper> likes;

}
