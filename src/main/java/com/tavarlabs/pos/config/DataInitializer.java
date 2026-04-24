package com.tavarlabs.pos.config;

import com.tavarlabs.pos.entity.Role;
import com.tavarlabs.pos.enums.RoleName;
import com.tavarlabs.pos.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository){
        return args -> {
            for(RoleName roleName : RoleName.values()){
                try {
                    roleRepository.findByRoleName(roleName).orElseGet(() -> {
                        Role role = Role.builder()
                                .roleName(roleName)
                                .build();
                        return roleRepository.save(role);
                    });
                } catch (ConstraintViolationException e) {
                    //TODO: set up logger for when it tries to insert a role and its duplicated in db
                } catch (Exception e) {
                    //TODO: same as above
                }
            }
        };
    }
}
