<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Vector"%>
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, maximum-scale=1.0, minimum-scale=1.0">
<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/board/W3S/w3css/4/w3.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/board/layouts_main.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/board/view/dropdown_user.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/board/main/dropdown_user.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/board/main/main_alert.css">
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
<%@include file="/board/layouts/Bean.jsp" %>
<div class="mainPage">
	<%@ include file="/board/layouts/sidebar.jsp" %>
    <div class="w3-container">
        <% 
        String type = request.getParameter("type");
        String so = null;
        String sv = null;
            
        if (type == null) {
               type = "자유";
            }
            if (request.getParameter("so") != null && !request.getParameter("so").isEmpty()
            	&& request.getParameter("sv") != null && !request.getParameter("sv").isEmpty())
            {
            	so = (String) request.getParameter("so");
            	sv = (String) request.getParameter("sv");
            }
            BoardMgr bMgr = new BoardMgr();
            commentMgr cMgr = new commentMgr();
        %>
	<div class="type_header">
	  <img src="<%=request.getContextPath()%>/assets/svg/back.svg">
	  <div class="main_header_page">
	  	<div class="main_get_type"><%=type %></div>
	  </div>
	  <br/> 
	</div>
	
<div class="list_header">
	<div class="list_header_items_left">
	     <p class="week-best-font1">주간 조회수 인기글</p>
		<div class="left-widget-wrapper">
		<%
			List<boardBean> w_list1 = bMgr.getBoardListWeek(0);

	        for (int i = 0; i < w_list1.size(); i++) {
	            Long post_no1 = w_list1.get(i).getPost_no();
	            Long commentNum1 = cMgr.totalCommentNumber(post_no1);
	            String post_title1 = w_list1.get(i).getPost_title();
	            if (post_title1.length() > 15) {
	            	post_title1 = post_title1.substring(0, 15) + "..";
	            }
	            Long post_viewcount1 = w_list1.get(i).getPost_viewcount();
	            Long post_likecount1 = w_list1.get(i).getPost_likecount(); 
		%> 
		<div class="left_list">
			<a href="<%=request.getContextPath()%>/board/n/<%=post_no1%>"><%=post_title1%>&nbsp;[<%=commentNum1%>]</a>
			<div class="left_list_right">
			<img src="<%=request.getContextPath()%>/assets/images/board/hand-thumbs-up.svg"><%=post_likecount1%>
			</div> 
		</div><span class="left_list_line"></span>
 	<%  } %>
		</div>
	</div>
	<div class="list_header_items_right">
		<p class="week-best-font2">주간 가장 활발한 인기글</p>
		<div class="right-widget-wrapper">
		<%
			List<boardBean> w_list2 = bMgr.getBoardListWeek(1);
			
	        for (int i = 0; i < w_list2.size(); i++) {
	            Long post_no2 = w_list2.get(i).getPost_no();
	            Long commentNum2 = cMgr.totalCommentNumber(post_no2);
	            String post_title2 = w_list2.get(i).getPost_title();
	            if (post_title2.length() > 15) {
	            	post_title2 = post_title2.substring(0, 15) + "..";
	            }
	            Long post_viewcount2 = w_list2.get(i).getPost_viewcount();
	            Long post_likecount2 = w_list2.get(i).getPost_likecount(); 
		%>
			<div class="right_list">
				<a href="<%=request.getContextPath()%>/board/n/<%=post_no2%>"><%=post_title2 %>&nbsp;[<%=commentNum2%>]</a>
				<div class="left_list_left">
				<img src="<%=request.getContextPath()%>/assets/images/board/hand-thumbs-up.svg"><%=post_likecount2%>
				</div> 
			</div><span class="right_list_line"></span>
		<% } %>
			</div>
		</div>
</div>

