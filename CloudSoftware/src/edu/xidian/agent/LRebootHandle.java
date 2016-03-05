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
 * @Description: linux��,��������
 * @author: wangyannan
 * @date: 2015-1-19 ����9:10:11
 */
public class LRebootHandle {

	/**
	 * @Title:LRebootHandle
	 * @Description:�����߳�
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
					 * д�뿪ʼִ�еļ�¼����־
					 */
					date = new Date(System.currentTimeMillis());
					time = date.toString();
					XMLRecord.write(opID, time, command, "ready to reboot");

					/**
					 * �鿴����ִ�еĲ������Ƿ�Ϊ0��Ϊ0����������Ϊ0����һֱ�ȴ���ֱ������ִ�еĲ�����Ϊ0
					 */
					while (Agent.opNum != 0)
						;

					/**
					 * д�뼴�������ļ�¼����־
					 */
					date = new Date(System.currentTimeMillis());
					time = date.toString();
					XMLRecord.write(opID, time, command, "reboot at once");

					/**
					 * ��������
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
