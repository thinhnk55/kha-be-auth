package com.defi.auth.session.service.impl;

import com.defi.auth.session.service.SessionCleanupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionCleanupServiceImpl implements SessionCleanupService {
    private final JdbcTemplate jdbcTemplate;
    @Scheduled(cron = "0 0 1 * * ?")
    public void cleanupExpiredSessions() {
        try {
            jdbcTemplate.execute("SELECT delete_expired_sessions();");
            log.info("clean up expired sessions");
        }catch (Exception e){
            log.error("", e);
        }
    }
}
