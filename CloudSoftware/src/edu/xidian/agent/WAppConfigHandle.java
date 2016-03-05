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
import edu.xidian.windows.WAppConfig;

/**
 * @ClassName: WAppConfigHandle
 * @Description: windows上，根据不同的消息类型，配置不同的应用
 * @author: wangyannan
 * @date: 2014-11-13 下午10:10:56
 */
public class WAppConfigHandle {

	/**
	 * @Title:WAppConfigHandle
	 * @Description:配置应用线程
	 * @param msg
	 */
	public WAppConfigHandle(final Socket socket) {
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
						WAppConfig wac = new WAppConfig();
						String command = msg.getType().toString();
						String opID = msg.getopID();
						Date date = new Date(System.currentTimeMillis());
						String time = date.toString();
						String[] values = (String[]) msg.getValues();

						/**
						 * 写入密钥验证成功的记录到日志
						 */
						XMLRecord.write(opID, time, command, "right key");

						/**
						 * 根据配置文件，判断是否可以执行操作，是则继续执行，否则返回拒绝执行的消息
						 */
						if (Agent.config.get(msg.getType().toString()).equals(
								"on"))
							result = "executing";
						else
							result = "0x0700001";

						if (result.equals("executing")) {

							/**
							 * 写入开始执行的记录到日志
							 */
							date = new Date(System.currentTimeMillis());
							time = date.toString();
							XMLRecord.write(opID, time, command,
									"start executing");

							/**
							 * 根据不同的消息类型，进行不同的处理
							 */
							/**
							 * 配置Tomact
							 */
							if (msg.getType().equals(MsgType.configTomcat)) {
								System.out.println("configuring Tomcat "
										+ values[1] + " " + values[2]);
								result = wac.configTomcat(values[0], values[1],
										values[2]);
							}
						
							/**
							 * 配置Mysql
							 */
							else if (msg.getType().equals(MsgType.configMySql)) {
								System.out.println("configuring MySql "
										+ values[1] + " " + values[2]);
								result = wac.configMySQL(values[0], values[1],
										values[2]);

							}
							/**
							 * 配置Apache
							 */
							else if (msg.getType().equals(MsgType.configApache)) {
								System.out.println("configuring Apache "
										+ values[1] + " " + values[2]);
								result = wac.configApache(values[0], values[1],
										values[2]);
							}
							/**
							 * 配置Nginx
							 */
							else if (msg.getType().equals(MsgType.configNginx)) {
								System.out.println("configuring Nginx "
										+ values[1] + " " + values[2]);
								result = wac.configNginx(values[0], values[1],
										values[2]);
							}
							/**
							 * 配置ZendGuardLoader
							 */
							else if (msg.getType().equals(
									MsgType.configZendGuardLoader)) {
								System.out
										.println("configuring ZendGuardLoader "
												+ values[1] + " " + values[2]);
								result = wac.configZendGuardLoader(values[0],
										values[1], values[2]);
							}
							/**
							 * 获取Tomact配置项
							 */
							else if (msg.getType().equals(
									MsgType.getTomcatConfig)) {
								System.out.println("getting Tomcat config");
								result = wac.getTomcatConfig(values[0],
										values[1]);
							}
							/**
							 * 获取Mysql配置项
							 */
							else if (msg.getType().equals(
									MsgType.getMySqlConfig)) {
								System.out.println("getting Mysql config");
								result = wac.getMySqlConfig(values[0],
										values[1]);

							}
							/**
							 * 获取Apache配置项
							 */
							else if (msg.getType().equals(
									MsgType.getApacheConfig)) {
								System.out.println("getting Apache config");
								result = wac.getApacheConfig(values[0],
										values[1]);
							}
							/**
							 * 获取Nginx配置项
							 */
							else if (msg.getType().equals(
									MsgType.getNginxConfig)) {
								System.out.println("getting Nginx config");
								result = wac.getNginxConfig(values[0],
										values[1]);
							}
							/**
							 * 获取ZendGuardLoader配置项
							 */
							else if (msg.getType().equals(
									MsgType.getZendGuardLoaderConfig)) {
								System.out
										.println("getting ZendGuardLoader config");
								result = wac
										.getZendGuardLoaderConfig(values[0]);
							}
						}
						System.out.println("app configure " + result);

						/**
						 * 输出操作执行的状态，并加密
						 */
						oos = new ObjectOutputStream(socket.getOutputStream());
						Message outMsg = new Message(msg.getType(), msg
								.getopID(), result);
						String datatemp = SerializeUtil.serialize(outMsg);
						byte[] outStr = AESUtil.encrypt(datatemp);
						oos.writeObject(outStr);
						oos.flush();

						/**
						 * 写入执行结果的记录到日志
						 */
						date = new Date(System.currentTimeMillis());
						time = date.toString();
						System.out.println("XML record "
								+ XMLRecord.write(opID, time, command, result));
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
