<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.io.*,java.util.*" %>
<%@ page import="javax.servlet.*,javax.servlet.http.*" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, maximum-scale=1.0, minimum-scale=1.0">
<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/board/layout.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/board/boardView.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/board/boardView_m.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/board/view/boardView_modal.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/board/view/boardView_comment.css">
<%@include file="/board/layouts/login_check.jsp" %>
<%@ include file="/board/bootstrap.jsp" %>
<%@include file="/board/layouts/Bean.jsp" %>
<%@include file="/board/layouts/header.jsp" %>
<script src="https://cdnjs.cloudflare.com/ajax/libs/socket.io/3.1.3/socket.io.js"></script>
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
<script src="<%=request.getContextPath()%>/assets/js/board/boardView.js"></script>
<%-- <script src="<%=request.getContextPath()%>/assets/js/board/webSocket/boardMain.js"></script>
 --%>
 <script src="<%=request.getContextPath()%>/assets/js/board/timeCalculate.js"></script>
<%
	Long post_no = Long.parseLong(request.getParameter("post_no"));
	
	BoardMgr bMgr = new BoardMgr();
	boardBean bean = bMgr.getBoard(post_no);
	
    String post_time = null;
    String post_title = null;
    String post_content = null;
    String post_filePath = null;
    String mem_profile_img2 = null;
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
        mem_profile_img2 = bMgr.findMem_img(post_writer); 

    	String session_mem_id = (String)session.getAttribute("mem_id");
    	Long comment_writer_id = bMgr.find_mem_no(session_mem_id);
    	
    	HttpServletRequest requestPage = (HttpServletRequest) pageContext.getRequest();
		
    	String completeUrl = requestPage.getRequestURL().toString();
    	//System.out.println("completeUrl => " + completeUrl);
    	String uri = requestPage.getRequestURI();
    	//System.out.println("uri => " + uri);
    	String baseUrl = completeUrl.substring(0, completeUrl.indexOf(uri));
    	//System.out.println(baseUrl);
    	String CurrentUrl = baseUrl + "/board/n/" + post_no;
%>
<script>
	var contextPath = '<%= request.getContextPath() %>';
</script>
<title><%=post_title %></title>
</head>
<body>

<%  String mysqlDatetime = post_time; %>
<%@include file="/board/layouts/timeCalculate.jsp" %>

<script>
var session_mem_no = <%=comment_writer_id%>;
var post_no = <%=post_no%>
var post_writer = <%=post_writer%>
window.addEventListener('DOMContentLoaded', function(){
	increaseViewcount(<%=post_no%>);
	getCommentList(<%=post_no%>,<%=post_writer%>);
});
</script>
	
<br/><br/>
<div class="container">
<%@include file="/board/layouts/sidebar.jsp" %>
    <div class="row">
        <div class="mainDivid" id="mainDivid-1">
        </div>
        <div class="post-details">
            <table>
                <tr>
                    <td colspan="2">
                    <br/>
                        <h1 class="post_title" id="boardView_post_title"><%= post_title %></h1>
                        <br/>
                    </td>
                </tr>
                <tr>
                    <td>
                        &nbsp;<span class="post_writer_id" id="boardView_post_writer_id">
                         <% if (mem_profile_img2 != null) { %>
						    <img src="<%=mem_profile_img2%>" style="width:20px;height:auto">
						<% } %><%= post_writer_id %></span>
                         &nbsp; | &nbsp; <img src="<%=request.getContextPath()%>/assets/images/board/clock.svg">&nbsp;<%= formattedDateTime %>
                    <br/>
                    </td >
                    
                    <td>
						<div class="icons_in_header" id="icons_in_header">
						    <img src="<%=request.getContextPath()%>/assets/images/board/eye.svg">
						    <div class="viewcount-loading" id="viewcount-loading">
						    	<span class="viewcount-label"><%=post_viewcount%></span>
						    </div>
						    &nbsp;
						    <img src="<%=request.getContextPath()%>/assets/images/board/hand-thumbs-up.svg">
						    <div class="recommend-loading2" id="recommend-loading2" >
						        <span class="recommend-label2"><%=post_likecount%></span>
						    </div>				 
						</div>	
                    </td>            
                </tr>
            </table>
              <div class="under_title">
              <hr>
              <div class="currentPageUrl" id="currentPageUrl" onclick="copyToClipboard('<%=CurrentUrl%>')">	
              	<img src="<%=request.getContextPath()%>/assets/images/board/clipboard.svg">&nbsp;<%=CurrentUrl%>
              </div>
			        <div class="post-content">
			            <div style="text-align: left; width: 105%">
			                <pre><%= post_content %></pre>
			            </div>
			        </div>
              </div>
              </div>
              <br/>
				<div class="recommend-button" id="recommend-button-<%=post_no %>" onclick="increaseRecommendation(<%=post_no%>)" style="max-width:120px">
				    <img class="recommend-icon" src="<%=request.getContextPath()%>/assets/images/board/hand-thumbs-up.svg" alt="Thumbs Up">
				  <div class="recommend-loading" id="recommend-loading-<%=post_no%>">
				    <span class="recommend-label">추천 <%=post_likecount%></span>
				    </div>
				</div>
	      <% if(mem_id.equals(post_writer_id)) { %>
				<div class="button_actions" id="button_actions">
				    <button type="button" class="board_delete" id="board_delete" onclick="board_delete_Action(<%=post_no%>)">
				    <img src="<%=request.getContextPath()%>/assets/svg/trash-fill.svg"></button>
				    <button type="button" class="board_edit" id="board_edit" onclick="board_edit_Action(<%=post_no%>)">
				    <img src="<%=request.getContextPath()%>/assets/svg/pencil-square.svg"></button>
				</div>
              <% } %>
        </div>
    </div>
    <!--  여긴 댓글 구간  -->
