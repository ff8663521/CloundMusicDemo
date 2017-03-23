package redis.demo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import redis.clients.jedis.Jedis;

public class RedisTest {
	
	private static Jedis jedis;

	static {
		// ����redis������
		//jedis = new Jedis("192.168.0.100", 6379);
		jedis = new Jedis("localhost");
		// Ȩ����֤
		jedis.auth("123456");
	}
	
	/**
	 * redis��������
	 */
	// @Test
	public void testPing() {
		System.out.println("Server is running: "+jedis.ping()); 
	}
	
	
    /**
     * redis�洢�ַ���
     */
    //@Test
    public void testString() {
        //-----�������----------  
        jedis.set("name","xinxin");//��key-->name�з�����value-->xinxin  
        System.out.println(jedis.get("name"));//ִ�н����xinxin  
        
        jedis.append("name", " is my lover"); //ƴ��
        System.out.println(jedis.get("name")); 
        
        jedis.del("name");  //ɾ��ĳ����
        System.out.println(jedis.get("name"));
        //���ö����ֵ��
        jedis.mset("name","liuling","age","23","qq","476777XXX");
        jedis.incr("age"); //���м�1����
        System.out.println(jedis.get("name") + "-" + jedis.get("age") + "-" + jedis.get("qq"));
        
    }
    
    /**
     * redis����Map
     */
    //@Test
    public void testMap() {
        //-----�������----------  
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", "xinxin");
        map.put("age", "22");
        map.put("qq", "123456");
        jedis.hmset("user",map);
        //ȡ��user�е�name��ִ�н��:[minxr]-->ע������һ�����͵�List  
        //��һ�������Ǵ���redis��map�����key����������Ƿ���map�еĶ����key�������key���Ը�������ǿɱ����  
        List<String> rsmap = jedis.hmget("user", "name", "age", "qq");
        System.out.println(rsmap);  
  
        //ɾ��map�е�ĳ����ֵ  
        jedis.hdel("user","age");
        System.out.println(jedis.hmget("user", "age")); //��Ϊɾ���ˣ����Է��ص���null  
        System.out.println(jedis.hlen("user")); //����keyΪuser�ļ��д�ŵ�ֵ�ĸ���2 
        System.out.println(jedis.exists("user"));//�Ƿ����keyΪuser�ļ�¼ ����true  
        System.out.println(jedis.hkeys("user"));//����map�����е�����key  
        System.out.println(jedis.hvals("user"));//����map�����е�����value 
  
        Iterator<String> iter=jedis.hkeys("user").iterator();  
        while (iter.hasNext()){  
            String key = iter.next();  
            System.out.println(key+":"+jedis.hmget("user",key));  
        }  
    }
    
    /** 
     * jedis����List 
     */  
   // @Test  
    public void testList(){
    	
        //��ʼǰ�����Ƴ����е�����  
        jedis.del("java framework");  
        System.out.println(jedis.lrange("java framework",0,-1));  
        //����key java framework�д����������  
        //��ͷѹ�룬�Ƚ���� [hibernate, struts, spring]
        jedis.lpush("java framework","spring");  
        jedis.lpush("java framework","struts");  
        jedis.lpush("java framework","hibernate");  
        //��ȡ����������jedis.lrange�ǰ���Χȡ����  
        // ��һ����key���ڶ�������ʼλ�ã��������ǽ���λ�ã�jedis.llen��ȡ���� -1��ʾȡ������  
        System.out.println(jedis.lrange("java framework",0,-1));  
        
        jedis.del("java framework");
        //��βѹ�룬�Ƚ��ȳ� [spring, struts, hibernate]
        jedis.rpush("java framework","spring");  
        jedis.rpush("java framework","struts");  
        jedis.rpush("java framework","hibernate"); 
        System.out.println(jedis.lrange("java framework",0,-1));
    }  
    
    /** 
     * jedis����Set 
     */  
//    @Test  
    public void testSet(){  
        //���  
        jedis.sadd("user1","liuling");  
        jedis.sadd("user1","xinxin");  
        jedis.sadd("user1","ling");  
        jedis.sadd("user1","zhangxinxin");
        jedis.sadd("user1","who");  
        //�Ƴ�noname  
        jedis.srem("user1","who");  
        System.out.println(jedis.smembers("user1"));//��ȡ���м����value  
        System.out.println(jedis.sismember("user1", "who"));//�ж� who �Ƿ���user���ϵ�Ԫ��  
        System.out.println(jedis.srandmember("user1"));  //�������set��һ��Ԫ��
        System.out.println(jedis.scard("user1"));//���ؼ��ϵ�Ԫ�ظ���  
    }  
  
   // @Test  
    public void test() throws InterruptedException {  
        //jedis ����  
        //ע�⣬�˴���rpush��lpush��List�Ĳ�������һ��˫���������ӱ��������ģ�  
        jedis.del("a");//��������ݣ��ټ������ݽ��в���  
        jedis.rpush("a", "1");  
        jedis.lpush("a","6");  
        jedis.lpush("a","3");  
        jedis.lpush("a","9");  
        System.out.println(jedis.lrange("a",0,-1));// [9, 3, 6, 1]  
        System.out.println(jedis.sort("a")); //[1, 3, 6, 9]  //�����������  
        System.out.println(jedis.lrange("a",0,-1));  
    }  
    
}
