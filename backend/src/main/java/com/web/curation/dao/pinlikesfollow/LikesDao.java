package com.web.curation.dao.pinlikesfollow;

import com.web.curation.model.pinlikesfollow.Likes;
import com.web.curation.model.pinlikesfollow.LikesMultiId;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesDao extends JpaRepository<Likes, LikesMultiId> {
    Likes save(LikesMultiId id);
    // void delete(Likes id);
    int deleteByEmailAndArticleid(String email, int articleid);
	Likes findByEmailAndArticleid(String email, int articleid);

}