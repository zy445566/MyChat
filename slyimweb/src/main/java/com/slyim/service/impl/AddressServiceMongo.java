package com.slyim.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.slyim.dao.impl.AddressDaoMongo;
import com.slyim.pojo.AddressMongo;
import com.slyim.service.IAddressServiceMongo;


@Service("addressServiceMongo")
public class AddressServiceMongo implements IAddressServiceMongo {

	@Autowired
	private AddressDaoMongo addressDaoMongo;
	@Override
	public void createCollection(String colname) {
		// TODO Auto-generated method stub
		addressDaoMongo.createCollection(colname);

	}

	@Override
	public List<AddressMongo> findList(String myId) {
		// TODO Auto-generated method stub
		return addressDaoMongo.findList(myId);
	}

	@Override
	public boolean addFriend(AddressMongo addressMongo) {
		// TODO Auto-generated method stub
		return addressDaoMongo.addFriend(addressMongo);
	}

	@Override
	public boolean delFriend(AddressMongo addressMongo) {
		// TODO Auto-generated method stub
		return addressDaoMongo.delFriend(addressMongo);
	}

	@Override
	public boolean blackFriend(AddressMongo addressMongo) {
		// TODO Auto-generated method stub
		return addressDaoMongo.blackFriend(addressMongo);
	}

	@Override
	public AddressMongo findOneByMyIdAndFriendId(String myId, String friendId) {
		// TODO Auto-generated method stub
		return addressDaoMongo.findOneByMyIdAndFriendId(myId, friendId);
	}

	@Override
	public void insert(AddressMongo addressMongo) {
		// TODO Auto-generated method stub
		addressDaoMongo.insert(addressMongo);
	}

	@Override
	public void updataById(AddressMongo addressMongo) {
		// TODO Auto-generated method stub
		addressDaoMongo.updataById(addressMongo);
	}

}
