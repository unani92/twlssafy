create table `member` (
  `no` int auto_increment NOT NULL,
  `email` varchar(128) DEFAULT NULL,
  `password` int DEFAULT NULL,
  `createDate` datetime DEFAULT current_timestamp(),
  `nickname` char(20) NOT NULL,
  `info` varchar(200),
  PRIMARY KEY (`email`),
  UNIQUE KEY `memberIdx_unique_no` (`no`),
  UNIQUE KEY `nickname` (`nickname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-----------------------------------------------------------------

create table `article` (
	`articleid` int auto_increment not null,
    `email` varchar(128) default null,
    `nickname` char(20) not null,
    `title` varchar(128) default null,
    `content` text default null,
    `imgUrl` text default null,
    `updatedAt` datetime default now(),
    `createdAt` datetime default now(),
    foreign key(`email`) references `member`(`email`) on delete cascade,
    foreign key(`nickname`) references `member`(`nickname`) on delete cascade,
    primary key(`articleid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-----------------------------------------------------------------

create table pin(
  `pinId` int auto_increment not null,
  `email` varchar(128) DEFAULT null,
  `articleid` int,
  foreign key(`email`) references `member`(`email`) on delete cascade,
  foreign key(`articleid`) references article(`articleid`) on delete cascade,
  primary key(`email`,`articleid`),
  unique key `pinId` (`pinId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-----------------------------------------------------------------

create table comment (
  `commentid` int auto_increment not null,
  `email` varchar(128) default null,
  `articleid` int,
  `content` text,
  `updatedAt` datetime default now(),
  `createdAt` datetime default now(), 
  foreign key (`email`) references `member`(`email`) on delete cascade,
  foreign key (`articleid`) references article(`articleid`) on delete cascade,
  primary key `commentid` (`commentid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-----------------------------------------------------------------

create table likes (
  `likesId` int auto_increment not null,
  `email` varchar(128) DEFAULT null,
  `articleid` int,
  foreign key(`email`) references `member`(`email`) on delete cascade,
  foreign key(`articleid`) references article(`articleid`) on delete cascade,
  primary key(`email`,`articleid`),
  unique key `likesId` (`likesId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-----------------------------------------------------------------

-- skills.csv import필요
create table `skills` (
 `sno` int auto_increment NOT NULL,
 `name` varchar(128) DEFAULT NULL,
 PRIMARY KEY (`sno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-----------------------------------------------------------------

create table `interest`(
 `interestId` int auto_increment NOT NULL,
`email`  varchar(128) DEFAULT NULL,
`sno` int not null,
foreign key (`email`) references member(`email`) on delete cascade,
foreign key (`sno`) references skills(`sno`) on delete cascade,
primary key (`email`,`sno`),
unique key `interestId` (`interestId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-----------------------------------------------------------------

create table `follow` (
 `followId` int auto_increment NOT NULL,
 `email` varchar(128) DEFAULT NULL,
 `followEmail` varchar(128) DEFAULT NULL,
 PRIMARY KEY (`email`,`followEmail`),
 FOREIGN KEY (`email`) REFERENCES member(`email`)
 ON DELETE CASCADE ON UPDATE CASCADE,
 FOREIGN KEY (`followEmail`) REFERENCES member(`email`)
 ON DELETE CASCADE ON UPDATE CASCADE,
 unique key (`followid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-----------------------------------------------------------------

create table keyword (
  `keywordId` int auto_increment not null,
  `articleid` int not null,
  `sno` int not null,
  primary key (`keywordId`),
  foreign key (`articleid`) references article(`articleid`) 
  on delete cascade on update cascade,
  foreign key (`sno`) references skills(`sno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-----------------------------------------------------------------

DELIMITER $$
DROP PROCEDURE IF EXISTS loopInsert$$
 
CREATE PROCEDURE loopInsert()
BEGIN
    DECLARE i INT DEFAULT 1;
        
    WHILE i <= 100 DO
        INSERT INTO article(email,nickname,title,content)
          VALUES('qwer@qwer.com', 'Qwerty!23', concat('제목',i), concat('내용',i));
      insert into keyword(articleId,sno) values ((select articleid from article order by articleid desc limit 1),  i+234);
        SET i = i + 1;
    END WHILE;
END$$
DELIMITER $$

call loopInsert();

select article.*, keyword.sno, skills.name
from article, keyword, skills
where article.articleid = keyword.articleid
and skills.sno = keyword.sno
order by articleid desc;

-----------------------------------------------------------------

create table `notification` (
 `notificationid` int auto_increment NOT NULL,
 `email` varchar(128) DEFAULT NULL,
 `other` varchar(128) DEFAULT NULL,
 `content` varchar(256) DEFAULT NULL,
 `type` varchar (128) default null,
 `readn` int default null,
 `articleid` int default null,
 `createtime` datetime default now(),
 unique KEY (`notificationid`),
 primary key (`email`,`other`,`type`,`content`,`articleid`,`readn`),
 FOREIGN KEY (`email`) REFERENCES `member`(`email`),
 FOREIGN KEY (`other`) REFERENCES `member`(`email`)
 ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-----------------------------------------------------------------

create table `socialMember` (
  `no` int auto_increment NOT NULL,
  `email` varchar(128) DEFAULT NULL,
  `createDate` datetime DEFAULT current_timestamp(),
  `nickname` char(20) NOT NULL,
  `info` varchar(200),
  `type` varchar(8),
  PRIMARY KEY (`email`),
  UNIQUE KEY `memberIdx_unique_no` (`no`),
  UNIQUE KEY `nickname` (`nickname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
