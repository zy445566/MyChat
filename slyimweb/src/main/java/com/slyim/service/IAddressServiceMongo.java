package com.slyim.service;

import java.util.List;

import com.slyim.pojo.AddressMongo;

public interface IAddressServiceMongo {
	void createCollection(String colname);
	
	List<AddressMongo> findList(String myId);
	
	AddressMongo findOneByMyIdAndFriendId(String myId, String friendId);
 
    boolean addFriend(AddressMongo addressMongo);  
    
    boolean delFriend(AddressMongo addressMongo);
    
    boolean blackFriend(AddressMongo addressMongo);
    
    void insert(AddressMongo addressMongo);
    
    void updataById(AddressMongo addressMongo);
}
