package com.web.curation.JWT;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;

import com.web.curation.dao.pinlikesfollow.FollowDao;
import com.web.curation.dao.pinlikesfollow.LikesDao;
import com.web.curation.dao.pinlikesfollow.NotificationDao;
import com.web.curation.dao.pinlikesfollow.PinDao;
import com.web.curation.dao.user.UserDao;
import com.web.curation.model.BasicResponse;
import com.web.curation.model.pinlikesfollow.Follow;
import com.web.curation.model.pinlikesfollow.Notification;
import com.web.curation.model.user.User;

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
public class JwtController {

    @Autowired
    UserService userService;
    @Autowired
    JwtService jwtService;
    @Autowired
    UserDao userDao;
    @Autowired
    PinDao pinDao;
    @Autowired
    FollowDao followDao;
    @Autowired
    LikesDao likesDao;
    @Autowired
    NotificationDao notificationDao;


    @PostMapping("/account/login")
    @ApiOperation(value = "로그인")
    public Object login (@RequestBody final Map<String, Object> body, HttpServletResponse res){

        final BasicResponse result = new BasicResponse();
        
        result.data = "failed";
        result.status = true;

        Map<String,Object> object = new HashMap<>();

            Optional<User> loginUser = userDao.
            findUserByEmailAndPassword(
                (String) body.get("email"), 
                (String) body.get("password"));
            
            if(loginUser.isPresent()){ // 있으면,,
                String token = jwtService.create(loginUser.get());
                System.out.println(token);

                System.out.println(jwtService.decodeJwt(token));

                // res.setHeader("id_token",token); 
                // // 왜 header에 안 담겨가!!!

                result.data = "success";

                // String email = loginUser.get().getEmail();

                object.put("id_token", token);
                object.put("isJoined", true);
            }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
}