package com.tavarlabs.pos.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String username;

    @ManyToMany
            @JoinTable(
                    name = "user_role",
                    joinColumns = @JoinColumn(name = "user_id"), //Column pointing to User entity
                    inverseJoinColumns = @JoinColumn(name = "role_id") //Column pointing to Role Entity
            )
    private List<Role> roles;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
