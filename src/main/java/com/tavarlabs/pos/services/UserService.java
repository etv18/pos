package com.tavarlabs.pos.services;

import com.tavarlabs.pos.dtos.user.UserRequestDto;
import com.tavarlabs.pos.entity.User;

public interface UserService {
    User createUser(UserRequestDto userDto);
}
