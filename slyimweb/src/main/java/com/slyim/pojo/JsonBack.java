package com.slyim.pojo;

public class JsonBack {
	private String code;
	private String msg;
	private Object res;
	
	public JsonBack(){
		this.code="";
		this.msg="";
		this.res="";
	}
	
	public JsonBack(String code, String msg, Object res){
		this.code=code;
		this.msg=msg;
		this.res=res;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getRes() {
		return res;
	}
	public void setRes(Object res) {
		this.res = res;
	}
	
}
