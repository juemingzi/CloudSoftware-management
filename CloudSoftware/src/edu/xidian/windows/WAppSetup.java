package edu.xidian.windows; //����

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

import edu.xidian.agent.FileLength;

public class WAppSetup {
	WVMScriptExecute wexe = new WVMScriptExecute();
	AppConfig ac = new AppConfig();

	public static String tomcatPath = "";
	public static String pythonPath = "";
	public static String mysqlPath = "";
	TestPath tp = new TestPath();
	String str = tp.getPath();

	/**
	 * @author wzy yixiugai show mysql ��װ
	 * @appConfigFilePath ���е������ļ���ŵ�λ��
	 * @param //zipFilePathPre
 ѹ������ŵ�λ��
	 * @param unzipDirectory
	 *            ��ѹ֮���λ��
	 * @param ZipFileName
	 *            ѹ����������
	 * @param password
	 *            ���ݿ�����õ�����
	 * @param //mySQLPath
 ���ݿ���򻷾�������д������Ľű��ļ�
	 * @param //mySQLexchangePath 
 �л���bin�ļ��µĽű��ļ�
	 * @param //mySQLInstallPath
 ���ݿ�ִ�а�װ�Ľű�
	 * @param m
	 *            //ySQLStartPath ���ݿ������Ľű��ļ�
	 * @param mySQLPwdPath
	 *            ���ݿ���������Ľű��ļ�
	 * @return 0x0100204 ��װmysqlʧ�� 0x0100200 ��װmysql�ɹ� //0x0100201
	 *         mysqlû�а�װ������ע��� //0x0100202:ϵͳװ��mysql 0x0100203 ϵͳû��װmysql
	 */
	public String setupMySql(String zipFileName, String unzipDirectory,
			String password, String oldPath) throws Exception {

		String appConfigFilePath = "C:\\setupscripts\\";
		String zipFilePathPre = "C:\\softsource\\";
		String code = "";// �������յı���
		PreInstall pre = new PreInstall();
		WAppUninstall un = new WAppUninstall();
		unzipDirectory = unzipDirectory.replace("/", "\\\\");
		if (oldPath != null) {
			if (!oldPath.equals("null")) {
				un.uninstallMySql(oldPath);
			}
		}

		String preinstall = pre.checkMySQL();
		System.out.println("��װ������!!!!!!!!!!!!");
		if (preinstall != "0x0100203") {
			return preinstall;// ������ʺϰ�װ,���ؾ������
		} else {
			/**
			 * ��ѹ��װ��
			 */
			try {
				System.out.println(" ��ѹ    ��" + zipFilePathPre + zipFileName);
				UnZip.unZip(zipFilePathPre + zipFileName, unzipDirectory + "\\");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			/**
			 * ͨ��ѹ��������ȡ��װ��������,����·��д�������ļ��� length ��ѹ�������ֵĳ��� unzipFileName ��װ��������
			 */

			int length = zipFileName.length();
			String unzipFileName = zipFileName.substring(0, length - 4);
			System.out.println(zipFileName + "===========" + unzipFileName
					+ "============");
			ac.appendToFile("C:\\" + unzipFileName + "\\my-default.ini",
					"basedir = " + unzipDirectory + "\\" + unzipFileName);
			ac.appendToFile("C:\\" + unzipFileName + "\\my-default.ini",
					"datadir = " + unzipDirectory + "\\" + unzipFileName
							+ "\\data");
			File filepath = new File("C:\\setupscripts\\MySQL\\");
			if (!filepath.isDirectory()) {
				filepath.mkdir();
			}
			/**
			 * mySQL�����ĸ�ִ�еĽű�
			 */
			String filePath = "C:\\setupscripts\\MySQL\\";
			File path = new File(filePath + "path.bat ");
			path.createNewFile();
			File change = new File(filePath + "change.bat");
			change.createNewFile();

			FileOutputStream outStr1 = new FileOutputStream(path);
			BufferedOutputStream buff1 = new BufferedOutputStream(outStr1);
			buff1.write(("cmd /c setx PATH \"%PATH%;" + unzipDirectory + "\\"
					+ unzipFileName + "\\bin;\" /M").getBytes());
			buff1.flush();
			buff1.close();
			outStr1.close();

			FileOutputStream outStr0 = new FileOutputStream(change);
			BufferedOutputStream buff0 = new BufferedOutputStream(outStr0);
			buff0.write(("cd " + unzipDirectory + "\\ \r\n").getBytes());
			buff0.write(("cd " + unzipDirectory + "\\" + unzipFileName + "/bin \r\n")
					.getBytes());
			buff0.write("mysqld -install \r\n".getBytes());
			buff0.write("net start mysql \r\n".getBytes());
			buff0.write(("mysqladmin -u root password " + password + "\r\n")
					.getBytes());
			buff0.flush();
			buff0.close();
			outStr0.close();

			/**
			 * MySQL��װ�ű���·��
			 */
			String mySQLPath = appConfigFilePath + "MySQL\\path.bat";
			String mySQLexchangePath = appConfigFilePath + "MySQL\\change.bat";

			/**
			 * ����ִ�нű��ļ�����mysql�İ�װ 1.ִ���򻷾�����path��д��mysql��λ��
			 * 2.�л���Mysql��bin�ļ���3.ִ��mysql�İ�װ�ű� 4.ִ��mysql�������ű�5.ִ��mysql����������ű�
			 */

			mysqlPath = unzipDirectory + "\\" + unzipFileName + "\\bin;";
			boolean Ismysql = tp.charge(mysqlPath, str);

			System.out.println("~~~~~~~~~~~~~~~~~~Ismysql" + Ismysql);
			System.out.println("~~~~~~~~~~~~~~~~~~mysqlPath" + mysqlPath);

			if (!Ismysql) {

				System.out.println("step1:����path·��");
				wexe.executeVMScript(mySQLPath);// ����ŵ��������ļ���λ��
				Thread.sleep(10000);
			}

			System.out
					.println("step2:�л���Mysql��bin�ļ���,step3:ִ��mysql�İ�װ�ű�,step4:ִ��mysql�������ű�,step5:ִ��mysql����������ű�");
			wexe.executeVMScript(mySQLexchangePath);// �л���ַ��mysql��bin�ļ���
			Thread.sleep(10000);

			String testCode = pre.checkMySQL();
			if (testCode != "0x0100202") {
				System.out.println("��װʧ��");
				un.uninstallMySql(unzipDirectory);
				code = "0x0100204";
			} else {
				code = "0x0100200";
			}
			System.out.println("end");
			return code;
		}
	}

	// public String setupMySql(String zipFileName, String unzipDirectory,
	// String password) throws Exception {
	// String appConfigFilePath = "C:\\setupscripts\\";
	// String zipFilePathPre = "C:\\softsource\\";
	// String code = "";//�������յı���
	// PreInstall pre = new PreInstall();
	// String preinstall = pre.checkMySQL();
	// WAppUninstall un = new WAppUninstall();
	// if (preinstall != "0x0100203") {
	// return preinstall;// ������ʺϰ�װ,���ؾ������
	// } else {
	// /**
	// * ��ѹ��װ��
	// */
	// try {
	// UnZip.unZip(zipFilePathPre + zipFileName, unzipDirectory + "\\");
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// /**
	// * ͨ��ѹ��������ȡ��װ��������,����·��д�������ļ��� length ��ѹ�������ֵĳ��� unzipFileName ��װ��������
	// */
	//
	// int length = zipFileName.length();
	// String unzipFileName = zipFileName.substring(0, length - 4);
	// System.out.println(zipFileName + "===========" + unzipFileName
	// + "============");
	// ac.appendToFile("C:\\" + unzipFileName + "\\my-default.ini",
	// "basedir = " + unzipDirectory + "\\" + unzipFileName);
	// ac.appendToFile("C:\\" + unzipFileName + "\\my-default.ini",
	// "datadir = " + unzipDirectory + "\\" + unzipFileName
	// + "\\data");
	// File filepath = new File("C:\\setupscripts\\MySQL\\");
	// if (!filepath.isDirectory()) {
	// filepath.mkdir();
	// }
	// /**
	// * mySQL�����ĸ�ִ�еĽű�
	// */
	// String filePath = "C:\\setupscripts\\MySQL\\";
	// File path = new File(filePath + "path.bat ");
	// path.createNewFile();
	// File change = new File(filePath + "change.bat");
	// change.createNewFile();
	// // File install = new File(filePath + "install.bat");
	// // install.createNewFile();
	// // File start = new File(filePath + "start.bat");
	// // start.createNewFile();
	// // File pw = new File(filePath + "pw.bat");
	// // pw.createNewFile();
	//
	// FileOutputStream outStr1 = new FileOutputStream(path);
	// BufferedOutputStream buff1 = new BufferedOutputStream(outStr1);
	// buff1.write(("cmd /c setx PATH \"%PATH%;" + unzipDirectory + "\\"
	// + unzipFileName + "\\bin;\" /M").getBytes());
	// buff1.flush();
	// buff1.close();
	// outStr1.close();
	//
	// FileOutputStream outStr0 = new FileOutputStream(change);
	// BufferedOutputStream buff0 = new BufferedOutputStream(outStr0);
	// buff0.write(("cd " + "C:\\ \r\n").getBytes());
	// buff0.write(("cd " + "C:\\" + unzipFileName + "\\bin \r\n").getBytes());
	// buff0.write("mysqld -install \r\n".getBytes());
	// buff0.write("net start mysql \r\n".getBytes());
	// buff0.write(("mysqladmin -u root password " + password +
	// "\r\n").getBytes());
	// buff0.flush();
	// buff0.close();
	// outStr0.close();
	//
	// /*FileOutputStream outStr2 = new FileOutputStream(install);
	// BufferedOutputStream buff2 = new BufferedOutputStream(outStr2);
	// buff2.write("mysqld -install".getBytes());
	// buff2.flush();
	// buff2.close();
	// outStr2.close();*/
	//
	// /*FileOutputStream outStr3 = new FileOutputStream(start);
	// BufferedOutputStream buff3 = new BufferedOutputStream(outStr3);
	// buff3.write("net start mysql".getBytes());
	// buff3.flush();
	// buff3.close();
	// outStr3.close();*/
	//
	// /*FileOutputStream outStr4 = new FileOutputStream(pw);
	// BufferedOutputStream buff4 = new BufferedOutputStream(outStr4);
	// buff4.write(("mysqladmin -u root password " + password).getBytes());
	// buff4.flush();
	// buff4.close();
	// outStr4.close();*/
	//
	// /**
	// * MySQL��װ�ű���·��
	// */
	// String mySQLPath = appConfigFilePath + "MySQL\\path.bat";
	// String mySQLexchangePath = appConfigFilePath + "MySQL\\change.bat";
	// //String mySQLInstallPath = appConfigFilePath + "MySQL\\install.bat";
	// //String mySQLStartPath = appConfigFilePath + "MySQL\\start.bat";
	// //String mySQLPwdPath = appConfigFilePath + "MySQL\\pw.bat";
	//
	// /**
	// * ����ִ�нű��ļ�����mysql�İ�װ 1.ִ���򻷾�����path��д��mysql��λ��
	// 2.�л���Mysql��bin�ļ���3.ִ��mysql�İ�װ�ű�
	// * 4.ִ��mysql�������ű�5.ִ��mysql����������ű�
	// */
	//
	// mysqlPath = unzipDirectory + "\\"+ unzipFileName + "\\bin;";
	// boolean Ismysql = tp.charge(mysqlPath, str);
	//
	// System.out.println("~~~~~~~~~~~~~~~~~~Ismysql"+Ismysql);
	// System.out.println("~~~~~~~~~~~~~~~~~~mysqlPath"+mysqlPath);
	//
	// if(!Ismysql){
	//
	// System.out.println("step1:����path·��");
	// wexe.executeVMScript(mySQLPath);// ����ŵ��������ļ���λ��
	// Thread.sleep(10000);
	// }
	//
	// System.out.println("step2:�л���Mysql��bin�ļ���,step3:ִ��mysql�İ�װ�ű�,step4:ִ��mysql�������ű�,step5:ִ��mysql����������ű�");
	// wexe.executeVMScript(mySQLexchangePath);// �л���ַ��mysql��bin�ļ���
	// Thread.sleep(10000);
	//
	// // System.out.println("step3:ִ��mysql�İ�װ�ű�");
	// // wexe.executeVMScript(mySQLInstallPath);
	// // Thread.sleep(60000);
	// //
	// // System.out.println("step4:ִ��mysql�������ű�");
	// // wexe.executeVMScript(mySQLStartPath);
	// // Thread.sleep(60000);
	// //
	// // System.out.println("step5:ִ��mysql����������ű�");
	// // wexe.executeVMScript(mySQLPwdPath);
	// // Thread.sleep(60000);
	//
	// String testCode = pre.checkMySQL();
	// if(testCode != "0x0100202"){
	// System.out.println("��װʧ��");
	// un.uninstallMySql(unzipDirectory);
	// code = "0x0100204";
	// }else{
	// code = "0x0100200";
	// }
	// System.out.println("end");
	// return code;
	// }
	// }

	/**
	 * wenyanqi---nginx��װ yixiugai
	 */
	public String setupNginx(String zipFileName, String path, String oldpath) {
		/**
		 * zipFileName ���Դ�ļ����� unzipDirectory �����װ·��
		 */
		String temp_path = path.replaceAll("/", "\\\\");
		File f = new File(path);
		if (!f.isDirectory()) {
			f.mkdir();
		}
		if (oldpath != null) {
			if (!oldpath.equals("null")) {
				WAppUninstall was = new WAppUninstall();
				was.uninstallNginx(oldpath);
			}
		}

		try {

			File filepath = new File("C:\\setupscripts\\nginx\\"); // �ű�����·��
			if (!filepath.isDirectory()) {
				filepath.mkdir();
			}
			File batfile = new File("C:\\setupscripts\\nginx\\nginx.bat");
			batfile.createNewFile();
			FileOutputStream outStr = new FileOutputStream(batfile);
			BufferedOutputStream buff = new BufferedOutputStream(outStr);

			// д��ű�����
			buff.write("C: \r\n cd C:\\Program Files (x86)\\WinRAR\r\n"
					.getBytes());
			buff.write(("winrar x C:\\softsource\\" + zipFileName + " * "
					+ temp_path + "\\").getBytes());
			buff.flush();
			buff.close();
			outStr.close();
			// ִ�нű�
			wexe.executeVMScript("C:\\setupscripts\\nginx\\nginx.bat");

			// �ж��Ƿ�װ�ɹ��ˣ����δ�ɹ�����ж�أ�
			String file_path = temp_path + "\\"
					+ zipFileName.substring(0, zipFileName.length() - 4);
			// System.out.println(file_path);
			WAppConfig wag = new WAppConfig();
			String result = wag.getNginxConfig(file_path, "listen");
			if (!result.endsWith("0x0800401")) {
				return "0x0100400";
			}
			WAppUninstall wau = new WAppUninstall();
			wau.uninstallNginx(temp_path);
			System.out.println("��װʧ��");
			return "0x0100401";
		} catch (IOException e) {
			return "0x0100401";
		}
	}

	/**
	 * @author wzy show Memcached 64λ��װ
	 * @param FileName
	 *            memcached������
	 * @return 0x0100700:��װ�ɹ�
	 * @throws Exception
	 * */
	public static void main(String[] args) throws Exception {
		// WAppSetup ws = new WAppSetup();
		// setupMemcached("memcached64.exe");
	}

	public static String setupMemcached(String FileName, String path,
			String oldpath) throws Exception {
		if (oldpath != null) {
			if (!oldpath.equals("null")) {
				WAppUninstall was = new WAppUninstall();
				was.uninstallMemcached(oldpath);
			}
		}
		// �ж�����·���Ƿ���ڣ�����������·��
		File file_path = new File(path);
		if (!file_path.isDirectory()) {
			file_path.mkdirs();
		}
		String code = "";

		File f2 = new File("C:\\setupscripts\\memcached"); // ������װ�ű��ļ���
		if (!f2.isDirectory()) {
			f2.mkdir();
		}
		File f3 = new File("C:\\setupscripts\\memcached\\memcached.bat");
		if (!f3.isFile()) {
			f3.createNewFile();
		}
		// �ҵ�����ִ�е��ļ�����
		String exe_filename = path + "\\"
				+ FileName.substring(0, FileName.length() - 4) + "\\"
				+ FileName.substring(0, FileName.length() - 4);

		String command = exe_filename + " -d install"; // ��װ
		String unzip1 = "cd C:\\Program Files (x86)\\WinRAR"; // �ҵ�ѹ������
		String unzip2 = "winrar x C:\\softsource\\" + FileName + " * " + path; // ѹ����ָ��·��֮��

		// д�ű�
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("@echo off");
		strBuffer.append(System.getProperty("line.separator"));
		strBuffer.append("C:");
		strBuffer.append(System.getProperty("line.separator"));
		strBuffer.append(unzip1);
		strBuffer.append(System.getProperty("line.separator"));
		strBuffer.append(unzip2);
		strBuffer.append(System.getProperty("line.separator"));
		strBuffer.append(command);
		strBuffer.append(System.getProperty("line.separator"));
		strBuffer.append(exe_filename + " -d start");
		strBuffer.append(System.getProperty("line.separator"));
		strBuffer.append("exit");
		strBuffer.append(System.getProperty("line.separator"));

		PrintWriter printWriter = new PrintWriter(
				"C:\\setupscripts\\memcached\\memcached.bat");

		printWriter.write(strBuffer.toString().toCharArray());
		printWriter.flush();
		printWriter.close();
		// ִ�нű�
		String jdkCommand = "cmd /c  C:/setupscripts/memcached/memcached.bat";
		System.out.println(jdkCommand);
		WVMScriptExecute we = new WVMScriptExecute();
		we.executeVMScript(jdkCommand);

		// ���memcached�Ƿ�װ�ɹ�

		String filePath = "C:\\setupscripts\\memcached";
		File check_path = new File(filePath + "\\checkmemcached.bat ");
		String checkPath = filePath + "\\" + "checkmemcached.bat";
		check_path.createNewFile();

		FileOutputStream outStr0 = new FileOutputStream(check_path);
		BufferedOutputStream buff0 = new BufferedOutputStream(outStr0);
		buff0.write(("net start \r\n").getBytes());
		buff0.flush();
		buff0.close();
		outStr0.close();

		String result = PreInstall.executeVMScript(checkPath);
		if (result.equals("OK")) {
			code = "0x0100700";
		} else {
			code = "0x100701";
			WAppUninstall.uninstallMemcached(path);
		}

		return code;
	}

	/**
	 * @author ZXQ ��װTomcat yixuigai
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public String setupTomcat(String TomcatZip, String TomcatPath,
			String JDKPath, String oldpath) throws IOException {
		System.out.println("oldpath ==========" + oldpath);

		if (oldpath != null) {
			if (!oldpath.equals("null")) {
				System.out.println("��װ��ж��tomcat");
				WAppUninstall wu = new WAppUninstall();
				wu.uninstallTomcat(oldpath);

			}
		}
		// ���Tomcat�ļ�������
		String[] versions = TomcatZip.split("-");
		String TomcatDirName = versions[0] + "-" + versions[1] + "-"
				+ versions[2];

		tomcatPath = TomcatPath + File.separator + TomcatDirName
				+ File.separator + "bin;";

		boolean isConfig = tp.charge(tomcatPath, str);
		System.out.println("~~~~~~~~~~~~~~~~~~istomcat" + isConfig);
		System.out.println("~~~~~~~~~~~~~~~~~~tomcatPath" + tomcatPath);

		File filepath = new File("C:\\setupscripts\\tomcat\\");
		if (!filepath.isDirectory()) {
			filepath.mkdirs();
		}

		filepath = new File(TomcatPath);
		if (!filepath.isDirectory()) {
			filepath.mkdirs();
		}

		// ��ѹ
		try {
			UnZip.unZip("C:/softsource/" + TomcatZip, TomcatPath);
		} catch (Exception e) {
			e.printStackTrace();
			WAppUninstall un = new WAppUninstall();// ʧ����ɾ���ļ�
			un.uninstallTomcat(TomcatPath);
			return "0x0100001";
		}

		// д�ű������û���������
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("cmd /c setx CATALINA_HOME  \"" + TomcatPath
				+ File.separator + TomcatDirName + "\" /M");
		strBuffer.append(System.getProperty("line.separator"));
		if (!isConfig) {
			strBuffer.append("cmd /c setx Path  \"%Path%;" + TomcatPath
					+ File.separator + TomcatDirName + File.separator
					+ "bin;\" /M");
			strBuffer.append(System.getProperty("line.separator"));
		}
		PrintWriter printWriter = new PrintWriter(
				"C:/setupscripts/tomcat/tomcat.bat");
		printWriter.write(strBuffer.toString().toCharArray());
		printWriter.flush();
		printWriter.close();
		// ִ�нű�
		String tomcatCommand = "cmd /c  C:/setupscripts/tomcat/tomcat.bat";
		System.out.println(tomcatCommand);
		WVMScriptExecute we = new WVMScriptExecute();
		we.executeVMScript(tomcatCommand);

		// �޸�startup.bat
		UpdateAppBat.updateBatFile(TomcatPath + File.separator + TomcatDirName,
				JDKPath, "startup.bat");
		// �޸�shutdown.bat
		UpdateAppBat.updateBatFile(TomcatPath + File.separator + TomcatDirName,
				JDKPath, "shutdown.bat");
		// �޸�service.bat
		UpdateAppBat.updateBatFile(TomcatPath + File.separator + TomcatDirName,
				JDKPath, "service.bat");
		// �޸�version.bat
		UpdateAppBat.updateBatFile(TomcatPath + File.separator + TomcatDirName,
				JDKPath, "version.bat");
		// �ж��Ƿ�װ�ɹ�,ͬʱ��װ����
		StringBuffer strBuffer1 = new StringBuffer();
		strBuffer1.append("call cd " + TomcatPath + File.separator
				+ TomcatDirName + "/bin");
		strBuffer1.append(System.getProperty("line.separator"));
		strBuffer1.append("call version");// �鿴�汾��Ϣ
		strBuffer1.append(System.getProperty("line.separator"));
		strBuffer1.append("call service install");// ��װ����
		strBuffer1.append(System.getProperty("line.separator"));
		PrintWriter printWriter1 = new PrintWriter(
				"C:/setupscripts/tomcat/tomcat-version.bat");
		printWriter1.write(strBuffer1.toString().toCharArray());
		printWriter1.flush();
		printWriter1.close();
		// ִ�нű�
		String tomcatCommand1 = "cmd /c C:/setupscripts/tomcat/tomcat-version.bat";
		Process process = Runtime.getRuntime().exec(tomcatCommand1);
		BufferedReader strCon = new BufferedReader(new InputStreamReader(
				process.getInputStream()));
		String line = "";
		// �Ⱥ�
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while ((line = strCon.readLine()) != null) {
			if (line.toLowerCase().contains("server number")) {
				System.out.println(line);
				return "0x0100000";
			}
		}
		// ʧ����ɾ���ļ�
		WAppUninstall un = new WAppUninstall();
		un.uninstallTomcat(TomcatPath);
		return "0x0100001";
	}

	/**
	 * ZendGuardLoader
	 * 
	 * @return
	 */
	public String setupZendGuardLoader(String zipFileName, String phppath,
			String oldPath) {
		phppath = phppath.replace("/", "\\");
		WAppUninstall wau = new WAppUninstall();
		if (oldPath != null) {
			if (!oldPath.equals("null")) {
				wau.uninstallZendGuardLoader(oldPath);
				;

			}
		}
		try {
			System.out.println("zendName =  " + zipFileName + " phpPath = "
					+ phppath);
			// 1.�ж�php·���Ƿ����
			File filepath1 = new File(phppath);
			if (!filepath1.isDirectory()) {
				return "0x0100502";
			}
			// 2.�ж�php�����ļ��Ƿ����
			filepath1 = new File("C:\\windows\\php.ini");
			if (!filepath1.isFile()) {
				return "0x0100503";
			}

			File filepath = new File("C:\\setupscripts\\ZendGuardLoader\\");
			if (!filepath.isDirectory()) {
				filepath.mkdir();
			}
			File batfile = new File(
					"C:\\setupscripts\\ZendGuardLoader\\ZendGuardLoader.bat");
			batfile.createNewFile();
			FileOutputStream outStr = new FileOutputStream(batfile);
			BufferedOutputStream buff = new BufferedOutputStream(outStr);

			UnZip.unZip("C:\\softsource\\" + zipFileName, "C:\\softsource\\");

			// int position = zipFileName.indexOf("PHP");
			// if (position < 0) {
			// position = zipFileName.indexOf("php");
			// }

			// String zendversion = zipFileName.substring(position + 4,
			// position + 7);

			String zendPathName = zipFileName.replace(".zip", "");

			File file = new File("C:\\softsource\\" + zendPathName + "\\");
			String[] filelist = file.list();
			String filename = null;
			for (int i = 0; i < filelist.length; i++) {
				if (filelist[i].toLowerCase().contains("php"))
					filename = filelist[i];
			}

			buff.write(("copy C:\\softsource\\" + zendPathName + "\\"
					+ filename + "\\ZendLoader.dll " + phppath + "\\ext \r\n")
					.getBytes());

			buff.flush();
			buff.close();
			outStr.close();
			wexe.executeVMScript("C:\\setupscripts\\ZendGuardLoader\\ZendGuardLoader.bat");

			Boolean f = AppConfig.appendToFile("C:\\windows\\php.ini",
					"[Zend.loader]\r");
			AppConfig.appendToFile("C:\\windows\\php.ini", "zend_extension="
					+ phppath + "\\ext\\ZendLoader.dll");
			AppConfig.appendToFile("C:\\windows\\php.ini",
					"zend_loader.enable=1");
			AppConfig.appendToFile("C:\\windows\\php.ini",
					"zend_loader.disable_licensing=1");
			AppConfig.appendToFile("C:\\windows\\php.ini",
					"zend_loader.obfuscation_level_support=4");
			Thread.sleep(4000);

			// ���
			File filetest = new File(phppath + "\\ext\\");
			String[] fls = filetest.list();
			for (int i = 0; i < fls.length; i++) {
				System.out.println(fls[i]);
				if (fls[i].toLowerCase().equals("zendloader.dll"))
					return "0x0100500";
			}
			System.out.println("out for ");
			wau.uninstallZendGuardLoader(phppath);
			return "0x0100501";

		} catch (IOException e) {
			wau.uninstallZendGuardLoader(phppath);
			System.out.println("out io ");
			return "0x0100501";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			wau.uninstallZendGuardLoader(phppath);
			System.out.println("out e ");
			return "0x0100501";
		}

	}

	// public String setupZendGuardLoader(String zipFileName, String
	// phppath,String oldPath) {
	//
	// WAppUninstall wau = new WAppUninstall();
	// if(oldPath != null){
	// wau.uninstallZendGuardLoader(oldPath);
	// }
	// try {
	// System.out.println("zendName =  " + zipFileName + "phpPath = " +
	// phppath);
	// // 1.�ж�php·���Ƿ����
	// File filepath1 = new File(phppath);
	// if (!filepath1.isDirectory()) {
	// return "0x0100502";
	// }
	// // 2.�ж�php�����ļ��Ƿ����
	// filepath1 = new File("C:\\windows\\php.ini");
	// if (!filepath1.isFile()) {
	// return "0x0100503";
	// }
	//
	// File filepath = new File("C:\\setupscripts\\ZendGuardLoader\\");
	// if (!filepath.isDirectory()) {
	// filepath.mkdir();
	// }
	// File batfile = new File(
	// "C:\\setupscripts\\ZendGuardLoader\\ZendGuardLoader.bat");
	// batfile.createNewFile();
	// FileOutputStream outStr = new FileOutputStream(batfile);
	// BufferedOutputStream buff = new BufferedOutputStream(outStr);
	//
	// UnZip.unZip("C:\\softsource\\" + zipFileName, "C:\\softsource\\");
	//
	// int position = zipFileName.indexOf("PHP");
	// if (position < 0) {
	// position = zipFileName.indexOf("php");
	// }
	//
	// String zendversion = zipFileName.substring(position + 4,
	// position + 7);
	// if (zendversion.equals("5.3")) {
	// buff.write(("copy C:\\softsource\\ZendGuardLoader-php-5.3-Windows\\php-5.3.x\\ZendLoader.dll "
	// + phppath + "\\ext \r\n").getBytes());
	// } else if (zendversion.equals("5.4")) {
	// buff.write(("copy C:\\softsource\\ZendGuardLoader-70429-PHP-5.4-Windows-x86\\php-5.4.x\\ZendLoader.dll "
	// + phppath + "\\ext \r\n").getBytes());
	// }
	//
	// buff.flush();
	// buff.close();
	// outStr.close();
	// wexe.executeVMScript("C:\\setupscripts\\ZendGuardLoader\\ZendGuardLoader.bat");
	//
	// Boolean f = AppConfig.appendToFile("C:\\windows\\php.ini",
	// "[Zend.loader]\r");
	// AppConfig.appendToFile("C:\\windows\\php.ini", "zend_extension="
	// + phppath + "\\ext\\ZendLoader.dll");
	// AppConfig.appendToFile("C:\\windows\\php.ini",
	// "zend_loader.enable=1");
	// AppConfig.appendToFile("C:\\windows\\php.ini",
	// "zend_loader.disable_licensing=1");
	// AppConfig.appendToFile("C:\\windows\\php.ini",
	// "zend_loader.obfuscation_level_support=4");
	// Thread.sleep(4000);
	// return "0x0100500";
	// } catch (IOException e) {
	// return "0x0100501";
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// return "0x0100501";
	// }
	//
	// }

	// ��ѯoracle�汾��
	public String oracleVersion() {
		String version = "";

		Process process = null;
		try {
			process = Runtime.getRuntime().exec("sqlplus -H");
			BufferedReader strCon = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			strCon.readLine();
			version = strCon.readLine();
			process.waitFor();

			System.out.println("sfsd" + version);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			version = "error";
			// System.out.println("0x0500001");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			version = "error";
			// System.out.println("0x0500001");
		}
		return version;
	}

	// yixiugai
	public String setupOracle(String zipFileName1, String zipFileName2,
			String hostname, String inventorypath, String oraclebase,
			String oraclehome, String password, String oldpath) {
		/**
		 * ����˵���� zipFileName���������װ�������֣�����·�� unzipDirectory���������ѹ��·������·�����ұ�����\����
		 * hostname����ʾ����wenyanqi-PC inventorypath����ʾ����C:\Program
		 * Files\Oracle\Inventory oraclebase����C:\app\wenyanqi
		 * oraclehome����ֻ�������֣�����oraclebaseƴ��
		 * ������:C:\app\wenyanqi\product\11.2.0\dbhome_1,��oraclehome=dbhome1
		 * password�����û����룬������8���ַ������ұ�����һ����д��ĸ��һ��Сд��ĸ
		 */
		inventorypath = inventorypath.replace("/", "\\");
		oraclebase = oraclebase.replace("/", "\\");

		FileLength fl = new FileLength();
		long len = 0L;

		if (oldpath != null) {
			if (!oldpath.equals("null")) {

				File ff = new File(oldpath.split("&")[0]);
				try {
					len = fl.getFileSize(ff) / 1024;
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				System.out.println("oldpath    +++++++   len" + oldpath
						+ "           " + len);

				if (len > 650) {
					System.out.println("��װ��ж��oracle");
					WAppUninstall wu = new WAppUninstall();
					String str = wu.uninstallOracle11g(oldpath);
					try {
						Thread.sleep(3000);
						wexe.executeVMScript("shutdown -r");
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

		/**
		 * ��װǰ��⻷�� 1.��������Ƿ����Ҫ������8λ���������֣���д��ĸ��Сд��ĸ ���벻���Ϲ��򣬷���0x0100C01
		 * 2.����Ƿ����㹻��Ӳ�̿ռ� 0x0100C02 3.����Ƿ����㹻���ڴ�ռ� 0x0100C03
		 */

		if (password.length() < 8) {
			return "0x0100C01";
		} else {
			boolean flag = false;
			// �ж��Ƿ������ĸ����������ķ���
			flag = password.matches("[a-z0-9A-Z\u4e00-\u9fa5]+$");
			if (flag == false) {
				return "0x0100C01";
			} else {
				// �ж��Ƿ��������
				flag = password.matches(".*\\d+.*");
				if (flag == false) {
					return "0x0100C01";
				} else {
					// �ж��Ƿ����Сд��ĸ
					flag = password.matches(".*[a-z].*");
					if (flag == false) {
						return "0x0100C01";
					} else {
						// �ж��Ƿ������д��ĸ
						flag = password.matches(".*[A-Z].*");
						if (flag == false) {
							return "0x0100C01";
						}
					}
				}
			}

		}

		// �ж��ڴ��С
		OperatingSystemMXBean osmb = (OperatingSystemMXBean) ManagementFactory
				.getOperatingSystemMXBean();
		System.out.println("ϵͳ�����ڴ��ܼƣ�" + osmb.getTotalPhysicalMemorySize()
				/ 1024 / 1024 + "MB");

		if (osmb.getTotalPhysicalMemorySize() < 1024 * 1024 * 1024) {
			System.out.println("�ڴ治�㣬ϵͳ�����ڴ��ܼƣ�"
					+ osmb.getTotalPhysicalMemorySize() / 1024 / 1024 + "MB");
			return "0x0100C02";
		}

		// �ж�Ӳ�̴�С
		File[] roots = File.listRoots();// ��ȡ���̷����б�
		for (File file : roots) {
			if (file.getPath().equals(hostname.substring(0, 3))) {
				if (file.getFreeSpace() / 1024 / 1024 / 1024 < 7) {
					System.out.println(file.getPath() + "��Ϣ����:");
					System.out.println("��ѡӲ�̿ռ䲻�㣬����δʹ�� = "
							+ file.getFreeSpace() / 1024 / 1024 / 1024 + "G");// ���пռ�
					return "0x0100C03";
				}

			}
		}
		// ���ϵͳ������

		try {
			// �����ű��ĸ�Ŀ¼
			File filepath;
			if (!(filepath = new File("C:\\setupscripts\\oracle"))
					.isDirectory()) {
				filepath.mkdir();
			}
			if (!(filepath = new File("C:\\softsource\\oracle_11g"))
					.isDirectory()) {
				filepath.mkdir();
			}

			File batfile1 = new File(
					"C:\\setupscripts\\oracle\\oracleunzip.bat");
			FileOutputStream outStr1 = new FileOutputStream(batfile1);
			BufferedOutputStream buff1 = new BufferedOutputStream(outStr1);
			// �½���ѹ�ű�����ѹoracle��װ��
			batfile1.createNewFile();
			buff1.write("C: \r\n cd C:\\Program Files (x86)\\WinRAR\r\n"
					.getBytes());
			buff1.write(("winrar x -o+ C:\\softsource\\" + zipFileName1 + " C:\\softsource\\oracle_11g\r\n")
					.getBytes()); // ע��unzipDiretoryĩβ�����\
			buff1.write(("winrar x -o+ C:\\softsource\\" + zipFileName2 + " C:\\softsource\\oracle_11g")
					.getBytes()); // ע��unzipDiretoryĩβ�����\
			buff1.flush();
			buff1.close();
			outStr1.close();

			System.out.println("oracle��װ�ļ���ʼ��ѹ");
			wexe.executeVMScript("C:\\setupscripts\\oracle\\oracleunzip.bat");
			Thread.sleep(900000);

			// �½�ִ�нű�,ʹ��rsp�ļ�ͨ����Ĭ��װoracle
			File batfile = new File("C:\\setupscripts\\oracle\\oraclesetup.bat");
			FileOutputStream outStr = new FileOutputStream(batfile);
			BufferedOutputStream buff = new BufferedOutputStream(outStr);
			batfile.createNewFile();

			buff.write(("cd C:\\softsource\\oracle_11g\\database\r\n")
					.getBytes());
			buff.write(("setup.exe -silent -responseFile C:\\setupscripts\\oracle\\db.rsp")
					.getBytes());
			buff.flush();
			buff.close();
			outStr.close();

			System.out.println("oracle��װ�ļ���ѹ��ɣ���ʼ����rsp�ļ�");
			// ����rsp�ļ�������װʱ��Ҫ�Ĳ���
			AppConfig.replaceToFile("C:\\setupscripts\\oracle\\db.rsp",
					"ORACLE_HOSTNAME", "ORACLE_HOSTNAME=" + hostname);
			AppConfig
					.replaceToFile("C:\\setupscripts\\oracle\\db.rsp",
							"INVENTORY_LOCATION", "INVENTORY_LOCATION="
									+ inventorypath);
			AppConfig.replaceToFile("C:\\setupscripts\\oracle\\db.rsp",
					"ORACLE_BASE", "ORACLE_BASE=" + oraclebase);
			AppConfig.replaceToFile("C:\\setupscripts\\oracle\\db.rsp",
					"ORACLE_HOME", "ORACLE_HOME=" + oraclebase
							+ "\\product\\11.2.0\\" + oraclehome);
			AppConfig.replaceToFile("C:\\setupscripts\\oracle\\db.rsp",
					"oracle.install.db.config.starterdb.password.ALL",
					"oracle.install.db.config.starterdb.password.ALL="
							+ password);
			AppConfig.replaceToFile("C:\\setupscripts\\oracle\\db.rsp",
					"oracle.install.db.config.starterdb.password.SYS",
					"oracle.install.db.config.starterdb.password.SYS="
							+ password);
			AppConfig.replaceToFile("C:\\setupscripts\\oracle\\db.rsp",
					"oracle.install.db.config.starterdb.password.SYSTEM",
					"oracle.install.db.config.starterdb.password.SYSTEM="
							+ password);
			AppConfig.replaceToFile("C:\\setupscripts\\oracle\\db.rsp",
					"oracle.install.db.config.starterdb.password.SYSMAN",
					"oracle.install.db.config.starterdb.password.SYSMAN="
							+ password);
			AppConfig.replaceToFile("C:\\setupscripts\\oracle\\db.rsp",
					"oracle.install.db.config.starterdb.password.DBSNMP",
					"oracle.install.db.config.starterdb.password.DBSNMP="
							+ password);
			AppConfig
					.replaceToFile(
							"C:\\setupscripts\\oracle\\db.rsp",
							"oracle.install.db.config.starterdb.fileSystemStorage.dataLocation",
							"oracle.install.db.config.starterdb.fileSystemStorage.dataLocation="
									+ oraclebase + "\\oradata");

			// ִ�нű�����ɰ�װ��һ����Ҫ���ʮ����
			System.out.println("oracle��װ�ű���ʼִ��");
			wexe.executeVMScript("C:\\setupscripts\\oracle\\oraclesetup.bat");
			Thread.sleep(1800000);
			return "0x0100C00";
		} catch (IOException e) {
			return "";
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}

	}

	/**
	 * @author ZXQ ��װApache yixiugai
	 * @param ApacheFileName
	 * @param ApachePath
	 * @return
	 * @throws IOException
	 */
	public String setupApache(String ApacheFileName, String ApachePath,
			String emailAddress, String oldpath) throws IOException {

		System.out.println("apachepackage:" + ApacheFileName);
		if (ApacheFileName.contains("zip")) {
			return setupApache2(ApacheFileName, ApachePath, emailAddress,
					oldpath);
		} else {
			return setupApache1(ApacheFileName, ApachePath, emailAddress,
					oldpath);
		}
	}

	public String setupApache1(String ApacheFileName, String ApachePath,
			String emailAddress, String oldpath) throws IOException {

		WAppUninstall wu = new WAppUninstall();
		if (oldpath != null) {
			if (!oldpath.equals("null")) {
				wu.uninstallApache(oldpath);

			}
		}

		// ɾ��ԭ�����
		System.out.println("emailAddress:" + emailAddress);
		File f = new File("C:/setupscripts/apache");
		if (!f.isDirectory()) {
			f.mkdirs();
		}
		// д�ű�
		String temppath = ApachePath;
		temppath.replaceAll("/", "\\\\");
		String command = "msiexec /a  C:\\softsource\\" + ApacheFileName
				+ " /qn " + "targetdir=\"" + temppath + "\"";
		WVMScriptExecute we = new WVMScriptExecute();
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append(command);
		strBuffer.append(System.getProperty("line.separator"));
		PrintWriter printWriter = new PrintWriter(
				"C:/setupscripts/apache/apache.bat");
		printWriter.write(strBuffer.toString().toCharArray());
		printWriter.flush();
		printWriter.close();
		// ִ�нű�
		String jdkCommand = "cmd /c  C:/setupscripts/apache/apache.bat";
		System.out.println(jdkCommand);
		String[] ss = we.executeVMScript(jdkCommand);
		if (!(ss[0].equals("0x0500000"))) {
			WAppUninstall un = new WAppUninstall();
			un.uninstallApache(ApachePath);
			return "0x0100301";
		}

		// // �޸�serveradmin����
		boolean flag = AppConfig.judgeExist(ApachePath + "/conf/httpd.conf",
				"ServerAdmin");
		if (flag) {
			flag = AppConfig.replaceToFile(ApachePath + "/conf/httpd.conf",
					"ServerAdmin", "ServerAdmin " + emailAddress);
		} else {
			flag = AppConfig.appendToFile(ApachePath + "/conf/httpd.conf",
					"ServerAdmin " + emailAddress);
		}
		// �޸�servername����
		flag = AppConfig.judgeExist(ApachePath + "/conf/httpd.conf",
				"ServerName");
		if (flag) {
			flag = AppConfig.replaceToFile(ApachePath + "/conf/httpd.conf",
					"ServerName", "ServerName 127.0.0.1:80");
		} else {
			flag = AppConfig.appendToFile(ApachePath + "/conf/httpd.conf",
					"ServerName 127.0.0.1:80");
		}

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// ��װapache����
		String serviceCommand = ApachePath + "/bin/httpd.exe -k install";
		System.out.println(serviceCommand);
		Runtime.getRuntime().exec(serviceCommand);
		// �ж��Ƿ�ɹ�
		File[] files = new File(ApachePath).listFiles();
		boolean flag1 = false;
		boolean flag2 = false;
		boolean flag3 = false;
		for (File file : files) {
			String fileName = file.getName().toLowerCase();
			if (fileName.equals("bin") && file.isDirectory()) {
				flag1 = true;
			} else if (fileName.equals("conf") && file.isDirectory()) {
				flag2 = true;
			} else if (fileName.equals("logs") && file.isDirectory()) {
				flag3 = true;
			}
		}
		if (flag1 && flag2 && flag3)
			return "0x0100300";
		return "0x0100301";
	}

	public String setupApache2(String ApacheFileName, String ApachePath,
			String emailAddress, String oldpath) throws IOException {
		if (oldpath != null) {
			if (!oldpath.equals("null")) {
				WAppUninstall wu = new WAppUninstall();
				wu.uninstallApache(oldpath);
			}
		}
		System.out.println("emailAddress:" + emailAddress);
		File f = new File("C:/setupscripts/apache");
		if (!f.isDirectory()) {
			f.mkdirs();
		}
		// ��ѹ
		try {
			UnZip.unZip("C:/softsource/" + ApacheFileName, ApachePath);
		} catch (Exception e) {
			e.printStackTrace();
			WAppUninstall un = new WAppUninstall();
			un.uninstallApache(ApachePath);
			return "0x0100301";
		}

		// �޸�ServerAdmin����
		boolean flag = AppConfig.judgeExist(ApachePath + "/conf/httpd.conf",
				"ServerAdmin");
		if (flag) {
			flag = AppConfig.replaceToFile(ApachePath + "/conf/httpd.conf",
					"ServerAdmin", "ServerAdmin " + emailAddress);
		} else {
			flag = AppConfig.appendToFile(ApachePath + "/conf/httpd.conf",
					"ServerAdmin " + emailAddress);
		}
		// �޸�ServerName����
		flag = AppConfig.judgeExist(ApachePath + "/conf/httpd.conf",
				"ServerName");
		if (flag) {
			flag = AppConfig.replaceToFile(ApachePath + "/conf/httpd.conf",
					"ServerName", "ServerName 127.0.0.1:80");
		} else {
			flag = AppConfig.appendToFile(ApachePath + "/conf/httpd.conf",
					"ServerName 127.0.0.1:80");
		}
		// �޸�ServerRoot����
		AppConfig.replaceToFile(ApachePath + "/conf/httpd.conf", "ServerRoot",
				"ServerRoot " + "\"" + ApachePath + "\"");

		// �޸�DocumentRoot����
		AppConfig.replaceToFile(ApachePath + "/conf/httpd.conf",
				"DocumentRoot", "DocumentRoot " + "\"" + ApachePath
						+ "/htdocs\"");

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// ��װapache����
		String serviceCommand = ApachePath + "/bin/httpd.exe -k install";
		System.out.println(serviceCommand);
		Runtime.getRuntime().exec(serviceCommand);
		// �жϰ�װ�Ƿ�ɹ�
		File[] files = new File(ApachePath).listFiles();
		boolean flag1 = false;
		boolean flag2 = false;
		boolean flag3 = false;
		for (File file : files) {
			String fileName = file.getName().toLowerCase();
			if (fileName.equals("bin") && file.isDirectory()) {
				flag1 = true;
			} else if (fileName.equals("conf") && file.isDirectory()) {
				flag2 = true;
			} else if (fileName.equals("logs") && file.isDirectory()) {
				flag3 = true;
			}
		}
		if (flag1 && flag2 && flag3)
			return "0x0100300";
		// ʧ����ɾ�����а�װ�ļ�
		WAppUninstall un = new WAppUninstall();
		un.uninstallApache(ApachePath);
		return "0x0100301";
	}

	/**
	 * @author ZXQ ��װ Python yixiugai
	 * @param PythonFileName
	 * @return
	 * @throws IOException
	 */
	public String setupPython(String PythonFileName, String path, String oldPath)
			throws IOException {
		path = path.replaceAll("/", "\\\\");
		File f = new File("C:\\setupscripts\\python");
		if (!f.isDirectory()) {
			f.mkdir();
		}

		if (oldPath != null) {
			if (!oldPath.equals("null")) {
				new WAppUninstall().uninstallPython(oldPath);
			}
		}
		String command = "msiexec.exe /a  C:\\softsource\\" + PythonFileName
				+ " /qn targetdir=\"" + path + "\"";
		System.out.println(command);
		pythonPath = path + ";";
		boolean isConfig = tp.charge(pythonPath, str);

		System.out.println("~~~~~~~~~~~~~~~~~~ispython" + isConfig);
		System.out.println("~~~~~~~~~~~~~~~~~~pythonPath" + pythonPath);
		// д�ű�
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append(command);
		strBuffer.append(System.getProperty("line.separator"));

		if (!isConfig) {
			strBuffer.append("cmd /c setx PATH \"%PATH%;" + pythonPath
					+ "\" /M");
			strBuffer.append(System.getProperty("line.separator"));
		}

		PrintWriter printWriter = new PrintWriter(
				"C:/setupscripts/python/python.bat");
		printWriter.write(strBuffer.toString().toCharArray());
		printWriter.flush();
		printWriter.close();
		// ִ�нű�
		String pythonCommand = "cmd /c  C:/setupscripts/python/python.bat";
		System.out.println(pythonCommand);
		WVMScriptExecute we = new WVMScriptExecute();
		String[] ss = we.executeVMScript(pythonCommand);

		// ��ⰲװ�Ƿ�ɹ�
		StringBuffer strBuffer2 = new StringBuffer();
		strBuffer2.append("cd " + path);
		strBuffer2.append(System.getProperty("line.separator"));
		strBuffer2.append("python");
		strBuffer2.append(System.getProperty("line.separator"));
		PrintWriter printWriter2 = new PrintWriter(
				"C:/setupscripts/python/python-version.bat");
		printWriter2.write(strBuffer.toString().toCharArray());
		printWriter2.flush();
		printWriter2.close();
		// ִ�нű�
		String tomcatCommand = "cmd /c C:/setupscripts/python/python-version.bat";
		Process process = Runtime.getRuntime().exec(tomcatCommand);
		BufferedReader strCon = new BufferedReader(new InputStreamReader(
				process.getInputStream()));
		String line = "";
		boolean flag = false;
		while ((line = strCon.readLine()) != null) {
			if (line.toLowerCase().contains("python")) {
				System.out.println("��װ�ɹ�");
				flag = true;
			}
		}
		if (!flag) {
			System.out.println("��װʧ��");
			new WAppUninstall().uninstallPython(path);
		}

		if (!(ss[0].equals("0x0500000"))) {
			return "0x0100601";
		}
		return "0x0100600";
	}

	public String setupFTP(String FTPFileName, String FTPPath, String oldPath)
			throws IOException {
		// �����·����Ϊ�գ�ɾ��
		FTPPath = FTPPath.replaceAll("/", "\\\\");

		if (oldPath != null) {
			if (!oldPath.equals("null")) {
				if (new WAppUninstall().uninstallFTP(oldPath).equals(
						"0x0200901")) {
					return " 0x0100901";
				}
			}
		}
		File f = new File("C:/setupscripts/ftp");
		if (!f.isDirectory()) {
			f.mkdirs();
		}
		String command = "C:\\softsource\\" + FTPFileName + " /S /D=" + FTPPath;
		System.out.println(command);
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append(command);
		strBuffer.append(System.getProperty("line.separator"));
		PrintWriter printWriter = new PrintWriter("C:/setupscripts/ftp/ftp.bat");
		printWriter.write(strBuffer.toString().toCharArray());
		printWriter.flush();
		printWriter.close();
		// ִ�нű�
		String jdkCommand = "cmd /c  C:/setupscripts/ftp/ftp.bat";
		System.out.println(jdkCommand);
		WVMScriptExecute we = new WVMScriptExecute();
		String[] ss = we.executeVMScript(jdkCommand);
		if (!(ss[0].equals("0x0500000"))) {
			new WAppUninstall().uninstallFTP(FTPPath);
			return "0x0100901";
		}

		// check
		File[] files = new File(FTPPath).listFiles();
		boolean flag1 = false;
		boolean flag2 = false;
		for (File file : files) {
			String fileName = file.getName().toLowerCase();
			if (fileName.equals("filezilla server.exe") && file.isFile()) {
				flag1 = true;
			} else if (fileName.equals("filezilla server interface.exe")
					&& file.isFile()) {
				flag2 = true;
			}
		}
		if (!(flag1 && flag2)) {
			new WAppUninstall().uninstallFTP(FTPPath);
			return "0x0100901";
		}
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "0x0100900";

	}

	public String setup360(String FileName, String Path, String oldPath)
			throws IOException {
		File f = new File("C:/setupscripts/360");
		if (!f.isDirectory()) {
			f.mkdir();
		}
		String command = "C:\\softsource\\" + FileName + " /S /D=" + Path;
		System.out.println(command);
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append(command);
		strBuffer.append(System.getProperty("line.separator"));
		PrintWriter printWriter = new PrintWriter("C:/setupscripts/360/360.bat");
		printWriter.write(strBuffer.toString().toCharArray());
		printWriter.flush();
		printWriter.close();
		// ִ�нű�
		String jdkCommand = "cmd /c  C:/setupscripts/360/360.bat";
		System.out.println(jdkCommand);
		WVMScriptExecute we = new WVMScriptExecute();
		String[] ss = we.executeVMScript(jdkCommand);
		if (!(ss[0].equals("0x0500000"))) {
			return "0x0100E01";
		}
		return "0x0100E00";
	}

	/**
	 * @author hp ��װiisRewrite
	 * @param iisRewriteFileName
	 * @param iisRewritePath
	 * @return
	 * @throws IOException
	 */
	public String setupiisRewrite(String iisRewriteName, String iisRewritePath,
			String oldPath) throws IOException {

		iisRewritePath = iisRewritePath.replaceAll("/", "\\\\");
		if (oldPath != null) {
			if (!oldPath.equals("null")) {
				if (!new WAppUninstall().uninstallIISRewrite(oldPath).equals(
						"0x0200800")) {
					return "0x0100801";
				}
			}
		}

		boolean flag = false;
		File f = new File("C:/setupscripts/iisRewrite");
		if (!f.isDirectory()) {
			f.mkdirs();
		}
		// д�ű�
		String command = "msiexec /a  C:\\softsource\\" + iisRewriteName
				+ " /qn " + "targetdir=\"" + iisRewritePath + "\"";
		WVMScriptExecute we = new WVMScriptExecute();
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append(command);
		strBuffer.append(System.getProperty("line.separator"));
		PrintWriter printWriter = new PrintWriter(
				"C:/setupscripts/iisRewrite/iisRewrite.bat");
		printWriter.write(strBuffer.toString().toCharArray());
		printWriter.flush();
		printWriter.close();
		// ִ�нű�
		String jdkCommand = "cmd /c  C:/setupscripts/iisRewrite/iisRewrite.bat";
		System.out.println(jdkCommand);
		String[] ss = we.executeVMScript(jdkCommand);
		if (!(ss[0].equals("0x0500000"))) {
			new WAppUninstall().uninstallIISRewrite(iisRewritePath);
			return "0x0100801";
		}

		// check
		File rootFile = new File(iisRewritePath
				+ "\\Program Files\\Helicon\\ISAPI_Rewrite3");
		if (rootFile.exists()) {

			for (File file : rootFile.listFiles()) {
				if (file.getName().equals("ConfigEditor.exe")) {

					return "0x0100800";
				}
			}
		}

		new WAppUninstall().uninstallIISRewrite(iisRewritePath);

		return "0x0100801";

	}

	/**
	 * @author repace
	 * @param zipFileName
	 *            ����������ѹ���ļ�������
	 * @param installPath
	 *            �������İ�װ·�� ��"c:\\zhangke"
	 * @param password
	 *            Ĭ�ϵ��ǻ�ϵ�¼��ʽ����Ҫ�û���������
	 * @param hostName
	 *            ��������͵�¼�˻���������
	 * @param userName
	 *            �û���
	 * @return
	 * @throws IOException
	 *             �޽��氲װSqlServer2008R2�ķ�����д��ж�ؽű���ɾ��ж���������ļ�����ѹ�ű��Ͱ�װ�ű���
	 *             ���н�ѹ�ű��Ͱ�װ�ű����ڰ�װ���õģ�ж�ؽű���ɾ���ļ��Ľű�����ж���ļ���ʱ���õ�
	 * @throws InterruptedException
	 */

	public String setupSQLServer2008R2(String zipFileName, String installPath,
			String password, String hostName, String userName, String oldPath)
			throws IOException, InterruptedException {
		installPath = installPath.replace("/", "\\");
		String appConfigFilePath = "C:\\setupscripts\\";
		String zipFilePathPre = "C:\\softsource\\";

		if (oldPath != null) {
			if (!oldPath.equals("null")) {
				if (!new WAppUninstall().uninstallSQLServer2008R2(oldPath)
						.equals("0x0200A00")) {
					return "0x0200A01";
				}
			}
		}

		// try {
		// Process process = Runtime.getRuntime().exec("osql /?");
		// BufferedReader strCon = new BufferedReader(new InputStreamReader(
		// process.getInputStream()));
		// String line;
		// int i = 1;
		// while ((line = strCon.readLine()) != null) {
		// if (i == 2) {
		// String info = "�Ѱ�װ��SqlServer2008R2   " + line;
		// System.out.println(info);// ����Ѱ�װSqlServer2008R2��������osql
		// // /?������ĵڶ���Ϊ�Ѱ�װ�İ汾��Ϣ
		// return "0x0100A04";// �Ѱ�װ�� �����پ�Ĭ��װ
		// }
		// i++;
		// }
		// } catch (IOException e) {
		//
		// }
		// ��ѹ֮��İ�װ���ļ���С��5.7G
		// ���ǰ�װ�����а�װ��Ҫ�İ�װ�ռ�Ҫ����7G
		double needSpace = 8; // ��������Ѿ���ѹ��������û�н�ѹ����Ҫ13G�Ŀռ䣬
								// �������ѹ�����Ѿ���ѹ����ֻ��Ҫ7G�Ŀռ�
		double constm = 1024 * 1024 * 1024;// constm=1G
		String sdisk = installPath.substring(0, 2);
		File _file = new File(sdisk);
		double freeSpace = _file.getFreeSpace() / constm;
		if (freeSpace < needSpace) {
			System.out.println("freeSpace " + freeSpace);
			System.out.println("Ӳ��ʣ��ռ䲻�㣬���ɽ��а�װ");
			return "0x0100A03";// ���ذ�װ����ռ䲻�㣬��δ���а�װ
		}

		String setupSqlScript = "c:\\setupscripts\\"
				+ "SqlServer2008R2\\setupSqlServer2008R2.bat";
		String uninstallSqlScript = "c:\\setupscripts\\"
				+ "SqlServer2008R2\\uninstallSqlServer2008R2.bat";
		String delInstallFileScript = "c:\\setupscripts\\"
				+ "SqlServer2008R2\\delInstallFileScript.bat";
		try {

			File fff = new File(installPath);
			if (!fff.exists()) {
				fff.mkdirs();
			}

			File unpackFile = new File(installPath + "\\SqlServer2008R2\\");
			if (!unpackFile.exists()) {
				unpackFile.mkdirs();
			}

			File f1 = new File("c:\\setupscripts\\");
			if (!f1.exists())
				f1.mkdir();
			File f2 = new File("c:\\setupscripts\\" + "SqlServer2008R2");
			if (!f2.exists())
				f2.mkdir();

			File uninstallFile = new File(uninstallSqlScript); // ж�ؽű�
																// ��������ط�д��ж�ؽű�
			if (uninstallFile.exists())
				uninstallFile.delete();
			uninstallFile.createNewFile();
			PrintWriter printWriter = new PrintWriter(uninstallFile);
			// printWriter.println("c:\\softsource\\SqlServer2008R2\\setup.exe ^");
			printWriter.println(installPath + "\\SqlServer2008R2\\setup.exe ^");
			// printWriter.println(installPath+"setup.exe ^");//installPath
			// �������İ�װ·�� ��"c:\\zhangke\\" ���һ��Ҫ�� \\ 20150618
			printWriter.println("/q ^"); // ע��������ط��޽������/q���н������/qs
			printWriter.println("/Action=Uninstall ^");
			printWriter.println("/INSTANCENAME=MSSQLSERVER  ^");
			printWriter
					.println("/FEATURES=SQLENGINE,REPLICATION,FULLTEXT,AS,RS,BIDS,CONN,IS,BC,SDK,BOL,SSMS,ADV_SSMS,SNAC_SDK,OCS   ^ ");
			printWriter.flush();
			printWriter.close();

			File deleteFileScript = new File(delInstallFileScript); // ж��֮��ɾ���ļ��Ľű�
			if (deleteFileScript.exists())
				deleteFileScript.delete();
			deleteFileScript.createNewFile();
			PrintWriter printWriter1 = new PrintWriter(delInstallFileScript);
			printWriter1
					.println("rd/s/q \"C:\\Program Files\\Microsoft SQL Server\"");
			printWriter1
					.println("rd/s/q \"C:\\Program Files (x86)\\Microsoft SQL Server\"");
			printWriter1
					.println("rd/s/q \"C:\\Program Files (x86)\\Microsoft SQL Server Compact Edition\"");
			printWriter1.println("rd/s/q \"" + installPath + "\"");
			printWriter1.flush();
			printWriter1.close();

			File batfile = new File(
					"C:\\setupscripts\\SqlServer2008R2\\unpack.bat"); // ��ѹ�ļ��Ľű�
			if (batfile.exists())
				batfile.delete();
			batfile.createNewFile();
			FileOutputStream outStr = new FileOutputStream(batfile);
			BufferedOutputStream buff = new BufferedOutputStream(outStr);
			buff.write("C: \r\n cd C:\\Program Files (x86)\\WinRAR\r\n"
					.getBytes()); // ע������ط���C:\\Program Files����C:\\Program
									// Files(X86)
			// buff.write(("winrar x -o+" + zipFilePathPre + zipFileName + " * "
			// + "C:\\softsource\\SqlServer2008R2\\")
			// .getBytes()); // ע��unzipDiretoryĩβ�����б��
			buff.write(("winrar x -o+ " + zipFilePathPre + zipFileName + " * "
					+ installPath + "\\SqlServer2008R2\\").getBytes()); // ע��unzipDiretoryĩβ�����б��
			// installPathĩβ�����б��
			// 20150618
			buff.flush();
			buff.close();
			outStr.close();
			System.out.println("1��ʼ��ѹ���ļ�");
			wexe.executeVMScript("C:\\setupscripts\\SqlServer2008R2\\unpack.bat");
			Thread.sleep(1000000);

			File setupScriptfile = new File(
					"C:\\setupscripts\\SqlServer2008R2\\setupSqlServer2008R2.bat"); // д��װ�ű�
			if (setupScriptfile.exists())
				setupScriptfile.delete();
			setupScriptfile.createNewFile();
			PrintWriter printWriter2 = new PrintWriter(setupScriptfile);
			// printWriter2.println("C:\\softsource\\SqlServer2008R2\\setup.exe  ^");
			printWriter2.println(installPath + "\\SqlServer2008R2\\"
					+ "setup.exe ^");// installPath
			// �������İ�װ·��
			// ��"c:\\zhangke"
			printWriter2.println("/IACCEPTSQLSERVERLICENSETERMS ^");
			printWriter2.println("/q ^"); // ע��������ط��޽������/q���н������/qs
			printWriter2.println("/ACTION=Install ^");
			printWriter2
					.println("/FEATURES=SQLENGINE,REPLICATION,FULLTEXT,AS,RS,BIDS,CONN,IS,BC,SDK,BOL,SSMS,ADV_SSMS,SNAC_SDK,OCS  ^");
			printWriter2.println("/INSTANCENAME=\"MSSQLSERVER\" ^");
			printWriter2
					.println("/AGTSVCACCOUNT=\"NT AUTHORITY\\NETWORK SERVICE\" ^");
			printWriter2
					.println("/ASSVCACCOUNT=\"NT AUTHORITY\\NETWORK SERVICE\"  ^");
			printWriter2
					.println("/SQLSVCACCOUNT=\"NT AUTHORITY\\NETWORK SERVICE\" ^");
			printWriter2
					.println("/ISSVCACCOUNT=\"NT AUTHORITY\\NETWORK SERVICE\" ^");
			printWriter2
					.println("/RSSVCACCOUNT=\"NT AUTHORITY\\NETWORK SERVICE\" ^");

			printWriter2.println("/SAPWD=" + "\"" + password + "\"  ^");
			printWriter2.println("/SECURITYMODE=\"SQL\"   ^"); // �����û���¼ģʽ�ǻ��ģʽ
			printWriter2.println("/INSTALLSHAREDDIR=" + "\"" + installPath
					+ "\\ProFiles" + "\"  ^");
			printWriter2.println("/INSTALLSHAREDWOWDIR=" + "\"" + installPath
					+ "\\ProFiles(x86)" + "\"  ^");
			printWriter2.println("/INSTANCEDIR=" + "\"" + installPath
					+ "\\ProFiles" + "\"  ^");
			printWriter2
					.println("/ASDATADIR=" + "\"" + installPath
							+ "\\ProFiles\\MSAS10_50.MSSQLSERVER\\OLAP\\Data"
							+ "\"  ^");
			printWriter2.println("/ASLOGDIR=" + "\"" + installPath
					+ "\\ProFiles\\MSAS10_50.MSSQLSERVER\\OLAP\\Log" + "\"  ^");
			printWriter2.println("/ASBACKUPDIR=" + "\"" + installPath
					+ "\\ProFiles\\MSAS10_50.MSSQLSERVER\\OLAP\\Backup"
					+ "\"  ^");
			printWriter2
					.println("/ASTEMPDIR=" + "\"" + installPath
							+ "\\ProFiles\\MSAS10_50.MSSQLSERVER\\OLAP\\Temp"
							+ "\"  ^");
			printWriter2.println("/ASCONFIGDIR=" + "\"" + installPath
					+ "\\ProFiles\\MSAS10_50.MSSQLSERVER\\OLAP\\Config"
					+ "\"  ^");

			printWriter2.println("/ASSYSADMINACCOUNTS=" + "\"" + hostName
					+ "\\" + userName + "\"   ^");
			printWriter2.println("/SQLSYSADMINACCOUNTS=" + "\"" + hostName
					+ "\\" + userName + "\"   ^");

			printWriter2.println("/AGTSVCSTARTUPTYPE=\"Manual\"   ^");
			printWriter2.println("/ISSVCSTARTUPTYPE=\"Manual\"   ^");
			printWriter2.println("/ASSVCSTARTUPTYPE=\"Manual\"   ^");
			printWriter2.println("/SQLSVCSTARTUPTYPE=\"Manual\"   ^");
			printWriter2.println("/BROWSERSVCSTARTUPTYPE=\"Manual\"   ^");
			printWriter2.println("/RSSVCSTARTUPTYPE=\"Manual\"   ^");

			printWriter2.println("/IACCEPTSQLSERVERLICENSETERMS");
			printWriter2.flush();
			printWriter2.close();

			// System.out.println("2��ʼ��װ");
			String[] r = wexe.executeVMScript(appConfigFilePath
					+ "SqlServer2008R2\\setupSqlServer2008R2.bat");
			if (r[0].equals("0x0500000")) {
				return "0x0100A00"; // �ű�ִ�гɹ������ذ�װ�ɹ�
			} else {
				return "0x0100A01"; // ��װ�ű�ִ�в��ɹ������ذ�װʧ��
			}

		} catch (IOException e) {
			e.printStackTrace();
			return "0x0100A02";// ���ذ�װSqlServer2008R2����IOException
		}
		// catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// return "0x0100A06"; //�����߳�����ʱ����ϵ�InterruptedException�쳣
		// }

	}

}
