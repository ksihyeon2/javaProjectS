show tables;

create table visit (
	visitDate  datetime not null default now(),
	visitCount int not null default 1
);

drop table visit;
delete from visit;

select * from visit;

insert into visit values (date(now()),default);
insert into visit values (default,default);
insert into visit values ('2023-12-27',8);
insert into visit values ('2023-12-26',5);
insert into visit values ('2023-12-24',10);
insert into visit values ('2023-12-23',12);
insert into visit values ('2023-12-21',5);
insert into visit values ('2023-12-19',3);
insert into visit values ('2023-12-18',15);
insert into visit values ('2023-12-16',21);
insert into visit values ('2023-12-15',6);

select * from visit order by visitDate desc limit 7;
select substring(visitDate, 1, 10) as visitDate, visitCount from visit order by visitDate desc limit 7;
