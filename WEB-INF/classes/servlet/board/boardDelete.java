package servlet.board;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import board.BoardMgr;


@WebServlet({"/board/view/deleteBoardAction"})
public class boardDelete extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
    	response.setContentType("application/json;charset=UTF-8");
    	request.setCharacterEncoding("UTF-8");
    	
    	String endPoint = request.getServletPath();
    	
    	if("/board/view/deleteBoardAction".equals(endPoint))
    	{
    		BoardMgr bMgr = new BoardMgr();
    		Long post_no = Long.parseLong(request.getParameter("post_no"));

    		int result = bMgr.deleteBoardAction(post_no);

    		if (result == 1)
    		{
        		response.sendRedirect(request.getContextPath()+"/board/index.jsp");
    	        response.getWriter().write("success");
    		} else {
    	        response.getWriter().write("failure");
    		}
    	}
	}
}
