package com.tavarlabs.pos.services.impl;

import com.tavarlabs.pos.dtos.user.UserRequestDto;
import com.tavarlabs.pos.entity.Role;
import com.tavarlabs.pos.entity.User;
import com.tavarlabs.pos.repositories.UserRepository;
import com.tavarlabs.pos.services.RoleService;
import com.tavarlabs.pos.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(UserRequestDto userDto) {
        List<Role> roles = roleService.mapRoles(userDto.getRoles());

        User user = User.builder()
                .fullName(userDto.getFullName())
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .isActive(true)
                .roles(roles)
                .build();

        return userRepository.save(user);
    }
}
