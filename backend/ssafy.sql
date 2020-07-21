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

create table `article` (
	`articleid` int auto_increment not null,
    `email` varchar(128) default null,
    `nickname` char(20) not null,
    `title` varchar(128) default null,
    `content` text default null,
    `imgUrl` text default null,
    `updatedAt` datetime default now(),
    `createdAt` datetime default now(),
    foreign key(`email`) references `member`(`email`),
    foreign key(`nickname`) references `member`(`nickname`),
    primary key(`articleid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
desc article;

create table pin(
  `pinId` int auto_increment not null,
  `email` varchar(128) DEFAULT null,
  `articleid` int,
  foreign key(`email`) references `member`(`email`) on delete cascade,
  foreign key(`articleid`) references article(`articleid`) on delete cascade,
  primary key(`email`,`articleid`),
  unique key `pinId` (`pinId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table comment (
  `commentId` int auto_increment not null,
  `email` varchar(128) default null,
  `articleid` int,
  `content` text,
  `updatedAt` datetime default now(),
  `createdAt` datetime default now(),
  foreign key (`email`) references `member`(`email`),
  foreign key (`articleid`) references article(`articleid`),
  primary key `commentId` (`commentId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


create table likes (
  `likesId` int auto_increment not null,
  `email` varchar(128) DEFAULT null,
  `articleid` int,
  foreign key(`email`) references `member`(`email`) on delete cascade,
  foreign key(`articleid`) references article(`articleid`) on delete cascade,
  primary key(`email`,`articleid`),
  unique key `likesId` (`likesId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- skills.csv import필요
create table `skills` (
 `sno` int auto_increment NOT NULL,
 `name` varchar(128) DEFAULT NULL,
 PRIMARY KEY (`sno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


create table `interest`(
 `interestId` int auto_increment NOT NULL,
`email`  varchar(128) DEFAULT NULL,
`sno` int not null,
foreign key (`email`) references member(`email`) on delete cascade,
foreign key (`sno`) references skills(`sno`) on delete cascade,
primary key (`email`,`sno`),
unique key `interestId` (`interestId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


create table `follow` (
 `followId` int auto_increment NOT NULL,
 `email` varchar(128) DEFAULT NULL,
 `followEmail` varchar(128) DEFAULT NULL,
 PRIMARY KEY (`followId`),
 FOREIGN KEY (`email`) REFERENCES member(`email`)
 ON DELETE CASCADE ON UPDATE CASCADE,
 FOREIGN KEY (`followEmail`) REFERENCES member(`email`)
 ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


create table keyword (
  `keywordId` int auto_increment not null,
  `articleid` int not null,
  `sno` int not null,
  primary key (`keywordId`),
  foreign key (`articleid`) references article(`articleid`) 
  on delete cascade on update cascade,
  foreign key (`sno`) references skills(`sno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;