package com.slyim.send;

import java.util.HashMap;
import java.util.Map;

import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

public class SendSms implements Runnable {
	private static final String url="http://gw.api.taobao.com/router/rest";
	private static final String appkey="23317645";
	private static final String secret="d4fd27e4b01822b14fad62d6b4b7e0aa";
	private int method;
	private String phone;
	private Map<String,String> map=new HashMap<String,String>(); 
	
	public boolean authUser(String phone,String password){
		this.method=1;
		this.phone=phone;
		this.map.put("password", password);
		Thread thread = new Thread(this);  
		thread.start();
		return true;

	}
	public boolean TAuthUser(){
		String password=this.map.get("password");
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setSmsType("normal");
		req.setSmsFreeSignName("身份验证");
		req.setSmsParamString("{\"code\":\""+password+"\",\"product\":\"暗恋通讯录\"}");
		req.setRecNum(phone);
		req.setSmsTemplateCode("SMS_3865063");
		try{
			AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req); 
			return rsp.isSuccess();			
		}catch (Exception e){
			return false;
		}
	}
	public void run() {
		// TODO Auto-generated method stub
		switch(this.method){
		case 1:
			this.TAuthUser();
			break;
		default:
			break;
		}
		
		
	}
	
}
