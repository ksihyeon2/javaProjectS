<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>captcha.jsp</title>
  <jsp:include page="/WEB-INF/views/include/bs4.jsp" />
  <style>
  	#captchaImage {
  		width:250px;
  		height:50px;
  		border:3px dotted #A3C552;
  		text-align:center;
  		padding:5px;
  	}
  </style>
  <script>
  	'use strict';
  	
  	$(document).ready(function(){
  		// captcha 이미지를 새로(다시) 만들기
  		$("#refreshBtn").click(function(e){
  			$.ajax({
  				url : "${ctp}/study/captcha/captchaImage",
  				async : false,  // 동기식으로 잠시 변경하기(작업이 끝난 후 다음 작업을 하겠다라는 정의)
  				type : "post",
  				success : function(){
  					//$("#captchaCode").load(location.href + "#captchaCode");
  					location.reload();
  				},
  				error : function(){
  					alert("전송 오류");
  				}
  			});
  		});
  		
	  	// captcha확인하기
	  	$("#confirmBtn").click(function(e){
	  		e.preventDefault();    // 새로고침(F5)버튼을 막아준다.
	  		
	  		let strCaptcha = $("#strCaptcha").val();
	  		
	  		$.ajax({
	  			url : "${ctp}/study/captcha/captcha",
	  			type : "post",
	  			data : {strCaptcha:strCaptcha},
	  		}).done(function(res){
	  				if(res != "1"){
	  					alert("로봇으로 의심됩니다. 다시 시도해주세요.");
	  				} else {
	  					alert("인증되었습니다.");
	  				}
	  		});
	  	});
  	});
  	
  </script>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp" />
<jsp:include page="/WEB-INF/views/include/slide2.jsp" />
<p><br /></p>
<div class="container">
	<h2>Captcha 연습</h2>
	<pre>
	CAPTCHA는 기계는 인식할 수 없으나, 사람은 쉽게 인식할 수 있는 텍스트나 이미지를 통해서 사람과 기계를 구변하는 프로그램이다.
	</pre>
	<hr />
	<form name="myform">
		<p id="captchaCode">
			다음 코드를 입력해 주세요 : <img src="${ctp}/data/study/${captchaImage}" id="captchaImage">
		</p>
		<p>
			<input type="text" name="strCaptcha" id="strCaptcha" />
			<input type="button" value="확인" id="confirmBtn" />
			<input type="button" value="새로고침" onclick="location.href='captchaImage';" />
		</p>
	</form>
</div>
<p><br /></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>