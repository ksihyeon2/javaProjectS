<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>sample.jsp</title>
  <jsp:include page="/WEB-INF/views/include/bs4.jsp" />
  <script type="text/javascript" src="https://code.jquery.com/jquery-1.12.4.min.js" ></script>
  <script type="text/javascript" src="https://cdn.iamport.kr/js/iamport.payment-1.2.0.js"></script>
  <script>
      var IMP = window.IMP;
      IMP.init("imp20734763");   // 내 식별코드에서 가맹점 식별코드 넣기
 
     // function requestPay() {
        IMP.request_pay(
          {
            pg: "html5_inicis.INIBillTst",
            pay_method: "card",    																	// 결제
            merchant_uid: "javaProjectS2_" + new Date().getTime(),  // 상점 고유번호 만들기
            name: '${vo.name}',   																	// 품목
            amount: '${vo.amount}',  											  				// 가격
            buyer_email: '${vo.buyer_email}',  											//이메일
            buyer_name: '${vo.buyer_name}',  												// 이름
            buyer_tel: '${vo.buyer_tel}',  													// 연락처
            buyer_addr: '${vo.buyer_addr}',			  									// 주소
            buyer_postcode: '${vo.buyer_postcode}',  								// 우편번호
          },
          function (rsp) {
        	  if(rsp.success) {
        		  alert("결제가 완료되었습니다.");
        		  location.href="${ctp}/study/payment/paymentOk";
        	  } else {
        		  alert("결제에 실패하셨습니다.");
        		  location.href="${ctp}/study/payment/payment";
        	  }
          }
        );
      //}
    </script>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp" />
<jsp:include page="/WEB-INF/views/include/slide2.jsp" />
<p><br /></p>
<div class="container">
	<p>
		<img src="${ctp}/images/payment.gif" />
	</p>
</div>
<p><br /></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>