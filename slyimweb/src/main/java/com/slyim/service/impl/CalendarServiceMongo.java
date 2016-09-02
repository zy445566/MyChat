package com.slyim.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.slyim.dao.impl.CalendarDaoMongo;
import com.slyim.pojo.CalendarMongo;
import com.slyim.pojo.UserBase;
import com.slyim.service.ICalendarServiceMongo;

@Service("calendarServiceMongo")
public class CalendarServiceMongo  implements ICalendarServiceMongo {

	@Autowired
	private CalendarDaoMongo calendarDaoMongo;
	@Override
	public void createCollection(String colname) {
		this.calendarDaoMongo.createCollection(colname);
		
	}

	@Override
	public List<CalendarMongo> findList(int skip, int limit) {
		// TODO Auto-generated method stub
		return this.calendarDaoMongo.findList(skip, limit);
	}

	@Override
	public List<CalendarMongo> findListByStartTime(UserBase acceptor, long startTime, int limit) {
		// TODO Auto-generated method stub
		return this.calendarDaoMongo.findListByStartTime(acceptor, startTime, limit);
	}
	
	@Override
	public CalendarMongo findOne(String id) {
		// TODO Auto-generated method stub
		return this.calendarDaoMongo.findOne(id);
	}

	@Override
	public void insert(CalendarMongo calendar) {
		this.calendarDaoMongo.insert(calendar);
		
	}

	@Override
	public void update(CalendarMongo calendar) {
		// TODO Auto-generated method stub
		this.calendarDaoMongo.update(calendar);
	}

	@Override
	public void remove(CalendarMongo calendar) {
		// TODO Auto-generated method stub
		this.calendarDaoMongo.remove(calendar);
	}
}
