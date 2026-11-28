package com.gym.GymManagementSystem.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gym.GymManagementSystem.DTO.Request.CreateMemberRequest;
import com.gym.GymManagementSystem.DTO.Response.MemberResponse;
import com.gym.GymManagementSystem.Exception.DuplicateResourceException;
import com.gym.GymManagementSystem.Exception.ResourceAccessDeniedException;
import com.gym.GymManagementSystem.Exception.ResourceNotFoundException;
import com.gym.GymManagementSystem.Exception.ValidationException;
import com.gym.GymManagementSystem.Model.Member;
import com.gym.GymManagementSystem.Model.User;
import com.gym.GymManagementSystem.Model.Enums.Role;
import com.gym.GymManagementSystem.Repository.MemberRepository;
import com.gym.GymManagementSystem.Repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public MemberResponse registerMember(CreateMemberRequest request){
        String email = normalizeEmail(request.getEmail());
        String phone = normalizePhoneNumber(request.getPhoneNumber());

        validatePasswordLength(request.getPassword());

        if (userRepository.existsByEmail(email)) {
            throw new   DuplicateResourceException("Email Already Exists");
        }

        if (userRepository.existsByPhoneNumber(phone)) {
            throw new DuplicateResourceException("Phone Number Already Exists");
        }
        User savedUser = userRepository.save(buildUserAccount(request, email ,phone));
        Member savedMember = memberRepository.save(buildMember(savedUser));
        return buildMemberResponse(savedMember);
    }

    @Transactional(readOnly = true)
    public MemberResponse getMemberById(Long id , User currentUser){
        Member member = memberRepository.findById(id).
        orElseThrow(()-> new ResourceNotFoundException("Member Not Found with id: " + id));

        boolean isAdmin = currentUser.getRole() == Role.ADMIN;
        boolean isOwner = member.getUser().getId().equals(currentUser.getId());

        if (!isAdmin && !isOwner) {
            throw new ResourceAccessDeniedException("you can only view your own member profile");
        }
        return buildMemberResponse(member);
    }
    @Transactional(readOnly = true)
    public List<MemberResponse> getAllMembers(){
        return memberRepository.findAll().stream().map(this::buildMemberResponse).toList();
    }


    private User buildUserAccount(CreateMemberRequest request , String email , String phone){
        User user = new User();
        user.setFirstName(request.getFirstName().trim());
        user.setLastName(request.getLastName().trim());
        user.setEmail(email);
        user.setPhoneNumber(phone);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setGender(request.getGender());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setRole(Role.MEMBER);
        user.setActive(true);
        user.setTokenVersion(0);
        return user;

    }

    private Member buildMember(User user){
        Member member = new Member();
        member.setUser(user);
        member.setActive(true);
        return  member;
    }

    private MemberResponse buildMemberResponse(Member member){
        User user = member.getUser();
        MemberResponse response = new MemberResponse();
        response.setId(member.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setDateOfBirth(user.getDateOfBirth());
        response.setGender(user.getGender());
        response.setActive(member.isActive());
        response.setCreatedAt(member.getCreatedAt());
        return  response;
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
