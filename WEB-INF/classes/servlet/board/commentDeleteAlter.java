package servlet.board;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import board.commentMgr;


@WebServlet({"/board/view/deleteCommentAction", "/board/view/alterCommentAction"})
public class commentDeleteAlter extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
    	response.setContentType("application/json;charset=UTF-8");
    	request.setCharacterEncoding("UTF-8");
    	
    	String endPoint = request.getServletPath();
		Long post_no = Long.parseLong(request.getParameter("post_no"));
		Long comment_no = Long.parseLong(request.getParameter("comment_no"));

    	if ("/board/view/deleteCommentAction".equals(endPoint)) {
    		commentMgr cMgr = new commentMgr();
    		int result = cMgr.deleteCommentAction(post_no, comment_no);
    		if (result == 1)
    		{
        		response.sendRedirect(request.getContextPath()+"/board/index.jsp");
    	        response.getWriter().write("success");
    		} else {
    	        response.getWriter().write("failure");
    		}
		} /*
			 * else if ("/board/view/alterCommentAction".equals(endPoint)) { commentMgr cMgr
			 * = new commentMgr(); int result = cMgr.alterCommentAction(post_no,
			 * comment_no); if (result == 1) {
			 * response.sendRedirect(request.getContextPath()+"/board/index.jsp");
			 * response.getWriter().write("success"); } else {
			 * response.getWriter().write("failure"); } }
			 */
	}

}
