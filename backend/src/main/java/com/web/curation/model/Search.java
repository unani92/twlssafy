package com.web.curation.model;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public class Search {
    // 제목으로 검색
    public static Specification<Article> searchByTitleOrderByArticleidDesc(final String title) {
        return new Specification<Article>() {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.orderBy(cb.desc(root.get("articleid")));
                return cb.like(root.get("title"), "%" + title + "%");
            }
        };
    }
    
    // 작성자로 검색
    public static Specification<Article> searchByNicknameOrderByArticleidDesc(final String nickname) {
        return new Specification<Article>() {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.orderBy(cb.desc(root.get("articleid")));
                return cb.like(root.get("nickname"), "%" + nickname + "%");
            }
        };
    }
    
    // 내용으로 검색
    public static Specification<Article> searchByContentOrderByArticleidDesc(final String content) {
        return new Specification<Article>() {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.orderBy(cb.desc(root.get("articleid")));
                return cb.like(root.get("content"), "%" + content + "%");
            }
        };
    }


}