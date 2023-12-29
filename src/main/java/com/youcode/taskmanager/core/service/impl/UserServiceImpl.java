package com.youcode.taskmanager.core.service.impl;

import com.youcode.taskmanager.core.database.model.entity.User;
import com.youcode.taskmanager.core.database.repository.UserRepository;
import com.youcode.taskmanager.core.service.RefreshTokenService;
import com.youcode.taskmanager.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;



    @Override
    public User save(User user) {
        if(userRepository.existsByEmail(user.getEmail())){
            throw new IllegalArgumentException("User already exists");
        }
        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return findByEmailOrThrow(email);
    }


    public User findByEmailOrThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}