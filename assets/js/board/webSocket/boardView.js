function connectSocket() {
    var wss = new WebSocket("wss://nodove.kro.kr/websocket_comment");
    socket = wss;

    socket.onopen = function () {
		
        if (socket.readyState === WebSocket.OPEN) {
            var message = {
                message: "getBoardViewcomment",
                mem_id: mem_id,
                post_no: post_no
            };
            socket.send(JSON.stringify(message));
        } else {
            console.error("WebSocket connection not open yet.");
        }
    };

    socket.onmessage = function (event) {
        try {
            if (event.data.length > 0) {
                messages = JSON.parse(event.data);
                console.log("count =>"+ messages.length); 
                messages.forEach(function(message){
					console.log(message.comment_no);
				});
				
				var refreshCountElement = document.querySelector(".refresh_comment_count");
				if (refreshCountElement){
					refreshCountElement.innerText = messages.length;
				}
            }
        } catch (error) {
            console.error(error);
        }
    };
}

