show tables;

create table kakaoAddress(
	address 		varchar(50) not null,			/* 지점명 */
	latitude		double not null,					/* 위도 */
	longtitude	double not null 					/* 경도 */
);

desc kakaoAddress;

select * from kakaoAddress order by address;

delete form kakaoAddress;