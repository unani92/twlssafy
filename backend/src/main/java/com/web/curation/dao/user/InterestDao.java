package com.web.curation.dao.user;

import java.util.List;

import com.web.curation.model.user.Interest;
import com.web.curation.model.user.MultiId;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InterestDao extends JpaRepository<Interest, MultiId> {
    Interest save(MultiId id);
    void delete(Interest id);
    List<Interest> findAllByEmail(String email);
    <Optional>Interest findByEmailAndSno(String email, int sno);
}