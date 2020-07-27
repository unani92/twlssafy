package com.web.curation.dao.pinlikesfollow;


import java.util.List;

import javax.transaction.Transactional;

import com.web.curation.model.pinlikesfollow.Notification;
import com.web.curation.model.pinlikesfollow.NotificationMultiId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NotificationDao extends JpaRepository<Notification, NotificationMultiId> {
 
   Notification save(NotificationMultiId notification);

   @Query(value = "SELECT count(readn) FROM notification where readn = 0 and email = ?1", nativeQuery = true)
   int countByEmailAndRead(String follow);
   @Query(value = 
   "select * from notification where email = ?1 and date(createtime) > date(subdate(now(), INTERVAL 30 DAY)) order by notificationid desc" 
   , nativeQuery = true)
   List<Notification> findAllIn30Days(String email);

   @Query(value = 
   "select * from notification where email = ?1 and date(createtime) <= date(subdate(now(), INTERVAL 30 DAY)) and readn = 0 order by notificationid desc" 
   , nativeQuery = true)
   List<Notification> findAllUnread(String email);
   
   int deleteByNotificationid(int notificationid);

   Notification findNotificationByNotificationid(int notificationid);

   @Query(value = 
   "select * from notification where email = ?1 and other = ?2 and type = ?3 and articleid = ?4 and content = ?5 and readn = ?6"
   , nativeQuery = true)
   Notification findNotification(String email, String other, String type, int articleid, String content, int readn);

   @Transactional
   // @Query(value = 
   // "delete from notification where email = ?1 and articleid = ?2 and type = ?3"
   // , nativeQuery = true)
   int deleteByOtherAndArticleidAndTypeAndReadn(String other, int articleid, String type, int readn);

}