package com.slyim.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.slyim.pojo.ChatMongo;
import com.slyim.pojo.JsonBack;
import com.slyim.pojo.UserBase;
import com.slyim.service.impl.ChatServiceMongo;

@Controller
@RequestMapping("/chat") 
public class ChatController {
	
	private JsonBack jsonBack = new JsonBack();
	private ChatMongo chatMongo= new ChatMongo();
	
	@Autowired
	private ChatServiceMongo chatServiceMongo;
	
	
	@RequestMapping("/sendChat")
	@ResponseBody
	public JsonBack sendChat(HttpServletRequest request, HttpSession session){
		String type=(String)request.getParameter("type");
		String content=(String)request.getParameter("content");
		String myId=(String)session.getAttribute("id");
		String friendId=(String)request.getParameter("friendId");
		chatMongo.setType(type);
		chatMongo.setContent(content);
		chatMongo.setMyId(myId);
		chatMongo.setFriendId(friendId);
		Date date = new Date();
		long sendTime=date.getTime();
		chatMongo.setSendTime(sendTime);
		chatServiceMongo.insert(chatMongo);
		jsonBack.setCode("0");
		jsonBack.setMsg("发送短信成功！");
		jsonBack.setRes(null);
		return jsonBack;
	}
	
	@RequestMapping("/getChat")
	@ResponseBody
	public JsonBack getChat(HttpServletRequest request, HttpSession session){
		long startTime=Long.parseLong(request.getParameter("startTime"));
		String acceptorId=(String)session.getAttribute("id");
		String acceptorName=(String)session.getAttribute("name");
		UserBase acceptor=new UserBase();
		acceptor.setId(acceptorId);
		acceptor.setName(acceptorName);
		List<ChatMongo> chatList= chatServiceMongo.findListByStartTime(acceptor, startTime, 100);
		jsonBack.setCode("0");
		jsonBack.setMsg("获取聊天记录成功");
		jsonBack.setRes(chatList);
		return jsonBack;
	}
	
	
}
