<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 등록 페이지</title>
<% String cPath = request.getContextPath(); %>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
<%@include file="/board/layouts/login_check.jsp" %>
		
<div class="container">
	<form name="postFrm" method="post" action="postAction.jsp" enctype="multipart/form-data">
		<table class="table" style="text-align: center; border: 1px solid #ddddd">
					<thead>
							<tr>
								<th colspan="2" style="background-color: #2e8b57; text-align: center"> 게시글 작성 </th>
							</tr>
					</thead>
					<tbody>
						<tr>
							<td>
								<input type="text" class="form-control" placeholder="게시글 제목" name="subject" maxlength="50">
							</td>
						</tr>
						<tr>
							<td colspan="5">
								<textarea id="editor" class="form-control" placeholder="글 내용" name="content" maxlength="50000">
								</textarea>
							</td>
						</tr>
						<tr>
							<td>
								<input type="hidden" class="form-control" name="mem_id" value="<%=mem_id%>">
							</td>
						</tr>
					</tbody>
			</table>
	</form>
</div>


<script src="<%=cPath %>/js/tinymce/tinymce.min.js"></script>
<script>
tinymce.init({
    selector: '#editor',
    forced_root_block: '',
    height: 500, // 원하는 편집기 높이 설정
    menubar: false, // 기본 메뉴바 제거
    plugins: 'textcolor image link | image | media | link',
    toolbar: 'undo redo | formatselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist | outdent indent | forecolor backcolor | image | media | link',
    images_upload_url: '/usr/local/tomcat/webapps/downloadfile',
    images_upload_base_path: '/usr/local/tomcat/webapps/downloadfile',
	relative_urls: false, // 상대 URL 사용 비활성화
    remove_script_host: false, // 스크립트 호스트 제거 비활성화
	content_css: '<%=cPath%>/css/custom_tinymce.css',
	setup: function (editor) {
        editor.on('change', function () {
            // 에디터 내용이 변경될 때 호출되는 함수
            adjustYouTubeIframes(editor);
        });
    }
  });

  function adjustYouTubeIframes(editor) {
    // 에디터 내용에서 모든 YouTube iframe을 선택
    var iframes = editor.dom.select('iframe[src*="youtube.com"]');
    
    for (var i = 0; i < iframes.length; i++) {
        var iframe = iframes[i];
        iframe.setAttribute('width', '100%'); // 너비를 100%로 설정
        iframe.removeAttribute('height'); // 높이 속성 제거 (반응형으로 조정)
    }
  }
</script>

	<script src="http://code.jquery.com/jquery-3.1.1.min.js"></script>
	<script src="js/bootstrap.js"></script>
</body>
</html>