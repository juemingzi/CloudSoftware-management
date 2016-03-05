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
 * @Description: 代理软件
 * @author: wangyannan
 * @date: 2014-11-11 下午8:51:09
 */
public class Agent {
	/**
	 * @fieldName: OSCONFIG_PORT
	 * @fieldType: int
	 * @Description: 基础环境配置线程监听的端口
	 */
	private final int OSCONFIG_PORT = 9000;
	/**
	 * @fieldName: APPSETUP_PORT
	 * @fieldType: int
	 * @Description: 应用软件部署线程监听的端口
	 */
	private final int APPSETUP_PORT = 9100;
	/**
	 * @fieldName: APPCONFIG_PORT
	 * @fieldType: int
	 * @Description: 应用软件配置线程监听的端口
	 */
	private final int APPCONFIG_PORT = 9200;
	/**
	 * @fieldName: APPUPDATE_PORT
	 * @fieldType: int
	 * @Description: 应用软件更新线程监听的端口
	 */
	private final int APPUPDATE_PORT = 9300;
	/**
	 * @fieldName: VMSCRIPTEXECUTE_PORT
	 * @fieldType: int
	 * @Description: 虚拟机脚本执行线程监听的端口
	 */
	private final int VMSCRIPTEXECUTE_PORT = 9400;
	/**
	 * @fieldName: STATUSGET_PORT
	 * @fieldType: int
	 * @Description: 操作状态获取线程监听的端口
	 */
	private final int STATUSGET_PORT = 9500;
	/**
	 * @fieldName: APPUNINSTALL_PORT
	 * @fieldType: int
	 * @Description: 应用软件卸载线程监听的端口
	 */
	private final int APPUNINSTALL_PORT = 9600;

	/**
	 * @fieldName: REBOOT_PORT
	 * @fieldType: int
	 * @Description: 重启线程监听的端口
	 */
	private final int REBOOT_PORT = 9700;
	/**
	 * @fieldName: os
	 * @fieldType: int
	 * @Description: 当前操作系统的类型，windows表示为0，linux表示为1
	 */
	private int os = -1;
	/**
	 * config配置文件中的映射关系
	 */
	static HashMap<String, String> config = new HashMap<String, String>();
	/**
	 * 正在执行的操作总数
	 */
	static int opNum = 0;

	/**
	 * @Title: getCurrentOS
	 * @Description: 获取当前操作系统类型
	 * @return: void
	 */
	public void getCurrentOS() {
		Properties prop = System.getProperties();
		String osName = prop.getProperty("os.name");
		System.out.println("当前操作系统是：" + osName.toLowerCase());
		if (osName.toLowerCase().contains("windows")) {
			os = 0;
		} else {
			os = 1;
		}
	}

