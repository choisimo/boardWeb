var webSocket = new WebSocket("wss://nodove.kro.kr/websocket_board_recommend");


//send websocket
webSocket.onopen = function (event) {
    var message = "recommend get from DB";
    webSocket.send(JSON.stringify({"message" :message, "post_no" : post_no}));
};


//receive data 
webSocket.onmessage = function (event) {

	var response = JSON.parse(event.data);
	var post_no = response.post_no; //이걸 굳이 받아야할까 싶은
	var post_likecount = response.post_likecount;
	updateRecommendationCount(post_no, post_likecount);
	
};
 
//close websocket
webSocket.onclose = function (event) {
	console.log("connection closed :", event);
};

webSocket.onerror = function (event) {
	console.log("connection error :", event);
};

function updateRecommendationCount(post_no, post_likecount) {

    console.log("추천수가 갱신되었습니다. post_no: " + post_no + ", post_likecount: " + post_likecount);
   
    var HTML_likecount2 = `<span class="recommend-label2">${post_likecount}</span>`;
    $('.recommend-label2').html(HTML_likecount2);


}
