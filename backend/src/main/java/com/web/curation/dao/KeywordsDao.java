package com.web.curation.dao;

import java.util.List;

import com.web.curation.model.Keywords;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface KeywordsDao extends JpaRepository<Keywords, String> {
    Keywords save(Keywords keyword);
    void delete(Keywords keyword);
    int deleteByArticleid(int articleId);
    List<Keywords> findAllByArticleid(int articleId);
    List<Keywords> findAllBySnoOrderByArticleidDesc(int sno);

    @Query(value=
        "SELECT"+
        " sno, count(*)"+
        " FROM Keywords k"+
        " GROUP BY k.sno ORDER BY count(*) DESC"
    )
    List<Object[]> findGroupByKeywordsWithJPQL();
}