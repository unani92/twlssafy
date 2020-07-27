package com.web.curation.dao;


import com.web.curation.model.Article;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SearchDao extends JpaRepository<Article, String>, JpaSpecificationExecutor<Article> {
}