package edu.xidian.agent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
//import java.util.HashSet;
import java.util.Properties;

import edu.xidian.DBOperation.SearchForSCS;
import edu.xidian.linux.LVMScriptExecute;
import edu.xidian.message.AESUtil;
import edu.xidian.message.Message;
import edu.xidian.message.MsgType;
import edu.xidian.message.SerializeUtil;
import edu.xidian.windows.WVMScriptExecute;

/**
 * @ClassName: Agent
 * @Description: �������
 * @author: wangyannan
 * @date: 2014-11-11 ����8:51:09
 */
public class Agent {
	/**
	 * @fieldName: OSCONFIG_PORT
	 * @fieldType: int
	 * @Description: �������������̼߳����Ķ˿�
	 */
	private final int OSCONFIG_PORT = 9000;
	/**
	 * @fieldName: APPSETUP_PORT
	 * @fieldType: int
	 * @Description: Ӧ����������̼߳����Ķ˿�
	 */
	private final int APPSETUP_PORT = 9100;
	/**
	 * @fieldName: APPCONFIG_PORT
	 * @fieldType: int
	 * @Description: Ӧ����������̼߳����Ķ˿�
	 */
	private final int APPCONFIG_PORT = 9200;
	/**
	 * @fieldName: APPUPDATE_PORT
	 * @fieldType: int
	 * @Description: Ӧ����������̼߳����Ķ˿�
	 */
	private final int APPUPDATE_PORT = 9300;
	/**
	 * @fieldName: VMSCRIPTEXECUTE_PORT
	 * @fieldType: int
	 * @Description: ������ű�ִ���̼߳����Ķ˿�
	 */
	private final int VMSCRIPTEXECUTE_PORT = 9400;
	/**
	 * @fieldName: STATUSGET_PORT
	 * @fieldType: int
	 * @Description: ����״̬��ȡ�̼߳����Ķ˿�
	 */
	private final int STATUSGET_PORT = 9500;
	/**
	 * @fieldName: APPUNINSTALL_PORT
	 * @fieldType: int
	 * @Description: Ӧ�����ж���̼߳����Ķ˿�
	 */
	private final int APPUNINSTALL_PORT = 9600;

	/**
	 * @fieldName: REBOOT_PORT
	 * @fieldType: int
	 * @Description: �����̼߳����Ķ˿�
	 */
	private final int REBOOT_PORT = 9700;
	/**
	 * @fieldName: os
	 * @fieldType: int
	 * @Description: ��ǰ����ϵͳ�����ͣ�windows��ʾΪ0��linux��ʾΪ1
	 */
	private int os = -1;
	/**
	 * config�����ļ��е�ӳ���ϵ
	 */
	static HashMap<String, String> config = new HashMap<String, String>();
	/**
	 * ����ִ�еĲ�������
	 */
	static int opNum = 0;

	/**
	 * @Title: getCurrentOS
	 * @Description: ��ȡ��ǰ����ϵͳ����
	 * @return: void
	 */
	public void getCurrentOS() {
		Properties prop = System.getProperties();
		String osName = prop.getProperty("os.name");
		System.out.println("��ǰ����ϵͳ�ǣ�" + osName.toLowerCase());
		if (osName.toLowerCase().contains("windows")) {
			os = 0;
		} else {
			os = 1;
		}
	}

