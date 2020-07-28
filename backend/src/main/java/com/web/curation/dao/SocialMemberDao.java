package com.web.curation.dao;

import java.util.Optional;
import com.web.curation.model.user.SocialMember;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialMemberDao extends JpaRepository<SocialMember, String> {

    Optional<SocialMember> findSocialmemberByEmail (String email);

}
