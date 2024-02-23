package servlet.board;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import board.BoardMgr;


@WebServlet({"/board/write/MemberFileUpload", "/login/RegisterMemberImg"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2 MB
maxFileSize = 1024 * 1024 * 10,      // 10 MB
maxRequestSize = 1024 * 1024 * 50)   // 50 MB
public class boardMember extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setCharacterEncoding("UTF-8");
    	response.setContentType("application/json;charset=UTF-8");
    	request.setCharacterEncoding("UTF-8");
		
		String endPoint = request.getServletPath();
	    
	   	if("/board/write/MemberFileUpload".equals(endPoint))
	   	{
			try {
				Part filePart = request.getPart("file");
				String mem_id = request.getParameter("mem_id");
				String fileName = UUID.randomUUID().toString() + "_" + filePart.getSubmittedFileName();
				
				String uploadPath = "/usr/local/tomcat/webapps/downloadfile2/userImg/" + fileName;
					
				try (InputStream fileContent = filePart.getInputStream()) {
					Files.copy(fileContent, Path.of(uploadPath), StandardCopyOption.REPLACE_EXISTING);
				}
				
	            String relativePath = "/downloadfile2/userImg/" + fileName;
	            BoardMgr bMgr = new BoardMgr();
	            Long mem_no = bMgr.find_mem_no(mem_id);
	            boolean result = bMgr.updateProfileImg(relativePath, mem_no);
	            if (result == true)
	            {
	            	response.getWriter().write(relativePath);
	            }
			}	catch (Exception e) {
	            e.printStackTrace();
	            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        }
			
	   	} else if ("/login/RegisterMemberImg".equals(endPoint))
	   	{
			try {
				Part filePart = request.getPart("file");

				String fileName = UUID.randomUUID().toString() + "_" + filePart.getSubmittedFileName();
				
				String uploadPath = "/usr/local/tomcat/webapps/downloadfile2/userImg/" + fileName;
					
				try (InputStream fileContent = filePart.getInputStream()) {
					Files.copy(fileContent, Path.of(uploadPath), StandardCopyOption.REPLACE_EXISTING);
				}
				
	            String relativePath = "/downloadfile2/userImg/" + fileName;
	            response.getWriter().write(relativePath);

			}	catch (Exception e) {
	            e.printStackTrace();
	            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
	   	}
	}
}
