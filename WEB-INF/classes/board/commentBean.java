package board;

import java.util.ArrayList;
import java.util.List;

public class commentBean {
	Long comment_no;
	String comment_content;
	String comment_time;
	Long comment_writer;
	Long comment_post_no;
	String comment_writer_id;
	String img_path;
	int comment_depth;
	Long comment_parent;
	String comment_parent_id;
	int isParent;
	
	private List<commentBean> replies;
	
	public commentBean() {
		this.replies = new ArrayList<>();
	}
	public List<commentBean> getReplies(){
		return replies;
	}
	public void addReply(commentBean reply) {
		replies.add(reply);
	}
	
	public Long getComment_no() {
		return comment_no;
	}
	public void setComment_no(Long comment_no) {
		this.comment_no = comment_no;
	}
	public String getComment_content() {
		return comment_content;
	}
	public void setComment_content(String comment_content) {
		this.comment_content = comment_content;
	}
	public String getComment_time() {
		return comment_time;
	}
	public void setComment_time(String comment_time) {
		this.comment_time = comment_time;
	}
	public Long getComment_writer() {
		return comment_writer;
	}
	public void setComment_writer(Long comment_writer) {
		this.comment_writer = comment_writer;
	}
	public Long getComment_post_no() {
		return comment_post_no;
	}
	public void setComment_post_no(Long comment_post_no) {
		this.comment_post_no = comment_post_no;
	}
	public String getComment_writer_id() {
		return comment_writer_id;
	}
	public void setComment_writer_id(String comment_writer_id) {
		this.comment_writer_id = comment_writer_id;
	}
	public String getImg_path() {
		return img_path;
	}
	public void setImg_path(String img_path) {
		this.img_path = img_path;
	}
	public int getComment_depth() {
		return comment_depth;
	}
	public void setComment_depth(int comment_depth) {
		this.comment_depth = comment_depth;
	}
	public Long getComment_parent() {
		return comment_parent;
	}
	public void setComment_parent(Long comment_parent) {
		this.comment_parent = comment_parent;
	}
	public String getComment_parent_id() {
		return comment_parent_id;
	}
	public void setComment_parent_id(String comment_parent_id) {
		this.comment_parent_id = comment_parent_id;
	}
	public int getIsParent() {
		return isParent;
	}
	public void setIsParent(int isParent) {
		this.isParent = isParent;
	}
	
	
}
