package webSocket.board;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.Gson;

import board.BoardMgr;
import board.MessageData;
import board.boardBean;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import java.io.IOException;


@ServerEndpoint("/websocket_board_recommend")
public class board_recommend {
	private static Set<Session> sessions = new HashSet<>(); //웹 소켓 세션 관리 
	//websocket_board_recommend_count
    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session); // 새로운 세션을 추가
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session); // 세션을 제거
    }
	@OnMessage
	public void onMessage(Session session, String message) {


		Long post_no = 0L;
		Long post_likecount = 0L;
			
		try {
			Gson gson = new Gson();
			MessageData  messageData = gson.fromJson(message, MessageData.class);
			String receivedMessage = messageData.getMessage();
			post_no = messageData.getPost_no();
			
	        if ("recommend get from DB".equals(receivedMessage)) {

	            try {
	                BoardMgr bMgr = new BoardMgr(); 
	                boardBean bean = bMgr.getBoard(post_no);
	                post_likecount = bean.getPost_likecount();

	                
	                SendResponseToAllSessions(post_no, post_likecount);
	            } catch (NumberFormatException | NullPointerException e) {
	                e.printStackTrace();
	            }
	            sendResponse(session, post_no, post_likecount);
	        }
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	
	private void sendResponse(Session session, Long post_no, Long post_likecount)
	{
		Gson gson = new Gson();
	    String responseMessage = gson.toJson(new ResponseData("Recommendation updated", post_no, post_likecount));
			try {
				session.getBasicRemote().sendText(responseMessage);
			} catch (Exception e)
		{
			e.printStackTrace(); 
		}
	}
	
	
	private void SendResponseToAllSessions(Long post_no, Long post_likecount) {
		Gson gson = new Gson();
        String responseMessage = gson.toJson(new ResponseData("Recommendation updated", post_no, post_likecount));
        
        for (Session session : sessions) {
        	try {
        		session.getBasicRemote().sendText(responseMessage);
        	} catch (IOException e) {
        		e.printStackTrace();
        	}
        }
	}
	
	class ResponseData {
	    private String message;
	    private Long post_no;
	    private Long post_likecount;

	    public ResponseData(String message, Long post_no, Long post_likecount) {
	        this.message = message;
	        this.post_no = post_no;
	        this.post_likecount = post_likecount;
	    }
	}
	
	
}