package com.web.curation.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.web.curation.dao.user.InterestDao;
import com.web.curation.JWT.JWTDecoding;
import com.web.curation.dao.KeywordsDao;
import com.web.curation.dao.SkillsDao;
import com.web.curation.model.BasicResponse;
import com.web.curation.model.user.Interest;
import com.web.curation.model.Skills;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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

    @Autowired
    KeywordsDao keywordsDao;

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

            // 이미 추가되어있는 관심사라면 무시
            // interest.sno == skill.sno and interest.email==email
            if(interestDao.findByEmailAndSno(email, skill.getSno()) != null) {
                continue;
            }
            

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

    @ApiOperation(value = "해시태그 조회")
    @GetMapping("/hashTag")
    public Object getHashTagList() {
        // 인기 키워드 10개 
        /*
            // 최근 1달간 인기 키워드
            select sno,count(*) from keyword, article 
            where article.updatedAt > date_add(now(), interval -1 month) 
            and keyword.articleid=article.articleid 
            group by sno 
            order by count(*) desc limit 10;
        */

        
        final BasicResponse result = new BasicResponse();
        result.status = false;
        result.data = "태그 조회 실패";
        
        List<Object[]> hashTagList = keywordsDao.findGroupByKeywordsWithJPQL();
        List<String> hashTag = new ArrayList<>();

        for(int i=0;i<10;i++){
            hashTag.add((skillsDao.findSkillBySno((int) hashTagList.get(i)[0])).getName());
        }

        result.status = true;
        result.data = "success";
        result.object = hashTag;
        
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}

