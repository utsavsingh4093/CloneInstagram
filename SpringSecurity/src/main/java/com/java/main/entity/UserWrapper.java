package com.java.main.entity;

import jakarta.persistence.Lob;
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
    private String image_name;
    private String image_type;
    @Lob
    private byte[] image_data;
}
