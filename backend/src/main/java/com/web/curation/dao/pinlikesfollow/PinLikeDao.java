package com.web.curation.dao.pinlikesfollow;

import com.web.curation.model.user.Interest;
import com.web.curation.model.user.MultiId;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PinLikeDao extends JpaRepository<Interest, MultiId> {
    Interest save(MultiId id);
    void delete(Interest id);
    //Optional<Interest> findSkillByEmail(String email);
}