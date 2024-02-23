<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>멤버 이미지 변경</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/board/member_edit.css">
<body>
<%@ include file="/board/layouts/Bean.jsp" %>
	<% 
		String mem_id = request.getParameter("mem_id");
		
		BoardMgr bMgr = new BoardMgr();
		Long mem_no = bMgr.find_mem_no(mem_id);
		String mem_profile_img = bMgr.getProfileImg(mem_no);
		
	%>
	<script>
	    var contextPath = '<%=request.getContextPath()%>';
	    var mem_id = '<%= mem_id %>'; 
	</script>
	<script src="<%=request.getContextPath()%>/assets/js/board/boardMember.js"></script>
	
	
	<div class="member_edit_main">
    <div class="main_title">
        <h1>profile 이미지</h1>
    </div>
        <div class="main_img" id="memberEdit_main_img">
            <label for="imageInput">
                <input type="file" class="imageInput" id="imageInput" style="display: none;" accept="image/*" onchange="Memberupload()">
                <% if(mem_profile_img == null){ %>
                <img src="<%=request.getContextPath()%>/assets/images/board/human_icon.jpg" 
                    height="100" width="100" alt="member_img<%=mem_id%>" role="button" tabindex="0" aria-label="Change Profile Image">
                   <% } else { %>
                 <img src="<%=request.getContextPath()%><%=mem_profile_img %>" 
                    height="100" width="100" alt="member_img<%=mem_id%>" role="button" tabindex="0" aria-label="Change Profile Image">
                    <% } %>
            </label>
        </div>
        <div class="member_name" id="memberEdit_member_name">
            <%=mem_id%>
        </div>
</div>

</body>
</html>