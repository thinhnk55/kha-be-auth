package com.defi.auth.user.service.impl;

import com.defi.auth.session.service.SessionService;
import com.defi.auth.user.service.PrivateUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrivateUserServiceImpl implements PrivateUserService {
    private final SessionService sessionService;

    @Override
    public void logout(Long sessionId) {
        sessionService.deleteSession(sessionId);
    }
}