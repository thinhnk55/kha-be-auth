package com.defi.auth.permission.entity;

import com.defi.common.BaseModel;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Permission extends BaseModel {
    private Long roleId;
    private Long groupId;
    private Long resourceId;
    private Long actionId;
}
