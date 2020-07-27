package com.web.curation.dao.pinlikesfollow;


import java.util.List;

import com.web.curation.model.pinlikesfollow.Notification;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationDao extends JpaRepository<Notification, String> {
 
   Notification save(Notification notification);
   long countByEmail(String follow);



}