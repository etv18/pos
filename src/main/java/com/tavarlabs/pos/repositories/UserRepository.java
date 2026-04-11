package com.tavarlabs.pos.repositories;

import com.tavarlabs.pos.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
