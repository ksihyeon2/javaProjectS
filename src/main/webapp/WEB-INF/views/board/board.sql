show tables;


/* lombok 사용시 필드명 두 번째 글자가 대문자이면 첫 번째 글자도 대문자로 적어주어야 에러가 나지 않음 */
create table board2(
	idx      int not null auto_increment primary key,     /* 게시글의 고유 번호 */
	mid      varchar(30) not null,												/* 게시글 작성이 아이디 */
	nickName varchar(30) not null,												/* 게시글 작성이 닉네임 */
	title    varchar(100) not null,												/* 게시글 제목 */
	email    varchar(60),																	/* 이메일 주소 */
	homePage varchar(60),																	/* 홈페이지 주소 */
	content  text not null,																/* 게시글 내용 */
	readNum  int not null default 0,											/* 게시글 조회수 */
	hostIp   varchar(40) not null,												/* 게시글 작성이 IP */
	openSw   char(2) default 'OK',												/* 게시글의 공개 여부 (OK:공개, NO:비공개) */
	wDate    datetime default now(),											/* 게시글 작성 날짜 */
	good		 int default 0    														/* '좋아요' 클릭 횟수 누적 */
);

desc board2;

insert into board2 values (default,'admin','관리맨','게시판 서비스를 시작합니다.','tlugs1426@naver.com','tlgus1426.tistory.com','게시할 내용들을 입력해 주세요.',default,'192.168.50.62',default,default,default);

select * from board2;


/* 게시판에 댓글 달기 */
create table board2Reply(
	idx     		int not null auto_increment,				/* 댓글의 고유 번호 */
	boardIdx	 	int not null,                       /* 원본글(부모글)의 고유번호(외래키로 설정) */
	re_step			int not null,												/* 레벨(re_step값)에 따른 들여쓰기(계층 번호) : 부모 댓글의 re_step은 0이다. 대댓글의 경우는 부모 re_step+1로 처리 */
	re_order 		int not null,												/* 댓글의 순서를 결정, 최신 부모 댓글은 1번, 대댓글의 경우는 re_order+1 시킨 후 나머지 댓글들은 re_order 보다 1개 증가*/
	mid      		varchar(30) not null,								/* 댓글 작성자의 아이디 */
	nickName 		varchar(30) not null,								/* 댓글 작성자의 닉네임 */
	wDate 			datetime default now(),							/* 댓글 작성 날짜 */
	hostIp 			varchar(50) not null, 							/* 댓글 작성 PC의 고유 IP */
	content 		text not null,											/* 댓글 내용*/
	primary key(idx),
	foreign key(boardIdx) references board2(idx)
	on update cascade    /* 부모 필드를 수정하면 함께 영향을 받는다 */
	on delete restrict   /* 부모 필드를 함부로 삭제할 수 없다.*/
);
desc board2Reply;
drop table board2Reply;

insert into board2Reply values (default,11,'ccc1234','씨씨맨',default,'210.100.20.25','안녕하세요');
insert into board2Reply values (default,11,'atom1234','홍장군',default,'200.130.25.2','안녕~');
insert into board2Reply values (default,19,'atom1234','홍장군',default,'200.130.25.2','안녕하세요~');   /* 외래키 설정이 되어있기 때문에 board2Idx에 19번이 없어서 저장이 되지 않음 */
insert into board2Reply values (default,1,'atom1234','홍장군',default,'200.130.25.2','안녕하세요~');

select * from board2Reply;


/* join 연습 */
select b.*, br.nickName from board2 as b , board2Reply as br where b.idx = br.boardIdx; 

/* 서브 쿼리 */
select b.*, br.nickName from board2 as b , board2Reply as br where b.idx = (select boardIdx from board2Reply where boardIdx=11 limit 1); 
select b.*, br.nickName,br.boardIdx from board2 as b, (select * from board2Reply where boardIdx=11) as br where b.idx=11; 

/* 댓글 수 연습 */
/* 게시판(board2)리스트 화면에서 글제목 옆에 해당 글의 댓글(board2Reply)수를 출력 */
-- 전체 board2 테이블의 내용을 최신순으로 출력 			 
select * from board2 order by idx desc;

