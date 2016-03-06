package edu.xidian.agent;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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
import edu.xidian.linux.LAppUpdate;
import edu.xidian.message.AESUtil;
import edu.xidian.message.MD5Util;
import edu.xidian.message.Message;
import edu.xidian.message.MsgType;
import edu.xidian.message.SerializeUtil;

/**
 * @ClassName: LAppUpdateHandle
 * @Description: linux�ϣ����ݲ�ͬ����Ϣ���ͣ����²�ͬ��Ӧ��
 * @author: wangyannan
 * @date: 2014-11-14 ����10:27:19
 */
public class LAppUpdateHandle {

	/**
	 * @Title:LAppUpdateHandle
	 * @Description: ����Ӧ���߳�
	 * @param msg
	 */
	public LAppUpdateHandle(final Socket socket) {
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
						String command = msg.getType().toString();
						String opID = msg.getopID();
						Date date = new Date(System.currentTimeMillis());
						String time = date.toString();
						String[] values = (String[]) msg.getValues();
						Runtime rt = Runtime.getRuntime();

						String updatePath = values[4];

						String[] pt = updatePath.split("/");
						System.out.println(pt[1]);
						File f=new File("/");
						Process proc=rt.exec("ls", null, f);

						BufferedReader in = null;

						in = new BufferedReader(new InputStreamReader(proc
								.getInputStream()));
						String s = null;
						boolean flage = false;
						while ((s = in.readLine()) != null) {
							if (s.contains(pt[1])) {
								flage = true;
								break;
							}
						}

						if (flage) {

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

							LAppUpdate lau = new LAppUpdate();

							String[] name = new String[2];

							/**
							 * д����Կ��֤�ɹ��ļ�¼����־
							 */
							XMLRecord.write(opID, time, command, "right key");

							// �����̿ռ�
							int freeSpace = Integer.valueOf(values[2]);
							double currentSpace = 0;

							Process p = rt.exec("df -hl");// df -hl �鿴Ӳ�̿ռ�

							in = new BufferedReader(new InputStreamReader(p
									.getInputStream()));
							String string = null;

							while ((string = in.readLine()) != null) {
								String[] temp = string.split("\\s+");
								if (temp.length >= 3 && temp[3].contains("G")) {
									currentSpace += Double.valueOf(temp[3]
											.trim().replace("G", "")) * 1024 * 1024;
								}
							}
							
							int l = msg.getType().toString().length();
							
							String sn = msg.getType().toString().substring(6, l);
							System.out.println(sn);
							
							GetTotallSpace getspace = new GetTotallSpace();
							double zip = getspace.searchSetupForSpace("/",sn) * 1.0 / 1024;
							double setup = getspace.searchSetupForZip(sn) * 1.0 / 1024;
							double space = zip+setup;
							getspace.close();
							if (currentSpace - space < freeSpace) {
								Thread.sleep(1000);
								result = "0x1000002";// ###########################################

								/**
								 * ��������������ȳ����Ͱ�װ�������Ϣ��������
								 */
//								Socket socket = new Socket("119.90.140.60",7001);
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
										+ XMLRecord.write(opID, time, command,
												result));

							} else {

								if (result.equals("downloading")) {

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
									// if (!command.equals("updateFTP")) {
									System.out.println("start download");
									if (values[1].contains(",")) {
										name = values[1].split(",");
										File f0 = new File("/home/softsource/"
												+ name[0]);
										File f1 = new File("/home/softsource/"
												+ name[1]);
										if (!f0.exists())
											FtpTransFile
													.fileDownload("linux",
															name[0],
															"/home/softsource",
															values[0], "test",
															"123456");
										if (!f1.exists())
											FtpTransFile
													.fileDownload("linux",
															name[1],
															"/home/softsource",
															values[0], "test",
															"123456");
									} else {
										File f1 = new File("/home/softsource/"
												+ values[1]);
										if (!f1.exists())
											FtpTransFile
													.fileDownload("linux",
															values[1],
															"/home/softsource",
															values[0], "test",
															"123456");
									}
									System.out.println("end download");
									// }

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

									/**
									 * ���ݲ�ͬ����Ϣ���ͣ����в�ͬ�Ĵ���
									 */
									/**
									 * ����Tomact
									 */
									if (msg.getType().equals(
											MsgType.updateTomcat)) {
										System.out.println("updateing Tomact");
										result = lau
												.updateTomcat(values[1],
														values[3], values[4],
														values[5]);
									}
									/**
									 * ����Mysql
									 */
									else if (msg.getType().equals(
											MsgType.updateMySql)) {
										System.out.println("updateing MySql");
										result = lau.updateMysql(name[0],
												name[1],values[4],values[3]);

									}
									/**
									 * ����Apache
									 */
									else if (msg.getType().equals(
											MsgType.updateApache)) {
										System.out.println("updateing Apache");
										result = lau
												.updateApache(values[1],
														values[3], values[4],
														values[5]);
									}
									/**
									 * ����Nginx
									 */
									else if (msg.getType().equals(
											MsgType.updateNginx)) {
										System.out.println("updateing Nginx");
										result = lau.updateNginx(values[1],
												values[4],values[3]);
									}
									/**
									 * ����ZendGuardLoader
									 */
									else if (msg.getType().equals(
											MsgType.updateZendGuardLoader)) {
										System.out
												.println("updateing ZendGuardLoader");
										result = lau.updateZendGuardLoader(
												values[1], values[4],values[5]);
									}
									/**
									 * ����Python
									 */
									else if (msg.getType().equals(
											MsgType.updatePython)) {
										System.out.println("updateing Python");
										result = lau.updatePython(values[1],
												values[3], values[4]);
									}
									/**
									 * ����Memcached
									 */
									else if (msg.getType().equals(
											MsgType.updateMemcached)) {
										System.out
												.println("updateing Memcached");
										result = lau.updateMemcached(values[1],
												values[4], values[3]);
									}
									/**
									 * ����FTP
									 */
									else if (msg.getType().equals(
											MsgType.updateFTP)) {
										System.out.println("updateing FTP");
										result = lau.updateFTP(values[1],values[4],values[3]);
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
							date = new Date(System.currentTimeMillis());
							time = date.toString();
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
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
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
