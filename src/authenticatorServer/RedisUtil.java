package authenticatorServer;

import redis.clients.jedis.Jedis;

public class RedisUtil {
	
	static Jedis jedis;
	
	public RedisUtil(){
		//���ӱ��ص� Redis����
		jedis = new Jedis("127.0.0.1");
	}
	
	public void set(String key,String value) {
		jedis.rpush(key, value);
	}
	
	public  String getpublickey(String key) {
		System.out.println("ȡ��Կkey"+key);
		return jedis.lrange(key,1,1).get(0);
	}
	
	public  String getpassword(String key) {
		return jedis.lrange(key,0,0).get(0);
	}
	
	public boolean isexists(String key) {
		return jedis.exists(key);
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
