package com.tavarlabs.pos.mappers;

import com.tavarlabs.pos.dtos.role.RoleDto;
import com.tavarlabs.pos.entity.Role;
import com.tavarlabs.pos.enums.RoleName;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper {
    default RoleName toRoleNameEnum(String role){
        try{
            return RoleName.valueOf("ROLE_" + role);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role: " + role);
        }
    }
    @Mapping(target = "roleName", source = ".", qualifiedByName = "removeRolePrefix")
    RoleDto toDto(Role role);

    @Named("removeRolePrefix")
    default String removeRolePrefix(Role role){
        final String ROLE_ = "ROLE_";
        String rawRoleName = role.getRoleName().name();
        return rawRoleName.substring(ROLE_.length());
    }
}
