package com.defi.auth.role.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "group_has_role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupHasRole {
    @EmbeddedId
    private GroupHasRoleId id;
}
