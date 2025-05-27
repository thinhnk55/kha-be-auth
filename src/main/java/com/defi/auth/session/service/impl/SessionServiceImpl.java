package com.defi.auth.session.service.impl;

import com.defi.auth.config.SessionConfig;
import com.defi.auth.session.entity.Session;
import com.defi.auth.session.repository.SessionRepository;
import com.defi.auth.session.service.SessionService;
import com.defi.common.token.entity.Token;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionConfig config;
    private final SessionRepository repository;

    @Override
    public Session createSession(Long userId, String ip, String userAgent, ObjectNode metadata) {
        boolean check = checkCreationLimits(userId, ip);
        if(!check){
            return null;
        }
        final long now = System.currentTimeMillis()/1000;
        Session session = Session.builder()
                .id(null)
                .userId(userId)
                .startTime(now)
                .refreshTime(now)
                .notBefore(now)
                .expiredTime(now + config.getDurationInSeconds())
                .ipAddress(ip)
                .userAgent(userAgent)
                .duration(config.getDurationInSeconds())
                .metadata(metadata)
                .build();
        return repository.save(session);
    }

    private boolean checkCreationLimits(Long userId, String ip) {
        if (config.isUserLimitActive()) {
            int userSessionCount = repository.countByUserId(userId);
            if (userSessionCount >= config.getUserSessionLimit()) {
                return  false;
            }
        }
        if (config.isIpLimitActive()) {
            int ipSessionCount = repository.countByIpAddress(ip);
            if (ipSessionCount >= config.getIpSessionLimit()) {
                return  false;
            }
        }
        return true;
    }

    public Session extendSession(Token token, String ipAddress, String userAgent) {
        Optional<Session> optionalSession = repository.findById(Long.valueOf(token.getSessionId()));
        if (optionalSession.isEmpty()) {
            return null;
        }
        Session session = optionalSession.get();
        if (!session.getIpAddress().equals(ipAddress) || !session.getUserAgent().equals(userAgent)) {
            return null;
        }
        if (session.getNotBefore() > token.getIat()) {
            return null;
        }
        long now = Instant.now().getEpochSecond();
        if (session.getExpiredTime() < now) {
            return null;
        }
        long newExpiredTime = now + session.getDuration();
        session.setExpiredTime(newExpiredTime);
        session.setRefreshTime(now);
        session.setNotBefore(now);

        return repository.save(session);
    }
}
