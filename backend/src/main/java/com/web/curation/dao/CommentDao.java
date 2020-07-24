package com.web.curation.dao;

import com.web.curation.model.Comment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentDao extends JpaRepository<Comment, String> {

	int deleteByCommentid(int commentid);
}