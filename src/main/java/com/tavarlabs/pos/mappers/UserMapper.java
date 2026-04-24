package com.tavarlabs.pos.mappers;

import com.tavarlabs.pos.dtos.user.UserResponseDto;
import com.tavarlabs.pos.entity.Role;
import com.tavarlabs.pos.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {
    @Mapping(target = "roles", source = ".", qualifiedByName = "mapToRolesToStrings")
    UserResponseDto toResponseDto(User user);

    @Named("mapToRolesToStrings")
    default List<String> mapToRolesToStrings(User user){
        final String ROLE_ = "ROLE_";
        return user.getRoles().stream()
                .map(role -> {
                    return role.getRoleName().name().replace(ROLE_, "");
                })
                .toList();
    }
}
