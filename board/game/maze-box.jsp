<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>화살표 탈출</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/game/maze_box.css">
</head>
<body>
<div id='container'>
  <div id='maze_box'>
    <div id='end'></div>
    <div id='ball'></div>      
  </div>  
  <div id='tracker'></div>  
</div>
<script src="<%=request.getContextPath()%>/assets/js/game/maze_box.js"></script>
</body>
</html>