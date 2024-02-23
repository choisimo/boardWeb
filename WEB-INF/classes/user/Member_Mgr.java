package user;

import java.lang.reflect.Member;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import user.SHA256;

import DBconnector.DBConnectionMgr;


public class Member_Mgr {
	
	private DBConnectionMgr pool;
	private static final String ENCTYPE = "UTF-8";
 
	public Member_Mgr()	{
		try {
			pool = DBConnectionMgr.getInstance();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// 회원 정보 ===============================================================================
	// ID 중복 체크
	public boolean checkId(String mem_id) {
		DBConnectionMgr pool = new DBConnectionMgr();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		boolean flag = false;
		try {
			con = pool.getConnection();
			sql = "select mem_id from member where mem_id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, mem_id);
			flag = pstmt.executeQuery().next();	
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return flag;
	}

	// 로그인
	public String loginMember(String mem_login_id, String mem_pw) {
		DBConnectionMgr pool = new DBConnectionMgr();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		boolean flag = false;
		String mem_id = null;
		String encrypt_mem_pw = SHA256.endcodSha256(mem_pw);
		try {
			con = pool.getConnection();
			sql = "SELECT mem_id FROM member WHERE mem_login_id =? AND mem_pw= ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, mem_login_id);
			pstmt.setString(2, encrypt_mem_pw);
			rs = pstmt.executeQuery();
			flag = rs.next();
			if (flag == true) {
				mem_id = getMem_idBYmem_login_id(mem_login_id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return mem_id;
	}
	
	public String getMem_idBYmem_login_id(String mem_login_id) {
		DBConnectionMgr pool = new DBConnectionMgr();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String result = null;
		try {
			con = pool.getConnection();
			String sql = "SELECT mem_id FROM member WHERE mem_login_id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, mem_login_id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = rs.getString("mem_id");
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return result;
	}

	// 회원 정보 수정
	public boolean updateMember(String mem_id, String name, String phone) {
		DBConnectionMgr pool = new DBConnectionMgr();
		Connection con = null;
		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			con = pool.getConnection();
			String sql = "UPDATE `member` SET `mem_name` = ?, `mem_phone`= ? WHERE `mem_id` = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setString(2, phone);
			pstmt.setString(3, mem_id);
			if (pstmt.executeUpdate() == 1) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt);
		}
		return flag;
	}
	

	// 회원 정보 삭제
	public void deleteMember(String mem_id) {
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = null;
		ResultSet rs = null;
		try {
			con = pool.getConnection();
			sql = "delete from `member` where `mem_id` = ? ";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, mem_id);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
	}
	
	public String registerMember(HttpServletRequest req) {
	    try (
	         Connection con = pool.getConnection()) {

	        String mem_id = req.getParameter("mem_id");
	        String mem_pw = req.getParameter("mem_pw");
	        String mem_name = req.getParameter("mem_name");
	        String mem_profile_img = req.getParameter("mem_profile_img");
	        String mem_login_id = req.getParameter("login_id");

	        boolean id_check = checkId(mem_id);

	        if (id_check) {
	            return "400/이미 사용중인 아이디입니다.";
	        } else {
	            String hashedPassword = SHA256.endcodSha256(mem_pw);

	            try (PreparedStatement pstmt = con.prepareStatement(
	                    "INSERT INTO member (`mem_id`, `mem_pw`, `mem_name`, `mem_ac`, `mem_profile_img`, `mem_phone`, `mem_regdate`, `mem_login_id`) VALUES (?, ?, ?, ?, ?, ?, now(), ?)")) {
	                pstmt.setString(1, mem_id);
	                pstmt.setString(2, hashedPassword);
	                pstmt.setString(3, mem_name);
	                pstmt.setString(4, "user");
	                pstmt.setString(5, mem_profile_img);
	                pstmt.setString(6, "01012341234");
	                pstmt.setString(7, mem_login_id);
	                pstmt.executeUpdate();
	            }

	            return "200/회원가입에 성공했습니다.";
	        }
	    } catch (Exception e) {
	        e.printStackTrace(); // Log the exception or use a logging framework
	        return "500/서버 오류가 발생했습니다."; // Generic error response for the client
	    }
	}


	public boolean checkmem_name(String mem_name) {
		DBConnectionMgr pool = new DBConnectionMgr();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		boolean flag = false;
		try {
			con = pool.getConnection();
			sql = "select mem_name from member where mem_name = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, mem_name);
			flag = pstmt.executeQuery().next();	
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return flag;
	}

	
	public List<String> findId(String name, String p_num) {
		DBConnectionMgr pool = new DBConnectionMgr();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		List<String> res = new ArrayList<String>();
		try {
			con = pool.getConnection();
			sql = "select mem_id from member where mem_name = ? AND mem_phone = ? ";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setString(2, p_num);
			rs = pstmt.executeQuery();
			
			
			while (rs.next()) {
				res.add(rs.getString("mem_id"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return res;
	}
	
	public boolean findpw(String id, String name , String phone ) {
		DBConnectionMgr pool = new DBConnectionMgr();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		boolean res = false;
		try {
			con = pool.getConnection();
			sql = "select mem_id from member where mem_id = ? AND mem_name = ? AND mem_phone = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, name);
			pstmt.setString(3, phone);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				res = true;
			} else {
				res = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return res;
	
	}

	public boolean change_pw(Member_Bean bean) {
		DBConnectionMgr pool = new DBConnectionMgr();
		Connection con = null;
		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			con = pool.getConnection();
			String sql = "update member set mem_pw=SHA2('xq" + bean.getMem_pw() + "q43',256) where mem_id=?";
			System.out.println(sql);
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, bean.getMem_id());
			int count = pstmt.executeUpdate();
			if (count > 0)
				flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt);
		}
		return flag;
	}
	
	//mem_no로 mem_id 찾기
	public String findUser(Long post_writer)
	{
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String result = null;
		try {
			con = pool.getConnection();
			sql = "SELECT member.mem_id AS writer_id FROM board "
					+ "JOIN member ON board.post_writer = member.mem_no "
					+ "WHERE board.post_writer = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, post_writer);
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
	//mem_id로 mem_no 찾기
	public Long find_mem_no (String mem_id)
	{
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		Long result = 0L;
		try {
			con = pool.getConnection();
			sql = "SELECT member.mem_no AS mem_no from member where member.mem_id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, mem_id);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				result = rs.getLong("mem_no");
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return result;
	}
	
	//mem_no로 사용자 권한 등급 체크 
	public String find_mem_ac (Long mem_no)
	{
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String result = null;
		try {
			con = pool.getConnection();
			sql = "SELECT mem_ac from member where mem_no = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, mem_no);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				result = rs.getString("mem_ac");
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return result;
	}
	

}
