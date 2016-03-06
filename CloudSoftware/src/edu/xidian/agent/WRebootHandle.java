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
import edu.xidian.windows.WOSConfig;

/**
 * @ClassName: WRebootHandle
 * @Description: windows上,重启处理
 * @author: wangyannan
 * @date: 2015-1-19 下午8:29:05
 */
public class WRebootHandle {

	/**
	 * @Title:WRebootHandle
	 * @Description:重启线程
	 * @param socket
	 */
	public WRebootHandle(final Message msg) {
		new Thread(new Runnable() {
			public void run() {
				try {
					String command = msg.getType().toString();
					String opID = msg.getopID();
					Date date = new Date(System.currentTimeMillis());
					String time = date.toString();

					/**
					 * 写入开始执行的记录到日志
					 */
					date = new Date(System.currentTimeMillis());
					time = date.toString();
					XMLRecord.write(opID, time, command, "ready to reboot");

					/**
					 * 查看正在执行的操作数是否为0，为0则重启，不为0，则一直等待，直到正在执行的操作数为0
					 */
					while (Agent.opNum != 0)
					{
						System.out.println("reboot opNUm "+Agent.opNum);;
					}
					System.out.println("while reboot opNum "+Agent.opNum);	

					/**
					 * 写入即将重启的记录到日志
					 */
					date = new Date(System.currentTimeMillis());
					time = date.toString();
					XMLRecord.write(opID, time, command, "reboot at once");

					/**
					 * 重启操作
					 */
					Process process = null;
					process = Runtime.getRuntime().exec("shutdown -r");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
}
