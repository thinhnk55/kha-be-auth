package com.defi.auth.session.service;

import com.defi.auth.session.entity.Session;
import com.fasterxml.jackson.databind.node.ObjectNode;

public interface SessionService {
    Session createSession(Long userId, String ip, String userAgent, ObjectNode metadata);
}
