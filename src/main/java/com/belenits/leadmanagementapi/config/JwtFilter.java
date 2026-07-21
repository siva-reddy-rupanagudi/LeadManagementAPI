package com.belenits.leadmanagementapi.config;

import com.belenits.leadmanagementapi.service.CounsellorsDetaileService;
import com.belenits.leadmanagementapi.service.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);

    private final JwtService jwtService;
    private final CounsellorsDetaileService counsellorsDetaileService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        String path = request.getServletPath();
        String method = request.getMethod();

        return path.equals("/counsellors/login")
                || path.equals("/csrf-token")
                || path.equals("/courses")
                || path.startsWith("/courses/")
                || (path.equals("/counsellors") && method.equalsIgnoreCase("POST"));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        log.info("JWT Filter : {} {}", request.getMethod(), request.getServletPath());

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7).trim();

        try {

            String email = jwtService.extractUserName(token);

            if (email != null && shouldAuthenticate()) {

                UserDetails userDetails =
                        counsellorsDetaileService.loadUserByUsername(email);

                if (jwtService.validateToken(token, userDetails)) {

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities());

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request));

                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);

                    // Extract counsellor id from JWT
                    String counsellorId =
                            jwtService.extractCounsellorId(token);

                    // Store for current request only
                    request.setAttribute("counsellorId", counsellorId);

                    log.info("JWT Authentication Success : {}", email);
                }

            }

        } catch (JwtException |
                 UsernameNotFoundException |
                 IllegalArgumentException ex) {

            SecurityContextHolder.clearContext();

            sendUnauthorizedResponse(
                    response,
                    "Invalid or Expired JWT Token");

            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean shouldAuthenticate() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        return authentication == null
                || authentication instanceof AnonymousAuthenticationToken;
    }

    private void sendUnauthorizedResponse(HttpServletResponse response,
                                          String message)
            throws IOException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        response.getWriter().write(
                """
                        {
                          "message":"%s",
                          "status":401
                        }
                        """.formatted(message));
    }
}