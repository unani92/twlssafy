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
    int countByEmail(String email);

    // select * from article where email in (select followemail from follow where email='vyzynn@gmail.com') order by articleid desc;
    @Query(value = "select * from article where email in (select followemail from follow where email = ?1) order by articleid desc", nativeQuery = true)
    List<Article> articleFromFollowing(String email);

    @Query(value = "select * from article where articleid in (select articleid from pin where email=?1) order by articleid desc", nativeQuery = true)
    List<Article> articleFromPin(String email);
    
    @Query(value = 
    "select * from article where date(createdat) =  ?1 and email = ?2 order by articleid desc",
    nativeQuery = true)
    List<Article> articleAt(String date,String email);

    List<Article> findAllByEmail(String email);
    List<Article> findAllByEmailOrderByArticleidDesc(String email);

    Page<Article> findAllByIspublic(Pageable pageable, int ispublic);


    @Query(value = 
    "select k.articleid from  " +
    "(select i.sno, s.name, i.email from interest as i, skills as s " +
    "where i.sno = s.sno and email = ?1) as a, " +
    "keyword as k, " +
    "(select l.articleid, count(*) as cnt, t.createdat as createdat from  " +
    "article as t, likes as l " +
    "where t.articleid = l.articleid " +
    "group by l.articleid " +
    "order by cnt desc) as t " +
    "where a.sno = k.sno " +
    "and t.articleid=k.articleid " +
    "and date(createdat) > date(subdate(now(), INTERVAL 3 DAY)) " +
    "order by cnt desc " +
    "limit 5 "
    , nativeQuery = true)
    List<Integer> rec1(String email);

    @Query(value = 
    "select k.articleid from  " +
    "(select i.sno, s.name, i.email from interest as i, skills as s " +
    "where i.sno = s.sno and email = ?1) as a, " +
    "keyword as k, " +
    "(select l.articleid, count(*) as cnt, t.createdat as createdat from  " +
    "article as t, pin as l " +
    "where t.articleid = l.articleid " +
    "group by l.articleid " +
    "order by cnt desc) as t " +
    "where a.sno = k.sno " +
    "and t.articleid=k.articleid " +
    "and date(createdat) > date(subdate(now(), INTERVAL 3 DAY)) " +
    "order by cnt desc " +
    "limit 5 "
    , nativeQuery = true)
    List<Integer> rec2(String email);

    @Query(value = 
        "select l.articleid from  " +
        "article as t, likes as l " +
        "where t.articleid = l.articleid " +
        "and date(t.createdat) > date(subdate(now(), INTERVAL 3 DAY)) " +
        "group by l.articleid " +
        "order by count(*) desc " +
        "limit 5 "
        , nativeQuery = true)
    List<Integer> rec3();

    @Query(value = 
        "select l.articleid from  " +
        "article as t, pin as l " +
        "where t.articleid = l.articleid " +
        "and date(t.createdat) > date(subdate(now(), INTERVAL 3 DAY)) " +
        "group by l.articleid " +
        "order by count(*) desc " +
        "limit 5 "
        , nativeQuery = true)
    List<Integer> rec4();



}