<div class="alert_list">
	<div class="alert_list_wrapper">
		<% 
			List<boardBean> alert_list = bMgr.getAlertBoardList(); 
			
			for (int i = 0; i < alert_list.size(); i++){
				Long post_alert_no = alert_list.get(i).getPost_no();
				Long commentNum_alert = cMgr.totalCommentNumber(post_alert_no);
				String post_title_alert = alert_list.get(i).getPost_title();
				Long post_viewcount_alert = alert_list.get(i).getPost_viewcount();
		%>
		<div class="alert_list_main">
			<a href="<%=request.getContextPath()%>/board/n/<%=post_alert_no%>"><%=post_title_alert%>&nbsp;[<%=commentNum_alert%>]</a>
		</div>
		<% } %>
	</div>
</div>
     <%
        int pageSize = 15; // Set your desired page size here
        int pageNum = 1;

        if (request.getParameter("pageNum") != null && !request.getParameter("pageNum").isEmpty()) {
            pageNum = Integer.parseInt(request.getParameter("pageNum"));
        }
		%>
			<script> var pageNum = `<%=pageNum%>` </script>
		<%
        
        Vector<boardBean> vlist = null;
        if(so == null || sv == null) {
        	vlist = bMgr.getBoardList1(pageNum, pageSize);
        } else {
        	vlist = bMgr.getBoardListbySearch(pageNum, pageSize, so, sv);
        }
        int listSize = vlist.size();
        Long post_no, post_writer;
        String post_time, post_title, post_content, post_filePath, post_writer_id, mem_profile_img2;
        Long post_viewcount, post_likecount;
        Long count = 0L;
		
        for (int i = 0; i < vlist.size(); i++) {
            post_no = vlist.get(i).getPost_no();
            //Long commentNum = cMgr.getCommentCount(post_no);
            Long commentNum = cMgr.totalCommentNumber(post_no);
            post_time = vlist.get(i).getPost_time();
            post_title = vlist.get(i).getPost_title();
            post_content = vlist.get(i).getPost_content();
            post_filePath = vlist.get(i).getPost_filePath();
            post_writer = vlist.get(i).getPost_writer();
            mem_profile_img2 = bMgr.findMem_img(post_writer); 
            post_writer_id = bMgr.findUser(post_writer);
            post_viewcount = vlist.get(i).getPost_viewcount();
            post_likecount = vlist.get(i).getPost_likecount(); 
            count++;
        %>
        <script>
        document.addEventListener('DOMContentLoaded', function() {
            getFirstImageLink(`<%=post_content%>`, <%=post_no%>);
        });
        var contextPath = `<%=request.getContextPath()%>`;
        </script>
        <%  String mysqlDatetime = post_time; %>
        <%@include file="/board/layouts/timeCalculate.jsp" %>
       	<div class="board_main_list_wrapper">
        <ul class="w3-ul">
            <li class="w3-bar w3-border">
                <a href="<%=request.getContextPath()%>/board/n/<%=post_no%>">
                    <!-- 이미지를 동적으로 변경할 대상 -->
                    <img id="thumbnail<%=post_no%>" src="<%=request.getContextPath()%>/assets/images/board/default-background.png" 
                    class="w3-bar-item w3 w3-hide-small" style="width:95px; height:80px">
                    <div class="w3-bar-item">
                        <span class="post_post_title" id="post_post_title"><%=post_title%></span>
                        <% if (commentNum > 0) { %>
                        <span class="post_comment_num" id="post_comment_num">[<%=commentNum%>]</span>
                        <% } %>
                        <span class="likeBR2"></span>
                       <div class="post_post_writer_id" id="post_post_writer_id">
	                       	<ul class="post_writer_depth1">
		                       	<li>
			                       	<a href="#">
			                        <% if (mem_profile_img2 != null) { %>
									    <img src="<%=mem_profile_img2%>" style="width:20px;height:auto">
									<% } %><%=post_writer_id%>
									</a>
									<ul class="post_writer_depth2">
										<li><a href="<%=request.getContextPath()%>/search/n/1?so=wr&sv=<%=post_writer_id%>"><span class="view_user_board">작성글 보기</span></a>
									</ul>
								</li>
							</ul>
						</div>
                        <span class="post_post_viewcount" id="post_post_viewcount">조회&nbsp;<%=post_viewcount%></span>
                        <span class="post_post_likecount" id="post_post_likecount"><img src="<%=request.getContextPath()%>/assets/images/board/hand-thumbs-up.svg"><%=post_likecount%></span>
                        <span class="post_Formatted_time" id="post_Formatted_time"><%=formattedDateTime%></span>
                    </div>
                </a>
            </li>
        </ul>
        </div>
        <% } %>
    </div>
