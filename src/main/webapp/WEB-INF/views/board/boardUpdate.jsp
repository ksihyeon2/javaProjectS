<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>boardInput.jsp</title>
	<script src="${ctp}/ckeditor/ckeditor.js"></script>
	<jsp:include page="/WEB-INF/views/include/bs4.jsp" />
	<style>
		th {
			text-align:center;
			background-color:#eee;
		}
	</style>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp" />
<jsp:include page="/WEB-INF/views/include/slide2.jsp" />
	<p><br /></p>
	<div class="container">
		<h2 class="text-center">게 시 판 글 수 정 하 기</h2>
		<form name="myform" method="post">
			<table class="table table-bordered">
				<tr>
					<th>작성자</th>
					<td>${sNickName}</td>
				</tr>
				<tr>
					<th>제목</th>
					<td><input type="text" name="title" id="title" value="${vo.title}" autofocus required class="form-control" /></td>
				</tr>
				<tr>
					<th>이메일</th>
					<td><input type="email" name="email" id="email" value="${vo.email}" class="form-control" /></td>
				</tr>
				<tr>
					<th>홈페이지</th>
					<td><input type="text" name="homePage" id="homePage" value="${vo.homePage}" class="form-control" /></td>
				</tr>
				<tr>
      	<!-- ckeditor을 사용하기 위해서는 id에 ckeditor에서 재공하는 id를 사용(= CKEDITOR) -->
	        <th>글내용</th>
	        <td><textarea rows="6" name="content" id="CKEDITOR" class="form-control" required>${vo.content}</textarea></td>
	        <!-- name=content에 있는 내용을 치환하겠다는 명령 -->
	        <script>
	        	CKEDITOR.replace("content",{
	        		height : 500,
	        		filebrowserUploadUrl : "${ctp}/imageUpload",  /* 파일(이미지) 업로드 시 매핑 경로 */
	        		uploadUrl : "${ctp}/imageUpload"							/* 여러개의 그림 파일을 드래그&드롭해서 올릴 수 있다. */
	        		/* 드래그&드롭 시 문제점 : 크기 설정 불가, 사진 업로드 전 파일 선택만 해도 파일에 자동 업로드 되어 파일을 삭제 후 글을 작성해도 해당 사진은 파일에 저장된다. */
	        	});
	        </script>
	      </tr>
				<tr>
					<th>공개여부</th>
					<td>
						<input type="radio" name="openSw" value="OK" <c:if test="${vo.openSw == 'OK'}" >checked</c:if>/>공개 &nbsp;
						<input type="radio" name="openSw" value="NO" <c:if test="${vo.openSw == 'NO'}" >checked</c:if>/>비공개
					</td>
				</tr>
				<tr>
					<td colspan="2" class="text-center">
						<input type="submit" value="수정하기" class="btn btn-info"/> &nbsp;
						<input type="reset" value="다시 입력" class="btn btn-warning"/> &nbsp;
						<input type="button" onclick="location.href='boardList?pag=${pag}&pageSize=${pageSize}';" value="돌아가기" class="btn btn-danger"/>
					</td>
				</tr>
			</table>		
			<input type="hidden" name="hostIp" value="${pageContext.request.remoteAddr}" />
			<input type="hidden" name="idx" value="${vo.idx}" />
			<input type="hidden" name="nickName" value="${sNickName}" />
			<input type="hidden" name="pag" value="${pag}" />
			<input type="hidden" name="pageSize" value="${pageSize}" />
		</form>
	</div>
	<p><br /></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>