package com.web.curation.dao;

import com.web.curation.model.user.SocialMember;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialMemberDao extends JpaRepository<SocialMember, String> {

}
