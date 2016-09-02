package com.slyim.dao.impl;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.slyim.dao.ABaseMongoDao;
import com.slyim.dao.IDynamicCircleDaoMongo;
import com.slyim.pojo.DynamicCircleMongo;
import com.slyim.pojo.UserBase;

@Repository
public class DynamicCircleDaoMongo extends ABaseMongoDao implements IDynamicCircleDaoMongo  {
	public void createCollection(String colname) {
		if (!this.mongoTemplate.collectionExists(colname)) {  
            this.mongoTemplate.createCollection(colname);  
        }	
	}

	public List<DynamicCircleMongo> findList(int skip, int limit) {
		Query query = new Query();  
        query.with(new Sort(new Order(Direction.DESC, "sendTime")));  
        query.skip(skip).limit(limit);  
        return this.mongoTemplate.find(query, DynamicCircleMongo.class);
	}
	public List<DynamicCircleMongo> findListBySendTime(long sendTime,int skip, int limit) {
		Query query = new Query();  
        Criteria criteriaSendTime = new Criteria("sendTime").gt(sendTime);
        query.addCriteria(criteriaSendTime);
        query.with(new Sort(new Order(Direction.DESC, "sendTime")));
        query.skip(skip).limit(limit);  
        return this.mongoTemplate.find(query, DynamicCircleMongo.class);
	}
	public List<DynamicCircleMongo> findListByStartTime(UserBase acceptor, long startTime, int limit){
		Query query = new Query();
		Criteria criteriaStartTime = new Criteria("startTime").gt(startTime);
		Criteria criteriaAcceptor = new Criteria("acceptor").is(acceptor);
		query.addCriteria(criteriaAcceptor);
		query.addCriteria(criteriaStartTime);
        query.limit(limit);
        return this.mongoTemplate.find(query, DynamicCircleMongo.class);
	}

	public DynamicCircleMongo findOne(String id) {
		Query query = new Query();  
        query.addCriteria(new Criteria("_id").is(id));  
        return this.mongoTemplate.findOne(query, DynamicCircleMongo.class); 
	}


	public void insert(DynamicCircleMongo dynamicCircle) {
		this.mongoTemplate.insert(dynamicCircle); 
		
	}

	public void update(DynamicCircleMongo dynamicCircle) {
		Query query = new Query();  
        query.addCriteria(new Criteria("_id").is(dynamicCircle.getId()));  
        Update update = new Update();  
        this.mongoTemplate.updateFirst(query, update, dynamicCircle.getClass());
	}
	
	public void remove(DynamicCircleMongo dynamicCircle) {
		this.mongoTemplate.remove(dynamicCircle);
		
	}

}
