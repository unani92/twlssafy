create table `article` (
	`article_id` int auto_increment not null,
    `email` varchar(128) default null,
    `title` varchar(128) default null,
    `content` text default null,
    `img_url` text default null,
    `updated_at` datetime default now(),
    `created_at` datetime default now(),
    foreign key(`email`) references `member`(`email`),
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
