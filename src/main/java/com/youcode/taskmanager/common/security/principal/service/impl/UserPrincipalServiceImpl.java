package com.youcode.taskmanager.common.security.principal.service.impl;

import com.youcode.taskmanager.common.security.principal.model.UserPrincipal;
import com.youcode.taskmanager.common.security.principal.service.UserPrincipalService;
import com.youcode.taskmanager.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPrincipalServiceImpl implements UserPrincipalService {
    private final UserService userService;
    private final UserPrincipal userPrincipal;
    @Override
    public UserDetailsService userDetailsService() {

        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                userPrincipal.setUser(userService.findByEmail(username));
                return userPrincipal;

            }
        };

    }
}
