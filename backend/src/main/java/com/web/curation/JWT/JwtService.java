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
    // @Value("${jwt.salt}")
    // private String salt;

    @Value("${jwt.expmin}")
    private Long expireMin;

    public String create (final User user) {
        log.trace("time : {}", expireMin);

        KeyPair kp = RsaProvider.generateKeyPair();
        PrivateKey privateKey = kp.getPrivate();

        String jwt = Jwts.builder().setSubject("id_token").signWith(SignatureAlgorithm.RS256, privateKey)
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * expireMin))
        .claim("user", user)
        .compact();

        // final JwtBuilder builder = Jwts.builder();

        // builder.setHeaderParam("typ", "JWT");

        // builder.setSubject("id_token") // toke 이름 설정
        // .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * expireMin))
        // .claim("user",user); // 담을 정보 설정
        
        // builder.signWith(SignatureAlgorithm.RS256, salt.getBytes()); // 암호화 -> salt는 뭐지

        // final String jwt = builder.compact();
        // log.debug("token : {}", jwt);

        
        return jwt;
    }

    // public void checkValid (final String jwt){
    //     log.trace("validating token : {}", jwt);
    //     System.out.println("check valid : ");
    //     Jwts.parser().setSigningKey(salt.getBytes()).parseClaimsJws(jwt);
    // }

    // public Map<String, Object> decodeJwt (final String jwt){
    //     Jws<Claims> claims = null;
    //     try{
    //         claims = Jwts.parser().setSigningKey(salt.getBytes()).parseClaimsJws(jwt);
    //         System.out.println(claims);

    //     } catch (final Exception e) {
    //         throw new RuntimeException();
    //     }

    //     log.trace("claims : {}", claims);

    //     return claims.getBody();
    // }
    
}