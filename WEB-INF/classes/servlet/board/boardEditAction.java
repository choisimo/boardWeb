package servlet.board;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import board.BoardMgr;

 
@WebServlet("/board/write/editBoardAction")
public class boardEditAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
    	response.setContentType("application/json;charset=UTF-8");
    	request.setCharacterEncoding("UTF-8");
    	
    	String endPoint = request.getServletPath();
    	
    	if ("/board/write/editBoardAction".equals(endPoint))
    	{
    		BoardMgr bMgr = new BoardMgr();
            String postTitle = request.getParameter("post_title");
    		Long post_no = Long.parseLong(request.getParameter("post_no"));

            if(postTitle != null && !postTitle.trim().isEmpty() && !postTitle.contains("<p>") &&
            		!checkUnsafeCharacters(postTitle)) { 
        		boolean result = bMgr.editBoardAction(request);
            	if (result) {
            		response.sendRedirect(request.getContextPath()+"/board/n/" + post_no);
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

}
