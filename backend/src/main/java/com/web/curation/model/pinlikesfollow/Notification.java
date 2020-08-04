
package com.web.curation.model.pinlikesfollow;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(NotificationMultiId.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name="notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int notificationid;

    private String email;
    private String content;
    private String type;
    private String other;
    private int readn;
    private int articleid;

    private int ready;

    @Column(insertable = false, updatable = false)
    private LocalDateTime createtime;


}
