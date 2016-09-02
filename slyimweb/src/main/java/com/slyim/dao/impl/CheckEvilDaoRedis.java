package com.slyim.dao.impl;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Repository;

import com.slyim.dao.ABaseRedisDao;
import com.slyim.dao.ICheckEvilDaoRedis;
import com.slyim.pojo.UserRedis;


@Repository
public class CheckEvilDaoRedis  extends ABaseRedisDao<String, UserRedis> implements ICheckEvilDaoRedis {

	@Override
	public int get(final String checkKey) {
		int result = redisTemplate.execute(new RedisCallback<Integer>() {  
            public Integer doInRedis(RedisConnection connection)  
                    throws DataAccessException {  
                RedisSerializer<String> serializer = getRedisSerializer();
                byte[] key  = serializer.serialize("CheckEvil:"+checkKey);
                byte[] value =connection.get(key);
                String strValue=serializer.deserialize(value);
                return Integer.parseInt(strValue);
            }  
        });  
        return result; 
	}

	@Override
	public boolean set(final String checkKey, final int checkvalue, final long checkexpire) {
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {  
            public Boolean doInRedis(RedisConnection connection)  
                    throws DataAccessException {  
                RedisSerializer<String> serializer = getRedisSerializer();
                byte[] key  = serializer.serialize("CheckEvil:"+checkKey);
                byte[] value  = serializer.serialize(String.valueOf(checkvalue));
                connection.set(key, value);
                return connection.expire(key, checkexpire);
            }  
        });  
        return result; 
	}

	@Override
	public boolean isExists(final String checkKey) {
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {  
            public Boolean doInRedis(RedisConnection connection)  
                    throws DataAccessException {  
                RedisSerializer<String> serializer = getRedisSerializer();
                byte[] key  = serializer.serialize("CheckEvil:"+checkKey);
                return connection.exists(key);
            }  
        });  
        return result; 
	}

	@Override
	public int incr(final String checkKey, final long checkexpire) {
		int result = redisTemplate.execute(new RedisCallback<Integer>() {  
            public Integer doInRedis(RedisConnection connection)  
                    throws DataAccessException {  
                RedisSerializer<String> serializer = getRedisSerializer();
                byte[] key  = serializer.serialize("CheckEvil:"+checkKey);
                connection.expire(key, checkexpire);
                return connection.incr(key).intValue();
            }  
        });  
		return result;
	}


	
}
