package com.defi.auth.user.repository;

import com.defi.auth.user.entity.UserCredential;
import com.defi.auth.user.entity.UserCredentialId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCredentialRepository extends JpaRepository<UserCredential, UserCredentialId> {
    Optional<UserCredential> findByIdUserIdAndIdType(Long userId, String type);
    boolean existsByIdUserIdAndIdType(Long userId, String type);
}
