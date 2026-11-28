package com.gym.GymManagementSystem.DTO.Response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.gym.GymManagementSystem.Model.Enums.Gender;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MemberResponse {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Gender gender;
    private LocalDate dateOfBirth;
    private boolean active;
    private LocalDateTime createdAt;
    
}
