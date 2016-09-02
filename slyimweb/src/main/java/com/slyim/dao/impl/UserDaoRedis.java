package com.slyim.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.dao.DataAccessException;  
import org.springframework.data.redis.connection.RedisConnection;  
import org.springframework.data.redis.core.RedisCallback;  
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.slyim.dao.ABaseRedisDao;
import com.slyim.dao.IUserDaoRedis;
import com.slyim.pojo.UserRedis;

import org.springframework.data.redis.connection.RedisZSetCommands.Tuple;

@Repository
public class UserDaoRedis  extends ABaseRedisDao<String, UserRedis> implements IUserDaoRedis  {


	public boolean setNum(final long num){
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {  
            public Boolean doInRedis(RedisConnection connection)  
                    throws DataAccessException {  
                RedisSerializer<String> serializer = getRedisSerializer();
                byte[] key  = serializer.serialize("userNum");
                byte[] userNum  = serializer.serialize(String.valueOf(num));
                connection.set(key, userNum);
                return true;
            }  
        });  
        return result; 
	}
	public long getNum(){
		long result = redisTemplate.execute(new RedisCallback<Long>() {  
            public Long doInRedis(RedisConnection connection)  
                    throws DataAccessException {  
                RedisSerializer<String> serializer = getRedisSerializer();
                byte[] key  = serializer.serialize("userNum");
                byte[] userNum  =connection.get(key);
                String stringNum = serializer.deserialize(userNum);
                long num =Long.parseLong(stringNum);
                return num;
            }  
        });  
        return result; 
	}	
	public boolean add(final UserRedis user) {
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {  
            public Boolean doInRedis(RedisConnection connection)  
                    throws DataAccessException {  
                RedisSerializer<String> serializer = getRedisSerializer();
                Map<byte[], byte[]> hash = new HashMap<byte[], byte[]>();
                Map<String,Object> map =UserDaoRedis.reflectVar(user);
                byte[] hashkey;byte[] hashValue;
                for(String mapkey : map.keySet()){
                	hashkey =serializer.serialize(mapkey);
                	hashValue =serializer.serialize((String)map.get(mapkey));
                	hash.put(hashkey, hashValue);
                }
                byte[] key  = serializer.serialize(user.getId());
                connection.hMSet(key, hash);
                return true;
            }  
        });  
        return result; 
	}
	

	public boolean add(final List<UserRedis> list) {
		Assert.notEmpty(list);  
        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {  
            public Boolean doInRedis(RedisConnection connection)  
                    throws DataAccessException {  
                RedisSerializer<String> serializer = getRedisSerializer();  
                for (UserRedis user : list) {  
                	Map<byte[], byte[]> hash = new HashMap<byte[], byte[]>();
                	Map<String,Object> map =UserDaoRedis.reflectVar(user);
                    byte[] hashkey;byte[] hashValue;
                    for(String mapkey : map.keySet()){
                    	hashkey =serializer.serialize(mapkey);
                    	hashValue =serializer.serialize((String)map.get(mapkey));
                    	hash.put(hashkey, hashValue);
                    }
                    byte[] key  = serializer.serialize(user.getId());
                    connection.hMSet(key, hash);  
                }  
                return true;  
            }  
        }, false, true);
        return result;
	}


	public void delete(String key) {
		List<String> list = new ArrayList<String>();  
        list.add(key);  
        delete(list);
		
	}


	public void delete(List<String> keys) {
		redisTemplate.delete(keys);
		
	}


	public boolean update(final UserRedis user) {
		String key = user.getId();  
        if (get(key) == null) {  
            throw new NullPointerException("数据行不存在, key = " + key);  
        }
        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {  
            public Boolean doInRedis(RedisConnection connection)  
                    throws DataAccessException {  
                RedisSerializer<String> serializer = getRedisSerializer();
                Map<byte[], byte[]> hash = new HashMap<byte[], byte[]>(); 
                Map<String,Object> map =UserDaoRedis.reflectVar(user);
                byte[] hashkey;byte[] hashValue;
                for(String mapkey : map.keySet()){
                	hashkey =serializer.serialize(mapkey);
                	hashValue =serializer.serialize((String)map.get(mapkey));
                	hash.put(hashkey, hashValue);
                }
                byte[] key  = serializer.serialize(user.getId());
                connection.hMSet(key, hash); 
                return true;  
            }

        });  
        return result;
	}


	public UserRedis get(final String keyId) {
		UserRedis result = redisTemplate.execute(new RedisCallback<UserRedis>() {  
            public UserRedis doInRedis(RedisConnection connection)  
                    throws DataAccessException {
            	UserRedis user=new UserRedis();
                RedisSerializer<String> serializer = getRedisSerializer();
                byte[] key = serializer.serialize(keyId);
                Map<byte[], byte[]> value = connection.hGetAll(key);
                if (value == null) {  
                    return null;  
                }
                Map<String, Object> map=new HashMap<String,Object>();
                for(byte[] mapkey : value.keySet()){
                	map.put(serializer.deserialize(mapkey), serializer.deserialize(value.get(mapkey)));
                }
                UserDaoRedis.reflectSet(user, map);
                return user;  
            }  
        });  
        return result;
	}
	@Override
	public boolean setStrongCount(final String strongKey, final List<String> strongValueList) {
		Boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {  
            public Boolean doInRedis(RedisConnection connection)  
                    throws DataAccessException {
                RedisSerializer<String> serializer = getRedisSerializer();
                byte[] key = serializer.serialize(strongKey);
                for (String strongValue : strongValueList) {
                	byte[] value = serializer.serialize(strongValue);
                	 if (connection.exists(key)) {
                     	connection.zAdd(key, 1, value);
                     } else {
                     	connection.zIncrBy(key, 1, value);
                     }
                }
                return true;  
            }  
        });  
        return result;
	}
	@Override
	public List<String> getStrongCount(final String strongKey, final long start, final long stop) {
		List<String> result = redisTemplate.execute(new RedisCallback<List<String>>() {  
            public List<String> doInRedis(RedisConnection connection)  
                    throws DataAccessException {
                RedisSerializer<String> serializer = getRedisSerializer();
                byte[] key = serializer.serialize(strongKey);
                if (!connection.exists(key)) {
                	return null;
                }
                Set<Tuple> tupleSet=connection.zRangeWithScores(key, start, stop);
                List<String> strongList=new ArrayList<String>();
                for (Tuple tuple: tupleSet) {
                	strongList.add(serializer.deserialize(tuple.getValue()));
                } 
                return strongList;  
            }  
        });  
        return result;
	}

}
