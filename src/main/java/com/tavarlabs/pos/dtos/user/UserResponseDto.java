package com.tavarlabs.pos.dtos.user;

import com.tavarlabs.pos.dtos.role.RoleDto;
import com.tavarlabs.pos.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {
    private String fullName;
    private String username;
    private boolean isActive;
    private List<String> roles;
}
