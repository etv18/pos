package com.tavarlabs.pos.repositories;

import com.tavarlabs.pos.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
