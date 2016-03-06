package edu.xidian.agent;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Date;

import edu.xidian.message.AESUtil;
import edu.xidian.message.Message;
import edu.xidian.message.MsgType;
import edu.xidian.message.SerializeUtil;

/**
 * @ClassName: LStatusGetHandle
 * @Description: ��ȡ���ؽ���
 * @author: wangyannan
 * @date: 2015-1-17 ����12:08:55
 */
public class LStatusGetHandle {

	/**
	 * @Title:LStatusGetHandle
	 * @Description:��ȡ���ؽ����߳�
	 * @param socket
	 */
	public LStatusGetHandle(final Socket socket) {
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
						String opID = msg.getopID();
						String command = msg.getType().toString();
						String fileName = (String) msg.getValues();
						System.out.println("getting  " + fileName
								+ " download status");

						/**
						 * д����Կ��֤�ɹ��ļ�¼����־
						 */
						Date date = new Date(System.currentTimeMillis());
						String time = date.toString();
						XMLRecord.write(opID, time, command, "right key");

						/**
						 * д�뿪ʼִ�еļ�¼����־
						 */
						date = new Date(System.currentTimeMillis());
						time = date.toString();
						XMLRecord.write(opID, time, command, "start executing");

						/**
						 * ��ȡ�ļ���С
						 */
						String storePath = new String("/home/softsource/");
						File file = new File("/home/softsource/" + fileName);
						String path;

						if (storePath.endsWith("\\") || storePath.endsWith("/"))// ������·�����½�һ����ʱ�ļ��У�����洢���Ǹ����߳����ص��ļ�
						{
							path = storePath
									+ fileName.substring(0,
											fileName.indexOf(".")) + "Temp/";// ��ʱ�ļ��е�����
																				// ����·��
						} else {
							path = storePath
									+ "/"
									+ fileName.substring(0,
											fileName.indexOf(".")) + "Temp/";
						}
						File dir = new File(path);

						long fl = 0, dl = 0;
						if (file.exists())
							fl = (int) file.length();
						if (dir.exists())
							dl = FileLength.getFileLength(path);
						/**
						 * length��KBΪ��λ
						 */
						long length = (fl + dl) / 1024 / 2;

						/**
						 * �����Ϣ��������
						 */
						oos = new ObjectOutputStream(socket.getOutputStream());
						Message outMsg = new Message(msg.getType(), opID,
								Long.toString(length));
						String datatemp = SerializeUtil.serialize(outMsg);
						byte[] outStr = AESUtil.encrypt(datatemp);
						oos.writeObject(outStr);
						oos.flush();

						/**
						 * д��ִ�н���ļ�¼����־
						 */
						date = new Date(System.currentTimeMillis());
						time = date.toString();
						XMLRecord.write(opID, time, command,
								Long.toString(length));
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
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
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
