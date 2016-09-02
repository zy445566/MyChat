package com.slyim.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.slyim.dao.ABaseMongoDao;
import com.slyim.dao.IAddressDaoMongo;
import com.slyim.pojo.AddressMongo;
import com.slyim.pojo.UserMongo;


@Repository
public class AddressDaoMongo extends ABaseMongoDao implements IAddressDaoMongo {

	@Override
	public void createCollection(String colname) {
		if (!this.mongoTemplate.collectionExists(colname)) {  
            this.mongoTemplate.createCollection(colname);  
        }	

	}

	@Override
	public List<AddressMongo> findList(String myId) {
		Query query = new Query();  
		query.addCriteria(new Criteria("myId").is(myId));
        return this.mongoTemplate.find(query, AddressMongo.class);
	}
	
	@Override
	public AddressMongo findOneByMyIdAndFriendId(String myId, String friendId) {
		Query query = new Query();
		query.addCriteria(new Criteria("myId").is(myId));
		query.addCriteria(new Criteria("friendId").is(friendId));
        return this.mongoTemplate.findOne(query, AddressMongo.class);
	}
	@Override
	public boolean addFriend(AddressMongo addressMongo) {
		Query query = new Query();  
		query.addCriteria(new Criteria("myId").is(addressMongo.getMyId()));
		query.addCriteria(new Criteria("friendId").is(addressMongo.getFriendId()));
		AddressMongo isExistFriend=this.mongoTemplate.findOne(query, AddressMongo.class);
		if (isExistFriend==null) {
			this.mongoTemplate.insert(addressMongo);
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public boolean delFriend(AddressMongo addressMongo) {
		Query query = new Query();  
		query.addCriteria(new Criteria("_id").is(addressMongo.getId()));
		this.mongoTemplate.remove(query, AddressMongo.class);
		return true;

	}

	@Override
	public boolean blackFriend(AddressMongo addressMongo) {
		Query query = new Query();  
		query.addCriteria(new Criteria("_id").is(addressMongo.getId()));
        Update update = new Update();
        update.set("state", 2);
        this.mongoTemplate.updateFirst(query, update, AddressMongo.class);
		return true;
	}

	@Override
	public void insert(AddressMongo addressMongo) {
		// TODO Auto-generated method stub
		this.mongoTemplate.insert(addressMongo);
	}

	@Override
	public void updataById(AddressMongo addressMongo) {
		Query query = new Query();  
        query.addCriteria(new Criteria("_id").is(addressMongo.getId()));  
        Update update = new Update();
        Map<String,Object> map =AddressDaoMongo.reflectVar(addressMongo);
        for(String key : map.keySet()){
        	update.set(key, map.get(key));
        }
        this.mongoTemplate.updateFirst(query, update, AddressMongo.class);
		
	}

}
