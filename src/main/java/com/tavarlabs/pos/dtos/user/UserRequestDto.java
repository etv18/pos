package com.tavarlabs.pos.dtos.user;

import com.tavarlabs.pos.entity.Role;
import com.tavarlabs.pos.enums.RoleName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestDto {
    private String fullName;
    private String username;
    private String password;
    private List<String> roles;
}
