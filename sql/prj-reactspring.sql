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

select *
from member;

#board 테이블 수정
#writer column 지우기
#member_id column references member(id)

alter table board
    drop column writer;
alter table board
    add column member_id int references member (id) after content;

update board
set member_id = (select id from member order by id desc limit 1)
where id > 0;

alter table board
    modify column member_id int not null;
desc board;
select *
from board
order by id desc;

select *
from member;

select *
from member
where email = 'test1@naver.com';

delete
from board
where board.member_id = 9;

#권한 테이블
create table authority
(
    member_id int         not null references member (id),
    name      varchar(20) not null,
    primary key (member_id, name)
);

insert into authority(member_id, name)
values (19, 'admin');

select m.nick_name, a.name
from member m
         join authority a on m.id = a.member_id
where m.nick_name = 'admin';

select *
from member;