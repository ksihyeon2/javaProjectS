<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>kakaoEx2.jsp</title>
  <jsp:include page="/WEB-INF/views/include/bs4.jsp" />
  <script>
  	'use strict';
  	
  	function addressSearch(){
  		let address = myform.address.value;
  		if(address == "") {
  			alert("검색할 지점을 선택하세요.");
  			return false;
  		}
  		
  		myform.submit();
  	}
  	
  	function addressDelete(){
  		let address = myform.address.value;
  		if(address == "") {
  			alert("검색할 지점을 선택하세요.");
  			return false;
  		}
  		let ans = confirm("선택하신 지점을 MyDB에서 삭제하시겠습니까?");
  		if(!ans) {
  			return false;
  		}
  		
  		$.ajax({
  			url : "${ctp}/study/kakao/kakaoAddressDelete",
  			type : "post",
  			data : {address : address},
  			success : function(){
  				alert("DB에 저장된 지점명이 삭제되었습니다.");
  				location.href = "kakaoEx2";
  			},
  			error : function(){
  				alert("전송 오류");
  			}
  		});
  	}
  </script>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp" />
<jsp:include page="/WEB-INF/views/include/slide2.jsp" />
<p><br /></p>
<div class="container">
	<h2>MyDB에 저장된 지명으로 검색하기</h2>
	<hr />
	<div>
		<form name="myform" class="mb-3">
			<select name="address"	id="address">
				<option value="">지역선택</option>
				<c:forEach var="aVO" items="${vos}">
					<option value="${aVO.address}" <c:if test="${aVO.address == vo.address}">selected</c:if>>${aVO.address}</option>
				</c:forEach>
			</select>
			<input type="button" value="지점검색" onclick="addressSearch()" class="btn btn-info btn-sm" />
			<input type="button" value="지점삭제" onclick="addressDelete()" class="btn btn-danger btn-sm"/>
		</form>
	</div>
	<div id="map" style="width:100%;height:350px;"></div>
	<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=926ce984f760d5736b7c85d9826b9ff9"></script>
	<script>
		var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
	    mapOption = {
	        center: new kakao.maps.LatLng(${vo.latitude}, ${vo.longtitude}), // 지도의 중심좌표
	        level: 3 // 지도의 확대 레벨
	    };  
	
		// 지도를 생성합니다    
		var map = new kakao.maps.Map(mapContainer, mapOption); 
		
		// 주소-좌표 변환 객체를 생성합니다
		//var geocoder = new kakao.maps.services.Geocoder();
		
		// 주소로 좌표를 검색합니다
		//geocoder.addressSearch('', function(result, status) {
		
		    // 정상적으로 검색이 완료됐으면 
		     //if (status === kakao.maps.services.Status.OK) {
		
		        var coords = new kakao.maps.LatLng(${vo.latitude}, ${vo.longtitude});
		
		        // 결과값으로 받은 위치를 마커로 표시합니다
		        var marker = new kakao.maps.Marker({
		            map: map,
		            position: coords
		        });
		
		        // 인포윈도우로 장소에 대한 설명을 표시합니다
		        var infowindow = new kakao.maps.InfoWindow({
		            content: '<div style="width:150px;text-align:center;padding:6px 0;">${vo.address}</div>'
		        });
		        infowindow.open(map, marker);
		
		        // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
		        map.setCenter(coords);
		    //} 
		//});    
	</script>
	<br />
	<jsp:include page="kakaoMenu.jsp" />
</div>
<p><br /></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>