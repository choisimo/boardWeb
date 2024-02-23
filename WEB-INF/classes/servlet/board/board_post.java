package servlet.board;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import board.BoardMgr;
import board.boardBean;


@WebServlet({"/board_post","/board/view/getBoardData", "/board/view/increaseRecommendation", "/board/view/increaseViewcount"})
public class board_post extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
    	response.setCharacterEncoding("UTF-8");
    	response.setContentType("application/json;charset=UTF-8");
    	
    	
    	String endPoint = request.getServletPath();
    	Long post_no = 0L;

    	BoardMgr bMgr = new BoardMgr();

    	if (request.getParameter("post_no") != null && !request.getParameter("post_no").isEmpty())
    	{
    		post_no = Long.parseLong(request.getParameter("post_no"));
    	}
    	
    	if ("/board/view/getBoardData".equals(endPoint)) {
    		boardBean bean = bMgr.getBoard(post_no);
    		
    		Long post_likecount = bean.getPost_likecount();
    		Long post_viewcount = bean.getPost_viewcount();
    		
    		PrintWriter script = response.getWriter();
    		script.print("{\"post_likecount\":" + post_likecount + ", \"post_viewcount\":" + post_viewcount + "}");
    		script.flush();
    		
    	} else if ("/board/view/increaseRecommendation".equals(endPoint)) {
    		Long post_likecount = bMgr.updateLikecount(post_no);
    		
    		PrintWriter script = response.getWriter();
    		
    		script.print("{\"post_likecount\":" + post_likecount + "}");
    		script.flush();
    	} else if ("/board/view/increaseViewcount".equals(endPoint)) {
    		Long post_viewcount = bMgr.updateViewcount(post_no);
    		
    		PrintWriter script = response.getWriter();
    		
    		script.print("{\"post_viewcount\":" + post_viewcount + "}");
    		script.flush();
    	}
	}

}
