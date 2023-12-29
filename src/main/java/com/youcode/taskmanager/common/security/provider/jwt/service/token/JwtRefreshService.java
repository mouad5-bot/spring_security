package com.youcode.taskmanager.common.security.provider.jwt.service.token;

import com.youcode.taskmanager.core.database.model.entity.RefreshToken;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtRefreshService {
    RefreshToken getRefreshToken(HttpServletRequest httpServletRequest , UserDetails userDetails);

    String validateRefreshToken(HttpServletRequest httpServletRequest );

}
