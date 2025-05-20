package com.defi.auth.config;

import com.defi.auth.token.helper.RSAKeyUtil;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
public class JwtConfig {
    @Value("${jwt.private-key}")
    private String privateKeyPem;
    @Value("${jwt.paraphrase}")
    private String paraphrase;
    @Value("${jwt.public-key}")
    private String publicKeyPem;

    @Bean
    public RSASSAVerifier getRSASSAVerifier() throws Exception {
        RSAPublicKey publicKey = RSAKeyUtil.readRSAPublicKeyFromPEM(publicKeyPem);
        return new RSASSAVerifier(publicKey);
    }
    @Bean
    public RSASSASigner getRSASSASigner() throws Exception {
        RSAPrivateKey privateKey = RSAKeyUtil.readRSAPrivateKeyFromPEM(privateKeyPem, paraphrase);
        return new RSASSASigner(privateKey);
    }
}
