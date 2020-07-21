create table `article` (
	`article_id` int auto_increment not null,
    `email` varchar(128) default null,
    `title` varchar(128) default null,
    `content` text default null,
    `img_url` text default null,
    `updated_at` datetime default now(),
    `created_at` datetime default now(),
    foreign key(`email`) references user(`email`),
    primary key(`article_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table pin(
  `pin_id` int auto_increment not null,
  `email` varchar(128) DEFAULT null,
  `article_id` int,
  foreign key(`email`) references user(`email`) on delete cascade,
  foreign key(`article_id`) references article(`article_id`) on delete cascade,
  primary key(`email`,`article_id`),
  unique key `pin_id` (`pin_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


