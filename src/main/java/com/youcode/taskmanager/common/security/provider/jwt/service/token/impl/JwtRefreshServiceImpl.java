package com.youcode.taskmanager.common.security.provider.jwt.service.token.impl;

import com.youcode.taskmanager.common.security.dto.vm.UserDeviceInfo;
import com.youcode.taskmanager.common.security.principal.service.UserPrincipalService;
import com.youcode.taskmanager.common.security.provider.jwt.service.info.UserDeviceInfoService;
import com.youcode.taskmanager.common.security.provider.jwt.service.token.JwtRefreshService;
import com.youcode.taskmanager.core.database.model.entity.RefreshToken;
import com.youcode.taskmanager.core.database.model.entity.User;
import com.youcode.taskmanager.shared.Enum.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;


@Service
@RequiredArgsConstructor
public class JwtRefreshServiceImpl  implements JwtRefreshService {

    private final UserDeviceInfoService userDeviceInfoService;
    private final JwtServiceImpl jwtServiceImpl;
    private final UserPrincipalService userPrincipalService;

    @Override
    public RefreshToken getRefreshToken(HttpServletRequest httpServletRequest, UserDetails userDetails) {

        UserDeviceInfo userDeviceInfo = userDeviceInfoService.getUserDeviceInfo(httpServletRequest);
        Date expiry_15_day = new Date(System.currentTimeMillis() + 15L * 24 * 60 * 60 * 1000);

        return RefreshToken.builder()

                .user((User) userDetails)
                .ipAddress(userDeviceInfo.getIpAddress())
                .userAgent(userDeviceInfo.getUserAgent())
                .token(jwtServiceImpl.generateToken(new HashMap<>(), userDetails, expiry_15_day, TokenType.Refresh))
                .build();
    }

    @Override
    public String validateRefreshToken(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        if (shouldFilterProceed(authHeader)) {

            String jwt = extractJwtFromHeader(authHeader);

            jwtServiceImpl.checkTokenType(jwt, TokenType.Refresh);

            String userEmail = jwtServiceImpl.extractUserName(jwt);

            checkUserName(userEmail);

            UserDetails userDetails = userPrincipalService.userDetailsService().loadUserByUsername(userEmail);

            if (jwtServiceImpl.isTokenValid(jwt, userDetails)) {
                return jwt;
            }

        }
        throw new IllegalArgumentException("Refresh token is not valid");
    }



    private void checkUserName(String userEmail) {
        if (StringUtils.isEmpty(userEmail)) {
            throw new IllegalArgumentException("Refresh token is not valid");
        }
    }


    private boolean shouldFilterProceed(String authHeader) {
        return StringUtils.isNotEmpty(authHeader) && StringUtils.startsWith(authHeader, "Bearer ");
    }


    private String extractJwtFromHeader(String authHeader) {
        return authHeader.substring(7);
    }





}
