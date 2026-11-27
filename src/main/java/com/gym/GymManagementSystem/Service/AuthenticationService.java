
package com.gym.GymManagementSystem.Service;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gym.GymManagementSystem.DTO.Request.LoginRequest;
import com.gym.GymManagementSystem.DTO.Request.RegisterRequest;
import com.gym.GymManagementSystem.DTO.Response.AuthResponse;
import com.gym.GymManagementSystem.Exception.AccountDisabledException;
import com.gym.GymManagementSystem.Exception.DuplicateResourceException;
import com.gym.GymManagementSystem.Exception.InvalidCredentialsException;
import com.gym.GymManagementSystem.Model.User;
import com.gym.GymManagementSystem.Model.Enums.Role;
import com.gym.GymManagementSystem.Repository.UserRepository;
import com.gym.GymManagementSystem.Security.JwtService;

import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {

        String email = normalizeEmail(request.getEmail());
        String phoneNumber = normalizePhoneNumber(request.getPhoneNumber());

        if (userRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("Email is already registered");
        }

        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DuplicateResourceException("Phone Number is already registered");
        }

        validatePasswordLength(request.getPassword());

        User savedUser = createAndSaveMember(request, email, phoneNumber);

        String token = jwtService.generateToken(savedUser);

        return buildAuthResponse(savedUser, token);
    }

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


    private User createAndSaveMember(
            RegisterRequest request,
            String normalizedEmail,
            String normalizedPhoneNumber) {

        User user = new User();

        user.setFirstName(request.getFirstName().trim());
        user.setLastName(request.getLastName().trim());
        user.setEmail(normalizedEmail);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(normalizedPhoneNumber);
        user.setGender(request.getGender());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setRole(Role.MEMBER);
        user.setActive(true);
        user.setTokenVersion(0);

        return userRepository.save(user);
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

    private String normalizePhoneNumber(String phoneNumber) {

        String normalized = phoneNumber.replaceAll("[\\s-]", "");

        if (normalized.startsWith("0020")) {
            normalized = normalized.substring(4);
        } else if (normalized.startsWith("+20")) {
            normalized = normalized.substring(3);
        }

        if (normalized.startsWith("0")) {
            normalized = normalized.substring(1);
        }

        return "+20" + normalized;
    }

    private void validatePasswordLength(String password) {

        int byteLength = password.getBytes(StandardCharsets.UTF_8).length;

        if (byteLength > 72) {
            throw new ValidationException("Password must not exceed 72 bytes");
        }
    }
}
