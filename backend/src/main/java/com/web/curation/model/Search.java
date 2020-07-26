package com.web.curation.model;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public class Search {
    public static Specification<Article> searchByTitle(final String title) {
        return new Specification<Article>() {
            @Override
            public Predicate toPredicate(Root<Article> root, 
                CriteriaQuery<?> query, CriteriaBuilder cb) {
                    return cb.like(root.get("title"), "%"+title+"%");
                }
        };

        
    }
}