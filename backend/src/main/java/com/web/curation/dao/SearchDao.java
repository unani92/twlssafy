package com.web.curation.dao;


import com.web.curation.model.Article;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SearchDao extends JpaRepository<Article, String>, JpaSpecificationExecutor<Article> {
  //  Page<Article> findAll(Specification<Article> article, Pageable pageable);
}