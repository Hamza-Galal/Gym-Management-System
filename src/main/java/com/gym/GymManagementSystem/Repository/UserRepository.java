package com.gym.GymManagementSystem.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gym.GymManagementSystem.Model.User;


public interface UserRepository extends JpaRepository <User , Long> {
    Optional <User> findByEmail(String email);
    boolean existsByEmail (String email);
    boolean existsByPhoneNumber(String PhoneNumber);
}
