package com.web.curation.JWT;

import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;


public class JWTDecoding  {

    static public String decode (String id_token) throws Exception {
        String[] tokens = ((String) id_token).split("\\.");
        Base64 base64 = new Base64(true);
        // String body = new String(base64.decode(tokens[1]));
        String body;
        try {
            body = new String(base64.decode(tokens[1]));
            
        } catch (Exception e) {
            return "";
        }

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> result = mapper.readValue(body, Map.class);

        // System.out.println("token[1] : " + new String(Base64.decodeBase64(tokens[1]), "utf-8"));
        
        String email;
        if(result.get("email") == null){
            Map<String,String> user = (Map<String, String>) result.get("user");
            email = (String) user.get("email");

        } else
            email = (String) result.get("email");

        return email;
    }

    static public String getImg (String id_token) throws Exception {
        String[] tokens = ((String) id_token).split("\\.");
        Base64 base64 = new Base64(true);
        String body = new String(base64.decode(tokens[1]));

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> result = mapper.readValue(body, Map.class);

        return result.get("picture");
    }

    static public Map<String,Object> getInfo (String id_token) throws Exception {
        String[] tokens = ((String) id_token).split("\\.");
        Base64 base64 = new Base64(true);
        String body = new String(base64.decode(tokens[1]));

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> result = mapper.readValue(body, Map.class);

        // System.out.println("token[1] : " + new String(Base64.decodeBase64(tokens[1]), "utf-8"));


        return (Map<String, Object>) result.get("user");
    }

}