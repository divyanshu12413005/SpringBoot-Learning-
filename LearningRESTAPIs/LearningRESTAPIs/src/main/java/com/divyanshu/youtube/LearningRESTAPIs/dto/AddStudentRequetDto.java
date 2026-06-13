package com.divyanshu.youtube.LearningRESTAPIs.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddStudentRequetDto {
    
    @NotBlank(message = "Student name cannot be blank or empty")
    @Size(min = 2, max = 50, message = "Student name must be between 2 and 50 characters")
    private String name;
    
    @NotBlank(message = "Student email cannot be blank or empty")
    @Email(message = "Email format is not valid")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "Email must be a valid email address (e.g. user@gmail.com)")
    private String email;
}