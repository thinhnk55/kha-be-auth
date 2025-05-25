package com.defi.auth.casbin;

import com.defi.common.CommonMessage;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.casbin.jcasbin.main.Enforcer;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@Aspect
@Component
@RequiredArgsConstructor
public class CasbinAuthorizeAspect {

    private final Enforcer enforcer;

    @Before("@annotation(casbinAuth)")
    public void enforcePermission(JoinPoint joinPoint, CasbinAuthorize casbinAuth) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, CommonMessage.UNAUTHORIZED);
        }

        List<String> roles = auth.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        String obj = casbinAuth.resource();
        String act = casbinAuth.action();

        boolean permitted = roles.stream()
                .anyMatch(role -> enforcer.enforce(role, obj, act));

        if (!permitted) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, CommonMessage.FORBIDDEN);
        }
    }
}
