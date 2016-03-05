package edu.xidian.agent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import edu.xidian.linux.LOSConfig;
import edu.xidian.linux.LOSGet;
import edu.xidian.message.AESUtil;
import edu.xidian.message.MD5Util;
import edu.xidian.message.Message;
import edu.xidian.message.MsgType;
import edu.xidian.message.SerializeUtil;

/**
 * @ClassName: LOSConfigHandle
 * @Description: linux�ϣ����ݲ�ͬ����Ϣ���ͣ����в�ͬ�Ĵ���
 * @author: wangyannan
 * @date: 2014-11-13 ����9:11:51
 */
public class LOSConfigHandle {

	/**
	 * @Title:LOSConfigHandle
	 * @Description:�����߳�
	 * @param msg
	 */
	public LOSConfigHandle(final Socket socket) {
		new Thread(new Runnable() {
			public void run() {
				/**
				 * ����ִ�еĲ�������1
				 */
				Agent.opNum++;
				
				ObjectInputStream ois = null;
				ObjectOutputStream oos = null;
				try {
					/**
					 * ��ȡ������Ϣ��������
					 */
					ois = new ObjectInputStream(socket.getInputStream());
					byte[] str = (byte[]) ois.readObject();
					byte[] str2 = AESUtil.decrypt(str);
					String str1 = new String(str2, "iso-8859-1");

					/**
					 * �ж��Ƿ�Ϊ��ȷ��Կ���������ִ�У����򷵻ش�����Կ����Ϣ
					 */
					if (!AESUtil.isErrorKey(str1)) {
						Message msg = (Message) SerializeUtil.deserialize(str1);
						LOSConfig loc = new LOSConfig();
						
						LOSGet  lg = new LOSGet();
						
						String command = msg.getType().toString();
						String opID = msg.getopID();
						Date date = new Date(System.currentTimeMillis());
						String time = date.toString();
						String result = new String();

						/**
						 * д����Կ��֤�ɹ��ļ�¼����־
						 */
						XMLRecord.write(opID, time, command, "right key");

						/**
						 * ���������ļ����ж��Ƿ����ִ�в������������ִ�У����򷵻ؾܾ�ִ�е���Ϣ
						 */
						if (Agent.config.get(msg.getType().toString()).equals(
								"on"))
							result = "executing";
						else 
							result = "0x0700001";
						

						if (result.equals("executing")) {

							/**
							 * д�뿪ʼִ�еļ�¼����־
							 */
							date = new Date(System.currentTimeMillis());
							time = date.toString();
							XMLRecord.write(opID, time, command,
									"start executing");

							/**
							 * ���ݲ�ͬ����Ϣ���ͣ����в�ͬ�Ĵ���
							 */
							/**
							 * �޸�����
							 */
							if (msg.getType().equals(MsgType.changePasswd)) {
								String[] userPasswd = (String[]) msg
										.getValues();
								System.out.println("changing " + userPasswd[0]
										+ " password");
								result = loc.changePasswd(userPasswd[0],
										userPasswd[1]);
							}
							/**
							 * �޸İ�ȫ����
							 */
							else if (msg.getType()
									.equals(MsgType.changeSecRule)) {
								String[] rule = (String[]) msg.getValues();
								System.out.println("changeing security rule "
										+ rule[0] + " " + rule[1] + " "
										+ rule[2]);
								result = loc.changeSecRule(rule[0], rule[1],
										rule[2]);
							}
							/**
							 * ��������
							 */
							else if (msg.getType().equals(MsgType.startService)) {
								String service = (String) msg.getValues();
								System.out.println("starting service "
										+ service);
								result = loc.startService(service);
							}
							/**
							 * �رշ���
							 */
							else if (msg.getType().equals(MsgType.stopService)) {
								String service = (String) msg.getValues();
								System.out
										.println("stoping service " + service);
								result = loc.stopService(service);

							}
						
						
							/**
							 * �޸ľ�̬IP
							 */
							else if (msg.getType().equals(MsgType.changeIP)) {
								String[] values = (String[]) msg.getValues();
								System.out.println("changing IP " + values[0]);
								result = loc.changeIP(values[0], values[1],
										values[2]);
							}
//							/**
//							 * �޸ĸ���IP
//							 */
//							else if (msg.getType().equals(MsgType.changeAffiIP)) {
//								String[] values = (String[]) msg.getValues();
//								System.out.println("changing IP " + values[0]);
//								result = loc.changeAffiIP(values[0], values[1],
//										values[2]);
//							}
							/**
							 * �޸�ulimit
							 */
							else if (msg.getType().equals(MsgType.changeUlimit)) {
								String ulimit = (String) msg.getValues();
								System.out.println("changing ulimit " + ulimit);
								result = loc.changeUlimit(ulimit);
							}
							
							/**
							 * ��ȡulimit
							 */
							else if (msg.getType().equals(MsgType.getUlimit)) {
								String ulimit = (String) msg.getValues();
								System.out.println("geting ulimit " + ulimit);
								result = lg.getUlimit();
							}
							/**
							 * ��ȡ��ȫ����
							 */
							else if (msg.getType().equals(MsgType.getSecRule)) {
								String secrule = (String) msg.getValues();
								System.out.println("geting secrule " + secrule);
								result = lg.getSecRule();
							}
							/**
							 * ��ȡ��̬IP
							 */
							else if (msg.getType().equals(MsgType.getIP)) {
								String ip = (String) msg.getValues();
								System.out.println("getting ip " + ip);
								result = lg.getIP();
							}
							/**
							 * ��ȡ����one service
							 */
							else if (msg.getType().equals(MsgType.getServiceState)) {
								System.out.println("getting all service ");
								result = lg.getService();
							}
							/**
							 * ��ȡ����״̬
							 */
							else if (msg.getType().equals(MsgType.getOneServiceState)) {
								String values = (String) msg.getValues();
								System.out.println("getting service " + values);
								result = lg.getServiceStatus(values);
							}
							
							
							
							/**
							 * ��ѯ����IP
							 */
							
							else if(msg.getType().equals(MsgType.searchAffiIP)){
								String[] values=(String[])msg.getValues();
								System.out.println("getting AffIP in "+values[0]);
								result=loc.findALLIPinNc(values[0]);
							}
							/**
							 * ���Ӹ���IP
							 */
							else if(msg.getType().equals(MsgType.addAffiIP)){
								String[] values=(String[])msg.getValues();
								System.out.println("add AffIP in "+values[0]+" IP "+values[1]+" netMask "+values[2]);
								result=loc.addFSIP(values[0], values[1], values[2]);
							}
							
							/**
							 * ɾ������IP
							 */
							else if(msg.getType().equals(MsgType.deleteAffiIP)){
								String[] values=(String[])msg.getValues();
								System.out.println("delete AffIP "+values[1]+" in "+values[0]);
								result=loc.deleteFSIP(values[0], values[1]);
							}
							/**
							 * �޸ĸ���IP
							 */
							else if(msg.getType().equals(MsgType.changeAffiIP)){
								String[] values=(String[])msg.getValues();
								System.out.println("change AffIP "+values[1]+" in "+values[0]+" to "+values[2]);
								result=loc.changeFSIP(values[0], values[1], values[2]);
							}
							
							
							
							
							/**
							 * ��ȡkey
							 */
							else if(msg.getType().equals(MsgType.readkey)){
								String[] values=(String[])msg.getValues();
								System.out.println("read  key");
								String[] s = loc.createKeyPairs();
								result = "";
								result="Private Key = "+s[1]+"       Public Key = "+s[2];
							}
						}
						System.out.println("os configure " + result);

						/**
						 * �������ִ�е���Ϣ��������
						 */
						oos = new ObjectOutputStream(socket.getOutputStream());
						Message outMsg = new Message(msg.getType(), msg
								.getopID(), result);
						String datatemp = SerializeUtil.serialize(outMsg);
						byte[] outStr = AESUtil.encrypt(datatemp);
						oos.writeObject(outStr);
						oos.flush();

						/**
						 * д��ִ�н���ļ�¼����־
						 */
						date = new Date(System.currentTimeMillis());
						time = date.toString();
						System.out.println("XML record "
								+ XMLRecord.write(opID, time, command, result));
					} else {
						/**
						 * �����Կ�������Ϣ��������
						 */
						oos = new ObjectOutputStream(socket.getOutputStream());
						Message outMsg = new Message(MsgType.errorKey, "1",
								"0x0600001");
						String datatemp = SerializeUtil.serialize(outMsg);
						byte[] outStr = AESUtil.encrypt(datatemp);
						oos.writeObject(outStr);
						oos.flush();

						/**
						 * д����Կ����ļ�¼����־
						 */
						Date date = new Date(System.currentTimeMillis());
						String time = date.toString();
						XMLRecord.write("null", time, "null", "error key");
					}
					
					/**
					 * ����ִ�еĲ�������1
					 */
					Agent.opNum--;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					try {
						ois.close();
					} catch (Exception ex) {
					}
					try {
						oos.close();
					} catch (Exception ex) {
					}
					try {
						socket.close();
					} catch (Exception ex) {
					}
				}
			}
		}).start();
	}
}