</div>
<div class="main_bottom">
    <%
    Vector<boardBean> vlistAll = null;
    if (so == null || sv == null){
    	vlistAll = bMgr.getBoardList1(0, pageSize);
    } else {
    	vlistAll = bMgr.getBoardListbySearch(0, pageSize, so, sv); 
    }
    int listSizeAll = vlistAll.size();

    int totalPages = (int) Math.ceil((double) listSizeAll / pageSize);

    int startPage = ((pageNum - 1) / 5 ) * 5 + 1;
    int endPage = startPage + 4;
    if (endPage > totalPages) {
        endPage = totalPages;
    }
    %>
<div class="Search_items">
	<button class="search_button" id="openModalBtn">
		<img src="<%=request.getContextPath()%>/assets/svg/search.svg" alt="search">search
	</button>
</div>
<% if (so == null || sv == null) { %>
<div class="pagination-flex-container">
    <div class="pagination_previous">
        <% if (startPage > 1) { %>
            <a href="<%=request.getContextPath()%>/page/n/<%=startPage - 1%>"><span class="pagination_side">이전</span></a>
        <% } %>
    </div>
    
    <div class="pagination-container">
        <% for (int i = startPage; i <= endPage; i++) { %>
            <div class="pagination">
                <a href="<%=request.getContextPath()%>/page/n/<%=i%>"><%= i %></a>
            </div>
        <% } %>
    </div>
    
    <div class="pagination_next">
        <% if (endPage < totalPages) { %>
            <a href="<%=request.getContextPath()%>/page/n/<%=endPage + 1 %>"><span class="pagination_side">다음</span></a>
        <% } %>
    </div>
</div>
<!-- if (search is executed) -->
<% } else { %>
	<div class="pagination-flex-container">
    <div class="pagination_previous">
        <% if (startPage > 1) { %>
			<a href="<%=request.getContextPath()%>/search/n/<%=startPage - 1%>?so=<%=so%>&sv=<%=sv%>"><span class="pagination_side">이전</span></a>
        <% } %>
    </div>
    
    <div class="pagination-container">
        <% for (int i = startPage; i <= endPage; i++) { %>
            <div class="pagination">
                <a href="<%=request.getContextPath()%>/search/n/<%=i%>?so=<%=so%>&sv=<%=sv%>"><%= i %></a>
            </div>
        <% } %>
    </div>
    
    <div class="pagination_next">
        <% if (endPage < totalPages) { %>
            <a href="<%=request.getContextPath()%>/search/n/<%=endPage + 1 %>?so=<%=so%>&sv=<%=sv%>"><span class="pagination_side">다음</span></a>
        <% } %>
    </div>
</div>
<% } %>
<!-- modal -->

<div class="modal" id="myModal">
    <div class="modal-content">
        <label for="searchOption" id="searchOption_label">찾기</label>
        <select id="searchOption">
        	<option value="tac">제목 + 내용 </option>
        	<option value="tt">제목</option>
        	<option value="ct">내용</option>
        	<option value="wr">글쓴이</option>
        </select>
        <input type="text" name="search_value" id="search_value_input" placeholder="검색할 내용을 입력하세요">
        <div class="button-container">
            <button onclick="searchAction()">검색</button>
            <button onclick="closeModal()">닫기</button>
        </div>
    </div>
</div>

<%-- <%@include file="/board/view/userDropdown.jsp" %>
 --%><script src="<%=request.getContextPath()%>/assets/js/board/view/dropdown_user.js"></script>
<script src="<%=request.getContextPath()%>/assets/js/board/main/Modal.js"></script>
</div>
