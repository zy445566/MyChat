package com.slyim.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Sort;  
import org.springframework.data.domain.Sort.Direction;  
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;  
import org.springframework.data.mongodb.core.query.Query;  
import org.springframework.data.mongodb.core.query.Update;
import com.slyim.pojo.CalendarMongo;
import com.slyim.pojo.UserBase;
import com.slyim.dao.ABaseMongoDao;
import com.slyim.dao.ICalendarDaoMongo;;

@Repository
public class CalendarDaoMongo extends ABaseMongoDao implements ICalendarDaoMongo {

	public void createCollection(String colname) {
		if (!this.mongoTemplate.collectionExists(colname)) {  
            this.mongoTemplate.createCollection(colname);  
        }	
	}
	@Override
	public List<CalendarMongo> findList(int skip, int limit) {
		Query query = new Query();  
        query.with(new Sort(new Order(Direction.ASC, "_id")));  
        query.skip(skip).limit(limit);  
        return this.mongoTemplate.find(query, CalendarMongo.class);
	}
	
	@Override
	public List<CalendarMongo> findListByStartTime(UserBase acceptor, long startTime, int limit) {
		Query query = new Query();
		Criteria criteriaStartTime = new Criteria("startTime").gt(startTime);
		Criteria criteriaAcceptor = new Criteria("acceptor").is(acceptor);
		query.addCriteria(criteriaAcceptor);
		query.addCriteria(criteriaStartTime);
        query.limit(limit);
        return this.mongoTemplate.find(query, CalendarMongo.class);
	}
	
	@Override
	public CalendarMongo findOne(String id) {
		Query query = new Query();  
        query.addCriteria(new Criteria("_id").is(id));  
        return this.mongoTemplate.findOne(query, CalendarMongo.class); 
	}

	@Override
	public void insert(CalendarMongo calendar) {
		this.mongoTemplate.insert(calendar); 
		
	}

	@Override
	public void update(CalendarMongo calendar) {
		Query query = new Query();  
        query.addCriteria(new Criteria("_id").is(calendar.getId()));  
        Update update = new Update();  
        update.set("state", calendar.getState());  
        this.mongoTemplate.updateFirst(query, update, calendar.getClass());
	}
	@Override
	public void remove(CalendarMongo calendar) {
		this.mongoTemplate.remove(calendar);
		
	}

}

