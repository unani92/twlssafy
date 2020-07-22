package com.web.curation.dao.pinlikesfollow;


import com.web.curation.model.pinlikesfollow.Follow;
import com.web.curation.model.pinlikesfollow.FollowMultiId;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowDao extends JpaRepository<Follow, FollowMultiId> {
    Follow save(FollowMultiId f);
	  int deleteByEmailAndFollowemail(String email, String follow);
	  Follow findByEmailAndFollowemail(String email, String follow);

}