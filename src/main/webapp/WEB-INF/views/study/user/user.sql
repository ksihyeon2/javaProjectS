show tables;

create table user (
  idx  int not null auto_increment primary key,
  mid  varchar(20) not null,
  name varchar(20) not null,
  age  int  default 20,
  address varchar(10)
);

desc user;

select * from user;

insert into user values (default,'aaaa','에에에',22,'서울');
insert into user values (default,'bbbb','비비비',23,'청주');
insert into user values (default,'cccc','씨씨씨',24,'제주');

delete from user where idx = 10;

select * from user where name like '%에%' order by idx desc;
select * from user where name like concat('%','에','%') order by idx desc;