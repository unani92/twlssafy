package com.web.curation.controller.account;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.web.curation.JWT.JWTDecoding;
import com.web.curation.JWT.JwtService;
import com.web.curation.dao.user.UserDao;
import com.web.curation.model.BasicResponse;
import com.web.curation.model.user.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@CrossOrigin
@Controller
public class GoogleLoginController {

    @Autowired
    UserDao userDao;
    @Autowired
    JwtService jwtService;

    @PostMapping("/googlelogin")
    public Object googleLogin(@RequestHeader final HttpHeaders header) throws Exception {

        String id_token = header.get("id_token").get(0);
        // System.out.println(jwtService.decodeJwt(id_token));

        // id_token 해독해서 이메일 토큰으로 create
        String email = JWTDecoding.decode(id_token);
        Optional<User> user = userDao.findUserByEmail(email);
        if(user.isPresent()) {
            User newUser = new User();
            newUser.setEmail(user.get().getEmail());
            newUser.setNickname(user.get().getNickname());
            newUser.setInfo(user.get().getInfo());
            newUser.setImg(user.get().getImg());
            newUser.setType(user.get().getType());
            newUser.setCreatedate(user.get().getCreatedate());
            id_token = jwtService.create(newUser);
        }

        // String[] tokens = (id_token.split("\\."));
        // Base64 base64 = new Base64(true);
        // String body = new String(base64.decode(tokens[1]));

        // System.out.println("token length : " + tokens.length);
        // System.out.println("token[0] : " + new String(Base64.decodeBase64(tokens[0]), "utf-8"));
        // System.out.println("token[1] : " + new String(Base64.decodeBase64(tokens[1]), "utf-8"));

        // ObjectMapper mapper = new ObjectMapper();
        // Map<String, String> result = mapper.readValue(body, Map.class);
        // System.out.println("result : " + result.get("email"));

        // 가입여부 판단
        Map<String, Object> object = new HashMap<>();
        object.put("isJoined", isJoined(JWTDecoding.decode(id_token)));
        object.put("id_token" , id_token);

        final BasicResponse res = new BasicResponse();
        // System.out.println("is joined : "+isJoined(result.get("email")));
        // System.out.println("TOKEN : "+id_token);

        Map<String, String> response = new HashMap<>();
        response.put("email", JWTDecoding.decode(id_token));

        // 가입된 회원이면 -> success 회원 정보 토큰 반환
        if(isJoined(JWTDecoding.decode(id_token))) {
            res.object = object;
            res.data = "success";
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
        Optional<User> user = userDao.findUserByEmail(email);

        if (user.isPresent()) {
           return true; 
        } else {
            return false;
        }
    }

}


