package com.youcode.taskmanager.common.security.filter;

import java.io.IOException;
import java.util.Collection;

import com.youcode.taskmanager.common.security.principal.service.UserPrincipalService;
import com.youcode.taskmanager.common.security.provider.jwt.service.token.JwtService;
import com.youcode.taskmanager.shared.Enum.TokenType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserPrincipalService  userPrincipalService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (shouldFilterProceed(authHeader)) {

            String jwt = extractJwtFromHeader(authHeader);

            validateTokenTypes(jwt, request);

            String userEmail = jwtService.extractUserName(jwt);

            if (isUserEligibleForAuthentication(userEmail)) {
                UserDetails userDetails = userPrincipalService.userDetailsService().loadUserByUsername(userEmail);
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    setAuthentication(userDetails, request);
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private void validateTokenTypes(String jwt, HttpServletRequest request) {
        if (getEndPoint(request).contains("refresh")) {
            jwtService.checkTokenType(jwt, TokenType.Refresh);
        } else {
            jwtService.checkTokenType(jwt, TokenType.Access);
        }
    }

    private String getEndPoint(HttpServletRequest request) {
        String url = request.getRequestURI();
        String contextPath = request.getContextPath();
        return url.substring(contextPath.length());
    }



    private boolean shouldFilterProceed(String authHeader) {
        return StringUtils.isNotEmpty(authHeader) && StringUtils.startsWith(authHeader, "Bearer ");
    }


    private String extractJwtFromHeader(String authHeader) {
        return authHeader.substring(7);
    }


    private boolean isUserEligibleForAuthentication(String userEmail) {
        return StringUtils.isNotEmpty(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null;
    }


    private void setAuthentication(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }


}
