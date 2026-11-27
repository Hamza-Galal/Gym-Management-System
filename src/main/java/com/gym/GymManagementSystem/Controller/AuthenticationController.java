package com.gym.GymManagementSystem.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gym.GymManagementSystem.DTO.Request.LoginRequest;
import com.gym.GymManagementSystem.DTO.Request.RegisterRequest;
import com.gym.GymManagementSystem.DTO.Response.AuthResponse;
import com.gym.GymManagementSystem.Service.AuthenticationService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

@PostMapping("/register")
public ResponseEntity<AuthResponse> createUser(@Valid @RequestBody RegisterRequest request) {
    AuthResponse response = authenticationService.register(request);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
}

@PostMapping("/login")
public ResponseEntity<AuthResponse> loginUser(@Valid @RequestBody LoginRequest request) {
    AuthResponse response = authenticationService.login(request);

    return ResponseEntity.ok(response);
}

    

}
