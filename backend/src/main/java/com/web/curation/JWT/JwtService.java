package com.web.curation.JWT;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.util.Date;

import com.web.curation.model.user.User;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.RsaProvider;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtService {

    @Value("${jwt.expmin}")
    private Long expireMin;

    public String create (final User user) {
        log.trace("time : {}", expireMin);

        KeyPair kp = RsaProvider.generateKeyPair();
        PrivateKey privateKey = kp.getPrivate();

        String jwt = Jwts.builder().setSubject("id_token")
        .signWith(SignatureAlgorithm.RS256, privateKey)
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
        .claim("user", user)
        .compact();

        
        return jwt;
    }

}