package com.slyim.service;

import java.util.List;

import com.slyim.pojo.UserRedis;

public interface IUserServiceRedis {
	
	boolean add(UserRedis user);  

    boolean add(List<UserRedis> list);  

    void delete(String key);  

    void delete(List<String> keys);  
 
    boolean update(UserRedis user);

    UserRedis get(String keyId); 
    
    boolean setStrongCount(String strongKey, List<String> strongValueList);
    
    List<String> getStrongCount(String strongKey, long start, long stop);
}
