package com.slyim.controller;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.slyim.common.ReadWriteBase;
import com.slyim.common.ReadWriteBase.FileType;
import com.slyim.pojo.AddressMongo;
import com.slyim.pojo.JsonBack;
import com.slyim.pojo.UserMongo;
import com.slyim.service.impl.AddressServiceMongo;
import com.slyim.service.impl.UserServiceMongo;

@Controller
@RequestMapping("/address") 
public class AddressController {
	private JsonBack jsonBack = new JsonBack();
	private AddressMongo addressMongo= new AddressMongo();
	@Autowired
	private AddressServiceMongo addressServiceMongo;
	@Autowired
	private UserServiceMongo userServiceMongo;

	@RequestMapping("/getAddressList")
	@ResponseBody
	public JsonBack getAddressList(HttpServletRequest request, HttpSession session){
		String myId=(String)session.getAttribute("id");
		List<AddressMongo> addressList=addressServiceMongo.findList(myId);
		jsonBack.setCode("0");
		jsonBack.setMsg("获取联系人成功");
		jsonBack.setRes(addressList);
		return jsonBack;
	}
	@RequestMapping("/addFriend")
	@ResponseBody
	public JsonBack addFriend(HttpServletRequest request, HttpSession session){
		String myId=(String)session.getAttribute("id");
		String friendId=(String)request.getParameter("friendId");
		String commit=(String)request.getParameter("commit");
		if (myId==null || myId.equals("")) {
			jsonBack.setCode("-1");
			jsonBack.setMsg("请先登录");
			jsonBack.setRes(null);
			return jsonBack;
		}
		AddressMongo isAdress=addressServiceMongo.findOneByMyIdAndFriendId(myId, friendId);
		if (isAdress!=null) {
			if (isAdress.getState()!=0) {
				jsonBack.setCode("-2");
				jsonBack.setMsg("已添加该好友");
				jsonBack.setRes(null);
			} else {
//				System.out.println("to setState");
				isAdress.setState(1);
				addressServiceMongo.updataById(isAdress);
				jsonBack.setCode("0");
				jsonBack.setMsg("添加好友成功");
				jsonBack.setRes(isAdress);
			}
			return jsonBack;
		}
		UserMongo friend=userServiceMongo.findOneById(friendId);
		if (friend==null) {
			jsonBack.setCode("-3");
			jsonBack.setMsg("添加好友的用户不存在");
			jsonBack.setRes(null);
			return jsonBack;
		}
		addressMongo.setState(1);
		addressMongo.setMyId(myId);
		addressMongo.setFriendId(friendId);
		addressMongo.setFriendName(friend.getName());
		addressMongo.setFriendAvatar(friend.getAvatar());
		addressMongo.setFriendSign(friend.getSign());
		addressMongo.setCommit(null);
		addressServiceMongo.insert(addressMongo);
		//-------------------------------------------
		UserMongo my=userServiceMongo.findOneById(myId);
		AddressMongo myaddress=new AddressMongo();
		myaddress.setState(0);
		myaddress.setMyId(friendId);
		myaddress.setFriendId(myId);
		myaddress.setFriendName(my.getName());
		myaddress.setFriendAvatar(my.getAvatar());
		myaddress.setFriendSign(my.getSign());
		myaddress.setCommit(commit);
		addressServiceMongo.insert(myaddress);
		jsonBack.setCode("0");
		jsonBack.setMsg("添加好友申请已发出");
		jsonBack.setRes(addressMongo);
		return jsonBack;
	}
	@RequestMapping("/delFriend")
	@ResponseBody
	public JsonBack delFriend(HttpServletRequest request, HttpSession session){
		String myId=(String)session.getAttribute("id");
		String friendId=(String)request.getParameter("friendId");
		if (myId==null || myId.equals("")) {
			jsonBack.setCode("-1");
			jsonBack.setMsg("请先登录");
			jsonBack.setRes(null);
			return jsonBack;
		}
		AddressMongo isAdress=addressServiceMongo.findOneByMyIdAndFriendId(myId, friendId);
		if (isAdress==null) {
			jsonBack.setCode("0");
			jsonBack.setMsg("这个好友您还未添加或已经删除");
			jsonBack.setRes(null);
			return jsonBack;
		} else if (isAdress.getState()==1) {
			addressServiceMongo.delFriend(isAdress);
			jsonBack.setCode("0");
			jsonBack.setMsg("好友删除成功");
			jsonBack.setRes(null);
			return jsonBack;
		} else {
			jsonBack.setCode("-2");
			jsonBack.setMsg("您没这个好友或这个好友已经被拉黑");
			jsonBack.setRes(null);
			return jsonBack;
		}
	}
	@RequestMapping("/backFriend")
	@ResponseBody
	public JsonBack backFriend(HttpServletRequest request, HttpSession session){
		String myId=(String)session.getAttribute("id");
		String friendId=(String)request.getParameter("friendId");
		AddressMongo isAdress=addressServiceMongo.findOneByMyIdAndFriendId(myId, friendId);
		if (myId==null || myId.equals("")) {
			jsonBack.setCode("-1");
			jsonBack.setMsg("请先登录");
			jsonBack.setRes(null);
			return jsonBack;
		}
		if (isAdress==null) {
			jsonBack.setCode("-2");
			jsonBack.setMsg("这个好友您还未添加");
			jsonBack.setRes(null);
			return jsonBack;
		} else if (isAdress.getState()==2) {
			isAdress.setState(1);
			addressServiceMongo.updataById(isAdress);
			jsonBack.setCode("0");
			jsonBack.setMsg("这个好友已重新被加为好友");
			jsonBack.setRes(isAdress);
			return jsonBack;
		} else {
			jsonBack.setCode("-3");
			jsonBack.setMsg("你还未拉黑该好友，不需要重新添加");
			jsonBack.setRes(isAdress);
			return jsonBack;
		}
	}
	@RequestMapping("/blackFriend")
	@ResponseBody
	public JsonBack blackFriend(HttpServletRequest request, HttpSession session){
		String myId=(String)session.getAttribute("id");
		String friendId=(String)request.getParameter("friendId");
		if (myId==null || myId.equals("")) {
			jsonBack.setCode("-1");
			jsonBack.setMsg("请先登录");
			jsonBack.setRes(null);
			return jsonBack;
		}
		AddressMongo isAdress=addressServiceMongo.findOneByMyIdAndFriendId(myId, friendId);
		if (isAdress==null) {
			jsonBack.setCode("-2");
			jsonBack.setMsg("这个好友您还未添加");
			jsonBack.setRes(null);
			return jsonBack;
		} else if (isAdress.getState()==2) {
			jsonBack.setCode("0");
			jsonBack.setMsg("这个好友已经被您拉黑");
			jsonBack.setRes(isAdress);
			return jsonBack;
		} else {
			isAdress.setState(2);
			addressServiceMongo.updataById(isAdress);
			jsonBack.setCode("0");
			jsonBack.setMsg("拉黑好友成功");
			jsonBack.setRes(isAdress);
			return jsonBack;
		}
	}
}
