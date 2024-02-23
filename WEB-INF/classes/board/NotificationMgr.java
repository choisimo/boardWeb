package board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;

import DBconnector.DBConnectionMgr;

public class NotificationMgr {

	
	private DBConnectionMgr pool;
	private static final String ENCTYPE = "UTF-8";
 
	public NotificationMgr()	{
		try {
			pool = DBConnectionMgr.getInstance();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public Long insert_commentAlert(Long comment_no)
	{
		Connection con = null;
		PreparedStatement pstmt = null;
	    ResultSet rs = null;
		int result2 = 0;
		String sql = null;
		commentMgr cMgr = new commentMgr();
		commentBean bean = cMgr.getComment2(comment_no);
		Long notification_no = 0L;
		try {
			con = pool.getConnection();
			sql = "INSERT INTO notification(notification_post_no, notification_writer, notification_comment_no, notification_read, notification_type, regdate) "
					+ "VALUES(?, ?, ?,  0, 'comment', now())";
			pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setLong(1, bean.getComment_post_no().longValue());
			pstmt.setLong(2, bean.getComment_writer().longValue());
			pstmt.setLong(3, bean.getComment_no().longValue());
			result2 = pstmt.executeUpdate();
			if (result2 > 0)
			{
				rs = pstmt.getGeneratedKeys();
				
				if (rs.next()) {
					notification_no = rs.getLong(1);
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt);
		}
		return notification_no;
	}
	
	public Long insert_commentAlert2(Long comment_no, Long parent_comment_no)
	{
		Connection con = null;
		PreparedStatement pstmt = null;
	    ResultSet rs = null;
		int result = 0;
		String sql = null;
		commentMgr cMgr = new commentMgr();
		commentBean bean = cMgr.getComment2(comment_no);
		Long notification_no = 0L;
		try {
			con = pool.getConnection();
			sql = "INSERT INTO notification(notification_post_no, notification_writer, notification_comment_no, notification_read, notification_type, regdate, comment_reply_parent) "
					+ "VALUES(?, ?, ?,  0, 'comment_reply', now(), ?)";
			pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setLong(1, bean.getComment_post_no().longValue());
			pstmt.setLong(2, bean.getComment_writer().longValue());
			pstmt.setLong(3, bean.getComment_no().longValue());
			pstmt.setLong(4, parent_comment_no);
			result = pstmt.executeUpdate();
			if (result > 0)
			{
				rs = pstmt.getGeneratedKeys();
				
				if (rs.next()) {
					notification_no = rs.getLong(1);
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt);
		}
		return notification_no;
	}

	public NotificationBean getCommentAlert(Long notification_no)
	{
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		NotificationBean bean = new NotificationBean();
		
		try {
			con = pool.getConnection();
			sql = "SELECT * FROM notification WHERE notification_no = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, notification_no);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
                bean.setNotification_no(rs.getLong("notification_no"));
                bean.setNotification_post_no(rs.getLong("notification_post_no"));
                bean.setNotification_writer(rs.getLong("notification_writer"));
                bean.setNotification_read(rs.getInt("notification_read"));
                bean.setNotification_type(rs.getString("notification_type"));
                bean.setNotification_comment_no(rs.getLong("notification_comment_no"));
                bean.setComment_reply_parent(rs.getLong("comment_reply_parent"));
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return bean;
	}

	//사용자 알림 
	public List <NotificationBean> getCommentAlert2(Long mem_no, int type){
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		List <NotificationBean> list = new ArrayList <NotificationBean>();
		try {
			con = pool.getConnection();
			if (type == 0)
			{
				sql = "SELECT * FROM notification WHERE notification_post_no IN (SELECT post_no FROM board WHERE post_writer = ?) AND notification_read = ? AND notification_writer != ? ORDER BY notification_no DESC";
					pstmt = con.prepareStatement(sql);
					pstmt.setLong(1, mem_no);
					pstmt.setInt(2, 0);
					pstmt.setLong(3, mem_no);
					
			} else if (type == 1) {
				sql = "SELECT * FROM notification "
						+ "INNER JOIN comment ON notification.comment_reply_parent "
						+ "= comment.comment_no WHERE comment.comment_writer = ? AND "
						+ "notification.notification_type = 'comment_reply' AND notification.notification_read != ? "
						+ "ORDER BY notification_no DESC";
					pstmt = con.prepareStatement(sql);
					pstmt.setLong(1, mem_no);
					pstmt.setInt(2, 1);
			}

			rs = pstmt.executeQuery();
			
	        while (rs.next()) {
	        	commentMgr cMgr = new commentMgr();
	        	BoardMgr bMgr = new BoardMgr();
	            NotificationBean bean = new NotificationBean();
	            bean.setNotification_no(rs.getLong("notification_no"));
	            bean.setNotification_post_no(rs.getLong("notification_post_no"));
	            bean.setNotification_writer(rs.getLong("notification_writer"));
	            bean.setNotification_read(rs.getInt("notification_read"));
	            bean.setNotification_type(rs.getString("notification_type"));
	            bean.setRegdate(rs.getString("regdate"));
	            bean.setNotification_comment_no(rs.getLong("notification_comment_no"));
                bean.setComment_reply_parent(rs.getLong("comment_reply_parent"));
                bean.setComment_writer_id(cMgr.getCommentParentId(rs.getLong("notification_comment_no")));
                if (rs.getString("notification_type").equals("comment")) {
                	bean.setAlertContent(bMgr.boardContentbyno(rs.getLong("notification_post_no")));
                } else if (rs.getString("notification_type").equals("comment_reply")) 
                	bean.setAlertContent(cMgr.commentContentbyno(rs.getLong("comment_reply_parent")));
	            list.add(bean);
	        }

		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return list;
	}


	public boolean updateCommentAlert(Long post_no, Long post_writer)
	{
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = null;
		boolean flag = false;
		try {
			con = pool.getConnection();
			sql = "UPDATE notification SET notification_read = 1 WHERE notification_post_no = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, post_no);
			int result = pstmt.executeUpdate();
			if (result == 1)
			{
				flag = true;
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt);
		}
		return flag;
	}
	
	public boolean updateCommentAlert2(Long post_no, Long comment_writer_id)
	{
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = null;
		boolean flag = false;
		try {
			con = pool.getConnection();
			sql = "UPDATE notification SET notification_read = 1 WHERE notification_post_no = ? AND notification_type ='comment_reply' AND "
					+ "comment_reply_parent IN (SELECT comment_no FROM comment WHERE comment_writer = ?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, post_no);
			pstmt.setLong(2, comment_writer_id);
			int result = pstmt.executeUpdate();
			if (result == 1)
			{
				flag = true;
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt);
		}
		return flag;
	}
	


}
