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

import edu.xidian.linux.LAppUninstall;
import edu.xidian.message.AESUtil;
import edu.xidian.message.MD5Util;
import edu.xidian.message.Message;
import edu.xidian.message.MsgType;
import edu.xidian.message.SerializeUtil;
import edu.xidian.windows.WAppSetup;

/**
 * @ClassName: LAppUninstallHandle
 * @Description: windows�ϣ����ݲ�ͬ����Ϣ���ͣ�ж�ز�ͬ��Ӧ��
 * @author: wangyannan
 * @date: 2015-1-16 ����10:18:14
 */
public class LAppUninstallHandle {

	/**
	 * @Title:LAppUninstallHandle
	 * @Description:ж��Ӧ���߳�
	 * @param socket
	 */
	public LAppUninstallHandle(final Socket socket) {
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
					 * ��ȡsocket��ip
					 */
					
					String ip = socket.getInetAddress().getHostAddress();
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
						String result = new String();

						/**
						 * ���������ļ����ж��Ƿ����ִ�в������������ִ�У����򷵻ؾܾ�ִ�е���Ϣ
						 */
						if (Agent.config.get(msg.getType().toString()).equals(
								"on"))
							result = "executing";
						else
							result = "0x0700001";

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

						LAppUninstall lau = new LAppUninstall();
						String command = msg.getType().toString();
						String opID = msg.getopID();
						Date date = new Date(System.currentTimeMillis());
						String time = date.toString();
						String[] values = (String[]) msg.getValues();

						/**
						 * д����Կ��֤�ɹ��ļ�¼����־
						 */
						XMLRecord.write(opID, time, command, "right key");

						if (result.equals("executing")) {

							/**
							 * д�뿪ʼִ�еļ�¼����־
							 */
							date = new Date(System.currentTimeMillis());
							time = date.toString();
							XMLRecord.write(opID, time, command,
									"start executing");

							/**
							 * д������ִ�еļ�¼����־
							 */
							date = new Date(System.currentTimeMillis());
							time = date.toString();
							XMLRecord.write(opID, time, command, result);

							Thread.sleep(1000);
							/**
							 * ���ݲ�ͬ����Ϣ���ͣ����в�ͬ�Ĵ���
							 */
							/**
							 * ж��Tomact
							 */
							if (msg.getType().equals(MsgType.uninstallTomcat)) {
								System.out.println("uninstalling Tomact");
								result = lau.uninstallTomcat(values[0]);
							}
							/**
							 * ж��Mysql
							 */
							else if (msg.getType().equals(
									MsgType.uninstallMySql)) {
								System.out.println("uninstalling MySql");
								result = lau.uninstallMySql(values[0]);

							}
							/**
							 * ж��Apache
							 */
							else if (msg.getType().equals(
									MsgType.uninstallApache)) {
								System.out.println("uninstalling Apache");
								result = lau.uninstallApache(values[0]);
							}
							/**
							 * ж��Nginx
							 */
							else if (msg.getType().equals(
									MsgType.uninstallNginx)) {
								System.out.println("uninstalling Nginx");
								result = lau.uninstallNginx(values[0]);
							}
							/**
							 * ж��ZendGuardLoader
							 */
							else if (msg.getType().equals(
									MsgType.uninstallZendGuardLoader)) {
								System.out
										.println("uninstalling ZendGuardLoader");
								result = lau
										.uninstallZendGuardLoader(values[0]);
							}
							/**
							 * ж��Python
							 */
							else if (msg.getType().equals(
									MsgType.uninstallPython)) {
								System.out.println("uninstalling Python");
								result = lau.uninstallPython(values[0]);
							}
							/**
							 * ж��Memcached
							 */
							else if (msg.getType().equals(
									MsgType.uninstallMemcached)) {
								System.out.println("uninstalling Memcached");
								result = lau.uninstallMemcached(values[0]);
							}
							/**
							 * ж��FTP
							 */
							else if (msg.getType().equals(MsgType.uninstallFTP)) {
								System.out.println("uninstalling FTP");
								result = lau.uninstallFTP(values[0]);
							}
					
							/**
							 * ж��Oracle11g
							 */
							else if (msg.getType().equals(
									MsgType.uninstallOracle11g)) {
								System.out.println("uninstalling Oracle11g");
								result = lau.uninstallOracle11g(values[0]);
							}

							/**
							 * ��������������ȳ�����ж�ؽ������Ϣ��������
							 */
//							Socket socket = new Socket("119.90.140.60", 7001);
							Socket socket = new Socket(ip,7001);
							outMsg = new Message(msg.getType(), msg.getopID(),
									result);
							ObjectOutputStream ooos = new ObjectOutputStream(
									socket.getOutputStream());
							datatemp = SerializeUtil.serialize(outMsg);
							outStr = AESUtil.encrypt(datatemp);
							ooos.writeObject(outStr);
							ooos.flush();
							socket.close();
						}
						System.out.println("app uninstall " + result);

						/**
						 * д��ж�ؽ���ļ�¼����־
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
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
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
