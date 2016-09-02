package com.slyim.controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;



import com.slyim.common.MyUser;
import com.slyim.common.ReadWriteBase.FileType;
import com.slyim.pojo.JsonBack;

import com.slyim.pojo.UserMongo;
//import com.slyim.pojo.UserRedis;
import com.slyim.service.impl.CheckEvilServiceRedis;
import com.slyim.service.impl.UserServiceMongo;
import com.slyim.service.impl.UserServiceRedis;
//import com.slyim.service.impl.UserServiceRedis;


@Controller
@RequestMapping("/user") 
public class UserController {
	
	private JsonBack jsonBack = new JsonBack();
	private UserMongo userMongo=new UserMongo();
	@Autowired
	private UserServiceMongo userServiceMongo;
	@Autowired
	private UserServiceRedis userServiceRedis;
	@Autowired
	private CheckEvilServiceRedis checkEvilServiceRedis;

	
	
	
	private boolean checkEvilStr(String checkKey){
		if (!checkEvilServiceRedis.isExists(checkKey)) {
			checkEvilServiceRedis.set(checkKey, 1, 3600);
		}
		int checkCount=checkEvilServiceRedis.get(checkKey);
		if (checkCount>10) {
			return true;
		} else {
			checkEvilServiceRedis.incr(checkKey, 3600);
			return false;
		}
		
	}
	