	/**
	 * @Title: startThread
	 * @Description: 启动所有端口上的监听线程
	 * @return: void
	 */
	public void startThread() {
		System.out.println("启动所有监听...");

		/**
		 * 启动基础环境配置监听线程
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
		 * 启动应用软件部署监听线程
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
		 * 启动应用软件配置监听线程
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
		 * 启动应用软件更新监听线程
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
		 * 启动虚拟机脚本执行监听线程
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
		 * 启动操作状态获取监听线程
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
		 * 启动应用软件卸载监听线程
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
		 * 启动重启监听线程
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
		 * 启动重启后的处理线程
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
	 * @Description: 重启后的执行脚本处理
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
			 * 执行脚本的每行脚本路径文件
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
	 * @Description: 重启监听方法
	 * @return: void
	 */
	public void rebootListen() {

		try {
			ServerSocket server = new ServerSocket(REBOOT_PORT);
			int i = 1;
			while (true) {
				Socket socket = server.accept();
				/**
				 * 获取输入消息，并解密
				 */
				ObjectInputStream ois = new ObjectInputStream(
						socket.getInputStream());
				byte[] str = (byte[]) ois.readObject();
				byte[] str2 = AESUtil.decrypt(str);
				String str1 = new String(str2, "iso-8859-1");

				/**
				 * 判断是否为正确密钥，是则继续执行，否则返回错误密钥的消息
				 */
				if (!AESUtil.isErrorKey(str1)) {
					Message msg = (Message) SerializeUtil.deserialize(str1);
					String result = new String();

					/**
					 * 写入密钥验证成功的记录到日志
					 */
					Date date = new Date(System.currentTimeMillis());
					String time = date.toString();
					String command = msg.getType().toString();
					String opID = msg.getopID();
					XMLRecord.write(opID, time, command, "right key");

					/**
					 * 根据配置文件，判断是否可以执行操作，是则继续执行，否则返回拒绝执行的消息
					 */
					
					System.out.println("reboot:" + Agent.config.get(msg.getType().toString())+"****");
					if (Agent.config.get(msg.getType().toString()).equals("on"))
						result = "executing";
					else
						result = "0x0700001";

					if (result.equals("executing")) {
						/**
						 * 接收到第一个重启请求后，就调用重启处理线程，之后的重启请求，都可以忽视，都做接收，但不做操作
						 */
						if (i == 1) {
							if (os == 0) {
								/**
								 * 启动windows重启处理线程
								 */
								new WRebootHandle(msg);

							} else if (os == 1) {
								/**
								 * 启动Linux重启处理线程
								 */
								new LRebootHandle(msg);
							}
							i++;
						} else {
							result = "already executing";
						}
					}

					/**
					 * 输出执行状态的消息，并加密
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
					 * 输出密钥错误的消息，并加密
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
					 * 写入密钥错误的记录到日志
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
	 * @Description: 应用软件卸载监听方法
	 * @return: void
	 */
	public void appUninstallListen() {

		try {
			ServerSocket server = new ServerSocket(APPUNINSTALL_PORT);
			while (true) {
				Socket socket = server.accept();
				if (os == 0) {
					/**
					 * 启动windows应用软件部署处理线程
					 */
					new WAppUninstallHandle(socket);

				} else if (os == 1) {
					/**
					 * 启动Linux应用软件部署处理线程
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
	 * @Description: 操作状态获取监听方法
	 * @return: void
	 */
	public void statusGetListen() {
		try {
			ServerSocket server = new ServerSocket(STATUSGET_PORT);
			while (true) {
				Socket socket = server.accept();
				if (os == 0) {
					/**
					 * 启动windows操作状态获取处理线程
					 */
					new WStatusGetHandle(socket);

				} else if (os == 1) {
					/**
					 * 启动Linux操作状态获取处理线程
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
	 * @Description: 虚拟机脚本执行监听方法
	 * @return: void
	 */
	public void vmScriptExecuteListen() {
		try {
			ServerSocket server = new ServerSocket(VMSCRIPTEXECUTE_PORT);
			while (true) {
				Socket socket = server.accept();
				if (os == 0) {
					/**
					 * 启动windows虚拟机脚本执行处理线程
					 */
					new WVMScriptExecuteHandle(socket);

				} else if (os == 1) {
					/**
					 * 启动linux虚拟机脚本执行处理线程
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
	 * @Description: 应用软件更新监听方法
	 * @return: void
	 */
	public void appUpdateListen() {
		try {
			ServerSocket server = new ServerSocket(APPUPDATE_PORT);
			while (true) {
				Socket socket = server.accept();
				if (os == 0) {
					/**
					 * 启动windows应用软件更新处理线程
					 */
					new WAppUpdateHandle(socket);

				} else if (os == 1) {
					/**
					 * 启动linux应用软件更新处理线程
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
	 * @Description: 应用软件配置监听方法
	 * @return: void
	 */
	public void appConfigListen() {
		try {
			ServerSocket server = new ServerSocket(APPCONFIG_PORT);
			while (true) {
				Socket socket = server.accept();
				if (os == 0) {
					/**
					 * 启动windows应用软件配置处理线程
					 */
					new WAppConfigHandle(socket);

				} else if (os == 1) {
					/**
					 * 启动linux应用软件配置处理线程
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
	 * @Description: 应用软件部署监听方法
	 * @return: void
	 */
	public void appSetupListen() {

		try {
			ServerSocket server = new ServerSocket(APPSETUP_PORT);
			while (true) {
				Socket socket = server.accept();
				if (os == 0) {
					/**
					 * 启动windows应用软件部署处理线程
					 */
					new WAppSetupHandle(socket);

				} else if (os == 1) {
					/**
					 * 启动Linux应用软件部署处理线程
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
	 * @Description: 基础环境配置监听方法
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
					 * 启动windows基础环境配置处理线程
					 */
					new WOSConfigHandle(socket);

				} else if (os == 1) {
					/**
					 * 启动linux基础环境配置处理线程
					 */
					new LOSConfigHandle(socket);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	
	
	
/*
 * 每次重新启动服务都会自动执行
 */
	public void readConfig() {
		File file = null;
		
		if (os == 0) {
			/**
			 * 启动windows
			 */
			file = new File("C:/config.txt");

		} else if (os == 1) {
			/**
			 * 启动linux
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
	 * @Description: 主函数
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
