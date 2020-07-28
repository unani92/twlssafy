package com.web.curation.social;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.curation.dao.SocialMemberDao;
import com.web.curation.model.BasicResponse;
import com.web.curation.model.user.SocialMember;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@CrossOrigin
@Controller
public class OAuth2Controller {

    @Autowired
    SocialMemberDao socialmemberDao;

    @PostMapping("/googlelogin")
    public Object doSessionAssignActionPage(@RequestHeader final HttpHeaders header) throws Exception {

        String id_token = header.get("id_token").get(0);
        String[] tokens = (id_token.split("\\."));
        Base64 base64 = new Base64(true);
        String body = new String(base64.decode(tokens[1]));

        System.out.println("token length : " + tokens.length);
        System.out.println("token[0] : " + new String(Base64.decodeBase64(tokens[0]), "utf-8"));
        System.out.println("token[1] : " + new String(Base64.decodeBase64(tokens[1]), "utf-8"));

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> result = mapper.readValue(body, Map.class);
        System.out.println("result : " + result.get("email"));

        // 가입여부 판단
        Map<String, Object> object = new HashMap<>();
        object.put("isJoined", isJoined(result.get("email")));
        object.put("id_token" , id_token);

        final BasicResponse res = new BasicResponse();
        System.out.println("is joined : "+isJoined(result.get("email")));
        System.out.println("TOKEN : "+id_token);

        Map<String, String> response = new HashMap<>();
        response.put("email", result.get("email"));

        // 가입된 회원이면 -> success 회원 정보 토큰 반환
        if(isJoined(result.get("email"))) {
            res.object = object;
            res.data = "sucess";
            res.status = true;
        }

        // 가입안된 회원이면 -> failure 반환
        else {
            res.data = "failed";
            res.status = true;
            res.object = response;
        }

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    private boolean isJoined(String email) {
        Optional<SocialMember> socialmember = socialmemberDao.findSocialmemberByEmail(email);

        if (socialmember.isPresent()) {
           return true; 
        } else {
            return false;
        }
    }

}


