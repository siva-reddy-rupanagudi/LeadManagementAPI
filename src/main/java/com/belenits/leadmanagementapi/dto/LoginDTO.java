package com.belenits.leadmanagementapi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {
    @Valid

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @NotNull(message = "Email cannot be null")
    private String email;
    @NotBlank(message = "Password is required")
    @NotNull(message = "Password cannot be null")
    private String password;
}
