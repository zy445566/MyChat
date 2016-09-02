package com.slyim.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import com.slyim.pojo.DynamicCircleMongo;
import com.slyim.pojo.JsonBack;
import com.slyim.pojo.UserBase;
import com.slyim.service.impl.DynamicCircleServiceMongo;

@Controller
@RequestMapping("/dynamicCircle") 
public class DynamicCircleContoller {
	
	private JsonBack jsonBack = new JsonBack();
//	private DynamicCircleMongo dynamicCircleMongo = new DynamicCircleMongo();
	
	@Autowired
	private DynamicCircleServiceMongo dynamicCircleServiceMongo;
	
	
	@RequestMapping("/getCircle")
	@ResponseBody
	public JsonBack getCircle(HttpServletRequest request, HttpSession session){
		String sendTimeStr=(String)request.getParameter("sendTime");
		long sendTime = Long.parseLong(sendTimeStr);
		if ((System.currentTimeMillis()/1000)-sendTime>3600*24*30*3) {
			sendTime=(System.currentTimeMillis()/1000)-3600*24*30*3;
		}
		List<DynamicCircleMongo> dCircleList= dynamicCircleServiceMongo.findListBySendTime(sendTime, 0, 50);
		if (dCircleList==null) {
			jsonBack.setCode("-1");
			jsonBack.setMsg("没有任何信息更新");
			jsonBack.setRes(null);
		} else {
			jsonBack.setCode("0");
			jsonBack.setMsg("获取成功");
			jsonBack.setRes(dCircleList);
		}
		return jsonBack;
	}
	@RequestMapping(value="/sendCircle", method = RequestMethod.POST)
	@ResponseBody
	public JsonBack sendCircle(HttpServletRequest request, HttpSession session, @RequestBody  DynamicCircleMongo dCircleMongo){
		String myId=(String)session.getAttribute("id");
		if (myId==null || myId.equals("")) {
			jsonBack.setCode("-1");
			jsonBack.setMsg("请先登录");
			jsonBack.setRes(null);
			return jsonBack;
		}
		UserBase userBase=dCircleMongo.getSender();
//		System.out.println(myId);
//		System.out.println(userBase.getId());
		if (userBase.getId()==null || !userBase.getId().equals(myId)) {
			jsonBack.setCode("-2");
			jsonBack.setMsg("账号信息错误，请重新登录。");
			jsonBack.setRes(null);
			return jsonBack;
		}
		dCircleMongo.setSendTime(System.currentTimeMillis()/1000);
		dynamicCircleServiceMongo.insert(dCircleMongo);
		jsonBack.setCode("0");
		jsonBack.setMsg("发送附近成功");
		jsonBack.setRes(dCircleMongo);
		return jsonBack;
	}
}
