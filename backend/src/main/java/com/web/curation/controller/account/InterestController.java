package com.web.curation.controller.account;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.web.curation.dao.user.InterestDao;
import com.web.curation.dao.user.SkillsDao;
import com.web.curation.dao.user.UserDao;
import com.web.curation.model.BasicResponse;
import com.web.curation.model.user.Interest;
import com.web.curation.model.user.Skills;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @Autowired
    UserDao userDao;

    @PostMapping("/account/interest/register")
    @ApiOperation(value = "관심사 등록")
    public Object interest(@RequestBody(required = true) final Map<String, Object> request) {

        String email = (String) request.get("email");

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
    @ApiOperation(value = "관심 분야 선택")
    public Object interestDelete (@RequestBody(required = true) final Map<String,Object> request){

        String email  = (String) request.get("email");
        // int no = userDao.findUserByEmail(email).get().getNo();

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

