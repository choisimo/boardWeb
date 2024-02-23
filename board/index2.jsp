<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/board/layout.css">
<script src="<%=request.getContextPath()%>/assets/js/board/board.js"></script>
<%@ include file="/board/layouts/login_check.jsp" %>
<%@ include file="/board/bootstrap.jsp" %>
<%@ include file="/board/layouts/header.jsp" %>
</head>
<body>  
	<div class="mainContent" id="mainContent">
		<%@ include file="/board/layouts/main_card.jsp" %>
	</div>
</body>
</html>