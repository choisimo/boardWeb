package servlet.board;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import user.Member_Mgr;

@WebServlet("/board/n/getCommentImgLists")
public class comment_imoticon extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        int itemsPerPage = 12;
        int currentPage = Integer.parseInt(request.getParameter("currentPage"));
        int startIndex = (currentPage - 1) * itemsPerPage;
        int endIndex = startIndex + itemsPerPage;
        
        String mem_id = request.getParameter("mem_id");
        String imoticon_name = request.getParameter("imoticon_name");

        Member_Mgr mMgr = new Member_Mgr();
        Long mem_no = mMgr.find_mem_no(mem_id);
        String imagePath = null;

        if (imoticon_name != null && !imoticon_name.isEmpty()) {
            imagePath = "/usr/local/tomcat/webapps/downloadfile2/commentImg/" + imoticon_name + "/";
            System.out.println("imoticon_name folder is " + imagePath);
        } else {
            imagePath = "/usr/local/tomcat/webapps/downloadfile2/commentImg/";
        }
        List<String> imagePaths = new ArrayList<>();

        // imagePath 경로를 나타내는 File 객체 생성하기
        File directory = new File(imagePath);

        if (directory.exists() && directory.isDirectory()) {
            // 디렉토리 내의 파일 목록 가지고 오기
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && isImageFile(file.getName())) {
                        imagePaths.add(file.getName());
                    }
                }
            }
        }

        // 이미지 목록을 페이지에 맞게 자르기
        List<String> paginatedImagePaths = imagePaths.subList(startIndex, Math.min(endIndex, imagePaths.size()));

        String jsonImagePaths = new Gson().toJson(paginatedImagePaths);
        response.getWriter().write(jsonImagePaths);
    }

    private boolean isImageFile(String fileName) {
        return fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".jpeg")
                || fileName.toLowerCase().endsWith(".png") || fileName.toLowerCase().endsWith(".gif")
                || fileName.toLowerCase().endsWith(".webp");
    }
}
