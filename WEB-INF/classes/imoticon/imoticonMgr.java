package imoticon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import DBconnector.DBConnectionMgr;
import board.boardBean;

public class imoticonMgr {

	private DBConnectionMgr pool;
	private static final String ENCTYPE = "UTF-8";

	public imoticonMgr()	{
		try {
			pool = DBConnectionMgr.getInstance();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	//이모티콘 목록 리스트 불러오기 
	public List <imoticonBean> getImoticons(Long mem_no, int imoticonNum, int pageSize){
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		List <imoticonBean> list = new ArrayList <imoticonBean>();
		int offset = 0;
		if (imoticonNum != 0) {
			offset = (imoticonNum - 1) * pageSize;
		}
		try {
			con = pool.getConnection();
			sql = "SELECT * FROM imoticon WHERE imoticon_owner = ? ORDER BY imoticon_no DESC LIMIT ? offset ? ";
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, mem_no);
			pstmt.setInt(2, pageSize);
			pstmt.setInt(3, offset);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				imoticonBean bean = new imoticonBean();
				bean.setImoticon_no(rs.getLong("imoticon_no"));
				bean.setImoticon_price(rs.getLong("imoticon_price"));
				bean.setImoticon_name(rs.getString("imoticon_name"));
				bean.setImoticon_own_date(rs.getString("imoticon_own_date"));
				list.add(bean);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return list;
	}
	
}
