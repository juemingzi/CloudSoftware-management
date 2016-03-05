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

import edu.xidian.linux.LOSConfig;
import edu.xidian.linux.LOSGet;
import edu.xidian.message.AESUtil;
import edu.xidian.message.MD5Util;
import edu.xidian.message.Message;
import edu.xidian.message.MsgType;
import edu.xidian.message.SerializeUtil;

/**
 * @ClassName: LOSConfigHandle
 * @Description: linux上，根据不同的消息类型，进行不同的处理
 * @author: wangyannan
 * @date: 2014-11-13 下午9:11:51
 */
public class LOSConfigHandle {

	/**
	 * @Title:LOSConfigHandle
	 * @Description:处理线程
	 * @param msg
	 */
	public LOSConfigHandle(final Socket socket) {
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
						LOSConfig loc = new LOSConfig();
						
						LOSGet  lg = new LOSGet();
						
						String command = msg.getType().toString();
						String opID = msg.getopID();
						Date date = new Date(System.currentTimeMillis());
						String time = date.toString();
						String result = new String();

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
							 * 修改密码
							 */
							if (msg.getType().equals(MsgType.changePasswd)) {
								String[] userPasswd = (String[]) msg
										.getValues();
								System.out.println("changing " + userPasswd[0]
										+ " password");
								result = loc.changePasswd(userPasswd[0],
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
								result = loc.changeSecRule(rule[0], rule[1],
										rule[2]);
							}
							/**
							 * 开启服务
							 */
							else if (msg.getType().equals(MsgType.startService)) {
								String service = (String) msg.getValues();
								System.out.println("starting service "
										+ service);
								result = loc.startService(service);
							}
							/**
							 * 关闭服务
							 */
							else if (msg.getType().equals(MsgType.stopService)) {
								String service = (String) msg.getValues();
								System.out
										.println("stoping service " + service);
								result = loc.stopService(service);

							}
						
						
							/**
							 * 修改静态IP
							 */
							else if (msg.getType().equals(MsgType.changeIP)) {
								String[] values = (String[]) msg.getValues();
								System.out.println("changing IP " + values[0]);
								result = loc.changeIP(values[0], values[1],
										values[2]);
							}
//							/**
//							 * 修改附属IP
//							 */
//							else if (msg.getType().equals(MsgType.changeAffiIP)) {
//								String[] values = (String[]) msg.getValues();
//								System.out.println("changing IP " + values[0]);
//								result = loc.changeAffiIP(values[0], values[1],
//										values[2]);
//							}
							/**
							 * 修改ulimit
							 */
							else if (msg.getType().equals(MsgType.changeUlimit)) {
								String ulimit = (String) msg.getValues();
								System.out.println("changing ulimit " + ulimit);
								result = loc.changeUlimit(ulimit);
							}
							
							/**
							 * 获取ulimit
							 */
							else if (msg.getType().equals(MsgType.getUlimit)) {
								String ulimit = (String) msg.getValues();
								System.out.println("geting ulimit " + ulimit);
								result = lg.getUlimit();
							}
							/**
							 * 获取安全规则
							 */
							else if (msg.getType().equals(MsgType.getSecRule)) {
								String secrule = (String) msg.getValues();
								System.out.println("geting secrule " + secrule);
								result = lg.getSecRule();
							}
							/**
							 * 获取静态IP
							 */
							else if (msg.getType().equals(MsgType.getIP)) {
								String ip = (String) msg.getValues();
								System.out.println("getting ip " + ip);
								result = lg.getIP();
							}
							/**
							 * 获取附属one service
							 */
							else if (msg.getType().equals(MsgType.getServiceState)) {
								System.out.println("getting all service ");
								result = lg.getService();
							}
							/**
							 * 获取服务状态
							 */
							else if (msg.getType().equals(MsgType.getOneServiceState)) {
								String values = (String) msg.getValues();
								System.out.println("getting service " + values);
								result = lg.getServiceStatus(values);
							}
							
							
							
							/**
							 * 查询复数IP
							 */
							
							else if(msg.getType().equals(MsgType.searchAffiIP)){
								String[] values=(String[])msg.getValues();
								System.out.println("getting AffIP in "+values[0]);
								result=loc.findALLIPinNc(values[0]);
							}
							/**
							 * 增加复数IP
							 */
							else if(msg.getType().equals(MsgType.addAffiIP)){
								String[] values=(String[])msg.getValues();
								System.out.println("add AffIP in "+values[0]+" IP "+values[1]+" netMask "+values[2]);
								result=loc.addFSIP(values[0], values[1], values[2]);
							}
							
							/**
							 * 删除复数IP
							 */
							else if(msg.getType().equals(MsgType.deleteAffiIP)){
								String[] values=(String[])msg.getValues();
								System.out.println("delete AffIP "+values[1]+" in "+values[0]);
								result=loc.deleteFSIP(values[0], values[1]);
							}
							/**
							 * 修改复数IP
							 */
							else if(msg.getType().equals(MsgType.changeAffiIP)){
								String[] values=(String[])msg.getValues();
								System.out.println("change AffIP "+values[1]+" in "+values[0]+" to "+values[2]);
								result=loc.changeFSIP(values[0], values[1], values[2]);
							}
							
							
							
							
							/**
							 * 读取key
							 */
							else if(msg.getType().equals(MsgType.readkey)){
								String[] values=(String[])msg.getValues();
								System.out.println("read  key");
								String[] s = loc.createKeyPairs();
								result = "";
								result="Private Key = "+s[1]+"       Public Key = "+s[2];
							}
						}
						System.out.println("os configure " + result);

						/**
						 * 输出正在执行的消息，并加密
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
