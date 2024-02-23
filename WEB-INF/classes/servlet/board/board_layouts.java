package servlet.board;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet({"/", "/index", "/page/n/*", "/search/n/*"})
public class board_layouts extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
    	request.setCharacterEncoding("UTF-8");
    	
    	String endPoint = request.getServletPath();
    	HttpSession session = request.getSession();
		//index page
		if("/".equals(endPoint) || "/index".equals(endPoint)) {
			if (session.getAttribute("mem_id") == null) {
				PrintWriter script = response.getWriter();
				script.println("<script>");
				script.println("alert('로그인이 필요한 페이지입니다!')");
	    	    script.println("location.href='" + request.getContextPath() + "/login';");
				script.println("</script>");
				// request.getRequestDispatcher("/login").forward(request, response);
			} else {
			request.getRequestDispatcher("/board/index.jsp").forward(request, response);
			}
		} else if ("/page/n".equals(endPoint)) {
			
			try {
		        String pageNum = request.getPathInfo().substring(1);
		        request.getRequestDispatcher("/board/index.jsp?pageNum=" + pageNum).forward(request, response);
				
				return;
				} catch (Exception e){
					e.printStackTrace();
			}
		} else if ("/search/n".equals(endPoint))
		{
			String so = request.getParameter("so");
			String sv = request.getParameter("sv");
			try {
			String pageNum = request.getPathInfo().substring(1);

			request.getRequestDispatcher("/board/index.jsp?pageNum=" + pageNum + "&so=" + so + "&sv=" + sv).forward(request, response);
			return;
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
 
