package com.web.curation.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;

import com.fasterxml.jackson.annotation.JsonInclude;


@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(MultiId.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Interest {

    @Id
    private String email;
    @Id
    private int sno;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int interest_id;


}
