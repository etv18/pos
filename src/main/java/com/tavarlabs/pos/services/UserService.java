package com.tavarlabs.pos.services;

import com.tavarlabs.pos.dtos.user.CreateUserRequestDto;
import com.tavarlabs.pos.dtos.user.UpdateUserRequestDto;
import com.tavarlabs.pos.entity.User;

import java.util.List;

public interface UserService {
    User createUser(CreateUserRequestDto userDto);
    List<User> getAllUsers();
    User getASingleUser(String username);
    User updateUser(UpdateUserRequestDto userDto);
    void deleteUser(String username);
}
