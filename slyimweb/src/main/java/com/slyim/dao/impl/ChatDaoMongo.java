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
import com.slyim.dao.IChatDaoMongo;
import com.slyim.pojo.ChatMongo;
import com.slyim.pojo.UserBase;


@Repository
public class ChatDaoMongo extends ABaseMongoDao implements IChatDaoMongo {

	public void createCollection(String colname) {
		if (!this.mongoTemplate.collectionExists(colname)) {  
            this.mongoTemplate.createCollection(colname);  
        }	
	}
	@Override
	public List<ChatMongo> findList(int skip, int limit) {
		Query query = new Query();  
        query.with(new Sort(new Order(Direction.ASC, "_id")));  
        query.skip(skip).limit(limit);  
        return this.mongoTemplate.find(query, ChatMongo.class);
	}
	public List<ChatMongo> findListByStartTime(UserBase acceptor, long startTime, int limit){
		Query query = new Query();
		Criteria criteriaStartTime = new Criteria("startTime").gt(startTime);
		Criteria criteriaAcceptor = new Criteria("acceptor").is(acceptor);
		query.addCriteria(criteriaAcceptor);
		query.addCriteria(criteriaStartTime);
        query.limit(limit);
        return this.mongoTemplate.find(query, ChatMongo.class);
	}
	@Override
	public ChatMongo findOne(String id) {
		Query query = new Query();  
        query.addCriteria(new Criteria("_id").is(id));  
        return this.mongoTemplate.findOne(query, ChatMongo.class); 
	}

	@Override
	public void insert(ChatMongo chat) {
		this.mongoTemplate.insert(chat); 
		
	}

	@Override
	public void update(ChatMongo chat) {
		Query query = new Query();  
        query.addCriteria(new Criteria("_id").is(chat.getId()));  
        Update update = new Update();  
        this.mongoTemplate.updateFirst(query, update, chat.getClass());
	}
	@Override
	public void remove(ChatMongo chat) {
		this.mongoTemplate.remove(chat);
		
	}
}
