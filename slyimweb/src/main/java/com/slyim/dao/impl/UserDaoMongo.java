package com.slyim.dao.impl;


import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Sort;  
import org.springframework.data.domain.Sort.Direction;  
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;  
import org.springframework.data.mongodb.core.query.Query;  
import org.springframework.data.mongodb.core.query.Update;
import com.slyim.dao.IUserDaoMongo;
import com.slyim.pojo.UserMongo;
import com.slyim.dao.ABaseMongoDao;;

@Repository
public class UserDaoMongo extends ABaseMongoDao implements IUserDaoMongo {

	public void createCollection(String colname) {
		if (!this.mongoTemplate.collectionExists(colname)) {  
            this.mongoTemplate.createCollection(colname);  
        }	
	}
	
	public List<UserMongo> findList(int skip, int limit) {
		Query query = new Query();  
        query.with(new Sort(new Order(Direction.ASC, "_id")));  
        query.skip(skip).limit(limit);  
        return this.mongoTemplate.find(query, UserMongo.class);
	}


	public UserMongo findOneByPhone(String phone) {
		Query query = new Query();  
        query.addCriteria(new Criteria("phone").is(phone));  
        return this.mongoTemplate.findOne(query, UserMongo.class); 
	}

	public void insert(UserMongo user) {
		this.mongoTemplate.insert(user); 
	}

	@Override
	public void remove(UserMongo user) {
		this.mongoTemplate.remove(user); 
	}
	public void update(UserMongo user) {
		Query query = new Query();  
        query.addCriteria(new Criteria("_id").is(user.getId()));  
        Update update = new Update();
        Map<String,Object> map =UserDaoMongo.reflectVar(user);
        for(String key : map.keySet()){
        	update.set(key, map.get(key));
        }  
        this.mongoTemplate.updateFirst(query, update, UserMongo.class);
	}

	@Override
	public void update(UserMongo user, String phone) {
		Query query = new Query();  
        query.addCriteria(new Criteria("phone").is(phone));  
        Update update = new Update();
        Map<String,Object> map =UserDaoMongo.reflectVar(user);
        for(String key : map.keySet()){
        	update.set(key, map.get(key));
        }
        this.mongoTemplate.updateFirst(query, update, UserMongo.class);
		
	}

	@Override
	public UserMongo findOneById(String id) {
		Query query = new Query();  
        query.addCriteria(new Criteria("_id").is(id));  
        return this.mongoTemplate.findOne(query, UserMongo.class); 
	}

	@Override
	public List<UserMongo> findListByName(String name, int skip, int limit) {
		Query query = new Query();  
		query.addCriteria(new Criteria("name").is(name));
        query.with(new Sort(new Order(Direction.ASC, "_id")));  
        query.skip(skip).limit(limit);  
        return this.mongoTemplate.find(query, UserMongo.class);
	}

	

}
