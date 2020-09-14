package authenticatorServer;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import redis.clients.jedis.Jedis;

public class RedisUtil {
	
	static Jedis jedis;
	ReadWriteLock rwl;
	public Lock lock;
	
	
	public RedisUtil(){
		//连接本地的 Redis服务
		jedis = new Jedis("127.0.0.1");
		rwl = new ReentrantReadWriteLock();
		lock = rwl.readLock();
	}
	
	public void set(String key,String value) {
		    jedis.rpush(key, value);	
	}
	
	public  String getpublickey(String key) {
		System.out.println("取公钥key"+key);
		return jedis.lrange(key,1,1).get(0);
	}
	
	public  String getpassword(String key) {
		return jedis.lrange(key,0,0).get(0);
	}
	
	public boolean isexists(String key) {
		//System.out.println(jedis.exists(key));
		lock.lock();
		boolean t = jedis.exists(key);
		lock.unlock();
		return t;
	}
	
	public void flushDB() {
		jedis.flushDB();
	}
	
	public void close() {
	    if (jedis != null) {
	        jedis.close();
	    }
	}
}
