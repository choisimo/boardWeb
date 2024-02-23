
function copyToClipboard(currentUrl) {

    // Create a temporary input element
    var tempInput = document.createElement('input');
    tempInput.value = currentUrl;

    // Append the input element to the DOM
    document.body.appendChild(tempInput);

    // Select the text inside the input element
    tempInput.select();
    tempInput.setSelectionRange(0, 99999); /*For mobile devices*/

    // Copy the selected text to the clipboard
    document.execCommand('copy');

    // Remove the temporary input element
    document.body.removeChild(tempInput);

    // Alert the user (you can customize this part)
    alert('해당 게시글 주소가 복사되었습니다. ' + "\n" + currentUrl);
}

        





function increaseViewcount(post_no){
		let url = "./increaseViewcount"
	if (location.href.includes("/n")) {
		//url = "https://nodove.kro.kr/board/view/increaseViewcount"
		url = contextPath + "/board/view/increaseViewcount"
	}
	    $.ajax({
        type: "POST",
        url: url,
        data: { post_no: post_no },
        dataType: "json",
        cache: false, //한번 cache 막아봅시다.
        success: function (response) {
            if (response && response.post_viewcount !== undefined) {
                // 성공적으로 서버에서 응답을 받으면 추천 수 업데이트

                updatePostViewcount(post_no, response.post_viewcount);
            } else {
                console.log("올바르지 않은 JSON format 입니다.");
            }
        },
        error: function (xhr, status, error) {
            console.error("추천에 실패했습니다.:", status, error);
        }
    });
}

  
function increaseRecommendation(post_no) {
			let url = "./increaseRecommendation"
	if (location.href.includes("/n")) {
		//url = "https://nodove.kro.kr/board/view/increaseRecommendation"
		url = contextPath + "/board/view/increaseRecommendation"
	}
	var hasRecommend = localStorage.getItem('recommend_' + post_no);
	if(!hasRecommend) {
		var button = document.getElementById('recommend-button-' + post_no);
		button.onclick = null;
		button.style.cursor = 'not-allowed';

    $.ajax({
        type: "POST",
        url: url,
        data: { post_no: post_no },
        dataType: "json",
        success: function (response) {
            if (response && response.post_likecount !== undefined) {
                // 성공적으로 서버에서 응답을 받으면 추천 수 업데이트
                updateRecommendationCount(post_no, response.post_likecount);
            } else {
                console.log("올바르지 않은 JSON format 입니다.");
            }
        },
        error: function (xhr, status, error) {
            console.error("추천에 실패했습니다.:", status, error);
        }
    });
    
    localStorage.setItem('recommend_' + post_no, true);
   	} else {
		   alert("이미 추천한 게시글 입니다.");
	   }
}



function updateRecommendationCount(post_no, post_likecount) {
    // 새로운 추천 수를 화면에 업데이트
               var HTML_likecount = 
               `	<span class="recommend-label">추천 ${post_likecount} </span>`;            
                var HTML_likecount2 = 
               `	<span class="recommend-label2">${post_likecount} </span>`;            
                // Update the content of the main element
                $('.recommend-loading').html(HTML_likecount);
                $('.recommend-loading2').html(HTML_likecount2);
}


function updatePostViewcount(post_no, post_viewcount) {
			var HTML_viewcount =
			`<span class="viewcount-label id="viewcount-label">${post_viewcount} </span>`;
			$('viewcount-loading').html(HTML_viewcount);
}




// 여기는 댓글
var currentPage = 999999;
var pageSize = 10;

function getCommentList(post_no, post_writer)
{
	var totalCommentCount = 0;
	var commentList = 0;
	let url = "./getCommentList"
	if (location.href.includes("/n")) {
		url = contextPath + "/board/view/getCommentList"
	}
	 $.ajax({
        type: "POST",
        url: url,
        data: 
        { 
			post_no: post_no,
			page: currentPage,
			pageSize : pageSize
         },
        dataType: "json",
        cache: false, //한번 cache 막아봅시다.
        success: function (response) {
			
            if (response) {
                totalCommentCount = response.totalCommentCount;
                totalCommentNum = response.totalCommentNum;
                commentList = response.commentList;
                updateCommentList(post_no, commentList , post_writer, totalCommentCount, totalCommentNum);
                
            } else {
				updateCommentList(post_no, [], post_writer);
                console.log("올바르지 않은 JSON format 입니다.");
            }
        },
        error: function (xhr, status, error) {
            console.error("댓글 목록 로딩 실패.:", status, error);
        }
    });
}


