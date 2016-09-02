package com.slyim.dao;

import java.util.List;

import com.slyim.pojo.DynamicCircleMongo;

public interface IDynamicCircleDaoMongo {
	void createCollection(String colname);
	
	 List<DynamicCircleMongo> findList(int skip, int limit);
	 
	 List<DynamicCircleMongo> findListBySendTime(long sendTime,int skip, int limit);
	 
	 DynamicCircleMongo findOne(String id);  

	 void insert(DynamicCircleMongo dynamicCircle);  

	 void update(DynamicCircleMongo dynamicCircle);
	 
	 void remove(DynamicCircleMongo dynamicCircle);
}
