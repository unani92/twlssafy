package com.web.curation.dao;


import com.web.curation.model.Keywords;

import org.springframework.data.jpa.repository.JpaRepository;

public interface KeywordsDao extends JpaRepository<Keywords, String> {
    Keywords save(Keywords keyword);
    void delete(Keywords keyword);

}