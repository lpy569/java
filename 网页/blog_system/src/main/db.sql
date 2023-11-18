create database if not exists blog_system charset utf8;

use blog_system;

drop table if exists blog;
drop table if exists user;

create table blog(
    blogId int primary key auto_increment,
    title varchar(1024),
    content varchar(4090),
    postTime datetime,
    userId int
);

create table user(
    userId int primary key auto_increment,
    username varchar(50) unique,
    password varchar(50)
);

-- 插入进行测试
insert into blog values(1, '这是第一篇博客', '我要认真写代码', now(), 1);
insert into blog values(2, '这是第二篇博客', '我要认真写代码', now(), 1);
insert into blog values(3, '这是第三篇博客', '我要认真写代码', now(), 1);

insert into user values(1, 'llt', '2569');
insert into user values(2, 'llt2', '25692');