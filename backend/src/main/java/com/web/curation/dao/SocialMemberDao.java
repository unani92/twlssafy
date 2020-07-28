package com.web.curation.dao;

import java.util.Optional;
import com.web.curation.model.user.SocialMember;
import com.web.curation.model.user.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialMemberDao extends JpaRepository<SocialMember, String> {

    Optional<SocialMember> findSocialmemberByEmail (String email);

	Optional<User> findUserByEmail(String followemail);

}
