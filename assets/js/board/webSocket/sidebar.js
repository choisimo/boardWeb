//socket 연결
var messages = [];  // Make sure it's defined and initialized
var socket;

function connectSocket(){
	var wss = new WebSocket("wss://nodove.kro.kr/websocket_sidebar/" + mem_id);
	socket = wss;
	
	socket.onopen = function() {

		var message = {
			message: "getCommentAlerts",
			mem_id : mem_id
		};
		socket.send(JSON.stringify(message));
	};
	
	//받을 알람이 있을 때
	socket.onmessage = function(event) {
	try {
		if (event.data.length > 0){
			messages = messages.concat(JSON.parse(event.data));
			updateNotificationCount(messages.length);
		}
	} catch (error){
			console.error(error);
		}
	};
}

var csocket;
function connectCommentSocket() {
	var commentSocket = new WebSocket("wss://nodove.kro.kr/websocket_sidebar/comment/" + mem_id);
	csocket = commentSocket;
	
	csocket.onopen = function(){

		 var message = {
			 message: "getReplyAlerts",
			 mem_id: mem_id
		 };
		 commentSocket.send(JSON.stringify(message));
	 };
	 
	 commentSocket.onmessage = function(event) {
		 try {
			 var data = event.data;

			 if (data && data.length > 0) {
				messages = messages.concat(JSON.parse(data));
				updateNotificationCount(messages.length);
			 }
		 } catch (error) {
			 console.error(error);
		 }
	 };
}
function updateNotificationCount(count) {

	var notificationCountElement = $("#notificationCount");
	notificationCountElement.text(count);
	notificationCountElement.show();
}

document.addEventListener("DOMContentLoaded", function() {
	connectSocket();
	connectCommentSocket();
});


function openNotificationModal() {
  receivedMessages = messages;
  var modal = document.getElementById("notificationModal");

  // Check if there are messages before displaying the modal
    modal.style.display = "block";

    // 여기에서 알림을 표시할 요소를 가져와서 추가합니다.
    var notificationList = document.getElementById("notificationList");
    notificationList.innerHTML = ""; // 기존 알림 초기화

    // 서버에서 받아온 알림 메시지를 동적으로 추가
  receivedMessages.forEach(function(message) {

    var post_no = message.notification_post_no;
    var comment_no = message.notification_comment_no;
    var comment_writer_id = message.comment_writer_id;
    var AlertContent = message.AlertContent;
    var notificationType = message.notification_type;

	var maxContentLength = 15;

/*	if (AlertContent.length > maxContentLength) {
	    AlertContent = AlertContent.substring(0, maxContentLength) + '...';
	}*/
	
    var li = document.createElement("li");
	AlertContent = decodeURIComponent(AlertContent);
	
    if (notificationType === "comment_reply") {
        // Handling comment reply notifications
        li.innerHTML = `<a href="${contextPath}/board/n/${post_no}#comment_${comment_no}" onclick="closeNotificationModal()">
        ${AlertContent}에 ${comment_writer_id}님이 대댓글을 달았습니다.</a>`;
    } else if (notificationType === "comment"){
        // Handling other types of notifications (modify as needed)
        li.innerHTML = `<a href="${contextPath}/board/n/${post_no}" onclick="closeNotificationModal()">
        ${post_no}번 게시글 (${AlertContent}) 에 ${comment_writer_id} 님이 댓글을 달았습니다.</a>`;
    }

    notificationList.appendChild(li);  
	});
	  // 알림 카운트 갱신
  	updateNotificationCount(receivedMessages.length);
}


function closeNotificationModal() {
  // 알림 모달 닫기
  var modal = document.getElementById("notificationModal");
  modal.style.display = "none";
}
