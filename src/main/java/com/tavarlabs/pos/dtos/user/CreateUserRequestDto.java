package com.tavarlabs.pos.dtos.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserRequestDto {

    @NotBlank(message = "User full name is required")
    private String fullName;

    @NotBlank(message = "User's username is required")
    private String username;

    @NotBlank(message = "You must type a password")
    private String password;

    @NotNull(message = "You must assign at least one role to a user")
    @Size(min = 1, message = "You must assign at least one role to a user")
    private List<String> roles;
}
