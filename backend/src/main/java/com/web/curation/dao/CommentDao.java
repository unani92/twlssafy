package com.web.curation.dao;

import java.util.List;

import com.web.curation.model.Comment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentDao extends JpaRepository<Comment, String> {

	int deleteByCommentid(int commentid);

	List<Comment> findAllByArticleidOrderByCommentidDesc(int articleid);

	Comment findByCommentid(int commentid);

	int countByArticleid(int articleid);
}