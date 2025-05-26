package com.defi.auth.config;

import com.defi.auth.token.helper.RSAKeyUtil;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "auth.jwt")
public class JwtConfig {
    private String privateKey;
    private String paraphrase;
    private String publicKey;
    private Duration accessTokenTimeToLive;
    private Duration refreshTokenTimeToLive;
    private List<String> publicPaths;

    @Bean
    public RSASSAVerifier rsassaVerifier() throws Exception {
        RSAPublicKey key = RSAKeyUtil.readRSAPublicKeyFromPEM(publicKey);
        return new RSASSAVerifier(key);
    }

    @Bean
    public RSASSASigner rsassaSigner() throws Exception {
        RSAPrivateKey key = RSAKeyUtil.readRSAPrivateKeyFromPEM(privateKey, paraphrase);
        return new RSASSASigner(key);
    }
}
