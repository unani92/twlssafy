package com.web.curation.controller.account;

import java.util.Map;

import javax.transaction.Transactional;

import com.web.curation.dao.pinlikesfollow.FollowDao;
import com.web.curation.dao.user.UserDao;
import com.web.curation.model.BasicResponse;
import com.web.curation.model.pinlikesfollow.Follow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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

    @Transactional
    @PostMapping("/account/follow")
    @ApiOperation(value = "팔로우 / 언팔로우")
    public Object follow (@RequestBody(required = true) final Map<String,Object> request) {

        /*
            팔로우 -> 내 이메일이랑 상대방 이메일이 들어옴
            "email"
            "follow"
        */
        String email = (String)request.get("email");
        String follow = (String) request.get("follow");

        Follow f = new Follow();
        f.setEmail(email);
        f.setFollowemail(follow);

        final BasicResponse result = new BasicResponse();
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
}