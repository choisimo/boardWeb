package servlet.board;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.google.gson.Gson;


@WebServlet({"/board/write/FileUpload","/board/write/MultipleFileUpload"})
@MultipartConfig(maxFileSize = 100 * 1024 * 1024, maxRequestSize = 20 * 100 * 1024 * 1024)
public class FileUpload extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		response.setCharacterEncoding("UTF-8");
    	response.setContentType("application/json;charset=UTF-8");
    	request.setCharacterEncoding("UTF-8");

    	String endPoint = request.getServletPath();
    	
   	if("/board/write/FileUpload".equals(endPoint))
   	{
		try {
			Part filePart = request.getPart("file");
			String fileName = UUID.randomUUID().toString() + "_" + filePart.getSubmittedFileName();
			String uploadPath = "/usr/local/tomcat/webapps/downloadfile2/boardSave/" + fileName;
				
			try (InputStream fileContent = filePart.getInputStream()) {
				Files.copy(fileContent, Path.of(uploadPath), StandardCopyOption.REPLACE_EXISTING);
			}
			
            String relativePath = "/downloadfile2/boardSave/" + fileName;
			response.getWriter().write(relativePath);
			
		}  catch (ServletException e) {
		    if (e.getMessage().contains("the request was rejected because its size")) {
		        response.setStatus(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE);
		        response.getWriter().write("파일 크기가 너무 큽니다.");
		    } else {
		        e.printStackTrace();
		        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		        response.getWriter().write("서버 내부 에러가 발생했습니다.");
		    } 
		}catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
		
		} else if ("/board/write/MultipleFileUpload".equals(endPoint))
		{
			try {
			List<String> fileUrls = new ArrayList<>();
			Collection<Part> fileParts = request.getParts();
			
            for (Part filePart : fileParts) {
                String fileName = UUID.randomUUID().toString() + "_" + getFileName(filePart);
                String uploadPath = "/usr/local/tomcat/webapps/downloadfile2/boardSave/" + fileName;

                try (InputStream fileContent = filePart.getInputStream()) {
                    Files.copy(fileContent, Path.of(uploadPath), StandardCopyOption.REPLACE_EXISTING);
                }

                fileUrls.add("/downloadfile2/boardSave/" + fileName);
            }
            
            	response.setContentType("application/json");
            	response.setCharacterEncoding("UTF-8");
            	response.getWriter().write(new Gson().toJson(fileUrls));
            	
			} catch (Exception e) {
	            e.printStackTrace();
	            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        }
		}
	}
		
	    private String getFileName(Part part) {
	        for (String content : part.getHeader("content-disposition").split(";")) {
	            if (content.trim().startsWith("filename")) {
	                return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
	            }
	        }
	        return null;
	        
	}
}
