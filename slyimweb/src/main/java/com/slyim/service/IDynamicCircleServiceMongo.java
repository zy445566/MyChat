package com.slyim.service;

import java.util.List;

import com.slyim.pojo.DynamicCircleMongo;

public interface IDynamicCircleServiceMongo {
void createCollection(String colname);
	
	List<DynamicCircleMongo> findList(int skip, int limit);   
	List<DynamicCircleMongo> findListBySendTime(long sendTime,int skip, int limit);
	
	DynamicCircleMongo findOne(String id);  
 
    void insert(DynamicCircleMongo chat);  
 
    void update(DynamicCircleMongo chat); 
    
    void remove(DynamicCircleMongo chat);
}