function updateCommentList(post_no, commentList, post_writer, totalCommentCount, totalCommentNum) {
	
    var commentArray = commentList.map(commentString => JSON.parse(commentString));

    var HTML_commentList = commentArray.map(function (comment) {

	
	//var commentWriterCheck = (String(comment.comment_writer) === String(post_writer)) ? '글쓴이': '';

	var commentWriterCheck = commentCheckWriter(comment.comment_writer, post_writer);
	
	var decodedContent;
	var formattedDateTime;
	var comment_writer_id = comment.comment_writer_id;
	formattedDateTime = formatDateTimeDifference(comment.comment_time);
	
	try {
		  decodedContent = decodeURIComponent(comment.comment_content, "UTF-8");
	} catch (error) {
		console.log("내용 디코딩 오류 : ", error);
		decodedContent = "";
	}

	//대댓글이 있는 댓글일 경우 대댓글 리스트 가지고 오기
	if (comment.comment_isParent == 1){
		getRepliesList(comment.comment_no, post_writer, comment_writer_id);
	}
	//대댓글 구분 
	var commentIndentation = "";
	if (comment.comment_parent_id != null && comment.comment_depth != null)
		{
			commentIndentation = "";
		
			for (var i = 0; i < comment.comment_depth; i++){
		         commentIndentation += " "; 
		}
	}
return `
	    <div class="comment_list_Map" id="comment_list_Map">
	        <p>
	            <span class="comment_writer_id"><img src="${comment.comment_img_path}" style="width:20px;">${comment.comment_writer_id}</span>
	            <span class="commentWriterCheck">${commentWriterCheck}</span>
	            <span class="date">${formattedDateTime}</span>
	            
	          	<div class="comment_actions">
		      	   <button type="button" class="comment_recomment_button" onclick="comment_recomment(${post_no}, ${comment.comment_no}, 
		            '${post_writer}', '${comment_writer_id}', ${comment.comment_depth})">
		                <img src='${contextPath}/assets/svg/chat-right-text.svg'>
		            </button>
	            ${Number(comment.comment_writer) === Number(session_mem_no) ? `
                    <button type="button" class="comment_delete_button" onclick="comment_deleteAction(${post_no}, ${comment.comment_no}, ${post_writer})">
                        <img src='${contextPath}/assets/images/board/x-square.svg'>
                    </button>
		                ` : ''}
		        </div>
	        </p>
	        <div contenteditable="${comment.editing}" class="editable-comment" id="editable-comment-${comment.comment_no}">${commentIndentation}${decodedContent}</div>
	    </div><div class="comment_${comment.comment_no}" id="comment_${comment.comment_no}"></div><hr/>
	    <div id="#comment_${comment.comment_no}"></div>
`;

    }).join(''); 
    
    generatePaginationButtons(totalCommentCount);
    $('#list_amount').text(` (${totalCommentNum}개)`);
    $('#board_commentUpdate').html(HTML_commentList);
}

function commentCheckWriter(comment_writer, post_writer)
{
	const result = (parseInt(comment_writer, 10) === parseInt(post_writer, 10)) ? '글쓴이' : '';
	return result;
}
function generatePaginationButtons(totalCommentCount) {
    totalPages = Math.ceil(totalCommentCount / pageSize);
	
    var paginationContainer = $('#comment_pagination_container');

    paginationContainer.html('');

    var buttonsToShow = 5; // Number of buttons to show

    var startPage = currentPage;
    var endPage = currentPage + 4;
	
	if (totalPages < endPage){
		endPage = totalPages;
	}
	if (startPage < 1) {
		startPage = 1;
	}
    if (endPage - startPage < buttonsToShow - 1) {
        startPage = Math.max(1, endPage - buttonsToShow + 1);
    }
	//first page button
	if (startPage > 1) {
		paginationContainer.append(createPageButton(1, '처음'));
	}
    // Previous Button
    if (startPage > 1) {
        paginationContainer.append(createPageButton(startPage - buttonsToShow, '이전'));
    }

    for (var i = startPage; i <= endPage; i++) {
        paginationContainer.append(createPageButton(i));
    }

    // Next Button
    if (endPage < totalPages) {
        paginationContainer.append(createPageButton(endPage + 1, '다음'));
    }
    //Last Page Button
    if (endPage < totalPages){
		paginationContainer.append(createPageButton(totalPages, '끝'));
	}
}

