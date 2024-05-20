package diploma.playlist.filter;

import diploma.playlist.entity.Role;
import diploma.playlist.util.HeaderConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static diploma.playlist.util.HeaderConstants.*;

public class JwtVerifierFilter extends OncePerRequestFilter {

    private final String BEARER = "Bearer";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }
        String username = request.getHeader(USERNAME_HEADER);
        Role userRole = Role.valueOf(request.getHeader(AUTHORITIES_HEADER));
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, List.of(userRole));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
