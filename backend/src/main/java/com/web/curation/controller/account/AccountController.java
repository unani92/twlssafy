package com.web.curation.controller.account;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.TreeMap;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.web.curation.JWT.JWTDecoding;
import com.web.curation.JWT.JwtService;
import com.web.curation.dao.ArticleDao;
import com.web.curation.dao.SkillsDao;
import com.web.curation.dao.pinlikesfollow.FollowDao;
import com.web.curation.dao.pinlikesfollow.LikesDao;
import com.web.curation.dao.pinlikesfollow.NotificationDao;
import com.web.curation.dao.pinlikesfollow.PinDao;
import com.web.curation.dao.user.InterestDao;
import com.web.curation.dao.user.UserDao;
import com.web.curation.model.BasicResponse;
import com.web.curation.model.pinlikesfollow.Follow;
import com.web.curation.model.pinlikesfollow.Notification;
import com.web.curation.model.user.Interest;
import com.web.curation.model.user.SignupRequest;
import com.web.curation.model.user.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
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
public class AccountController {

    @Autowired
    UserDao userDao;

    @Autowired
    ArticleDao articleDao;
    
    @Autowired
    FollowDao followDao;

    @Autowired
    PinDao pinDao;

    @Autowired
    LikesDao likesDao;

    @Autowired
    NotificationDao notificationDao;

    @Autowired
    InterestDao interestDao;

    @Autowired
    SkillsDao skillsDao;
    
    @Autowired
    JwtService jwtService;

