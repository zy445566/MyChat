package com.slyim.dao;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.data.redis.core.StringRedisTemplate;  
import org.springframework.data.redis.serializer.RedisSerializer; 

public abstract class ABaseRedisDao<K, V> {
    @Autowired  
    protected StringRedisTemplate redisTemplate;  

    public StringRedisTemplate getRedisTemplate() {
		return this.redisTemplate;
	}

	public void setRedisTemplate(StringRedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	/** 
     * 获取 RedisSerializer 
     */  
    protected RedisSerializer<String> getRedisSerializer() {  
        return redisTemplate.getStringSerializer();  
    }
    
	public static Map<String, Object> reflectVar(Object obj){
        Field[] fields= obj.getClass().getDeclaredFields();
        Map<String,Object> map = new HashMap<String,Object>();
        for(int i=0;i<fields.length;i++){
        	char[] cString=fields[i].getName().toCharArray();
        	cString[0]-=32;
        	String mName="get"+String.valueOf(cString);
        	Method m;
			try {
				m = obj.getClass().getDeclaredMethod(mName);
				map.put(fields[i].getName(),m.invoke(obj));
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        }
        return map;
	}
	public static boolean reflectSet(Object obj, Map<String,Object> map){
		String mName;
    	Method m;
		for(String mapkey : map.keySet()){
			mName="set"+mapkey;
			try {
				m = obj.getClass().getDeclaredMethod(mName);
				m.invoke(obj, map.get(mapkey));
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
        }
		return true;
	}
}
