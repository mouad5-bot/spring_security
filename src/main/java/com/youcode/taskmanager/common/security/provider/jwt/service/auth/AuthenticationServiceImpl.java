package com.youcode.taskmanager.common.security.provider.jwt.service.auth;

import com.youcode.taskmanager.common.security.dto.vm.UserDeviceInfo;
import com.youcode.taskmanager.common.security.principal.model.UserPrincipal;
import com.youcode.taskmanager.common.security.provider.jwt.service.info.UserDeviceInfoService;
import com.youcode.taskmanager.common.security.provider.jwt.service.token.JwtRefreshService;
import com.youcode.taskmanager.common.security.provider.jwt.service.token.JwtService;
import com.youcode.taskmanager.common.security.dto.request.SignUpRequest;
import com.youcode.taskmanager.common.security.dto.request.SigninRequest;
import com.youcode.taskmanager.common.security.dto.response.JwtAuthenticationResponse;
import com.youcode.taskmanager.core.database.model.entity.RefreshToken;
import com.youcode.taskmanager.core.database.model.entity.User;
import com.youcode.taskmanager.core.service.RefreshTokenService;
import com.youcode.taskmanager.core.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;

    private final RefreshTokenService refreshTokenService;

    private final UserPrincipal userPrincipal;

    private final ModelMapper modelMapper;

    private final JwtService jwtService;
    private final JwtRefreshService jwtRefreshService;
    private final AuthenticationManager authenticationManager;
    private final UserDeviceInfoService userDeviceInfoService;


    @Override
    public JwtAuthenticationResponse signup(SignUpRequest request, HttpServletRequest httpServletRequest) {

        User user = modelMapper.map(request, User.class);
        User userRegistered = userService.save(user);

        userPrincipal.setUser(userRegistered);

        var jwt = jwtService.generaAccessToken(userPrincipal);

        RefreshToken refreshToken = refreshTokenService.save(jwtRefreshService.getRefreshToken(httpServletRequest, userPrincipal));


        return JwtAuthenticationResponse.builder()
                .accessToken(jwt)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    @Override
    public JwtAuthenticationResponse signin(SigninRequest request ,HttpServletRequest httpServletRequest) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userService.findByEmail(request.getEmail());

        userPrincipal.setUser(user);

        var jwt = jwtService.generaAccessToken(userPrincipal);

        RefreshToken refreshToken = jwtRefreshService.getRefreshToken(httpServletRequest, userPrincipal);
        refreshToken.setUser(user);
        refreshToken = refreshTokenService.update(refreshToken);

        return JwtAuthenticationResponse.builder().
                accessToken(jwt)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    @Override
    public JwtAuthenticationResponse refresh(HttpServletRequest httpServletRequest) {

        String refreshToken = jwtRefreshService.validateRefreshToken(httpServletRequest);

        RefreshToken refreshTokenEntity = refreshTokenService.findByToken(refreshToken);

        userDeviceInfoService.validateUserDeviceInfo(httpServletRequest, refreshTokenEntity.getUserDeviceInfo());

        userPrincipal.setUser(refreshTokenEntity.getUser());

        var jwt = jwtService.generaAccessToken(userPrincipal);

        return JwtAuthenticationResponse.builder().accessToken(jwt).build();

    }
}

