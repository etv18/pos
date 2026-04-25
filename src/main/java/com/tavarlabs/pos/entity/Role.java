package com.tavarlabs.pos.entity;

import com.tavarlabs.pos.enums.RoleName;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private RoleName roleName;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(id, role.id) && roleName == role.roleName && Objects.equals(createdAt, role.createdAt) && Objects.equals(updatedAt, role.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roleName, createdAt, updatedAt);
    }

    public boolean shareRoleNames(RoleName externalRoleName){
        return externalRoleName.name().equals(this.roleName.name());
    }
}
