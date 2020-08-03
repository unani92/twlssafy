package com.web.curation.dao.pinlikesfollow;

import java.util.List;

import com.web.curation.model.pinlikesfollow.Likes;
import com.web.curation.model.pinlikesfollow.LikesMultiId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LikesDao extends JpaRepository<Likes, LikesMultiId> {
    Likes save(LikesMultiId id);
    // void delete(Likes id);
    int deleteByEmailAndArticleid(String email, int articleid);
	Likes findByEmailAndArticleid(String email, int articleid);
	List<Likes> findAllByEmail(String email);
    int countByArticleid(int articleid);
    
    @Query(value = "select articleid, count(*) from Likes group by articleid order by count(*) desc")
    List<Object[]> articleFromHot();
}