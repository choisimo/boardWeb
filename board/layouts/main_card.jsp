<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.Vector"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, maximum-scale=1.0, minimum-scale=1.0">
<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/board/main/main_card.css">
</head>
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
<%@include file="/board/layouts/Bean.jsp" %>
<%@ include file="/board/layouts/sidebar.jsp" %>

<body>
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
        %>



        <% 
        int pageSize = 10; // Set your desired page size here
        int pageNum = 1;

        if (request.getParameter("pageNum") != null && !request.getParameter("pageNum").isEmpty()) {
            pageNum = Integer.parseInt(request.getParameter("pageNum"));
        }
		%>
			<script> var pageNum = `<%=pageNum%>` </script>
		<%
        BoardMgr bMgr = new BoardMgr();
        commentMgr cMgr = new commentMgr();
        
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

<div class="support-grid"></div>
 	 <div class="band">
    	<div class="item-1">
          	<a href="https://design.tutsplus.com/articles/international-artist-feature-malaysia--cms-26852" class="card">
            	<div class="thumb" style="background-image: url(https://s3-us-west-2.amazonaws.com/s.cdpn.io/210284/flex-1.jpg);"></div>
            	<article>
              	<h1>International Artist Feature: Malaysia</h1>
              	<span>Mary Winkler</span>
            	</article>
        	  </a>
    	</div>
	<% } %>
</div>


<div>
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
<script src="<%=request.getContextPath()%>/assets/js/board/main/Modal.js"></script>
</div>


</body>
</html>