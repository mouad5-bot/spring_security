package com.youcode.taskmanager.common.security.provider.jwt.service.auth;

import com.youcode.taskmanager.common.security.dto.request.SignUpRequest;
import com.youcode.taskmanager.common.security.dto.request.SigninRequest;
import com.youcode.taskmanager.common.security.dto.response.JwtAuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request, HttpServletRequest httpServletRequest);
    JwtAuthenticationResponse signin(SigninRequest request, HttpServletRequest httpServletRequest);

    JwtAuthenticationResponse refresh(HttpServletRequest httpServletRequest);
}