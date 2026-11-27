package com.gym.GymManagementSystem.DTO.Response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.gym.GymManagementSystem.Model.Enums.Role;

import com.gym.GymManagementSystem.Model.Enums.Gender;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthResponse {
    private String token;
    private String tokenType;
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private Gender gender;
    private Role role;
    
    private LocalDateTime createdAt;

    
}
