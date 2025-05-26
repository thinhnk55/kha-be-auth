package com.defi.auth.role.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserHasRoleId implements java.io.Serializable {
    private Long userId;
    private Long roleId;
}