	/**
	 * @Title: startThread
	 * @Description: �������ж˿��ϵļ����߳�
	 * @return: void
	 */
	public void startThread() {
		System.out.println("�������м���...");

		/**
		 * ���������������ü����߳�
		 */
		new Thread(new Runnable() {
			public void run() {
				try {
					osConfigListen();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

		/**
		 * ����Ӧ�������������߳�
		 */
		new Thread(new Runnable() {
			public void run() {
				try {

					appSetupListen();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

		/**
		 * ����Ӧ��������ü����߳�
		 */
		new Thread(new Runnable() {
			public void run() {
				try {

					appConfigListen();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

		/**
		 * ����Ӧ��������¼����߳�
		 */
		new Thread(new Runnable() {
			public void run() {
				try {

					appUpdateListen();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

		/**
		 * ����������ű�ִ�м����߳�
		 */
		new Thread(new Runnable() {
			public void run() {
				try {

					vmScriptExecuteListen();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

		/**
		 * ��������״̬��ȡ�����߳�
		 */
		new Thread(new Runnable() {
			public void run() {
				try {

					statusGetListen();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

		/**
		 * ����Ӧ�����ж�ؼ����߳�
		 */
		new Thread(new Runnable() {
			public void run() {
				try {
					appUninstallListen();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

		/**
		 * �������������߳�
		 */
		new Thread(new Runnable() {
			public void run() {
				try {
					rebootListen();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

		/**
		 * ����������Ĵ����߳�
		 */
		new Thread(new Runnable() {
			public void run() {
				try {
					afterReboot();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * @Title: afterReboot
	 * @Description: �������ִ�нű�����
	 * @return: void
	 */
	public void afterReboot() {
		Agent agent = new Agent();
		agent.getCurrentOS();
		try {
			File f = null;
			if(os == 0)
			{
				f = new File("C:/rebootscript.txt");
			}else if(os == 1){
				f = new File("/home/rebootscript.txt");
			}
//			File f = new File("C:/rebootscript.txt");
////			File f = new File("/home/rebootscript.txt");
			BufferedReader br = new BufferedReader(new FileReader(f));
			String path = new String();
			
			/**
			 * ִ�нű���ÿ�нű�·���ļ�
			 */
			while ((path = br.readLine()) != null) {
				System.out.println("after reboot, executing " + path);
				if (os == 0) {
					WVMScriptExecute wvmse = new WVMScriptExecute();
					wvmse.executeVMScript(path);
				} else if (os == 1) {
					LVMScriptExecute lvmse = new LVMScriptExecute();
					lvmse.executeVMScript(path);
				}
			}
			br.close();
			f.delete();
			f.createNewFile();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @Title: appUninstallListen
	 * @Description: ������������
	 * @return: void
	 */
	public void rebootListen() {

		try {
			ServerSocket server = new ServerSocket(REBOOT_PORT);
			int i = 1;
			while (true) {
				Socket socket = server.accept();
				/**
				 * ��ȡ������Ϣ��������
				 */
				ObjectInputStream ois = new ObjectInputStream(
						socket.getInputStream());
				byte[] str = (byte[]) ois.readObject();
				byte[] str2 = AESUtil.decrypt(str);
				String str1 = new String(str2, "iso-8859-1");

				/**
				 * �ж��Ƿ�Ϊ��ȷ��Կ���������ִ�У����򷵻ش�����Կ����Ϣ
				 */
				if (!AESUtil.isErrorKey(str1)) {
					Message msg = (Message) SerializeUtil.deserialize(str1);
					String result = new String();

					/**
					 * д����Կ��֤�ɹ��ļ�¼����־
					 */
					Date date = new Date(System.currentTimeMillis());
					String time = date.toString();
					String command = msg.getType().toString();
					String opID = msg.getopID();
					XMLRecord.write(opID, time, command, "right key");

					/**
					 * ���������ļ����ж��Ƿ����ִ�в������������ִ�У����򷵻ؾܾ�ִ�е���Ϣ
					 */
					
					System.out.println("reboot:" + Agent.config.get(msg.getType().toString())+"****");
					if (Agent.config.get(msg.getType().toString()).equals("on"))
						result = "executing";
					else
						result = "0x0700001";

					if (result.equals("executing")) {
						/**
						 * ���յ���һ����������󣬾͵������������̣߳�֮����������󣬶����Ժ��ӣ��������գ�����������
						 */
						if (i == 1) {
							if (os == 0) {
								/**
								 * ����windows���������߳�
								 */
								new WRebootHandle(msg);

							} else if (os == 1) {
								/**
								 * ����Linux���������߳�
								 */
								new LRebootHandle(msg);
							}
							i++;
						} else {
							result = "already executing";
						}
					}

					/**
					 * ���ִ��״̬����Ϣ��������
					 */
					ObjectOutputStream oos = new ObjectOutputStream(
							socket.getOutputStream());
					Message outMsg = new Message(MsgType.reboot, msg.getopID(),
							result);
					System.out.println("reboot result "+result);
					String datatemp = SerializeUtil.serialize(outMsg);
					byte[] outStr = AESUtil.encrypt(datatemp);
					oos.writeObject(outStr);
					oos.flush();

					ois.close();
					oos.close();
					socket.close();

				} else {
					/**
					 * �����Կ�������Ϣ��������
					 */
					ObjectOutputStream oos = new ObjectOutputStream(
							socket.getOutputStream());
					Message outMsg = new Message(MsgType.errorKey, "1",
							"0x0600001");
					String datatemp = SerializeUtil.serialize(outMsg);
					byte[] outStr = AESUtil.encrypt(datatemp);
					oos.writeObject(outStr);
					oos.flush();

					/**
					 * д����Կ����ļ�¼����־
					 */
					Date date = new Date(System.currentTimeMillis());
					String time = date.toString();
					XMLRecord.write("null", time, "null", "error key");

					ois.close();
					oos.close();
					socket.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @Title: appUninstallListen
	 * @Description: Ӧ�����ж�ؼ�������
	 * @return: void
	 */
	public void appUninstallListen() {

		try {
			ServerSocket server = new ServerSocket(APPUNINSTALL_PORT);
			while (true) {
				Socket socket = server.accept();
				if (os == 0) {
					/**
					 * ����windowsӦ������������߳�
					 */
					new WAppUninstallHandle(socket);

				} else if (os == 1) {
					/**
					 * ����LinuxӦ������������߳�
					 */
					new LAppUninstallHandle(socket);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Title: statusGetListen
	 * @Description: ����״̬��ȡ��������
	 * @return: void
	 */
	public void statusGetListen() {
		try {
			ServerSocket server = new ServerSocket(STATUSGET_PORT);
			while (true) {
				Socket socket = server.accept();
				if (os == 0) {
					/**
					 * ����windows����״̬��ȡ�����߳�
					 */
					new WStatusGetHandle(socket);

				} else if (os == 1) {
					/**
					 * ����Linux����״̬��ȡ�����߳�
					 */
					new LStatusGetHandle(socket);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Title: vmScriptExecuteListen
	 * @Description: ������ű�ִ�м�������
	 * @return: void
	 */
	public void vmScriptExecuteListen() {
		try {
			ServerSocket server = new ServerSocket(VMSCRIPTEXECUTE_PORT);
			while (true) {
				Socket socket = server.accept();
				if (os == 0) {
					/**
					 * ����windows������ű�ִ�д����߳�
					 */
					new WVMScriptExecuteHandle(socket);

				} else if (os == 1) {
					/**
					 * ����linux������ű�ִ�д����߳�
					 */
					new LVMScriptExecuteHandle(socket);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @Title: appUpdateListen
	 * @Description: Ӧ��������¼�������
	 * @return: void
	 */
	public void appUpdateListen() {
		try {
			ServerSocket server = new ServerSocket(APPUPDATE_PORT);
			while (true) {
				Socket socket = server.accept();
				if (os == 0) {
					/**
					 * ����windowsӦ��������´����߳�
					 */
					new WAppUpdateHandle(socket);

				} else if (os == 1) {
					/**
					 * ����linuxӦ��������´����߳�
					 */
					new LAppUpdateHandle(socket);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @Title: appConfigListen
	 * @Description: Ӧ��������ü�������
	 * @return: void
	 */
	public void appConfigListen() {
		try {
			ServerSocket server = new ServerSocket(APPCONFIG_PORT);
			while (true) {
				Socket socket = server.accept();
				if (os == 0) {
					/**
					 * ����windowsӦ��������ô����߳�
					 */
					new WAppConfigHandle(socket);

				} else if (os == 1) {
					/**
					 * ����linuxӦ��������ô����߳�
					 */
					new LAppConfigHandle(socket);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @Title: appSetupListen
	 * @Description: Ӧ����������������
	 * @return: void
	 */
	public void appSetupListen() {

		try {
			ServerSocket server = new ServerSocket(APPSETUP_PORT);
			while (true) {
				Socket socket = server.accept();
				if (os == 0) {
					/**
					 * ����windowsӦ������������߳�
					 */
					new WAppSetupHandle(socket);

				} else if (os == 1) {
					/**
					 * ����LinuxӦ������������߳�
					 */
					new LAppSetupHandle(socket);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Title: osConfigListen
	 * @Description: �����������ü�������
	 * @return: void
	 * @throws ClassNotFoundException
	 */
	public void osConfigListen() {
		try {
			ServerSocket server = new ServerSocket(OSCONFIG_PORT);
			while (true) {
				Socket socket = server.accept();
				if (os == 0) {
					/**
					 * ����windows�����������ô����߳�
					 */
					new WOSConfigHandle(socket);

				} else if (os == 1) {
					/**
					 * ����linux�����������ô����߳�
					 */
					new LOSConfigHandle(socket);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	
	
	
/*
 * ÿ�������������񶼻��Զ�ִ��
 */
	public void readConfig() {
		File file = null;
		
		if (os == 0) {
			/**
			 * ����windows
			 */
			file = new File("C:/config.txt");

		} else if (os == 1) {
			/**
			 * ����linux
			 */
			file = new File("/home/config.txt");
		}
				
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				String[] line = (String[]) tempString.split(" ");
				config.put(line[0].trim(), line[1].trim());
			}
			System.out.println("read config success");
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
	}
//	public void readConfig() {
//		File file = new File("C:/config.txt");
////		File file = new File("/home/config.txt");
//		BufferedReader reader = null;
//		try {
//			reader = new BufferedReader(new FileReader(file));
//			String tempString = null;
//			while ((tempString = reader.readLine()) != null) {
//				String[] line = (String[]) tempString.split(" ");
//				config.put(line[0].trim(), line[1].trim());
//			}
//			System.out.println("read config success");
//			reader.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			if (reader != null) {
//				try {
//					reader.close();
//				} catch (IOException e1) {
//				}
//			}
//		}
//	}

	public void startException() throws Exception{
		SearchForSCS sf = new SearchForSCS();
		sf.searchSetup(os);
		sf.searchConfig(os);
		sf.searchScript(os);
		sf.close();
	}
	public void creatFile(){
		if(os == 0){
			File f1 = new File("C:\\scripts");
			File f2 = new File("C:\\setupscripts");
			File f3 = new File("C:\\softsource");
			File f4 = new File("C:\\uninstallscripts");
			File f5 = new File("C:\\preinstall");
			if(!f1.exists())
				f1.mkdirs();
			if(!f2.exists())
				f2.mkdirs();
			if(!f3.exists())
				f3.mkdirs();
			if(!f4.exists())
				f4.mkdirs();
			if(!f5.exists())
				f5.mkdirs();
		}else if(os == 1){
			File f1 = new File("/home/scripts");
			File f2 = new File("/home/setupscripts");
			File f3 = new File("/home/softsource");
			File f4 = new File("/home/uninstallscripts");
			File f5 = new File("/home/u01");
			File f6 = new File("/home/preinstall");
			if(!f1.exists())
				f1.mkdirs();
			if(!f2.exists())
				f2.mkdirs();
			if(!f3.exists())
				f3.mkdirs();
			if(!f4.exists())
				f4.mkdirs();
			if(!f5.exists())
				f5.mkdirs();
			if(!f6.exists())
				f6.mkdirs();
		}
	}
	/**
	 * @Title: main
	 * @Description: ������
	 * @param args
	 * @return: void
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Agent agent = new Agent();
		agent.getCurrentOS();
		agent.readConfig();
		agent.startThread();
		agent.creatFile();
		agent.startException();
	}

}
