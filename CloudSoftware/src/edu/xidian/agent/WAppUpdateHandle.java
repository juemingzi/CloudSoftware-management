package edu.xidian.agent;

import java.io.File;
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

import edu.xidian.DBOperation.GetTotallSpace;
import edu.xidian.message.AESUtil;
import edu.xidian.message.MD5Util;
import edu.xidian.message.Message;
import edu.xidian.message.MsgType;
import edu.xidian.message.SerializeUtil;
import edu.xidian.windows.WAppUpdate;

/**
 * @ClassName: WAppUpdateHandle
 * @Description: windows�ϣ����ݲ�ͬ����Ϣ���ͣ����²�ͬ��Ӧ��
 * @author: wangyannan
 * @date: 2014-11-14 ����10:15:13
 */
public class WAppUpdateHandle {

	/**
	 * @Title:WAppUpdateHandle
	 * @Description:����Ӧ���߳�
	 * @param socket
	 */
	public WAppUpdateHandle(final Socket socket) {
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
						String[] values = (String[]) msg.getValues();
						String command = msg.getType().toString();
						String opID = msg.getopID();
						// //////////////////
						String updatePath = values[4];
						File f = new File(updatePath.substring(0, 2));
						if (f.exists()) {

							/**
							 * ���������ļ����ж��Ƿ����ִ�в������������ִ�У����򷵻ؾܾ�ִ�е���Ϣ
							 */
							if (Agent.config.get(msg.getType().toString())
									.equals("on"))
								result = "downloading";
							else
								result = "0x0700001";

							/**
							 * ����������ص���Ϣ��������
							 */
							oos = new ObjectOutputStream(socket
									.getOutputStream());
							Message outMsg = new Message(msg.getType(), msg
									.getopID(), result);
							String datatemp = SerializeUtil.serialize(outMsg);
							byte[] outStr = AESUtil.encrypt(datatemp);
							oos.writeObject(outStr);
							oos.flush();

							WAppUpdate wau = new WAppUpdate();

							Date date = new Date(System.currentTimeMillis());
							String time = date.toString();

							String[] name = new String[2];

							/**
							 * д����Կ��֤�ɹ��ļ�¼����־
							 */
							XMLRecord.write(opID, time, command, "right key");

							if (result.equals("downloading")) {

								
								int l = msg.getType().toString().length();
								String sn = null;
								if(msg.getType().toString().contains("Interface"))
									sn = msg.getType().toString().substring(5, l-9);
								else 
									sn = msg.getType().toString().substring(5, l);
								System.out.println(sn);
								
								
								GetTotallSpace getzip = new GetTotallSpace();
								double setupzip = getzip.searchSetupForZip(sn) * 1.0 / 1024;
								getzip.close();
								
								
								// ������ؿռ��Ƿ��㹻
								double freeDownLoadSpace = Long
										.valueOf(values[3]) * 1.0 / 1024;
								// double currentDownLoadSpace = new
								// File(updatePath.substring(0, 2)+"\\")
								// .getFreeSpace() * 1.0 / 1024 / 1024;
								double currentDownLoadSpace = new File("C:\\")
										.getFreeSpace() * 1.0 / 1024 / 1024 - setupzip;
								System.out
										.println("currentSpace  ###########   freespace :"
												+ currentDownLoadSpace
												+ "      " + freeDownLoadSpace);

								if (currentDownLoadSpace < freeDownLoadSpace) {
									Thread.sleep(1000);
									result = "0x1000001";// ##########################
									/**
									 * ��������������ȳ����Ͱ�װ�������Ϣ��������
									 */
//									Socket socket = new Socket("119.90.140.60",
//											7001);
									Socket socket = new Socket(ip,7001);
									
									outMsg = new Message(msg.getType(), msg
											.getopID(), result);
									ObjectOutputStream ooos = new ObjectOutputStream(
											socket.getOutputStream());
									datatemp = SerializeUtil.serialize(outMsg);
									outStr = AESUtil.encrypt(datatemp);
									ooos.writeObject(outStr);
									ooos.flush();
									socket.close();

									System.out.println("app setup " + result);

									/**
									 * д�밲װ����ļ�¼����־
									 */
									date = new Date(System.currentTimeMillis());
									time = date.toString();
									System.out.println("XML record "
											+ XMLRecord.write(opID, time,
													command, result));

								} else {

									/**
									 * д�뿪ʼִ�еļ�¼����־
									 */
									date = new Date(System.currentTimeMillis());
									time = date.toString();
									XMLRecord.write(opID, time, command,
											"start executing");
									System.out.println("start download");

									/**
									 * д���������صļ�¼����־
									 */
									date = new Date(System.currentTimeMillis());
									time = date.toString();
									XMLRecord
											.write(opID, time, command, result);

									/**
									 * �������أ��ж��Ƿ��Ѵ��ڣ����ǣ��������أ���������
									 */

									if (values[1].contains(",")) {
//										name = values[1].split(",");
//										File f0 = new File("C:\\softsource\\"
//												+ name[0]);
//										File f1 = new File("C:\\softsource\\"
//												+ name[1]);
//										if (!f0.exists())
											FtpTransFile
													.fileDownload("windows",
															name[0],
															"C:\\softsource",
															values[0], "test",
															"123456");
//										if (!f1.exists())
											FtpTransFile
													.fileDownload("windows",
															name[1],
															"C:\\softsource",
															values[0], "test",
															"123456");
									} else {
//										File f1 = new File("C:\\softsource\\"
//												+ values[1]);
//										if (!f1.exists())
											FtpTransFile
													.fileDownload("windows",
															values[1],
															"C:\\softsource",
															values[0], "test",
															"123456");
									}
									System.out.println("end download");

									/**
									 * ��������������ȳ�����������ɵ���Ϣ��������
									 */
//									Socket socket = new Socket("119.90.140.60",
//											7001);
									Socket socket = new Socket(ip,7001);
									
									outMsg = new Message(msg.getType(), msg
											.getopID(), "download completed");
									ObjectOutputStream ooos = new ObjectOutputStream(
											socket.getOutputStream());
									datatemp = SerializeUtil.serialize(outMsg);
									outStr = AESUtil.encrypt(datatemp);
									ooos.writeObject(outStr);
									ooos.flush();
									socket.close();

									/**
									 * д��������ɵļ�¼����־
									 */
									date = new Date(System.currentTimeMillis());
									time = date.toString();
									XMLRecord.write(opID, time, command,
											"download completed");

									Thread.sleep(3000);

									// �����̿ռ�
									double freeSpace = Long.valueOf(values[2]) * 1.0 / 1024;
									

									
									int ll = msg.getType().toString().length();
									String snn = null;
									if(msg.getType().toString().contains("Interface"))
										snn = msg.getType().toString().substring(5, ll-9);
									else 
										snn = msg.getType().toString().substring(5, ll);
									System.out.println(snn);
									
									
									
									
									// ��ȡ��װ·��

									String diskPath = values[4].split(":")[0];
									
									GetTotallSpace getspace = new GetTotallSpace();
									double setupspace = getspace.searchSetupForSpace(diskPath+":",snn) * 1.0 / 1024;
									getspace.close();
									
									
									System.out
											.println("diskPath *****************************"
													+ diskPath);
									double currentSpace = new File(diskPath+ ":\\")
											.getFreeSpace() * 1.0 / 1024 / 1024 - setupspace;

									System.out
											.println("currentSpace  ###########   freespace :"
													+ currentSpace
													+ "      "
													+ freeSpace);

									if (currentSpace < freeSpace) {

										result = "0x1000002";// ###########################################

									} else {

										/**
										 * ���ݲ�ͬ����Ϣ���ͣ����в�ͬ�Ĵ���
										 */
										/**
										 * ����Tomact
										 */
										if (msg.getType().equals(
												MsgType.updateTomcat)) {
											System.out
													.println("updateing Tomact");
											result = wau.updateTomcat(
													values[1], values[4],
													values[5], values[6]);
										}
										/**
										 * ����Mysql
										 */
										else if (msg.getType().equals(
												MsgType.updateMySql)) {
											System.out
													.println("updateing MySql");
											result = wau.updateMySql(values[1],
													values[5], values[4],
													values[6]);

										}
										/**
										 * ����Apache
										 */
										else if (msg.getType().equals(
												MsgType.updateApache)) {
											System.out
													.println("updateing Apache");
											result = wau.updateApache(
													values[1], values[4],
													values[5], values[6]);
										}
										/**
										 * ����Nginx
										 */
										else if (msg.getType().equals(
												MsgType.updateNginx)) {
											System.out
													.println("updateing Nginx");
											result = wau.updateNginx(values[1],
													values[5], values[4]);
										}
										/**
										 * ����ZendGuardLoader
										 */
										else if (msg.getType().equals(
												MsgType.updateZendGuardLoader)) {
											System.out
													.println("updateing ZendGuardLoader");
											result = wau.updateZendGuardLoader(
													values[1], values[4],
													values[5]);
										}
										/**
										 * ����Python
										 */
										else if (msg.getType().equals(
												MsgType.updatePython)) {
											System.out
													.println("updateing Python");
											result = wau.updatePython(
													values[1], values[4],
													values[5]);
										}
										/**
										 * ����Memcached
										 */
										else if (msg.getType().equals(
												MsgType.updateMemcached)) {
											System.out
													.println("updateing Memcached");
											result = wau.updateMemcached(
													values[1], values[4],
													values[5]);
										}

										/**
										 * ����FTP
										 */
										else if (msg.getType().equals(
												MsgType.updateFTP)) {
											System.out.println("updateing FTP");
											result = wau.updateFTP(values[1],
													values[4], values[5]);
										}

										/**
										 * ����360
										 */
										else if (msg.getType().equals(
												MsgType.update360)) {
											System.out.println("updateing 360");
											result = wau.update360(values[1],
													values[4]);
										}
									}

									/**
									 * ��������������ȳ����������������Ϣ��������
									 */
//									socket = new Socket("119.90.140.60", 7001);
									socket = new Socket(ip,7001);
									outMsg = new Message(msg.getType(), msg
											.getopID(), result);
									ooos = new ObjectOutputStream(socket
											.getOutputStream());
									datatemp = SerializeUtil.serialize(outMsg);
									outStr = AESUtil.encrypt(datatemp);
									ooos.writeObject(outStr);
									ooos.flush();
									socket.close();
								}
								System.out.println("app update " + result);

								/**
								 * д����������ļ�¼����־
								 */
								date = new Date(System.currentTimeMillis());
								time = date.toString();
								System.out.println("XML record "
										+ XMLRecord.write(opID, time, command,
												result));
							}
						} else {
							result = "0x1200001";
							/**
							 * ��������������ȳ����������������Ϣ��������
							 */
//							Socket socket = new Socket("119.90.140.60", 7001);
							Socket socket = new Socket(ip,7001);
							Message outMsg = new Message(msg.getType(), msg
									.getopID(), result);
							ObjectOutputStream ooos = new ObjectOutputStream(
									socket.getOutputStream());
							String datatemp = SerializeUtil.serialize(outMsg);
							byte[] outStr = AESUtil.encrypt(datatemp);
							ooos.writeObject(outStr);
							ooos.flush();
							socket.close();
							/**
							 * д����������ļ�¼����־
							 */
							Date date = new Date(System.currentTimeMillis());
							String time = date.toString();
							System.out.println("XML record "
									+ XMLRecord.write(opID, time, command,
											result));
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
