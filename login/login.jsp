<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>로그인</title>
<link rel="icon" href="<%=request.getContextPath()%>/assets/images/favicon/acf6e294-49ad-4044-99e6-ae0670342a1d_favicon.ico">
<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/login/login.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/login/login_register.css" type="text/css">

</head>
<div class="changing-title"><h1>Hello</h1></div>
<body>
</body>
    <div class="login-wrapper">
        <h2>Login</h2>
        <form method="post" action="<%=request.getContextPath()%>/login/loginAction" id="login-form">
            <input type="text" name="mem_login_id" placeholder="Email / Id">
            <input type="password" name="mem_pw" placeholder="Password">
            <label for="remember-check">
                <input type="checkbox" id="remember-check">
				<span class="remember-check-title">아이디 저장하기</span>
            </label>
            <a href="<%=request.getContextPath()%>/register" class="register-link">회원가입</a>
            <input type="submit" value="Login">
        </form>
    </div>
<script src="<%=request.getContextPath()%>/assets/js/login/login.js"></script>
</html>