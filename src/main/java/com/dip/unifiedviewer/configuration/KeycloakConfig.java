//package com.dip.unifiedviewer.configuration;
//
//import java.security.KeyFactory;
//import java.security.NoSuchAlgorithmException;
//import java.security.PublicKey;
//import java.security.spec.InvalidKeySpecException;
//import java.security.spec.X509EncodedKeySpec;
//import java.util.Base64;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import io.jsonwebtoken.SignatureAlgorithm;
//
//@Configuration
//public class KeycloakConfig {
//
//    @Value("${dip.publicapi.rule.baseUrl}")
//    private String publicKey;
//
//    @Bean
//    public PublicKey generatePublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
//        KeyFactory keyFactory = KeyFactory.getInstance(SignatureAlgorithm.RS256.getFamilyName());
//        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey));
//        return keyFactory.generatePublic(publicKeySpec);
//    }
//}
