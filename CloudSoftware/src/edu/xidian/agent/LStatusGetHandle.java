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
 * @Description: 获取下载进度
 * @author: wangyannan
 * @date: 2015-1-17 下午12:08:55
 */
public class LStatusGetHandle {

	/**
	 * @Title:LStatusGetHandle
	 * @Description:获取下载进度线程
	 * @param socket
	 */
	public LStatusGetHandle(final Socket socket) {
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
						String opID = msg.getopID();
						String command = msg.getType().toString();
						String fileName = (String) msg.getValues();
						System.out.println("getting  " + fileName
								+ " download status");

						/**
						 * 写入密钥验证成功的记录到日志
						 */
						Date date = new Date(System.currentTimeMillis());
						String time = date.toString();
						XMLRecord.write(opID, time, command, "right key");

						/**
						 * 写入开始执行的记录到日志
						 */
						date = new Date(System.currentTimeMillis());
						time = date.toString();
						XMLRecord.write(opID, time, command, "start executing");

						/**
						 * 获取文件大小
						 */
						String storePath = new String("/home/softsource/");
						File file = new File("/home/softsource/" + fileName);
						String path;

						if (storePath.endsWith("\\") || storePath.endsWith("/"))// 给出的路径下新建一个临时文件夹，里面存储的是各个线程下载的文件
						{
							path = storePath
									+ fileName.substring(0,
											fileName.indexOf(".")) + "Temp/";// 临时文件夹的名字
																				// 绝对路径
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
						 * length以KB为单位
						 */
						long length = (fl + dl) / 1024 / 2;

						/**
						 * 输出信息，并加密
						 */
						oos = new ObjectOutputStream(socket.getOutputStream());
						Message outMsg = new Message(msg.getType(), opID,
								Long.toString(length));
						String datatemp = SerializeUtil.serialize(outMsg);
						byte[] outStr = AESUtil.encrypt(datatemp);
						oos.writeObject(outStr);
						oos.flush();

						/**
						 * 写入执行结果的记录到日志
						 */
						date = new Date(System.currentTimeMillis());
						time = date.toString();
						XMLRecord.write(opID, time, command,
								Long.toString(length));
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
