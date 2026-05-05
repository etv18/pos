package com.tavarlabs.pos.dtos.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserRequestDto {
    @NotBlank(message = "User full name is required")
    private String fullName;

    @NotBlank(message = "User's username is required")
    private String username;

    @NotBlank(message = "Old user's username must be field")
    private String oldUsername;

    private String password;

    @NotNull(message = "You must assign at least one role to a user")
    @Size(min = 1, message = "You must assign at least one role to a user")
    private List<String> roles;

    private boolean active;
}
