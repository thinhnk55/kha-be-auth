package com.defi.auth.token.service;


import com.defi.auth.token.entity.Token;
import com.defi.auth.token.entity.TokenType;
import java.util.List;

public interface TokenService {
    String generateToken(String sessionId, TokenType type,
                         String subjectID, String subjectName,  List<String> roles,
                         List<String> groups, long timeToLive);
    String refreshToken(Token token, int timeToLive);
    Token parseToken(String token);
}