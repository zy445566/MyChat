package com.slyim.service;

import java.util.List;

import com.slyim.pojo.UserMongo;

public interface IUserServiceMongo {
	void createCollection(String colname);
	
	List<UserMongo> findList(int skip, int limit);   
 
    UserMongo findOneByPhone(String phone);  
    
    UserMongo findOneById(String id);
    
    List<UserMongo> findListByName(String name, int skip, int limit);
    
    void insert(UserMongo user);  
 
    void update(UserMongo user); 
    
    void update(UserMongo user, String phone); 
    
    void remove(UserMongo user); 
}
