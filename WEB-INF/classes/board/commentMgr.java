package board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.mysql.jdbc.Statement;

import DBconnector.DBConnectionMgr;

public class commentMgr {

	
	private DBConnectionMgr pool;
	private static final String ENCTYPE = "UTF-8";
 
	public commentMgr()	{
		try {
			pool = DBConnectionMgr.getInstance();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public String findMem_img(Long mem_no)
	{
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String result = null;
		try {
			con = pool.getConnection();
			sql = "SELECT member.mem_profile_img AS mem_img from member where member.mem_no = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, mem_no);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				result = rs.getString("mem_img");
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return result;
	}

	//post_no에 맞는 댓글 리스트 가지고 오기
	public List<commentBean> getComment(Long comment_post_no, Long page, Long pageSize) {
	    Connection con = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    String sql = null;
	    List<commentBean> list = new ArrayList<>();
	    String commentWriterId = null;
	    String imgPath = null;
	    
	    Long totalCommentCount = totalCommentNumber(comment_post_no);
	    Long totalPageSize = (long) Math.ceil((double) totalCommentCount / pageSize);
	    Long startPage = (page - 1) * pageSize;
	   
	    try {
	        	con = pool.getConnection();
	        	sql = "SELECT * FROM comment WHERE comment_post_no = ? ORDER BY comment_parent, comment_no ASC LIMIT ? OFFSET ?";
	            pstmt = con.prepareStatement(sql);
	            pstmt.setLong(1, comment_post_no);
	            pstmt.setLong(2, pageSize);
	            pstmt.setLong(3, startPage);
	            rs = pstmt.executeQuery();

	            Map<Long, commentBean> commentMap = new HashMap<>();
	            
	            while (rs.next()) {
	                commentBean bean = new commentBean();
	                commentWriterId = findUser(rs.getLong("comment_writer"));
	                imgPath = findMem_img(rs.getLong("comment_writer"));
	                bean.setComment_no(rs.getLong("comment_no"));
	                bean.setComment_content(rs.getString("comment_content"));
	                bean.setComment_time(rs.getString("comment_time"));
	                bean.setComment_writer_id(commentWriterId);
	                bean.setImg_path(imgPath);
	                bean.setComment_writer(rs.getLong("comment_writer"));
	                bean.setComment_post_no(rs.getLong("comment_post_no"));
	                bean.setComment_parent(rs.getLong("comment_parent"));
	                bean.setComment_depth(rs.getInt("comment_depth"));
	                String comment_parent_id = getCommentParentId(rs.getLong("comment_parent"));
	                bean.setComment_parent_id(comment_parent_id);
	                bean.setIsParent(rs.getInt("isParent"));

	                if (bean.getComment_parent() == 0) {
	                	list.add(bean);
	                } else {
	                	commentBean parentComment = commentMap.get(bean.getComment_parent());
	                	if (parentComment != null) {
	                		parentComment.addReply(bean);
	                		commentMap.put(bean.getComment_no(), bean);
	                	}
	                }
	            }
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        pool.freeConnection(con, pstmt, rs);
	    }
	    return list;
	}

	//부모 댓글로 대댓글 구하기
	public List<commentBean> getRepliesList(HttpServletRequest req) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		Long parent_no = Long.parseLong(req.getParameter("parent_no"));
	    List<commentBean> list = new ArrayList<>();
	    String commentWriterId = null;
	    String imgPath = null;

		try {
			con = pool.getConnection();
			sql = "SELECT * FROM comment WHERE comment_parent = ? AND comment_depth > 0 ORDER BY comment_no ASC";
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, parent_no);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				commentBean bean = new commentBean();
                commentWriterId = findUser(rs.getLong("comment_writer"));
                imgPath = findMem_img(rs.getLong("comment_writer"));
                bean.setComment_no(rs.getLong("comment_no"));
                bean.setComment_content(rs.getString("comment_content"));
                bean.setComment_time(rs.getString("comment_time"));
                bean.setComment_writer_id(commentWriterId);
                bean.setImg_path(imgPath);
                bean.setComment_writer(rs.getLong("comment_writer"));
                bean.setComment_post_no(rs.getLong("comment_post_no"));
                bean.setComment_parent(rs.getLong("comment_parent"));
                bean.setComment_depth(rs.getInt("comment_depth"));
                String comment_parent_id = getCommentParentId(rs.getLong("comment_parent"));
                bean.setComment_parent_id(comment_parent_id);
                bean.setIsParent(rs.getInt("isParent"));
                list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return list;
	}
	
	public Long totalCommentNumber(Long comment_post_no)
	{
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		Long result = 0L;
		try {
			con = pool.getConnection();
			sql = "SELECT count(comment_no) AS count FROM comment WHERE comment_post_no = ?";
			pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, comment_post_no);
            rs = pstmt.executeQuery();
            
            if (rs.next())
            {
            	result = rs.getLong("count");
            }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return result;
	}
	public commentBean getComment2(Long comment_no)
	{
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		commentBean bean = new commentBean();
		
		try {
			con = pool.getConnection();
			sql = "SELECT * FROM comment WHERE comment_no = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, comment_no);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
                String commentWriterId = findUser(rs.getLong("comment_writer"));
                bean.setComment_no(rs.getLong("comment_no"));
                bean.setComment_content(rs.getString("comment_content"));
                bean.setComment_time(rs.getString("comment_time"));
                bean.setComment_writer_id(commentWriterId);
                bean.setComment_writer(rs.getLong("comment_writer"));
                bean.setComment_post_no(rs.getLong("comment_post_no"));
                bean.setComment_parent(rs.getLong("comment_parent"));
                bean.setComment_depth(rs.getInt("comment_depth"));
                bean.setIsParent(rs.getInt("isParent"));
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return bean;
	}
	
   //post_no으로 댓글 개수 구하기 (대댓글은 제외함)
	public Long getCommentCount(Long comment_post_no) {
	    Connection con = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    String sql = null;
	    Long commentCount = 0L;

	    try {
	        con = pool.getConnection();
	        sql = "SELECT COUNT(*) AS count FROM comment WHERE comment_post_no = ? AND comment_depth = 0";
	        pstmt = con.prepareStatement(sql);
	        pstmt.setLong(1, comment_post_no);
	        rs = pstmt.executeQuery();

	        if (rs.next()) {
	            commentCount = rs.getLong("count");
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        pool.freeConnection(con, pstmt, rs);
	    }

	    return commentCount;
	}

	
	
	public String findUser(Long comment_writer)
	{
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String result = null;
		try {
			con = pool.getConnection();
			sql = "SELECT member.mem_id AS writer_id FROM comment "
					+ "JOIN member ON comment.comment_writer = member.mem_no "
					+ "WHERE comment.comment_writer = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, comment_writer);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				result = rs.getString("writer_id");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return result;
	}

	
	/*
	 * public Long member_no(String mem_id) { Connection con = null;
	 * PreparedStatement pstmt = null; String sql = null; ResultSet rs = null; Long
	 * result = 0L; try { con = pool.getConnection(); sql =
	 * "SELECT distinct member.mem_no FROM member JOIN comment ON member.mem_id = ?"
	 * ; pstmt = con.prepareStatement(sql); pstmt.setString(1 , mem_id); rs =
	 * pstmt.executeQuery(); if (rs.next()) { result = rs.getLong("mem_no"); } }
	 * catch (Exception e) { e.printStackTrace(); } finally {
	 * pool.freeConnection(con, pstmt, rs); } return result; }
	 */
	
	// 댓글 입력 처리
	public Long insertComment(HttpServletRequest req) {
	    Connection con = null;
	    PreparedStatement pstmtSelect = null;
	    PreparedStatement pstmtInsert = null;
	    ResultSet rs = null;
	    String sql = null;
	    boolean flag = false;
	    int result = 0;
	    Long commentNo = 0L;
	    Long comment_writer = 0L;
	    String comment_content = req.getParameter("comment_content");
	    Long comment_post_no = Long.parseLong(req.getParameter("comment_post_no"));
	    Long parent_comment_no = 0L;
	    int comment_depth = 0;
	    if (req.getParameter("parent_comment_no") != null && !req.getParameter("parent_comment_no").isEmpty() &&
	    	req.getParameter("comment_depth") != null && !req.getParameter("comment_depth").isEmpty())
	    {
		    parent_comment_no = Long.parseLong(req.getParameter("parent_comment_no"));
		    comment_depth = Integer.parseInt(req.getParameter("comment_depth"));
	    }
	    try {
	        con = pool.getConnection();
	        sql = "SELECT  member.mem_no FROM member WHERE member.mem_id = ?";
	        pstmtSelect = con.prepareStatement(sql);
	        pstmtSelect.setString(1, req.getParameter("comment_writer"));
	        rs = pstmtSelect.executeQuery();

	        if (rs.next()) {
	            comment_writer = rs.getLong("mem_no");
	            result = 1;
	        }

	        if (result == 1) {
	            pool.freeConnection(con, pstmtSelect, rs);

	            con = pool.getConnection();
	            sql = "INSERT INTO comment(comment_content, comment_time, comment_writer, comment_post_no, comment_parent, comment_depth) VALUES (?, now(), ?, ?, ?, ?)";
	            pstmtInsert = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	            pstmtInsert.setString(1, comment_content);
	            pstmtInsert.setLong(2, comment_writer);
	            pstmtInsert.setLong(3, comment_post_no);
	            pstmtInsert.setLong(4, parent_comment_no);
	            pstmtInsert.setInt(5, comment_depth);
            
	            if (pstmtInsert.executeUpdate() == 1) {
	            	UpdateIsParent(parent_comment_no);
	                rs = pstmtInsert.getGeneratedKeys();
	                if (rs.next()) {
	                    commentNo = rs.getLong(1);
	                }
	            }
	        } else {
	            flag = false;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        pool.freeConnection(con, pstmtSelect);
	        pool.freeConnection(con, pstmtInsert);
	    }
	    return commentNo;
	}

	//부모 댓글 isParent true로 변경
	public boolean UpdateIsParent(Long comment_no) {
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = null;
		ResultSet rs = null;
		boolean flag = false;
		try {
			con = pool.getConnection();
			sql = "UPDATE comment SET isParent = 1 WHERE comment_no = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, comment_no);
			int result = pstmt.executeUpdate();
			if (result == 1)
			{
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return flag;
	}
	
	//댓글 삭제
	public int deleteCommentAction(Long comment_post_no, Long comment_no) {
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = null;
		ResultSet rs = null;
		int flag = -1;
		try {
			con = pool.getConnection();
			sql = "DELETE FROM comment WHERE comment_post_no=? AND comment_no = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, comment_post_no);
			pstmt.setLong(2, comment_no);
			flag = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		//실패시 -1 반환
		return flag;
	}
	
	//댓글 번호로 댓글 작성자 아이디 구하기
	public String getCommentParentId(Long comment_parent)
	{
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = null;
		ResultSet rs = null;
		String id = null;
		boolean flag = false;
		try {
			con = pool.getConnection();
			sql = "SELECT member.mem_id AS userId FROM comment JOIN member "
					+ "on comment.comment_writer = member.mem_no WHERE comment_no = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, comment_parent);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				id = rs.getString("userId");
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		} 
		
		if (flag == true) {
			return id;
		}
		return id;
	}
	
	//comment no로 내용 불러오기
	public String commentContentbyno(Long comment_no)
	{
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String post_content = null;
		try {
			con = pool.getConnection();
			String sql = "SELECT comment_content FROM comment WHERE comment_no = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, comment_no);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				post_content = rs.getString("comment_content");
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return post_content;
		
	}
}
