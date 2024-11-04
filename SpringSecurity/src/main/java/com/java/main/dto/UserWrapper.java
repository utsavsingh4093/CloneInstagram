package com.java.main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserWrapper {
    private int id;
    private String first_name;
    private String last_name;
    private String email;
    private String city;
    private String state;
    private String password;
    private String stringImageFile;
    private String followType;
    private String username;
}
