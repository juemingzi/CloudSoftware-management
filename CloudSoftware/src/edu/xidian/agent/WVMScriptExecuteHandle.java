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
 * @Description: windows上，虚拟机脚本执行
 * @author: wangyannan
 * @date: 2014-11-14 下午2:37:21
 */
public class WVMScriptExecuteHandle {

	/**
	 * @Title:WVMScriptExecuteHandle
	 * @Description:虚拟机脚本执行线程
	 * @param msg
	 */
	public WVMScriptExecuteHandle(final Socket socket) {
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
					 * 获取输入信息，并解密
					 */
					String fullPath = "";
					ois = new ObjectInputStream(socket.getInputStream());
					byte[] str = (byte[]) ois.readObject();
					byte[] str2 = AESUtil.decrypt(str);
					String str1 = new String(str2, "iso-8859-1");

					/**
					 * 判断是否为正确密钥，是则继续执行，否则返回错误密钥的消息
					 */
					if (!AESUtil.isErrorKey(str1)) {
						Message msg = (Message) SerializeUtil.deserialize(str1);
						String status = new String();

						/**
						 * 根据配置文件，判断是否可以执行操作，是则继续执行，否则返回拒绝执行的消息
						 */
						if (Agent.config.get(msg.getType().toString()).equals(
								"on"))
							status = "executing";
						else
							status = "0x0700001";

						/**
						 * 输出正在执行的状态，并加密
						 */
						Message outMsg = new Message(MsgType.executeVMScript,
								msg.getopID(), status);
						oos = new ObjectOutputStream(socket.getOutputStream());
						String datatemp = SerializeUtil.serialize(outMsg);
						byte[] outStr = AESUtil.encrypt(datatemp);
						oos.writeObject(outStr);
						oos.flush();

						/**
						 * 获取输入信息的操作ID，消息类型
						 */
						String opID = msg.getopID();
						String command = msg.getType().toString();
						Date date = new Date(System.currentTimeMillis());
						String time = date.toString();
						String[] result = new String[2];

						/**
						 * 写入密钥验证成功的记录到日志
						 */
						XMLRecord.write(opID, time, command, "right key");

						if (status.equals("executing")) {

							/**
							 * 写入开始执行的记录到日志
							 */
							date = new Date(System.currentTimeMillis());
							time = date.toString();
							XMLRecord.write(opID, time, command,
									"start executing");

							/**
							 * 写入正在执行的记录到日志
							 */
							date = new Date(System.currentTimeMillis());
							time = date.toString();
							XMLRecord.write(opID, time, command, status);

							/**
							 * 读入脚本，写入新脚本文件
							 */
							// 获得文件名
							String fileName = (String) msg.getValues();
							fullPath = "C:\\scripts\\" + fileName;
							// 接收文件
							DataOutputStream fileOut = new DataOutputStream(
									new FileOutputStream(fullPath));
							DataInputStream inputStream = new DataInputStream(
									new BufferedInputStream(socket
											.getInputStream()));
							// 缓冲区大小
							int bufferSize = 8192;
							byte[] buf = new byte[bufferSize];
							long passedlen = 0;
							long len = 0;
							// 获取文件长度
							len = inputStream.readLong();
							System.out.println("文件的长度为:" + len + "  B");
							System.out.println("开始接收文件!");
							// 获取文件
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
								System.out.println("文件接收了"
										+ (passedlen * 100 / len) + "%");

								fileOut.write(buf, 0, read);

								if ((passedlen * 100 / len) == 100) {
									f = false;
								}
							}
							System.out.println("接收完成，文件存为" + fullPath);
							fileOut.close();// 关闭文件流

							/**
							 * 执行脚本
							 */
							WVMScriptExecute wvmse = new WVMScriptExecute();
							System.out.println("executing script " + fullPath);

							result = wvmse.executeVMScript(fullPath);

							/**
							 * 向服务器监听进度程序发送安装结果的消息，并加密
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
							 * 写入安装结果的记录到日志
							 */
							date = new Date(System.currentTimeMillis());
							time = date.toString();
							System.out.println("XML record "
									+ XMLRecord.write(opID, time, command,
											result[0] + "&&" + result[1]));

						} else {
							/**
							 * 写入拒绝执行的结果的记录到日志
							 */
							date = new Date(System.currentTimeMillis());
							time = date.toString();
							System.out.println("XML record "
									+ XMLRecord.write(opID, time, command,
											status));

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
