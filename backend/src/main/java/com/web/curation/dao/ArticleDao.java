package com.web.curation.dao;

import com.web.curation.model.Article;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleDao extends JpaRepository<Article, String> {
    Article save(Article article);
    void delete(Article article);
}