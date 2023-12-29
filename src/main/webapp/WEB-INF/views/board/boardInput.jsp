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
      text-align: center;
      background-color: #eee;
    }
  </style>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp" />
<jsp:include page="/WEB-INF/views/include/slide2.jsp" />
<p><br/></p>
<div class="container">
  <h2 class="text-center">게 시 판 글 쓰 기</h2>
  <form name="myform" method="post">
    <table class="table table-bordered">
      <tr>
        <th>글쓴이</th>
        <td>${sNickName}</td>
      </tr>
      <tr>
        <th>글제목</th>
        <td><input type="text" name="title" id="title" placeholder="글제목을 입력하세요" autofocus required class="form-control"/></td>
      </tr>
      <tr>
        <th>이메일</th>
        <td><input type="text" name="email" id="email" placeholder="이메일을 입력하세요" class="form-control"/></td>
      </tr>
      <tr>
        <th>홈페이지</th>
        <td><input type="text" name="homePage" id="homePage" value="https://" placeholder="홈페이지를 입력하세요" class="form-control"/></td>
      </tr>
      <tr>
      <!-- ckeditor을 사용하기 위해서는 id에 ckeditor에서 재공하는 id를 사용(= CKEDITOR) -->
        <th>글내용</th>
        <td><textarea rows="6" name="content" id="CKEDITOR" class="form-control" required></textarea></td>
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
          <input type="radio" name="openSw" value="OK" checked />공개 &nbsp;
          <input type="radio" name="openSw" value="NO" />비공개
        </td>
      </tr>
      <tr>
        <td colspan="2" class="text-center">
          <input type="submit" value="글올리기" class="btn btn-success" /> &nbsp;
          <input type="reset" value="다시입력" class="btn btn-warning" /> &nbsp;
          <input type="button" onclick="location.href='boardList';" value="돌아가기" class="btn btn-info" />
        </td>
      </tr>
    </table>
    <input type="hidden" name="hostIp" value="${pageContext.request.remoteAddr}" />
    <input type="hidden" name="nickName" value="${sNickName}" />
    <input type="hidden" name="mid" value="${sMid}" />
  </form>
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>