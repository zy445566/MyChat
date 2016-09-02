package com.slyim.dao;

import java.util.List;

import com.slyim.pojo.ChatMongo;
import com.slyim.pojo.UserBase;;

public interface IChatDaoMongo {
	void createCollection(String colname);
	
	 List<ChatMongo> findList(int skip, int limit);
	 
	 List<ChatMongo> findListByStartTime(UserBase acceptor, long startTime, int limit);  

	 ChatMongo findOne(String id);  

	 void insert(ChatMongo chat);  

	 void update(ChatMongo chat);
	 
	 void remove(ChatMongo chat);
}
