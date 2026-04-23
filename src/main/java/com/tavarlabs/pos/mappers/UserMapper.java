package com.tavarlabs.pos.mappers;

import com.tavarlabs.pos.dtos.user.UserResponseDto;
import com.tavarlabs.pos.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserResponseDto toResponseDto(User user);
}
