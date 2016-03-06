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

import sample.DBOP.DBOperation;

public class ApplicationUninstallBase {
	/**
	 * ��ж������¼����뵽���ݿ���
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
	
	public int sendUninstallMySqlMsg(String ip,String path) {
		// TODO Auto-generated method stub
		int opID = insertEvent(ip,"uninstall-MySql");
		//����Socket��Ϣ��Agent
		try {
			Socket socket = new Socket(ip, 9600);
			String[] values = new String[1];
			values[0] = path;
			Message msg = new Message(MsgType.uninstallMySql, opID+"",values);
			//����
			String datatemp = SerializeUtil.serialize(msg);  
			byte[] str = AESUtil.encrypt(datatemp,ip);
			//����
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			oos.writeObject(str);
			//��÷�����Ϣ
			ObjectInputStream ois = new ObjectInputStream(
					socket.getInputStream());
			byte[] rcvstr = (byte[])ois.readObject();
			//����
			byte[] str2 = AESUtil.decrypt(rcvstr,ip);
			String str1 = new String(str2,"iso-8859-1");
			if(str1.equals("NoSuchAlgorithmException")||str1.equals("NoSuchPaddingException")||str1.equals("InvalidKeyException")||str1.equals("BadPaddingException")||str1.equals("IllegalBlockSizeException")){
				System.out.println("JAVA security, error key");
			}else{
				msg = (Message) SerializeUtil.deserialize(str1);
				if (msg.getType().equals(MsgType.uninstallMySql)) {
					String ret = (String) msg.getValues();
					//�������ݿ�
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

	public int sendUninstallMySqlOnLinuxMsg( String ip,String path) {
		// TODO Auto-generated method stub
		int opID = insertEvent(ip,"uninstall-MySql");
		//����Socket��Ϣ��Agent
		try {
			Socket socket = new Socket(ip, 9600);
			String[] values = new String[1];
			values[0] = path;
			Message msg = new Message(MsgType.uninstallMySql, opID+"",values);
			//����
			String datatemp = SerializeUtil.serialize(msg);  
			byte[] str = AESUtil.encrypt(datatemp,ip);
			//����
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			oos.writeObject(str);
			//��÷�����Ϣ
			ObjectInputStream ois = new ObjectInputStream(
					socket.getInputStream());
			byte[] rcvstr = (byte[])ois.readObject();
			//����
			byte[] str2 = AESUtil.decrypt(rcvstr,ip);
			String str1 = new String(str2,"iso-8859-1");
			if(str1.equals("NoSuchAlgorithmException")||str1.equals("NoSuchPaddingException")||str1.equals("InvalidKeyException")||str1.equals("BadPaddingException")||str1.equals("IllegalBlockSizeException")){
				System.out.println("JAVA security, error key");
			}else{
				msg = (Message) SerializeUtil.deserialize(str1);
				if (msg.getType().equals(MsgType.uninstallMySql)) {
					String ret = (String) msg.getValues();
					//�������ݿ�
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

	public int sendUninstallTomcatMsg( String ip,String path) {
		// TODO Auto-generated method stub
		int opID = insertEvent(ip,"uninstall-Tomcat");
		String[] values = new String[1];
		values[0] = path;
		
		System.out.println("values[0] is path  &&&&&"+values[0]);
		
		//����Socket��Ϣ��Agent
		try {
			Socket socket = new Socket(ip, 9600);
			
			Message msg = new Message(MsgType.uninstallTomcat, opID+"",values);
			//����
			String datatemp = SerializeUtil.serialize(msg);  
			byte[] str = AESUtil.encrypt(datatemp,ip);
			//����
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			oos.writeObject(str);
			//��÷�����Ϣ
			ObjectInputStream ois = new ObjectInputStream(
					socket.getInputStream());
			byte[] rcvstr = (byte[])ois.readObject();
			//����
			byte[] str2 = AESUtil.decrypt(rcvstr,ip);
			String str1 = new String(str2,"iso-8859-1");
			if(str1.equals("NoSuchAlgorithmException")||str1.equals("NoSuchPaddingException")||str1.equals("InvalidKeyException")||str1.equals("BadPaddingException")||str1.equals("IllegalBlockSizeException")){
				System.out.println("JAVA security, error key");
			}else{
				msg = (Message) SerializeUtil.deserialize(str1);
				if (msg.getType().equals(MsgType.uninstallTomcat)) {
					String ret = (String) msg.getValues();
					//�������ݿ�
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
	
	public int sendUninstallTomcatLinuxMsg( String ip,String path) {
		// TODO Auto-generated method stub
		int opID = insertEvent(ip,"uninstall-Tomcat");
		//����Socket��Ϣ��Agent
		try {
			Socket socket = new Socket(ip, 9600);
			String[] values = new String[1];
			values[0] = path;
			Message msg = new Message(MsgType.uninstallTomcat, opID+"",values);
			//����
			String datatemp = SerializeUtil.serialize(msg);  
			byte[] str = AESUtil.encrypt(datatemp,ip);
			//����
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			oos.writeObject(str);
			//��÷�����Ϣ
			ObjectInputStream ois = new ObjectInputStream(
					socket.getInputStream());
			byte[] rcvstr = (byte[])ois.readObject();
			//����
			byte[] str2 = AESUtil.decrypt(rcvstr,ip);
			String str1 = new String(str2,"iso-8859-1");
			if(str1.equals("NoSuchAlgorithmException")||str1.equals("NoSuchPaddingException")||str1.equals("InvalidKeyException")||str1.equals("BadPaddingException")||str1.equals("IllegalBlockSizeException")){
				System.out.println("JAVA security, error key");
			}else{
				msg = (Message) SerializeUtil.deserialize(str1);
				if (msg.getType().equals(MsgType.uninstallTomcat)) {
					String ret = (String) msg.getValues();
					//�������ݿ�
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


	public int sendUninstallApacheMsg( String ip,String path) {
		// TODO Auto-generated method stub
		int opID = insertEvent(ip,"uninstall-Apache");
		String[] values = new String[1];
		values[0] = path;
		//����Socket��Ϣ��Agent
		try {
			Socket socket = new Socket(ip, 9600);
			
			Message msg = new Message(MsgType.uninstallApache, opID+"",values);
			//����
			String datatemp = SerializeUtil.serialize(msg);  
			byte[] str = AESUtil.encrypt(datatemp,ip);
			//����
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			oos.writeObject(str);
			//��÷�����Ϣ
			ObjectInputStream ois = new ObjectInputStream(
					socket.getInputStream());
			byte[] rcvstr = (byte[])ois.readObject();
			//����
			byte[] str2 = AESUtil.decrypt(rcvstr,ip);
			String str1 = new String(str2,"iso-8859-1");
			if(str1.equals("NoSuchAlgorithmException")||str1.equals("NoSuchPaddingException")||str1.equals("InvalidKeyException")||str1.equals("BadPaddingException")||str1.equals("IllegalBlockSizeException")){
				System.out.println("JAVA security, error key");
			}else{
				msg = (Message) SerializeUtil.deserialize(str1);
				if (msg.getType().equals(MsgType.uninstallApache)) {
					String ret = (String) msg.getValues();
					//�������ݿ�
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

	
	public int sendUninstallApacheLinuxMsg( String ip,String path) {
		// TODO Auto-generated method stub
		int opID = insertEvent(ip,"uninstall-Apache");
		//����Socket��Ϣ��Agent
		try {
			Socket socket = new Socket(ip, 9600);
			String[] values = new String[1];
			values[0] = path;
			Message msg = new Message(MsgType.uninstallApache, opID+"",values);
			//����
			String datatemp = SerializeUtil.serialize(msg);  
			byte[] str = AESUtil.encrypt(datatemp,ip);
			//����
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			oos.writeObject(str);
			//��÷�����Ϣ
			ObjectInputStream ois = new ObjectInputStream(
					socket.getInputStream());
			byte[] rcvstr = (byte[])ois.readObject();
			//����
			byte[] str2 = AESUtil.decrypt(rcvstr,ip);
			String str1 = new String(str2,"iso-8859-1");
			if(str1.equals("NoSuchAlgorithmException")||str1.equals("NoSuchPaddingException")||str1.equals("InvalidKeyException")||str1.equals("BadPaddingException")||str1.equals("IllegalBlockSizeException")){
				System.out.println("JAVA security, error key");
			}else{
				msg = (Message) SerializeUtil.deserialize(str1);
				if (msg.getType().equals(MsgType.uninstallApache)) {
					String ret = (String) msg.getValues();
					//�������ݿ�
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
	
	public int sendUninstallNginxMsg( String ip,String Path) {
		// TODO Auto-generated method stub
		int opID = insertEvent(ip,"uninstall-Nginx");
		//����Socket��Ϣ��Agent
		try {
			Socket socket = new Socket(ip, 9600);
			String[] values = new String[1];
			values[0]=Path;
			Message msg = new Message(MsgType.uninstallNginx, opID+"",values);
			//����
			String datatemp = SerializeUtil.serialize(msg);  
			byte[] str = AESUtil.encrypt(datatemp,ip);
			//����
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			oos.writeObject(str);
			//��÷�����Ϣ
			ObjectInputStream ois = new ObjectInputStream(
					socket.getInputStream());
			byte[] rcvstr = (byte[])ois.readObject();
			//����
			byte[] str2 = AESUtil.decrypt(rcvstr,ip);
			String str1 = new String(str2,"iso-8859-1");
			if(str1.equals("NoSuchAlgorithmException")||str1.equals("NoSuchPaddingException")||str1.equals("InvalidKeyException")||str1.equals("BadPaddingException")||str1.equals("IllegalBlockSizeException")){
				System.out.println("JAVA security, error key");
			}else{
				msg = (Message) SerializeUtil.deserialize(str1);
				if (msg.getType().equals(MsgType.uninstallNginx)) {
					String ret = (String) msg.getValues();
					//�������ݿ�
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

	public int sendUninstallZendGuardLoaderMsg( String ip,String Path) {
		// TODO Auto-generated method stub
		int opID = insertEvent(ip,"uninstall-ZendGuardLoader");
		//����Socket��Ϣ��Agent
		try {
			Socket socket = new Socket(ip, 9600);
			String[] values = new String[1];
			values[0]=Path;
			Message msg = new Message(MsgType.uninstallZendGuardLoader, opID+"",values);
			//����
			String datatemp = SerializeUtil.serialize(msg);  
			byte[] str = AESUtil.encrypt(datatemp,ip);
			//����
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			oos.writeObject(str);
			//��÷�����Ϣ
			ObjectInputStream ois = new ObjectInputStream(
					socket.getInputStream());
			byte[] rcvstr = (byte[])ois.readObject();
			//����
			byte[] str2 = AESUtil.decrypt(rcvstr,ip);
			String str1 = new String(str2,"iso-8859-1");
			if(str1.equals("NoSuchAlgorithmException")||str1.equals("NoSuchPaddingException")||str1.equals("InvalidKeyException")||str1.equals("BadPaddingException")||str1.equals("IllegalBlockSizeException")){
				System.out.println("JAVA security, error key");
			}else{
				msg = (Message) SerializeUtil.deserialize(str1);
				if (msg.getType().equals(MsgType.uninstallZendGuardLoader)) {
					String ret = (String) msg.getValues();
					//�������ݿ�
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

	public int sendUninstallZendGuardLoaderMsgOnLinux( String ip,String Path) {
		// TODO Auto-generated method stub
		int opID = insertEvent(ip,"uninstall-ZendGuardLoader");
		//����Socket��Ϣ��Agent
		try {
			Socket socket = new Socket(ip, 9600);
			String[] values = new String[1];
			values[0]=Path;
			Message msg = new Message(MsgType.uninstallZendGuardLoader, opID+"",values);
			//����
			String datatemp = SerializeUtil.serialize(msg);  
			byte[] str = AESUtil.encrypt(datatemp,ip);
			//����
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			oos.writeObject(str);
			//��÷�����Ϣ
			ObjectInputStream ois = new ObjectInputStream(
					socket.getInputStream());
			byte[] rcvstr = (byte[])ois.readObject();
			//����
			byte[] str2 = AESUtil.decrypt(rcvstr,ip);
			String str1 = new String(str2,"iso-8859-1");
			if(str1.equals("NoSuchAlgorithmException")||str1.equals("NoSuchPaddingException")||str1.equals("InvalidKeyException")||str1.equals("BadPaddingException")||str1.equals("IllegalBlockSizeException")){
				System.out.println("JAVA security, error key");
			}else{
				msg = (Message) SerializeUtil.deserialize(str1);
				if (msg.getType().equals(MsgType.uninstallZendGuardLoader)) {
					String ret = (String) msg.getValues();
					//�������ݿ�
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

	public int sendUninstallPythonMsg( String ip,String path) {
		// TODO Auto-generated method stub
		int opID = insertEvent(ip,"uninstall-Python");
		String[] values = new String[1];
		values[0] = path;
		//����Socket��Ϣ��Agent
		try {
			Socket socket = new Socket(ip, 9600);
			
			Message msg = new Message(MsgType.uninstallPython, opID+"",values);
			//����
			String datatemp = SerializeUtil.serialize(msg);  
			byte[] str = AESUtil.encrypt(datatemp,ip);
			//����
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			oos.writeObject(str);
			//��÷�����Ϣ
			ObjectInputStream ois = new ObjectInputStream(
					socket.getInputStream());
			byte[] rcvstr = (byte[])ois.readObject();
			//����
			byte[] str2 = AESUtil.decrypt(rcvstr,ip);
			String str1 = new String(str2,"iso-8859-1");
			if(str1.equals("NoSuchAlgorithmException")||str1.equals("NoSuchPaddingException")||str1.equals("InvalidKeyException")||str1.equals("BadPaddingException")||str1.equals("IllegalBlockSizeException")){
				System.out.println("JAVA security, error key");
			}else{
				msg = (Message) SerializeUtil.deserialize(str1);
				if (msg.getType().equals(MsgType.uninstallPython)) {
					String ret = (String) msg.getValues();
					//�������ݿ�
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

	public int sendUninstallPythonLinuxMsg( String ip,String path) {
		// TODO Auto-generated method stub
		int opID = insertEvent(ip,"uninstall-Python");
		//����Socket��Ϣ��Agent
		try {
			Socket socket = new Socket(ip, 9600);
			String[] values = new String[1];
			values[0]=path;
			Message msg = new Message(MsgType.uninstallPython, opID+"",values);
			//����
			String datatemp = SerializeUtil.serialize(msg);  
			byte[] str = AESUtil.encrypt(datatemp,ip);
			//����
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			oos.writeObject(str);
			//��÷�����Ϣ
			ObjectInputStream ois = new ObjectInputStream(
					socket.getInputStream());
			byte[] rcvstr = (byte[])ois.readObject();
			//����
			byte[] str2 = AESUtil.decrypt(rcvstr,ip);
			String str1 = new String(str2,"iso-8859-1");
			if(str1.equals("NoSuchAlgorithmException")||str1.equals("NoSuchPaddingException")||str1.equals("InvalidKeyException")||str1.equals("BadPaddingException")||str1.equals("IllegalBlockSizeException")){
				System.out.println("JAVA security, error key");
			}else{
				msg = (Message) SerializeUtil.deserialize(str1);
				if (msg.getType().equals(MsgType.uninstallPython)) {
					String ret = (String) msg.getValues();
					//�������ݿ�
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

	public int sendUninstallMemcachedLinuxMsg( String ip,String path) {
		// TODO Auto-generated method stub
		int opID = insertEvent(ip,"uninstall-Memcached");
		//����Socket��Ϣ��Agent
		try {
			Socket socket = new Socket(ip, 9600);
			String[] values = new String[1];
			values[0]=path;
			Message msg = new Message(MsgType.uninstallMemcached, opID+"",values);
			//����
			String datatemp = SerializeUtil.serialize(msg);  
			byte[] str = AESUtil.encrypt(datatemp,ip);
			//����
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			oos.writeObject(str);
			//��÷�����Ϣ
			ObjectInputStream ois = new ObjectInputStream(
					socket.getInputStream());
			byte[] rcvstr = (byte[])ois.readObject();
			//����
			byte[] str2 = AESUtil.decrypt(rcvstr,ip);
			String str1 = new String(str2,"iso-8859-1");
			if(str1.equals("NoSuchAlgorithmException")||str1.equals("NoSuchPaddingException")||str1.equals("InvalidKeyException")||str1.equals("BadPaddingException")||str1.equals("IllegalBlockSizeException")){
				System.out.println("JAVA security, error key");
			}else{
				msg = (Message) SerializeUtil.deserialize(str1);
				if (msg.getType().equals(MsgType.uninstallMemcached)) {
					String ret = (String) msg.getValues();
					//�������ݿ�
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


	
	public int sendUninstallMemcachedMsg( String ip,String path) {
		// TODO Auto-generated method stub
		int opID = insertEvent(ip,"uninstall-Memcached");
		//����Socket��Ϣ��Agent
		try {
			Socket socket = new Socket(ip, 9600);
			String[] values = new String[1];
			values[0]=path;
			
			Message msg = new Message(MsgType.uninstallMemcached, opID+"",values);
			//����
			String datatemp = SerializeUtil.serialize(msg);  
			byte[] str = AESUtil.encrypt(datatemp,ip);
			//����
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			oos.writeObject(str);
			//��÷�����Ϣ
			ObjectInputStream ois = new ObjectInputStream(
					socket.getInputStream());
			byte[] rcvstr = (byte[])ois.readObject();
			//����
			byte[] str2 = AESUtil.decrypt(rcvstr,ip);
			String str1 = new String(str2,"iso-8859-1");
			if(str1.equals("NoSuchAlgorithmException")||str1.equals("NoSuchPaddingException")||str1.equals("InvalidKeyException")||str1.equals("BadPaddingException")||str1.equals("IllegalBlockSizeException")){
				System.out.println("JAVA security, error key");
			}else{
				msg = (Message) SerializeUtil.deserialize(str1);
				if (msg.getType().equals(MsgType.uninstallMemcached)) {
					String ret = (String) msg.getValues();
					//�������ݿ�
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


	public int sendUninstallIISRewriteMsg( String ip,String path) {
		// TODO Auto-generated method stub
		int opID = insertEvent(ip,"uninstall-IISRewrite");
		
		//����Socket��Ϣ��Agent
		try {
			Socket socket = new Socket(ip, 9600);
			String[] values = new String[1];
			values[0] = path;
			Message msg = new Message(MsgType.uninstallIISRewrite, opID+"",values);
			//����
			String datatemp = SerializeUtil.serialize(msg);  
			byte[] str = AESUtil.encrypt(datatemp,ip);
			//����
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			oos.writeObject(str);
			//��÷�����Ϣ
			ObjectInputStream ois = new ObjectInputStream(
					socket.getInputStream());
			byte[] rcvstr = (byte[])ois.readObject();
			//����
			byte[] str2 = AESUtil.decrypt(rcvstr,ip);
			String str1 = new String(str2,"iso-8859-1");
			if(str1.equals("NoSuchAlgorithmException")||str1.equals("NoSuchPaddingException")||str1.equals("InvalidKeyException")||str1.equals("BadPaddingException")||str1.equals("IllegalBlockSizeException")){
				System.out.println("JAVA security, error key");
			}else{
				msg = (Message) SerializeUtil.deserialize(str1);
				if (msg.getType().equals(MsgType.uninstallIISRewrite)) {
					String ret = (String) msg.getValues();
					//�������ݿ�
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

	
	public int sendUninstallFTPMsg( String ip,String path) {
		// TODO Auto-generated method stub
		int opID = insertEvent(ip,"uninstall-FTP");
		//����Socket��Ϣ��Agent
		try {
			Socket socket = new Socket(ip, 9600);
			String[] values = new String[1];
			values[0] = path;
			Message msg = new Message(MsgType.uninstallFTP, opID+"",values);
			//����
			String datatemp = SerializeUtil.serialize(msg);  
			byte[] str = AESUtil.encrypt(datatemp,ip);
			//����
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			oos.writeObject(str);
			//��÷�����Ϣ
			ObjectInputStream ois = new ObjectInputStream(
					socket.getInputStream());
			byte[] rcvstr = (byte[])ois.readObject();
			//����
			byte[] str2 = AESUtil.decrypt(rcvstr,ip);
			String str1 = new String(str2,"iso-8859-1");
			if(str1.equals("NoSuchAlgorithmException")||str1.equals("NoSuchPaddingException")||str1.equals("InvalidKeyException")||str1.equals("BadPaddingException")||str1.equals("IllegalBlockSizeException")){
				System.out.println("JAVA security, error key");
			}else{
				msg = (Message) SerializeUtil.deserialize(str1);
				if (msg.getType().equals(MsgType.uninstallFTP)) {
					String ret = (String) msg.getValues();
					//�������ݿ�
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

	public int sendUninstallFTPMsgOnLinux( String ip,String path) {
		// TODO Auto-generated method stub
		int opID = insertEvent(ip,"uninstall-FTP");
		//����Socket��Ϣ��Agent
		try {
			Socket socket = new Socket(ip, 9600);
			String[] values = new String[1];
			values[0] = path;
			Message msg = new Message(MsgType.uninstallFTP, opID+"",values);
			//����
			String datatemp = SerializeUtil.serialize(msg);  
			byte[] str = AESUtil.encrypt(datatemp,ip);
			//����
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			oos.writeObject(str);
			//��÷�����Ϣ
			ObjectInputStream ois = new ObjectInputStream(
					socket.getInputStream());
			byte[] rcvstr = (byte[])ois.readObject();
			//����
			byte[] str2 = AESUtil.decrypt(rcvstr,ip);
			String str1 = new String(str2,"iso-8859-1");
			if(str1.equals("NoSuchAlgorithmException")||str1.equals("NoSuchPaddingException")||str1.equals("InvalidKeyException")||str1.equals("BadPaddingException")||str1.equals("IllegalBlockSizeException")){
				System.out.println("JAVA security, error key");
			}else{
				msg = (Message) SerializeUtil.deserialize(str1);
				if (msg.getType().equals(MsgType.uninstallFTP)) {
					String ret = (String) msg.getValues();
					//�������ݿ�
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


	public int sendUninstallSQLServer2008R2Msg( String ip,String path) {
		// TODO Auto-generated method stub
		int opID = insertEvent(ip,"uninstall-SQLServer2008R2");
		//����Socket��Ϣ��Agent
		try {
			Socket socket = new Socket(ip, 9600);
			String[] values = new String[1];
			values[0] = path;
			Message msg = new Message(MsgType.uninstallSQLServer2008R2, opID+"",values);
			//����
			String datatemp = SerializeUtil.serialize(msg);  
			byte[] str = AESUtil.encrypt(datatemp,ip);
			//����
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			oos.writeObject(str);
			//��÷�����Ϣ
			ObjectInputStream ois = new ObjectInputStream(
					socket.getInputStream());
			byte[] rcvstr = (byte[])ois.readObject();
			//����
			byte[] str2 = AESUtil.decrypt(rcvstr,ip);
			String str1 = new String(str2,"iso-8859-1");
			if(str1.equals("NoSuchAlgorithmException")||str1.equals("NoSuchPaddingException")||str1.equals("InvalidKeyException")||str1.equals("BadPaddingException")||str1.equals("IllegalBlockSizeException")){
				System.out.println("JAVA security, error key");
			}else{
				msg = (Message) SerializeUtil.deserialize(str1);
				if (msg.getType().equals(MsgType.uninstallSQLServer2008R2)) {
					String ret = (String) msg.getValues();
					//�������ݿ�
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

	public int sendUninstallSQLServer2000Msg( String ip) {
		// TODO Auto-generated method stub
		int opID = insertEvent(ip,"uninstall-SQLServer2000");
		//����Socket��Ϣ��Agent
		try {
			Socket socket = new Socket(ip, 9600);
			
			Message msg = new Message(MsgType.uninstallSQLServer2000, opID+"",null);
			//����
			String datatemp = SerializeUtil.serialize(msg);  
			byte[] str = AESUtil.encrypt(datatemp,ip);
			//����
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			oos.writeObject(str);
			//��÷�����Ϣ
			ObjectInputStream ois = new ObjectInputStream(
					socket.getInputStream());
			byte[] rcvstr = (byte[])ois.readObject();
			//����
			byte[] str2 = AESUtil.decrypt(rcvstr,ip);
			String str1 = new String(str2,"iso-8859-1");
			if(str1.equals("NoSuchAlgorithmException")||str1.equals("NoSuchPaddingException")||str1.equals("InvalidKeyException")||str1.equals("BadPaddingException")||str1.equals("IllegalBlockSizeException")){
				System.out.println("JAVA security, error key");
			}else{
				msg = (Message) SerializeUtil.deserialize(str1);
				if (msg.getType().equals(MsgType.uninstallSQLServer2000)) {
					String ret = (String) msg.getValues();
					//�������ݿ�
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

	public int sendUninstallOracle11gMsg( String ip,String path) {
		// TODO Auto-generated method stub
		int opID = insertEvent(ip,"uninstall-Oracle11g");
		//����Socket��Ϣ��Agent
		try {
			Socket socket = new Socket(ip, 9600);
			String[] values = new String[1];
			values[0] = path;
			Message msg = new Message(MsgType.uninstallOracle11g, opID+"",values);
			//����
			String datatemp = SerializeUtil.serialize(msg);  
			byte[] str = AESUtil.encrypt(datatemp,ip);
			//����
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			oos.writeObject(str);
			//��÷�����Ϣ
			ObjectInputStream ois = new ObjectInputStream(
					socket.getInputStream());
			byte[] rcvstr = (byte[])ois.readObject();
			//����
			byte[] str2 = AESUtil.decrypt(rcvstr,ip);
			String str1 = new String(str2,"iso-8859-1");
			if(str1.equals("NoSuchAlgorithmException")||str1.equals("NoSuchPaddingException")||str1.equals("InvalidKeyException")||str1.equals("BadPaddingException")||str1.equals("IllegalBlockSizeException")){
				System.out.println("JAVA security, error key");
			}else{
				msg = (Message) SerializeUtil.deserialize(str1);
				if (msg.getType().equals(MsgType.uninstallOracle11g)) {
					String ret = (String) msg.getValues();
					//�������ݿ�
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
	
	
	public int sendUninstallOracle11gLinuxMsg( String ip,String oraclepath) {
		// TODO Auto-generated method stub
		int opID = insertEvent(ip,"uninstall-Oracle11g");
		//����Socket��Ϣ��Agent
		try {
			Socket socket = new Socket(ip, 9600);
			String[] values = new String[1];
			values[0] = oraclepath;
			Message msg = new Message(MsgType.uninstallOracle11g, opID+"",values);
			//����
			String datatemp = SerializeUtil.serialize(msg);  
			byte[] str = AESUtil.encrypt(datatemp,ip);
			//����
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			oos.writeObject(str);
			//��÷�����Ϣ
			ObjectInputStream ois = new ObjectInputStream(
					socket.getInputStream());
			byte[] rcvstr = (byte[])ois.readObject();
			//����
			byte[] str2 = AESUtil.decrypt(rcvstr,ip);
			String str1 = new String(str2,"iso-8859-1");
			if(str1.equals("NoSuchAlgorithmException")||str1.equals("NoSuchPaddingException")||str1.equals("InvalidKeyException")||str1.equals("BadPaddingException")||str1.equals("IllegalBlockSizeException")){
				System.out.println("JAVA security, error key");
			}else{
				msg = (Message) SerializeUtil.deserialize(str1);
				if (msg.getType().equals(MsgType.uninstallOracle11g)) {
					String ret = (String) msg.getValues();
					//�������ݿ�
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

	public int sendUninstall360Msg( String ip,String path) {
		// TODO Auto-generated method stub
		int opID = insertEvent(ip,"uninstall-360");
		//����Socket��Ϣ��Agent
		try {
			Socket socket = new Socket(ip, 9600);
			String[] values = new String[1];
			values[0] = path;
			Message msg = new Message(MsgType.uninstall360, opID+"",values);
			//����
			String datatemp = SerializeUtil.serialize(msg);  
			byte[] str = AESUtil.encrypt(datatemp,ip);
			//����
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			oos.writeObject(str);
			//��÷�����Ϣ
			ObjectInputStream ois = new ObjectInputStream(
					socket.getInputStream());
			byte[] rcvstr = (byte[])ois.readObject();
			//����
			byte[] str2 = AESUtil.decrypt(rcvstr,ip);
			String str1 = new String(str2,"iso-8859-1");
			if(str1.equals("NoSuchAlgorithmException")||str1.equals("NoSuchPaddingException")||str1.equals("InvalidKeyException")||str1.equals("BadPaddingException")||str1.equals("IllegalBlockSizeException")){
				System.out.println("JAVA security, error key");
			}else{
				msg = (Message) SerializeUtil.deserialize(str1);
				if (msg.getType().equals(MsgType.uninstall360)) {
					String ret = (String) msg.getValues();
					//�������ݿ�
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
