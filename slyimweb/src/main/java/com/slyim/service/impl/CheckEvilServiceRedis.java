package com.slyim.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.slyim.dao.impl.CheckEvilDaoRedis;
import com.slyim.service.ICheckEvilServiceRedis;

@Service("checkEvilServiceRedis")
public class CheckEvilServiceRedis implements ICheckEvilServiceRedis {

	@Autowired
	private CheckEvilDaoRedis checkEvilDaoRedis;
	@Override
	public int get(String checkKey) {
		return checkEvilDaoRedis.get(checkKey);
	}

	@Override
	public boolean set(String checkKey, int checkvalue, long checkexpire) {
		return checkEvilDaoRedis.set(checkKey, checkvalue, checkexpire);
	}

	@Override
	public boolean isExists(String checkKey) {
		// TODO Auto-generated method stub
		return checkEvilDaoRedis.isExists(checkKey);
	}

	@Override
	public int incr(String checkKey, long checkexpire) {
		// TODO Auto-generated method stub
		return checkEvilDaoRedis.incr(checkKey, checkexpire);
	}

}
