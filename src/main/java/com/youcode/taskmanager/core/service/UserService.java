package com.youcode.taskmanager.core.service;

import com.youcode.taskmanager.core.database.model.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    User save(User user);


    User findByEmail(String email);
}
