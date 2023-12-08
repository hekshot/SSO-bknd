package com.hekshot.skillrebe.Dto;

import com.hekshot.skillrebe.Entity.User;
import lombok.Data;

@Data
public class UserDto {
    private int userId;
    private String userName;
    private boolean enabled;

    public UserDto(User user) {
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.enabled = user.isEnabled();
    }
}

