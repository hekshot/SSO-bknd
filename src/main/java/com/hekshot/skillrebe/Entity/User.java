package com.hekshot.skillrebe.Entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Builder
public class User {

    @GeneratedValue
    @Id
    private int userId;

    @Column(unique = true)
    private String userName;

    private String userPassword;

    private boolean enabled;


    public User(int userId, String userName, String userPassword, boolean enabled) {
        this.userId = userId;
        this.userName = userName;
        this.userPassword = userPassword;
        this.enabled = enabled;
    }

    public User() {

    }
}
