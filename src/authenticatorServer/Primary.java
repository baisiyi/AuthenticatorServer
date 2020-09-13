package authenticatorServer;
import java.io.IOException;

import des.DesEncrypt;
import md5.MD5;
import rsa.RSA;

public class Primary {
	
	static RedisUtil redisserver;
	
	Primary(){
		redisserver = new RedisUtil();
		//redisserver.flushDB();
	}
	
	@SuppressWarnings("unused")
	public boolean signIn(String data) {
		System.out.println("用户正在登陆");
		String id = data.substring(1,10);
		String order = data.substring(10, 11);
		String time1 = data.substring(11,24);
		String password = data.substring(24,data.length());
		System.out.println("用户账号："+id);
		System.out.println("用户密码："+password);
		//
		if(!redisserver.isexists(id)) {
			System.out.println("账户未注册");
			return false;
		};
		MD5 md = new MD5();
		password = md.start(password);
		System.out.println("用户密码加密结果："+password);
			if(redisserver.getpassword(id).equals(password)) {
				System.out.println("用户登陆成功");
				return true;
			}else{
				return false;
			}
	}
	
	public boolean publicKey(String data) {

		String n = data.substring(10, 15);
		@SuppressWarnings("unused")
		String e = data.substring(15, data.length());
		String id = data.substring(1, 10);
		
		//保存公钥
		redisserver.set(id, n);
		return true;
	}
	
	public boolean signUp(String data) {

		System.out.println("用户正在注册");
		String id = data.substring(1,10);//提取id
		
		int password_length = Integer.parseInt(data.substring(10,12));
		String password = data.substring(12, (12+password_length));//提取密码长度
		System.out.println("用户账号："+id);
		System.out.println("用户密码："+password);
		
		//查验账号是否已被注册
			if(redisserver.isexists(id)) {
				System.out.println("账号已被注册");
				return false;
			};
		MD5 md = new MD5();
		password = md.start(password);
		System.out.println("用户密码加密结果："+password);
		redisserver.set(id, password);
		if(!redisserver.getpassword(id).equals(password))
				return false;
		return true;
	}

	
	public String makeTickets(String Ktgs, String Kc_tgs,String id,String ad,String TS2,String Lifetime2) throws IOException {
		String ticket = null;

		System.out.println("Kctgs:"+Kc_tgs);
		ticket = Kc_tgs+id+"127127127127"+"1"+TS2+Lifetime2;
		DesEncrypt desencrypt = new DesEncrypt(ticket,Ktgs);
		desencrypt.encrypt();
		ticket = desencrypt.ciphertexts;
		System.out.println("ticket:"+ticket);
		return ticket;
	}
	
	public String AStoC(String id,String ad,String Ktgs) throws IOException, InterruptedException {
		String data = "";
		
		//随机生成密钥
		String Kc_tgs = DesEncrypt.getRandomKey(64);
		System.out.println("Kc_tgs:"+Kc_tgs);
		String TS2 = System.currentTimeMillis()+"";
		String Lifetime2 = System.currentTimeMillis()+60000+"";
		String ticket = makeTickets(Ktgs, Kc_tgs,id ,ad,TS2,Lifetime2);

		String data_f = Kc_tgs+"1"+TS2+Lifetime2;
		for(int i=0;i<data_f.length();i++) {
			data+=DesEncrypt.chartoascii(data_f.charAt(i));
		}
		data = data+ticket;
		
		RSA rsa = new RSA();

		int n = Integer.parseInt(redisserver.getpublickey(id));

		String t="";
		String str ="";
		for(int i=0;i<124;i++) {
			t = "" + rsa.Algorithm(Integer.parseInt((String) data.substring(i*13, (i+1)*13), 2),65537,n);
			switch(t.length()) {
			case(4):t="0"+t;break;
			case(3):t="00"+t;break;
			case(2):t="000"+t;break;
			case(1):t="0000"+t;break;
			}
			str+=t;
		}
		t=""+rsa.Algorithm(Integer.parseInt((String)data.substring(124*13), 2),65537,n);
		switch(t.length()) {
		case(4):t="0"+t;break;
		case(3):t="00"+t;break;
		case(2):t="000"+t;break;
		case(1):t="0000"+t;break;
		}
		str+=t;
		data = str;
		System.out.println("data:"+data);
		return data;
	}

}
