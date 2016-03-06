package sample.hello.bean;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
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

public class VMScript {
	DBForSCS scs = new DBForSCS();
	public VMScript() throws SQLException{
		scs.Delete("script");
	}
	/**
	 * 将虚拟机脚本执行事件插入到数据库中
	 * @param hostIp
	 */
	public int insertEvent(String hostIp){
		int opID = -1;
		DBOperation dbop = new DBOperation();
		
		try {
			opID = dbop.insertOperation(hostIp,"executeVMScript","");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbop.close();
		return opID;
	}
	
	public int sendExeVmScriptMsg( String ip, File file,String name,String uploadpath) {
		int opID = insertEvent(ip);
		try {
			Socket socket = new Socket(ip, 9400);
			Message msg = new Message(MsgType.executeVMScript, opID+"",
					file.getName());
			//加密
			String datatemp = SerializeUtil.serialize(msg);  
			byte[] str = AESUtil.encrypt(datatemp,ip);
			//传输
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			oos.writeObject(str);		
			
			
			// 发送文件
			System.out.println("#####script file length : " + file.length());
			DataOutputStream dos = new DataOutputStream(
					socket.getOutputStream());
			FileInputStream fis = new FileInputStream(file.getPath());
			// 传送文件长度
			dos.writeLong((long) file.length());
			dos.flush();
			System.out.println(file.length());
			System.out.println("#####start to send script file : " + file.getName() + "...");

			// 缓冲区大小
			int bufferSize = 8192;
			// 缓冲区
			byte[] buf = new byte[bufferSize];
			// 传输文件
			while (true) {
				int read = 0;
				if (fis != null) {
					read = fis.read(buf);

				}
				if (read == -1) {
					break;
				}
				dos.write(buf, 0, read);

			}
			dos.flush();
			System.out.println("#####script file transmission completed!");
			
			//在此处插入script表数据，并设置为false
//			String uploadpath = "C:/scripts/"+name;
			scs.InsertToScript(opID,ip, uploadpath);
			
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

				// // 获得反馈信息
				// ObjectInputStream ois = new ObjectInputStream(
				// socket.getInputStream());
				// str = (String) ois.readObject();
				// // 解密
				// String str2 = MD5Util.convertMD5(str);
				// msg = (Message) SerializeUtil.deserialize(str2);
				if (msg.getType().equals(MsgType.executeVMScript)) {
					String ret = (String) msg.getValues();
					if (ret.equals("success") || ret.equals("executing")) {
						System.out.println("executeVMScript opID:" + ret);
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
