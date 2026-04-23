package com.tavarlabs.pos.controllers;

import com.tavarlabs.pos.dtos.user.UserRequestDto;
import com.tavarlabs.pos.dtos.user.UserResponseDto;
import com.tavarlabs.pos.entity.User;
import com.tavarlabs.pos.mappers.UserMapper;
import com.tavarlabs.pos.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(
            @RequestBody UserRequestDto userRequestDto
    ){
        User user = userService.createUser(userRequestDto);
        UserResponseDto dto = userMapper.toResponseDto(user);
        return ResponseEntity.ok(dto);
    }
}
