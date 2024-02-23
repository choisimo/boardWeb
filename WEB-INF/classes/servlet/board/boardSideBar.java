package servlet.board;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import board.BoardMgr;


@WebServlet("/board/write/boardSideBar")
public class boardSideBar extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			
	    	response.setCharacterEncoding("UTF-8");
	    	response.setContentType("application/json;charset=UTF-8");
	    	
	    	String endPoint = request.getServletPath();
	
	    		if ("/board/write/boardSideBar".equals(endPoint)) {
	    		BoardMgr bMgr = new BoardMgr();
	    	
	    		String mem_id = (String)request.getParameter("mem_id");
	            Long mem_no = bMgr.find_mem_no(mem_id);
	    		String mem_profile_img = bMgr.getProfileImg(mem_no);
	    		
	    		PrintWriter script = response.getWriter();
	    		
	            script.print("{\"mem_profile_img\":\"" + mem_profile_img + "\"}");
	            script.flush();
    		}
	}
}