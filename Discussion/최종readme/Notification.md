
### Notification

#### 1 DAO
```java
   // 새로운 알림 생성
   Notification save(NotificationMultiId notification);
   // 총 알림 개수 리턴
   @Query(value = "SELECT count(readn) FROM notification where ready = 0 and email = ?1", nativeQuery = true)
   int countByEmail(String follow);
   // 최근 30일간의 알림 리턴
   @Query(value = 
   "select * from notification where email = ?1 and date(createtime) > date(subdate(now(), INTERVAL 2 DAY)) order by notificationid desc" 
   , nativeQuery = true)
   List<Notification> findAllIn30Days(String email);
   // 안읽은 알림만 리턴
   @Query(value = 
   "select * from notification where email = ?1 and date(createtime) <= date(subdate(now(), INTERVAL 30 DAY)) and ready = 0 order by notificationid desc" 
   , nativeQuery = true)
   List<Notification> findAllUnread(String email);
   // 알림 삭제
   @Transactional
   int deleteByNotificationid(int notificationid);
   // id로 알림 찾기
   Notification findNotificationByNotificationid(int notificationid);
   // 모든 조건이 일치하는 알림 찾기 (댓글용)
   @Query(value = 
   "select * from notification where email = ?1 and other = ?2 and type = ?3 and articleid = ?4 and content = ?5 and readn = ?6"
   , nativeQuery = true)
   Notification findNotification(String email, String other, String type, int articleid, String content, int readn);
   // 조건이 일치하는 알림 삭제 (댓글용)
   @Transactional
   int deleteByOtherAndArticleidAndTypeAndReadn(String other, int articleid, String type, int readn);
```


#### 2 Controller
     - 댓글 작성, 좋아요, 북마크, 팔로우를 누른 경우
      -> 새로운 알림을 db에 저장
     ```java
        Notification notification = new Notification();
        notification.setContent(content);
        notification.setEmail(article.getEmail());
        notification.setNickname(userDao.findUserByEmail(article.getEmail()).get().getNickname());
        notification.setOther(other);
        notification.setOthernickname(userDao.findUserByEmail(other).get().getNickname());
        notification.setType(type);
        notification.setReadn(0);
        notification.setArticleid(article.getArticleid());
        notificationDao.save(notification);
     ```
     - 전체 알림 목록을 보여주는 경우
      -> 최근 30일간의 알림 중 읽지 않은 알림만 리턴
     ```java
        List<Notification> notificationList = notificationDao.findAllIn30Days(email);
        notificationList.addAll(notificationDao.findAllUnread(email));
     ```
     - 알림을 읽은 경우
      -> id를 이용하여 해당하는 알림을 찾아 setReadn, setReady를 이용해 읽음 표시
     ```java
        Notification notification = 
        notificationDao.findNotificationByNotificationid(notificationid);
        if(notification.getReady() == 0)
        {
            notification.setReadn(1);
            notification.setReady(1);
            notificationDao.save(notification);
        }
     ```
     - 알림을 삭제하는 경우
      -> id를 이용해 알림 삭제
     ```java
        notificationDao.deleteByNotificationid(notificationid)>0
     ```