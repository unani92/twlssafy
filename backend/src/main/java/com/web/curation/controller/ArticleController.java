package com.web.curation.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.web.curation.dao.ArticleDao;
import com.web.curation.dao.user.InterestDao;
import com.web.curation.dao.user.UserDao;
import com.web.curation.model.Article;
import com.web.curation.model.BasicResponse;
import com.web.curation.model.user.Interest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
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
public class ArticleController {


    @Autowired
    ArticleDao articleDao;

    @ApiOperation(value = "리스트 조회")
	@ResponseBody
    @GetMapping("/article/page={page}")
    public Object getArticleList(@PathVariable int page) {
        // List<Article> list = articleDao.findAll();

        Page<Article> articles = articleDao.findAll(PageRequest.of(page, 10, Sort.Direction.DESC,"articleid"));
        final BasicResponse result = new BasicResponse();

        result.status = true;
        result.data = "success";
        result.object = articles;
        
        return new ResponseEntity<>(result, HttpStatus.OK);

    }
    
    @ApiOperation(value = "글쓰기")
    @ResponseBody
    @PostMapping("/article")
    public Object writeArticle (@RequestBody(required = true) final Map<String,Object> request){

        /*
        {
            "title" : "qwerty@gmail.com",
            "content":"내용내용",
            "image_URL":"/media/picture.jpg",
            keyword : [{"c"},{"java"}]
            }

        */


        String email = (String) request.get("email");
        String nickname = (String) request.get("nickname");
        String title = (String) request.get("title");
        String content = (String) request.get("content");
        String imgurl = (String) request.get("imgUrl");
        

        List<String> keywords = (List<String>) request.get("keyword");

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
        
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}

