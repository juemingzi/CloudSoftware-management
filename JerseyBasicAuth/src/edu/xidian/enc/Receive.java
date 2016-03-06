package edu.xidian.enc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import edu.xidian.message.Message;



public class Receive {

	public void  receive() throws Exception{
		String ip = "127.0.0.1";
		ServerSocket serverSocket =new ServerSocket(9100);
		while(true) {
			Socket socket = serverSocket.accept();
			ObjectInputStream ois = new ObjectInputStream(
					socket.getInputStream());
			byte[] rcvstr = (byte[])ois.readObject();
			String str11 = new String(rcvstr,"iso-8859-1");
			System.out.println("加密后的序列化串："+"**************");
			System.out.println(str11);			
			byte[] str2 = AESUtil.decrypt(rcvstr,ip);
			String str1 = new String(str2,"iso-8859-1");
			System.out.println("解密后的序列化串："+"**************");
			System.out.println(str1);
			Message msg = (Message)SerializeUtil.deserialize(str1); 
			System.out.println("解密后的反序列化串："+"**************");
			System.out.println(msg.getType().name());
			
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Receive receive = new Receive();
		try {
			receive.receive();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
