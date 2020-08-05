package com.web.curation.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.web.curation.JWT.JWTDecoding;
import com.web.curation.dao.ArticleDao;
import com.web.curation.dao.KeywordsDao;
import com.web.curation.dao.SkillsDao;
import com.web.curation.dao.pinlikesfollow.LikesDao;
import com.web.curation.dao.pinlikesfollow.PinDao;
import com.web.curation.model.Article;
import com.web.curation.model.BasicResponse;
import com.web.curation.model.Keywords;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api("Article Controller")
@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
        @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })
@CrossOrigin
@RestController
public class ArticleController2 {
    
    @Autowired
    ArticleDao articleDao;

    @Autowired
    SkillsDao skillsDao;

    @Autowired
    KeywordsDao keywordsDao;
    
    @Autowired
    LikesDao likesDao;

    @Autowired
    PinDao pinDao;

    @ApiOperation(value = "팔로잉 글 조회")
    @GetMapping("/article/following")
    public Object getFollowingArticle(@RequestParam(value = "page") final int page, @RequestHeader final HttpHeaders header) throws Exception {

        final BasicResponse result = new BasicResponse();
        result.status = false;
        result.data = "fail";

        String id_token = header.get("id_token").get(0);
        String email = JWTDecoding.decode(id_token);

        List<Article> list = articleDao.articleFromFollowing(email);

        if(list!=null){
            result.status = true;
            result.data = "success";

            int totalArticle = list.size();
            // int totalPage = totalArticle/10;
            // if (totalArticle%10 > 0) 
            //     totalPage += 1;
            
            List<Article> articles = new ArrayList<>();

            for(int i=page*10; i<page*10+10; i++) {
                if(i<totalArticle) {
                    articles.add(list.get(i));
                }
            }

            List<List<String>> keywordsList = new ArrayList<>();
            List<Integer> likesList = new ArrayList<>();
            List<Integer> pinList = new ArrayList<>();
    
            for(Article a : articles){
                // 게시글 번호를 이용해 이 글의 키워드 리스트를 받아옴 (ex. 1번글의 키워드 c, c++)
                List<Keywords> tmpKeyword = keywordsDao.findAllByArticleid(a.getArticleid());
                if(tmpKeyword!=null){ // 임시리스트 만들어서 키워드들 넣고, 최종 리스트에 담음
                    List<String> tmplist = new ArrayList<>();
                    for(Keywords k : tmpKeyword){
                        tmplist.add(skillsDao.findSkillBySno(k.getSno()).getName());
                    }
                    keywordsList.add(tmplist);
                    result.status = true;
                    result.data = "글 조회 성공";
                }
                else return new ResponseEntity<>(result, HttpStatus.OK); // 글에 keyword 없으면 false return
    
                // 이 글의 총 좋아요, 핀 수
               likesList.add(likesDao.countByArticleid(a.getArticleid()));
               pinList.add(pinDao.countByArticleid(a.getArticleid()));
    
            }
            
            Map<String,Object> object = new HashMap<>();
            object.put("article", articles);
            object.put("keyword", keywordsList);
            object.put("likesCntList", likesList);
            object.put("pinCntList", pinList);
    
            result.object = object;
            
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ApiOperation(value = "북마크 글 조회")
    @GetMapping("/article/pin")
    public Object getPinnedArticle(@RequestParam(value = "page") final int page, @RequestHeader final HttpHeaders header) throws Exception {

        final BasicResponse result = new BasicResponse();
        result.status = false;
        result.data = "fail";

        String id_token = header.get("id_token").get(0);
        String email = JWTDecoding.decode(id_token);

        List<Article> list = articleDao.articleFromPin(email);

        if(list!=null){
            result.status = true;
            result.data = "success";

            int totalArticle = list.size();
            // int totalPage = totalArticle/10;
            // if (totalArticle%10 > 0) 
            //     totalPage += 1;
            
            List<Article> articles = new ArrayList<>();

            for(int i=page*10; i<page*10+10; i++) {
                if(i<totalArticle) {
                    articles.add(list.get(i));
                }
            }

            List<List<String>> keywordsList = new ArrayList<>();
            List<Integer> likesList = new ArrayList<>();
            List<Integer> pinList = new ArrayList<>();
    
            for(Article a : articles){
                // 게시글 번호를 이용해 이 글의 키워드 리스트를 받아옴 (ex. 1번글의 키워드 c, c++)
                List<Keywords> tmpKeyword = keywordsDao.findAllByArticleid(a.getArticleid());
                if(tmpKeyword!=null){ // 임시리스트 만들어서 키워드들 넣고, 최종 리스트에 담음
                    List<String> tmplist = new ArrayList<>();
                    for(Keywords k : tmpKeyword){
                        tmplist.add(skillsDao.findSkillBySno(k.getSno()).getName());
                    }
                    keywordsList.add(tmplist);
                    result.status = true;
                    result.data = "글 조회 성공";
                }
                else return new ResponseEntity<>(result, HttpStatus.OK); // 글에 keyword 없으면 false return
    
                // 이 글의 총 좋아요, 핀 수
               likesList.add(likesDao.countByArticleid(a.getArticleid()));
               pinList.add(pinDao.countByArticleid(a.getArticleid()));
    
            }
            
            Map<String,Object> object = new HashMap<>();
            object.put("article", articles);
            object.put("keyword", keywordsList);
            object.put("likesCntList", likesList);
            object.put("pinCntList", pinList);
    
            result.object = object;
        }
            
            return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ApiOperation(value = "인기글 조회")
    @GetMapping("/article/hot")
    public Object getHotArticle(@RequestParam(value = "page") final int page) throws Exception {
        final BasicResponse result = new BasicResponse();
        result.status = false;
        result.data = "글 조회 실패";
    
        // 게시글 페이징 10개 단위
        List<Object[]> list = likesDao.articleFromHot();

        //  keywords list - "keyword": [ ["Swift","Swagger"], ["C"], ["STS", "SQL"], ...]
        if(list==null){
            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        List<Article> articles = new ArrayList<>();

        int totalArticle = list.size();
        for(int i=page*10; i<page*10+10; i++) {
            if(i<totalArticle) {
                articles.add((articleDao.findByArticleid((int) list.get(i)[0])));
            }
        }

        List<List<String>> keywordsList = new ArrayList<>();
        List<Integer> likesList = new ArrayList<>();
        List<Integer> pinList = new ArrayList<>();

        for(Article a : articles){
            // 게시글 번호를 이용해 이 글의 키워드 리스트를 받아옴 (ex. 1번글의 키워드 c, c++)
            List<Keywords> tmpKeyword = keywordsDao.findAllByArticleid(a.getArticleid());
            if(tmpKeyword!=null){ // 임시리스트 만들어서 키워드들 넣고, 최종 리스트에 담음
                List<String> tmplist = new ArrayList<>();
                for(Keywords k : tmpKeyword){
                    tmplist.add(skillsDao.findSkillBySno(k.getSno()).getName());
                }
                keywordsList.add(tmplist);
                result.status = true;
                result.data = "글 조회 성공";
            }
            else return new ResponseEntity<>(result, HttpStatus.OK); // 글에 keyword 없으면 false return

            // 이 글의 총 좋아요, 핀 수
           likesList.add(likesDao.countByArticleid(a.getArticleid()));
           pinList.add(pinDao.countByArticleid(a.getArticleid()));

        }
        
        Map<String,Object> object = new HashMap<>();
        object.put("article", articles);
        object.put("keyword", keywordsList);
        object.put("likesCntList", likesList);
        object.put("pinCntList", pinList);

        result.object = object;
        
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    

    @ApiOperation(value = "게시글 날짜 조회")
    @GetMapping("/article/date/{date}")
    public Object searchDate(@PathVariable final String date,
    @RequestParam (value = "email") final String email,
    @RequestParam(value = "page") final int page) {

        final BasicResponse result = new BasicResponse();
        result.status = false;
        result.data = "fail";

        List<Article> list = articleDao.articleAt(date, email);

        int totalArticle = list.size();
        // int totalPage = totalArticle/10;
        // if (totalArticle%10 > 0) 
        //     totalPage += 1;
        
        List<Article> articles = new ArrayList<>();

        
        
        try {
            if(list.get(0) != null){
                for(int i=page*10; i<page*10+10; i++) {
                    if(i<totalArticle) {
                        articles.add(list.get(i));
                    }
                }
            }
        } catch (Exception e) {
            list = null;
        }


        List<List<String>> keywordsList = new ArrayList<>();
        List<Integer> likesList = new ArrayList<>();
        List<Integer> pinList = new ArrayList<>();

        for(Article a : articles){
            // 게시글 번호를 이용해 이 글의 키워드 리스트를 받아옴 (ex. 1번글의 키워드 c, c++)
            List<Keywords> tmpKeyword = keywordsDao.findAllByArticleid(a.getArticleid());
            if(tmpKeyword!=null){ // 임시리스트 만들어서 키워드들 넣고, 최종 리스트에 담음
                List<String> tmplist = new ArrayList<>();
                for(Keywords k : tmpKeyword){
                    tmplist.add(skillsDao.findSkillBySno(k.getSno()).getName());
                }
                keywordsList.add(tmplist);
                result.status = true;
                result.data = "글 조회 성공";
            }
            else return new ResponseEntity<>(result, HttpStatus.OK); // 글에 keyword 없으면 false return

           likesList.add(likesDao.countByArticleid(a.getArticleid()));
           pinList.add(pinDao.countByArticleid(a.getArticleid()));

        }
        
        Map<String,Object> object = new HashMap<>();
        object.put("article", articles);
        object.put("keyword", keywordsList);
        object.put("likesCntList", likesList);
        object.put("pinCntList", pinList);

        result.object = object;
        
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ApiOperation(value = "추천 게시글")
    @GetMapping("/article/recommend")
    public Object recommend (@RequestHeader final HttpHeaders header) throws Exception {
        final BasicResponse result = new BasicResponse();
        result.status = false;
        result.data = "fail";

        String email = "";
        String id_token = "";

        try{
            id_token = header.get("id_token").get(0);
            if(id_token!=null){
                email = JWTDecoding.decode(id_token);
            }
        }catch(NullPointerException e){

        }

       
        Set<Integer> set = new HashSet<>();
        if(email != null){
            List<Integer> r1 = articleDao.rec1(email);
            List<Integer> r2 = articleDao.rec2(email);
            for(int r : r1) set.add(r);
            for(int r : r2) set.add(r);
        }
        List<Integer> r3 = articleDao.rec3();
        List<Integer> r4 = articleDao.rec4();
        for(int r : r3) set.add(r);
        for(int r : r4) set.add(r);

        List<Article> articles = new ArrayList<>();
        
        int setSize = set.size();
        
        Set<Integer> random = new HashSet<>();
        
        Random ran = new Random();
        while(random.size()<5){
            random.add(ran.nextInt(setSize));
        }
        
        for(int r : random){
            articles.add(articleDao.findByArticleid((int)set.toArray()[r]));
        }
        
        return articles;
    }

}