package com.web.curation.dao;

import java.util.List;

import com.web.curation.model.Article;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleDao extends JpaRepository<Article, String> {
    Article save(Article article);
	int deleteByArticleid(int articleid);
    
    Article findFirstByEmailOrderByArticleidDesc(String email);
    Article findByArticleid(int articleId);
    Page<Article> findAllByNickname(Pageable pageable, String nickname);
	// Page<Article> findByArticleid(PageRequest of, int articleid);

}