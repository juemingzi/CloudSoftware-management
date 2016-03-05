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

import edu.xidian.message.AESUtil;
import edu.xidian.message.MD5Util;
import edu.xidian.message.Message;
import edu.xidian.message.MsgType;
import edu.xidian.message.SerializeUtil;
import edu.xidian.windows.AffiIP;
import edu.xidian.windows.SystemConfigurations;
import edu.xidian.windows.TestService;
import edu.xidian.windows.WOSConfig;

/**
 * @ClassName: WOSConfigHandle
 * @Description: windows�ϣ����ݲ�ͬ����Ϣ���ͣ����в�ͬ�Ĵ���
 * @author: wangyannan
 * @date: 2014-11-12 ����5:03:47
 */
public class WOSConfigHandle {
	/**
	 * @Title:WOSConfigHandle
	 * @Description:�����߳�
	 * @param socket
	 */
	public WOSConfigHandle(final Socket socket) {
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
						WOSConfig woc = new WOSConfig();
						AffiIP aff = new AffiIP();
						SystemConfigurations sc = new SystemConfigurations();

						String command = msg.getType().toString();
						String opID = msg.getopID();
						Date date = new Date(System.currentTimeMillis());
						String time = date.toString();
						String result = new String();

						/**
						 * д����Կ��֤�ɹ��ļ�¼����־
						 */
						XMLRecord.write(opID, time, command, "right key");
						System.out.println("the agent is $$$$$$$$$$"
								+ Agent.config.get(msg.getType().toString()));
						/**
						 * ���������ļ����ж��Ƿ����ִ�в������������ִ�У����򷵻ؾܾ�ִ�е���Ϣ
						 */
						System.out.println(msg.getType().toString());
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
								result = woc.changePasswd(userPasswd[0],
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
								result = woc.changeSecRule(rule[0], rule[1],
										rule[2], rule[3]);
							}
							/**
							 * ��������
							 */
							else if (msg.getType().equals(MsgType.startService)) {
								String service = (String) msg.getValues();
								System.out.println("starting service "
										+ service);
								result = woc.startService(service);
							}
							/**
							 * �رշ���
							 */
							else if (msg.getType().equals(MsgType.stopService)) {
								String service = (String) msg.getValues();
								System.out
										.println("stoping service " + service);
								result = woc.stopService(service);

							}
							/**
							 * ���̸�ʽ��
							 */
							else if (msg.getType().equals(MsgType.diskFormat)) {
								System.out.println("formating disk");
								result = woc.diskFormat();
							}
							/**
							 * �޸ľ�̬IP
							 */
							else if (msg.getType().equals(MsgType.changeIP)) {
								Object[] values = (Object[]) msg.getValues();
								System.out.println("changing IP ");
								result = woc.changeIP((String) values[0],
										(String) values[1], (String) values[2],
										(String[]) values[3]);
							}
//							/**
//							 * �޸ĸ���IP
//							 */
//							else if (msg.getType().equals(MsgType.addAffiIP)) {
//								Object[] values = (Object[]) msg.getValues();
//								System.out.println("changing IP ");
//								result = woc.addAffiIP((String[]) values[0],
//										(String[]) values[1],
//										(String[]) values[2]);
//							}

							/**
							 * ��ȡ��ȫ����
							 */
							else if (msg.getType().equals(MsgType.getSecRule)) {
								Object[] values = (Object[]) msg.getValues();
								System.out.println("getting secrule ");
								result = sc.getSystemSecRule();
							}
							/**
							 * ��ȡ��̬IP
							 */
							else if (msg.getType().equals(MsgType.getIP)) {
								Object[] values = (Object[]) msg.getValues();
								System.out.println("getting IP ");
								result = sc.getSystemIP();
							}
							// /**
							// * ��ȡ����IP
							// */
							// else if (msg.getType().equals(MsgType.getAffiIP))
							// {
							// Object[] values = (Object[]) msg.getValues();
							// System.out.println("changing IP ");
							// result = sc.getSystemaffiIP();
							// }
							/**
							 * ��ȡ����״̬
							 */
							else if (msg.getType().equals(
									MsgType.getServiceState)) {
								Object[] values = (Object[]) msg.getValues();
								System.out.println("getting servicestate ");
								result = sc.getSystemServiceState();
							}
							/**
							 * ��ȡһ������״̬
							 */
							else if (msg.getType().equals(
									MsgType.getOneServiceState)) {
								String values = (String) msg.getValues();
								TestService ts = new TestService();
								System.out.println("getting service " + values);
								result = ts.getServiceState(values);
							}

							/**
							 * ���Ҹ���IP
							 */
							else if (msg.getType().equals(MsgType.searchAffiIP)) {
								String[] values = (String[]) msg.getValues();
								System.out.println("getting ip" + values[0]);
								result = aff.getIPs(values[0]);

							}
							/**
							 * �޸ĸ���IP
							 */
							else if (msg.getType().equals(MsgType.changeAffiIP)) {
								String[] values = (String[]) msg.getValues();
								System.out.println("changing IP" + values[1]
										+ " to " + values[2]);
								result = aff.changeIP(values[0], values[1],
										values[2], values[3]);
							}
							/**
							 * ���Ӹ���IP
							 */
							else if (msg.getType().equals(MsgType.addAffiIP)) {
								String[] values = (String[]) msg.getValues();
								System.out.println("adding IP" + values[1]);
								result = aff.addIP(values[0], values[1],
										values[2]);
							}
							/**
							 * ɾ������IP
							 * 
							 */
							else if (msg.getType().equals(MsgType.deleteAffiIP)) {
								String[] values = (String[]) msg.getValues();
								System.out.println("deleting IP" + values[1]);
								result = aff.deleteIP(values[0], values[1]);

							}

						}

						System.out.println("os configure " + result);

						/**
						 * ���ִ�н������Ϣ��������
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
