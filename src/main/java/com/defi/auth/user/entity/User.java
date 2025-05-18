package com.defi.auth.user.entity;

import com.defi.auth.common.BaseModel;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "users")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseModel {

    private String userName;

    private String fullName;

    private String email;

    private boolean emailVerified;

    private String phone;

    private boolean phoneVerified;

    private boolean locked;

    private Long lockedUntil;
}
