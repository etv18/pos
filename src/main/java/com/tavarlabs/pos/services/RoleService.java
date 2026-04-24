package com.tavarlabs.pos.services;

import com.tavarlabs.pos.entity.Role;

import java.util.List;

public interface RoleService {
    List<Role> mapRoles(List<String> rolesStr);
}
