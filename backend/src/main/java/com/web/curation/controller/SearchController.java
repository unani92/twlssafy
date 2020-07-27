package com.web.curation.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

import com.web.curation.dao.SearchDao;
import com.web.curation.model.Article;
import com.web.curation.model.BasicResponse;
import com.web.curation.model.Search;

@Api("Search Controller")
@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
        @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })
@CrossOrigin
@RestController
public class SearchController {

    @Autowired
    SearchDao searchDao;


    @ApiOperation(value = "글 검색")
    @PostMapping("/article/search={search}/page={page}")
    public Object searchArticle(@PathVariable final String search, @PathVariable final int page) {
        final BasicResponse result = new BasicResponse();
        result.status = false;
        result.data = "글 검색 실패";

        // 글 검색
        Specification<Article> s = Search.searchByTitle(search).or(Search.searchByWriter(search))
            .or(Search.searchByContent(search));
        Page<Article> articles = searchDao.findAll(s, PageRequest.of(page, 10, Sort.Direction.DESC,"articleid"));
       
       if(!articles.isEmpty()){
           result.status = true;
           result.data = "success";
           result.object = articles;
       }

       
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
}