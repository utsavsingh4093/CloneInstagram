package com.java.main.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component

public class AddFollowers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int followId;

    // This is from User Class
    @ManyToOne(cascade = CascadeType.ALL) // Change here
    @JoinColumn(name = "fk_user_Id") // This id is from user id
    private User user;

    private String type;
}
