package com.web.curation.dao.user;

import com.web.curation.model.user.Interest;
import com.web.curation.model.user.MultiId;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterestDao extends JpaRepository<Interest, MultiId> {
    Interest save(MultiId id);
    void delete(Interest id);
    //Optional<Interest> findSkillByEmail(String email);
}