package com.defi.auth.role.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleInGroupId implements java.io.Serializable {
    private Long userId;
    private Long roleId;
    private Long groupId;
}