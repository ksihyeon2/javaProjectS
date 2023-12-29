<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<% pageContext.setAttribute("newLine", "\n"); %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>boardContent.jsp</title>
	<jsp:include page="/WEB-INF/views/include/bs4.jsp" />
	<style>
		th {
			text-align : center;
			background-color : #eee;
		}
	</style>
	<script>
		'use strict';
		/* 좋아요 증가(중복 불허) */
		function goodCheck() {
			$.ajax({
				url : "boardGoodCheck",
				type : "post",
				data : {idx:${vo.idx}},
				success : function(res) {
					if(res == "0") {
						alert("이미 좋아요 버튼을 클릭하셨습니다.");
					} else {
						location.reload();
					}
				},
				error : function() {
					alert("전송 오류");
				}
			});
		}
		
		/* 좋아요 증가(중복 허용) */
		function goodCheckPlus() {
			$.ajax({
				url : "boardGoodCheckPlusMinus",
				type : "post",
				data : {
					idx:${vo.idx},
					goodCnt : +1
			},
				success : function() {
						location.reload();
				},
				error : function() {
					alert("전송 오류");
				}
			});
		}
		
		/* 좋아요 감소(중복 허용) */
		function goodCheckMinus() {
			$.ajax({
				url : "boardGoodCheckPlusMinus",
				type : "post",
				data : {
					idx:${vo.idx},
					goodCnt : -1
			},
				success : function() {
						location.reload();
				},
				error : function() {
					alert("전송 오류");
				}
			});
		}
		
		function boardDelete() {
			let ans = confirm("해당 게시물을 삭제하시겠습니까?");
			if(ans) {
				location.href="boardDelete?idx=${vo.idx}&pag=${pag}&pageSize=${pageSize}";
			}
		}
		
		/* 원본글에 대한 댓글 달기 */
		function replyCheck() {
			let content = $("#content").val();
			if(content.trim() == "") {
				alert("댓글을 입력하세요.");
				$("#content").focus();
				return false;
			}
			
			let query = {
					boardIdx:${vo.idx},
					mid:'${sMid}',
					nickName:'${sNickName}',
					hostIp:'${pageContext.request.remoteAddr}',
					content:content
			}
			$.ajax({
				url : "${ctp}/board/boardReplyInput",
				type : "post",
				data : query,
				success : function(res) {
					if(res == "1") {
						alert("댓글이 입력되었습니다.");
						location.reload();
					} else {
						alert("댓글 입력에 실패하셨습니다.");
					}
				},
				error : function() {
					alert("전송 오류");
				}
			});
		}
		
		/* 댓글 삭제하기 */
		function replyDelete(idx) {
			let ans = confirm("댓글을 삭제하시겠습니까?");
			if(!ans) {
				return false;
			}
			
			$.ajax({
				url : "boardReplyDelete",
				type : "post",
				data : {idx:idx},
				success : function(res) {
					if(res == "1") {
						alert("댓글이 삭제되었습니다.");
						location.reload();
					}
					else {
						alert("댓글 삭제에 실패하셨습니다.");
					}
				},
				error : function() {
					alert("전송 오류");
				}
			});
		}
		
		$(function(){
			$(".replyCloseBtn").hide();
		});
		
		/* 답글 테이블 보여주기 */
		function replyShow(idx){
			$("#replyShowBtn"+idx).hide();
			$("#replyCloseBtn"+idx).show();
			$("#replyDemo"+idx).slideDown(200);
		}
		
		/* 답글 테이블 닫기 */
		function replyClose(idx){
			$("#replyShowBtn"+idx).show();
			$("#replyCloseBtn"+idx).hide();
			$("#replyDemo"+idx).slideUp(100);
		}
		
		/* 답글 입력 처리 */
		function replyCheckRe(idx,re_step,re_order){
			let content = $("#contentRe"+idx).val();
			if(content.trim() == ""){
				alert("답변글을 입력하세요.");
				$("#contentRe"+idx).focus();
				return false;
			}
			
			let query = {
					boardIdx:${vo.idx},
					re_step : re_step,
					re_order : re_order,
					mid:'${sMid}',
					nickName:'${sNickName}',
					hostIp:'${pageContext.request.remoteAddr}',
					content:content
			}
			
			$.ajax({
				url : "${ctp}/board/boardReplyInputRe",
				type : "post",
				data : query,
				success : function(res) {
					if(res == "1") {
						alert("답글이 입력되었습니다.");
						location.reload();
					} else {
						alert("답글 입력에 실패했습니다.");
					}
				},
				error : function() {
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
		<table class="table table-borderless m-0 p-0">
			<tr>
				<td><h2 class="text-center">글 내 용 보 기</h2></td>
			</tr>
		</table>
		<table class="table table-bordered">
			<tr>
				<th>작성자</th>
				<td>${vo.nickName}</td>
				<th>작성일</th>
				<td>${fn:substring(vo.WDate,0,16)}</td>
			</tr>
			<tr>
				<th>글제목</th>
				<td colspan="3" class="text-left">${vo.title}</td>
			</tr>
			<tr>
				<th>전자메일</th>
				<td>
				 <c:if test="${empty vo.email}">없음</c:if> 
				 <c:if test="${!empty vo.email}">${	vo.email}</c:if> 
				</td>
				<th>조회수</th>
				<td>${vo.readNum}</td>
			</tr>
			<tr>
				<th>홈페이지</th>
				<td>
					<c:if test="${empty vo.homePage || (fn:indexOf(vo.homePage,'http://') == -1 && fn:indexOf(vo.homePage,'https://') == -1) || fn:length(vo.homePage) <= 10}">없음</c:if>
        	<c:if test="${!empty vo.homePage && (fn:indexOf(vo.homePage,'http://') != -1 || fn:indexOf(vo.homePage,'https://') != -1) && fn:length(vo.homePage) > 10}"><a href = "${vo.homePage}" target="_blank">${vo.homePage}</a></c:if>
				</td>
				<th>좋아요수</th>
				<td><font color="red"><a href="javascript:goodCheck()">❤</a></font>(${vo.good}) / <a href="javascript:goodCheckPlus()">👍</a><a href="javascript:goodCheckMinus()">👎</a> </td>
			</tr>
			<tr>
				<th>글내용</th>
				<!-- 글 내용 replace로 위에서 newLine 설정 후 엔터키는 <br />로 설정하기 -->
				<td colspan="3" style="height:220px">${fn:replace(vo.content,newLine, "<br />")}</td>
			</tr>
		</table>
		<table class="table table-borderless m-0 p-0">
			<tr>
				<td class="text-left">
					<c:if test="${param.flag != 'search' }"><input type="button" value="돌아가기" onclick="location.href='boardList?pag=${param.pag}&pageSize=${param.pageSize}';" class="btn btn-info" /> &nbsp;</c:if>
					<c:if test="${param.flag == 'search' }"><input type="button" value="돌아가기" onclick="location.href='boardSearch?pag=${param.pag}&pageSize=${param.pageSize}&search=${param.search}&searchString=${param.searchString}';" class="btn btn-info" /> &nbsp;</c:if>
					<c:if test="${vo.mid == sMid || sLevel == 0}">
						<c:if test="${vo.mid == sMid}">
							<input type="button" value="수정하기" onclick="location.href='boardUpdate?idx=${vo.idx}&pag=${pag}&pageSize=${pageSize}';" class="btn btn-success" /> &nbsp;
						</c:if>
						<input type="button" value="삭제하기" onclick="boardDelete()" class="btn btn-danger" />
					</c:if>
				</td>
				<td class="text-right">
					<c:if test="${vo.mid != sMid}">
						<button type="button" class="btn btn-danger" data-toggle="modal" data-target="#myModal">신고하기</button>
					</c:if>
				</td>
			</tr>
		</table>
		<br />
		<!-- 이전글/다음글 처리 -->
		<table class="table table-borderless">
	    <tr>
	      <td>
	      	<c:if test="${!empty nextVo.title}"> 
	        	☝<a href="boardContent?idx=${nextVo.idx}&pag=${pag}&pageSize=${pageSize}">다음글 : ${nextVo.title}</a><br/>
	       	</c:if>
	       	<c:if test="${!empty preVo.title}">
	        	👇<a href="boardContent?idx=${preVo.idx}&pag=${pag}&pageSize=${pageSize}">이전글 : ${preVo.title}</a><br/>
	     		</c:if>
	      </td>
	    </tr>
		</table>
	</div>
	<!-- 댓글 처리 -->
	<div class="container">
	<!-- 댓글 리스트 보여주기 -->
		<table class="table table-hover">
			<tr  class="text-center">
				<th>작성자</th>
				<th>댓글 내용</th>
				<th>작성일</th>
				<th>접속IP</th>
				<th></th>
			</tr>
			<c:forEach var="replyVo" items="${replyVos}" varStatus="st">
				<tr>
					<td >
						<c:if test="${replyVo.re_order >= 1}">
							<c:forEach var="i" begin="1" end="${replyVo.re_step}">&nbsp;&nbsp;</c:forEach> └
						</c:if>
							${replyVo.nickName}
						<c:if test="${replyVo.mid == sMid || sLevel == 0}">
							(<a href="javascript:replyDelete(${replyVo.idx})" title="삭제">❌</a>)						
						</c:if>
					</td>
					<td class="text-left">${fn:replace(replyVo.content,newLine,"<br />")}</td>
					<td class="text-center">${fn:substring(replyVo.WDate,0,10)}</td>
					<td class="text-center">${replyVo.hostIp}</td>
					<td>
						<a href="javascript:replyShow(${replyVo.idx})" id="replyShowBtn${replyVo.idx}" class="badge badge-secondary">답글</a> 
						<a href="javascript:replyClose(${replyVo.idx})" id="replyCloseBtn${replyVo.idx}" class="badge badge-danger replyCloseBtn">닫기</a> 
					</td>
				</tr>
				<tr>
					<td colspan="5" class="m-0 p-0">
						<div id="replyDemo${replyVo.idx}" style="display:none">
							<table class="table table-center">
								<tr>
									<td style="width:85%" class="text-left">
										답글 입력
										<textarea rows="4" name="contentRe" id="contentRe${replyVo.idx}" class="form-control">@${replyVo.nickName}</textarea>
									</td>
									<td style="width:15%">
										<br />
										<p style="font-size:13px">작성자 : ${sNickName}</p>
										<p><input type="button" value="답글달기" onclick="replyCheckRe(${replyVo.idx},${replyVo.re_step},${replyVo.re_order})" class="btn btn-info btn-sm" /></p>
									</td>
								</tr>
							</table>
						</div>
					</td>
				</tr>
				<tr><td colspan="5" class="m-0 p-0"></td></tr>
			</c:forEach>
		</table>
		
		<!-- 댓글 입력창 -->
		<form name="replyForm">
			<table class="table table-center">
				<tr>
					<td style="width:85%" class="text-left">
						댓글 입력
						<textarea rows="4" name="content" id="content" class="form-control"></textarea>
					</td>
					<td style="width:15%">
						<br />
						<p style="font-size:13px">작성자 : ${sNickName}</p>
						<p><input type="button" value="댓글달기" onclick="replyCheck()" class="btn btn-info btn-sm" /></p>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<p><br /></p>
	<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>	