package authenticatorServer;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ChattoTGS {
	
	@SuppressWarnings("unused")
	public ChattoTGS(String Ktgs) throws IOException, InterruptedException{
		String host = "192.168.43.180";
        int port = 8887;
        Socket socket = new Socket(host, port);
		System.out.println("连接TGS服务器");
        BufferedReader console=new BufferedReader(new InputStreamReader(System.in));
        DataOutputStream dos=new DataOutputStream(socket.getOutputStream());
        DataInputStream dis=new DataInputStream(socket.getInputStream());
        String data="d"+Ktgs;
        System.out.println("Ktgs:"+Ktgs);
        String getData="";
        while(true) {
            dos.writeUTF(data);
            dos.flush();
            getData=dis.readUTF();
            System.out.println("TGS server:"+getData);
            if(getData.contains("s1")) {
                break;
            }else{
                Thread.sleep(1000);
            }
        }
        System.out.println("与TGS密钥发送完毕");
        dis.close();
        dos.close();
        socket.close();
	}
}
