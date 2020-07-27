package com.web.curation.dao.pinlikesfollow;


import java.util.List;

import com.web.curation.model.pinlikesfollow.Notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NotificationDao extends JpaRepository<Notification, String> {
 
   Notification save(Notification notification);

   @Query(value = "SELECT count(readn) FROM notification where readn = 0 and email = ?1", nativeQuery = true)
   int countByEmailAndRead(String follow);


}