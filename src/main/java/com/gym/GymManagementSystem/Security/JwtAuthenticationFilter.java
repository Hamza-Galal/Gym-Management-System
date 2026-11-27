package com.gym.GymManagementSystem.Security;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.gym.GymManagementSystem.DTO.Response.ErrorResponse;
import com.gym.GymManagementSystem.Exception.InvalidTokenException;
import com.gym.GymManagementSystem.Model.User;
import com.gym.GymManagementSystem.Repository.UserRepository;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import tools.jackson.databind.ObjectMapper;
@AllArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    ObjectMapper objectMapper;

     @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return path.equals("/api/auth/register") ||
        path.equals("/api/auth/login") || 
        request.getMethod().equalsIgnoreCase("OPTIONS");
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return ;
        }
        String token = authHeader.substring(7);
        try {
            String email = jwtService.extractEmail(token);
            Integer tokenVersion = jwtService.extractTokenVersion(token);
            User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new InvalidTokenException("Invalid or expired token"));
            
            if (!user.isActive()) {
                throw new InvalidTokenException("Invalid or expired token");
            }

            if (tokenVersion == null || !tokenVersion.equals(user.getTokenVersion())) {
                throw new InvalidTokenException("Invalid or expired token");
            }

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                CustomUserDetails userDetails = new CustomUserDetails(user);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null , userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (JwtException | IllegalArgumentException exception) {
            handleInvalidToken(response , request);
            return ;
        }
        catch (InvalidTokenException exception){
            handleInvalidToken(response , request);
            return;
        }

        filterChain.doFilter(request, response);
    }
    private void handleInvalidToken(HttpServletResponse response , HttpServletRequest request )throws IOException{
        SecurityContextHolder.clearContext();
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimeStamp(LocalDateTime.now());
        errorResponse.setStatus(401);
        errorResponse.setError("Unauthorized");
        errorResponse.setMessage("Invalid or expired token");
        errorResponse.setPath(request.getRequestURI());

        response.setStatus(401);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        objectMapper.writeValue(response.getWriter(), errorResponse);

    }
}
