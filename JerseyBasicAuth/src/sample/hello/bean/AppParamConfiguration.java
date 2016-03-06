package sample.hello.bean;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;

import sample.DBOP.*;

import edu.xidian.enc.AESUtil;
import edu.xidian.enc.MD5Util;
import edu.xidian.enc.SerializeUtil;
import edu.xidian.message.Message;
import edu.xidian.message.MsgType;

public class AppParamConfiguration {
	
	public AppParamConfiguration() throws SQLException{
		DBForSCS scs = new DBForSCS();
		scs.Delete("config");
		scs.close();
	}
	/**
	 * 将配置软件参数事件插入到数据库中
	 * @param hostIp
	 * @param opName
	 */
	public int insertEvent(String hostIp,String opName){
		int opID = -1;
		DBOperation dbop = new DBOperation();
		try {
			opID = dbop.insertOperation(hostIp,opName,"");
			dbop.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return opID;
	}
	/**
	 * 更新配置软件参数事件的状态，操作的是opinfo表
	 * @param opID
	 * @param status
	 * @return
	 */
	public boolean updateOpStatus(int opID,String status){
		boolean flag = false;
		DBOperation dbop = new DBOperation();
		try {
			flag = dbop.updateOpStatus(opID,status);
			dbop.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * 修改mysql的参数
	 * 
	 * @param uid
	 * @param ip
	 * @param paramName
	 * @param paramValue
	 * @return
	 * @throws SQLException 
	 */
	public String sendConfigMySqlMsg(String ip,String cfgFilePath, String paramName,
			String paramValue) throws SQLException {
		int opID = insertEvent(ip,"config-MySql");
		DBForSCS scs = new DBForSCS();
		// 发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9200);
			String[] values = new String[3];
			values[0] = cfgFilePath;
			values[1] = paramName;
			values[2] = paramValue;
			Message msg = new Message(MsgType.configMySql, opID+"", values);
			//加密
			String datatemp = SerializeUtil.serialize(msg);  
			byte[] str = AESUtil.encrypt(datatemp,ip);
			//传输
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			oos.writeObject(str);
			//获得反馈信息
			ObjectInputStream ois = new ObjectInputStream(
					socket.getInputStream());
			byte[] rcvstr = (byte[])ois.readObject();
			//解密
			byte[] str2 = AESUtil.decrypt(rcvstr,ip);
			String str1 = new String(str2,"iso-8859-1");
			if(str1.equals("NoSuchAlgorithmException")||str1.equals("NoSuchPaddingException")||str1.equals("InvalidKeyException")||str1.equals("BadPaddingException")||str1.equals("IllegalBlockSizeException")){
				System.out.println("JAVA security, error key");
			}else{
				msg = (Message) SerializeUtil.deserialize(str1);
				if (msg.getType().equals(MsgType.configMySql)) {
					String ret = (String) msg.getValues();
					if (ret.equals("0x0300200")) {
						updateOpStatus(opID, "0x0300200");
						scs.updateConfig(ip, "mysql", paramName, paramValue);
						scs.close();
						return "0x0300200";
					}
				}
			}
			socket.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		updateOpStatus(opID,"0x0300201");
		scs.updateConfig(ip, "mysql", paramName, paramValue);
		scs.close();
		return "0x0300201";
	}

	public String sendConfigTomcatMsg(String ip,String cfgFilePath, String paramName,
			String paramValue) throws SQLException {
		int opID = insertEvent(ip,"config-Tomcat");
		DBForSCS scs = new DBForSCS();
		// 发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9200);
			String[] values = new String[3];
			values[0] = cfgFilePath;
			values[1] = paramName;
			values[2] = paramValue;
			Message msg = new Message(MsgType.configTomcat, opID+"", values);
			//加密
			String datatemp = SerializeUtil.serialize(msg);  
			byte[] str = AESUtil.encrypt(datatemp,ip);
			//传输
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			oos.writeObject(str);
			//获得反馈信息
			ObjectInputStream ois = new ObjectInputStream(
					socket.getInputStream());
			byte[] rcvstr = (byte[])ois.readObject();
			//解密
			byte[] str2 = AESUtil.decrypt(rcvstr,ip);
			String str1 = new String(str2,"iso-8859-1");
			if(str1.equals("NoSuchAlgorithmException")||str1.equals("NoSuchPaddingException")||str1.equals("InvalidKeyException")||str1.equals("BadPaddingException")||str1.equals("IllegalBlockSizeException")){
				System.out.println("JAVA security, error key");
			}else{
				msg = (Message) SerializeUtil.deserialize(str1);
				if (msg.getType().equals(MsgType.configTomcat)) {
					String ret = (String) msg.getValues();
					if (ret.equals("0x0300000")) {
						updateOpStatus(opID, "0x0300000");
						
						scs.updateConfig(ip, "tomcat", paramName, paramValue);
						scs.close();
						return "0x0300000";
					}
				}
			}
			socket.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		updateOpStatus(opID,"0x0300001");
		scs.updateConfig(ip, "tomcat", paramName, paramValue);
		scs.close();
		return "0x0300001";
	}


	public String sendConfigApacheMsg(String ip,String cfgFilePath, String paramName,
			String paramValue) throws SQLException {
		int opID = insertEvent(ip,"config-Apache");
		DBForSCS scs = new DBForSCS();
		// 发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9200);
			String[] values = new String[3];
			values[0] = cfgFilePath;
			values[1] = paramName;
			values[2] = paramValue;
			Message msg = new Message(MsgType.configApache, opID+"", values);
			//加密
			String datatemp = SerializeUtil.serialize(msg);  
			byte[] str = AESUtil.encrypt(datatemp,ip);
			//传输
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			oos.writeObject(str);
			//获得反馈信息
			ObjectInputStream ois = new ObjectInputStream(
					socket.getInputStream());
			byte[] rcvstr = (byte[])ois.readObject();
			//解密
			byte[] str2 = AESUtil.decrypt(rcvstr,ip);
			String str1 = new String(str2,"iso-8859-1");
			if(str1.equals("NoSuchAlgorithmException")||str1.equals("NoSuchPaddingException")||str1.equals("InvalidKeyException")||str1.equals("BadPaddingException")||str1.equals("IllegalBlockSizeException")){
				System.out.println("JAVA security, error key");
			}else{
				msg = (Message) SerializeUtil.deserialize(str1);
				if (msg.getType().equals(MsgType.configApache)) {
					String ret = (String) msg.getValues();
					if (ret.equals("0x0300300")) {
						updateOpStatus(opID, "0x0300300");
						scs.updateConfig(ip, "apache", paramName, paramValue);
						scs.close();
						return "0x0300300";
					}
					
				}
			}
			socket.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		updateOpStatus(opID,"0x0300301");
		scs.updateConfig(ip, "apache", paramName, paramValue);
		scs.close();
		return "0x0300301";
	}

	public String sendConfigNginxMsg(String ip,String cfgFilePath, String paramName,
			String paramValue) throws SQLException {
		int opID = insertEvent(ip,"config-Nginx");
		DBForSCS scs = new DBForSCS();
		// 发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9200);
			String[] values = new String[3];
			values[0] = cfgFilePath;
			values[1] = paramName;
			values[2] = paramValue;
			Message msg = new Message(MsgType.configNginx, opID+"", values);
			
			//加密
			String datatemp = SerializeUtil.serialize(msg);  
			byte[] str = AESUtil.encrypt(datatemp,ip);
			//传输
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			oos.writeObject(str);
			//获得反馈信息
			ObjectInputStream ois = new ObjectInputStream(
					socket.getInputStream());
			byte[] rcvstr = (byte[])ois.readObject();
			//解密
			byte[] str2 = AESUtil.decrypt(rcvstr,ip);
			String str1 = new String(str2,"iso-8859-1");
			if(str1.equals("NoSuchAlgorithmException")||str1.equals("NoSuchPaddingException")||str1.equals("InvalidKeyException")||str1.equals("BadPaddingException")||str1.equals("IllegalBlockSizeException")){
				System.out.println("JAVA security, error key");
			}else{
				msg = (Message) SerializeUtil.deserialize(str1);
				/*
				 * ObjectOutputStream oos = new ObjectOutputStream(
				 * socket.getOutputStream()); oos.writeObject(msg);
				 * ObjectInputStream ois = new ObjectInputStream(
				 * socket.getInputStream()); msg = (Message) ois.readObject();
				 */

				if (msg.getType().equals(MsgType.configNginx)) {
					String ret = (String) msg.getValues();
					if (ret.equals("0x0300400")) {
						updateOpStatus(opID, "0x0300400");
						scs.updateConfig(ip, "nginx", paramName, paramValue);
						scs.close();
						return "0x0300400";
					}
					
				}
			}
			socket.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		updateOpStatus(opID,"0x0300401");
		scs.updateConfig(ip, "nginx", paramName, paramValue);
		scs.close();
		return "0x0300401";
	}

	public String sendConfigZendGuardLoaderMsg(String ip,String cfgFilePath,
			String paramName, String paramValue) throws SQLException {
		int opID = insertEvent(ip,"config-ZendGuardLoader");
		DBForSCS scs = new DBForSCS();
		// 发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9200);
			String[] values = new String[3];
			values[0] = cfgFilePath;
			values[1] = paramName;
			values[2] = paramValue;
			Message msg = new Message(MsgType.configZendGuardLoader, opID+"", values);
			//加密
			String datatemp = SerializeUtil.serialize(msg);  
			byte[] str = AESUtil.encrypt(datatemp,ip);
			//传输
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			oos.writeObject(str);
			//获得反馈信息
			ObjectInputStream ois = new ObjectInputStream(
					socket.getInputStream());
			byte[] rcvstr = (byte[])ois.readObject();
			//解密
			byte[] str2 = AESUtil.decrypt(rcvstr,ip);
			String str1 = new String(str2,"iso-8859-1");
			if(str1.equals("NoSuchAlgorithmException")||str1.equals("NoSuchPaddingException")||str1.equals("InvalidKeyException")||str1.equals("BadPaddingException")||str1.equals("IllegalBlockSizeException")){
				System.out.println("JAVA security, error key");
			}else{
				msg = (Message) SerializeUtil.deserialize(str1);
				if (msg.getType().equals(MsgType.configZendGuardLoader)) {
					String ret = (String) msg.getValues();
					System.out.println(ret);
					if (ret.equals("0x0300500")) {
						updateOpStatus(opID, "0x0300500");
						scs.updateConfig(ip, "zendguardloader", paramName, paramValue);
						scs.close();
						return "0x0300500";
					}else {
						updateOpStatus(opID, ret);
						return ret;
					}
				}
			}
			socket.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		updateOpStatus(opID,"0x0300501");
		scs.updateConfig(ip, "zendguardloader", paramName, paramValue);
		scs.close();
		return "0x0300501";
	}

//	public String sendConfigPythonMsg(String ip, String cfgFilePath,String paramName,
//			String paramValue) {
//		int opID = insertEvent(ip,"config-Python");
//		// 发送Socket消息给Agent
//		try {
//			Socket socket = new Socket(ip, 9200);
//			String[] values = new String[3];
//			values[0] = cfgFilePath;
//			values[1] = paramName;
//			values[2] = paramValue;
//			Message msg = new Message(MsgType.configPython, opID+"", values);
//			//加密
//			String datatemp = SerializeUtil.serialize(msg);  
//			byte[] str = AESUtil.encrypt(datatemp,ip);
//			//传输
//			ObjectOutputStream oos = new ObjectOutputStream(
//					socket.getOutputStream());
//			oos.writeObject(str);
//			//获得反馈信息
//			ObjectInputStream ois = new ObjectInputStream(
//					socket.getInputStream());
//			byte[] rcvstr = (byte[])ois.readObject();
//			//解密
//			byte[] str2 = AESUtil.decrypt(rcvstr,ip);
//			String str1 = new String(str2,"iso-8859-1");
//			if(str1.equals("NoSuchAlgorithmException")||str1.equals("NoSuchPaddingException")||str1.equals("InvalidKeyException")||str1.equals("BadPaddingException")||str1.equals("IllegalBlockSizeException")){
//				System.out.println("JAVA security, error key");
//			}else{
//				msg = (Message) SerializeUtil.deserialize(str1);
//				if (msg.getType().equals(MsgType.configPython)) {
//					String ret = (String) msg.getValues();
//					if (ret.equals("0x0300600")) {
//						updateOpStatus(opID, "0x0300600");
//						return "0x0300600";
//					}
//					
//				}
//			}
//			socket.close();
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		updateOpStatus(opID,"0x0300601");
//		return "0x0300601";
//	}
//
//	public String sendConfigMemcachedMsg(String ip,String cfgFilePath,
//			String paramName, String paramValue) {
//		int opID = insertEvent(ip,"config-Memcached");
//		// 发送Socket消息给Agent
//		try {
//			Socket socket = new Socket(ip, 9200);
//			String[] values = new String[3];
//			values[0] = cfgFilePath;
//			values[1] = paramName;
//			values[2] = paramValue;
//			Message msg = new Message(MsgType.configMemcached, opID+"", values);
//			//加密
//			String datatemp = SerializeUtil.serialize(msg);  
//			byte[] str = AESUtil.encrypt(datatemp,ip);
//			//传输
//			ObjectOutputStream oos = new ObjectOutputStream(
//					socket.getOutputStream());
//			oos.writeObject(str);
//			//获得反馈信息
//			ObjectInputStream ois = new ObjectInputStream(
//					socket.getInputStream());
//			byte[] rcvstr = (byte[])ois.readObject();
//			//解密
//			byte[] str2 = AESUtil.decrypt(rcvstr,ip);
//			String str1 = new String(str2,"iso-8859-1");
//			if(str1.equals("NoSuchAlgorithmException")||str1.equals("NoSuchPaddingException")||str1.equals("InvalidKeyException")||str1.equals("BadPaddingException")||str1.equals("IllegalBlockSizeException")){
//				System.out.println("JAVA security, error key");
//			}else{
//				msg = (Message) SerializeUtil.deserialize(str1);
//				if (msg.getType().equals(MsgType.configMemcached)) {
//					String ret = (String) msg.getValues();
//					if (ret.equals("0x0300700")) {
//						updateOpStatus(opID, "0x0300700");
//						return "0x0300700";
//					}
//				}
//			}
//			socket.close();
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		updateOpStatus(opID,"0x0300701");
//		return "0x0300701";
//	}
//
//	public String sendConfigIISRewriteMsg(String ip,String cfgFilePath,
//			String paramName, String paramValue) {
//		int opID = insertEvent(ip,"config-IISRewrite");
//		// 发送Socket消息给Agent
//		try {
//			Socket socket = new Socket(ip, 9200);
//			String[] values = new String[3];
//			values[0] = cfgFilePath;
//			values[1] = paramName;
//			values[2] = paramValue;
//			Message msg = new Message(MsgType.configIISRewrite, opID+"", values);
//			//加密
//			String datatemp = SerializeUtil.serialize(msg);  
//			byte[] str = AESUtil.encrypt(datatemp,ip);
//			//传输
//			ObjectOutputStream oos = new ObjectOutputStream(
//					socket.getOutputStream());
//			oos.writeObject(str);
//			//获得反馈信息
//			ObjectInputStream ois = new ObjectInputStream(
//					socket.getInputStream());
//			byte[] rcvstr = (byte[])ois.readObject();
//			//解密
//			byte[] str2 = AESUtil.decrypt(rcvstr,ip);
//			String str1 = new String(str2,"iso-8859-1");
//			if(str1.equals("NoSuchAlgorithmException")||str1.equals("NoSuchPaddingException")||str1.equals("InvalidKeyException")||str1.equals("BadPaddingException")||str1.equals("IllegalBlockSizeException")){
//				System.out.println("JAVA security, error key");
//			}else{
//				msg = (Message) SerializeUtil.deserialize(str1);
//				if (msg.getType().equals(MsgType.configIISRewrite)) {
//					String ret = (String) msg.getValues();
//					if (ret.equals("0x0300800")) {
//						updateOpStatus(opID, "0x0300800");
//						return "0x0300800";
//					}
//					
//				}
//			}
//			socket.close();
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		updateOpStatus(opID,"0x0300801");
//		return "0x0300801";
//	}

	/*public String sendConfigASPMsg(String ip,String cfgFilePath, String paramName,
			String paramValue) {
		int opID = insertEvent(ip,"config-ASP");
		// 发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9200);
			String[] values = new String[3];
			values[0] = cfgFilePath;
			values[1] = paramName;
			values[2] = paramValue;
			Message msg = new Message(MsgType.configASP, opID+"", values);
			// 加密
			String datatemp = SerializeUtil.serialize(msg);
			String str = MD5Util.convertMD5(datatemp);
			// 传输
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			oos.writeObject(str);
			// 获得反馈信息
			ObjectInputStream ois = new ObjectInputStream(
					socket.getInputStream());
			str = (String) ois.readObject();
			// 解密
			String str2 = MD5Util.convertMD5(str);
			msg = (Message) SerializeUtil.deserialize(str2);
			if (msg.getType().equals(MsgType.configASP)) {
				String ret = (String) msg.getValues();
				if (ret.equals("success") || ret.equals("executing")) {
				updateOpStatus(opID,"0x300");
					return true;
				}
				
			}
			socket.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}*/

	public String sendConfigFTPMsg(String ip,String cfgFilePath, String paramName,
			String paramValue) throws SQLException {
		int opID = insertEvent(ip,"config-FTP");
		DBForSCS scs = new DBForSCS();
		// 发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9200);
			String[] values = new String[3];
			values[0] = cfgFilePath;
			values[1] = paramName;
			values[2] = paramValue;
			Message msg = new Message(MsgType.configFTP, opID+"", values);
			//加密
			String datatemp = SerializeUtil.serialize(msg);  
			byte[] str = AESUtil.encrypt(datatemp,ip);
			//传输
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			oos.writeObject(str);
			//获得反馈信息
			ObjectInputStream ois = new ObjectInputStream(
					socket.getInputStream());
			byte[] rcvstr = (byte[])ois.readObject();
			//解密
			byte[] str2 = AESUtil.decrypt(rcvstr,ip);
			String str1 = new String(str2,"iso-8859-1");
			if(str1.equals("NoSuchAlgorithmException")||str1.equals("NoSuchPaddingException")||str1.equals("InvalidKeyException")||str1.equals("BadPaddingException")||str1.equals("IllegalBlockSizeException")){
				System.out.println("JAVA security, error key");
			}else{
				msg = (Message) SerializeUtil.deserialize(str1);
				if (msg.getType().equals(MsgType.configFTP)) {
					String ret = (String) msg.getValues();
					if (ret.equals("0x0300900")) {
						updateOpStatus(opID, "0x0300900");
						scs.updateConfig(ip, "ftp", paramName, paramValue);
						scs.close();
						return "0x0300900";
					}
					
				}
			}
			socket.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		updateOpStatus(opID,"0x0300901");
		scs.updateConfig(ip, "ftp", paramName, paramValue);
		scs.close();
		return "0x0300901";
	}

	/*public String sendConfigASPNETMsg(String ip,String cfgFilePath, String paramName,
			String paramValue) {
		int opID = insertEvent(ip,"config-ASPNET");
		// 发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9200);
			String[] values = new String[3];
			values[0] = cfgFilePath;
			values[1] = paramName;
			values[2] = paramValue;
			Message msg = new Message(MsgType.configASPNET, opID+"", values);
			// 加密
			String datatemp = SerializeUtil.serialize(msg);
			String str = MD5Util.convertMD5(datatemp);
			// 传输
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			oos.writeObject(str);
			// 获得反馈信息
			ObjectInputStream ois = new ObjectInputStream(
					socket.getInputStream());
			str = (String) ois.readObject();
			// 解密
			String str2 = MD5Util.convertMD5(str);
			msg = (Message) SerializeUtil.deserialize(str2);
			if (msg.getType().equals(MsgType.configASPNET)) {
				String ret = (String) msg.getValues();
				if (ret.equals("success") || ret.equals("executing")) {
				updateOpStatus(opID,"0x300");
					return true;
				}
				
			}
			socket.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}*/

/*	public String sendConfigSQLServer2008R2Msg(String ip,String cfgFilePath,
			String paramName, String paramValue) {
		int opID = insertEvent(ip,"config-SQLServer2008R2");
		// 发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9200);
			String[] values = new String[3];
			values[0] = cfgFilePath;
			values[1] = paramName;
			values[2] = paramValue;
			Message msg = new Message(MsgType.configSQLServer2008R2, opID+"", values);
			//加密
			String datatemp = SerializeUtil.serialize(msg);  
			byte[] str = AESUtil.encrypt(datatemp,ip);
			//传输
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			oos.writeObject(str);
			//获得反馈信息
			ObjectInputStream ois = new ObjectInputStream(
					socket.getInputStream());
			byte[] rcvstr = (byte[])ois.readObject();
			//解密
			byte[] str2 = AESUtil.decrypt(rcvstr,ip);
			String str1 = new String(str2,"iso-8859-1");
			if(str1.equals("NoSuchAlgorithmException")||str1.equals("NoSuchPaddingException")||str1.equals("InvalidKeyException")||str1.equals("BadPaddingException")||str1.equals("IllegalBlockSizeException")){
				System.out.println("JAVA security, error key");
			}else{
				msg = (Message) SerializeUtil.deserialize(str1);
				if (msg.getType().equals(MsgType.configSQLServer2008R2)) {
					String ret = (String) msg.getValues();
					if (ret.equals("0x0300A00")) {
						updateOpStatus(opID, "0x0300A00");
						return "0x0300A00";
					}
				}
			}
			socket.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		updateOpStatus(opID,"0x0300A01");
		return "0x0300A01";
	}

	public String sendConfigSQLServer2000Msg(String ip,String cfgFilePath,
			String paramName, String paramValue) {
		int opID = insertEvent(ip,"config-SQLServer2000");
		// 发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9200);
			String[] values = new String[3];
			values[0] = cfgFilePath;
			values[1] = paramName;
			values[2] = paramValue;
			Message msg = new Message(MsgType.configSQLServer2000, opID+"", values);
			//加密
			String datatemp = SerializeUtil.serialize(msg);  
			byte[] str = AESUtil.encrypt(datatemp,ip);
			//传输
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			oos.writeObject(str);
			//获得反馈信息
			ObjectInputStream ois = new ObjectInputStream(
					socket.getInputStream());
			byte[] rcvstr = (byte[])ois.readObject();
			//解密
			byte[] str2 = AESUtil.decrypt(rcvstr,ip);
			String str1 = new String(str2,"iso-8859-1");
			if(str1.equals("NoSuchAlgorithmException")||str1.equals("NoSuchPaddingException")||str1.equals("InvalidKeyException")||str1.equals("BadPaddingException")||str1.equals("IllegalBlockSizeException")){
				System.out.println("JAVA security, error key");
			}else{
				msg = (Message) SerializeUtil.deserialize(str1);
				if (msg.getType().equals(MsgType.configSQLServer2000)) {
					String ret = (String) msg.getValues();
					if (ret.equals("0x0300B00")) {
						updateOpStatus(opID, "0x0300B00");
						return "0x0300B00";
					}
					
				}
			}
			socket.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		updateOpStatus(opID,"0x0300B01");
		return "0x0300B01";
	}

	public String sendConfigOracle10gMsg(String ip,String cfgFilePath,
			String paramName, String paramValue) {
		int opID = insertEvent(ip,"config-Oracle10g");
		// 发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9200);
			String[] values = new String[3];
			values[0] = cfgFilePath;
			values[1] = paramName;
			values[2] = paramValue;
			Message msg = new Message(MsgType.configOracle10g, opID+"", values);
			//加密
			String datatemp = SerializeUtil.serialize(msg);  
			byte[] str = AESUtil.encrypt(datatemp,ip);
			//传输
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			oos.writeObject(str);
			//获得反馈信息
			ObjectInputStream ois = new ObjectInputStream(
					socket.getInputStream());
			byte[] rcvstr = (byte[])ois.readObject();
			//解密
			byte[] str2 = AESUtil.decrypt(rcvstr,ip);
			String str1 = new String(str2,"iso-8859-1");
			if(str1.equals("NoSuchAlgorithmException")||str1.equals("NoSuchPaddingException")||str1.equals("InvalidKeyException")||str1.equals("BadPaddingException")||str1.equals("IllegalBlockSizeException")){
				System.out.println("JAVA security, error key");
			}else{
				msg = (Message) SerializeUtil.deserialize(str1);
				if (msg.getType().equals(MsgType.configOracle10g)) {
					String ret = (String) msg.getValues();
					if (ret.equals("0x0300D00")) {
						updateOpStatus(opID, "0x0300D00");
						return "0x0300D00";
					}
					
				}
			}
			socket.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		updateOpStatus(opID,"0x0300D01");
		return "0x0300D01";
	}

	public String sendConfigOracle11gMsg(String ip,String cfgFilePath,
			String paramName, String paramValue) {
		int opID = insertEvent(ip,"config-Oracle11g");
		// 发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9200);
			String[] values = new String[3];
			values[0] = cfgFilePath;
			values[1] = paramName;
			values[2] = paramValue;
			Message msg = new Message(MsgType.configOracle11g, opID+"", values);
			//加密
			String datatemp = SerializeUtil.serialize(msg);  
			byte[] str = AESUtil.encrypt(datatemp,ip);
			//传输
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			oos.writeObject(str);
			//获得反馈信息
			ObjectInputStream ois = new ObjectInputStream(
					socket.getInputStream());
			byte[] rcvstr = (byte[])ois.readObject();
			//解密
			byte[] str2 = AESUtil.decrypt(rcvstr,ip);
			String str1 = new String(str2,"iso-8859-1");
			if(str1.equals("NoSuchAlgorithmException")||str1.equals("NoSuchPaddingException")||str1.equals("InvalidKeyException")||str1.equals("BadPaddingException")||str1.equals("IllegalBlockSizeException")){
				System.out.println("JAVA security, error key");
			}else{
				msg = (Message) SerializeUtil.deserialize(str1);
				if (msg.getType().equals(MsgType.configOracle11g)) {
					String ret = (String) msg.getValues();
					if (ret.equals("0x0300C00")) {
						updateOpStatus(opID, "0x0300C00");
						return "0x0300C00";
					}
				}
			}
			socket.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		updateOpStatus(opID,"0x0300C01");
		return "0x0300C01";
	}

	public String sendConfig360Msg(String ip,String cfgFilePath, String paramName,
			String paramValue) {
		int opID = insertEvent(ip,"config-360");
		// 发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9200);
			String[] values = new String[3];
			values[0] = cfgFilePath;
			values[1] = paramName;
			values[2] = paramValue;
			Message msg = new Message(MsgType.config360, opID+"", values);
			//加密
			String datatemp = SerializeUtil.serialize(msg);  
			byte[] str = AESUtil.encrypt(datatemp,ip);
			//传输
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			oos.writeObject(str);
			//获得反馈信息
			ObjectInputStream ois = new ObjectInputStream(
					socket.getInputStream());
			byte[] rcvstr = (byte[])ois.readObject();
			//解密
			byte[] str2 = AESUtil.decrypt(rcvstr,ip);
			String str1 = new String(str2,"iso-8859-1");
			if(str1.equals("NoSuchAlgorithmException")||str1.equals("NoSuchPaddingException")||str1.equals("InvalidKeyException")||str1.equals("BadPaddingException")||str1.equals("IllegalBlockSizeException")){
				System.out.println("JAVA security, error key");
			}else{
				msg = (Message) SerializeUtil.deserialize(str1);
				if (msg.getType().equals(MsgType.config360)) {
					String ret = (String) msg.getValues();
					if (ret.equals("0x0300E00")) {
						updateOpStatus(opID, "0x0300E00");
						return "0x0300E00";
					}
				}
			}
			socket.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		updateOpStatus(opID,"0x0300E01");
		return "0x0300E01";
	}*/
}
