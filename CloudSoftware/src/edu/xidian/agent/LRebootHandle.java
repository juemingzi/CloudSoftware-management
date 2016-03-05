package edu.xidian.agent;

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
 * @ClassName: LRebootHandle
 * @Description: linux上,重启处理
 * @author: wangyannan
 * @date: 2015-1-19 下午9:10:11
 */
public class LRebootHandle {

	/**
	 * @Title:LRebootHandle
	 * @Description:重启线程
	 * @param socket
	 */
	public LRebootHandle(final Message msg) {
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
						;

					/**
					 * 写入即将重启的记录到日志
					 */
					date = new Date(System.currentTimeMillis());
					time = date.toString();
					XMLRecord.write(opID, time, command, "reboot at once");

					/**
					 * 重启操作
					 */
					Runtime rt = Runtime.getRuntime();
					String[] cmds = new String[] { "/bin/sh", "-c", "reboot" };
					Process proc = rt.exec(cmds);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
}
