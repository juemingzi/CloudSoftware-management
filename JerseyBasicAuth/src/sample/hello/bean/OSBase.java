package sample.hello.bean;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.sql.SQLException;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import sample.DBOP.DBOperation;
import edu.xidian.enc.AESUtil;
import edu.xidian.enc.MD5Util;
import edu.xidian.enc.SerializeUtil;
import edu.xidian.message.Message;
import edu.xidian.message.MsgType;

public class OSBase {
	/**
	 * 将基础环境配置事件插入到数据库中
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
	 * 更新基础环境配置的状态，操作的是opinfo表
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
	 * 修改密码
	 * @param uid
	 * @param ip
	 * @param cUserName
	 * @param cPasswd
	 * @return
	 * @throws SQLException 
	 */
	public String sendChangePasswdMsg(String ip,String cUserName,String cPasswd){
		int opID = insertEvent(ip,"changePasswd");
		//发送Socket消息给Agent
		try {
			System.out.println("ip*************************"+ip);
			Socket socket = new Socket(ip, 9000);
			String[] values=new String[2];
			values[0]=cUserName;
			values[1]=cPasswd;
			Message msg = new Message(MsgType.changePasswd, opID+"",values);
			
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
				if (msg.getType().equals(MsgType.changePasswd)) {
					String ret = (String)msg.getValues();
					if(ret.equals("0x0000000")){
						//在数据库里更新该op的状态
						updateOpStatus(opID,"0x0000000");
						return "0x0000000";
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
		updateOpStatus(opID,"0x0000001");
		return "0x0000001";
	}
	/**
	 * 查看系统服务
	 * @param uid
	 * @param ip
	 */
	/*public void sendGetSysServiceMsg(String ip){
		try {
			Socket socket = new Socket(ip, 9000);
			String[] values=new String[2];

			Message msg = new Message(MsgType.getSysService, opID+"",null);
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			oos.writeObject(msg);
			ObjectInputStream ois = new ObjectInputStream(
					socket.getInputStream());
			msg = (Message) ois.readObject();
			if (msg.getType().equals(MsgType.getSysService)) {
				String ret = (String)msg.getValues();
				if(ret.equals("success")){
					
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
	}*/
	/**
	 * 启动/停止 系统服务
	 * @param uid
	 * @param ip
	 * @return
	 * @throws SQLException 
	 */
	public String sendchgSysServiceStateMsg(String ip,String serviceName,String operation){
		int opID = -1;
		try {
			Socket socket = new Socket(ip, 9000);
			String[] values=new String[2];
			values[0] = serviceName;
			values[1] = operation;
			
			Message msg;
			if(operation.equals("start")){
				opID = insertEvent(ip,"startService");
				msg = new Message(MsgType.startService, opID+"",values[0]);
			}else{
				opID = insertEvent(ip,"stopService");
				msg = new Message(MsgType.stopService, opID+"",values[0]);
			}

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
				if (msg.getType().equals(MsgType.startService)) {
					String ret = (String) msg.getValues();
					if (ret.equals("0x0000100")) {
						updateOpStatus(opID, "0x0000100");
						return "0x0000100";
					} else {
						updateOpStatus(opID, "0x0000101");
						return "0x0000101";
					}
				} else if (msg.getType().equals(MsgType.stopService)) {
					String ret = (String) msg.getValues();
					if (ret.equals("0x0000200")) {
						updateOpStatus(opID, "0x0000200");
						return "0x0000200";
					} else {
						updateOpStatus(opID, "0x0000201");
						return "0x0000201";
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
		return "";
	}
	/**
	 * 查看系统日志
	 * @param uid
	 * @param ip
	 * @param logType
	 * @return
	 */
	public JSONObject sendViewSysLogMsg(String ip,String logType){
		JSONObject o = new JSONObject();
		int opID = insertEvent(ip,"viewErrLog");
		//发送Socket消息给Agent
		/*try {
			Socket socket = new Socket(ip, 9000);
			String[] values=new String[1];
			values[0]=logType;
			Message msg = new Message(MsgType.viewErrLog, opID+"",values);
			//加密
			String datatemp = SerializeUtil.serialize(msg);  
            String str = MD5Util.convertMD5(datatemp);
            //传输
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			oos.writeObject(str);
			//获得反馈信息
			ObjectInputStream ois = new ObjectInputStream(
					socket.getInputStream());
			str = (String)ois.readObject();
			//解密
			String str2 = MD5Util.convertMD5(str);
			msg = (Message)SerializeUtil.deserialize(str2); 
			if (msg.getType().equals(MsgType.viewErrLog)) {
				//此处应该返回一个日志文件 字符串
//				String ret = (msg.getValues())[0];
//				if(ret.equals("success")){
//					return true;
//				}
//				
				o.put(logType, "This is a windows log.");
			}
			socket.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		try {
			o.put(logType, "This is a windows log.");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return o;
	}
	
	
	
	
	
	/**
	 * 修改主机IP on Linux
	 * @param uid
	 * @param ip
	 * @param mac
	 * @param mask
	 * @param dns
	 * @return
	 */
//	public String sendChangeIPOnLinuxMsg(String ip,String deviceName,String mask,String changeToIP){
//		int opID = insertEvent(ip,"changeIP");//发送Socket消息给Agent
//		try {
//			Socket socket = new Socket(ip, 9000);
//			String[] values = new String[3];
//			values[0]=deviceName;
//			values[1]=mask;
//			values[2]=changeToIP;
//			Message msg = new Message(MsgType.changeIP, opID+"",values);
//			//加密
//			String datatemp = SerializeUtil.serialize(msg);  
//			byte[] str = AESUtil.encrypt(datatemp,ip);
//			//传输
//			ObjectOutputStream oos = new ObjectOutputStream(
//					socket.getOutputStream());
//			oos.writeObject(str);
//			//获得反馈信息
//			socket.setSoTimeout(3000);
//			try {
//				ObjectInputStream ois = new ObjectInputStream(
//						socket.getInputStream());
//				byte[] rcvstr = (byte[])ois.readObject();
//				//解密
//				byte[] str2 = AESUtil.decrypt(rcvstr,ip);
//				String str1 = new String(str2,"iso-8859-1");
//				if(str1.equals("NoSuchAlgorithmException")||str1.equals("NoSuchPaddingException")||str1.equals("InvalidKeyException")||str1.equals("BadPaddingException")||str1.equals("IllegalBlockSizeException")){
//					System.out.println("JAVA security, error key");
//				}else{
//					msg = (Message) SerializeUtil.deserialize(str1);
//					if (msg.getType().equals(MsgType.changeIP)) {
//						//
//						String ret = (String) msg.getValues();
//						if (ret.equals("0x0000500")) {
//							updateOpStatus(opID, "0x0000500");
//							return "0x0000500";
//						} else {
//							System.out.println("modify ip addr failed!");
//							updateOpStatus(opID, "0x0000501");
//							return "0x0000501";
//						}
//					}
//				}
//			} catch (SocketTimeoutException ste) {
//				// 修改数据库，将原来的ip改成changeToIP
//				System.out.println("modify windows ip addr success!");
//				DBOperation dbop = new DBOperation();
//				dbop.updateHostIP(ip, changeToIP);
//				dbop.close();
//				updateOpStatus(opID,"0x0000500");
//				return "0x0000500";
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
//		updateOpStatus(opID,"0x0000501");
//		return "0x0000501";
//	}
	/**
	 * 修改主机IP on Linux
	 * @param uid
	 * @param ip
	 * @param mac
	 * @param mask
	 * @param dns
	 * @return
	 */
	public String sendChangeIPOnLinuxMsg(String ip,String deviceName,String mask,String changeToIP){
		int opID = insertEvent(ip,"changeIP");//发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9000);
			String[] values = new String[3];
			values[0]=deviceName;
			values[1]=mask;
			values[2]=changeToIP;
			Message msg = new Message(MsgType.changeIP, opID+"",values);
			//加密
			String datatemp = SerializeUtil.serialize(msg);  
			byte[] str = AESUtil.encrypt(datatemp,ip);
			//传输
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			oos.writeObject(str);
			//获得反馈信息
			socket.setSoTimeout(3000);
			try {
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
					if (msg.getType().equals(MsgType.changeIP)) {
						String ret = (String) msg.getValues();
						if (ret.equals("0x0000500")) {
							// 修改数据库，将原来的ip改成changeToIP
							System.out.println("modify ip addr success!");
							DBOperation dbop = new DBOperation();
							dbop.updateHostIP(ip, changeToIP);
							dbop.close();
							updateOpStatus(opID,"0x0000500");
							return  "0x0000500";
						}
					}
				}
			} catch (SocketTimeoutException ste) {
				ste.printStackTrace();
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
		updateOpStatus(opID,"0x0000501");
		return "0x0000501";
	}
	
	/**
	 * windows修改主机affi IP
	 * @param uid
	 * @param ip
	 * @param mac
	 * @param mask
	 * @param gateway
	 * @param dns
	 * @param affiIP
	 * @param affiMask
	 * @return
	 * @throws SQLException 
	 */
	public String sendChangeAffiIPMsg( String ip, String mac, String AffiIP,String NewAffiIP,
			String mask){
		int opID = insertEvent(ip,"changeAffiIP");
		String s = null;
		// 发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9000);
			String[] values = new String[4];
			values[0] = mac;
			values[1] = AffiIP;
			values[2] = NewAffiIP;
			values[3] = mask;
			Message msg = new Message(MsgType.changeAffiIP, opID+"", values);
//			System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"+msg.getType().toString());
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
				if (msg.getType().equals(MsgType.changeAffiIP)) {
					String ret = (String) msg.getValues();
//					if (ret.equals("0x0000600")) {
						System.out.println("windows change affi ip :" + ret);
						updateOpStatus(opID, ret);
						return ret;
//					}
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
		updateOpStatus(opID,"0x0000601");
		return "0x0000601";
	}
	/*public boolean sendChangeAffiIPMsg( String ip, String mac,String changeToIp,
			String mask,String gateway, String[] dns, String[] affiIP,
			String[] affiMask,String[] affiGateway) {
		// 发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9000);
			Object[] values = new Object[7];
//			values[0] = mac;
			values[0] = changeToIp;
			values[1] = mask;
			values[2] = gateway;
			
			values[3] = dns;
			values[4] = affiIP;
			values[5] = affiMask;
			values[6] = affiGateway;
			Message msg = new Message(MsgType.changeAffiIP, opID+"", values);
			//加密
			String datatemp = SerializeUtil.serialize(msg);  
            String str = MD5Util.convertMD5(datatemp);
            //传输
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			oos.writeObject(str);
			//获得反馈信息  -------  -*-
			socket.setSoTimeout(3000);
			try {
				ObjectInputStream ois = new ObjectInputStream(
						socket.getInputStream());
				str = (String) ois.readObject();
				// 解密
				String str2 = MD5Util.convertMD5(str);
				msg = (Message) SerializeUtil.deserialize(str2);
				if (msg.getType().equals(MsgType.changeAffiIP)) {
					// 此处应该返回执行结果失败
					String ret = (String)msg.getValues();
					if(ret.equals("success")){
						return true;
					}else{
					System.out.println("modify ip addr failed!");
						return false;
					}
				}
			} catch (SocketTimeoutException ste) {
				// 修改数据库，将原来的ip改成changeToIP
				System.out.println("modify ip addr success!");
				DBOperation dbop = new DBOperation();
				dbop.updateHostIP(ip, changeToIp);
				return true;
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
		return false;
	}*/
	/**
	 * windows change ip
	 * @param uid
	 * @param ip
	 * @param mac
	 * @param changeToIp
	 * @param mask
	 * @param gateway
	 * @param dns
	 * @return
	 */
	public String sendChangeIPMsg( String ip, String mac,String changeToIp,
			String mask,String gateway, String[] dns) {
		int opID = insertEvent(ip,"changeIP");
		// 发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9000);
			Object[] values = new Object[4];
			values[0] = changeToIp;
			values[1] = mask;
			values[2] = gateway;
			values[3] = dns;
			Message msg = new Message(MsgType.changeIP, opID+"", values);
			//加密
			String datatemp = SerializeUtil.serialize(msg);  
			byte[] str = AESUtil.encrypt(datatemp,ip);
			//传输
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			oos.writeObject(str);
			
			//获得反馈信息  -------  -*-
			socket.setSoTimeout(3000);
			try {
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
					if (msg.getType().equals(MsgType.changeIP)) {
						//
						String ret = (String) msg.getValues();
						if (ret.equals("0x0000500")) {
							updateOpStatus(opID, "0x0000500");
							return "0x0000500";
						} else {
							System.out.println("modify ip addr failed!");
							updateOpStatus(opID, "0x0000501");
							return "0x0000501";
						}
					}
				}
			} catch (SocketTimeoutException ste) {
				// 修改数据库，将原来的ip改成changeToIP
				System.out.println("modify windows ip addr success!");
				DBOperation dbop = new DBOperation();
				dbop.updateHostIP(ip, changeToIp);
				dbop.close();
				updateOpStatus(opID,"0x0000500");
				return "0x0000500";
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
		updateOpStatus(opID,"0x0000501");
		return "0x0000501";
	}
	/**
	 * 修改主机附属IP on Linux
	 * @param uid
	 * @param ip
	 * @param deviceName
	 * @param mask
	 * @param changeToIp
	 * @return
	 * @throws SQLException 
	 */
	public String sendChangeAffiIPOnLinuxMsg( String ip, String mac,String AffiIP,String NewAffiIP) {
		int opID = insertEvent(ip,"changeAffiIP");
		// 发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9000);
			String[] values = new String[3];
			values[0] = mac;
			values[1] = AffiIP;
			values[2] = NewAffiIP;
			Message msg = new Message(MsgType.changeAffiIP, opID+"", values);
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
				if (msg.getType().equals(MsgType.changeAffiIP)) {
					String ret = (String) msg.getValues();
//					if (ret.equals("0x0000600")) {
						updateOpStatus(opID, ret);
						return ret;
//					}
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
		updateOpStatus(opID,"0x0000601");
		return "0x0000601";
	}
	/**
	 * 磁盘格式化
	 * @param uid
	 * @param ip
	 * @return
	 */
	public String sendDiskFormatMsg( String ip) {
		int opID = insertEvent(ip,"diskFormat");
		// 发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9000);
			Message msg = new Message(MsgType.diskFormat, opID+"", null);
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
				if (msg.getType().equals(MsgType.diskFormat)) {
					String ret = (String) msg.getValues();
					if (ret.equals("0x0000300")) {
						updateOpStatus(opID, "0x0000300");
						return "0x0000300";
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
		updateOpStatus(opID,"0x0000301");
		return "0x0000301";
	}
	/**
	 * 修改安全规则
	 * @param uid
	 * @param ip
	 * @param policyName
	 * @param protocol
	 * @param port
	 * @param addSecIP
	 * @return
	 */
	public String sendChangeSecRuleMsg(String ip,String policyName,String protocol, String port,String addSecIP){
		int opID = insertEvent(ip,"changeSecRule");
		try {
			Socket socket = new Socket(ip, 9000);
			String[] values = new String[4];
			values[0] = policyName;
			values[1] = protocol;
			values[2] = port;
			values[3] = addSecIP;
			Message msg = new Message(MsgType.changeSecRule, opID+"", values);
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
				if (msg.getType().equals(MsgType.changeSecRule)) {
					String ret = (String) msg.getValues();
					if (ret.equals("0x0000400")) {
						updateOpStatus(opID, "0x0000400");
						return "0x0000400";
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
		updateOpStatus(opID,"0x0000401");
		return "0x0000401";
	}
	/**
	 * 修改安全规则 on linux
	 * @param uid
	 * @param ip
	 * @param policyName
	 * @param protocol
	 * @param port
	 * @param addSecIP
	 * @return
	 */
	public String sendChangeSecRuleOnLinuxMsg(String ip,String protocol,String sourceIP, String port){
		int opID = insertEvent(ip,"changeSecRule");
		try {
			Socket socket = new Socket(ip, 9000);
			String[] values = new String[4];
			values[0] = protocol;
			values[1] = sourceIP;
			values[2] = port;
			Message msg = new Message(MsgType.changeSecRule, opID+"", values);
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
				if (msg.getType().equals(MsgType.changeSecRule)) {
					String ret = (String) msg.getValues();
					if (ret.equals("0x0000400")) {
						updateOpStatus(opID, "0x0000400");
						return "0x0000400";
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
		updateOpStatus(opID,"0x0000401");
		return "0x0000401";
	}
	
	
	public String sendChangeUlimitOnLinuxMsg(String ip,String ulimit){
		int opID = insertEvent(ip,"changeUlimit");
		try {
			Socket socket = new Socket(ip, 9000);
			Message msg = new Message(MsgType.changeUlimit, opID+"", ulimit);
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
				if (msg.getType().equals(MsgType.changeUlimit)) {
					String ret = (String) msg.getValues();
					if (ret.equals("0x0000700")) {
						updateOpStatus(opID, "0x0000700");
						return "0x0000700";
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
		updateOpStatus(opID,"0x0000701");
		return "0x0000701";
	}
	/**
	 * 查看某个系统服务状态
	 * @param ip
	 * @return
	 */
	public String sendGetASystemServiceStateMsgOnLinux(String ip,String serviceName){
		int opID = insertEvent(ip,"getOneServiceState");
		//发送Socket消息给Agent
		try {
			System.out.println("ip*************************"+ip);
			Socket socket = new Socket(ip, 9000);
//			String[] values = new String[2];
//			values[0] = serviceName;
			Message msg = new Message(MsgType.getOneServiceState, opID+"",serviceName);
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
				if (msg.getType().equals(MsgType.getOneServiceState)) {
					String ret = (String) msg.getValues();
					updateOpStatus(opID,"0x0900200");
					return ret;
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
		updateOpStatus(opID,"0x0900201");
		return "0x0900201";
	}
	/**
	 * 查看某个系统服务状态
	 * @param ip
	 * @return
	 */
	public String sendGetASystemServiceStateMsg(String ip,String serviceName){
		int opID = insertEvent(ip,"getOneServiceState");
		//发送Socket消息给Agent
		try {
			System.out.println("ip*************************"+ip);
			Socket socket = new Socket(ip, 9000);
//			String[] values = new String[2];
//			values[0] = serviceName;
			Message msg = new Message(MsgType.getOneServiceState, opID+"",serviceName);
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
				if (msg.getType().equals(MsgType.getOneServiceState)) {
					String ret = (String) msg.getValues();
					updateOpStatus(opID,"0x0900200");
					return ret;
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
		updateOpStatus(opID,"0x0900201");
		return "0x0900201";
	}
	

	/**
	 * 查看系统所有服务
	 * @param ip
	 * @return
	 */
	public String sendGetSystemServiceStateMsg(String ip){//获取全部的服务
		int opID = insertEvent(ip,"getServiceState");
		//发送Socket消息给Agent
		try {
			System.out.println("ip*************************"+ip);
			Socket socket = new Socket(ip, 9000);
			Message msg = new Message(MsgType.getServiceState, opID+"",null);
			
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
//				msg = (Message)SerializeUtil.deserialize(str1); 
//				if (msg.getType().equals(MsgType.changePasswd)) {
//					String ret = (String)msg.getValues();
//					if(ret.equals("0x0900000")){
//						//在数据库里更新该op的状态
//						updateOpStatus(opID,"0x0900000");
//						return "0x0900000";
//					}
//				}
				msg = (Message) SerializeUtil.deserialize(str1);
				if (msg.getType().equals(MsgType.getServiceState)) {
					String ret = (String) msg.getValues();
					updateOpStatus(opID,"0x0900200");
					return ret;
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
		updateOpStatus(opID,"0x0900201");
		return "0x0900201";
	}
	/**
	 * 获取系统安全规则
	 * @param ip
	 * @return
	 */
	public String sendGetSystemSecRuleMsg(String ip){
		int opID = insertEvent(ip,"getSecRule");
		//发送Socket消息给Agent
		try {
			System.out.println("ip*************************"+ip);
			Socket socket = new Socket(ip, 9000);
			Message msg = new Message(MsgType.getSecRule, opID+"",null);
			
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
//				msg = (Message)SerializeUtil.deserialize(str1); 
//				if (msg.getType().equals(MsgType.changePasswd)) {
//					String ret = (String)msg.getValues();
//					if(ret.equals("0x0900000")){
//						//在数据库里更新该op的状态
//						updateOpStatus(opID,"0x0900000");
//						return ret;
//					}
//				}
				msg = (Message) SerializeUtil.deserialize(str1);
				if (msg.getType().equals(MsgType.getSecRule)) {
					String ret = (String) msg.getValues();
					updateOpStatus(opID,"0x0900100");
					return ret;
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
		updateOpStatus(opID,"0x0900101");
		return "0x0900101";
	}
	/**
	 * 获取系统IP详细信息
	 * @param ip
	 * @return
	 */
	public String sendGetSystemIPMsg(String ip){
		int opID = insertEvent(ip,"getIP");
		//发送Socket消息给Agent
		try {
			System.out.println("ip*************************"+ip);
			Socket socket = new Socket(ip, 9000);
			Message msg = new Message(MsgType.getIP, opID+"",null);
			
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
				if (msg.getType().equals(MsgType.getIP)) {
					String ret = (String) msg.getValues();
					updateOpStatus(opID,"0x0900300");
					return ret;
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
		updateOpStatus(opID,"0x0900301");
		return "0x0900301";
	}
	
	
	/**
	 * 获取系统附属IP详细信息
	 * @param ip
	 * @return
	 */
	public String sendGetSystemAffiIPMsg(String ip){
		int opID = insertEvent(ip,"getAffiIP");
		//发送Socket消息给Agent
		try {
			System.out.println("ip*************************"+ip);
			Socket socket = new Socket(ip, 9000);
			Message msg = new Message(MsgType.getAffiIP, opID+"",null);
			
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
				if (msg.getType().equals(MsgType.getAffiIP)) {
					String ret = (String) msg.getValues();
					updateOpStatus(opID,"0x0900400");
					return ret;
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
		updateOpStatus(opID,"0x0900401");
		return "0x0900401";
	}
	/**
	 * @param ip
	 * @return
	 */
	public String sendGetSystemUlimitMsg(String ip) {
		// TODO Auto-generated method stub
		int opID = insertEvent(ip,"getUlimit");
		//发送Socket消息给Agent
		try {
			System.out.println("ip*************************"+ip);
			Socket socket = new Socket(ip, 9000);
			Message msg = new Message(MsgType.getUlimit, opID+"",null);
			
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
				if (msg.getType().equals(MsgType.getUlimit)) {
					String ret = (String) msg.getValues();
					updateOpStatus(opID,"0x0900500");
					return ret;
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
		updateOpStatus(opID,"0x0900501");
		return "0x0900501";
	}
	
	
	
	
	
	
	
	
	
	/**
	 * 
	 * @param uid
	 * @param ip
	 * @param deviceName
	 * @param mask
	 * @param changeToIp
	 * @return
	 * @throws SQLException 
	 */
	public String sendAddAffiIPMsg(String ip, String mac, String AffiIP,String mask) {
		int opID = insertEvent(ip,"addAffiIP");
		// 发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9000);
			String[] values = new String[3];
			values[0] = mac;
			values[1] = AffiIP;
			values[2] = mask;
			Message msg = new Message(MsgType.addAffiIP, opID+"", values);
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
				if (msg.getType().equals(MsgType.addAffiIP)) {
					String ret = (String) msg.getValues();
//					if (ret.equals("0x0001000")) {
						updateOpStatus(opID, ret);
						return ret;
//					}
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
		updateOpStatus(opID,"0x0001001");
		return "0x0001001";
	}
	/**
	 * 
	 * @param uid
	 * @param ip
	 * @param deviceName
	 * @param mask
	 * @param changeToIp
	 * @return
	 * @throws SQLException 
	 */
	public String sendDeleteAffiIPMsg( String ip, String mac,String AffiIP) {
		int opID = insertEvent(ip,"deleteAffiIP");
		// 发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9000);
			String[] values = new String[2];
			values[0] = mac;
			values[1] = AffiIP;
			Message msg = new Message(MsgType.deleteAffiIP, opID+"", values);
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
				if (msg.getType().equals(MsgType.deleteAffiIP)) {
					String ret = (String) msg.getValues();
//					if (ret.equals("0x0001100")) {
						updateOpStatus(opID, ret);
						return ret;
//					}
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
		updateOpStatus(opID,"0x0001101");
		return "0x0001101";
	}
	/**
	 * 修改主机附属IP on Linux
	 * @param uid
	 * @param ip
	 * @param deviceName
	 * @param mask
	 * @param changeToIp
	 * @return
	 * @throws SQLException 
	 */
	public String sendSearchAffiIPMsg( String ip, String mac) {
		int opID = insertEvent(ip,"searchAffiIP");
		// 发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9000);
			String[] values = new String[1];
			values[0] = mac;
			Message msg = new Message(MsgType.searchAffiIP, opID+"", values);
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
				if (msg.getType().equals(MsgType.searchAffiIP)) {
					String ret = (String) msg.getValues();
					if (ret != null) {
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
		updateOpStatus(opID,"0x0001201");
		return "0x0001201";
	}
	/**
	 * 修改主机附属IP on Linux
	 * @param uid
	 * @param ip
	 * @param deviceName
	 * @param mask
	 * @param changeToIp
	 * @return
	 * @throws SQLException 
	 */
	public String sendAddAffiIPOnLinuxMsg( String ip, String mac,String AffiIP,String mask) {
		int opID = insertEvent(ip,"addAffiIP");
		// 发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9000);
			String[] values = new String[3];
			values[0] = mac;
			values[1] = AffiIP;
			values[2] = mask;
			Message msg = new Message(MsgType.addAffiIP, opID+"", values);
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
				if (msg.getType().equals(MsgType.addAffiIP)) {
					String ret = (String) msg.getValues();
//					if (ret.equals("0x0001000")) {
						updateOpStatus(opID, ret);
						return ret;
//					}
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
		updateOpStatus(opID,"0x0001001");
		return "0x0001001";
	}
	/**
	 * 修改主机附属IP on Linux
	 * @param uid
	 * @param ip
	 * @param deviceName
	 * @param mask
	 * @param changeToIp
	 * @return
	 * @throws SQLException 
	 */
	public String sendDeleteAffiIPOnLinuxMsg( String ip, String mac,String AffiIP) {
		int opID = insertEvent(ip,"deleteAffiIP");
		String s = null;
		// 发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9000);
			String[] values = new String[2];
			values[0] = mac;
			values[1] = AffiIP;
			Message msg = new Message(MsgType.deleteAffiIP, opID+"", values);
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
				if (msg.getType().equals(MsgType.deleteAffiIP)) {
					String ret = (String) msg.getValues();
					s = ret;
//					if (ret.equals("0x0001100")) {
						updateOpStatus(opID, ret);
						return ret;
//					}
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
		updateOpStatus(opID,s);
		return s;
	}
	/**
	 * 修改主机附属IP on Linux
	 * @param uid
	 * @param ip
	 * @param deviceName
	 * @param mask
	 * @param changeToIp
	 * @return
	 * @throws SQLException 
	 */
	public String sendSearchAffiIPOnLinuxMsg( String ip, String mac) {
		int opID = insertEvent(ip,"searchAffiIP");
		// 发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9000);
			String[] values = new String[1];
			values[0] = mac;
			Message msg = new Message(MsgType.searchAffiIP, opID+"", values);
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
				if (msg.getType().equals(MsgType.searchAffiIP)) {
					String ret = (String) msg.getValues();
					if (!ret.equals("0x0001201")) {
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
		updateOpStatus(opID,"0x0001201");
		return "0x0001201";
	}
	
	
	/**
	 * 获取key
	 * @param uid
	 * @param ip
	 * @return
	 * @throws SQLException 
	 */
	public String sendReadKeyMsg( String ip) {
		int opID = insertEvent(ip,"readkey");
		// 发送Socket消息给Agent
		try {
			Socket socket = new Socket(ip, 9000);
			Message msg = new Message(MsgType.readkey, opID+"", null);
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
				if (msg.getType().equals(MsgType.readkey)) {
					String ret = (String) msg.getValues();
					if (!ret.equals("")) {
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
		updateOpStatus(opID,"0x0001301");
		return "0x0001301";
	}
	
	
	
}
