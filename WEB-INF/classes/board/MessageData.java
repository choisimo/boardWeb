package board;

public class MessageData {
    private String message;
    private Long post_no;
    private Long post_likecount;
    private Long comment_no;
    private Long count;
    private String mem_id;
    
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Long getPost_no() {
		return post_no;
	}
	public void setPost_no(Long post_no) {
		this.post_no = post_no;
	}
	public Long getPost_likecount() {
		return post_likecount;
	}
	public void setPost_likecount(Long post_likecount) {
		this.post_likecount = post_likecount;
	}
	public Long getComment_no() {
		return comment_no;
	}
	public void setComment_no(Long comment_no) {
		this.comment_no = comment_no;
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public String getMem_id() {
		return mem_id;
	}
	public void setMem_id(String mem_id) {
		this.mem_id = mem_id;
	}
	
}

