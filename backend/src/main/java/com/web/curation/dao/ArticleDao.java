package com.web.curation.dao;

import java.util.List;

import com.web.curation.model.Article;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ArticleDao extends JpaRepository<Article, String> {
    Article save(Article article);
	int deleteByArticleid(int articleid);
    
    Article findFirstByEmailOrderByArticleidDesc(String email);
    Article findByArticleid(int articleId);
    Page<Article> findAllByNickname(Pageable pageable, String nickname);
	// Page<Article> findByArticleid(PageRequest of, int articleid);

    int countByNickname(String nickname);

    // select * from article where email in (select followemail from follow where email='vyzynn@gmail.com') order by articleid desc;
    @Query(value = " select * from article where email in (select followemail from follow where email = ?1) order by articleid desc", nativeQuery = true)
    List<Article> articleFromFollowing(String email);

}