function createPageButton(page, text) {
    text = text || page; // Use page number if text is not provided
    return $('<button>', {
        text: text,
        click: function () {
            currentPage = page;
			
			
			$('#board_commentUpdate').empty();
            getCommentList(post_no, post_writer);
        }
    }); 
}




function handleCommentEditFocus(element, post_no, comment_no, commentContent) {
    element.contentEditable = true;
    element.innerText = commentContent;
    element.focus();
}


function getRepliesList(comment_no, post_writer, comment_writer_id){
	$.ajax({
		type : "POST",
		url : contextPath + "/board/view/getRepliesList",
		data : {
			parent_no : comment_no
		},
		dataType: "json",
		success: function(response){
			if(response){
			var HTML_replies = generateRepliesHTML(response, post_writer, comment_writer_id);
			
		    $(`#comment_${comment_no}`).append(HTML_replies);
			
			} else {
				console.log("올바르지 않은 JSON format 입니다 (대댓글)");
			}
		},
		error: function (xhr, status, error) {
			console.error("Error Fetching replies : ", status, error);
		}
	});
}
function generateRepliesHTML(replies, post_writer, comment_writer_id) {
    var HTML_replies = replies.map(function (reply) {
	var commentWriterCheck = (String(reply.comment_writer) === String(post_writer)) ? '글쓴이': '';

       return `
            <div class="reply">
            	<div class="reply-header">
            		<span class="reply_parent_id"><img src="${contextPath}/assets/svg/arrow-return-right.svg"></span><br/>
	                <span class="reply_writer_id"><img src="${reply.img_path}" style="width:20px;">${reply.comment_writer_id}</span>
	                <span class="replyWriterCheck">${commentWriterCheck}</span>
                <br/>
                <span class="reply-time">${formatDateTimeDifference(reply.comment_time)}</span>
                </div>
                <span class="reply-content">${decodeURIComponent(reply.comment_content, "UTF-8")}</span>
                
                <div class="reply_actions">
                    ${Number(reply.comment_writer) === Number(session_mem_no) ? `
                        <button type="button" class="reply_delete_button" onclick="comment_deleteAction(${post_no}, ${reply.comment_no}, ${post_writer})">
                            <img src='${contextPath}/assets/images/board/x-square.svg'>
                        </button>
                    ` : ''}
                </div>
            </div>
        `;
    }).join('');

    return HTML_replies;
}

function inputComment() {
    var commentContent = $('.comment_content').val().trim(); 
    var commentWriter = $('[name="comment_writer"]').val();
    var commentPostNo = $('[name="comment_post_no"]').val();
    var parentCommentNo = $('[name="parent_comment_no"]').val(); // Added line to get parent comment number
	var comment_depth = $('[name="comment_depth"]').val();
	
    if (commentContent.length > 1000) {
    alert("댓글은 1000자 이하로 작성해야 합니다.");
    return;
    }
    
    var encodedContent = encodeURIComponent(commentContent);

    if (commentContent === "") {
        alert("댓글 내용을 입력하세요.");
        return; 
    }
    
    var forbidden = 
    ['<img', 'style', 'src', 'href', '씨발', '시발', '닥쳐', 'fuck', '병신', '개새끼', 'ㅅㅂ', 'ㅄ', 'ㅂㅅ','img src'];
    
    if (!commentContent.includes("/downloadfile2/commentImg/")){
	    if (forbidden.some(word => commentContent.includes(word))){
			alert("고양이의 말 : 착한 말 쓰세요 ^^");
			return;
		}
    }
    
    let url = "./inputComment"
	if (location.href.includes("/n")) {
		//url = "https://nodove.kro.kr/board/view/inputComment"
		url = contextPath + "/board/view/inputComment"
	}
		
    $.ajax({
        type: "POST",
        url: url,
        data: {
            comment_content: encodedContent,
            comment_writer: commentWriter,
            comment_post_no: commentPostNo,
            parent_comment_no: parentCommentNo,
            comment_depth : comment_depth
        },
        dataType: "json",
        cache: false,
        success: function (response) {
			var commentNo = response.commentNo;
			
			setTimeout(function () {
           		getCommentList(commentPostNo, commentWriter);
			},170);
			
            commentAlert(commentPostNo, commentWriter, commentNo, parentCommentNo, comment_depth);
            
			$('.comment_content').val('');
			$('html, body').animate({ scrollTop: $(document).height() }, 'slow');
/*     		  $('#scrollTop').css('display', 'block').animate({ scrollTop: $('#scrollTop').offset().top }, 'slow');
*//*	     	var currentScrollTop = $(window).scrollTop();
	        var newScrollTop = currentScrollTop + 200;
	        $('html, body').animate({ scrollTop: newScrollTop }, 'slow');*/
	      //window.location.href = contextPath + `/board/view/boardView.jsp?post_no=${commentPostNo}#comment_input_position`;
        },
        error: function (xhr, status, error) {
            console.error("댓글 입력 실패:", status, error);
            console.log("Server response:", xhr.responseText);
        }
    });
}

