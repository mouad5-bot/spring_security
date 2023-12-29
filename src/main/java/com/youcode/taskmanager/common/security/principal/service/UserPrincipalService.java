package com.youcode.taskmanager.common.security.principal.service;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserPrincipalService {
    UserDetailsService userDetailsService();
}
