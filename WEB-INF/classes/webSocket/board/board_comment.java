package webSocket.board;
import java.util.ArrayList;
import java.util.List;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;

import board.MessageData;
import board.commentBean;
import board.commentMgr;

@ServerEndpoint("/websocket_comment")
public class board_comment {
	
	private static List<Session> sessions = new ArrayList<>();
	
	@OnOpen
	public void onOpen(Session session)
	{
		sessions.add(session);
	}
	@OnClose
	public void onClose(Session session)
	{
		sessions.remove(session);
	}
	
	@OnMessage
	public void handleComment(Session session, String jsonmessage)
	{
		Gson gson = new Gson();
		//jsonmessage를 MessageData.class의 객체로 변환
		MessageData messageData = gson.fromJson(jsonmessage, MessageData.class);
		
		if(messageData.getMessage().equals("getBoardViewcomment")) {
			String mem_id = messageData.getMem_id();
			Long post_no = messageData.getPost_no();
			
			try {
	    		commentMgr cMgr = new commentMgr();
	    		List<commentBean> comment_list = new ArrayList<>();
	    		comment_list = cMgr.getComment(post_no);
	    		
	    		String jsonCommentList = gson.toJson(comment_list);
	    		
	    		for (Session users : sessions) {
	    			if (users.isOpen()) {
	    				users.getBasicRemote().sendText(jsonCommentList);
	    			}
	    		}
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