-- board2 테이블 고유번호 11번에 해당하는 댓글 테이블의 댓글 수
select count(*) from board2Reply where boardIdx=11; 

-- 앞의 예제서 원본글의 고유번호와 함께, 총 댓글의 갯수는 replyCnt란 변수로 출력
select boardIdx,count(*) as replyCnt from board2Reply where boardIdx=11 ;

-- 앞의 예제에 이어서, 원본글을 쓴 닉네임도 함께 출력(여기서 닉네임은 부모테이블에서 가져와 출력)
select boardIdx,count(*) as replyCnt,(select nickName from board2 where idx=11) as nickName from board2Reply where boardIdx=11 ;

-- 앞의 내용의 부모관점(board2)에서 처리
-- 11번 게시글의 mid와 닉네임을 출력
select mid, nickName from board2 where idx=11;

-- 앞에 이어서 닉네임을 자식(댓글 테이블)에서 가져와 보여주기
select mid,(select nickName from board2Reply where boardIdx=11 limit 1) as nickName from board2 where idx=11; 

-- 부모글에 해당하는 자식글의 갯수를 부모글과 함께 출력
select mid, (select count(*) from board2Reply where boardIdx=11) as replyCnt from board2 where idx=11;
select *, (select count(*) from board2Reply where boardIdx=11) as replyCnt from board2 where idx=11;

select *, datediff(wDate, now()) from board2 order by idx desc limit 0,10;
/*뒤에서 앞에를 뺌 = 오늘 날짜에서 wDate를 뺌 (일수 구하기)*/
/* new.gif를 24시간동안만 보여주기 위한 처리*/
select *, timestampdiff(hour,wDate, now()) from board2 order by idx desc limit 0,10; /* (시간 구하기)*/
select *,datediff(wDate,now()) as date_diff, timestampdiff(hour,wDate, now()) from board2 order by idx desc limit 0, 10;

/* 이전글/다음글 꺼내오기 */
select * from board2 where idx=6;   /*현재 글*/
select * from board2 where idx < 6 order by idx desc limit 1;  /*이전 글*/
select * from board2 where idx > 6 order by idx limit 1;  /*다음 글*/

/* 날짜 함수 연습 <datetime 형식 비교> */
select now();    /* 오늘 날짜 */
/* date_add() */
select now() as 오늘날짜, date_add(now(), interval 1 day) as 다음날;
select now() as 오늘날짜, date_add(now(), interval -1 day) as 전날;
select now() as 오늘날짜, date_add(now(), interval 10 hour) as 10시간뒤;
select now() as 오늘날짜, date_add(now(), interval -10 hour) as 10시간전;

/* date_sub() */
select now() as 오늘날짜, date_sub(now(), interval 1 day) as 전날;
select now() as 오늘날짜, date_sub(now(), interval -1 day) as 다음날;

/* board2 테이블에 적용 */
-- 게시글중에서 하루전에 올라온 글만 보여주시오
select wDate, date_add(now(), interval 1 day) from board2;
select substring(wDate,1,10), substring(date_add(now(), interval -1 day),1,10) from board2;
select idx, nickName,wDate from board2 where substring(wDate,1,10) = substring(date_add(now(), interval -1 day),1,10);

/* 날짜차이 계산 : DATEDIFF(시작날짜, 마지막날짜) <int 형식 비교> */
select datediff('2023-11-14',now());
select datediff(now(),wDate) from board2;

select timestampdiff(hour,now(),wDate) from board2;
select timestampdiff(day,wDate,now()) from board2;

/* 날짜형식(date_format(날짜형식자료,포멧)) : 년도4자리 (%Y), 월(%m), 일(%d), 시간(%H), 분(%i) */
select wDate, date_format(wDate,'%Y-%m-%d') from board2;
select wDate, date_format(wDate,'%Y-%m-%d %H:%i') from board2;

/* -------------------------------------------------------------------------------- */
select * from board2 order by idx desc limit 0,5;

/* 날자 계산 datediff(비교할 날짜, 오늘날짜,) , 시간 계산 timestampdiff(시간 비교,비교 날짜, 오늘 날짜)*/
select *, datediff(wDate, now()) as date_diff, 
timestampdiff(hour, wDate, now()) as hour_diff 
from board2 order by idx desc limit 0,15;

