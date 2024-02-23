<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintWriter" %>
<%@ page import= "java.io.PrintWriter" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, maximum-scale=1.0, minimum-scale=1.0">
<link rel="icon" href="<%=request.getContextPath()%>/assets/images/favicon/acf6e294-49ad-4044-99e6-ae0670342a1d_favicon.ico">
<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/board/write.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/board/layout.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/board/write/uploadProgress.css">
<%@ include file="/board/bootstrap.jsp" %>
<%@ include file="/board/layouts/Bean.jsp" %>
<title>게시글 수정</title>
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
<script src="<%=request.getContextPath()%>/assets/js/board/tinymce/tinymce.min.js"></script>
<script src="<%=request.getContextPath()%>/assets/js/board/edit.js"></script>
<script src="<%=request.getContextPath()%>/assets/js/board/write/uploadProgress.js"></script>
<script>
window.addEventListener('DOMContentLoaded', function() {
    tinymce_editor();
});
var contextPath = <%=request.getContextPath()%>
</script>
<%@include file="/board/layouts/login_check.jsp" %>
</head>
<body>
	<%
    String warningMessage = (String) session.getAttribute("warningMessage");
    if (warningMessage != null) {
        out.println("<script>alert('" + warningMessage + "');</script>");
        session.removeAttribute("warningMessage");
    }
    
		Long post_no = Long.parseLong(request.getParameter("post_no"));
		
		BoardMgr bMgr = new BoardMgr();
		boardBean bean = bMgr.getBoard(post_no);
		
	    String post_time = null;
	    String post_title = null;
	    String post_content = null;
	    String post_filePath = null;
	    Long post_writer = 0L;
	    String post_writer_id = null;
	    Long post_viewcount = 0L;
	    Long post_likecount = 0L;
	    Long count = 0L;

	    	post_no = bean.getPost_no();
	    	post_time = bean.getPost_time();
	    	post_title =  bean.getPost_title();
	    	post_content = bean.getPost_content();
	    	post_filePath = bean.getPost_filePath();
	    	post_writer = bean.getPost_writer();
	    	post_writer_id = bMgr.findUser(post_writer);
	    	post_viewcount = bean.getPost_viewcount();
	    	post_likecount = bean.getPost_likecount(); 
	    
	%>
	<div class="container">
	<%@ include file="/board/layouts/sidebar.jsp" %>
		<div class="row">
			<form method="post" name="board_post" action="<%=request.getContextPath()%>/board/write/editBoardAction" enctype="mulipart/form-data" accept-charset = "utf-8">
				<div class="board_post_wrapper">
					<div class="board_post_inputgroup">
							<input type="text" class="form-control" placeholder="글 제목" name="post_title" maxlength="50" value="<%=post_title%>">
							<div class="board_type_category">
							<p>카테고리</p>
							<select name="board_type" id="board_type">
							    <option value="free" selected>자유</option>
							    <option value="computer">컴퓨터</option>
							    <option value="music">음악</option>
							    <option value="cook">음식/요리</option>
							    <%if (mem_ac.equals("admin")){ %>
							    <option value="alert">공지</option>
							    <% } %>
							</select>
						</div>
						<textarea id="editor" class="form-control" placeholder="글 내용" name="post_content" maxlength="60000" style="height: 350px"><%=post_content%></textarea>
							<input type="file" class="form-control" id="file_input" name="file" accept="image/*,video/*" multiple onchange="fileUploadChange()">
							<div class="file_input_btns">
								<label for="file_input" class="custom-file_input"></label>
								<button type="button" class="file_button1" onclick="deleteAllPreviews()">전체 삭제</button>
								<button type="button" class="file_button2" onclick="deleteSelectionPreviews()">선택 삭제</button>
							</div>
							<button type="button" id="uploadFileBtn" onclick="javascript:uploadFile()">사진 업로드</button>
							<input type="hidden" name="post_time" value="<%=post_time%>">
							<input type="hidden" name ="post_no" value="<%=post_no%>">
							<input type="hidden" name="post_writer" value="<%=mem_id%>">
						<input type="reset" class="board_post_reset_btn1" value="다시쓰기">
						<input type="submit" class="board_post_btn1" value="수정하기">
						</div>
					</div>
				</form>
			</div>
		</div>
</body>
 <div id="uploadModal">
      <div id="modalContent">
          <div class="fileUploadProgress" id="fileUploadProgress">
              <div class="loader">
              <div class="showPercent"></div>
                  <div class="bar">
                      <div class="loaded"  id="progressText"></div>
                  </div>
              </div>
          </div>
      </div>
  </div>
</html>