package com.slyim.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.slyim.dao.impl.ChatDaoMongo;
import com.slyim.pojo.ChatMongo;
import com.slyim.pojo.UserBase;
import com.slyim.service.IChatServiceMongo;

@Service("chatServiceMongo")
public class ChatServiceMongo implements IChatServiceMongo {

	@Autowired
	private ChatDaoMongo chatDaoMongo;
	@Override
	public void createCollection(String colname) {
		// TODO Auto-generated method stub
		this.chatDaoMongo.createCollection(colname);
	}

	@Override
	public List<ChatMongo> findList(int skip, int limit) {
		// TODO Auto-generated method stub
		return this.chatDaoMongo.findList(skip, limit);
	}
	
	@Override
	public List<ChatMongo> findListByStartTime(UserBase acceptor, long startTime, int limit) {
		// TODO Auto-generated method stub
		return this.chatDaoMongo.findListByStartTime(acceptor, startTime, limit);
	}
	@Override
	public ChatMongo findOne(String id) {
		// TODO Auto-generated method stub
		return this.chatDaoMongo.findOne(id);
	}

	@Override
	public void insert(ChatMongo chat) {
		this.chatDaoMongo.insert(chat);
		
	}

	@Override
	public void update(ChatMongo chat) {
		// TODO Auto-generated method stub
		this.chatDaoMongo.update(chat);
	}

	@Override
	public void remove(ChatMongo chat) {
		// TODO Auto-generated method stub
		this.chatDaoMongo.remove(chat);
	}
}
