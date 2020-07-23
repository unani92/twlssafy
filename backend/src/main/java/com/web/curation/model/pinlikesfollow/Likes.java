package com.web.curation.model.pinlikesfollow;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;

import com.fasterxml.jackson.annotation.JsonInclude;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(LikesMultiId.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Likes implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Id
    private String email;
    @Id
    private int articleid;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int likesid;


}