    @PostMapping("/account/googleInfo")
    @ApiOperation(value = "유저 정보 전달")
    public Object userInfo(HttpServletRequest request) throws Exception {
        // 핀, 좋아요, 팔로우, 팔로워, 작성 글, interest 전달

        
        String id_token = request.getHeader("id_token");

        String email = JWTDecoding.decode(id_token);
        Map<String, Object> userToken = JWTDecoding.getInfo(id_token);
        System.out.println("USERTOKEN : "+userToken);
        Map<String, Object> userInfo = new TreeMap<>();
        userInfo.put("id_token", id_token);
        userInfo.put("email", email);
        userInfo.put("nickname", userToken.get("nickname"));
        userInfo.put("img", userToken.get("img"));
        userInfo.put("pinList", pinDao.findAllByEmail(email));
        userInfo.put("likesList", likesDao.findAllByEmail(email));
        userInfo.put("notificationCnt", notificationDao.countByEmail(email));
        userInfo.put("articleCount", articleDao.countByEmail(email));
            
        // 내가 팔로우 하는 사람 목록            
        List<Follow> follow = followDao.findAllByEmail(email);
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
        List<Follow> follower = followDao.findAllByFollowemail(email);
        List<String> followerNickname = new ArrayList<>();
        Map<String, Object> followerList = new TreeMap<>();
        for(Follow fol : follower) {
            Optional<User> followernickname = userDao.findUserByEmail(fol.getEmail());
            followerNickname.add(followernickname.get().getNickname());
        }            
        
        followerList.put("follower", follower);
        followerList.put("followerNickname", followerNickname);
        userInfo.put("followerList", followerList);

        List<Notification> notificationList = notificationDao.findAllIn30Days(email);
        notificationList.addAll(notificationDao.findAllUnread(email));
        userInfo.put("notification", notificationList);

        // 관심사 
        List<Interest> interest = interestDao.findAllByEmail(email);
        List<Object> interestList = new ArrayList<>();
        
        for(Interest in : interest) {
            interestList.add(skillsDao.findSkillBySno(in.getSno()));
        }

        userInfo.put("interestList", interestList);

        final BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = "success";
        result.object = userInfo; 
        
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/account/googleSignup")
    @ApiOperation(value = "구글로 가입하기")
    public Object signup(@RequestBody final Map<String, Object> body, @RequestHeader final HttpHeaders header) throws Exception {
        //String email = JWTDecoding.decode(header.get("id_token").get(0));
        // 여기는 구글 토큰 사용함!!

        String id_token = header.get("id_token").get(0);
        
        String email = JWTDecoding.decode(header.get("id_token").get(0));

        String nickname = (String) body.get("nickname");
        String info = (String)body.get("info");
        String github = (String)body.get("hithub");

        User userInfo = new User();
        userInfo.setEmail(email);
        userInfo.setNickname(nickname);
        userInfo.setInfo(info);
        userInfo.setImg(JWTDecoding.getImg(header.get("id_token").get(0)));
        userInfo.setType("google");
        userInfo.setGithub(github);
        userDao.save(userInfo);
    
        id_token = jwtService.create(userInfo);

        Map<String, String> response = new HashMap<>();
        response.put("email", email);
        response.put("nickname", nickname);
        response.put("id_token", id_token);
        
        final BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = "success";
        result.object = response; 
        
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    ////////////////////////////////////////////////
    // 여기까지 google login
    ////////////////////////////////////////////////


    // @PostMapping("/account/login")
    // @ApiOperation(value = "로그인")
    // public Object login(@RequestBody(required = true) final Map<String,Object> request) {

    //     String email = (String)request.get("email");
    //     String password = ((String)request.get("password"));

    //     final Optional<User> userOpt = userDao.findUserByEmailAndPassword(email, password);

    //     ResponseEntity response = null;

    //     final BasicResponse result = new BasicResponse();
    //     if (userOpt.isPresent()) { // id/pw 다 맞을 때
    //         result.status = true;
    //         result.data = "success"; 

    //         // 로그인 한 사람의 팔로우, 좋아요, 핀 정보 배열
    //         Map<String, Object> userInfo = new TreeMap<>();
    //         userInfo.put("email", userOpt);
    //         userInfo.put("pinList", pinDao.findAllByEmail(email));
    //         userInfo.put("likesList", likesDao.findAllByEmail(email));
    //         userInfo.put("notificationCnt", notificationDao.countByEmailAndRead(email));
            
    //         // 내가 팔로우 하는 사람 목록
    //         List<Follow> follow = followDao.findAllByEmail(email);
    //         List<String> followNickname = new ArrayList<>();
    //         Map<String, Object> followList = new TreeMap<>();
    //         for(Follow fol : follow) {
    //             Optional<User> folllownickname = userDao.findUserByEmail(fol.getFollowemail());
    //             followNickname.add(folllownickname.get().getNickname());
                            
    //         }
    //         followList.put("follow", follow);
    //         followList.put("followNickname", followNickname);
    //         userInfo.put("followList", followList);
            
    //         // 나를 팔로잉 하는 사람 목록
    //         List<Follow> follower = followDao.findAllByFollowemail(email);
    //         List<String> followerNickname = new ArrayList<>();
    //         Map<String, Object> followerList = new TreeMap<>();
    //         for(Follow fol : follower) {
    //             Optional<User> followernickname = userDao.findUserByEmail(fol.getEmail());
    //             followerNickname.add(followernickname.get().getNickname());
    //         }            
            
    //         followerList.put("follower", follower);
    //         followerList.put("followerNickname", followerNickname);
    //         userInfo.put("followerList", followerList);
            

    //         List<Notification> notificationList = notificationDao.findAllIn30Days(email);
    //         notificationList.addAll(notificationDao.findAllUnread(email));
    //         userInfo.put("notification", notificationList);
            

    //         result.object = userInfo;

    //     } else { // id/pw 둘 중 하나라도 안 맞으면
    //         Optional<User> emailOpt = userDao.findUserByEmail(email);
    //         result.status = false;            

    //         if(emailOpt.isPresent()) { // pw 가 틀린 경우
    //             result.data = "비밀번호가 일치하지 않습니다.";
    //         } else { // id 자체가 존재하지 않는 경우
    //             result.data = "등록되지 않은 E-mail 입니다.";
    //         }
            
    //     }
    //     response = new ResponseEntity<>(result, HttpStatus.OK); 

    //     return response;
    // }

 
    @PostMapping("/account/signup")
    @ApiOperation(value = "가입하기")
    public Object signup(@Valid @RequestBody final SignupRequest request) {

        String email = request.getEmail();
        String password = ((String)request.getPassword());
        String nickname = request.getNickname();
        String info = request.getInfo();
        String github = request.getGithub();
        
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setNickname(nickname);
        user.setInfo(info);
        user.setGithub(github);
        userDao.save(user);
        final BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = "success";
        result.object = userDao.findUserByEmail(email).get(); 
        
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @PostMapping("/account/checkEmail")
    @ApiOperation(value = "이메일 체크")
    public Object checkEmail(@RequestBody(required = true) final Map<String, Object> request) {

        String email = (String)request.get("email");
        final Optional<User> user = userDao.findUserByEmail(email);

        final BasicResponse result = new BasicResponse();

        if (user.isPresent()) { // user 있으면 => 가입 x

            result.status = false;
            result.data = "이미 가입 된 이메일 입니다";
        } else {

            result.status = true;
            result.data = "사용 가능한 이메일 입니다";

            // 인증 이메일 보내는 작업
            try {
                Map<String, String> response = new HashMap<>();
                response.put("code", sendMail(email));
                result.object = response;
                
            } catch (MessagingException e) {
                e.printStackTrace();
            }

        }

        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    //이메일 설정 함수
    @Autowired
    JavaMailSender javaMailSender;
    
    public String sendMail(final String email) throws MessagingException {
        //인증 번호 랜덤 생성
        StringBuffer ranString = new StringBuffer();
        Random ran = new Random();
        int len = ran.nextInt(5);
        for(int i=0;i<len+10;i++){
            int Index = ran.nextInt(3);
            switch(Index){
                case 0:
                    //소문자 랜덤
                    ranString.append((char) ((int) (ran.nextInt(26)) + 97));
                    break;
                case 1:
                    //대문자 랜덤
                    ranString.append((char) ((int) (ran.nextInt(26)) + 65));
                    break; 
                case 2:
                    //숫자 랜덤
                    ranString.append((ran.nextInt(10)));
                    break;
            }

        }
        
        MimeMessage message = javaMailSender.createMimeMessage();
        message.setSubject("TWL 인증 메일입니다.");
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
        message.setText("안녕하세요. 인증 번호는 "+ranString+"입니다.");
        message.setSentDate(new Date());
        javaMailSender.send(message);

        return ranString.toString();
    }
    



    @PostMapping("/account/checkNickname")
    @ApiOperation(value = "닉네임 중복 체크")
    public Object checkNickname(@RequestBody(required = true) final Map<String, Object> request) {
        final Optional<User> user = userDao.findUserByNickname((String)request.get("nickname"));

        final BasicResponse result = new BasicResponse();

        if(user.isPresent()){ // user 있으면 => 가입 x
            result.status = false;
            result.data = "이미 사용중인 닉네임 입니다";
        } else{
            result.status = true;
            result.data = "사용 가능한 닉네임 입니다";
        }

        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @PostMapping("/account/findPwd")
    @ApiOperation(value = "비밀번호 찾기 - 이메일 입력 후 인증 이메일 발송")
    public Object findPwd(@RequestBody(required = true) final Map<String,Object> email) {

        final Optional<User> user = userDao.findUserByEmail((String)email.get("email"));

        final BasicResponse result = new BasicResponse();

        if(user.isPresent()){ // user 있으면 인증 이메일 보내기
            result.status = true;
            result.data = "가입하신 이메일로 인증번호를 보내드렸습니다";
            
            // 이메일 발송 코드
            try {
                Map<String, String> response = new HashMap<>();
                response.put("code", sendMail((String)email.get("email")));
                result.object = response;
            } catch (MessagingException e) {
                e.printStackTrace();
            }

        } else{
            result.status = false;
            result.data = "등록되지 않은 이메일 입니다";
        }

        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    
    @PostMapping("/account/changePwd")
    @ApiOperation(value = "비밀번호 변경")
    public Object changePwd(@RequestBody(required = true) final Map<String,Object> request){

        final Optional<User> userOpt = userDao.findUserByEmail((String)request.get("email"));

        final BasicResponse result = new BasicResponse();
        
        result.status = true;
        result.data = "비밀번호 변경 성공! 다시 로그인 해주세요";
        
        User user = userOpt.get();
        user.setPassword(((String)request.get("password")));

        userDao.save(user);

        return new ResponseEntity<>(result, HttpStatus.OK);

    }
}
