package com.tavarlabs.pos.controllers;

import com.tavarlabs.pos.dtos.user.CreateUserRequestDto;
import com.tavarlabs.pos.dtos.user.UpdateUserRequestDto;
import com.tavarlabs.pos.dtos.user.UserResponseDto;
import com.tavarlabs.pos.entity.User;
import com.tavarlabs.pos.mappers.UserMapper;
import com.tavarlabs.pos.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(
          @Valid @RequestBody CreateUserRequestDto createUserRequestDto
    ){
        User user = userService.createUser(createUserRequestDto);
        UserResponseDto dto = userMapper.toResponseDto(user);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers(){
        List<UserResponseDto> users = userService.getAllUsers().stream()
                .map(user -> {
                    return userMapper.toResponseDto(user);
                }).toList();
        return ResponseEntity.ok(users);
    }

    @GetMapping(path = "/{username}")
    public ResponseEntity<UserResponseDto> getASingleUser(@PathVariable String username) {
        User user = userService.getASingleUser(username);
        UserResponseDto dto = userMapper.toResponseDto(user);
        return ResponseEntity.ok(dto);
    }

    @PutMapping
    public ResponseEntity<UserResponseDto> updateUser(
            @Valid @RequestBody UpdateUserRequestDto updateUserRequestDto
    ) {
        User user = userService.updateUser(updateUserRequestDto);
        UserResponseDto dto = userMapper.toResponseDto(user);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping(path = "/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }

}
