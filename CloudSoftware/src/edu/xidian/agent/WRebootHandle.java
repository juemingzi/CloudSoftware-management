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
 * @Description: windows��,��������
 * @author: wangyannan
 * @date: 2015-1-19 ����8:29:05
 */
public class WRebootHandle {

	/**
	 * @Title:WRebootHandle
	 * @Description:�����߳�
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
					 * д�뿪ʼִ�еļ�¼����־
					 */
					date = new Date(System.currentTimeMillis());
					time = date.toString();
					XMLRecord.write(opID, time, command, "ready to reboot");

					/**
					 * �鿴����ִ�еĲ������Ƿ�Ϊ0��Ϊ0����������Ϊ0����һֱ�ȴ���ֱ������ִ�еĲ�����Ϊ0
					 */
					while (Agent.opNum != 0)
					{
						System.out.println("reboot opNUm "+Agent.opNum);;
					}
					System.out.println("while reboot opNum "+Agent.opNum);	

					/**
					 * д�뼴�������ļ�¼����־
					 */
					date = new Date(System.currentTimeMillis());
					time = date.toString();
					XMLRecord.write(opID, time, command, "reboot at once");

					/**
					 * ��������
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
