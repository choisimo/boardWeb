package servlet.board;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class game_path
 */
@WebServlet("/game/*")
public class game_path extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
    	request.setCharacterEncoding("UTF-8");
    	
    	String endPoint = request.getServletPath();
    	HttpSession session = request.getSession();
    	
    	if ("/game".equals(endPoint))
    	{
    		String gameName = request.getPathInfo().substring(1);
			request.getRequestDispatcher("/board/game/" + gameName + ".jsp").forward(request, response);
    	}

	}
}
