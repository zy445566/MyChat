package com.slyim.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.slyim.dao.impl.DynamicCircleDaoMongo;
import com.slyim.pojo.DynamicCircleMongo;
import com.slyim.service.IDynamicCircleServiceMongo;


@Service("dynamicCircleServiceMongo")
public class DynamicCircleServiceMongo implements IDynamicCircleServiceMongo {

	@Autowired
	private DynamicCircleDaoMongo dynamicCircleDaoMongo;
	

	public void createCollection(String colname) {
		// TODO Auto-generated method stub
		this.dynamicCircleDaoMongo.createCollection(colname);
	}


	public List<DynamicCircleMongo> findList(int skip, int limit) {
		// TODO Auto-generated method stub
		return this.dynamicCircleDaoMongo.findList(skip, limit);
	}
	
	@Override
	public List<DynamicCircleMongo> findListBySendTime(long sendTime, int skip, int limit) {
		// TODO Auto-generated method stub
		return this.dynamicCircleDaoMongo.findListBySendTime(sendTime, skip, limit);
	}

	public DynamicCircleMongo findOne(String id) {
		// TODO Auto-generated method stub
		return this.dynamicCircleDaoMongo.findOne(id);
	}


	public void insert(DynamicCircleMongo dynamicCircleMongo) {
		this.dynamicCircleDaoMongo.insert(dynamicCircleMongo);
		
	}


	public void update(DynamicCircleMongo dynamicCircleMongo) {
		// TODO Auto-generated method stub
		this.dynamicCircleDaoMongo.update(dynamicCircleMongo);
	}


	public void remove(DynamicCircleMongo dynamicCircleMongo) {
		// TODO Auto-generated method stub
		this.dynamicCircleDaoMongo.remove(dynamicCircleMongo);
	}


	
}
