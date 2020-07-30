package com.web.curation.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import com.web.curation.dao.ArticleDao;
import com.web.curation.dao.KeywordsDao;
import com.web.curation.dao.SkillsDao;
import com.web.curation.dao.pinlikesfollow.FollowDao;
import com.web.curation.dao.pinlikesfollow.LikesDao;
import com.web.curation.dao.pinlikesfollow.PinDao;
import com.web.curation.dao.user.InterestDao;
import com.web.curation.dao.user.UserDao;
import com.web.curation.model.Article;
import com.web.curation.model.BasicResponse;
import com.web.curation.model.Keywords;
import com.web.curation.model.pinlikesfollow.Follow;
import com.web.curation.model.user.Interest;
import com.web.curation.model.user.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Sort;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api("Mypage Controller")
@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
        @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })
@CrossOrigin
@RestController
public class MypageController {

    @Autowired
    UserDao userDao;

    @Autowired
    ArticleDao articleDao;

    @Autowired
    SkillsDao skillsDao;

    @Autowired
    PinDao pinDao;

    @Autowired
    LikesDao likesDao;

    @Autowired
    FollowDao followDao;

    @Autowired
    KeywordsDao keywordsDao;

    @Autowired
    InterestDao interestDao;


    @GetMapping("/account/{nickname}")
    public Object getArticle(@PathVariable String nickname, @RequestParam(value = "page") int page) {
        // 해당 nickname을 가진 사용자 정보 리스트에 담기. (email, nickname, info, follow, article,
        // interest)
        // article은 10개씩 페이징처리 후 전송
        final BasicResponse result = new BasicResponse();
        result.status = false;
        result.data = "마이페이지 조회 실패";
        Optional<User> user = userDao.findUserByNickname(nickname);
        if(user.isPresent()){
            result.status = true;
            result.data = "success";
            Map<String, Object> userInfo = new TreeMap<>();
            userInfo.put("user", user);
            userInfo.put("pinList", pinDao.findAllByEmail(user.get().getEmail()));
            userInfo.put("likesList", likesDao.findAllByEmail(user.get().getEmail()));
            
           // 내가 팔로우 하는 사람 목록
           List<Follow> follow = followDao.findAllByEmail(user.get().getEmail());
           List<String> followNickname = new ArrayList<>();
           Map<String, Object> followList = new TreeMap<>();
           for(Follow fol : follow) {
               Optional<User> folllownickname = userDao.findUserByEmail(fol.getFollowemail());
               followNickname.add(folllownickname.get().getNickname());
               
           }
           followList.put("follow", follow);
           followList.put("followNickname", followNickname);
           userInfo.put("followList", followList);

           // 나를 팔로잉 하는 사람 목록
           List<Follow> follower = followDao.findAllByFollowemail(user.get().getEmail());
           List<String> followerNickname = new ArrayList<>();
           Map<String, Object> followerList = new TreeMap<>();
           for(Follow fol : follower) {
               Optional<User> followernickname = userDao.findUserByEmail(fol.getEmail());
               followerNickname.add(followernickname.get().getNickname());
           }            

           followerList.put("follower", follower);
           followerList.put("followerNickname", followerNickname);
           userInfo.put("followerList", followerList);

            
            // 글 가져오기
            Page<Article> articles = articleDao.findAllByNickname(PageRequest.of(page, 10, Sort.Direction.DESC,"articleid"), nickname);
            
            if(articles==null){
                result.data="글 조회 실패";
                return new ResponseEntity<>(result, HttpStatus.OK);
            }

            List<List<String>> keywordsList = new ArrayList<>();
            for(Article a : articles){
                // 게시글 번호를 이용해 이 글의 키워드 리스트를 받아옴 (ex. 1번글의 키워드 c, c++)
                List<Keywords> tmpKeyword = keywordsDao.findAllByArticleid(a.getArticleid());
                if(tmpKeyword!=null){ // 임시리스트 만들어서 키워드들 넣고, 최종 리스트에 담음
                    List<String> tmplist = new ArrayList<>();
                    for(Keywords k : tmpKeyword){
                        tmplist.add(skillsDao.findSkillBySno(k.getSno()).getName());
                    }
                    keywordsList.add(tmplist);
                }
                else {
                    result.data="keyword 조회 실패";
                    return new ResponseEntity<>(result, HttpStatus.OK); // 글에 keyword 없으면 false return
                }
            }
            userInfo.put("totalArticleCount", articleDao.countByNickname(nickname));
            userInfo.put("article", articles);
            userInfo.put("keyword", keywordsList);

            // 관심사 
            List<Interest> interest = interestDao.findAllByEmail(user.get().getEmail());
            List<Object> interestList = new ArrayList<>();
            
            for(Interest in : interest) {
                interestList.add(skillsDao.findSkillBySno(in.getSno()));
            }

            userInfo.put("interestList", interestList);

            
            result.object = userInfo;
        }
        
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}