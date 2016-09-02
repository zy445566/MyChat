package com.slyim.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.slyim.dao.impl.UserDaoMongo;
import com.slyim.pojo.UserMongo;
import com.slyim.service.IUserServiceMongo;

@Service("userServiceMongo")
public class UserServiceMongo implements IUserServiceMongo {

	@Autowired
	private UserDaoMongo userDaoMongo;
	
	public void createCollection(String colname) {
		this.userDaoMongo.createCollection(colname);

	}

	public List<UserMongo> findList(int skip, int limit) {
		// TODO Auto-generated method stub
		return this.userDaoMongo.findList(skip, limit);
	}

	public UserMongo findOneByPhone(String phone) {
		// TODO Auto-generated method stub
		return this.userDaoMongo.findOneByPhone(phone);
	}

	public void insert(UserMongo user) {
		// TODO Auto-generated method stub
		this.userDaoMongo.insert(user);
	}

	public void update(UserMongo user) {
		// TODO Auto-generated method stub
		this.userDaoMongo.update(user);
	}

	@Override
	public void remove(UserMongo user) {
		// TODO Auto-generated method stub
		this.userDaoMongo.remove(user);
	}

	@Override
	public void update(UserMongo user, String phone) {
		// TODO Auto-generated method stub
		this.userDaoMongo.update(user, phone);
	}

	@Override
	public UserMongo findOneById(String id) {
		// TODO Auto-generated method stub
		return this.userDaoMongo.findOneById(id);
	}

	@Override
	public List<UserMongo> findListByName(String name, int skip, int limit) {
		// TODO Auto-generated method stub
		return this.userDaoMongo.findListByName(name, skip, limit);
	}

}
