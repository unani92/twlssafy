create table `member` (
  `no` int auto_increment NOT NULL,
  `email` varchar(128) DEFAULT NULL,
  `password` int DEFAULT NULL,
  `create_date` datetime DEFAULT current_timestamp(),
  `nickname` char(20) NOT NULL,
  `info` varchar(200),
  PRIMARY KEY (`email`),
  UNIQUE KEY `member_idx_unique_no` (`no`),
  UNIQUE KEY `nickname` (`nickname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table `article` (
	`article_id` int auto_increment not null,
    `email` varchar(128) default null,
    `nickname` char(20) not null,
    `title` varchar(128) default null,
    `content` text default null,
    `img_url` text default null,
    `updated_at` datetime default now(),
    `created_at` datetime default now(),
    foreign key(`email`) references `member`(`email`),
    foreign key(`nickname`) references `member`(`nickname`),
    primary key(`article_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
desc article;

create table pin(
  `pin_id` int auto_increment not null,
  `email` varchar(128) DEFAULT null,
  `article_id` int,
  foreign key(`email`) references `member`(`email`) on delete cascade,
  foreign key(`article_id`) references article(`article_id`) on delete cascade,
  primary key(`email`,`article_id`),
  unique key `pin_id` (`pin_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table comment (
  `comment_id` int auto_increment not null,
  `email` varchar(128) default null,
  `article_id` int,
  `content` text,
  `updated_at` datetime default now(),
  `created_at` datetime default now(),
  foreign key (`email`) references `member`(`email`),
  foreign key (`article_id`) references article(`article_id`),
  primary key `comment_id` (`comment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


create table likes (
  `likes_id` int auto_increment not null,
  `email` varchar(128) DEFAULT null,
  `article_id` int,
  foreign key(`email`) references `member`(`email`) on delete cascade,
  foreign key(`article_id`) references article(`article_id`) on delete cascade,
  primary key(`email`,`article_id`),
  unique key `likes_id` (`likes_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- skills.csv import필요
create table `skills` (
 `sno` int auto_increment NOT NULL,
 `name` varchar(128) DEFAULT NULL,
 PRIMARY KEY (`sno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


create table `interest`(
 `interest_id` int auto_increment NOT NULL,
`email`  varchar(128) DEFAULT NULL,
`sno` int not null,
foreign key (`email`) references member(`email`) on delete cascade,
foreign key (`sno`) references skills(`sno`) on delete cascade,
primary key (`email`,`sno`),
unique key `interest_id` (`interest_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


create table `follow` (
 `follow_id` int auto_increment NOT NULL,
 `email` varchar(128) DEFAULT NULL,
 `follow_email` varchar(128) DEFAULT NULL,
 PRIMARY KEY (`follow_id`),
 FOREIGN KEY (`email`) REFERENCES member(`email`)
 ON DELETE CASCADE ON UPDATE CASCADE,
 FOREIGN KEY (`follow_email`) REFERENCES member(`email`)
 ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


create table keyword (
  `keyword_id` int auto_increment not null,
  `article_id` int not null,
  `sno` int not null,
  primary key (`keyword_id`),
  foreign key (`article_id`) references article(`article_id`) 
  on delete cascade on update cascade,
  foreign key (`sno`) references skills(`sno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;