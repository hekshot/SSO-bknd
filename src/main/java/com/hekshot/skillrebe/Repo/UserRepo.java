package com.hekshot.skillrebe.Repo;

import com.hekshot.skillrebe.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Integer> {
    User findByUserName(String userName);
}
