package com.slyim.dao;

public interface ICheckEvilDaoRedis {
	
	public int get(final String checkKey);
	
	public boolean isExists(final String checkKey);
	
	public int incr(final String checkKey, final long checkexpire);
	
	public boolean set(final String checkKey, final int checkvalue, final long checkexpire);
}
