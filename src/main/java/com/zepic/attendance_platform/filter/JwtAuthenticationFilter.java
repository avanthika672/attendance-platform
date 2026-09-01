package com.zepic.attendance_platform.filter;
import com.zepic.attendance_platform.security.AuthenticatedUser;
import com.zepic.attendance_platform.security.JwtService;
import com.zepic.attendance_platform.security.TenantContext;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;
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
        try {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("access_token".equals(cookie.getName())) {
                        String token = cookie.getValue();
                        try {
                            Claims claims =
                                    jwtService.getClaims(token);
                            Long userId =
                                    Long.valueOf(claims.getSubject());
                            Long collegeId =
                                    claims.get("collegeId", Long.class);
                            String role =
                                    claims.get("role", String.class);
                            TenantContext.set(collegeId);
                            AuthenticatedUser user =
                                    new AuthenticatedUser(userId, collegeId, role);
                            request.setAttribute("authenticatedUser", user);
                            UsernamePasswordAuthenticationToken authentication =
                                    new UsernamePasswordAuthenticationToken(user, null, List.of(
                                                    new SimpleGrantedAuthority("ROLE_" + role)
                                            )
                                    );
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        } catch (Exception ignored) {
                        }
                        break;
                    }
                }
            }
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
            SecurityContextHolder.clearContext();
        }
    }
}