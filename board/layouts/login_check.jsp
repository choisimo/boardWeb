<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintWriter" %>

<%
String mem_id = null;
if (session.getAttribute("mem_id") != null) {
    mem_id = (String) session.getAttribute("mem_id");
} else {
    PrintWriter script = response.getWriter();
    script.println("<script>");
    script.println("alert('로그인이 필요한 페이지입니다!');");
    script.println("location.href='" + request.getContextPath() + "/login';");
    script.println("</script>");
}
%>