var isReplying = false;
// 대댓글 구현
function comment_recomment(post_no, parent_comment_no, post_writer, parent_comment_writer_id, comment_depth) {

    var commentInputDiv = document.getElementById('comment_input');
    
    if (commentInputDiv) {
        commentInputDiv.style.display = 'block';

        var parentCommentInput = document.getElementById('parent_comment_no');
        var comment_depthInput = document.getElementById('comment_depth');
        parentCommentInput.value = parent_comment_no;
        comment_depthInput.value = parseInt(comment_depth) + 1;

		var commentInputPosition = document.getElementById('comment_input_position');
		commentInputPosition.innerHTML = `대댓글 작성 중 :  ${parent_comment_no}번 (작성자 : ${parent_comment_writer_id})
		<button class="commentCancelAction" id="commentCancelAction" onclick="cancelReply()">취소</button>`;
				
        commentInputDiv.scrollIntoView({ behavior: 'smooth' });
    } else {
        console.error('comment_input not found');
    }
}

function cancelReply() {
    var cancelReplyButton = document.querySelector('.commentCancelAction');

    if (cancelReplyButton) {

        var parentCommentInput = document.getElementById('parent_comment_no');
        parentCommentInput.value = '';

        var commentInputPosition = document.getElementById('comment_input_position');
        commentInputPosition.innerHTML = '';

        isReplying = false; // Reset the replying state

        var commentContentTextarea = document.querySelector('.comment_content');
        if (commentContentTextarea) {
            commentContentTextarea.value = '';
        }
    } else {
        console.error('comment_input or commentCancelAction not found');
    }
}

// Function to check the replying state
function isUserReplying() {
    return isReplying;
}


// board delete empty @return redirect to index page
function board_delete_Action(post_no)
{
	    var isConfirmed = confirm("정말로 삭제하시겠습니까?");
		
		if (isConfirmed) {
			performDelete(post_no);
		} else {
			console.log("삭제 취소");
		}
}
function performDelete(post_no) {
		let url="./deleteBoardAction"
		if (location.href.includes("/n")) {
			//url = "https://nodove.kro.kr/board/view/deleteBoardAction"
			url = contextPath + "/board/view/deleteBoardAction"
		}
	    $.ajax({
        type: "POST",
        url: url,
        data: {
	 		post_no : post_no
        },
        dataType: "text",
        cache: false,
        success: function (response) {
			console.log("삭제 성공");
			window.location.href = contextPath + "/";
        },
        error: function (xhr, status, error) {
            console.error("삭제 실패:", status, error);
        }
    });
}
//board edit @return
function board_edit_Action(post_no)
{
			window.location.href = contextPath + `/board/write/edit/` + post_no;
}


function comment_deleteAction(post_no, comment_no, post_writer)
{
	    var isConfirmed = confirm("정말로 삭제하시겠습니까?");
		
		if (isConfirmed) {
			performCommentDelete(post_no, comment_no, post_writer);
		} else {
			console.log("삭제 취소");
		}
}
function performCommentDelete(post_no, comment_no, post_writer) {
		let url="./deleteCommentAction"
			if (location.href.includes("/n")) {
		//url = "https://nodove.kro.kr/board/view/deleteCommentAction"
		url =  contextPath + "/board/view/deleteCommentAction"
		}
	    $.ajax({
        type: "POST",
        url: url,
        data: {
	 		post_no : post_no,
	 		comment_no : comment_no,
	 		post_writer : post_writer
        },
        dataType: "text",
        cache: false,
        success: function (response) {
			console.log("삭제 성공");
			getCommentList(post_no, post_writer);
			window.location.href = contextPath + `/board/n/${post_no}#comment_${comment_no}`;
        },
        error: function (xhr, status, error) {
            console.error("삭제 실패:", status, error);
        }
    });
}


