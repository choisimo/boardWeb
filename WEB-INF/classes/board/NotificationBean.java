package board;

public class NotificationBean {
	Long notification_no;
	Long notification_comment_no;
	Long notification_post_no;
	Long notification_writer;
	int notification_read;
	String notification_type;
	Long comment_reply_parent;
	String regdate;
	//밑은 DB에 없고 temporary 하게 사용
	String AlertContent;
	String comment_writer_id;
	
	public Long getNotification_comment_no() {
		return notification_comment_no;
	}
	public void setNotification_comment_no(Long notification_comment_no) {
		this.notification_comment_no = notification_comment_no;
	}
	public Long getNotification_no() {
		return notification_no;
	}
	public void setNotification_no(Long notification_no) {
		this.notification_no = notification_no;
	}
	public Long getNotification_post_no() {
		return notification_post_no;
	}
	public void setNotification_post_no(Long notification_post_no) {
		this.notification_post_no = notification_post_no;
	}
	public Long getNotification_writer() {
		return notification_writer;
	}
	public void setNotification_writer(Long notification_writer) {
		this.notification_writer = notification_writer;
	}
	public int getNotification_read() {
		return notification_read;
	}
	public void setNotification_read(int notification_read) {
		this.notification_read = notification_read;
	}
	public String getNotification_type() {
		return notification_type;
	}
	public void setNotification_type(String notification_type) {
		this.notification_type = notification_type;
	}
	public Long getComment_reply_parent() {
		return comment_reply_parent;
	}
	public void setComment_reply_parent(Long comment_reply_parent) {
		this.comment_reply_parent = comment_reply_parent;
	}
	public String getRegdate() {
		return regdate;
	}
	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}
	public String getAlertContent() {
		return AlertContent;
	}
	public void setAlertContent(String alertContent) {
		AlertContent = alertContent;
	}
	public String getComment_writer_id() {
		return comment_writer_id;
	}
	public void setComment_writer_id(String comment_writer_id) {
		this.comment_writer_id = comment_writer_id;
	}
	
	
}
