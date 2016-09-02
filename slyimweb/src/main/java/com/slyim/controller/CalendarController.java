package com.slyim.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.slyim.pojo.CalendarMongo;
import com.slyim.pojo.JsonBack;
import com.slyim.pojo.UserBase;
import com.slyim.service.impl.CalendarServiceMongo;


@Controller
@RequestMapping("/calender") 
public class CalendarController {
	
	private JsonBack jsonBack = new JsonBack();
	private CalendarMongo calendarMongo=new CalendarMongo();
	
	@Autowired
	private CalendarServiceMongo calendarServiceMongo;
	
	@RequestMapping("/getCalendar")
	@ResponseBody
	public JsonBack getCalendar(HttpServletRequest request, HttpSession session){
		long startTime=Long.parseLong(request.getParameter("startTime"));
		String acceptorId=(String)session.getAttribute("id");
		String acceptorName=(String)session.getAttribute("name");
		UserBase acceptor=new UserBase();
		acceptor.setId(acceptorId);
		acceptor.setName(acceptorName);
		List<CalendarMongo> calendarList=calendarServiceMongo.findListByStartTime(acceptor, startTime, 100);
		jsonBack.setCode("0");
		jsonBack.setMsg("获取日历活动成功！");
		jsonBack.setRes(calendarList);
		return jsonBack;
	}
	
	@RequestMapping("/sendCalendar")
	@ResponseBody
	public JsonBack sendCalendar(HttpServletRequest request, HttpSession session){
		long startTime=Long.parseLong(request.getParameter("startTime"));
		long endTime=Long.parseLong(request.getParameter("endTime"));
		String event=(String)request.getParameter("event");
		String address=(String)request.getParameter("address");
		String acceptorId=(String)request.getParameter("acceptorId");
		String acceptorName=(String)request.getParameter("acceptorName");
		String sponsorId=(String)session.getAttribute("id");
		String sponsorName=(String)session.getAttribute("name");
		UserBase sponsor=new UserBase();
		UserBase acceptor=new UserBase();
		acceptor.setId(acceptorId);
		acceptor.setName(acceptorName);
		sponsor.setId(sponsorId);
		sponsor.setName(sponsorName);
		calendarMongo.setStartTime(startTime);
		calendarMongo.setEndTime(endTime);
		calendarMongo.setEvent(event);
		calendarMongo.setAddress(address);
		calendarMongo.setAcceptor(acceptor);
		calendarMongo.setSponsor(sponsor);
		jsonBack.setCode("0");
		jsonBack.setMsg("发起日历成功");
		jsonBack.setRes(null);
		return jsonBack;
	}
	
	
	
}
