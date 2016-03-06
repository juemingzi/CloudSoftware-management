package edu.xidian.test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import edu.xidian.message.MD5Util;
import edu.xidian.message.Message;
import edu.xidian.message.MsgType;
import edu.xidian.message.SerializeUtil;

public class Md5Test {
	public static void main(String[] args){
		try {
		String[] values=new String[3];
//		values[0]="TCP";
//		values[1]="219.245.68.39";
//		values[2]="9100";
//		values[0]="auditd";
//		values[2]="192.168.0.100";
//		values[0]="eth0:0";
//		values[1]="255.255.255.0";
		values[0]="65535";
		Message outMes=new Message(MsgType.changeUlimit,"123",values);
		//加密
		System.out.println("*************加密*************");
		String datatemp;
		
			datatemp = SerializeUtil.serialize(outMes);
		 
        String str = MD5Util.convertMD5(datatemp);
		//System.out.println(str);
		 //传输
		Socket socket = new Socket("23.95.33.208",9000);
		ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
		oos.writeObject(str);
		oos.flush();
		//获得信息
		ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
		str = (String)ois.readObject();
		//解密
		String str2 = MD5Util.convertMD5(str);
		Message msg = (Message)SerializeUtil.deserialize(str2);  
	    if(msg.getType().equals(MsgType.changeUlimit)) {
	    	System.out.println(((String)msg.getValues()));
		}
		socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
