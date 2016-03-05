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

import edu.xidian.message.AESUtil;
import edu.xidian.message.MD5Util;
import edu.xidian.message.Message;
import edu.xidian.message.MsgType;
import edu.xidian.message.SerializeUtil;
import edu.xidian.windows.WAppSetup;
import edu.xidian.windows.WAppUninstall;
import edu.xidian.DBOperation.*;
import edu.xidian.windows.*;

/**
 * @ClassName: WAppSetupHandle
 * @Description: windows上，根据不同的消息类型，安装不同的应用
 * @author: wangyannan
 * @date: 2014-11-13 下午9:30:10
 */
public class WAppSetupHandle {

	/**
	 * @Title:WAppSetupHandle
	 * @Description:安装应用线程
	 * @param socket
	 */
	public WAppSetupHandle(final Socket socket) {
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
					 * 获取输入消息，并解密
					 */

					String ip = socket.getInetAddress().getHostAddress();
					
					
					if (socket == null)
						System.out.println("socket =====================");
					System.out.println("socket =====================");
					ois = new ObjectInputStream(socket.getInputStream());
					byte[] str = (byte[]) ois.readObject();
					byte[] str2 = AESUtil.decrypt(str);
					String str1 = new String(str2, "iso-8859-1");

					/**
					 * 判断是否为正确密钥，是则继续执行，否则返回错误密钥的消息
					 */
					if (!AESUtil.isErrorKey(str1)) {
						Message msg = (Message) SerializeUtil.deserialize(str1);
						String[] values = (String[]) msg.getValues();
						String command = msg.getType().toString();
						String opID = msg.getopID();
						String result = new String();

						String filePath = values[4];
						File f = new File(filePath.substring(0, 2));
						System.out.println(filePath.substring(0, 2));

						if (f.exists()) {
							/**
							 * 根据配置文件，判断是否可以执行操作，是则继续执行，否则返回拒绝执行的消息
							 */
							if (Agent.config.get(msg.getType().toString())
									.equals("on"))
								result = "downloading";
							else
								result = "0x0700001";

							System.out.println("result " + result);
							/**
							 * 输出正在执行的消息，并加密
							 */
							oos = new ObjectOutputStream(socket
									.getOutputStream());
							Message outMsg = new Message(msg.getType(), msg
									.getopID(), result);
							String datatemp = SerializeUtil.serialize(outMsg);
							byte[] outStr = AESUtil.encrypt(datatemp);
							oos.writeObject(outStr);
							oos.flush();

							WAppSetup was = new WAppSetup();
							WAppUninstall wan = new WAppUninstall();

							Date date = new Date(System.currentTimeMillis());
							String time = date.toString();

							String[] name = new String[2];

							/**
							 * 写入密钥验证成功的记录到日志
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
								
								// setup jiance
								GetTotallSpace getzip = new GetTotallSpace();
								double setupzip = getzip.searchSetupForZip(sn) * 1.0 / 1024;
								getzip.close();

								// 检测下载空间是否足够
								double freeDownLoadSpace = Long
										.valueOf(values[3]) * 1.0 / 1024;
								// double currentDownLoadSpace = new
								// File(filePath
								// .substring(0, 2) + "\\").getFreeSpace() * 1.0
								// / 1024 / 1024;
								double currentDownLoadSpace = new File("C:\\")
										.getFreeSpace()
										* 1.0
										/ 1024
										/ 1024
										- setupzip;
								System.out
										.println("currentSpace  ###########   freespace :"
												+ currentDownLoadSpace
												+ "      " + freeDownLoadSpace);
								System.out
								.println("setupzip    @@@@@@@@@@@" + setupzip);

								if (currentDownLoadSpace < freeDownLoadSpace) {

									Thread.sleep(1000);

									result = "0x1000001";// ##########################
									/**
									 * 向服务器监听进度程序发送安装结果的消息，并加密
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
									 * 写入安装结果的记录到日志
									 */
									date = new Date(System.currentTimeMillis());
									time = date.toString();
									System.out.println("XML record "
											+ XMLRecord.write(opID, time,
													command, result));

								} else {

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
									if (values[1].contains(",")) {
										name = values[1].split(",");
										
											FtpTransFile
													.fileDownload("windows",
															name[0],
															"C:\\softsource",
															values[0], "test",
															"123456");
										
											FtpTransFile
													.fileDownload("windows",
															name[1],
															"C:\\softsource",
															values[0], "test",
															"123456");
									} else {
										
											FtpTransFile
													.fileDownload("windows",
															values[1],
															"C:\\softsource",
															values[0], "test",
															"123456");
									}
									System.out.println("end download");

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

									Thread.sleep(1000);

									String path = null;
									// 检测磁盘空间
									double freeSpace = Long.valueOf(values[2]) * 1.0 / 1024;

									// 截取安装路径

									int ll = msg.getType().toString().length();
									String snn = null;
									if(msg.getType().toString().contains("Interface"))
										snn = msg.getType().toString().substring(5, ll-9);
									else 
										snn = msg.getType().toString().substring(5, ll);
									System.out.println(snn);
									
									String diskPath = values[4].split(":")[0];

									GetTotallSpace getspace = new GetTotallSpace();
									double setupspace = getspace
											.searchSetupForSpace(diskPath + ":",snn) * 1.0 / 1024;
									getspace.close();
									System.out
									.println("setupspace    @@@@@@@@@@@" + setupspace);
									double currentSpace = new File(diskPath
											+ ":\\").getFreeSpace()
											* 1.0 / 1024 / 1024 - setupspace;

									if (currentSpace < freeSpace) {
										System.out
												.println("currentSpace  ###########   freespace :"
														+ currentSpace
														+ "      " + freeSpace);
										result = "0x1000002";// ###########################################

									} else {

										/**
										 * 根据不同的消息类型，进行不同的处理
										 */
										/**
										 * 安装Tomact
										 */
										if (msg.getType().equals(
												MsgType.setupTomcat)) {
											System.out
													.println("setuping Tomact");
											for (int i = 0; i < values.length; i++)
												System.out.println(values[i]);
											result = was.setupTomcat(values[1],
													values[4], values[5],
													values[6]);
										}

										/**
										 * 安装Mysql
										 */
										else if (msg.getType().equals(
												MsgType.setupMySql)) {
											System.out
													.println("setuping MySql");
											result = was.setupMySql(values[1],
													values[4], values[5],
													values[6]);

										}
										/**
										 * 安装Apache
										 */
										else if (msg.getType().equals(
												MsgType.setupApache)) {
											System.out
													.println("setuping Apache");
											result = was.setupApache(values[1],
													values[4], values[5],
													values[6]);
										}
										/**
										 * 安装Nginx
										 */
										else if (msg.getType().equals(
												MsgType.setupNginx)) {

											System.out
													.println("setuping Nginx");
											result = was.setupNginx(values[1],
													values[4], values[5]);
										}
										/**
										 * 安装ZendGuardLoader
										 */
										else if (msg.getType().equals(
												MsgType.setupZendGuardLoader)) {

											System.out
													.println("setuping ZendGuardLoader");
											result = was.setupZendGuardLoader(
													values[1], values[4],
													values[5]);
										}
										/**
										 * 安装Python
										 */
										else if (msg.getType().equals(
												MsgType.setupPython)) {

											System.out
													.println("setuping Python");
											result = was.setupPython(values[1],
													values[4], values[5]);
										}
										/**
										 * 安装Memcached
										 */
										else if (msg.getType().equals(
												MsgType.setupMemcached)) {

											System.out
													.println("setuping Memcached");
											result = was.setupMemcached(
													values[1], values[4],
													values[5]);
										}
										/**
										 * 安装IISRewrite
										 */
										else if (msg.getType().equals(
												MsgType.setupIISRewrite)) {

											System.out
													.println("setuping IISRewrite");
											result = was.setupiisRewrite(
													values[1], values[4],
													values[5]);
										}

										/**
										 * 安装FTP
										 */
										else if (msg.getType().equals(
												MsgType.setupFTP)) {

											System.out.println("setuping FTP");
											result = was.setupFTP(values[1],
													values[4], values[5]);
										}

										/**
										 * 安装SQLServer2008R2
										 */
										else if (msg.getType().equals(
												MsgType.setupSQLServer2008R2)) {
											System.out
													.println("setuping SQLServer2008R2");
											result = was.setupSQLServer2008R2(
													values[1], values[4],
													values[5], values[6],
													values[7], values[8]);
										}

										/**
										 * 安装Oracle11g
										 */
										else if (msg.getType().equals(
												MsgType.setupOracle11g)) {
											System.out
													.println("setuping Oracle11g");
											result = was.setupOracle(name[0],
													name[1], values[6],
													values[5], values[4],
													values[7], values[8],values[9]);
										}
										/**
										 * 安装360
										 */
										else if (msg.getType().equals(
												MsgType.setup360)) {
											System.out.println("setuping 360");
											result = was.setup360(values[1],
													values[4], values[5]);
										}

										/**
										 * 界面安装SQLServer2008R2
										 */
										else if (msg
												.getType()
												.equals(MsgType.setupSQLServer2008R2Interface)) {
											result = "setupsql";
											System.out
													.println("setuping SQLServer2008R2 Interface");

											if (values[8] != null) {
												if (!values[8].equals("null")) {
													if (!new WAppUninstall()
															.uninstallSQLServer2008R2(
																	values[8])
															.equals("0x0200A00")) {
														result = "0x0200A01";
													}
												}
											}
											if (!result.equals("0x0200A01")) {
												/**
												 * 向界面安装监听程序发送请求
												 */
												Socket socketOracle = new Socket(
														"127.0.0.1", 9900);
												String[] oracleValues = new String[6];
												oracleValues[0] = "sqlserver";
												oracleValues[1] = values[1];
												oracleValues[2] = values[4];
												oracleValues[3] = values[5];
												oracleValues[4] = values[6];
												oracleValues[5] = values[7];
												ObjectOutputStream ooosOracle = new ObjectOutputStream(
														socketOracle
																.getOutputStream());
												ooosOracle
														.writeObject(oracleValues);
												ObjectInputStream ooisOracle = new ObjectInputStream(
														socketOracle
																.getInputStream());
												result = (String) ooisOracle
														.readObject();
												ooosOracle.close();
												ooisOracle.close();
												socketOracle.close();
											}
										}

										/**
										 * 界面安装Oracle11g
										 */
										else if (msg
												.getType()
												.equals(MsgType.setupOracle11gInterface)) {
											System.out
													.println("setuping Oracle11g Interface");
											result = "aaa";
											if (values[9] != null) {
												if (!values[9].equals("null")) {
													if (!new WAppUninstall()
															.uninstallOracle11g(
																	values[9])
															.equals("0x0200C00")) {
														result = "0x0200C03";
													}
													try {
														Thread.sleep(3000);
														WVMScriptExecute wexe = new WVMScriptExecute();
														wexe.executeVMScript("shutdown -r");
													} catch (InterruptedException e) {
														// TODO Auto-generated catch block
														e.printStackTrace();
													}
												}
											}
											if (!result.equals("0x0200C03")) {
												/**
												 * 向界面安装监听程序发送请求
												 */
												Socket socketOracle = new Socket(
														"127.0.0.1", 9900);
												String[] oracleValues = new String[8];
												oracleValues[0] = "oracle";
												oracleValues[1] = name[0];
												oracleValues[2] = name[1];
												oracleValues[3] = values[4];
												oracleValues[4] = values[5];
												oracleValues[5] = values[6];
												oracleValues[6] = values[7];
												oracleValues[7] = values[8];
												ObjectOutputStream ooosOracle = new ObjectOutputStream(
														socketOracle
																.getOutputStream());
												ooosOracle
														.writeObject(oracleValues);
												ObjectInputStream ooisOracle = new ObjectInputStream(
														socketOracle
																.getInputStream());
												result = (String) ooisOracle
														.readObject();
												ooosOracle.close();
												ooisOracle.close();
												socketOracle.close();
											}
										}

										/**
										 * 判断是否是界面安装错误
										 */

										if (result.contains(",")) {
											path = result.split(",")[1];
											result = result.split(",")[0];
										}
									}

									/**
									 * 向服务器监听进度程序发送安装结果的消息，并加密
									 */
									
									System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%ip is ===="+ip);
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

									/**
									 * 若是界面安装错误，则还需返回图片信息
									 */
									if (path != null)
										PicOutput.output(path,ip);

									System.out.println("app setup " + result);

									/**
									 * 写入安装结果的记录到日志
									 */
									date = new Date(System.currentTimeMillis());
									time = date.toString();
									System.out.println("XML record "
											+ XMLRecord.write(opID, time,
													command, result));
								}
							}
						} else {
							result = "0x1200001";
							/**
							 * 向服务器监听进度程序发送安装结果的消息，并加密
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
							 * 写入安装结果的记录到日志
							 */
							Date date = new Date(System.currentTimeMillis());
							String time = date.toString();
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
