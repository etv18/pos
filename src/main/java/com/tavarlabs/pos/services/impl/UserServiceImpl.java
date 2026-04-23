package com.tavarlabs.pos.services.impl;

import com.tavarlabs.pos.dtos.user.UserRequestDto;
import com.tavarlabs.pos.entity.User;
import com.tavarlabs.pos.repositories.UserRepository;
import com.tavarlabs.pos.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(UserRequestDto userDto) {
        User user = User.builder()
                .fullName(userDto.getFullName())
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .roles(userDto.getRoles())
                .build();

        return userRepository.save(user);
    }
}
