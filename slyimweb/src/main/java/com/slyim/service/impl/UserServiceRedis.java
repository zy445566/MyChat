package com.slyim.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.slyim.pojo.UserRedis;
import com.slyim.service.IUserServiceRedis;
import com.slyim.dao.impl.UserDaoRedis;

@Service("userServiceRedis")
public class UserServiceRedis implements IUserServiceRedis{
	
	@Autowired
	private UserDaoRedis userDaoRedis;
	public boolean add(UserRedis user) {
		return this.userDaoRedis.add(user);
	}

	public boolean add(List<UserRedis> list) {
		// TODO Auto-generated method stub
		return this.userDaoRedis.add(list);
	}

	public void delete(String key) {
		// TODO Auto-generated method stub
		this.userDaoRedis.delete(key);	
	}

	public void delete(List<String> keys) {
		// TODO Auto-generated method stub
		this.userDaoRedis.delete(keys);
	}

	public boolean update(UserRedis user) {
		// TODO Auto-generated method stub
		return this.userDaoRedis.update(user);
	}

	public UserRedis get(String keyId) {
		// TODO Auto-generated method stub
		return this.userDaoRedis.get(keyId);
	}

	@Override
	public boolean setStrongCount(String strongKey, List<String> strongValueList) {
		// TODO Auto-generated method stub
		return this.userDaoRedis.setStrongCount(strongKey, strongValueList);
	}

	@Override
	public List<String> getStrongCount(String strongKey, long start, long stop) {
		// TODO Auto-generated method stub
		return this.userDaoRedis.getStrongCount(strongKey, start, stop);
	}

}
