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
 * @Description: linux上，根据不同的消息类型，更新不同的应用
 * @author: wangyannan
 * @date: 2014-11-14 上午10:27:19
 */
public class LAppUpdateHandle {

	/**
	 * @Title:LAppUpdateHandle
	 * @Description: 更新应用线程
	 * @param msg
	 */
	public LAppUpdateHandle(final Socket socket) {
		new Thread(new Runnable() {
			public void run() {
				/**
				 * 正在执行的操作数加1
				 */
				Agent.opNum++;

				ObjectInputStream ois = null;
				ObjectOutputStream oos = null;
				try {
					/**
					 * 获取socket的ip
					 */
					
					String ip = socket.getInetAddress().getHostAddress();
					/**
					 * 获取输入消息，并解密
					 */
					ois = new ObjectInputStream(socket.getInputStream());
					byte[] str = (byte[]) ois.readObject();
					byte[] str2 = AESUtil.decrypt(str);
					String str1 = new String(str2, "iso-8859-1");

					/**
					 * 判断是否为正确密钥，是则继续执行，否则返回错误密钥的消息
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
							 * 根据配置文件，判断是否可以执行操作，是则继续执行，否则返回拒绝执行的消息
							 */
							if (Agent.config.get(msg.getType().toString())
									.equals("on"))
								result = "downloading";
							else
								result = "0x0700001";

							/**
							 * 输出正在下载的消息，并加密
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
							 * 写入密钥验证成功的记录到日志
							 */
							XMLRecord.write(opID, time, command, "right key");

							// 检测磁盘空间
							int freeSpace = Integer.valueOf(values[2]);
							double currentSpace = 0;

							Process p = rt.exec("df -hl");// df -hl 查看硬盘空间

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
								 * 向服务器监听进度程序发送安装结果的消息，并加密
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
								 * 写入安装结果的记录到日志
								 */
								date = new Date(System.currentTimeMillis());
								time = date.toString();
								System.out.println("XML record "
										+ XMLRecord.write(opID, time, command,
												result));

							} else {

								if (result.equals("downloading")) {

									/**
									 * 写入开始执行的记录到日志
									 */
									date = new Date(System.currentTimeMillis());
									time = date.toString();
									XMLRecord.write(opID, time, command,
											"start executing");
									System.out.println("start download");

									/**
									 * 写入正在下载的记录到日志
									 */
									date = new Date(System.currentTimeMillis());
									time = date.toString();
									XMLRecord
											.write(opID, time, command, result);

									/**
									 * 进行下载，判断是否已存在，若是，则不再下载，否则，下载
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
									 * 向服务器监听进度程序发送下载完成的消息，并加密
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
									 * 写入下载完成的记录到日志
									 */
									date = new Date(System.currentTimeMillis());
									time = date.toString();
									XMLRecord.write(opID, time, command,
											"download completed");

									Thread.sleep(3000);

									/**
									 * 根据不同的消息类型，进行不同的处理
									 */
									/**
									 * 更新Tomact
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
									 * 更新Mysql
									 */
									else if (msg.getType().equals(
											MsgType.updateMySql)) {
										System.out.println("updateing MySql");
										result = lau.updateMysql(name[0],
												name[1],values[4],values[3]);

									}
									/**
									 * 更新Apache
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
									 * 更新Nginx
									 */
									else if (msg.getType().equals(
											MsgType.updateNginx)) {
										System.out.println("updateing Nginx");
										result = lau.updateNginx(values[1],
												values[4],values[3]);
									}
									/**
									 * 更新ZendGuardLoader
									 */
									else if (msg.getType().equals(
											MsgType.updateZendGuardLoader)) {
										System.out
												.println("updateing ZendGuardLoader");
										result = lau.updateZendGuardLoader(
												values[1], values[4],values[5]);
									}
									/**
									 * 更新Python
									 */
									else if (msg.getType().equals(
											MsgType.updatePython)) {
										System.out.println("updateing Python");
										result = lau.updatePython(values[1],
												values[3], values[4]);
									}
									/**
									 * 更新Memcached
									 */
									else if (msg.getType().equals(
											MsgType.updateMemcached)) {
										System.out
												.println("updateing Memcached");
										result = lau.updateMemcached(values[1],
												values[4], values[3]);
									}
									/**
									 * 更新FTP
									 */
									else if (msg.getType().equals(
											MsgType.updateFTP)) {
										System.out.println("updateing FTP");
										result = lau.updateFTP(values[1],values[4],values[3]);
									}

									/**
									 * 向服务器监听进度程序发送升级结果的消息，并加密
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
								 * 写入升级结果的记录到日志
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
							 * 向服务器监听进度程序发送升级结果的消息，并加密
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
							 * 写入升级结果的记录到日志
							 */
							date = new Date(System.currentTimeMillis());
							time = date.toString();
							System.out.println("XML record "
									+ XMLRecord.write(opID, time, command,
											result));
						}
					} else {
						/**
						 * 输出密钥错误的消息，并加密
						 */
						oos = new ObjectOutputStream(socket.getOutputStream());
						Message outMsg = new Message(MsgType.errorKey, "1",
								"0x0600001");
						String datatemp = SerializeUtil.serialize(outMsg);
						byte[] outStr = AESUtil.encrypt(datatemp);
						oos.writeObject(outStr);
						oos.flush();

						/**
						 * 写入密钥错误的记录到日志
						 */
						Date date = new Date(System.currentTimeMillis());
						String time = date.toString();
						XMLRecord.write("null", time, "null", "error key");
					}

					/**
					 * 正在执行的操作数减1
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