function comment_alterAction(post_no, comment_no) {
	    $.ajax({
        type: "POST",
        url: "./alterCommentAction",
        data: {
	 		post_no : post_no,
	 		comment_no : comment_no
        },
        dataType: "text",
        cache: false,
        success: function (response) {
			console.log("수정 성공");
			window.location.href = contextPath + `/board/view/boardView.jsp?post_no=${post_no}`;
        },
        error: function (xhr, status, error) {
            console.error("수정 실패:", status, error);
        }
    });
}


function commentAlert(commentPostNo, commentWriter, commentNo, parentCommentNo, comment_depth) {
    $.ajax({
        type: "POST",
        url: "./commentAlert",
        data: {
            commentPostNo: commentPostNo,
            commentWriter: commentWriter,
            comment_no: commentNo,
            parentCommentNo : parentCommentNo,
            comment_depth : comment_depth
        },
        dataType: "json",
        cache: false,
        success: function (response) {			
            if (response) {

                window.location.href = contextPath + `/board/n/${post_no}#comment_${commentNo}`;
                notificationSend(response);
            } else {
                const errorMessage = response && response.message ? response.message : "No error message provided";
                console.error("Notification failed: " + errorMessage);
            }
        },
        error: function (xhr, status, error) {
            console.error("Notification request failed:", status, error);
        }
    });
}

document.addEventListener('DOMContentLoaded', function () {
    // 페이지네이션을 위한 초기 페이지 설정
    var currentPage = 1;

    // 이벤트 리스너 등록
    document.getElementById('addCommentPhotoBtn').addEventListener('click', function () {
        addCommentPhoto(currentPage);
    });
    
    document.querySelectorAll('.imoticon_name_button').forEach(function(button) {
        button.addEventListener('click', function(){
            var imoticon_name = this.getAttribute('data-imoticon');
            addCommentPhoto(currentPage, false, imoticon_name);
        });
    });


    // 이전 페이지로 이동
    document.getElementById('prevPageBtn').addEventListener('click', function () {
        changePage(-1);
    });

    // 다음 페이지로 이동
    document.getElementById('nextPageBtn').addEventListener('click', function () {
        changePage(1);
    });
});

function closeCommentModal(){
    document.getElementById('Commentmodal').style.display = 'none';
}

function addCommentPhoto(currentPage, updatePage, imoticon_name) {
    document.getElementById('Commentmodal').style.display = 'flex';
	console.log("addCommentPhoto : ", imoticon_name);
    $.ajax({
        type: "POST",
        url: "./getCommentImgLists",
        data: {
            mem_id: mem_id,
            currentPage: currentPage,
            imoticon_name : imoticon_name
        },
        dataType: "json",
        cache: false,
        success: function (response) {
            if (response && response.length > 0) {
                var commentImgContents = document.getElementById('comment-img-contents');

                commentImgContents.innerHTML = '';

                response.forEach(function (imageName) {
                    var imgElement = document.createElement('img');
                    if (imoticon_name != null){
                    	imgElement.src = contextPath + `/downloadfile2/commentImg/${imoticon_name}/` + imageName;
                    } else {
						imgElement.src = contextPath + '/downloadfile2/commentImg/' + imageName;
					}
                    imgElement.alt = '이모티콘';

                    imgElement.style.width = '80px';
                    imgElement.style.height = '80px';

                    imgElement.addEventListener('click', function () {
                        insertImageIntoComment(imgElement.src, imgElement.alt);
                        inputComment();
                    });

                    commentImgContents.appendChild(imgElement);
                });

                if (updatePage !== false) {
                    updatePaginationControls(currentPage);
                }
            } else {
                console.error("Notification failed: ");
            }
        },
        error: function (xhr, status, error) {
            console.error("Notification request failed:", status, error);
        }
    });
}


function insertImageIntoComment(imageSrc, altText){
    var commentInput = document.querySelector('.comment_content');
    commentInput.value += `<img src="${imageSrc}" alt="${altText}" width="80" height="80">`;
}

function updatePaginationControls(currentPage) {
    // Update the current page number in the pagination controls
    document.getElementById('currentPage').textContent = currentPage;
}

function changePage(offset) {
    var currentPageElement = document.getElementById('currentPage');
    var currentPage = parseInt(currentPageElement.textContent) + offset;

    addCommentPhoto(currentPage);
}

