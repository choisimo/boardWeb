package webSocket.board;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import board.MessageData;
import board.NotificationBean;
import board.NotificationMgr;
import user.Member_Mgr;
   
@ServerEndpoint("/websocket_sidebar/{mem_id}") 
public class sidebar_alert {

private static final Logger logger = LogManager.getLogger(sidebar_alert.class);
//session을 가지고 있는 user 전체 저장
private static List<Session> sessions = new ArrayList<>();	
	
	//webSocket 새로운 session이 열릴 때마다 해당 세션을 sessions 리스트에 추가한다
	@OnOpen
	public void onOpen(Session session, @PathParam("mem_id") String mem_id)
	{
		// user identifier in the session properties
		session.getUserProperties().put("mem_id", mem_id);
		sessions.add(session);
	}
	
	@OnClose
	public void onClose(Session session)
	{
		//웹 소켓 연결 끊어지면 session 제거
		sessions.remove(session);
	}
	
	@OnMessage
	public void handleAlert(Session session, String jsonmessage)
	{
		Gson gson = new Gson();
		MessageData messageData = gson.fromJson(jsonmessage, MessageData.class);
		
		if (messageData.getMessage().equals("getCommentAlerts")) {
	            String mem_id = messageData.getMem_id();
	            //나의 세션 구하기
	            Session userSession = null;
	            String sessionMemId = null;
	            
	            for (Session s : sessions) {
	                sessionMemId = (String) s.getUserProperties().get("mem_id");
	                if (sessionMemId != null && sessionMemId.equals(mem_id)) {
	                    userSession = s;
	                    break;
	                }
	            }
	            
	    	    try {
	    	        Member_Mgr mMgr = new Member_Mgr();
	    	        Long mem_no = mMgr.find_mem_no(mem_id);

	    	        List<NotificationBean> list = new ArrayList<>();
	    	        
	    	        NotificationMgr nMgr = new NotificationMgr();
	    	        // 안 읽은 메시지 목록 리스트에 저장
	    	        list = nMgr.getCommentAlert2(mem_no, 0);
	    	        
	    	        String json = gson.toJson(list);
	    	        
	    	        for (Session users : sessions) {
	    	            String targetMemId = (String) users.getUserProperties().get("mem_id");
	    	        	if (users.isOpen() && userSession != null && targetMemId.equals(mem_id)) {
	    	        	    users.getAsyncRemote().sendText(json);
	    	        	}
	    	        }
	    	    } catch (Exception e) {
	    	        e.printStackTrace();
	   	    }
		}
	}
}
