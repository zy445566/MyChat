package com.slyim.pojo;

public class CalendarMongo {
	private String id;
	private long startTime;
	private long endTime;
	private String event;
	private String address;
	private UserBase sponsor;
	private UserBase acceptor;
	private float rent;
//	1已经发起，
//	2为已经接受，
//	3为接受者放鸽子，
//	4为发起人没来，
//	5为接受者已退款，
//	6为已纠纷（发起人说放鸽子，接受者不退款）
//	7订单已取消（不可评分）
//	8订单成功（可评分）
//	9订单完成（评分结束）
	private int state;
	private int star;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public UserBase getAcceptor() {
		return acceptor;
	}
	public void setAcceptor(UserBase acceptor) {
		this.acceptor = acceptor;
	}
	public UserBase getSponsor() {
		return sponsor;
	}
	public void setSponsor(UserBase sponsor) {
		this.sponsor = sponsor;
	}
	public float getRent() {
		return rent;
	}
	public void setRent(float rent) {
		this.rent = rent;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getStar() {
		return star;
	}
	public void setStar(int star) {
		this.star = star;
	}
}