<%-- 	 <script src="<%=request.getContextPath()%>/assets/js/board/webSocket/boardView.js"></script>
 --%>	
 <%
 	//만약 로그인한 유저가 글쓴이라면 (이건 게시글 작성자 알림)
 	if(comment_writer_id.equals(post_writer)){
		NotificationMgr nMgr = new NotificationMgr();
		boolean flag = nMgr.updateCommentAlert(post_no, post_writer);
		if (flag == true)
		{
			System.out.println("댓글 읽음 표시");
		}
 	} else {
		NotificationMgr nMgr = new NotificationMgr();
		boolean flag = nMgr.updateCommentAlert2(post_no, comment_writer_id); 
		if (flag == true)
		{
			System.out.println("대댓글 읽음 표시");
		}
 	}
 %>
 
    <div class="board_commentArea" id="board_commentArea">
    	<div class="comment_list_menu" id="comment_list_menu">댓글 목록<span id="list_amount"></span></div>
			<hr class="board_hr_line" id="board_hr_line">
		<div class="board_commentUpdate" id="board_commentUpdate"></div>
		<div class="comment_pagination_container" id="comment_pagination_container"></div>
		<hr/>
		<!-- 댓글 새로고침 버튼 -->
 	<div class="refresh_comment_list_btn">
	<button type="button" class="refresh_comment_list" onclick="getCommentList(<%=post_no%>,<%=post_writer%>)">
		<img src="<%=request.getContextPath()%>/assets/svg/arrow-clockwise.svg"><span class="refresh_text">refresh</span></button>
<!-- 		<span class="refresh_comment_count">0</span>
 -->		</div>
     
			     <!-- 댓글 입력 -->
		<div class="comment_input" id="comment_input">
			<div class="comment_form" id="comment_form">
				<div class="scrollBottom" id="scrollBottom" style="display :none"></div>
				<textarea class="comment_content" name="comment_content" placeholder="댓글을 입력하세요..."></textarea>
					<input type="hidden" name="comment_writer" value="<%=mem_id%>">
					<input type="hidden" name="comment_post_no" value="<%=post_no%>">
					 <input type="hidden" id="parent_comment_no" name="parent_comment_no" value="">
					 <input type="hidden" id="comment_depth" name="comment_depth" value="">
				<button type="button"  class="commentInputAction" onclick="inputComment()">입력</button>
			</div>
			<button type="button" id="addCommentPhotoBtn">이모티콘</button>
			<div class="comment_input_position" id="comment_input_position"></div>
		</div>
		<div class="comment_input_position" id="comment_input_position"></div>
	</div>


<!-- 댓글 구간 끝 -->
<div class="Commentmodal" id="Commentmodal"><br/>
    <div class="Comment-modal-content">
    	<div class="comment-img-contents" id="comment-img-contents"></div>
    	<div class="select_comment_img_folder">
    	 <% 
    			imoticonMgr iMgr = new imoticonMgr();  
    			List<imoticonBean> imoticon_list = new ArrayList<>();
    			int imoticon_pagesize = 5;
    			int imoticon_page_num = 0;
    			String imoticon_name  = null;
    			if (request.getParameter("imoticon_page_num") != null){
    				imoticon_page_num = Integer.parseInt(request.getParameter("imoticon_page_num"));
    			} 
    			imoticon_list = iMgr.getImoticons(mem_no, imoticon_page_num, imoticon_pagesize);
    			if (!imoticon_list.isEmpty()) {
    				System.out.println("imoticon_list.size()"+ imoticon_list.size());
    			for (int i = 0; i < imoticon_list.size(); i++){
    				imoticon_name = imoticon_list.get(i).getImoticon_name();
    		%>
    		<button class="imoticon_name_button" id="imoticon_name_button" data-imoticon="<%=imoticon_name%>">
    			<%=imoticon_name%>
    		</button>
    		<%
    			} 
    		}
    		%>
    	</div>
    	
	  <div id="pagination">
	    <button id="prevPageBtn" onclick="changePage(-1)">Previous</button>
	    <span id="currentPage">1</span>
	    <button id="nextPageBtn" onclick="changePage(1)">Next</button>
	   	<button onclick="closeCommentModal()" id="closeCommentModal_btn">닫기</button>
		</div>
    </div>
</div>
</html>