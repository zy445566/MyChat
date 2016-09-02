package com.slyim.pojo;

public class DynamicCircleMongo {
	private String id;
	private long sendTime;
	private String content;
	private String type;
	private long favorNum=0;
	private UserBase sender;
	private Localtion localtion;
	private String commentId=null;
	private String forwardId=null;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public long getSendTime() {
		return sendTime;
	}
	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getFavorNum() {
		return favorNum;
	}
	public void setFavorNum(long favorNum) {
		this.favorNum = favorNum;
	}
	public UserBase getSender() {
		return sender;
	}
	public void setSender(UserBase sender) {
		this.sender = sender;
	}
	public Localtion getLocaltion() {
		return localtion;
	}
	public void setLocaltion(Localtion localtion) {
		this.localtion = localtion;
	}
	public String getCommentId() {
		return commentId;
	}
	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}
	public String getForwardId() {
		return forwardId;
	}
	public void setForwardId(String forwardId) {
		this.forwardId = forwardId;
	}
}
