package com.gao.aym.bean;

public class CommentInfo {
	private int no; //自增，种子为1
	private int id;//作品标志id
	private String phonenum;//发布者手机号
	private String msg;//评论内容
	private String date;//评论发布时间
	
	public CommentInfo() {
		// TODO Auto-generated constructor stub
	}
	public CommentInfo( int no,int id, String phonenum, String msg,String date) {
		super();
		this.no=no;
		this.id = id;
		this.phonenum = phonenum;
		this.msg = msg;
		this.date=date;
	}
	public CommentInfo(int id, String phonenum, String msg,String date) {
		super();
		this.id = id;
		this.phonenum = phonenum;
		this.msg = msg;
		this.date=date;
	}
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPhonenum() {
		return phonenum;
	}
	public void setPhonenum(String phonenum) {
		this.phonenum = phonenum;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	@Override
	public String toString() {
		return "CommentInfo [no=" + no + ", id=" + id + ", phonenum="
				+ phonenum + ", msg=" + msg + ", date=" + date + "]";
	}
	
	

}


