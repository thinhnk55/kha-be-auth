package com.defi.auth.filter;

import com.defi.auth.token.entity.Token;
import com.defi.auth.token.entity.TokenType;
import com.defi.auth.token.service.TokenService;
import com.defi.common.BaseResponse;
import com.defi.common.CommonMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor
@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final ObjectMapper mapper;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest req,@NonNull HttpServletResponse res,
                                    @NonNull FilterChain chain) throws ServletException, IOException {
        String accessToken = resolveToken(req);
        Token token = tokenService.parseToken(accessToken);
        if (token == null || token.getTokenType() != TokenType.ACCESS_TOKEN) {
            res.setContentType("application/json;charset=UTF-8");
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            BaseResponse<?> baseResponse = BaseResponse.of(HttpStatus.UNAUTHORIZED.value()
                    , CommonMessage.UNAUTHORIZED);
            res.getWriter().write(mapper.writeValueAsString(baseResponse));
            return;
        }
        Authentication auth = getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(auth);
        chain.doFilter(req, res);
    }
    private String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        return (bearerToken != null && bearerToken.startsWith("Bearer ")) ? bearerToken.substring(7) : null;
    }
    public Authentication getAuthentication(Token token) {
        UserDetails userDetails = User
                .withUsername(token.getSubjectName())
                .password("")
                .authorities(token.getRoles().stream()
                        .map(String::valueOf).toList()
                        .toArray(new String[0]))
                .build();
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
