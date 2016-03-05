package edu.xidian.agent;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Date;

import edu.xidian.message.AESUtil;
import edu.xidian.message.MD5Util;
import edu.xidian.message.Message;
import edu.xidian.message.MsgType;
import edu.xidian.message.SerializeUtil;
import edu.xidian.windows.WVMScriptExecute;

/**
 * @ClassName: WVMScriptExecuteHandle
 * @Description: windows�ϣ�������ű�ִ��
 * @author: wangyannan
 * @date: 2014-11-14 ����2:37:21
 */
public class WVMScriptExecuteHandle {

	/**
	 * @Title:WVMScriptExecuteHandle
	 * @Description:������ű�ִ���߳�
	 * @param msg
	 */
	public WVMScriptExecuteHandle(final Socket socket) {
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
					String fullPath = "";
					ois = new ObjectInputStream(socket.getInputStream());
					byte[] str = (byte[]) ois.readObject();
					byte[] str2 = AESUtil.decrypt(str);
					String str1 = new String(str2, "iso-8859-1");

					/**
					 * �ж��Ƿ�Ϊ��ȷ��Կ���������ִ�У����򷵻ش�����Կ����Ϣ
					 */
					if (!AESUtil.isErrorKey(str1)) {
						Message msg = (Message) SerializeUtil.deserialize(str1);
						String status = new String();

						/**
						 * ���������ļ����ж��Ƿ����ִ�в������������ִ�У����򷵻ؾܾ�ִ�е���Ϣ
						 */
						if (Agent.config.get(msg.getType().toString()).equals(
								"on"))
							status = "executing";
						else
							status = "0x0700001";

						/**
						 * �������ִ�е�״̬��������
						 */
						Message outMsg = new Message(MsgType.executeVMScript,
								msg.getopID(), status);
						oos = new ObjectOutputStream(socket.getOutputStream());
						String datatemp = SerializeUtil.serialize(outMsg);
						byte[] outStr = AESUtil.encrypt(datatemp);
						oos.writeObject(outStr);
						oos.flush();

						/**
						 * ��ȡ������Ϣ�Ĳ���ID����Ϣ����
						 */
						String opID = msg.getopID();
						String command = msg.getType().toString();
						Date date = new Date(System.currentTimeMillis());
						String time = date.toString();
						String[] result = new String[2];

						/**
						 * д����Կ��֤�ɹ��ļ�¼����־
						 */
						XMLRecord.write(opID, time, command, "right key");

						if (status.equals("executing")) {

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
							XMLRecord.write(opID, time, command, status);

							/**
							 * ����ű���д���½ű��ļ�
							 */
							// ����ļ���
							String fileName = (String) msg.getValues();
							fullPath = "C:\\scripts\\" + fileName;
							// �����ļ�
							DataOutputStream fileOut = new DataOutputStream(
									new FileOutputStream(fullPath));
							DataInputStream inputStream = new DataInputStream(
									new BufferedInputStream(socket
											.getInputStream()));
							// ��������С
							int bufferSize = 8192;
							byte[] buf = new byte[bufferSize];
							long passedlen = 0;
							long len = 0;
							// ��ȡ�ļ�����
							len = inputStream.readLong();
							System.out.println("�ļ��ĳ���Ϊ:" + len + "  B");
							System.out.println("��ʼ�����ļ�!");
							// ��ȡ�ļ�
							boolean f = true;
							while (f) {
								int read = 0;
								if (inputStream != null) {
									read = inputStream.read(buf);
								}
								passedlen += read;
								if (read == -1) {
									break;
								}
								System.out.println("�ļ�������"
										+ (passedlen * 100 / len) + "%");

								fileOut.write(buf, 0, read);

								if ((passedlen * 100 / len) == 100) {
									f = false;
								}
							}
							System.out.println("������ɣ��ļ���Ϊ" + fullPath);
							fileOut.close();// �ر��ļ���

							/**
							 * ִ�нű�
							 */
							WVMScriptExecute wvmse = new WVMScriptExecute();
							System.out.println("executing script " + fullPath);

							result = wvmse.executeVMScript(fullPath);

							/**
							 * ��������������ȳ����Ͱ�װ�������Ϣ��������
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

							System.out.println("execute script " + result[0]);

							/**
							 * д�밲װ����ļ�¼����־
							 */
							date = new Date(System.currentTimeMillis());
							time = date.toString();
							System.out.println("XML record "
									+ XMLRecord.write(opID, time, command,
											result[0] + "&&" + result[1]));

						} else {
							/**
							 * д��ܾ�ִ�еĽ���ļ�¼����־
							 */
							date = new Date(System.currentTimeMillis());
							time = date.toString();
							System.out.println("XML record "
									+ XMLRecord.write(opID, time, command,
											status));

						}
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
