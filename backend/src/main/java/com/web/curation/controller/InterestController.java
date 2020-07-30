package com.web.curation.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.web.curation.dao.user.InterestDao;
import com.web.curation.JWT.JWTDecoding;
import com.web.curation.dao.SkillsDao;
import com.web.curation.model.BasicResponse;
import com.web.curation.model.user.Interest;
import com.web.curation.model.Skills;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
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
public class InterestController {

    @Autowired
    InterestDao interestDao;

    @Autowired
    SkillsDao skillsDao;

    @PostMapping("/account/interest/register")
    @ApiOperation(value = "관심사 등록")
    public Object interest(@RequestHeader(required = true) final HttpHeaders header, @RequestBody(required = true) final Map<String, Object> request)
            throws Exception {

        String email = JWTDecoding.decode(header.get("id_token").get(0));
        List<Object> list = new ArrayList<>();

        list = (List<Object>) request.get("skill");

        List<Object> test = new ArrayList<>();

        for (Object s : list) {
            Skills skill = new Skills();
            skill = skillsDao.findSkillByName((String) s);
            Interest i = new Interest();
            i.setEmail(email);
            i.setSno(skill.getSno());;
            interestDao.save(i);

            test.add(i);
        }

        final BasicResponse result = new BasicResponse();

        result.status = true;
        result.data = "success";
        result.object = test;
        
        return new ResponseEntity<>(result, HttpStatus.OK);

    }


    @PostMapping("/account/interest/delete")
    @ApiOperation(value = "관심 분야 삭제")
    public Object interestDelete (@RequestHeader(required = true) final HttpHeaders header, @RequestBody(required = true) final Map<String,Object> request)
            throws Exception {

        String email = JWTDecoding.decode(header.get("id_token").get(0));

        Object test = new ArrayList<>();
     
            Skills skill = new Skills();
            skill = skillsDao.findSkillByName((String) request.get("skill"));

            Interest i = new Interest();
            i.setEmail(email);
            i.setSno(skill.getSno());
            interestDao.delete(i);

        ((List<Object>) test).add(i);


        final BasicResponse result = new BasicResponse();

        result.status = true;
        result.data = "success";
        result.object = test;
        
        return new ResponseEntity<>(result, HttpStatus.OK);

    }
}

