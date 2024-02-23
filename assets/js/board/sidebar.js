
function openPopupMember(url) {
  var popupWidth = 300;
  var popupHeight = 400;
  var popupX = window.screen.width / 2 - popupWidth / 2;
  var popupY = window.screen.height / 2 - popupHeight / 2;

  // Check if the popup would go off the screen
  if (popupX < 0) {
    popupX = 0;
  }
  if (popupY < 0) {
    popupY = 0;
  }

  var popup = window.open(url, 'PopupWindow', 'width=' + popupWidth + ', height=' + popupHeight + ', scrollbars=yes, left=' + popupX + ', top=' + popupY);
}

	
function openPopup(url) {
  var popupWidth = 500;
  var popupHeight = 300;
  var popupX = (window.screen.width/2) - (popupWidth/2);
  var popupY = (window.screen.height/2) - (popupHeight/2);
  var popup = window.open(url, 'PopupWindow', 'width=popupWidth, height=popupHeight, scrollbars=yes, left=popupX, top=popupY');
}

function loadContent(url) {
  var xhr = new XMLHttpRequest();
  xhr.onreadystatechange = function() {
    if (xhr.readyState === 4 && xhr.status === 200) {
      // Ajax 요청이 완료되고 페이지를 가져온 경우 메인 페이지를 업데이트
      document.getElementById('mainContent').innerHTML = xhr.responseText;
      // 맨 상단으로 가도록
/*      window.scrollTo(0, 0);
*/    }
  };
  xhr.open('GET', url, true);
  xhr.send();
}

/*function updateProfileImg(mem_id){
	    $.ajax({
        type: "POST",
        url: "./boardSideBar",
        data: { mem_id : mem_id },
        dataType: "json",
        cache: false, //한번 cache 막아봅시다.
        success: function (response) {
            if (response && response.post_viewcount !== undefined) {

            $("#profileImage").attr("src", response.mem_profile_img);
            } else {
                console.log("올바르지 않은 JSON format 입니다.");
            }
        },
        error: function (xhr, status, error) {
            console.error("추천에 실패했습니다.:", status, error);
        }
    });
}
*/

function logout() {

    $.ajax({
        type: 'POST',
        url: contextPath+'/board/logout', 
        success: function (response) {

            if (response === 'success') {
                alert('로그아웃되었습니다.');
                window.location.href = contextPath+'/login';
            } else {
                alert('로그아웃에 실패했습니다.');
            }
        },
        error: function () {
            alert('서버 오류로 로그아웃에 실패했습니다.');
        }
    });
}

/*function getCommentAlert()
{
	    $.ajax({
        type: "POST",
        url: contextPath + "/getCommentAlert",
        data: {
            mem_id : mem_id
        },
        dataType: "json",
        cache: false,
			success: function (response) {
			    if (response && response.length > 0) {
					
					updateNotificationCount(response.length);
					
					var uniqueNotifications = new Set(response.map(notification => JSON.stringify(notification)));
					
					var uniqueNotificationsArray = Array.from(uniqueNotifications).map(notificationString => JSON.parse(notificationString));
					
			        uniqueNotificationsArray.forEach(function (notification) {
						displayNotificationsPopup(notification);
''			        });
			    } else {
			        console.log("No notifications received.");
			    }
			},
        error: function (xhr, status, error) {
            console.error("Notification request failed:", status, error);
        }
    });
}

function updateNotificationCount(count) {

	var notificationCountElement = $("#notificationCount");
	notificationCountElement.text(count);
	notificationCountElement.show();
}


function displayNotificationsPopup(notification) {
    // 팝업 창 엘리먼트를 가져오거나 생성
    var popup = $("#notificationPopup");
        
    if (!popup.length) {
        popup = $("<div id='notificationPopup'></div>");
        $("body").append(popup);
    }

    // 알림을 HTML로 만들어 팝업에 추가
    var notificationHtml = "<div>";
    notificationHtml += "Type: " + notification.notification_type + "<br>";
    notificationHtml += "Post No: " + notification.notification_post_no + "<br>";
    // 여기에 필요한 다른 속성들을 추가
    notificationHtml += "</div>";

    popup.append(notificationHtml);

    // 팝업을 보여주는 등의 스타일 및 동작을 추가할 수 있음
    popup.show();
}

// 알림 버튼 클릭 시 알림 목록을 가져와서 표시
$(".notification_button_alert").on("click", function () {
    getCommentAlert();  // 알림 목록을 가져오는 함수 호출
});
*/
