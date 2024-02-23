<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import = "java.io.PrintWriter" %>
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
<script src="<%=request.getContextPath()%>/assets/js/board/sidebar.js"></script>
<script src="<%=request.getContextPath()%>/assets/js/board/boardMember.js"></script>
<script src="<%=request.getContextPath()%>/assets/js/board/webSocket/sidebar.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/board/sidebar/sidebar.css">
<%@ include file="/board/layouts/Bean.jsp" %>
<script>
    var mem_id = '<%= (String)session.getAttribute("mem_id") %>';
    var contextPath = `<%=request.getContextPath() %>`
</script>
<%
    BoardMgr boardMgr = new BoardMgr();
	Member_Mgr mMgr = new Member_Mgr();
    String memID = (String)session.getAttribute("mem_id");
    Long mem_no = boardMgr.find_mem_no(memID);
    String mem_ac = mMgr.find_mem_ac(mem_no); 
    String mem_profile_img = boardMgr.getProfileImg(mem_no);
%>

<nav class="navbar navbar-light" style="background-color: #fff;">
    <div class="container-fluid">
        <a class="navbar-brand" href="<%=request.getContextPath()%>/">
            <img class="bi me-2" src="<%=request.getContextPath()%>/assets/images/board/main_logo_small.png" style="width:45px; height:45px">nodove
        </a>
        <div class="notification_button">
	<button class="notification_button_alert" onclick="javascript:openNotificationModal();">
		<img src="<%=request.getContextPath()%>/assets/images/board/alert_icon.png" alt="alert">
		<span id="notificationCount" class="notification-count"></span>
	</button>
	</div>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
            aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav">
                <li class="nav-item">
                    <a class="nav-link link-dark" href="<%=request.getContextPath()%>/board/write/post">
                        <svg class="bi me-2" width="16" height="16"><use xlink:href="#speedometer2"></use></svg>
                        글쓰기
                    </a>
                </li>
            </ul>
            <% if(memID != null) { %>
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button"
                            data-bs-toggle="dropdown" aria-expanded="false">
                            <% if (mem_profile_img == null) { %>
                                <img src="<%=request.getContextPath()%>/assets/images/board/human_icon.jpg" alt=""
                                    width="32" height="32" class="rounded-circle me-2" id="profileImage">
                            <% } else { %>
                                <img src="<%=mem_profile_img%>" alt="" width="32" height="32"
                                    class="rounded-circle me-2" id="profileImage">
                            <% } %>
                            <strong><%=(String)session.getAttribute("mem_id")%></strong>
                        </a>
                        <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
                            <li><a class="dropdown-item" href="<%=request.getContextPath()%>/">게시판</a></li>
                            <li><a class="dropdown-item"
                                    href="<%=request.getContextPath()%>/search/n/1?so=wr&sv=<%=(String)session.getAttribute("mem_id")%>">작성글 보기</a></li>
                            <li><a class="dropdown-item"
                                    href="<%=request.getContextPath()%>/mypage/personal.jsp">회원 정보 수정</a></li>
                            <li><a class="dropdown-item" href="javascript:void(0)"
                                    onclick="openPopupMember('<%=request.getContextPath()%>/board/write/memberEdit.jsp?mem_id=<%=(String)session.getAttribute("mem_id")%>')">프로필
                                    이미지 변경</a></li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item" href="javascript:logout();">로그아웃</a></li>
                        </ul>
                    </li>
                </ul>
            <% } %>
        </div>
    </div>
    <%@include file ="/board/layouts/sidebar_alert_modal.jsp" %>
</nav>

