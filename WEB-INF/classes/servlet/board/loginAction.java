package servlet.board;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import user.Member_Mgr;


@WebServlet({"/login","/login/loginAction","/register","/register/registerAction", "/board/logout", "/pwgen"})
public class loginAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	

	  @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  
	  response.setCharacterEncoding("UTF-8");
	  response.setContentType("text/html;charset=UTF-8");
	  request.setCharacterEncoding("UTF-8");
	  
	  String endPoint = request.getServletPath(); 
	  HttpSession session = request.getSession();
	  
	  if ("/login".equals(endPoint)) {
			if (session.getAttribute("mem_id") != null) {
				PrintWriter script = response.getWriter();
				script.println("<script>");
				script.println("alert('이미 로그인 되어 있습니다.')");
				script.println("location.href='" + request.getContextPath() + "/';");
				script.println("</script>");
				//request.getRequestDispatcher("/board/index.jsp").forward(request, response);
			} else {
				request.getRequestDispatcher("/login/login.jsp").forward(request, response);
			}
		} else if ("/register".equals(endPoint)) {
			request.getRequestDispatcher("/login/register.jsp").forward(request, response);
		} else if ("/pwgen".equals(endPoint)) {
	        request.getRequestDispatcher("/login/passwordGenerator.jsp").forward(request, response);
		}
	  }

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
    	request.setCharacterEncoding("UTF-8");

    	String endPoint = request.getServletPath();
    	HttpSession session = request.getSession();
    	
    	if ("/login/loginAction".equals(endPoint)) {
            String mem_login_id = request.getParameter("mem_login_id");
            String mem_pw = request.getParameter("mem_pw");
    	
    	Member_Mgr memMgr = new Member_Mgr();
    	// 로그인 성공시 session에 저장할 mem_id 호출
    	String mem_id = memMgr.loginMember(mem_login_id, mem_pw);

    	if(mem_id != null) {
    		//로그인 성공시 mem_id session에 저장
    		session.setAttribute("mem_id", mem_id);
    		response.sendRedirect(request.getContextPath() + "/");
    	} else {
    	    PrintWriter out = response.getWriter();
    	    out.println("<script>");
    	    out.println("alert('로그인 정보가 틀립니다!');");
    	    out.println("location.href='" + request.getContextPath() + "/login';");
    	    out.println("</script>");
    	 }
	} else if ("/login".equals(endPoint)) {
		if (session.getAttribute("mem_id") != null) {
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('이미 로그인 되어 있습니다.')");
    	    script.println("location.href='" + request.getContextPath() + "/login';");
			script.println("</script>");
			//request.getRequestDispatcher("/board/index.jsp").forward(request, response);
		} else {
	        request.getRequestDispatcher("/login/login.jsp").forward(request, response);
		}
	} else if ("/register/registerAction".equals(endPoint)) {
		Member_Mgr memMgr = new Member_Mgr();
		String register_check = memMgr.registerMember(request);  
		
		if (register_check.equals("200/회원가입에 성공했습니다.")) {
			request.getRequestDispatcher("/login").forward(request, response);
		} else {
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('이미 사용 중인 아이디 입니다.')");
    	    script.println("location.href='" + request.getContextPath() + "/register';");
			script.println("</script>");
		}
	} else if ("/board/logout".equals(endPoint)) {
        session.invalidate(); // 세션 만료
        response.getWriter().write("success"); // 성공 응답 전송
	} 
}

}
