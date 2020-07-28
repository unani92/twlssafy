package com.web.curation.controller;

import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;


public class JWTDecoding  {

    static public String decode (String id_token) throws Exception {
        String[] tokens = ((String) id_token).split("\\.");
        Base64 base64 = new Base64(true);
        String body = new String(base64.decode(tokens[1]));

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> result = mapper.readValue(body, Map.class);

        return result.get("email");
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