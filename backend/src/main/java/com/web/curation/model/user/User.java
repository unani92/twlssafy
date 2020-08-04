// 하단 DB 설정 부분은 Sub PJT II에서 데이터베이스를 구성한 이후에 주석을 해제하여 사용.

package com.web.curation.model.user;

import lombok.AllArgsConstructor;
// import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@Entity
// @Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name="member")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;

    private String email;

    @JsonIgnore
    private String password;

    private String nickname;

    private String type;

    private String info;

    private String img;

    private int grade;

    @Column(insertable = false, updatable = false)
    private LocalDateTime createdate;

}
