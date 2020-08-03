package com.web.curation.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.web.curation.JWT.JWTDecoding;
import com.web.curation.dao.pinlikesfollow.NotificationDao;
import com.web.curation.model.BasicResponse;
import com.web.curation.model.pinlikesfollow.Notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api("Notification Controller")
@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
        @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })
@CrossOrigin
@RestController
public class NotificationController {

    @Autowired
    NotificationDao notificationDao;

    @ApiOperation(value = "알림 리스트")
    @GetMapping("/notification")
    public Object notification(@RequestHeader final HttpHeaders header) throws Exception {
        final BasicResponse result = new BasicResponse();
        result.data="fail";
        result.status=false;

        String email = JWTDecoding.decode(header.get("id_token").get(0));

        Map<String, Object> object = new HashMap<>();

        List<Notification> notificationList = notificationDao.findAllIn30Days(email);
        notificationList.addAll(notificationDao.findAllUnread(email));

        if(notificationList!=null){
            object.put("notification", notificationList);
            object.put("notificationCnt",notificationDao.countByEmailAndRead(email));
            result.object = object;
            result.data="success";
            result.status=true;
        }

        return result;
    }
    
    
}