package com.web.curation.social;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.curation.dao.user.UserDao;
import com.web.curation.model.BasicResponse;
import com.web.curation.model.user.User;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin
@Controller
public class OAuth2Controller {

    @Autowired
    UserDao userDao;

    @PostMapping("/googlelogin")
    public Object doSessionAssignActionPage(
    @RequestBody final Map<String,Object> id_token) throws Exception {
        
        String[] tokens = ((String) id_token.get("id_token")).split("\\.");
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
        object.put("tokens" ,tokens);
        object.put("email", result.get("email"));


        final BasicResponse res = new BasicResponse();
        res.object = object;
        res.data = "sucess";
        res.status = true;

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    private String isJoined(String email) {
        Optional<User> user = userDao.findUserByEmail(email);

        if (user.isPresent()) {
            int pw = user.get().getPassword();
            if(pw<0 || pw>=0) {
                // id도 있고, pw도 있음 -> 이미 가입 된 회원
                return "이메일 회원";
            }
            else // id는 있고, pw는 없음 -> 소셜로만 가입
            return "소셜 회원"; 
        } else {
            return "미가입 회원";
        }
    }

}