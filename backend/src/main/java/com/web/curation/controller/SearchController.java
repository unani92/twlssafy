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
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import com.web.curation.dao.ArticleDao;
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
    @PostMapping("/article/search={search}")
    public Object searchArticle(@PathVariable String search) {
        final BasicResponse result = new BasicResponse();
        result.status = false;
        result.data = "글 검색 실패";

        // 글 검색
        //Specifications<Article> article = Specifications.where(Search.searchByTitle(title));
       // Page<Article> p = searchDao.findAll(article, page);
       List<Article> articles = searchDao.findAll(Search.searchByTitle(search));
       
       if(!articles.isEmpty()){
           result.status = true;
           result.data = "success";
           result.object = articles;
       }

       
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
}