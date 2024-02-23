package servlet.board;

import java.io.IOException;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import board.BoardMgr;
import board.NotificationBean;
import board.NotificationMgr;
import board.boardBean;
import board.commentBean;
import board.commentMgr;
import user.Member_Mgr;

import org.apache.commons.text.StringEscapeUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


@WebServlet({"/comment_post", "/board/view/getCommentList", "/board/view/inputComment", "/board/n/commentAlert"
	,"/getCommentAlert", "/board/view/getRepliesList"})
public class comment_post extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Long post_no = 0L;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

			response.setCharacterEncoding("UTF-8");
	    	response.setContentType("application/json;charset=UTF-8");
	    	request.setCharacterEncoding("UTF-8");
	    	
	    	String endPoint = request.getServletPath();

	    	if ("/board/view/getCommentList".equals(endPoint))
	    	{
		    	post_no = Long.parseLong(request.getParameter("post_no"));
	    		commentMgr cMgr = new commentMgr();
	    		List<commentBean> comment_list = new ArrayList<>();
	    		Long comment_count = 0L;
	    		Long totalCommentNum = 0L;
	    		Long pageSize = Long.parseLong(request.getParameter("pageSize"));
	    		
	    		comment_count = cMgr.getCommentCount(post_no);
	    		totalCommentNum = cMgr.totalCommentNumber(post_no);
	    		Long page = Long.parseLong(request.getParameter("page"));
	    		if (page == 999999) {
	    			page = (long) Math.ceil(comment_count / (double) pageSize);
	    		}
	    		comment_list = cMgr.getComment(post_no, page, pageSize);
                String jsonResponse = convertCommentListToJSON(comment_list, comment_count, totalCommentNum);
                
                response.getWriter().write(jsonResponse);

	    	} else if ("/board/view/inputComment".equals(endPoint)) {
	    		commentMgr cMgr = new commentMgr();
	    		Long commentNo = cMgr.insertComment(request);
	    		if (commentNo != null) {
		    		response.setContentType("application/json;charset=UTF-8");
		    		response.getWriter().write("{ \"success\": true, \"message\": \"댓글 입력 성공\", \"commentNo\" : " + commentNo +  " }");
	    		}
	    	} else if ("/board/n/commentAlert".equals(endPoint)) {
	    		Long comment_no = Long.parseLong(request.getParameter("comment_no"));
	    		NotificationMgr nMgr = new NotificationMgr();
	    		Long parentCommentNo = 0L;
	    		int comment_depth = 0;
	    		Long notification_no = 0L;
	    		
	    		if (request.getParameter("parentCommentNo") != null && !request.getParameter("parentCommentNo").isEmpty() &&
	    			request.getParameter("comment_depth") != null && !request.getParameter("comment_depth").isEmpty()) {
		    			parentCommentNo = Long.parseLong(request.getParameter("parentCommentNo"));
		    			comment_depth = Integer.parseInt(request.getParameter("comment_depth"));
		    			notification_no = nMgr.insert_commentAlert2(comment_no, parentCommentNo);
	    		} else {
	    			notification_no = nMgr.insert_commentAlert(comment_no);
	    		}
	    		if (notification_no != null) {
	    			NotificationBean bean = nMgr.getCommentAlert(notification_no);
	    			
	    			Gson gson = new Gson();
	    			String jsonNotification = gson.toJson(bean);
	    			
	    			response.setContentType("application/json;charset=UTF-8");
	    			response.getWriter().write(jsonNotification);
	    		}
			} /*
				 * else if ("/getCommentAlert".equals(endPoint)) { try { String mem_id =
				 * request.getParameter("mem_id"); Member_Mgr mMgr = new Member_Mgr(); Long
				 * mem_no = mMgr.find_mem_no(mem_id);
				 * 
				 * List<NotificationBean> list = new ArrayList<>(); NotificationMgr nMgr = new
				 * NotificationMgr(); list = nMgr.getCommentAlert2(mem_no); Gson gson = new
				 * Gson(); String jsonList = gson.toJson(list);
				 * 
				 * response.setContentType("application/json;charset=UTF-8");
				 * 
				 * response.getWriter().write(jsonList); } catch (Exception e) {
				 * e.printStackTrace(); } }
				 */else if ("/board/view/getRepliesList".equals(endPoint)) {
	    		commentMgr cMgr = new commentMgr();
	    		List<commentBean> replyList = new ArrayList<>();
	    		replyList = cMgr.getRepliesList(request);
	    		
	    		String replyResponse = new Gson().toJson(replyList);
	    		response.getWriter().write(replyResponse);
	    	}
	}
	   	
	
	
	
	private String convertCommentListToJSON(List<commentBean> commentList, Long comment_count, Long totalCommentNum) {
	    try {
	        List<String> jsonComments = new ArrayList<>();
	        for (commentBean comment : commentList) {
	            String jsonComment = convertCommentToDecodeJson(comment);
	            if (!jsonComment.isEmpty()) {
	                jsonComments.add(jsonComment);
	            }
	        }

	        JsonObject jsonObject = new JsonObject();
	        jsonObject.add("commentList", JsonParser.parseString(new Gson().toJson(jsonComments)));
	        jsonObject.addProperty("totalCommentCount", comment_count);
	        jsonObject.addProperty("totalCommentNum", totalCommentNum);
	        
	        return jsonObject.toString();
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "{}"; // Return an empty JSON object in case of an error
	    }
	}
	private String convertCommentToDecodeJson(commentBean comment) {
	    try {
	        return "{"
	                + "\"comment_no\":\"" + comment.getComment_no() + "\","
	                + "\"comment_content\":\"" + StringEscapeUtils.escapeJson(URLDecoder.decode(comment.getComment_content(), "UTF-8")) + "\","
	                + "\"comment_time\":\"" + comment.getComment_time() + "\","
	                + "\"comment_writer\":\"" + comment.getComment_writer() + "\","
	                + "\"comment_writer_id\":\"" + comment.getComment_writer_id() + "\","
	                + "\"comment_img_path\":\"" + comment.getImg_path() + "\","
	    	        + "\"comment_depth\":\"" + comment.getComment_depth() + "\","
	    	    	+ "\"comment_parent\":\"" + comment.getComment_parent() + "\","
	    	    	+ "\"comment_parent_id\":\"" + comment.getComment_parent_id() + "\","
	                + "\"comment_post_no\":\"" + comment.getComment_post_no() + "\","
	    	        + "\"comment_isParent\":\"" + comment.getIsParent() + "\","
	                + "\"comment_replies\":\"" + comment.getReplies() + "\""
	                + "}";
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "";
	    } 
	}

	/*
	 * private String decodeURIComponent(String s) { try { return
	 * URLDecoder.decode(s, "UTF-8"); } catch (Exception e) { e.printStackTrace();
	 * return ""; } }
	 */
}
