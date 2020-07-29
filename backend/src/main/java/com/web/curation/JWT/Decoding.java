package com.web.curation.JWT;

import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

import com.web.curation.model.user.User;

import org.springframework.beans.factory.annotation.Value;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
@Slf4j
public class Decoding {
    @Value("${jwt.salt}")
    private static String salt;

    @Value("${jwt.expmin}")
    private Long expireMin;

    static public String decode(String id_token) throws Exception {
        try {
            String[] tokens = ((String) id_token).split("\\.");
            Base64 base64 = new Base64(true);
            String body = new String(base64.decode(tokens[1]));

            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> result = mapper.readValue(body, Map.class);
            System.out.println("------------구글----------");
            return result.get("email");
        } catch (Exception ee) {
            Jws<Claims> claims = null;
            try {
                claims = Jwts.parser().setSigningKey(salt.getBytes()).parseClaimsJws(id_token);
    
            } catch (final Exception e) {
                throw new RuntimeException();
            }
    
            log.trace("claims : {}", claims);
            System.out.println("------------이메일----------");
    
            return (String) claims.getBody().get("email");
        }
    }
    static public String getImg (String id_token) throws Exception {
        String[] tokens = ((String) id_token).split("\\.");
        Base64 base64 = new Base64(true);
        String body = new String(base64.decode(tokens[1]));

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> result = mapper.readValue(body, Map.class);

        return result.get("picture");
    }
}