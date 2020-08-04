package com.web.curation.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import com.web.curation.JWT.JWTDecoding;
import com.web.curation.dao.ArticleDao;
import com.web.curation.dao.CommentDao;
import com.web.curation.dao.KeywordsDao;
import com.web.curation.dao.SkillsDao;
import com.web.curation.dao.pinlikesfollow.LikesDao;
import com.web.curation.dao.pinlikesfollow.PinDao;
import com.web.curation.dao.user.UserDao;
import com.web.curation.model.Article;
import com.web.curation.model.BasicResponse;
import com.web.curation.model.Comment;
import com.web.curation.model.Keywords;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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

    @Autowired
    CommentDao commentDao;

    @Autowired
    UserDao userDao;
    
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
    public Object writeArticle (@RequestHeader (required = true) final HttpHeaders header , @RequestBody(required = true) final Map<String,Object> request)
            throws Exception {
        
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

        Map<String,Object> userToken = JWTDecoding.getInfo(header.get("id_token").get(0));
        
        String email = JWTDecoding.decode(header.get("id_token").get(0));

        
        String nickname = (String) userToken.get("nickname");
        // String nickname = (String) request.get("nickname");
        String title = (String) request.get("title");
        String content = (String) request.get("content");
        String imgurl = (String) request.get("imgUrl");
        String preview = subStrByte((String) request.get("preview"), 200);
        
        
        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setEmail(email);
        article.setNickname(nickname);
        article.setImgurl(imgurl);
        article.setPreview(preview);
        
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
    
    // preview 50자 제한
    private String subStrByte(String str, int len) {
        if(!str.isEmpty()){
            str = str.trim();
            if(str.getBytes().length <= len) {
                return str;
            }
            else {
                StringBuffer sbf = new StringBuffer(len);
                int cnt = 0;
                for(char ch:str.toCharArray()){
                    cnt+=String.valueOf(ch).getBytes().length;
                    if(cnt>len) break;
                    sbf.append(ch);
                }
                return sbf.toString()+"...";
            }
        }
        return "";
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
        article.setPreview(subStrByte((String) request.get("preview"), 200));

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

        // 해당 글의 댓글 list 저장
        List<Comment> commentList = commentDao.findAllByArticleidOrderByCommentidDesc(no);


        result.status = true;
        result.data = "상세 조회 성공";

        Map<String,Object> object = new HashMap<>();
        object.put("article", article);
        object.put("keyword", keywordList);
        object.put("cntLikes", cntLikes);
        object.put("cntPin", cntPin);
        object.put("commentList", commentList);
        object.put("userinfo", userDao.findUserByEmail(article.getEmail()).get() );
        result.object = object;
        
        return new ResponseEntity<>(result, HttpStatus.OK);

    }


    
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
    public Object searchDate(@PathVariable final String date,@RequestParam (value = "email") final String email) {

        final BasicResponse result = new BasicResponse();
        result.status = false;
        result.data = "fail";

        String[] now = LocalDateTime.now().toString().split("T");

        List<Article> list = articleDao.articleAt(date, email);
        
        try {
            if(date.trim().contains(now[0].trim())){
                list = articleDao.findAllByEmailOrderByArticleidDesc(email);
            }
            else if(list.get(0) != null){
            }
        } catch (Exception e) {
            list = articleDao.findAllByEmailOrderByArticleidDesc(email);
        }


        List<List<String>> keywordsList = new ArrayList<>();
        List<Integer> likesList = new ArrayList<>();
        List<Integer> pinList = new ArrayList<>();

        for(Article a : list){
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
        object.put("article", list);
        object.put("keyword", keywordsList);
        object.put("likesCntList", likesList);
        object.put("pinCntList", pinList);

        result.object = object;
        
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}

