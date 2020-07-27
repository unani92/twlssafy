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
    public static Specification<Article> searchByWriter(final String nickname) {
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

    // 태그로 검색 -> sno 찾기
    public static Specification<Skills> searchByKeyword(final String keywordName) {

        return new Specification<Skills>() {
            @Override
            public Predicate toPredicate(Root<Skills> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Join<Skills, Keywords> sno = root.join("sno", JoinType.INNER);
                return cb.equal(sno.get("name"), keywordName);
            }
        };
    }

    // // 태그로 검색 -> sno로 태그 글 찾기
    public static Specification<Article> searchBySno(final Specification<Skills> sno) {

        return new Specification<Article>() {
            @Override
            public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Join<Article, Skills> article = root.join("articleid", JoinType.INNER);
                try {
                    return cb.equal(article.get("sno"), sno.getClass().getField("sno"));
                } catch (NoSuchFieldException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (SecurityException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
                }
        };
    }
}