package com.spring.javaProjectS.vo;

import lombok.Data;

@Data
public class BoardVO {
	private int idx;
	private String mid;
	private String nickName;
	private String title;
	private String email;
	private String homePage;
	private String content;
	private int readNum;
	private String hostIp;
	private String openSw;
	private String wDate;
	private int good;
	
	private int hour_diff;  // 게시글 24시간동안 new.gif 이미지 표시를 위한 변수
	private int date_diff;	// 게시글이 1일 이전인지 체크하기 위한 변수
	
	private int replyCnt;  // 게시글 댓글 갯수 구하기 위한 변수
}
