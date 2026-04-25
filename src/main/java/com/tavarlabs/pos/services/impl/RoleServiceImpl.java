package com.tavarlabs.pos.services.impl;

import com.tavarlabs.pos.entity.Role;
import com.tavarlabs.pos.enums.RoleName;
import com.tavarlabs.pos.mappers.RoleMapper;
import com.tavarlabs.pos.repositories.RoleRepository;
import com.tavarlabs.pos.services.RoleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleMapper roleMapper;
    private final RoleRepository roleRepository;

    @Override
    public List<Role> mapRoles(List<String> rolesStr) {
        if(rolesStr.isEmpty()) throw new IllegalArgumentException("User must have at least one role.");
        return rolesStr.stream()
                .map(str -> {
                    return roleMapper.toRoleNameEnum(str);
                })
                .map(roleName -> {
                    return roleRepository.findByRoleName(roleName)
                            .orElseThrow(() ->
                                    new EntityNotFoundException(
                                            "Role name [ " + roleName + " ] was not found"
                                    )
                            );
                })
                .collect(Collectors.toList());
    }
}
