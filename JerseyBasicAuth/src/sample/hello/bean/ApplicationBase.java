package sample.hello.bean;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;

import javax.ws.rs.QueryParam;

import sample.DBOP.DBForSCS;
import sample.DBOP.DBOperation;

import edu.xidian.enc.AESUtil;
import edu.xidian.enc.MD5Util;
import edu.xidian.enc.SerializeUtil;
import edu.xidian.message.Message;
import edu.xidian.message.MsgType;
import sample.DBOP.*;

public class ApplicationBase {
	
	public ApplicationBase() throws SQLException{
		DBForSCS scs = new DBForSCS();
		scs.Delete("setup");
		scs.close();
	}
	/**
	 * 将安装软件事件插入到数据库中
	 * @param hostIp
	 * @param opName
	 */
	public int insertEvent(String hostIp,String opName,String softPath){
		int opID = -1;
		DBOperation dbop = new DBOperation();
		try {
			String version =dbop.queryVersionBySoftPath(softPath);
			opID = dbop.insertOperation(hostIp,opName,version);
			dbop.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return opID;
	}
	/**
	 * 安装tomcat
	 * @param uid
	 * @param ip
	 * @param scIPAddr
	 * @param installPath
	 * @return
	 * @throws SQLException 
	 */
	public int sendSetupTomcatMsg(String ip,String[] scIPAddr,int spaceRequirement,int zipSpace,String installPath,String jdkPath) throws SQLException{
		int opID = insertEvent(ip,"setup-Tomcat",scIPAddr[1]);
		
		System.out.println("  ENTER  SEND ~~~~~~~~~");
		GetInstallPath getpath = new GetInstallPath();
		String oldpath = getpath.getpath(ip, "Tomcat");
		getpath.close();
		//发送Socket消息给Agent
	
		try {
			Socket socket = new Socket(ip, 9100);
			String[] values = new String[7];
			values[0] = scIPAddr[0];
			values[1] = scIPAddr[1];
			values[2] = spaceRequirement+"";
			values[3] = zipSpace+"";
			values[4] = installPath;
			values[5] = jdkPath;
			values[6] = oldpath;
			
			Message msg = new Message(MsgType.setupTomcat, opID+"",values);
			//加密
			String datatemp = SerializeUtil.serialize(msg);  
			byte[] str = AESUtil.encrypt(datatemp,ip);
			
			System.out.println("Send message~~~~~~~~~~~~~~~~~~~"+str);
			
			//传输
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			oos.writeObject(str);
			
			System.out.println("Receive message~~~~~~~~~~~~~~~~~~~");
			
			//获得反馈信息
			ObjectInputStream ois = new ObjectInputStream(
					socket.getInputStream());
			byte[] rcvstr = (byte[])ois.readObject();
			System.out.println("received info");
			//解密
			byte[] str2 = AESUtil.decrypt(rcvstr,ip);
			String str1 = new String(str2,"iso-8859-1");
			if(str1.equals("NoSuchAlgorithmException")||str1.equals("NoSuchPaddingException")||str1.equals("InvalidKeyException")||str1.equals("BadPaddingException")||str1.equals("IllegalBlockSizeException")){
				System.out.println("JAVA security, error key");
			}else{
				msg = (Message) SerializeUtil.deserialize(str1);
				if (msg.getType().equals(MsgType.setupTomcat)) {
					String ret = (String) msg.getValues();
					//插入数据库
					DBOperation dbop = new DBOperation();
					dbop.updateOpStatus(opID, ret);
					dbop.close();
					if (ret.equals("success") || ret.equals("executing")) {
						return opID;
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
		return opID;
	}
	/**
	 * 安装Mysql
	 * @param uid
	 * @param ip
	 * @param scIPAddr
	 * @param installPath
	 * @return
	 * @throws SQLException 
	 */
	public int sendSetupMySqlMsg(String ip,String[] scIPAddr,int spaceRequirement,int zipSpace,String installPath,String pswd) throws SQLException{
		GetInstallPath getpath = new GetInstallPath();
		int opID = insertEvent(ip,"setup-MySql",scIPAddr[1]);
		String oldpath = getpath.getpath(ip, "Mysql");
		getpath.close();
		//发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9100);
			String[] values = new String[7];
			values[0] = scIPAddr[0];
			values[1] = scIPAddr[1];
			values[2] = spaceRequirement+"";
			values[3] = zipSpace+"";
			values[4] = installPath;
			values[5] = pswd;
			values[6] = oldpath;
			Message msg = new Message(MsgType.setupMySql, opID+"",values);
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
			msg = (Message)SerializeUtil.deserialize(str1); 
			if(str1.equals("NoSuchAlgorithmException")||str1.equals("NoSuchPaddingException")||str1.equals("InvalidKeyException")||str1.equals("BadPaddingException")||str1.equals("IllegalBlockSizeException")){
				System.out.println("JAVA security, error key");
			}else{
				if (msg.getType().equals(MsgType.setupMySql)) {
					String ret = (String) msg.getValues();
					//插入数据库
					DBOperation dbop = new DBOperation();
					dbop.updateOpStatus(opID, ret);
					dbop.close();
					if (ret.equals("success") || ret.equals("executing")) {
						return opID;
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
		return opID;
	}
	/**
	 * 安装 Mysql on Linux
	 * @param uid
	 * @param ip
	 * @param scIPAddr
	 * @param installPath
	 * @return
	 * @throws SQLException 
	 */
	public int sendSetupMySqlOnLinuxMsg(String ip,String[] scIPAddr,int spaceRequirement,String installPath) throws SQLException{
		int opID = insertEvent(ip,"setup-MySql",scIPAddr[1]);
		GetInstallPath getpath = new GetInstallPath();
		String oldpath = getpath.getpath(ip, "mysql");
		getpath.close();
		//发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9100);
			String[] values = new String[5];
			values[0] = scIPAddr[0];
			values[1] = scIPAddr[1];
			values[2] = spaceRequirement+"";
			values[3] = oldpath;
			values[4] = installPath;
			Message msg = new Message(MsgType.setupMySql, opID+"",values);
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
			msg = (Message)SerializeUtil.deserialize(str1); 
			if(str1.equals("NoSuchAlgorithmException")||str1.equals("NoSuchPaddingException")||str1.equals("InvalidKeyException")||str1.equals("BadPaddingException")||str1.equals("IllegalBlockSizeException")){
				System.out.println("JAVA security, error key");
			}else{
				if (msg.getType().equals(MsgType.setupMySql)) {
					String ret = (String) msg.getValues();
					//插入数据库
					DBOperation dbop = new DBOperation();
					dbop.updateOpStatus(opID, ret);
					dbop.close();
					if (ret.equals("success") || ret.equals("executing")) {
						return opID;
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
		return opID;
	}
	/**
	 * 安装Apache
	 * @param uid
	 * @param ip
	 * @param scIPAddr
	 * @param installPath
	 * @return
	 * @throws SQLException 
	 */
	public int sendSetupApacheMsg(String ip,String[] scIPAddr,int spaceRequirement,int zipSpace,String installPath,String emailAddress) throws SQLException{
		int opID = insertEvent(ip,"setup-Apache",scIPAddr[1]);
		GetInstallPath getpath = new GetInstallPath();
		String oldpath = getpath.getpath(ip, "Apache");
		getpath.close();
		//发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9100);
			String[] values = new String[7];
			values[0] = scIPAddr[0];
			values[1] = scIPAddr[1];
			values[2] = spaceRequirement+"";
			values[3] = zipSpace+"";
			values[4] = installPath;
			values[5] = emailAddress;
			values[6] = oldpath;
			Message msg = new Message(MsgType.setupApache, opID+"",values);
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
				if (msg.getType().equals(MsgType.setupApache)) {
					String ret = (String) msg.getValues();
					//插入数据库
					DBOperation dbop = new DBOperation();
					dbop.updateOpStatus(opID, ret);
					dbop.close();
					if (ret.equals("success") || ret.equals("executing")) {
						return opID;
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
		return opID;
	}
	/**
	 * 安装Nginx
	 * @param uid
	 * @param ip
	 * @param scIPAddr
	 * @param installPath
	 * @return
	 * @throws SQLException 
	 */
	public int sendSetupNginxMsg(String ip,String[] scIPAddr,int spaceRequirement,int zipSpace,String installPath) throws SQLException{
		int opID = insertEvent(ip,"setup-Nginx",scIPAddr[1]);
		GetInstallPath getpath = new GetInstallPath();
		String oldpath = getpath.getpath(ip, "Nginx");
		getpath.close();
		//发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9100);
			String[] values = new String[6];
			values[0] = scIPAddr[0];
			values[1] = scIPAddr[1];
			values[2] = spaceRequirement+"";
			values[3] = zipSpace+"";
			values[4] = installPath;
			values[5] = oldpath;
			Message msg = new Message(MsgType.setupNginx, opID+"",values);
			System.out.println("values:"+values[0]+","+values[1]+","+values[2]);
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

				if (msg.getType().equals(MsgType.setupNginx)) {
					String ret = (String) msg.getValues();
					//插入数据库
					DBOperation dbop = new DBOperation();
					dbop.updateOpStatus(opID, ret);
					dbop.close();
					if (ret.equals("success") || ret.equals("executing")) {
						return opID;
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
		return opID;
	}
	/**
	 * 安装ZendGuardLoader
	 * @param uid
	 * @param ip
	 * @param scIPAddr
	 * @param installPath
	 * @return
	 * @throws SQLException 
	 */
	public int sendSetupZendGuardLoaderMsg(String ip,String[] scIPAddr,int spaceRequirement,int zipSpace,String phpPath) throws SQLException{
		int opID = insertEvent(ip,"setup-ZendGuardLoader",scIPAddr[1]);
		GetInstallPath getpath = new GetInstallPath();
		String oldpath = getpath.getpath(ip, "ZendGuardLoader");
		getpath.close();
		//发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9100);
			String[] values = new String[6];
			values[0] = scIPAddr[0];
			values[1] = scIPAddr[1];
			values[2] = spaceRequirement+"";
			values[3] = zipSpace+"";
			values[4] = phpPath;
			values[5] = oldpath;
			Message msg = new Message(MsgType.setupZendGuardLoader, opID+"",values);
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
				if (msg.getType().equals(MsgType.setupZendGuardLoader)) {
					String ret = (String) msg.getValues();
					//插入数据库
					DBOperation dbop = new DBOperation();
					dbop.updateOpStatus(opID, ret);
					dbop.close();
					if (ret.equals("success") || ret.equals("executing")) {
						return opID;
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
		return opID;
	}
	/**
	 * 安装Python
	 * @param uid
	 * @param ip
	 * @param scIPAddr
	 * @param installPath
	 * @return
	 * @throws SQLException 
	 */
	public int sendSetupPythonMsg(String ip,String[] scIPAddr,int spaceRequirement,int zipSpace,String installpath) throws SQLException{
		int opID = insertEvent(ip,"setup-Python",scIPAddr[1]);
		GetInstallPath getpath = new GetInstallPath();
		String oldpath = getpath.getpath(ip, "python");
		getpath.close();
		//发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9100);
			String[] values = new String[6];
			values[0] = scIPAddr[0];
			values[1] = scIPAddr[1];
			values[2] = spaceRequirement+"";
			values[3] = zipSpace+"";
			values[4] = installpath;
			values[5] = oldpath;
			Message msg = new Message(MsgType.setupPython, opID+"",values);
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
				if (msg.getType().equals(MsgType.setupPython)) {
					String ret = (String) msg.getValues();
					//插入数据库
					DBOperation dbop = new DBOperation();
					dbop.updateOpStatus(opID, ret);
					dbop.close();
					if (ret.equals("success") || ret.equals("executing")) {
						return opID;
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
		
		return opID;
	}
	/**
	 * 安装Python Linux
	 * @param uid
	 * @param ip
	 * @param scIPAddr
	 * @param installPath
	 * @return
	 * @throws SQLException 
	 */
	public int sendSetupPythonMsgOnLinux(String ip,String[] scIPAddr,int spaceRequirement,String installPath) throws SQLException{
		int opID = insertEvent(ip,"setup-Python",scIPAddr[1]);
		GetInstallPath getpath = new GetInstallPath();
		String oldpath = getpath.getpath(ip, "Python");
		getpath.close();
		//发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9100);
			String[] values = new String[5];
			values[0] = scIPAddr[0];
			values[1] = scIPAddr[1];
			values[2] = spaceRequirement+"";
			values[3] = oldpath;
			values[4] = installPath;
			Message msg = new Message(MsgType.setupPython, opID+"",values);
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
				if (msg.getType().equals(MsgType.setupPython)) {
					String ret = (String) msg.getValues();
					//插入数据库
					DBOperation dbop = new DBOperation();
					dbop.updateOpStatus(opID, ret);
					dbop.close();
					if (ret.equals("success") || ret.equals("executing")) {
						return opID;
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
		
		return opID;
	}
	/**
	 * 安装Memcached
	 * @param uid
	 * @param ip
	 * @param scIPAddr
	 * @param installPath
	 * @return
	 * @throws SQLException 
	 */
	public int sendSetupMemcachedMsg(String ip,String[] scIPAddr,int spaceRequirement,int zipSpace,String installpath) throws SQLException{
		int opID = insertEvent(ip,"setup-Memcached",scIPAddr[1]);
		
		//获取旧版本的安装名称
		GetInstallPath getpath = new GetInstallPath();
		DBOperation dbop = new DBOperation();
		String oldpath = getpath.getpath(ip, "memcached");
		getpath.close();
//		String version = getpath.getVersion(ip, "memcached");
//		String[] sc = dbop .getRCAddrByIP("hp", ip,
//				"memcached", version);
//		String oldname = sc[1];
//		System.out.println("oldname  :  "+oldname);

		
		//发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9100);
			String[] values = new String[6];
			values[0] = scIPAddr[0];
			values[1] = scIPAddr[1];
			values[2] = spaceRequirement+"";
			values[3] = zipSpace+"";
			values[4] = installpath;
			values[5] = oldpath;
			
			Message msg = new Message(MsgType.setupMemcached, opID+"",values);
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
				if (msg.getType().equals(MsgType.setupMemcached)) {
					String ret = (String) msg.getValues();
					//插入数据库
					dbop.updateOpStatus(opID, ret);
					dbop.close();
					if (ret.equals("success") || ret.equals("executing")) {
						return opID;
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
		return opID;
	}
	/**
	 * 安装Memcached Linux
	 * @param uid
	 * @param ip
	 * @param scIPAddr
	 * @param installPath
	 * @return
	 * @throws SQLException 
	 */
	public int sendSetupMemcachedMsgOnLinux(String ip,String[] scIPAddr,int spaceRequirement,String installPath) throws SQLException{
		int opID = insertEvent(ip,"setup-Memcached",scIPAddr[1]);
		GetInstallPath getpath = new GetInstallPath();
		String oldpath = getpath.getpath(ip, "Memcached");
		getpath.close();
		//发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9100);
			String[] values = new String[45];
			values[0] = scIPAddr[0];
			values[1] = scIPAddr[1];
			values[2] = spaceRequirement+"";
			values[3] = oldpath;
			values[4] = installPath;
			Message msg = new Message(MsgType.setupMemcached, opID+"",values);
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
				if (msg.getType().equals(MsgType.setupMemcached)) {
					String ret = (String) msg.getValues();
					//插入数据库
					DBOperation dbop = new DBOperation();
					dbop.updateOpStatus(opID, ret);
					dbop.close();
					if (ret.equals("success") || ret.equals("executing")) {
						return opID;
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
		return opID;
	}
	/**
	 * 安装IISRewrite
	 * @param uid
	 * @param ip
	 * @param scIPAddr
	 * @param installPath
	 * @return
	 * @throws SQLException 
	 */
	public int sendSetupIISRewriteMsg(String ip,String[] scIPAddr,int spaceRequirement,int zipSpace,String installPath) throws SQLException{
		int opID = insertEvent(ip,"setup-IISRewrite",scIPAddr[1]);
		GetInstallPath getpath = new GetInstallPath();
		String oldpath = getpath.getpath(ip, "IISRewrite");
		getpath.close();
		//发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9100);
			String[] values = new String[6];
			values[0] = scIPAddr[0];
			values[1] = scIPAddr[1];
			values[2] = spaceRequirement+"";
			values[3] = zipSpace+"";
			values[4] = installPath;
			values[5] = oldpath;
			Message msg = new Message(MsgType.setupIISRewrite, opID+"",values);
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
				if (msg.getType().equals(MsgType.setupIISRewrite)) {
					String ret = (String) msg.getValues();
					//插入数据库
					DBOperation dbop = new DBOperation();
					dbop.updateOpStatus(opID, ret);
					dbop.close();
					if (ret.equals("success") || ret.equals("executing")) {
						return opID;
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
		return opID;
	}
	
	/**
	 * 安装FTP
	 * @param uid
	 * @param ip
	 * @param scIPAddr
	 * @param installPath
	 * @return
	 * @throws SQLException 
	 */
	public int sendSetupFTPMsg(String ip,String[] scIPAddr,int spaceRequirement,int zipSpace,String installPath) throws SQLException{
		int opID = insertEvent(ip,"setup-FTP",scIPAddr[1]);
		GetInstallPath getpath = new GetInstallPath();
		String oldpath = getpath.getpath(ip, "FTP");
		getpath.close();
		//发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9100);
			String[] values = new String[6];
			values[0] = scIPAddr[0];
			values[1] = scIPAddr[1];
			values[2] = spaceRequirement+"";
			values[3] = zipSpace+"";
			values[4] = installPath;
			values[5] = oldpath;
			Message msg = new Message(MsgType.setupFTP, opID+"",values);
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
				if (msg.getType().equals(MsgType.setupFTP)) {
					String ret = (String) msg.getValues();
					//插入数据库
					DBOperation dbop = new DBOperation();
					dbop.updateOpStatus(opID, ret);
					dbop.close();
					if (ret.equals("success") || ret.equals("executing")) {
						return opID;
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
		return opID;
	}
	/**
	 * 安装FTP Linux
	 * @param uid
	 * @param ip
	 * @param scIPAddr
	 * @param installPath
	 * @return
	 * @throws SQLException 
	 */
	public int sendSetupFTPMsgOnLinux(String ip,String[] scIPAddr,int spaceRequirement,String installPath) throws SQLException{
		int opID = insertEvent(ip,"setup-FTP",scIPAddr[1]);
		GetInstallPath getpath = new GetInstallPath();
		String oldpath = getpath.getpath(ip, "FTP");
		getpath.close();
		//发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9100);
			String[] values = new String[5];
			values[0] = scIPAddr[0];
			values[1] = scIPAddr[1];
			values[2] = spaceRequirement+"";
			values[3] = oldpath;
			values[4] = installPath;
			Message msg = new Message(MsgType.setupFTP, opID+"",values);
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
				if (msg.getType().equals(MsgType.setupFTP)) {
					String ret = (String) msg.getValues();
					//插入数据库
					DBOperation dbop = new DBOperation();
					dbop.updateOpStatus(opID, ret);
					dbop.close();
					if (ret.equals("success") || ret.equals("executing")) {
						return opID;
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
		return opID;
	}
	/**
	 * 安装SQLServer2008R2
	 * @param uid
	 * @param ip
	 * @param scIPAddr
	 * @param installPath
	 * @param rootPswd
	 * @param hostName
	 * @param userName
	 * @return
	 * @throws SQLException 
	 */
	public int sendSetupSQLServer2008R2Msg(String ip,String[] scIPAddr,int spaceRequirement,int zipSpace,String installPath,String rootPswd,String hostName,String userName) throws SQLException{
		int opID = insertEvent(ip,"setup-SQLServer2008R2",scIPAddr[1]);
		GetInstallPath getpath = new GetInstallPath();
		String oldpath = getpath.getpath(ip, "SQLServer2008R2");
		getpath.close();
		//发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9100);
			String[] values = new String[9];
			values[0] = scIPAddr[0];
			values[1] = scIPAddr[1];
			values[2] = spaceRequirement+"";
			values[3] = zipSpace+"";
			values[4] = installPath;
			values[5] = rootPswd;
			values[6] = hostName;
			values[7] = userName;
			values[8] = oldpath;
			Message msg = new Message(MsgType.setupSQLServer2008R2, opID+"",values);
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
			msg = (Message)SerializeUtil.deserialize(str1); 
				if (msg.getType().equals(MsgType.setupSQLServer2008R2)) {
					String ret = (String) msg.getValues();
					//插入数据库
					DBOperation dbop = new DBOperation();
					dbop.updateOpStatus(opID, ret);
					dbop.close();
					if (ret.equals("success") || ret.equals("executing")) {
						return opID;
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
		return opID;
	}
	
	
	/**
	 * 界面安装SQLServer2008R2
	 * @param uid
	 * @param ip
	 * @param scIPAddr
	 * @param installPath
	 * @param rootPswd
	 * @param hostName
	 * @param userName
	 * @return
	 * @throws SQLException 
	 */
	public int sendSetupSQLServer2008R2InterfaceMsg(String ip,String[] scIPAddr,int spaceRequirement,int zipSpace,String installPath,String rootPswd,String hostName,String userName) throws SQLException{
		int opID = insertEvent(ip,"setup-SQLServer2008R2",scIPAddr[1]);
		GetInstallPath getpath = new GetInstallPath();
		String oldpath = getpath.getpath(ip, "SQLServer2008R2");
		getpath.close();
		//发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9100);
			String[] values = new String[9];
			values[0] = scIPAddr[0];
			values[1] = scIPAddr[1];
			values[2] = spaceRequirement+"";
			values[3] = zipSpace+"";
			values[4] = installPath;
			values[5] = rootPswd;
			values[6] = hostName;
			values[7] = userName;
			values[8] = oldpath;
			Message msg = new Message(MsgType.setupSQLServer2008R2Interface, opID+"",values);
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
			msg = (Message)SerializeUtil.deserialize(str1); 
				if (msg.getType().equals(MsgType.setupSQLServer2008R2Interface)) {
					String ret = (String) msg.getValues();
					//插入数据库
					DBOperation dbop = new DBOperation();
					dbop.updateOpStatus(opID, ret);
					dbop.close();
					if (ret.equals("success") || ret.equals("executing")) {
						return opID;
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
		return opID;
	}
	/**
	 * 安装Oracle11g
	 * @param uid
	 * @param ip
	 * @param scIPAddr
	 * @param hostname
	 * @param inventorypath
	 * @param installPath
	 * @param oraclehome
	 * @param rootPswd
	 * @return
	 * @throws SQLException 
	 */
	public int sendSetupOracle11gMsg(String ip,String[] scIPAddr,int spaceRequirement,int zipSpace,String hostname,String inventorypath,
			String oraclebase,String oraclehome, String rootPswd) throws SQLException{
		int opID = insertEvent(ip,"setup-Oracle11g",scIPAddr[1]);
		GetInstallPath getpath = new GetInstallPath();
		String oldpath = getpath.getpath(ip, "Oracle11g");
		getpath.close();
		//发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9100);
			String[] values = new String[10];
			values[0] = scIPAddr[0];
			values[1] = scIPAddr[1];
			values[2] = spaceRequirement+"";
			values[3] = zipSpace+"";
			values[4] = oraclebase;
			values[5] = inventorypath;
			values[6] = hostname;
			values[7] = oraclehome;
			values[8] = rootPswd;
			values[9] = oldpath;
			Message msg = new Message(MsgType.setupOracle11g, opID+"",values);
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
				if (msg.getType().equals(MsgType.setupOracle11g)) {
					String ret = (String) msg.getValues();
					//插入数据库
					DBOperation dbop = new DBOperation();
					dbop.updateOpStatus(opID, ret);
					dbop.close();
					if (ret.equals("success") || ret.equals("executing")) {
						return opID;
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
		return opID;
	}
	
	
	public int sendSetupOracle11gLinuxMsg(String ip,String[] scIPAddr,int spaceRequirement,String username, String oraclebase, String oracleinventory, String oraclehome, String oracle_sid, String rootPswd, String oradata) throws SQLException{
		int opID = insertEvent(ip,"setup-Oracle11g",scIPAddr[1]);
		GetInstallPath getpath = new GetInstallPath();
		String oldpath = getpath.getpath(ip, "Oracle11g");
		getpath.close();
		//发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9100);
			String[] values = new String[11];
			values[0] = scIPAddr[0];
			values[1] = scIPAddr[1];
			values[2] = spaceRequirement+"";
			values[3] = username;
			values[4] = oraclebase;
			values[5] = oracleinventory;
			values[6] = oraclehome;
			values[7] = oracle_sid;
			values[8] = rootPswd;
			values[9] = oradata;
			values[10] = oldpath;
			Message msg = new Message(MsgType.setupOracle11g, opID+"",values);
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
				if (msg.getType().equals(MsgType.setupOracle11g)) {
					String ret = (String) msg.getValues();
					//插入数据库
					DBOperation dbop = new DBOperation();
					dbop.updateOpStatus(opID, ret);
					dbop.close();
					if (ret.equals("success") || ret.equals("executing")) {
						return opID;
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
		return opID;
	}
	
	/**
	 * 安装Oracle11g
	 * @param uid
	 * @param ip
	 * @param scIPAddr
	 * @param hostname
	 * @param inventorypath
	 * @param installPath
	 * @param oraclehome
	 * @param rootPswd
	 * @return
	 * @throws SQLException 
	 */
	public int sendSetupOracle11gInterfaceMsg(String ip,String[] scIPAddr,int spaceRequirement,int zipSpace,String oraclebase, String oraclehome,
			String inventorypath,String databasename,String rootPswd) throws SQLException{
		int opID = insertEvent(ip,"setup-Oracle11g",scIPAddr[1]);
		GetInstallPath getpath = new GetInstallPath();
		String oldpath = getpath.getpath(ip, "Oracle11g");
		getpath.close();
		//发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9100);
			String[] values = new String[10];
			values[0] = scIPAddr[0];
			values[1] = scIPAddr[1];
			values[2] = spaceRequirement+"";
			values[3] = zipSpace+"";
			values[4] = oraclebase;
			values[5] = oraclehome;
			values[6] = inventorypath;
			values[7] = databasename;
			values[8] = rootPswd;
			values[9] = oldpath;
			Message msg = new Message(MsgType.setupOracle11gInterface, opID+"",values);
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
				if (msg.getType().equals(MsgType.setupOracle11gInterface)) {
					String ret = (String) msg.getValues();
					//插入数据库
					DBOperation dbop = new DBOperation();
					dbop.updateOpStatus(opID, ret);
					dbop.close();
					if (ret.equals("success") || ret.equals("executing")) {
						return opID;
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
		return opID;
	}
	
	/**
	 * 安装360
	 * @param uid
	 * @param ip
	 * @param scIPAddr
	 * @param installPath
	 * @return
	 * @throws SQLException 
	 */
	public int sendSetup360Msg(String ip,String[] scIPAddr,int spaceRequirement,int zipSpace,String installPath) throws SQLException{
		int opID = insertEvent(ip,"setup-360",scIPAddr[1]);
		GetInstallPath getpath = new GetInstallPath();
		String oldpath = getpath.getpath(ip, "360");
		getpath.close();
		//发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9100);
			String[] values = new String[6];
			values[0] = scIPAddr[0];
			values[1] = scIPAddr[1];
			values[2] = spaceRequirement+"";
			values[3] = zipSpace+"";
			values[4] = installPath;
			values[5] = oldpath;
			Message msg = new Message(MsgType.setup360, opID+"",values);
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
				if (msg.getType().equals(MsgType.setup360)) {
					String ret = (String) msg.getValues();
					//插入数据库
					DBOperation dbop = new DBOperation();
					dbop.updateOpStatus(opID, ret);
					dbop.close();
					if (ret.equals("success") || ret.equals("executing")) {
						return opID;
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
		return opID;
	}
	
}
