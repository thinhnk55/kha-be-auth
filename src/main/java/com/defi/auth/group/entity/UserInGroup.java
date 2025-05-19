package com.defi.auth.group.entity;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "user_in_group")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInGroup {
    @EmbeddedId
    private UserInGroupId id;
}

