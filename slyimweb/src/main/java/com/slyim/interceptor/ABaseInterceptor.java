package com.slyim.interceptor;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.slyim.common.MyUser;


//基类拦截器,在此验证用户名密码
public abstract class ABaseInterceptor {

	
	
	 public boolean islogin(HttpServletResponse response, HttpServletRequest request){
		 return MyUser.islogin(response, request);
	 }

}
