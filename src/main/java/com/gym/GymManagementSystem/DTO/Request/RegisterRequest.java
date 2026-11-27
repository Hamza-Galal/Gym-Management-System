package com.gym.GymManagementSystem.DTO.Request;

import java.time.LocalDate;

import com.gym.GymManagementSystem.Model.Enums.Gender;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    @NotBlank(message = "First Name is required!")
    private  String firstName;

    @NotBlank(message = "Last Name is required!")
    private  String lastName;

    @NotBlank(message = "Email is required!")
    @Email(message = "Invalid Email Format!")
    private String email;

    @NotBlank(message = "Password is required!")
    @Size(min = 8 , max = 72 ,message = "Password must be in between 8 to 72 characters")
    private String password;

    @NotBlank(message = "Phone Number is required!")
    @Pattern(
        regexp = "^(\\+20|0020|0)1[0125][0-9]{8}$",
        message = "Phone Number must be a vaild Egyptian Number!"
    )
    private String phoneNumber;

    @NotNull(message = "Gender is required!")
    private Gender gender;

    @NotNull(message = "Date Of Birth is required!")
    @Past(message = "Date Of Birth Must be in the past!")
    private LocalDate dateOfBirth;

}
