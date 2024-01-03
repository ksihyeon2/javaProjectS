<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page errorPage="/WEB-INF/views/errorPage/errorMessage1.jsp" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>error500.jsp</title>
  <jsp:include page="/WEB-INF/views/include/bs4.jsp" />
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp" />
<jsp:include page="/WEB-INF/views/include/slide2.jsp" />
<p><br /></p>
<div class="container">
	<h6>에러 발생시 호출되는 페이지입니다.(500에러 발생시 보이는 화면)</h6>
	<hr />
	<div>(사용에 불편을 드려서 죄송합니다.)</div>
	<div>빠른 시일 내에 복구되도록 하겠습니다.</div>
	<hr />
	<div><img src="${ctp}/images/newyork.jpg" width="300px" /></div>
	<hr />
	<div>
		<a href="${ctp}/errorPage/errorMain" class="btn btn-danger">돌아가기</a>
	</div>	
</div>
<p><br /></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>