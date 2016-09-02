package com.slyim.service;

public interface ICheckEvilServiceRedis {

	public int get(String checkKey);
	
	public boolean isExists(String checkKey);
	
	public int incr(String checkKey, long checkexpire);
	
	public boolean set(String checkKey, int checkvalue, long checkexpire);
}
