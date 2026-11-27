package com.gym.GymManagementSystem.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.gym.GymManagementSystem.Model.Enums.Gender;
import com.gym.GymManagementSystem.Model.Enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "email" , nullable = false , length = 150)
    private String email;

    @Column(name = "date_of_birth" , nullable = false )
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false , length = 20)
    private Gender gender;

    @Column(name = "phone_number" , nullable = false , unique = true, length = 20)
    private String phoneNumber; 

    @Column(nullable =  false )
    private String password;
    
    @Enumerated (EnumType.STRING)
    @Column(nullable = false , length = 20)
    private Role role;
    
    @Column(nullable = false)
    private boolean active = true ;

    @Column(name = "token_version" , nullable = false)
    private Integer tokenVersion = 0;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate(){
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (tokenVersion == null) {
            tokenVersion = 0;
        }
        active = true;
    }
}
