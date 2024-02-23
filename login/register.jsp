<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="icon" href="<%=request.getContextPath()%>/assets/images/favicon/acf6e294-49ad-4044-99e6-ae0670342a1d_favicon.ico">
<title>회원가입</title>
<script> var contextPath = '<%=request.getContextPath()%>';</script>
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/login/register.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/login/login_register.css" type="text/css">
<script src="<%=request.getContextPath()%>/assets/js/login/register.js"></script>
</head>
<div class="changing-title"><h1>Hello</h1></div>
<body>
    <div class="login-wrapper">
        <h2>Register</h2>
        <form method="post" action="<%=request.getContextPath()%>/register/registerAction" id="login-form" accept-charset="UTF-8">
            <div class="main_img" id="memberEdit_main_img">
			<label for="imageInput" class="file-input-label">
			    <img src="<%=request.getContextPath()%>/assets/images/board/human_icon.jpg" 
			        height="100" width="100" alt="member_img" role="button" tabindex="0" aria-label="Change Profile Image">
			    <input type="file" class="imageInput" id="imageInput" style="display:none"accept="image/*" onchange="Memberupload()">
			</label>
        </div>
            <input type="text" name="mem_id" placeholder="id  (게시판에서 사용할 닉네임)">
            <input type="text" name="login_id" placeholder=" login_id (로그인시 사용됩니다.)">
            <input type="text" name="mem_name" placeholder="name">
            <input type="password" name="mem_pw" placeholder="Password">
			    <label for="remember-check">
			        <span class="remember-check-title">
			            <a href="<%=request.getContextPath()%>/login">로그인</a>
			            <a href="<%=request.getContextPath()%>/pwgen">비밀번호 생성기</a>
			        </span>
			    </label>
            <input type="submit" value="Register">
        </form>
    </div>
</body>
<script src="<%=request.getContextPath()%>/assets/js/login/login.js"></script>
</html>