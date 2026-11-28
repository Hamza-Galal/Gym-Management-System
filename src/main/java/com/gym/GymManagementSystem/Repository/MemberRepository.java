package com.gym.GymManagementSystem.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gym.GymManagementSystem.Model.Member;

public interface MemberRepository extends JpaRepository<Member , Long> {

    @Override
    @EntityGraph(attributePaths = "user")
    List<Member> findAll();

    @Override
    @EntityGraph(attributePaths = "user")
    Optional<Member> findById(Long id);
}
