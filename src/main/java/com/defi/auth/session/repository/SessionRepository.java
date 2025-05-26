package com.defi.auth.session.repository;

import com.defi.auth.session.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {
    int countByUserId(Long userId);
    int countByIpAddress(String ipAddress);
}
