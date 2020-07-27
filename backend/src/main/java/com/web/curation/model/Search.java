package com.web.curation.model;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public class Search {
    // 제목으로 검색
    public static Specification<Article> searchByTitle(final String title) {
        return new Specification<Article>() {
            @Override
            public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.like(root.get("title"), "%" + title + "%");
            }
        };
    }

    // 작성자로 검색
    public static Specification<Article> searchByNickname(final String nickname) {
        return new Specification<Article>() {
            @Override
            public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.like(root.get("nickname"), "%" + nickname + "%");
            }
        };
    }

    // 내용으로 검색
    public static Specification<Article> searchByContent(final String content) {
        return new Specification<Article>() {
            @Override
            public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.like(root.get("content"), "%" + content + "%");
            }
        };
    }

    // 태그로 검색 
    public static Specification<Article> searchBySno(final int sno) {
        return new Specification<Article>() {
            @Override
            public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Join<Article, Keywords> article = root.join("articleid", JoinType.INNER);
                return cb.equal(article.get("sno"), sno);
            }
        };
    }

	public static Specification<Article> searchByArticleid(int articleid) {
		return null;
	}

}