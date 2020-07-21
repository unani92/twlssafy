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
    @GetMapping("/article?page={page}")
    public ResponseEntity<Map<String, Object>> getArticleList(@PathVariable int page) {

        List<Article> list = articleDao.findAll();

        
		Map<String, Object> ret = new HashMap<>();
		ret.put("list", list);


		return new ResponseEntity<Map<String, Object>>(ret, HttpStatus.OK);
	}

}

