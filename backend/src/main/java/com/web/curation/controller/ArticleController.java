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
import com.web.curation.model.user.User;

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
    public Object getArticleList(@RequestParam(value = "page") final int page, @RequestHeader final HttpHeaders header)
            throws Exception {
        final BasicResponse result = new BasicResponse();
        result.status = false;
        result.data = "글 조회 실패";

        String email = "";
        try {
            String id_token  = header.get("id_token").get(0);
            email = JWTDecoding.decode(id_token);
        } catch (Exception e) {
            
        }
        
        Page<Article> articles ;
        if(email == null){
            articles = articleDao.findByIspublic(PageRequest.of(page, 10, Sort.Direction.DESC,"articleid"),1);
        } else {

            articles = articleDao.findByIspublicOrEmail(PageRequest.of(page, 10, Sort.Direction.DESC,"articleid"),1,email);
        }

        //  keywords list - "keyword": [ ["Swift","Swagger"], ["C"], ["STS", "SQL"], ...]
        if(articles==null){
            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        List<List<String>> keywordsList = new ArrayList<>();
        List<Integer> likesList = new ArrayList<>();
        List<Integer> pinList = new ArrayList<>();
        List<Integer> articleCount = new ArrayList<>();
        List<Integer> commentCntList = new ArrayList<>();

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
           articleCount.add(articleDao.countByEmail(a.getEmail()));
           commentCntList.add(commentDao.countByArticleid(a.getArticleid()));

        }

        Map<String,Object> object = new HashMap<>();
        object.put("article", articles);
        object.put("keyword", keywordsList);
        object.put("likesCntList", likesList);
        object.put("pinCntList", pinList);
        object.put("articleCount", articleCount);
        object.put("commentCntList", commentCntList);

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
        String title = (String) request.get("title");
        String content = (String) request.get("content");
        String preview = subStrByte((String) request.get("preview"), 200);
        int ispublic = Integer.parseInt((String) request.get("ispublic"));
        
        // 글에서 이미지 뽑는 함수
        String imgurl = getImgFromArticle(content);
        
        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setEmail(email);
        article.setNickname(nickname);
        article.setImgurl(imgurl);
        article.setPreview(preview);
        article.setIspublic(ispublic);
        
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

        // 글쓴이 총 글 개수 프런트에 전달
        int articleCount = articleDao.countByEmail(email);

        Map<String, Integer> object = new HashMap<>();
        object.put("articleId", articleId);
        object.put("articleCount", articleCount);

        result.status = true;
        result.data = "글쓰기 성공";
        result.object = object;
        
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    
    // 글에서 이미지 뽑는 함수
    private String getImgFromArticle(String content) {
        char[] con = content.toCharArray();
        int if_img1=0;
        boolean if_img=false;
        boolean if_url=false;
        String url = "";
        for(int i=0;i<content.length();i++){
            if(!if_img && con[i]=='!') {
                if_img1+=1;
            }
            else if(!if_img && con[i]=='[' && if_img1==1){
                if_img=true;
            }
            else if(!if_img && if_img1==1 && con[i]!='['){
                if_img1=0;
                url="";
            }
            else if(if_img && con[i]=='('){
                if_url=true;
            }
            else if(con[i]==')' && if_img){
                return url;
            }
            else if(if_url){
                url+=con[i];
            }
        }

        return null;
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
    public Object deleteArticle (@RequestHeader (required = true) final HttpHeaders header, @RequestParam(required = true) final int no)
            throws Exception {
        
        final BasicResponse result = new BasicResponse();
        String email = JWTDecoding.decode(header.get("id_token").get(0));

        if(articleDao.deleteByArticleid(no) > 0){
            int articleCount = articleDao.countByEmail(email);
            result.status = true;
            result.data = "삭제 성공";
            result.object = articleCount;
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
        article.setImgurl(getImgFromArticle((String)request.get("content")));
        article.setUpdatedat(LocalDateTime.now());
        article.setPreview(subStrByte((String) request.get("preview"), 200));
        article.setIspublic(Integer.parseInt((String)request.get("ispublic")));


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
    public Object getArticle(@PathVariable final int no, @RequestHeader final HttpHeaders header) throws Exception {

        
        final BasicResponse result = new BasicResponse();
        result.status = false;
        result.data = "상세 조회 실패";

        
        String email = "";
        
        try {
            String id_token = header.get("id_token").get(0);
            email = JWTDecoding.decode(id_token);
            
        } catch (Exception e) {
        }
        
        Article article = articleDao.findByArticleid(no);

        if(!article.getEmail().equals(email) && article.getIspublic() == 3) { // 남의 글인데 비공개면
            result.data = "비공개 글";
            return new ResponseEntity<>(result, HttpStatus.OK);
        }

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
        
        List<Integer> commentArticleCountList = new ArrayList<>();
        List<String> commentNickname = new ArrayList<>();
        for(Comment c : commentList){
            commentNickname.add(userDao.findUserByEmail(c.getEmail()).get().getNickname());
            commentArticleCountList.add(articleDao.countByEmail(c.getEmail()));
        }
        
        result.status = true;
        result.data = "상세 조회 성공";
        
        User user = userDao.findUserByEmail(article.getEmail()).get();
        
        Map<String,Object> object = new HashMap<>();
        object.put("article", article);
        object.put("keyword", keywordList);
        object.put("articleCount", articleDao.countByEmail(user.getEmail()));
        object.put("cntLikes", cntLikes);
        object.put("cntPin", cntPin);
        object.put("commentList", commentList);
        object.put("userinfo", user.getInfo());
        object.put("commentNickname",commentNickname);
        object.put("commentArticleCount", commentArticleCountList);
        object.put("ispublic", article.getIspublic());

        result.object = object;
        

        return new ResponseEntity<>(result, HttpStatus.OK);
        
    }
    
}
