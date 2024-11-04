package com.java.main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddFollowersWrapper {

    private int followId;
    private Integer userId;
    private int followedId;
    private String followType;

}
