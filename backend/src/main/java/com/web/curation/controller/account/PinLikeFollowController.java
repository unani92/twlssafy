package com.web.curation.controller.account;

import java.util.Map;

import javax.transaction.Transactional;

import com.web.curation.dao.pinlikesfollow.FollowDao;
import com.web.curation.dao.pinlikesfollow.LikesDao;
import com.web.curation.dao.pinlikesfollow.PinDao;
import com.web.curation.dao.user.UserDao;
import com.web.curation.model.BasicResponse;
import com.web.curation.model.pinlikesfollow.Follow;
import com.web.curation.model.pinlikesfollow.Likes;
import com.web.curation.model.pinlikesfollow.Pin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
        @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })

@CrossOrigin
@RestController
public class PinLikeFollowController {

    @Autowired
    UserDao userDao;

    @Autowired
    FollowDao followDao;

    @Autowired
    PinDao pinDao;

    @Autowired
    LikesDao likesDao;

    @Transactional
    @PostMapping("/account/follow")
    @ApiOperation(value = "팔로우 / 언팔로우")
    public Object follow (@RequestBody(required = true) final Map<String,Object> request) {

        /*
            팔로우 -> 내 이메일이랑 상대방 이메일이 들어옴
           {
            "email" : "string",
            "follow" : "qwer@qwer.com"
            }
        */
        String email = (String)request.get("email");
        String follow = (String) request.get("follow");

        Follow f = new Follow();
        f.setEmail(email);
        f.setFollowemail(follow);

        final BasicResponse result = new BasicResponse();
        result.status = false;

        if(followDao.findByEmailAndFollowemail(email, follow) ==null) { // 팔로잉 안 했음
            
            if(followDao.save(f)==null){
                result.data = "follow 실패";
                return new ResponseEntity<>(result, HttpStatus.OK);
            }
            result.data = "follow";
        } else{
            if(followDao.deleteByEmailAndFollowemail(email, follow)<=0){
                result.data = "unfollow 실패";
                return new ResponseEntity<>(result, HttpStatus.OK);
            }

            result.data = "unfollow";
        }

        result.status = true;
        
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    @Transactional
    @PostMapping("/article/{article_id}/pin/{email}")
    @ApiOperation(value = "핀")
    public Object pin (@PathVariable(required = true) final String article_id, @PathVariable(required = true) final String email) {
        
        // String email = "";
        int no = Integer.parseInt(article_id);
        Pin pin = new Pin();
        pin.setArticleid(no);
        pin.setEmail(email);
        
        final BasicResponse result = new BasicResponse();
        result.status = false;
        
        if(pinDao.findByEmailAndArticleid(email, no) != null){ // 있으면
            if(pinDao.deleteByEmailAndArticleid(email, no)>0){ // 지우면
                result.data = "pin 취소";
            } else { // 지우는 거 실패
                result.data = "pin 취소 실패";
            }
        } else{
            if(pinDao.save(pin)==null){ // 설정 못하면
                result.data = "pin 설정 실패";
            } else {
                result.data = "pin 설정";
            }
        }
        
        result.status = true;
        
        return new ResponseEntity<>(result, HttpStatus.OK);

    }


    @Transactional
    @PostMapping("/article/{article_id}/likes/{email}")
    @ApiOperation(value = "좋아요")
    public Object likes (@PathVariable(required = true) final String article_id, @PathVariable(required = true) final String email) {
        
        // String email = "";
        int no = Integer.parseInt(article_id);
        Likes likes = new Likes();
        likes.setArticleid(no);
        likes.setEmail(email);
        
        final BasicResponse result = new BasicResponse();
        result.status = false;
        
        if(likesDao.findByEmailAndArticleid(email, no) != null){ // 있으면
            if(likesDao.deleteByEmailAndArticleid(email, no)>0){ // 지우면
                result.data = "likes 취소";
            } else { // 지우는 거 실패
                result.data = "likes 취소 실패";
            }
        } else{
            if(likesDao.save(likes)==null){ // 설정 못하면
                result.data = "likes 설정 실패";
            } else {
                result.data = "likes 설정";
            }
        }
        
        result.status = true;
        
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

}