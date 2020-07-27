package com.web.curation.social.security;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class OAuth2Controller {


    @GetMapping("/googleLogInCallback")
    public Object doSessionAssignActionPage(HttpServletRequest request) throws Exception {


        String code = request.getParameter("code");
        System.out.println(code);

        // RestTemplate을 사용하여 Access Token 및 profile을 요청한다.
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("code", code);
        parameters.add("client_id", "634062607964-elrm78as5396cdodbtf1p6mp6nd0dib4.apps.googleusercontent.com");
        parameters.add("client_secret", "nL761NfTtQUWOQeMsDXLtwUS");
        parameters.add("redirect_uri", "http://localhost:8080/googleLogInCallback");
        parameters.add("grant_type", "authorization_code");

        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(
            parameters, headers);
            System.out.println(requestEntity.getHeaders().toString());
            ResponseEntity<Map> responseEntity = restTemplate.exchange("https://www.googleapis.com/oauth2/v4/token",
            HttpMethod.POST, requestEntity, Map.class);
        Map<String, Object> responseMap = responseEntity.getBody();

        // id_token 라는 키에 사용자가 정보가 존재한다.
        // 받아온 결과는 JWT (Json Web Token) 형식으로 받아온다. 콤마 단위로 끊어서 첫 번째는 현 토큰에 대한 메타 정보, 두
        // 번째는 우리가 필요한 내용이 존재한다.
        // 세번째 부분에는 위변조를 방지하기 위한 특정 알고리즘으로 암호화되어 사이닝에 사용한다.
        // Base 64로 인코딩 되어 있으므로 디코딩한다.

        String[] tokens = ((String) responseMap.get("id_token")).split("\\.");
        Base64 base64 = new Base64(true);
        String body = new String(base64.decode(tokens[1]));
        
        System.out.println("token length : " + tokens.length);
        System.out.println("token[0] : " + new String(Base64.decodeBase64(tokens[0]), "utf-8"));
        System.out.println("token[1] : " + new String(Base64.decodeBase64(tokens[1]), "utf-8"));

        // Jackson을 사용한 JSON을 자바 Map 형식으로 변환
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> result = mapper.readValue(body, Map.class);
        System.out.println("result : "  + result.get("name"));


        Map<String,Object> object = isJoined(result.get("email"));

        return new ResponseEntity<>(tokens, HttpStatus.OK); // 아직 연결된 뷰가 없음
    }

    private Map<String, Object> isJoined(String string) {
        return null;
    }

    // @GetMapping({ "", "/" })
    // public String getAuthorizationMessage() {
    // return "home";
    // }

    // @GetMapping("/account/login")
    // public String login() {
    // return "login";
    // }

    // @GetMapping({ "/loginSuccess", "/hello" })
    // public String loginSuccess() {
    // return "hello";
    // }

    // @GetMapping("/loginFailure")
    // public String loginFailure() {
    // return "loginFailure";
    // }

}