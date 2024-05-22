use `prj-reactspring`;

create table board
(
    id      int primary key auto_increment,
    title   varchar(100)  not null,
    content varchar(1000) not null,
    writer  varchar(100)  not null,
    regDate datetime default now()
);

desc board;
select *
from board;

create table member
(
    id        int primary key auto_increment,
    email     varchar(100) not null unique,
    password  varchar(100) not null,
    nick_name varchar(100) not null unique,
    regDate   datetime default now()
);


