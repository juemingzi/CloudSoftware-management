package sample.hello.bean;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import sample.DBOP.*;
import edu.xidian.enc.AESUtil;
import edu.xidian.enc.SerializeUtil;
import edu.xidian.message.Message;
import edu.xidian.message.MsgType;
import edu.xidian.message.XmlUtils;

/**
 * 监听所有虚拟机代理软件发送过来的修改事件状态的信息， 将事件状态信息更新到数据库中
 * 
 * @author hp
 * 
 */
public class TCPServer extends Thread {
	// 监听端口
	private static int listenPort;
	private Socket socket;// 接入的客户端Socket
	private ServerSocket serverSocket;

	public static void main(String[] args) throws IOException {
		TCPServer ts = new TCPServer();
		ts.start();
	}

	@Override
	public void run() {
		try {
			Document doc = XmlUtils.getDocument();
			Element root = doc.getRootElement();// 得到根节点
			Element pathEle = root.element("listen-port");
			listenPort = Integer.parseInt(pathEle.getText());
			serverSocket = new ServerSocket(listenPort);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * try { serverSocket = new ServerSocket(7001); } catch (IOException e1)
		 * { // TODO Auto-generated catch block e1.printStackTrace(); }
		 */
		System.out.println("##### TCPServer start to listen " + listenPort
				+ "...");
		while (true) {
			// while (!isInterrupted()) {
			try {
				socket = serverSocket.accept();
				System.out
						.println("##### There's a client/other nodes connect: "
								+ socket.getInetAddress().getHostAddress());
				new ResponseThread(socket);
				// System.out.println("有客户端/其他节点接入: "+
				// socket.getInetAddress().getHostAddress());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}

class ResponseThread extends Thread {
	private Socket socket;

	public ResponseThread(Socket socket) {
		this.socket = socket;
		start();
	}

	@Override
	public void run() {
		System.out.println("listening....");
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(socket.getInputStream());
			byte[] rcvstr = (byte[]) ois.readObject();
			// 解密
			byte[] str2 = AESUtil.decrypt(rcvstr, socket.getInetAddress()
					.getHostAddress());
			String str1 = new String(str2, "iso-8859-1");
			if (str1.equals("NoSuchAlgorithmException")
					|| str1.equals("NoSuchPaddingException")
					|| str1.equals("InvalidKeyException")
					|| str1.equals("BadPaddingException")
					|| str1.equals("IllegalBlockSizeException")) {
				System.out.println("JAVA security, error key");
			} else {
				Message msg = (Message) SerializeUtil.deserialize(str1);
				// Message msg = (Message) ois.readObject();//
				// Message outMes = null;
				// ObjectOutputStream oos = new ObjectOutputStream(
				// socket.getOutputStream());
				// 代理软件给Tomcat发送事件执行状态的信息
				System.out
						.println("received change op status msg from Agent to Tomcat...");
				MsgType opName = msg.getType();
				int opId = Integer.parseInt(msg.getOpID());
				System.out.println("opName:" + opName.name() + "***opID:"
						+ opId + "***");

				/***
				 * 2015-1-20 15:31更改
				 */
				if (opName.equals(MsgType.executeVMScript)) {
					/**
					 * 虚拟机执行脚本（返回status和执行结果）
					 */
					String[] info = (String[]) msg.getValues();
					System.out.println("*****opID:" + opId + "\t status:"
							+ info[0] + "\t opName:" + opName + "*****");
					DBOperation dbop = new DBOperation();
					DBForSCS scs = new DBForSCS();
					// 更新脚本执行的列表
					scs.updateScript(opId);
					// 在数据库里查询opID对应的记录，改变其status
					dbop.updateOpStatus(opId, info[0], info[1]);
					
					scs.close();
					dbop.close();
					System.out.println("outMes OK!");

				} else {
					if (opName.equals(MsgType.setupOracle10gInterface)
							|| opName.equals(MsgType.setupOracle11gInterface)
							|| opName
									.equals(MsgType.setupSQLServer2000Interface)
							|| opName
									.equals(MsgType.setupSQLServer2008R2Interface)) {
						
						System.out.println("in @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@interface");
						
						/**
						 * 界面安装
						 */
						String status = (String) msg.getValues();
						// 输出获取到的msg信息，测试用
						System.out.println("*****opID:" + opId + "\t status:"
								+ status + "\t opName:" + opName + "*****");
						DBOperation dbop = new DBOperation();
						String softVersion = dbop.queryOpTableForVersion(opId);
						// 如果是软件安装成功返回的编码，插入一条记录到hostapp表中
						String regEx1 = "0x0100.00";
						String ip = socket.getInetAddress().getHostAddress();
						String software = opName.toString().substring(5,
								opName.toString().length() - 9);
						GetInstallPath getpath = new GetInstallPath();
						String installpath = getpath.getpathForHostapp(ip,
								software);
						DBForSCS scs1 = new DBForSCS();
						scs1.getSetupStatus(ip,software);
						scs1.close();
						System.out.println("installPath  ********************"+installpath);
						getpath.close();
						if (Pattern.matches(regEx1, status)) {

							// dbop.insertHostappTable(
							// socket.getInetAddress().getHostAddress(),
							// opName.toString().substring(5,
							// opName.toString().length()-9),
							// softVersion);
							DBForSCS scs = new DBForSCS();
							if(installpath != null){
								if(!installpath.equals("null")){
									dbop.insertHostappTable(ip, software, softVersion,
									installpath);
									scs.updateSetup(ip, software);
									scs.close();
								}
							}
							
						} else if(status.toString().contains("0x01")){
							DBForSCS scs = new DBForSCS();
							scs.updateSetup(ip, software);
							scs.close();

						}
						// 更新opinfo表
						dbop.updateOpStatus(opId, status);
						dbop.close();
						System.out.println("outMes OK!");

					} else {
						/**
						 * 其他情况，只返回status
						 */
						String status = (String) msg.getValues();
						// 输出获取到的msg信息，测试用
						System.out.println("*****opID:" + opId + "\t status:"
								+ status + "\t opName:" + opName + "*****");

						DBOperation dbop = new DBOperation();
						DBForSCS scs = new DBForSCS();
						GetInstallPath getpath = new GetInstallPath();
						String softVersion = dbop.queryOpTableForVersion(opId);
						// 如果是软件安装成功返回的编码，插入一条记录到hostapp表中
						String regEx1 = "0x0100.00";
						String regEx1_1 = "0x0100.01";
						String regEx1_2 = "0x0100.02";
						String regEx1_3 = "0x0100.03";
						String regEx1_4 = "0x0100.04";
						String regEx2 = "0x0200.00";
						String regEx3 = "0x0400.00";
						String regEx3_1 = "0x0400.01";
						String regEx3_2 = "0x0400.02";
						String regEx3_3 = "0x0400.03";
						String regEx3_4 = "0x0400.04";
						if (Pattern.matches(regEx1, status)) {
							String installpath = getpath.getpathForHostapp(
									socket.getInetAddress().getHostAddress(),
									opName.toString().substring(5,
											opName.toString().length()));
							dbop.insertHostappTable(socket.getInetAddress()
									.getHostAddress(), opName.toString()
									.substring(5, opName.toString().length()),
									softVersion, installpath);
							scs.updateSetup(socket.getInetAddress()
									.getHostAddress(), opName.toString()
									.substring(5, opName.toString().length()));
						} else if (Pattern.matches(regEx1_1, status)
								|| Pattern.matches(regEx1_1, status)
								|| Pattern.matches(regEx1_2, status)
								|| Pattern.matches(regEx1_3, status)
								|| Pattern.matches(regEx1_4, status)) {
							scs.updateSetup(socket.getInetAddress()
									.getHostAddress(), opName.toString()
									.substring(5, opName.toString().length()));
						}
						// 如果是软件卸载成功返回的编码，删除一条记录到hostapp表中
						else if (Pattern.matches(regEx2, status)) {
							dbop.deleteHostappTable(socket.getInetAddress()
									.getHostAddress(), opName.toString()
									.substring(9, opName.toString().length()));
						}
						// 如果是软件更新成功返回的编码，更新一条记录到hostapp表中
						else if (Pattern.matches(regEx3, status)) {

							String installpath = getpath.getpathForHostapp(
									socket.getInetAddress().getHostAddress(),
									opName.toString().substring(6,
											opName.toString().length()));
							dbop.updateHostappTable(socket.getInetAddress()
									.getHostAddress(), opName.toString()
									.substring(6, opName.toString().length()),
									softVersion, installpath);
							scs.updateSetup(socket.getInetAddress()
									.getHostAddress(), opName.toString()
									.substring(6, opName.toString().length()));
						} else if (Pattern.matches(regEx3_1, status)
								|| Pattern.matches(regEx3_2, status)
								|| Pattern.matches(regEx3_3, status)
								|| Pattern.matches(regEx3_4, status)) {
							scs.updateSetup(socket.getInetAddress()
									.getHostAddress(), opName.toString()
									.substring(6, opName.toString().length()));
							dbop.deleteHostappTable(socket.getInetAddress()
									.getHostAddress(), opName.toString()
									.substring(6, opName.toString().length()));
						}
						else if(opName.toString().contains("setup")&&status.toString().contains("0x")&& !status.equals("0x0100C10")){
							scs.updateSetup(socket.getInetAddress()
									.getHostAddress(), opName.toString()
									.substring(5, opName.toString().length()));
						}else if(opName.toString().contains("update")&&status.toString().contains("0x")){
							scs.updateSetup(socket.getInetAddress()
									.getHostAddress(), opName.toString()
									.substring(6, opName.toString().length()));
						}
						scs.close();
						getpath.close();

						// Linux Oracle安装
						if (opName.equals(MsgType.setupOracle11g)
								&& status.equals("0x0100C10")) {
							ChangeService cep = new ChangeService("wenyanqi",
									"123456");
							if (cep.isHasError()) {
								System.out.println(cep.getErrorMessage());
								cep = null;
								return;
							}
							cep.setservice();
							if (cep.isHasError()) {
								System.out.println(cep.getErrorMessage());
								cep = null;
								return;
							}

							if (cep.isSuccessfully()) {
								System.out.println(cep.getSystemMessage());
							}
							System.out.println("outMes OK!");
						} else {
							System.out.println("the opid is ！！！！！！！！！！！！！！！！！！！！！"+opId);
							// 在数据库里查询opID对应的记录，改变其status
							// DBOperation dbop = new DBOperation();
							dbop.updateOpStatus(opId, status);
							dbop.close();

							// 不给Agent的反馈信息了
							// outMes = new Message();
							// oos.writeObject("OK.I've receiced your message.");
							// oos.flush();
							System.out.println("outMes OK!");
						}
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
