package com.slyim.dao;

import java.util.List;

import com.slyim.pojo.CalendarMongo;
import com.slyim.pojo.UserBase;


public interface ICalendarDaoMongo {
	void createCollection(String colname);
	
	 List<CalendarMongo> findList(int skip, int limit);   
	 
	 List<CalendarMongo> findListByStartTime(UserBase acceptor, long startTime, int limit);  
 
	 CalendarMongo findOne(String id);  
 
    void insert(CalendarMongo calendar);  
 
    void update(CalendarMongo calendar);
    
    void remove(CalendarMongo calendar);
}
