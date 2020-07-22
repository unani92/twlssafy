package com.web.curation.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import com.web.curation.dao.ArticleDao;
import com.web.curation.dao.KeywordsDao;
import com.web.curation.dao.user.SkillsDao;
import com.web.curation.model.Article;
import com.web.curation.model.BasicResponse;
import com.web.curation.model.Keywords;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PutMapping;

@Api("Article Controller")
@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
        @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })
@CrossOrigin
@RestController
public class ArticleController {

    @Autowired
    ArticleDao articleDao;

    @Autowired
    SkillsDao skillsDao;

    @Autowired
    KeywordsDao keywordsDao;


    @ApiOperation(value = "리스트 조회")
    @GetMapping("/article")
    public Object getArticleList(@RequestParam(value = "page") final int page) {
    
        Page<Article> articles = articleDao.findAll(PageRequest.of(page, 10, Sort.Direction.DESC,"articleid"));

        List<List<String>> list = new ArrayList<>();
        for(Article a : articles){
            List<Keywords> tmpKeyword = keywordsDao.findAllByArticleid(a.getArticleid()); //
            List<String> tmplist = null;
            tmplist =  new ArrayList<>();
            for(Keywords k : tmpKeyword){
                tmplist.add(skillsDao.findSkillBySno(k.getSno()).getName());
            }
            list.add(tmplist);
        }

        final BasicResponse result = new BasicResponse();

        result.status = true;
        result.data = "success";

        Map<String,Object> object = new HashMap<>();
        object.put("article", articles);
        object.put("keyword", list);

        result.object = object;
        
        return new ResponseEntity<>(result, HttpStatus.OK);

    }
    
    @ApiOperation(value = "글쓰기")
    @ResponseBody
    @PostMapping("/article")
    public Object writeArticle (@RequestBody(required = true) final Map<String,Object> request){

        /*
        {
            "email" : "email@email.com",
            "nickname" : "nickname",
            "title" : "제목제목",
            "content":"내용내용",
            "image_URL":"/media/picture.jpg",
            "keyword" : [{"skill" : "C"},{"skill" : "Ajax"}]
            }

        */


        String email = (String) request.get("email");
        String nickname = (String) request.get("nickname");
        String title = (String) request.get("title");
        String content = (String) request.get("content");
        String imgurl = (String) request.get("imgUrl");
        
        
        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setEmail(email);
        article.setNickname(nickname);
        article.setImgurl(imgurl);
        
        articleDao.save(article);
        
        final BasicResponse result = new BasicResponse();
        
        result.status = true;
        result.data = "success";
        

        if(request.get("keyword")==null)
            return new ResponseEntity<>(result, HttpStatus.OK);


        List<String> keywords = (List<String>) request.get("keyword");
        
        for(String k : keywords){

            Keywords keyword = new Keywords();
            keyword.setArticleid(articleDao.findFirstByEmailOrderByArticleidDesc(email).getArticleid());
            System.out.println(k);
            keyword.setSno(skillsDao.findSkillByName(k).getSno());
            keywordsDao.save(keyword);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Transactional
    @ApiOperation(value = "글삭제")
    @DeleteMapping("/article")
    public Object deleteArticle (@RequestParam(required = true) final int no){
        
        final BasicResponse result = new BasicResponse();
        if(articleDao.deleteByArticleid(no) > 0){
            result.status = true;
            result.data = "삭제 성공";
            
        }
        else {
            result.status = false;
            result.data = "삭제 실패";
            
        }
        
        
        
        return new ResponseEntity<>(result, HttpStatus.OK);
        
    }
    
    @Transactional
    @ApiOperation(value = "글수정")
    @PutMapping(value="/article")
    public Object updateArticle (@RequestBody(required = true) final Map<String,Object> request) {


        /*
        {
        "articleId" : "2",
        "title" : "제목",
        "content":"내용내용",
        "image_URL":"/media/picture.jpg",
        "keyword" : [{"skill" : "Ajax"},{"skill" : "EJS"}]
        }
        */

        int articleId = Integer.parseInt((String)request.get("articleId"));
        
        Article article = articleDao.findByArticleid(articleId);

        article.setContent((String)request.get("content"));
        article.setTitle((String)request.get("title"));
        article.setImgurl((String)request.get("imgUrl"));
        article.setUpdatedat(LocalDateTime.now());

        final BasicResponse result = new BasicResponse();
        if(articleDao.save(article) != null){

            if(keywordsDao.deleteByArticleid(articleId)<0){
                result.data = "수정 실패";
                result.status = false;
                return new ResponseEntity<>(result, HttpStatus.OK);
            }
            List<Map<String,String>> keywords = (List<Map<String, String>>) request.get("keyword");
            for(Map<String,String> k : keywords){
                Keywords keyword = new Keywords();
                keyword.setArticleid(articleId);
                keyword.setSno(skillsDao.findSkillByName(k.get("skill")).getSno());
                if(keywordsDao.save(keyword) ==null){
                    result.data = "수정 실패";
                    result.status = false;
                    return new ResponseEntity<>(result, HttpStatus.OK);
                }
            }

            result.status = true;
            result.data = "수정 성공";
        }
        else {
            result.data = "수정 실패";
            result.status = false;
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ApiOperation(value = "상세 조회")
    @GetMapping("/article/{no}")
    public Object getArticle(@PathVariable final int no) {
    
        Article article = articleDao.findByArticleid(no);

        List<Keywords> keywords = keywordsDao.findAllByArticleid(no);
        
        List<String> keywordList = new ArrayList<>();
        for(Keywords k : keywords){
            keywordList.add(skillsDao.findSkillBySno(k.getSno()).getName());        
        }

        final BasicResponse result = new BasicResponse();

        result.status = true;
        result.data = "success";

        Map<String,Object> object = new HashMap<>();
        object.put("article", article);
        object.put("keyword", keywordList);
        result.object = object;
        
        return new ResponseEntity<>(result, HttpStatus.OK);

    }
    
}

