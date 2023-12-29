<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>chart.jsp</title>
  <jsp:include page="/WEB-INF/views/include/bs4.jsp" />
  <script>
  	'use strict';
  	
  	function chartChange(){
  		let part = document.getElementById("part").value;
  		if(part == ""){
  			alert("차트를 선택하세요.");
  			return false;
  		}
  		location.href = '${ctp}/study/chart/chart?part='+part;
  	}
  </script>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp" />
<jsp:include page="/WEB-INF/views/include/slide2.jsp" />
<p><br /></p>
<div class="container">
	<h2>구글 차트 연습</h2>
	<div>
		<p>학습할 차트를 선택하세요.
			<select name="part" id="part" onchange="chartChange()">
				<option value="">차트선택</option>
				<option value="barH" 		 ${part == 'barH' 		? 'selected' : ''}>수평막대 차트</option>
				<option value="barV" 		 ${part == 'barV' 		? 'selected' : ''}>수직막대 차트</option>
				<option value="line" 		 ${part == 'line' 		? 'selected' : ''}>선 차트</option>
				<option value="pie" 		 ${part == 'pie' 			? 'selected' : ''}>원형 차트</option>
				<option value="pie3D" 	 ${part == 'pie3D' 		? 'selected' : ''}>3D 원형</option>
				<option value="bubble"   ${part == 'bubble' 	? 'selected' : ''}>버븥 차트</option>
				<option value="combo" 	 ${part == 'combo' 		? 'selected' : ''}>콤보 차트</option>
				<option value="gant" ${part == 'gant' ? 'selected' : ''}>간트 차트</option>
				<option value="timeLine" ${part == 'timeLine' ? 'selected' : ''}>timeLine 차트</option>
			</select>
		</p>
		<hr />
		<div>
			<c:if test="${part == 'barH'}"><jsp:include page="barHChart.jsp" /></c:if>
			<c:if test="${part == 'barV'}"><jsp:include page="barVChart.jsp" /></c:if>
			<c:if test="${part == 'line'}"><jsp:include page="lineChart.jsp" /></c:if>
			<c:if test="${part == 'pie'}"><jsp:include page="pieChart.jsp" /></c:if>
			<c:if test="${part == 'pie3D'}"><jsp:include page="pie3DChart.jsp" /></c:if>
			<c:if test="${part == 'bubble'}"><jsp:include page="bubbleChart.jsp" /></c:if>
			<c:if test="${part == 'combo'}"><jsp:include page="comboChart.jsp" /></c:if>
			<c:if test="${part == 'gant'}"><jsp:include page="gantChart.jsp" /></c:if>
			<c:if test="${part == 'timeLine'}"><jsp:include page="timeLineChart.jsp" /></c:if>
		</div>
	</div>
</div>
<p><br /></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>