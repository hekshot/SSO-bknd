package com.hekshot.skillrebe.Service;

import com.hekshot.skillrebe.Entity.User;
import com.hekshot.skillrebe.Repo.UserRepo;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Data
public class UserService {

    @Autowired
    private UserRepo repo;

    public void processOAuthPostLogin(String username) {
        User existUser = repo.findByUserName(username);

        if (existUser == null) {
            User newUser = new User();
            newUser.setUserName(username);
            newUser.setEnabled(true);

            repo.save(newUser);
            System.out.println("Created new user: " + newUser);

        }else {
            System.out.println(existUser.getUserId());
        }

    }
}
