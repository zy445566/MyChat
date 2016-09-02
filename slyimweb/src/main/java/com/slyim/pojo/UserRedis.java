package com.slyim.pojo;

import java.io.Serializable;

public class UserRedis implements Serializable {

	private String id;
	private String name;
	private String avatar;

	private static final long serialVersionUID = 1L; 
	private String password;

	public UserRedis() {  
        super(); 
    }
	public UserRedis(String id, String name, String password) {  
        super();  
        this.setId(id);  
        this.setName(name);  
        this.setPassword(password);  
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
}
