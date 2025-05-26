package com.defi.auth.role.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_has_role")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class UserHasRole {
    @EmbeddedId
    private UserHasRoleId id;
}
