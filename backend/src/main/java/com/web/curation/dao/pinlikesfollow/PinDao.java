package com.web.curation.dao.pinlikesfollow;

import com.web.curation.model.pinlikesfollow.Pin;
import com.web.curation.model.pinlikesfollow.PinMultiId;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PinDao extends JpaRepository<Pin, PinMultiId> {
    Pin save(PinMultiId id);
    // void delete(Pin id);
    int deleteByEmailAndArticleid(String email, int articleid);
	Pin findByEmailAndArticleid(String email, int articleid);

}