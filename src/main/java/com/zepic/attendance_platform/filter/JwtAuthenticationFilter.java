package com.zepic.attendance_platform.filter;
import com.zepic.attendance_platform.security.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("access_token".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    if (jwtService.isValid(token)) {

                        Long userId = jwtService.getUserId(token);
                        Long collegeId = jwtService.getCollegeId(token);
                        String role = jwtService.getRole(token);

                        System.out.println("User ID: " + userId);
                        System.out.println("College ID: " + collegeId);
                        System.out.println("Role: " + role);
                    } else {
                        System.out.println("JWT is invalid");
                    }
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}