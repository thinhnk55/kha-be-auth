package com.defi.auth.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_credentials")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCredential {

    @EmbeddedId
    private UserCredentialId id;

    private String secretData;
    private String salt;
    private String credentialData;

    private Long createdAt;
    private Long updatedAt;

    @PrePersist
    public void prePersist() {
        long now = System.currentTimeMillis();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = System.currentTimeMillis();
    }
}

