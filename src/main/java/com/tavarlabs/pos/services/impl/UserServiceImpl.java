package com.tavarlabs.pos.services.impl;

import com.tavarlabs.pos.dtos.user.CreateUserRequestDto;
import com.tavarlabs.pos.dtos.user.UpdateUserRequestDto;
import com.tavarlabs.pos.entity.Role;
import com.tavarlabs.pos.entity.User;
import com.tavarlabs.pos.enums.RoleName;
import com.tavarlabs.pos.mappers.RoleMapper;
import com.tavarlabs.pos.repositories.UserRepository;
import com.tavarlabs.pos.services.RoleService;
import com.tavarlabs.pos.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final RoleMapper roleMapper;

    @Override
    public User createUser(CreateUserRequestDto userDto) {
        List<Role> roles = roleService.mapRoles(userDto.getRoles());

        User user = User.builder()
                .fullName(userDto.getFullName())
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .isActive(false)
                .roles(roles)
                .build();

        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAllWithRoles();
    }

    @Override
    public User updateUser(UpdateUserRequestDto userDto) {
        String oldUsername = userDto.getOldUsername();
        String newUsername = userDto.getUsername();
        final String ROLE_ = "ROLE_";

        User savedUser = userRepository.findByUsername(oldUsername)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User with username [ " + oldUsername + " ] was not found"
                ));

        if(!savedUser.getUsername().equals(newUsername) && !isUsernameBeingUsed(newUsername)) {
            savedUser.setUsername(newUsername);
        }

        if(!savedUser.getFullName().equals(userDto.getFullName())){
            savedUser.setFullName(userDto.getFullName());
        }

        if(!passwordEncoder.matches(userDto.getPassword(), savedUser.getPassword())) {
            savedUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        if(savedUser.getIsActive() != userDto.isActive()){
            savedUser.setIsActive(userDto.isActive());
        }

        if(!rolesAreTheSame(userDto.getRoles(), savedUser.getRoles())){
            List<Role> newRoles = roleService.mapRoles(userDto.getRoles());
            savedUser.setRoles(newRoles);
        }

        return userRepository.save(savedUser);
    }

    private boolean isUsernameBeingUsed(String username) throws IllegalArgumentException{
        boolean exists = userRepository.existsByUsername(username);
        if(exists) throw new IllegalArgumentException("This username [ " + username + " ] is registered.");
        return false;
    }

    private boolean rolesAreTheSame(List<String> userRequestRoles, List<Role> savedUserRoles){
        Set<RoleName> userRequestRolesEnum = userRequestRoles.stream()
                .map(roleStr -> roleMapper.toRoleNameEnum(roleStr))
                .collect(Collectors.toSet());

        Set<RoleName> savedUserRolesEnum = savedUserRoles.stream()
                .map(role -> role.getRoleName())
                .collect(Collectors.toSet());

        return savedUserRolesEnum.equals(userRequestRolesEnum);
    }
}
