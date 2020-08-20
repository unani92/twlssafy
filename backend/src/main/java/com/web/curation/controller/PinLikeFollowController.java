package com.web.curation.controller;

import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import com.web.curation.JWT.JWTDecoding;
import com.web.curation.dao.ArticleDao;
import com.web.curation.dao.pinlikesfollow.FollowDao;
import com.web.curation.dao.pinlikesfollow.LikesDao;
import com.web.curation.dao.pinlikesfollow.NotificationDao;
import com.web.curation.dao.pinlikesfollow.PinDao;
import com.web.curation.dao.user.UserDao;
import com.web.curation.model.Article;
import com.web.curation.model.BasicResponse;
import com.web.curation.model.pinlikesfollow.Follow;
import com.web.curation.model.pinlikesfollow.Likes;
import com.web.curation.model.pinlikesfollow.Notification;
import com.web.curation.model.pinlikesfollow.Pin;
import com.web.curation.model.user.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
    FollowDao followDao;

    @Autowired
    PinDao pinDao;

    @Autowired
    LikesDao likesDao;

    @Autowired
    NotificationDao notificationDao;

    @Autowired
    ArticleDao articleDao;

    @Autowired
    UserDao userDao;

    @Transactional
    @PostMapping("/account/follow")
    @ApiOperation(value = "팔로우 / 언팔로우")
    public Object follow(@RequestHeader(required = true) final HttpHeaders header, @RequestBody(required = true) final Map<String, Object> request)
            throws Exception {

        /*
         * 팔로우 -> 내 이메일이랑 상대방 이메일이 들어옴 
          { 
          "email" : "string", 
          "follow" : "qwer@qwer.com"
          }
         */

        String email = JWTDecoding.decode(header.get("id_token").get(0));
        
        String follow = (String) request.get("follow");

        Follow f = new Follow();
        f.setEmail(email);
        f.setFollowemail(follow);

        final BasicResponse result = new BasicResponse();
        result.status = false;

        if (followDao.findByEmailAndFollowemail(email, follow) == null) { // 팔로잉 안 했음

            if (followDao.save(f) == null) {
                result.data = "follow 실패";
                return new ResponseEntity<>(result, HttpStatus.OK);
            }

            if (!follow.equals(email)) { // 팔로우하는 사람이 내가 아닐 때

                String other = email; // 날 팔로우하는 사람
                String type = "follow";

                Notification notification = new Notification();

                notification.setEmail(follow);
                notification.setNickname(userDao.findUserByEmail(follow).get().getNickname());
                notification.setOther(other);
                notification.setOthernickname(userDao.findUserByEmail(other).get().getNickname());
                notification.setType(type);
                notification.setReadn(0);
                notification.setArticleid(1);
                notification.setContent("");
                
                Notification noti = notificationDao.findNotification(follow,email,type,1,"",0);
                // "select * from notification where email = ?1 and other = ?2 and type = ?3 and articleid = ?4 and content = ?5"
                if(noti !=null){ 
                    if(noti.getReady() != 1) // 이미 이 사람이 날 팔로우한다는 알림이 있고, 내가 그걸 안 읽었으면 이전 알림은 지우고 다시 보내줌
                        notificationDao.delete(notification);
                }

                if (notificationDao.save(notification) != null) {
                    // 알림 저장 하면
                    int cnt = (int) notificationDao.countByEmail(follow);
                    Map<String, Object> object = new HashMap<>();
                    object.put("notification", notification);
                    object.put("notificationCnt", cnt);
                    result.object = object;
                }
            }

            result.data = "follow";

        } else { // 언팔하기
            if (followDao.deleteByEmailAndFollowemail(email, follow) <= 0) {
                result.data = "unfollow 실패";
                return new ResponseEntity<>(result, HttpStatus.OK);
            }

                Notification noti = notificationDao.findNotification(follow,email,"follow",1,"",0);
                // "select * from notification where email = ?1 and other = ?2 and type = ?3 and articleid = ?4 and content = ?5"
                if( noti !=null){ 
                    if(noti.getReady() != 1)
                    // 예전에 저 사람이 날 팔로우 했다는 알람을 안 읽었으면 지움
                    notificationDao.delete(notificationDao.findNotification(follow,email,"follow",1,"",0));
                }


            result.data = "unfollow";
        }

        result.status = true;

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/article/{article_id}/pin")
    @ApiOperation(value = "핀")
    public Object pin(@RequestHeader(required = true) final HttpHeaders header, @PathVariable(required = true) final String article_id)
            throws Exception {

       
        String email = JWTDecoding.decode(header.get("id_token").get(0));

        
        int no = Integer.parseInt(article_id);
        Pin pin = new Pin();
        pin.setArticleid(no);
        pin.setEmail(email);

        final BasicResponse result = new BasicResponse();
        result.status = false;

        if (pinDao.findByEmailAndArticleid(email, no) != null) { // 핀 이미 설정 돼있으면
            if (pinDao.deleteByEmailAndArticleid(email, no) > 0) { // 핀 지우기
                notificationDao.deleteByOtherAndArticleidAndTypeAndReadn(email, no, "pin",0); // 알림 안 읽었으면 지우기
                result.data = "pin 취소";

                User user = userDao.getUserByEmail(articleDao.findByArticleid(no).getEmail());
                user.setScore(user.getScore()-1);
                userDao.save(user);
            } else { // 지우는 거 실패
                result.data = "pin 취소 실패";
            }
        } else {
            if (pinDao.save(pin) == null) { // 필 설정 실패시
                result.data = "pin 설정 실패";
            } else { // 핀 설정
                Article article = articleDao.findByArticleid(Integer.parseInt(article_id));
                if (!article.getEmail().equals(email)) {
                    result.object = saveNotification(article, email, "pin"); // 알림 설정

                    User user = userDao.getUserByEmail(articleDao.findByArticleid(no).getEmail());
                    user.setScore(user.getScore()+1);
                    userDao.save(user);
                }
                result.data = "pin 설정";
            }
        }
        result.status = true;

        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @Transactional
    @PostMapping("/article/{article_id}/likes")
    @ApiOperation(value = "좋아요")
    public Object likes(@RequestHeader (required = true) final HttpHeaders header, @PathVariable(required = true) final String article_id)
            throws Exception {

        String email = JWTDecoding.decode(header.get("id_token").get(0));

        int no = Integer.parseInt(article_id);
        Likes likes = new Likes();
        likes.setArticleid(no);
        likes.setEmail(email);

        final BasicResponse result = new BasicResponse();
        result.status = false;

        if (likesDao.findByEmailAndArticleid(email, no) != null) { // 좋아요 있으면
            // String email2 = articleDao.findByArticleid(no).getEmail();
            if (likesDao.deleteByEmailAndArticleid(email, no) > 0) { // 좋아요 지우기
                notificationDao.deleteByOtherAndArticleidAndTypeAndReadn(email, no, "like",0); // 좋아요 알림도 안 읽었으면 지우기 -> 읽었어도 지워야하나?
                result.data = "like 취소";

                User user = userDao.getUserByEmail(articleDao.findByArticleid(no).getEmail());
                user.setScore(user.getScore()-1);
                userDao.save(user);
            } else { // 지우는 거 실패
                result.data = "like 취소 실패";
            }
        } else {
            if (likesDao.save(likes) == null) { // 좋아요 설정 못하면
                result.data = "like 설정 실패";
            } else { // 좋아요 설정
                Article article = articleDao.findByArticleid(Integer.parseInt(article_id)); 
                if (!article.getEmail().equals(email)) {
                    result.object = saveNotification(article, email, "like"); // 알림 설정
                        
                    User user = userDao.getUserByEmail(articleDao.findByArticleid(no).getEmail());
                    user.setScore(user.getScore()+1);
                    userDao.save(user);
                }
                result.data = "like 설정";
            }
        }

        result.status = true;

        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    Map<String, Object> saveNotification (Article article, String other, String type) { // 알림 설정 함수

        String content = article.getTitle();

    
        if(notificationDao.findNotification(article.getEmail(), other, type, article.getArticleid(), content, 0) != null){
            notificationDao.delete(notificationDao.findNotification(article.getEmail(), other, type, article.getArticleid(), content, 0));
        }
        else if(notificationDao.findNotification(article.getEmail(), other, type, article.getArticleid(), content, 1) != null){
            notificationDao.delete(notificationDao.findNotification(article.getEmail(), other, type, article.getArticleid(), content, 1));
        }

        Notification notification = new Notification();
        notification.setContent(content);
        notification.setEmail(article.getEmail());
        notification.setNickname(userDao.findUserByEmail(article.getEmail()).get().getNickname());
        notification.setOther(other);
        notification.setOthernickname(userDao.findUserByEmail(other).get().getNickname());
        notification.setType(type);
        notification.setReadn(0);
        notification.setArticleid(article.getArticleid());

        if (notificationDao.save(notification) != null) {
            int cnt = (int) notificationDao.countByEmail(article.getEmail());
            Map<String, Object> object = new HashMap<>();
            object.put("notification", notification);
            object.put("cnt", cnt);

            return object;
        } else
            return null;
    }
}