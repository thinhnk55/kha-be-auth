package com.defi.auth.token.service.impl;

import com.defi.auth.token.entity.ClaimField;
import com.defi.auth.token.entity.SubjectType;
import com.defi.auth.token.entity.Token;
import com.defi.auth.token.entity.TokenType;
import com.defi.auth.token.service.TokenService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * {@code TokenManager} is a singleton class responsible for issuing, signing, validating,
 * and parsing JWT access tokens using RSA public/private key pairs via the Nimbus JOSE + JWT library.
 *
 * <p>Tokens are generated from a {@link Token} domain object and signed with RS256.</p>
 *
 * <p>This utility supports:</p>
 * <ul>
 *     <li>Loading RSA keys from PEM strings</li>
 *     <li>Creating signed JWTs for sessions</li>
 *     <li>Refreshing expired tokens</li>
 *     <li>Validating and parsing existing tokens</li>
 * </ul>
 */


@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final RSASSASigner signer;
    private final  RSASSAVerifier verifier;
    /**
     * Generates a signed JWT access token with the provided session and subject details.
     *
     * @param sessionId    the session
     * @param type         the token type (e.g., access, refresh)
     * @param subjectID    the ID of the subject (usually user ID)
     * @param subjectName  the display name or username of the subject
     * @param roles        list of role IDs
     * @param groups       list of group IDs
     * @param timeToLive   token TTL in seconds
     * @return a signed JWT string
     */
    public String generateToken(String sessionId, TokenType type,
                                String subjectID, String subjectName, List<String> roles,
                                List<String> groups, long timeToLive) {
        long issuedAt = Instant.now().getEpochSecond();
        Token token = Token.builder()
                .sessionId(sessionId)
                .tokenType(type)
                .subjectId(subjectID)
                .subjectName(subjectName)
                .subjectType(SubjectType.USER)
                .roles(roles)
                .groups(groups)
                .iat(issuedAt)
                .exp(issuedAt + timeToLive)
                .build();
        return signToken(token);
    }

    /**
     * Refreshes an existing token by creating a new one with updated issue and expiration times.
     *
     * @param token       the original token object
     * @param timeToLive  time-to-live in seconds for the new token
     * @return a new signed JWT string
     */
    public String refreshToken(Token token, int timeToLive) {
        long issuedAt = Instant.now().getEpochSecond();
        Token newToken = Token.builder()
                .tokenType(TokenType.ACCESS_TOKEN)
                .sessionId(token.getSessionId())
                .subjectId(token.getSubjectId())
                .subjectName(token.getSubjectName())
                .subjectType(token.getSubjectType())
                .roles(token.getRoles())
                .groups(token.getGroups())
                .iat(issuedAt)
                .exp(issuedAt + timeToLive)
                .build();
        return signToken(newToken);
    }

    /**
     * Signs a {@link Token} object as a JWT using RS256 algorithm.
     *
     * @param payload the token payload
     * @return a signed JWT string
     */
    private String signToken(Token payload) {
        try {
            JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256)
                    .type(JOSEObjectType.JWT)
                    .build();

            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(payload.getSubjectId())
                    .issueTime(new Date(payload.getIat() * 1000))
                    .expirationTime(new Date(payload.getExp() * 1000))
                    .claim(ClaimField.ID.getName(), payload.getSessionId())
                    .claim(ClaimField.TYPE.getName(), payload.getTokenType().getName())
                    .claim(ClaimField.SUBJECT_NAME.getName(), payload.getSubjectName())
                    .claim(ClaimField.SUBJECT_TYPE.getName(), payload.getSubjectName())
                    .claim(ClaimField.ROLES.getName(), payload.getRoles())
                    .claim(ClaimField.GROUPS.getName(), payload.getGroups())
                    .build();

            SignedJWT signedJWT = new SignedJWT(header, claimsSet);
            signedJWT.sign(signer);
            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Validates the signature and expiration of the provided JWT.
     *
     * @param signedJWT the parsed JWT object
     * @return {@code true} if the token is valid and not expired, otherwise {@code false}
     */
    public boolean validateToken(SignedJWT signedJWT) {
        try {
            boolean signatureValid = signedJWT.verify(verifier);
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            long expiresAt = claims.getExpirationTime().toInstant().getEpochSecond();
            long now = Instant.now().getEpochSecond();
            boolean notExpired = now < expiresAt;
            return signatureValid && notExpired;
        } catch (JOSEException | ParseException e) {
            return false;
        }
    }

    /**
     * Parses a JWT string and returns the {@link Token} object if valid.
     *
     * @param token the JWT string
     * @return a valid {@link Token} object, or {@code null} if invalid
     */
    public Token parseToken(String token) {
        try {
            if(token == null){
                return null;
            }
            SignedJWT signedJWT = SignedJWT.parse(token);
            if (!validateToken(signedJWT)) {
                return null;
            }

            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

            List<String> roles = claims.getListClaim(ClaimField.ROLES.getName())
                    .stream()
                    .map(String::valueOf)
                    .collect(Collectors.toList());

            List<String> groups = claims.getListClaim(ClaimField.GROUPS.getName())
                    .stream()
                    .map(String::valueOf)
                    .collect(Collectors.toList());

            long issuedAt = claims.getIssueTime().toInstant().getEpochSecond();
            long expiresAt = claims.getExpirationTime().toInstant().getEpochSecond();

            return Token.builder()
                    .sessionId( (String) claims.getClaim(ClaimField.ID.getName()))
                    .tokenType(TokenType.forName((String) claims.getClaim(ClaimField.TYPE.getName())))
                    .subjectId(claims.getSubject())
                    .subjectName((String) claims.getClaim(ClaimField.SUBJECT_NAME.getName()))
                    .subjectType(SubjectType.forName((String) claims.getClaim(ClaimField.SUBJECT_TYPE.getName())))
                    .roles(roles)
                    .groups(groups)
                    .iat(issuedAt)
                    .exp(expiresAt)
                    .build();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}

