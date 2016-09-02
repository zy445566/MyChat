package com.slyim.common;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.slyim.pojo.UserMongo;
import com.slyim.service.impl.UserServiceMongo;

@Component
public class MyUser {
	
	private static UserMongo userMongo;
	
	private static UserServiceMongo userServiceMongo;
	
	@Autowired
	public void setUserServiceMongo(UserServiceMongo userServiceMongo) {
		MyUser.userServiceMongo=userServiceMongo;
	}
	
	public static void makeLoginInfo(HttpServletResponse response, HttpSession session, UserMongo userMongo)
	{
		Map<String,Cookie> cookieMap = new HashMap<String,Cookie>();
		cookieMap.put("id", new Cookie("id", userMongo.getId()));
		try {
			cookieMap.put("name", new Cookie("name", URLEncoder.encode(userMongo.getName(), "UTF-8")));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			cookieMap.put("name", new Cookie("name", null));
			e.printStackTrace();
		}
		cookieMap.put("phone", new Cookie("phone", userMongo.getPhone()));
		cookieMap.put("password", new Cookie("password", userMongo.getPassword()));
		cookieMap.get("id").setMaxAge(60*60*24*90);
		cookieMap.get("name").setMaxAge(60*60*24*90);
		cookieMap.get("phone").setMaxAge(60*60*24*90);
		cookieMap.get("password").setMaxAge(60*60*24*90);
		for (String cookiename : cookieMap.keySet()){
			cookieMap.get(cookiename).setPath("/");
		}
		response.addCookie(cookieMap.get("id"));
		response.addCookie(cookieMap.get("name"));
		response.addCookie(cookieMap.get("phone"));
		response.addCookie(cookieMap.get("password"));
		session.setAttribute("id", userMongo.getId());
		session.setAttribute("phone", userMongo.getPhone());
		session.setAttribute("password", userMongo.getPassword());
		session.setAttribute("name", userMongo.getName());
	}
	
	public static void makeLoginOut(HttpServletResponse response, HttpSession session)
	{
		Map<String,Cookie> cookieMap = new HashMap<String,Cookie>();
		cookieMap.put("id", new Cookie("id", null));
		cookieMap.put("name", new Cookie("name", null));
		cookieMap.put("phone", new Cookie("phone", null));
		cookieMap.put("password", new Cookie("password", null));
		cookieMap.get("id").setMaxAge(-1);
		cookieMap.get("name").setMaxAge(-1);
		cookieMap.get("phone").setMaxAge(-1);
		cookieMap.get("password").setMaxAge(-1);
		response.addCookie(cookieMap.get("id"));
		response.addCookie(cookieMap.get("name"));
		response.addCookie(cookieMap.get("phone"));
		response.addCookie(cookieMap.get("password"));
		session.setAttribute("id", null);
		session.setAttribute("phone", null);
		session.setAttribute("password", null);
		session.setAttribute("name", null);
	}
	
	public static boolean islogin(HttpServletResponse response, HttpServletRequest request){
		 HttpSession session=request.getSession();
		 String id=(String)session.getAttribute("id");
		 Cookie[] cookies=request.getCookies();
		 Map<String,Cookie> cookieMap =MyUser.ReadCookieMap(cookies);
//		 System.out.println("islogin");
		 if (MyUser.isEmpty(id)) {
			 if(cookieMap.get("phone")==null || cookieMap.get("password")==null){
//				 System.out.println("用户本地Cookie没储存");
				 return false;
			 }
			 String phone=cookieMap.get("phone").getValue();
			 String password=cookieMap.get("password").getValue();
//			 System.out.println(phone);
			 System.out.println(MyUser.userServiceMongo);
			 MyUser.userMongo=MyUser.userServiceMongo.findOneByPhone(phone);
			 if (phone.equals(MyUser.userMongo.getPhone()) && 
				password.equals(MyUser.userMongo.getPassword())) {
				 MyUser.makeLoginInfo(response, session, MyUser.userMongo);
//				 System.out.println("用户重新登陆了");
				 MyUser.userMongo.setModifyTime(System.currentTimeMillis()/1000);
				 if (MyUser.userMongo.getVipDeadLine()>System.currentTimeMillis()/1000){
					 while ((System.currentTimeMillis()/1000)-MyUser.userMongo.getVipRefreshTime()>=24*3600) {
						 MyUser.userMongo.setVipRefreshTime(MyUser.userMongo.getVipRefreshTime()+24*3600);
						 MyUser.userMongo.setVipScore(MyUser.userMongo.getVipScore()+10);
					 }
				 } else {
					 while (MyUser.userMongo.getVipDeadLine()-MyUser.userMongo.getVipRefreshTime()>=24*3600) {
							 MyUser.userMongo.setVipRefreshTime(MyUser.userMongo.getVipRefreshTime()+24*3600);
							 MyUser.userMongo.setVipScore(MyUser.userMongo.getVipScore()+10);
					 }
					 if (MyUser.userMongo.getVipScore()>0) {
						 while ((System.currentTimeMillis()/1000)-MyUser.userMongo.getVipRefreshTime()>=24*3600) {
							 MyUser.userMongo.setVipRefreshTime(MyUser.userMongo.getVipRefreshTime()+24*3600);
							 if (MyUser.userMongo.getVipScore()-5>=0) {
								 MyUser.userMongo.setVipScore(MyUser.userMongo.getVipScore()-5);
							 } else {
								 MyUser.userMongo.setVipScore(0);
							 }
							 
						 }
					 }
				 }
				 MyUser.userServiceMongo.update(MyUser.userMongo);
				 return true;
			}
		 } else {
			 //System.out.println("Sesstion存在ID");
			 return true;
		 }
//		 System.out.println("用户登陆失败了");
		 return false;
	 }
	
	private static boolean isEmpty(String str){
		 if (str==null || str.equals("")) {
			 return true;
		 } else {
			 return false;
		 }
	 }
	 
	 private static Map<String,Cookie> ReadCookieMap(Cookie[] cookies){ 
		  Map<String,Cookie> cookieMap = new HashMap<String,Cookie>();
		  if(null!=cookies){
		    for(Cookie cookie : cookies){
		     cookieMap.put(cookie.getName(), cookie);
		    }
		  }
		  return cookieMap;
	}
}
