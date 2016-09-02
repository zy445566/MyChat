package com.slyim.common;

public class MyUrl {
	private static String _baseUrl="http://www.slyim.com/";
	
	public static String baseUrl(){
		return MyUrl._baseUrl;
	}
	public static String getAppUrl(){
		return MyUrl._baseUrl+"/app/";
	}
	public static String getLoginUrl(){
		return MyUrl._baseUrl+"/login.html";
	}
}
