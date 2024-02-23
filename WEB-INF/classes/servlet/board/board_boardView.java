

package servlet.board;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet({"/board/n/*", "/board/edit/*"})
public class board_boardView extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
    	request.setCharacterEncoding("UTF-8");
    	
    	String endPoint = request.getServletPath();

    	if ("/board/n".equals(endPoint)) {
        String pathInfo = request.getPathInfo();

        if (pathInfo != null) {
            try {
                String post_no = pathInfo.substring(1);
                String comment_no = request.getParameter("comment_no");
                
                if (post_no != null && !post_no.isEmpty()) {
                	if (comment_no != null && !comment_no.isEmpty()) {
                        request.getRequestDispatcher("/board/view/boardView.jsp?post_no=" + post_no+ "#comment_" + comment_no).forward(request, response);
                	} else {
                		request.getRequestDispatcher("/board/view/boardView.jsp?post_no=" + post_no).forward(request, response);
                	}
                }
                return;
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/error.jsp");
                return;
            }
        }
        	response.sendRedirect(request.getContextPath() + "/error.jsp");
    	}
    }
}
