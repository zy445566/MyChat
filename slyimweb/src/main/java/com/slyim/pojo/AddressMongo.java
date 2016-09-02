package com.slyim.pojo;
//这个class的信息，除了id是自己id，其它信息都是好友信息，id是自己的便于查找
public class AddressMongo{
	private String id;
	//好友名字
	private String friendName;
	//好友头像
	private String friendAvatar;
	//好友签名
	private String friendSign=null;
	//状态 0为申请好友，1为好友，2为拉黑（显示在黑名单中）
	private int state=0;
	//frienId,好友id
	private String friendId;
	//我的id
	private String myId;
	//申请好友留言
	private String commit;
	
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getFriendId() {
		return friendId;
	}
	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}
	public String getCommit() {
		return commit;
	}
	public void setCommit(String commit) {
		this.commit = commit;
	}
	public String getMyId() {
		return myId;
	}
	public void setMyId(String myId) {
		this.myId = myId;
	}
	public String getFriendName() {
		return friendName;
	}
	public void setFriendName(String friendname) {
		this.friendName = friendname;
	}
	public String getFriendAvatar() {
		return friendAvatar;
	}
	public void setFriendAvatar(String friendavatar) {
		this.friendAvatar = friendavatar;
	}
	public String getFriendSign() {
		return friendSign;
	}
	public void setFriendSign(String friendsign) {
		this.friendSign = friendsign;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
