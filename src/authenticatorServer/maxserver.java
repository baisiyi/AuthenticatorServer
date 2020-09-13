package authenticatorServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import des.DesEncrypt;

public class maxserver {
	@SuppressWarnings({ "resource", "unused" })
	public static void main(String[] args) throws IOException, InterruptedException {
		
		
		//��TGS����Ktgs
		String Ktgs = DesEncrypt.getRandomKey(64);
		ChattoTGS chattotgs =  new ChattoTGS(Ktgs);
		
		int port=55533;
		ServerSocket server=new ServerSocket(port);
		System.out.println("server ��һֱ�ȴ�����");
		//Primary primary = new Primary();
		while(true) {
			Socket socket=server.accept();
			
			new Thread(()->{
				Primary primary = new Primary();
				
				//��ȡ�ͻ���ip
				InetAddress addr = socket.getInetAddress();
				String ad = addr.getHostAddress();
				System.out.println(ad);
				//���տͻ�����Ϣ
				DataInputStream dis = null;
				try {
					dis = new DataInputStream(socket.getInputStream());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				DataOutputStream dos = null;
				try {
					dos = new DataOutputStream(socket.getOutputStream());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					String data = null;
					try {
						data = dis.readUTF();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("client:"+data);
					// ����data,�ж�ָ��
					int order = Integer.parseInt(data.substring(0, 1));
					switch (order) {
							// ע��
						case (0): {
							if (primary.signUp(data)) {
								data = "61";
							} else {
								data = "60";
							}
							break;
						}
						// ��Կ����
						case (1): {
							if (primary.publicKey(data)) {
								data = "71";
							} else {
								data = "70";
							}
								break;
						}
							// �û���¼
						case (2): {
							if(primary.signIn(data)) {
								//�����û�id
								String id = data.substring(1,10);
								try {
									data = "81"+primary.AStoC(id,ad,Ktgs);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}else {
								data = "80";
							}
							break;
						}
					}
					System.out.println("server answer:"+data);
					try {
						dos.writeUTF(data);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						dos.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				try {
					dis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}).start();
		}
		
	}
}
