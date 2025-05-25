package com.defi.auth.role.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_role_in_group")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class UserHasRole {

    @EmbeddedId
    private UserHasRoleId id;

    private Long assignedAt;

    @PrePersist
    public void prePersist() {
        this.assignedAt = System.currentTimeMillis();
    }
}
