package edu.xidian.enc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import edu.xidian.message.Message;
import edu.xidian.message.MsgType;

public class ClientTest {
	
	/*public static void main(String[] args) {
		System.out.println("client is starting...");
		try {
			ArrayList<String> info = new ArrayList<String>();
			info.add("hello");
			info.add("world");
			Message outMes=new Message(MsgType.changeIP,info);
			//加密
			System.out.println("*************加密*************");
			String datatemp = SerializeUtil.serialize(outMes);  
            String str = MD5Util.convertMD5(datatemp);
			//System.out.println(str);
			 //传输
			Socket socket = new Socket("127.0.0.1",10000);
			ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(str);
			oos.flush();
			//获得信息
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			str = (String)ois.readObject();
			//解密
			String str2 = MD5Util.convertMD5(str);
			Message msg = (Message)SerializeUtil.deserialize(str2);  
		    if(msg.getType().equals(MsgType.SendFile)) {
		    	System.out.println(msg.getBody());
			}
			socket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		} 
	}*/
	public static void main(String [] args) throws Exception {
		String uid = "12";
		String ip = "127.0.0.1";
//		String[] scIPAddr = {"1","3","2","4"};
//		String installPath = "C://profile";
//		String jdkPath = "D://profile";
		//int eid = insertEvent(ip,"setupTomcat");
		//发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9100);
//			String[] values = new String[4];
//			values[0] = scIPAddr[0];
//			values[1] = scIPAddr[1];
//			values[2] = installPath;
//			values[3] = jdkPath;
			Message msg = new Message(MsgType.setupTomcat, uid,"123456");
			//加密
			String datatemp = SerializeUtil.serialize(msg);  
			System.out.println("加密前的序列化串："+"**************");
			System.out.println(datatemp);
			
         //   String str = MD5Util.convertMD5(datatemp);
            byte[] str = AESUtil.encrypt(datatemp,ip);
            System.out.println("加密后的序列化串："+"**************");
            System.out.println(new String(str,"iso-8859-1"));
            //传输
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			oos.writeObject(str);
			//获得反馈信息
			/*ObjectInputStream ois = new ObjectInputStream(
					socket.getInputStream());
			str = (byte[])ois.readObject();
			//解密
			//String str2 = MD5Util.convertMD5(str);
			byte[] str2 = AESUtil.decrypt(str);
			String str1 = new String(str2);
			msg = (Message)SerializeUtil.deserialize(str1); 
			if (msg.getType().equals(MsgType.setupTomcat)) {
				String ret = (String)msg.getValues();
				if(ret.equals("success")||ret.equals("executing")){
					//return eid;
				}
				System.out.println(ret);
			}*/
			socket.close();
		}  catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//return eid;
	}
}
