package com.web.curation.controller.account;

import java.util.Map;

import com.web.curation.JWT.JWTDecoding;
import com.web.curation.dao.user.UserDao;
import com.web.curation.model.BasicResponse;
import com.web.curation.model.user.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
        @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })

@CrossOrigin
@RestController
public class AccountController2 {

    @Autowired
    UserDao userDao;

    @Transactional
    @DeleteMapping("/account")
    @ApiOperation(value = "회원 탈퇴")
    public Object deleteAccount(@RequestHeader(required = true) final HttpHeaders headers) throws Exception {

        String id_token = headers.get("id_token").get(0);
        String email = JWTDecoding.decode(id_token);
        
        final BasicResponse result = new BasicResponse();
        result.status = false;
        result.data = "fail";
        
        
        if(userDao.deleteByEmail(email)>0){
            result.status = true;
            result.data = "success";
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
        }
    }
    
    @Transactional
    @PutMapping("/account/intro")
    @ApiOperation(value = "회원 소개 수정")
    public Object modifyIntro (@RequestHeader(required = true) final HttpHeaders headers, @RequestBody final Map<String, Object> body) throws Exception {
        final BasicResponse result = new BasicResponse();
        result.status = false;
        result.data = "fail";

        String id_token = headers.get("id_token").get(0);
        String email = JWTDecoding.decode(id_token);
        
        User user = userDao.findUserByEmail(email).get();

        String modified = (String) body.get("intro");
            user.setInfo(modified);
        
        if(userDao.save(user)==null) // 저장 실패
            return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
        
        result.status = true;
        result.data = "success";

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    @PutMapping("/account/git")
    @ApiOperation(value = "회원 GitHub 수정")
    public Object modifyGit (@RequestHeader(required = true) final HttpHeaders headers, @RequestBody final Map<String, Object> body)
            throws Exception {
                final BasicResponse result = new BasicResponse();
                result.status = false;
                result.data = "fail";
        
                
                String id_token = headers.get("id_token").get(0);
                String email = JWTDecoding.decode(id_token);
        
                User user = userDao.findUserByEmail(email).get();
        
                String modified = (String) body.get("github");
                    user.setGithub(modified);
                
                if(userDao.save(user)==null) // 저장 실패
                    return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
                
                result.status = true;
                result.data = "success";
        
                return new ResponseEntity<>(result, HttpStatus.OK);
    }

}