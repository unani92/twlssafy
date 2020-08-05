package com.web.curation.dao;
import com.web.curation.model.Skills;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillsDao extends JpaRepository<Skills, String> {

    Skills findSkillByName(String name);
    Skills findSkillBySno(int sno);
}   