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
 * @Description: windows上，根据不同的消息类型，进行不同的处理
 * @author: wangyannan
 * @date: 2014-11-12 下午5:03:47
 */
public class WOSConfigHandle {
	/**
	 * @Title:WOSConfigHandle
	 * @Description:处理线程
	 * @param socket
	 */
	public WOSConfigHandle(final Socket socket) {
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
						WOSConfig woc = new WOSConfig();
						AffiIP aff = new AffiIP();
						SystemConfigurations sc = new SystemConfigurations();

						String command = msg.getType().toString();
						String opID = msg.getopID();
						Date date = new Date(System.currentTimeMillis());
						String time = date.toString();
						String result = new String();

						/**
						 * 写入密钥验证成功的记录到日志
						 */
						XMLRecord.write(opID, time, command, "right key");
						System.out.println("the agent is $$$$$$$$$$"
								+ Agent.config.get(msg.getType().toString()));
						/**
						 * 根据配置文件，判断是否可以执行操作，是则继续执行，否则返回拒绝执行的消息
						 */
						System.out.println(msg.getType().toString());
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
							 * 修改密码
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
							 * 修改安全规则
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
							 * 开启服务
							 */
							else if (msg.getType().equals(MsgType.startService)) {
								String service = (String) msg.getValues();
								System.out.println("starting service "
										+ service);
								result = woc.startService(service);
							}
							/**
							 * 关闭服务
							 */
							else if (msg.getType().equals(MsgType.stopService)) {
								String service = (String) msg.getValues();
								System.out
										.println("stoping service " + service);
								result = woc.stopService(service);

							}
							/**
							 * 磁盘格式化
							 */
							else if (msg.getType().equals(MsgType.diskFormat)) {
								System.out.println("formating disk");
								result = woc.diskFormat();
							}
							/**
							 * 修改静态IP
							 */
							else if (msg.getType().equals(MsgType.changeIP)) {
								Object[] values = (Object[]) msg.getValues();
								System.out.println("changing IP ");
								result = woc.changeIP((String) values[0],
										(String) values[1], (String) values[2],
										(String[]) values[3]);
							}
//							/**
//							 * 修改附属IP
//							 */
//							else if (msg.getType().equals(MsgType.addAffiIP)) {
//								Object[] values = (Object[]) msg.getValues();
//								System.out.println("changing IP ");
//								result = woc.addAffiIP((String[]) values[0],
//										(String[]) values[1],
//										(String[]) values[2]);
//							}

							/**
							 * 获取安全规则
							 */
							else if (msg.getType().equals(MsgType.getSecRule)) {
								Object[] values = (Object[]) msg.getValues();
								System.out.println("getting secrule ");
								result = sc.getSystemSecRule();
							}
							/**
							 * 获取静态IP
							 */
							else if (msg.getType().equals(MsgType.getIP)) {
								Object[] values = (Object[]) msg.getValues();
								System.out.println("getting IP ");
								result = sc.getSystemIP();
							}
							// /**
							// * 获取附属IP
							// */
							// else if (msg.getType().equals(MsgType.getAffiIP))
							// {
							// Object[] values = (Object[]) msg.getValues();
							// System.out.println("changing IP ");
							// result = sc.getSystemaffiIP();
							// }
							/**
							 * 获取服务状态
							 */
							else if (msg.getType().equals(
									MsgType.getServiceState)) {
								Object[] values = (Object[]) msg.getValues();
								System.out.println("getting servicestate ");
								result = sc.getSystemServiceState();
							}
							/**
							 * 获取一个服务状态
							 */
							else if (msg.getType().equals(
									MsgType.getOneServiceState)) {
								String values = (String) msg.getValues();
								TestService ts = new TestService();
								System.out.println("getting service " + values);
								result = ts.getServiceState(values);
							}

							/**
							 * 查找附属IP
							 */
							else if (msg.getType().equals(MsgType.searchAffiIP)) {
								String[] values = (String[]) msg.getValues();
								System.out.println("getting ip" + values[0]);
								result = aff.getIPs(values[0]);

							}
							/**
							 * 修改附属IP
							 */
							else if (msg.getType().equals(MsgType.changeAffiIP)) {
								String[] values = (String[]) msg.getValues();
								System.out.println("changing IP" + values[1]
										+ " to " + values[2]);
								result = aff.changeIP(values[0], values[1],
										values[2], values[3]);
							}
							/**
							 * 增加附属IP
							 */
							else if (msg.getType().equals(MsgType.addAffiIP)) {
								String[] values = (String[]) msg.getValues();
								System.out.println("adding IP" + values[1]);
								result = aff.addIP(values[0], values[1],
										values[2]);
							}
							/**
							 * 删除附属IP
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
						 * 输出执行结果的消息，并加密
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