	//封IP和封手机一个小时,redis设置一个小时的过期时间，1小时IP或手机超过5次恶意就封杀1个小时
	private boolean checkEvil(HttpServletRequest request, String phone)
	{
		 String ip = request.getHeader("x-forwarded-for");
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
             ip = request.getHeader("Proxy-Client-IP");
         }
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
             ip = request.getHeader("WL-Proxy-Client-IP");
         }
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
             ip = request.getRemoteAddr();
         }
         if (this.checkEvilStr(ip) || this.checkEvilStr(phone)) {
        	 return true;
         } else {
        	 return false;
         }
	}
	
	private String getPassword()
	{
		int password;
		Date date = new Date();
		long now=date.getTime();
		Random random = new Random(now);
		password=(Math.abs(random.nextInt()))%1000000;
		while (password<100000) {
			password=password*10;
		}		
		return String.valueOf(password);
	}
	
	private boolean checkPhone(String phone)
	{
		if (phone==null) {
			return false;
		}
		Pattern pattern = Pattern.compile("^1\\d{10}$"); 
		return !pattern.matcher(phone).matches(); 
	}
	
	private int checkCode(HttpServletRequest request, HttpSession session, String checkName)
	{
		String passwordStr=checkName+"password";
		String phoneStr=checkName+"phone";
		String sessValue=(String)session.getAttribute(passwordStr);
		String reqValue=(String)request.getParameter(passwordStr);
		String phone=(String)session.getAttribute(phoneStr);
		if (this.checkEvil(request,phone)) {
			return -3;
		}
		if(sessValue==null || reqValue==null || sessValue.equals("") || reqValue.equals("")){
			return -1;
		}
		if(sessValue.equals(reqValue)){
			return 0;
		} else {
			return -2;
		}
	}
	@RequestMapping("/getRegSms")
	@ResponseBody
	public JsonBack getRegSms(HttpServletRequest request, HttpSession session)
	{
		String phone=(String)request.getParameter("phone");
		if(this.checkPhone(phone)){
			jsonBack.setCode("-1");
			jsonBack.setMsg("指定参数没有传入");
			jsonBack.setRes("");
			return jsonBack;
		}
		String password=this.getPassword();
		session.setAttribute("phone", phone);
		session.setAttribute("password", password);	
		//---------------------Test-------------------------------
		System.out.println(password);
		jsonBack.setCode("0");
		jsonBack.setMsg("发送短信成功！");
		jsonBack.setRes("");
		
		if (this.checkEvil(request,phone)) {
			jsonBack.setCode("-3");
			jsonBack.setMsg("你操作过于频繁，请1小时后再试！");
			jsonBack.setRes("");
			return jsonBack;
		}
		//---------------------Test-------------------------------
//		SendSms sendSms=new SendSms();
//		if ( sendSms.authUser(phone, password) ) {
//			jsonBack.setCode("0");
//			jsonBack.setMsg("发送短信成功！");
//			jsonBack.setRes("");
//		} else {
//			jsonBack.setCode("-2");
//			jsonBack.setMsg("发送短信失败！");
//			jsonBack.setRes(null);
//		}
		return jsonBack;
	}
	
	@RequestMapping("/checkOldPhone")
	@ResponseBody
	public JsonBack checkOldPhone(HttpServletRequest request, HttpSession session)
	{
		String phone=(String)request.getParameter("oldphone");
		System.out.println(phone);
		if(this.checkPhone(phone)){
			jsonBack.setCode("-1");
			jsonBack.setMsg("指定参数没有传入");
			jsonBack.setRes("");
			return jsonBack;
		}
		userMongo=userServiceMongo.findOneByPhone(phone);
		if(userMongo==null){
			jsonBack.setCode("-2");
			jsonBack.setMsg("该手机并没有注册");
			jsonBack.setRes("");
			return jsonBack;
		} else {
			jsonBack.setCode("0");
			jsonBack.setMsg("手机已注册");
			jsonBack.setRes("");
			return jsonBack;
		}
	}
	
	@RequestMapping("/getOldPhoneSms")
	@ResponseBody
	public JsonBack getOldPhoneSms(HttpServletRequest request, HttpSession session)
	{
		String oldphone=(String)request.getParameter("oldphone");
		if(this.checkPhone(oldphone)){
			jsonBack.setCode("-1");
			jsonBack.setMsg("指定参数没有传入");
			jsonBack.setRes("");
			return jsonBack;
		}
		String oldpassword=this.getPassword();
		session.setAttribute("oldphone", oldphone);
		session.setAttribute("oldpassword", oldpassword);
		//---------------------Test-------------------------------
		System.out.println(oldpassword);
		jsonBack.setCode("0");
		jsonBack.setMsg("发送短信成功！");
		jsonBack.setRes("");
		//-------------------------------------------------------
		if (this.checkEvil(request,oldphone)) {
			jsonBack.setCode("-3");
			jsonBack.setMsg("你操作过于频繁，请1小时后再试！");
			jsonBack.setRes("");
			return jsonBack;
		}
//		SendSms sendSms=new SendSms();
//		if ( sendSms.authUser(oldphone, oldpassword)) {
//			jsonBack.setCode("0");
//			jsonBack.setMsg("发送短信成功！");
//			jsonBack.setRes("");
//		} else {
//			jsonBack.setCode("-2");
//			jsonBack.setMsg("发送短信失败！");
//			jsonBack.setRes(null);
//		}
		return jsonBack;
	}
	@RequestMapping("/checkOldCode")
	@ResponseBody
	public JsonBack checkOldCode(HttpServletRequest request, HttpSession session)
	{
		int errorCode=this.checkCode(request, session, "old");
		
		if(errorCode==0){
			jsonBack.setCode("0");
			jsonBack.setMsg("手机验证码成功");
			jsonBack.setRes("");
		} else if (errorCode==-3) {
			jsonBack.setCode("-3");
			jsonBack.setMsg("你操作过于频繁，请1小时后再试！");
			jsonBack.setRes("");
		} else {
			jsonBack.setCode("-1");
			jsonBack.setMsg("手机验证码错误！");
			jsonBack.setRes("");
		}
		return jsonBack;
	}
	@RequestMapping("/getNewPhoneSms")
	@ResponseBody
	public JsonBack getNewPhoneSms(HttpServletRequest request, HttpSession session)
	{
		String newphone=(String)request.getParameter("newphone");
		if(this.checkPhone(newphone)){
			jsonBack.setCode("-1");
			jsonBack.setMsg("指定参数没有传入");
			jsonBack.setRes("");
			return jsonBack;
		}
		String newpassword=this.getPassword();
		session.setAttribute("newphone", newphone);
		session.setAttribute("newpassword", newpassword);
		//---------------------Test-------------------------------
		System.out.println(newpassword);
		jsonBack.setCode("0");
		jsonBack.setMsg("发送短信成功！");
		jsonBack.setRes("");
		//---------------------Test-------------------------------
		if (this.checkEvil(request,newphone)) {
			jsonBack.setCode("-3");
			jsonBack.setMsg("你操作过于频繁，请1小时后再试！");
			jsonBack.setRes("");
			return jsonBack;
		}
//		SendSms sendSms=new SendSms();
//		if ( sendSms.authUser(newphone, newpassword)) {
//			jsonBack.setCode("0");
//			jsonBack.setMsg("发送短信成功！");
//			jsonBack.setRes("");
//		} else {
//			jsonBack.setCode("-2");
//			jsonBack.setMsg("发送短信失败！");
//			jsonBack.setRes(null);
//		}
		return jsonBack;
	}

	
	@RequestMapping("/reginBefore")
	@ResponseBody
	public JsonBack reginBefore(HttpServletResponse response, HttpServletRequest request, HttpSession session)
	{
		int errorCode=this.checkCode(request, session, "");
		if(errorCode==0){
			String phone=(String)session.getAttribute("phone");
			userMongo=userServiceMongo.findOneByPhone(phone);
			if (userMongo==null) {
				jsonBack.setCode("0");
				jsonBack.setMsg("手机验证码成功");
				jsonBack.setRes("");
			} else {
				userMongo.setPassword((String)session.getAttribute("password"));
				MyUser.makeLoginInfo(response, session,userMongo);
				userServiceMongo.update(userMongo);
				jsonBack.setCode("1");
				jsonBack.setMsg("账号已存在，即将取出账号！");
				jsonBack.setRes(userMongo);
			}
		} else if (errorCode==-3) {
			jsonBack.setCode("-3");
			jsonBack.setMsg("你操作过于频繁，请1小时后再试！");
			jsonBack.setRes("");
		} else {
			jsonBack.setCode("-1");
			jsonBack.setMsg("手机验证码错误！");
			jsonBack.setRes("");
		}
		return jsonBack;
	}
	

	
	@RequestMapping(value="/regin", method = RequestMethod.POST)
	@ResponseBody
	public JsonBack regin(HttpServletResponse response, HttpServletRequest request, HttpSession session, @RequestBody  UserMongo userData)
	{
		//,@RequestBody  List<String> strong, @RequestBody  Localtion localtion
		String phone=(String)session.getAttribute("phone");
		String password=(String)session.getAttribute("password");
		String pwd=userData.getPassword();
		if(password==null){
			jsonBack.setCode("-4");
			jsonBack.setMsg("注册超时！请重新获取短信！");
			jsonBack.setRes(null);
			return jsonBack;
		}
		if(!password.equals(pwd)){
			jsonBack.setCode("-1");
			jsonBack.setMsg("手机验证码错误！");
			jsonBack.setRes(null);
			return jsonBack;
		}
		userData.setPhone(phone);
		userData.setModifyTime(System.currentTimeMillis()/1000);
		userData.setVipRefreshTime(System.currentTimeMillis()/1000);
		userData.setVipDeadLine(System.currentTimeMillis()/1000);
//		System.out.println(userData.isEmptyPhone());
//		System.out.println(userData.isEmptyPassword());
//		System.out.println(userData.isEmptyAvatar());
//		System.out.println(userData.isEmptySex());
//		System.out.println(userData.isEmptyName());
//		System.out.println(userData.isEmptyStrong());
//		System.out.println(userData.isEmptyRent());
		//-------------------------------------------
//		System.out.println(userData.getPhone());
//		System.out.println(userData.getPassword());
//		System.out.println(userData.getAvatar());
//		System.out.println(userData.getSex());
//		System.out.println(userData.getName());
//		System.out.println(userData.getStrong());
//		System.out.println(userData.getRent());
		if (
				userData.isEmptyPhone() || userData.isEmptyPassword() || 
				userData.isEmptyAvatar() || userData.isEmptySex() || 
				userData.isEmptyName() || userData.isEmptyStrong() ||
				userData.isEmptyRent()
			) 
		{
			jsonBack.setCode("-2");
			jsonBack.setMsg("请正确填写完全部信息！");
			jsonBack.setRes(null);
			return jsonBack;
		}
		userMongo=userServiceMongo.findOneByPhone(phone);
		//System.out.println(userMongo);
		if (userMongo==null) {
			//userMongo.setLocaltion(localtion);
			userServiceRedis.setStrongCount("userStrong", userData.getStrong());
			userData.converAvatar(request, FileType.AVATAR);
			userServiceMongo.insert(userData);
			MyUser.makeLoginInfo(response, session,userData);
			jsonBack.setCode("0");
			jsonBack.setMsg("注册成功！");
			jsonBack.setRes(userData);
		} else {
			userServiceRedis.setStrongCount("userStrong", userMongo.getStrong());
			MyUser.makeLoginInfo(response, session,userMongo);
			jsonBack.setCode("1");
			jsonBack.setMsg("账号已存在，即将取出账号！");
			jsonBack.setRes(userMongo);
		}
		return jsonBack;
	}
	
	@RequestMapping("/changePhone")
	@ResponseBody
	public JsonBack changPhone(HttpServletResponse response, HttpServletRequest request, HttpSession session)
	{
		int errorOldCode=this.checkCode(request, session, "old");
		int errorNewCode=this.checkCode(request, session, "new");
		if(errorOldCode==-3 || errorNewCode==-3){
//			System.out.println(errorOldCode);
//			System.out.println(errorNewCode);
			jsonBack.setCode("-1");
			jsonBack.setMsg("手机验证码错误！");
			jsonBack.setRes("");
			return jsonBack;
		} else if (errorOldCode!=0 || errorNewCode!=0) {
//			System.out.println(errorOldCode);
//			System.out.println(errorNewCode);
			jsonBack.setCode("-3");
			jsonBack.setMsg("你操作过于频繁，请1小时后再试！");
			jsonBack.setRes("");
			return jsonBack;
		} 
		String oldphone=(String)session.getAttribute("oldphone");
		String newphone=(String)session.getAttribute("newphone");
		String newpassword=(String)session.getAttribute("newpassword");
		UserMongo olduser=userServiceMongo.findOneByPhone(oldphone);
		if(olduser==null){
			jsonBack.setCode("-2");
			jsonBack.setMsg("旧手机账号不存在");
			jsonBack.setRes(null);
			return jsonBack;
		}
		UserMongo newuser=userServiceMongo.findOneByPhone(newphone);
		if (newuser!=null) {
			userServiceMongo.remove(userMongo);
		}
		olduser.setPhone(newphone);
		olduser.setPassword(newpassword);
		userServiceMongo.update(olduser);
		MyUser.makeLoginInfo(response, session,olduser);
		jsonBack.setCode("0");
		jsonBack.setMsg("取回更新数据");
		jsonBack.setRes(olduser);
		return jsonBack;
	}
	
	@RequestMapping("/logout")
	@ResponseBody
	public JsonBack logout(HttpServletResponse response, HttpServletRequest request, HttpSession session)
	{
		MyUser.makeLoginOut(response, session);
		jsonBack.setCode("0");
		jsonBack.setMsg("下线成功");
		jsonBack.setRes(null);
		return jsonBack;
	}
	
	@RequestMapping("/isLogin")
	@ResponseBody
	public JsonBack isLogin(HttpServletResponse response, HttpServletRequest request, HttpSession session)
	{
		//System.out.println("islogin");
		if (MyUser.islogin(response, request)) {
			//System.out.println("登录");
			jsonBack.setCode("0");
			jsonBack.setMsg("已登录");
			jsonBack.setRes(null);
		} else {
			//System.out.println("未登录");
			jsonBack.setCode("-1");
			jsonBack.setMsg("未登录");
			jsonBack.setRes(null);
		}
		return jsonBack;
	}
	
	@RequestMapping("/searchFriendByPhone")
	@ResponseBody
	public JsonBack searchFriendByPhone(HttpServletResponse response, HttpServletRequest request, HttpSession session)
	{
		String phone=(String)request.getParameter("phone");
		UserMongo myFriend=userServiceMongo.findOneByPhone(phone);
		if (myFriend!=null) {
			List<UserMongo> userList=new ArrayList<UserMongo>();
			userList.add(myFriend);
			jsonBack.setCode("0");
			jsonBack.setMsg("好友已找到");
			jsonBack.setRes(userList);
		} else {
			jsonBack.setCode("-1");
			jsonBack.setMsg("好友不存在，是否帮助好友填写信息");
			jsonBack.setRes(null);
		}
		return jsonBack;
	}
	@RequestMapping("/searchFriendByName")
	@ResponseBody
	public JsonBack searchFriendByName(HttpServletResponse response, HttpServletRequest request, HttpSession session)
	{
		String name=(String)request.getParameter("name");
		List<UserMongo> userList=userServiceMongo.findListByName(name, 0, 10);
		if (userList.size()!=0) {
			jsonBack.setCode("0");
			jsonBack.setMsg("好友已找到，但只显示最先使用该昵称的前10名");
			jsonBack.setRes(userList);
		} else {
			jsonBack.setCode("-1");
			jsonBack.setMsg("该昵称好友不存在");
			jsonBack.setRes(null);
		}
		return jsonBack;
	}
	@RequestMapping("/getUserById")
	@ResponseBody
	public JsonBack getUserById(HttpServletResponse response, HttpServletRequest request, HttpSession session)
	{
		String id=(String)request.getParameter("id");
		UserMongo user=userServiceMongo.findOneById(id);
		if (user!=null) {
			user.setPhone(null);
			user.setPassword(String.valueOf(System.currentTimeMillis()/1000));
			user.setLocaltion(null);
			jsonBack.setCode("0");
			jsonBack.setMsg("好友已找到");
			jsonBack.setRes(user);
		} else {
			jsonBack.setCode("-1");
			jsonBack.setMsg("用户不存在");
			jsonBack.setRes(null);
		}
		return jsonBack;
	}
	@RequestMapping(value="/upUserInfo", method = RequestMethod.POST)
	@ResponseBody
	public JsonBack upUserInfo(HttpServletResponse response, HttpServletRequest request, HttpSession session, @RequestBody  UserMongo userData)
	{
		userMongo=userServiceMongo.findOneById(userData.getId());
		if (userMongo!=null) {
			String id=(String)session.getAttribute("id");
			//System.out.println(userData.getName());
			if (userData.getId().equals(id)) {
				userMongo.setAvatar(userData.getAvatar());
				userMongo.setSign(userData.getSign());
				userMongo.setSex(userData.getSex());
				userMongo.setName(userData.getName());
				userMongo.setStrong(userData.getStrong());
				userMongo.setRent(userData.getRent());
				userMongo.converAvatar(request, FileType.AVATAR);
				userServiceMongo.update(userMongo);
			} else {
				if (userMongo.getModifyTime()+14*24*3600-(System.currentTimeMillis()/1000)<0) {
					userMongo.setAvatar(userData.getAvatar());
					userMongo.setSign(userData.getSign());
					userMongo.setSex(userData.getSex());
					userMongo.setName(userData.getName());
					userMongo.setStrong(userData.getStrong());
					userMongo.setModifyTime(System.currentTimeMillis()/1000);
					userMongo.setVipRefreshTime(System.currentTimeMillis()/1000);
					userMongo.setVipDeadLine(System.currentTimeMillis()/1000);
					userMongo.converAvatar(request, FileType.AVATAR);
					userServiceMongo.update(userMongo);
				} else {
					jsonBack.setCode("-2");
					jsonBack.setMsg("修改时间未到，不能修改");
					jsonBack.setRes(null);
					return jsonBack;
				}
			}
			jsonBack.setCode("0");
			jsonBack.setMsg("修改用户成功");
			jsonBack.setRes(null);
		} else {
			jsonBack.setCode("-1");
			jsonBack.setMsg("用户不存在");
			jsonBack.setRes(null);
		}
		return jsonBack;
	}
	@RequestMapping(value="/addFriendInfo", method = RequestMethod.POST)
	@ResponseBody
	public JsonBack addFriendInfo(HttpServletResponse response, HttpServletRequest request, HttpSession session, @RequestBody  UserMongo userData)
	{
		userMongo=userServiceMongo.findOneByPhone(userData.getPhone());
		if (userMongo==null) {
			if (this.checkPhone(userData.getPhone())) {
				jsonBack.setCode("-2");
				jsonBack.setMsg("信息错误");
				jsonBack.setRes(null);
				return jsonBack;
			}
			userData.setPassword(this.getPassword());
			userData.setRent(0);
			userData.setModifyTime(System.currentTimeMillis()/1000);
			userData.setVipRefreshTime(System.currentTimeMillis()/1000);
			userData.setVipDeadLine(System.currentTimeMillis()/1000);
			userData.converAvatar(request, FileType.AVATAR);
			userServiceMongo.insert(userData);
			jsonBack.setCode("0");
			jsonBack.setMsg("帮助好友填写资料成功");
			jsonBack.setRes(null);
		} else {
			jsonBack.setCode("-1");
			jsonBack.setMsg("就在刚刚之前，这个账号被别人注册了或被别人填写了资料");
			jsonBack.setRes(null);
		}
		return jsonBack;
	}
}
