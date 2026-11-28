
package com.gym.GymManagementSystem.Service;

import java.util.Locale;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gym.GymManagementSystem.DTO.Request.LoginRequest;
import com.gym.GymManagementSystem.DTO.Response.AuthResponse;
import com.gym.GymManagementSystem.Exception.AccountDisabledException;
import com.gym.GymManagementSystem.Exception.InvalidCredentialsException;
import com.gym.GymManagementSystem.Model.User;
import com.gym.GymManagementSystem.Repository.UserRepository;
import com.gym.GymManagementSystem.Security.JwtService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request){
        String email = normalizeEmail(request.getEmail());
        User user = userRepository.findByEmail(email).
        orElseThrow(()-> new InvalidCredentialsException("Invalid Email or token"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
        if (!user.isActive()) {
            throw new AccountDisabledException("Your account has been disabled");
        }
        String token = jwtService.generateToken(user);
        return  buildAuthResponse(user, token);
    }

    private AuthResponse buildAuthResponse(User user, String token) {

        AuthResponse response = new AuthResponse();

        response.setToken(token);
        response.setTokenType("Bearer");
        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setGender(user.getGender());
        response.setDateOfBirth(user.getDateOfBirth());
        response.setRole(user.getRole());
        response.setCreatedAt(user.getCreatedAt());

        return response;
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    
}
