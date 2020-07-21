package com.web.curation.dao.user;

import com.web.curation.model.user.Skills;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillsDao extends JpaRepository<Skills, String> {

    Skills findSkillByName(String name);
}