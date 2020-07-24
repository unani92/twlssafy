package com.web.curation.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import com.web.curation.dao.ArticleDao;
import com.web.curation.dao.KeywordsDao;
import com.web.curation.dao.SkillsDao;
import com.web.curation.dao.pinlikesfollow.LikesDao;
import com.web.curation.dao.pinlikesfollow.PinDao;
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
    
    @Autowired
    LikesDao likesDao;

    @Autowired
    PinDao pinDao;
    
    @ApiOperation(value = "리스트 조회")
    @GetMapping("/article")
    public Object getArticleList(@RequestParam(value = "page") final int page) {
        final BasicResponse result = new BasicResponse();
        result.status = false;
        result.data = "글 조회 실패";
    
        // 게시글 페이징 10개 단위
        Page<Article> articles = articleDao.findAll(PageRequest.of(page, 10, Sort.Direction.DESC,"articleid"));

        //  keywords list - "keyword": [ ["Swift","Swagger"], ["C"], ["STS", "SQL"], ...]
        if(articles==null){
            return new ResponseEntity<>(result, HttpStatus.OK);
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
    
    @ApiOperation(value = "글쓰기")
    @ResponseBody
    @PostMapping("/article")
    public Object writeArticle (@RequestBody(required = true) final Map<String,Object> request){
        
        /*
        {
            "email" : "asdf@asdf.com",
            "nickname" : "asdf",
            "title" : "제목제목",
            "content":"내용내용",
            "imgUrl":"/media/picture.jpg",
            "keyword" : ["C","Nexus","DB2"]
        }
        
        */
        final BasicResponse result = new BasicResponse();
        result.status = false;
        result.data = "글쓰기 실패";
        

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
        
        if(articleDao.save(article)==null){
            result.data = "글쓰기 실패 - DB 저장 실패";
            return new ResponseEntity<>(result,HttpStatus.OK);   
        }
        
        List<String> keywords = (List<String>) request.get("keyword"); 
        
        // keyword 설정 안 하면 글 못씀
        if(keywords == null){
            result.data = "글쓰기 실패 - 키워드 설정 안함";
            articleDao.delete(articleDao.findFirstByEmailOrderByArticleidDesc(email));
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
            int articleId = articleDao.findFirstByEmailOrderByArticleidDesc(email).getArticleid();
            for(String k : keywords){
                Keywords keyword = new Keywords();
                keyword.setArticleid(articleId);
                keyword.setSno(skillsDao.findSkillByName(k).getSno());
                if(keywordsDao.save(keyword) == null){ // keyword 저장 못 하면 글 못씀
                    result.data = "글쓰기 실패 - 키워드 DB 저장 실패";
                    articleDao.delete(articleDao.findFirstByEmailOrderByArticleidDesc(email));
                    return new ResponseEntity<>(result, HttpStatus.OK);
                }

        }

        result.status = true;
        result.data = "글쓰기 성공";
        result.object = articleId;
        
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
        "keyword" : ["C","Python","DB2"]
       }
        */

        int articleId = Integer.parseInt((String)request.get("articleId"));
        
        Article article = articleDao.findByArticleid(articleId);

        article.setContent((String)request.get("content"));
        article.setTitle((String)request.get("title"));
        article.setImgurl((String)request.get("imgUrl"));
        article.setUpdatedat(LocalDateTime.now());

        final BasicResponse result = new BasicResponse();
            result.data = "수정 실패";
            result.status = false;

        if(articleDao.save(article) != null){

            if(keywordsDao.deleteByArticleid(articleId)<0){
                result.data = "수정 실패 - 글 번호 오류";
                return new ResponseEntity<>(result, HttpStatus.OK);
            }
            List<String> keywords = (List<String>) request.get("keyword");
            for(String k : keywords){
                Keywords keyword = new Keywords();
                keyword.setArticleid(articleId);
                keyword.setSno(skillsDao.findSkillByName(k).getSno());
                if(keywordsDao.save(keyword) ==null){
                    result.data = "수정 실패 - 키워드 저장 실패";
                    return new ResponseEntity<>(result, HttpStatus.OK);
                }
            }

            result.status = true;
            result.data = "수정 성공";
        }
        

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ApiOperation(value = "상세 조회")
    @GetMapping("/article/{no}")
    public Object getArticle(@PathVariable final int no) {

        final BasicResponse result = new BasicResponse();
        result.status = false;
        result.data = "상세 조회 실패";
    
        Article article = articleDao.findByArticleid(no);
        List<Keywords> keywords = keywordsDao.findAllByArticleid(no);
        int cntLikes = likesDao.countByArticleid(no);
        int cntPin = pinDao.countByArticleid(no);

        if(article == null || keywords == null){
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        
        List<String> keywordList = new ArrayList<>();
        for(Keywords k : keywords){
            keywordList.add(skillsDao.findSkillBySno(k.getSno()).getName());        
        }


        result.status = true;
        result.data = "상세 조회 성공";

        Map<String,Object> object = new HashMap<>();
        object.put("article", article);
        object.put("keyword", keywordList);
        object.put("cntLikes", cntLikes);
        object.put("cntPin", cntPin);
        result.object = object;
        
        return new ResponseEntity<>(result, HttpStatus.OK);

    }
    
}

