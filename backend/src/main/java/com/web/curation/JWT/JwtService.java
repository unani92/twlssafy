package com.web.curation.JWT;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtService {
    @Value("${jwt.salt}")
    private String salt;

    
}