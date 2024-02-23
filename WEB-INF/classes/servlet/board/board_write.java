package servlet.board;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import board.BoardMgr;


@WebServlet({"/board/write/writeAction", "/board/write/post", "/board/write/edit/*"})
public class board_write extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
    	response.setCharacterEncoding("UTF-8");
    	response.setContentType("application/json;charset=UTF-8");
    	request.setCharacterEncoding("UTF-8");
    	
    	String endPoint = request.getServletPath();
    	
    	if ("/board/write/writeAction".equals(endPoint))
    	{
        	BoardMgr bMgr = new BoardMgr();
            String postTitle = request.getParameter("post_title");
            
            if (postTitle != null && !postTitle.trim().isEmpty() && !postTitle.contains("<p>")
            		&& !checkUnsafeCharacters(postTitle)) {
        	boolean result = bMgr.insertBoard(request);
        	if (result) {
        		response.sendRedirect(request.getContextPath()+"/");
        	} else {
        		System.out.println("글쓰기 실패");
        		}
            } else {
            	HttpSession session = request.getSession();
                session.setAttribute("warningMessage", "제목에 사용할 수 없는 문자열이 포함되어 있습니다."
                		+ "사용불가 (<>&\\\",<p>, 공백)");
            	response.sendRedirect(request.getHeader("Referer"));
            }
    	} 
	}
	
	private boolean checkUnsafeCharacters(String input) {
		String unsafeCharacters = "<>&\"";
		for (char c : unsafeCharacters.toCharArray()) {
			if (input.contains(String.valueOf(c))) {
				return true;
			}
		}
		return false;
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
    	response.setCharacterEncoding("UTF-8");
    	response.setContentType("application/json;charset=UTF-8");
    	request.setCharacterEncoding("UTF-8");
    	
    	String endPoint = request.getServletPath();
    	
    	if ("/board/write/post".equals(endPoint))
    	{
            request.getRequestDispatcher("/board/write/write.jsp").forward(request, response);
    	} else if ("/board/write/edit".equals(endPoint)) {
            String pathInfo = request.getPathInfo();
            String post_no = pathInfo.substring(1);
    		request.getRequestDispatcher("/board/write/edit.jsp?post_no="+post_no).forward(request, response);
    	}
    	/* else if ("/board/write/edit/*".equals(endPoint))
    	{
    		String pathInfo = request.getPathInfo();
            String post_no = pathInfo.substring(1);
           
    		request.getRequestDispatcher("/board/post/edit.jsp?post_no="+post_no).forward(request, response);
    	}
    	*/
	}
}
