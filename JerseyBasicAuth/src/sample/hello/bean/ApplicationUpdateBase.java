package sample.hello.bean;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;

import edu.xidian.enc.AESUtil;
import edu.xidian.enc.SerializeUtil;
import edu.xidian.message.Message;
import edu.xidian.message.MsgType;

import sample.DBOP.DBForSCS;
import sample.DBOP.DBOperation;

public class ApplicationUpdateBase {
	public ApplicationUpdateBase() throws SQLException{
		DBForSCS scs = new DBForSCS();
		scs.Delete("setup");
		scs.close();
	}
	/**
	 * 将更新（升级）软件事件插入到数据库中
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
	public int sendUpdateMySqlMsg( String ip, String[] scIPAddr,int spaceRequirement,int zipSpace,
			String uninstallPath,String updatePath, String rootPswd) {
		// TODO Auto-generated method stub
		int opID = insertEvent(ip,"update-MySql",scIPAddr[1]);
		//发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9300);
			String[] values = new String[7];
			values[0] = scIPAddr[0];
			values[1] = scIPAddr[1];
			values[2] = spaceRequirement+"";
			values[3] = zipSpace+"";
			values[4] = updatePath;
			values[5] = uninstallPath;
			values[6] = rootPswd;
			Message msg = new Message(MsgType.updateMySql, opID+"",values);
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
				if (msg.getType().equals(MsgType.updateMySql)) {
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

	public int sendUpdateMySqlOnLinuxMsg( String ip,
			String[] scIPAddr,int spaceRequirement,String updatepath,String uninstallPath) {
		// TODO Auto-generated method stub
		int opID = insertEvent(ip,"update-MySql",scIPAddr[1]);
		//发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9300);
			String[] values = new String[5];
			values[0] = scIPAddr[0];
			values[1] = scIPAddr[1];
			values[2] = spaceRequirement+"";
			values[3] = uninstallPath;
			values[4] = updatepath;
			Message msg = new Message(MsgType.updateMySql, opID+"",values);
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
				if (msg.getType().equals(MsgType.updateMySql)) {
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

	public int sendUpdateTomcatMsg( String ip, String[] scIPAddr,int spaceRequirement,int zipSpace,
			String updatePath, String jdkPath,String uninstallPath) {
		// TODO Auto-generated method stub
		int opID = insertEvent(ip,"update-Tomcat",scIPAddr[1]);
		//发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9300);
			String[] values = new String[7];
			values[0] = scIPAddr[0];
			values[1] = scIPAddr[1];
			values[2] = spaceRequirement+"";
			values[3] = zipSpace+"";
			values[4] = updatePath;
			values[5] = jdkPath;
			values[6] = uninstallPath;
			Message msg = new Message(MsgType.updateTomcat, opID+"",values);
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
				if (msg.getType().equals(MsgType.updateTomcat)) {
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

	
	public int sendUpdateTomcatLinuxMsg( String ip, String[] scIPAddr,int spaceRequirement,
			 String unistallPath,String updatePath,String jdkPath) {
		// TODO Auto-generated method stub
		int opID = insertEvent(ip,"update-Tomcat",scIPAddr[1]);
		//发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9300);
			String[] values = new String[6];
			values[0] = scIPAddr[0];
			values[1] = scIPAddr[1];
			values[2] = spaceRequirement+"";
			values[3] = unistallPath;
			values[4] = updatePath;
			values[5] = jdkPath;
			Message msg = new Message(MsgType.updateTomcat, opID+"",values);
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
				if (msg.getType().equals(MsgType.updateTomcat)) {
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

	public int sendUpdateApacheMsg( String ip, String[] scIPAddr,int spaceRequirement,int zipSpace,
			String updatePath,String emailAddress,String uninstallPath) {
		// TODO Auto-generated method stub
		int opID = insertEvent(ip,"update-Apache",scIPAddr[1]);
		//发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9300);
			String[] values = new String[7];
			values[0] = scIPAddr[0];
			values[1] = scIPAddr[1];
			values[2] = spaceRequirement+"";
			values[3] = zipSpace+"";
			values[4] = updatePath;
			values[5] = emailAddress;
			values[6] = uninstallPath;
			Message msg = new Message(MsgType.updateApache, opID+"",values);
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
				if (msg.getType().equals(MsgType.updateApache)) {
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
	
	public int sendUpdateApacheLinuxMsg( String ip, String[] scIPAddr,int spaceRequirement,
			String uninstallPath,String updatePath,String emailAddress) {
		// TODO Auto-generated method stub
		int opID = insertEvent(ip,"update-Apache",scIPAddr[1]);
		//发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9300);
			String[] values = new String[6];
			values[0] = scIPAddr[0];
			values[1] = scIPAddr[1];
			values[2] = spaceRequirement+"";
			values[3] = uninstallPath;
			values[4] = updatePath;
			values[5] = emailAddress;
			Message msg = new Message(MsgType.updateApache, opID+"",values);
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
				if (msg.getType().equals(MsgType.updateApache)) {
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

	public int sendUpdateNginxMsg( String ip, String[] scIPAddr,int spaceRequirement,int zipSpace,
			String updatePath,String unistallPath) {
		// TODO Auto-generated method stub
		int opID = insertEvent(ip,"update-Nginx",scIPAddr[1]);
		//发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9300);
			String[] values = new String[6];
			values[0] = scIPAddr[0];
			values[1] = scIPAddr[1];
			values[2] = spaceRequirement+"";
			values[3]= zipSpace+"";
			values[4] = updatePath;
			values[5] = unistallPath;
			Message msg = new Message(MsgType.updateNginx, opID+"",values);
			System.out.println("values:"+values[0]+","+values[1]+","+values[2]+","+values[3]);
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

				if (msg.getType().equals(MsgType.updateNginx)) {
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
	
	
	public int sendUpdateNginxLinuxMsg( String ip, String[] scIPAddr,int spaceRequirement,
			String updatePath,String uninstallPath) {
		// TODO Auto-generated method stub
		int opID = insertEvent(ip,"update-Nginx",scIPAddr[1]);
		//发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9300);
			String[] values = new String[5];
			values[0] = scIPAddr[0];
			values[1] = scIPAddr[1];
			values[2] = spaceRequirement+"";
			values[3] = uninstallPath;
			values[4] = updatePath;
			Message msg = new Message(MsgType.updateNginx, opID+"",values);
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
				if (msg.getType().equals(MsgType.updateNginx)) {
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
			
			e.printStackTrace();
		}
		return opID;
	}

	public int sendUpdateZendGuardLoaderMsg( String ip,
			String[] scIPAddr,int spaceRequirement, int zipSpace,String phpPath,String uninstallPath) {
		// TODO Auto-generated method stub
		int opID = insertEvent(ip,"update-ZendGuardLoader",scIPAddr[1]);
		//发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9300);
			String[] values = new String[6];
			values[0] = scIPAddr[0];
			values[1] = scIPAddr[1];
			values[2] = spaceRequirement+"";
			values[3] = zipSpace+"";
			values[4] = phpPath;
			values[5] = uninstallPath;
			Message msg = new Message(MsgType.updateZendGuardLoader, opID+"",values);
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
				if (msg.getType().equals(MsgType.updateZendGuardLoader)) {
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

	public int sendUpdateZendGuardLoaderMsgOnLinux( String ip,
			String[] scIPAddr,int spaceRequirement, String updatePath, String phpPath) {
		// TODO Auto-generated method stub
		int opID = insertEvent(ip,"update-ZendGuardLoader",scIPAddr[1]);
		//发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9300);
			String[] values = new String[5];
			values[0] = scIPAddr[0];
			values[1] = scIPAddr[1];
			values[2] = spaceRequirement+"";
			values[3] = phpPath;
			values[4] = updatePath;
			Message msg = new Message(MsgType.updateZendGuardLoader, opID+"",values);
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
				if (msg.getType().equals(MsgType.updateZendGuardLoader)) {
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

	public int sendUpdatePythonMsg( String ip, String[] scIPAddr,int spaceRequirement,int zipSpace,String updatePath,String uninstallPath) {
		// TODO Auto-generated method stub
		int opID = insertEvent(ip,"update-Python",scIPAddr[1]);
		//发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9300);
			String[] values = new String[6];
			values[0] = scIPAddr[0];
			values[1] = scIPAddr[1];
			values[2] = spaceRequirement+"";
			values[3] = zipSpace+"";
			values[4] = updatePath;
			values[5] = uninstallPath;
			Message msg = new Message(MsgType.updatePython, opID+"",values);
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
				if (msg.getType().equals(MsgType.updatePython)) {
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

	public int sendUpdatePythonMsgOnLinux( String ip,
			String[] scIPAddr,int spaceRequirement,String uninstallPath, String updatePath) {
		// TODO Auto-generated method stub
		int opID = insertEvent(ip,"update-Python",scIPAddr[1]);
		//发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9300);
			String[] values = new String[5];
			values[0] = scIPAddr[0];
			values[1] = scIPAddr[1];
			values[2] = spaceRequirement+"";
			values[3] = uninstallPath;
			values[4] = updatePath;
			Message msg = new Message(MsgType.updatePython, opID+"",values);
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
				if (msg.getType().equals(MsgType.updatePython)) {
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

	public int sendUpdateMemcachedMsg( String ip, String[] scIPAddr,int spaceRequirement,int zipSpace,String updatePath,String uninstallPath) {
		// TODO Auto-generated method stub
		int opID = insertEvent(ip,"update-Memcached",scIPAddr[1]);
		//发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9300);
			String[] values = new String[6];
			values[0] = scIPAddr[0];
			values[1] = scIPAddr[1];
			values[2] = spaceRequirement+"";
			values[3] = zipSpace+"";
			values[4] = updatePath;
			values[5] = uninstallPath;
		
			
			Message msg = new Message(MsgType.updateMemcached, opID+"",values);
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
				if (msg.getType().equals(MsgType.updateMemcached)) {
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

	
	public int sendUpdateMemcachedLinuxMsg( String ip, String[] scIPAddr,int spaceRequirement,String uninstallPath,String path) {
		// TODO Auto-generated method stub
		int opID = insertEvent(ip,"update-Memcached",scIPAddr[1]);
		//发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9300);
			String[] values = new String[5];
			values[0] = scIPAddr[0];
			values[1] = scIPAddr[1];
			values[2] = spaceRequirement+"";
			values[3] = uninstallPath;
			values[4] = path;
			
			Message msg = new Message(MsgType.updateMemcached, opID+"",values);
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
				if (msg.getType().equals(MsgType.updateMemcached)) {
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

	public int sendUpdateIISRewriteMsg( String ip,
			String[] scIPAddr, int spaceRequirement,int zipSpace,String updatePath,String uninstallPath) {
		// TODO Auto-generated method stub
		int opID = insertEvent(ip,"update-IISRewrite",scIPAddr[1]);
		//发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9300);
			String[] values = new String[6];
			values[0] = scIPAddr[0];
			values[1] = scIPAddr[1];
			values[2] = spaceRequirement+"";
			values[3] = zipSpace+"";
			values[4] = updatePath;
			values[5] = uninstallPath;
			Message msg = new Message(MsgType.updateIISRewrite, opID+"",values);
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
				if (msg.getType().equals(MsgType.updateIISRewrite)) {
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

	public int sendUpdateFTPMsg( String ip, String[] scIPAddr,int spaceRequirement,int zipSpace,
			String updatePath,String uninstallPath) {
		// TODO Auto-generated method stub
		int opID = insertEvent(ip,"update-FTP",scIPAddr[1]);
		//发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9300);
			String[] values = new String[6];
			values[0] = scIPAddr[0];
			values[1] = scIPAddr[1];
			values[2] = spaceRequirement+"";
			values[3] = zipSpace+"";
			values[4] = updatePath;
			values[5] = uninstallPath;
			Message msg = new Message(MsgType.updateFTP, opID+"",values);
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
				if (msg.getType().equals(MsgType.updateFTP)) {
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

	public int sendUpdateFTPMsgOnLinux( String ip, String[] scIPAddr,int spaceRequirement,String updatePath,String uninstallPath) {
		// TODO Auto-generated method stub
		int opID = insertEvent(ip,"update-FTP",scIPAddr[1]);
		//发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9300);
			String[] values = new String[5];
			values[0] = scIPAddr[0];
			values[1] = scIPAddr[1];
			values[2] = spaceRequirement+"";
			values[3] = uninstallPath;
			values[4] = updatePath;
			Message msg = new Message(MsgType.updateFTP, opID+"",values);
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
				if (msg.getType().equals(MsgType.updateFTP)) {
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
