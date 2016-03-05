package edu.xidian.windows; //张珂

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
	 * @author wzy yixiugai show mysql 安装
	 * @appConfigFilePath 所有的配置文件存放的位置
	 * @param //zipFilePathPre
 压缩包存放的位置
	 * @param unzipDirectory
	 *            解压之后的位置
	 * @param ZipFileName
	 *            压缩包的名字
	 * @param password
	 *            数据库的设置的密码
	 * @param //mySQLPath
 数据库的向环境变量中写入变量的脚本文件
	 * @param //mySQLexchangePath 
 切换到bin文件下的脚本文件
	 * @param //mySQLInstallPath
 数据库执行安装的脚本
	 * @param m
	 *            //ySQLStartPath 数据库启动的脚本文件
	 * @param mySQLPwdPath
	 *            数据库设置密码的脚本文件
	 * @return 0x0100204 安装mysql失败 0x0100200 安装mysql成功 //0x0100201
	 *         mysql没有安装，存在注册表 //0x0100202:系统装有mysql 0x0100203 系统没有装mysql
	 */
	public String setupMySql(String zipFileName, String unzipDirectory,
			String password, String oldPath) throws Exception {

		String appConfigFilePath = "C:\\setupscripts\\";
		String zipFilePathPre = "C:\\softsource\\";
		String code = "";// 返回最终的编码
		PreInstall pre = new PreInstall();
		WAppUninstall un = new WAppUninstall();
		unzipDirectory = unzipDirectory.replace("/", "\\\\");
		if (oldPath != null) {
			if (!oldPath.equals("null")) {
				un.uninstallMySql(oldPath);
			}
		}

		String preinstall = pre.checkMySQL();
		System.out.println("安装检测完成!!!!!!!!!!!!");
		if (preinstall != "0x0100203") {
			return preinstall;// 如果不适合安装,返回具体编码
		} else {
			/**
			 * 解压安装包
			 */
			try {
				System.out.println(" 解压    ：" + zipFilePathPre + zipFileName);
				UnZip.unZip(zipFilePathPre + zipFileName, unzipDirectory + "\\");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			/**
			 * 通过压缩包来获取安装包的名字,并把路径写到配置文件中 length 是压缩包名字的长度 unzipFileName 安装包的名字
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
			 * mySQL建立四个执行的脚本
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
			 * MySQL安装脚本的路径
			 */
			String mySQLPath = appConfigFilePath + "MySQL\\path.bat";
			String mySQLexchangePath = appConfigFilePath + "MySQL\\change.bat";

			/**
			 * 依次执行脚本文件进行mysql的安装 1.执行向环境变量path中写入mysql的位置
			 * 2.切换到Mysql的bin文件下3.执行mysql的安装脚本 4.执行mysql的启动脚本5.执行mysql的设置密码脚本
			 */

			mysqlPath = unzipDirectory + "\\" + unzipFileName + "\\bin;";
			boolean Ismysql = tp.charge(mysqlPath, str);

			System.out.println("~~~~~~~~~~~~~~~~~~Ismysql" + Ismysql);
			System.out.println("~~~~~~~~~~~~~~~~~~mysqlPath" + mysqlPath);

			if (!Ismysql) {

				System.out.println("step1:配置path路径");
				wexe.executeVMScript(mySQLPath);// 里面放的是配置文件的位置
				Thread.sleep(10000);
			}

			System.out
					.println("step2:切换到Mysql的bin文件下,step3:执行mysql的安装脚本,step4:执行mysql的启动脚本,step5:执行mysql的设置密码脚本");
			wexe.executeVMScript(mySQLexchangePath);// 切换地址到mysql的bin文件下
			Thread.sleep(10000);

			String testCode = pre.checkMySQL();
			if (testCode != "0x0100202") {
				System.out.println("安装失败");
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
	// String code = "";//返回最终的编码
	// PreInstall pre = new PreInstall();
	// String preinstall = pre.checkMySQL();
	// WAppUninstall un = new WAppUninstall();
	// if (preinstall != "0x0100203") {
	// return preinstall;// 如果不适合安装,返回具体编码
	// } else {
	// /**
	// * 解压安装包
	// */
	// try {
	// UnZip.unZip(zipFilePathPre + zipFileName, unzipDirectory + "\\");
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// /**
	// * 通过压缩包来获取安装包的名字,并把路径写到配置文件中 length 是压缩包名字的长度 unzipFileName 安装包的名字
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
	// * mySQL建立四个执行的脚本
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
	// * MySQL安装脚本的路径
	// */
	// String mySQLPath = appConfigFilePath + "MySQL\\path.bat";
	// String mySQLexchangePath = appConfigFilePath + "MySQL\\change.bat";
	// //String mySQLInstallPath = appConfigFilePath + "MySQL\\install.bat";
	// //String mySQLStartPath = appConfigFilePath + "MySQL\\start.bat";
	// //String mySQLPwdPath = appConfigFilePath + "MySQL\\pw.bat";
	//
	// /**
	// * 依次执行脚本文件进行mysql的安装 1.执行向环境变量path中写入mysql的位置
	// 2.切换到Mysql的bin文件下3.执行mysql的安装脚本
	// * 4.执行mysql的启动脚本5.执行mysql的设置密码脚本
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
	// System.out.println("step1:配置path路径");
	// wexe.executeVMScript(mySQLPath);// 里面放的是配置文件的位置
	// Thread.sleep(10000);
	// }
	//
	// System.out.println("step2:切换到Mysql的bin文件下,step3:执行mysql的安装脚本,step4:执行mysql的启动脚本,step5:执行mysql的设置密码脚本");
	// wexe.executeVMScript(mySQLexchangePath);// 切换地址到mysql的bin文件下
	// Thread.sleep(10000);
	//
	// // System.out.println("step3:执行mysql的安装脚本");
	// // wexe.executeVMScript(mySQLInstallPath);
	// // Thread.sleep(60000);
	// //
	// // System.out.println("step4:执行mysql的启动脚本");
	// // wexe.executeVMScript(mySQLStartPath);
	// // Thread.sleep(60000);
	// //
	// // System.out.println("step5:执行mysql的设置密码脚本");
	// // wexe.executeVMScript(mySQLPwdPath);
	// // Thread.sleep(60000);
	//
	// String testCode = pre.checkMySQL();
	// if(testCode != "0x0100202"){
	// System.out.println("安装失败");
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
	 * wenyanqi---nginx安装 yixiugai
	 */
	public String setupNginx(String zipFileName, String path, String oldpath) {
		/**
		 * zipFileName 软件源文件名称 unzipDirectory 软件安装路径
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

			File filepath = new File("C:\\setupscripts\\nginx\\"); // 脚本语言路径
			if (!filepath.isDirectory()) {
				filepath.mkdir();
			}
			File batfile = new File("C:\\setupscripts\\nginx\\nginx.bat");
			batfile.createNewFile();
			FileOutputStream outStr = new FileOutputStream(batfile);
			BufferedOutputStream buff = new BufferedOutputStream(outStr);

			// 写入脚本操作
			buff.write("C: \r\n cd C:\\Program Files (x86)\\WinRAR\r\n"
					.getBytes());
			buff.write(("winrar x C:\\softsource\\" + zipFileName + " * "
					+ temp_path + "\\").getBytes());
			buff.flush();
			buff.close();
			outStr.close();
			// 执行脚本
			wexe.executeVMScript("C:\\setupscripts\\nginx\\nginx.bat");

			// 判断是否安装成功了，如果未成功，则卸载：
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
			System.out.println("安装失败");
			return "0x0100401";
		} catch (IOException e) {
			return "0x0100401";
		}
	}

	/**
	 * @author wzy show Memcached 64位安装
	 * @param FileName
	 *            memcached的名字
	 * @return 0x0100700:安装成功
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
		// 判断所给路径是否存在，不存在则建立路径
		File file_path = new File(path);
		if (!file_path.isDirectory()) {
			file_path.mkdirs();
		}
		String code = "";

		File f2 = new File("C:\\setupscripts\\memcached"); // 创建安装脚本文件夹
		if (!f2.isDirectory()) {
			f2.mkdir();
		}
		File f3 = new File("C:\\setupscripts\\memcached\\memcached.bat");
		if (!f3.isFile()) {
			f3.createNewFile();
		}
		// 找到可以执行的文件名称
		String exe_filename = path + "\\"
				+ FileName.substring(0, FileName.length() - 4) + "\\"
				+ FileName.substring(0, FileName.length() - 4);

		String command = exe_filename + " -d install"; // 安装
		String unzip1 = "cd C:\\Program Files (x86)\\WinRAR"; // 找到压缩程序
		String unzip2 = "winrar x C:\\softsource\\" + FileName + " * " + path; // 压缩到指定路径之中

		// 写脚本
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
		// 执行脚本
		String jdkCommand = "cmd /c  C:/setupscripts/memcached/memcached.bat";
		System.out.println(jdkCommand);
		WVMScriptExecute we = new WVMScriptExecute();
		we.executeVMScript(jdkCommand);

		// 检测memcached是否安装成功

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
	 * @author ZXQ 安装Tomcat yixuigai
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public String setupTomcat(String TomcatZip, String TomcatPath,
			String JDKPath, String oldpath) throws IOException {
		System.out.println("oldpath ==========" + oldpath);

		if (oldpath != null) {
			if (!oldpath.equals("null")) {
				System.out.println("安装中卸载tomcat");
				WAppUninstall wu = new WAppUninstall();
				wu.uninstallTomcat(oldpath);

			}
		}
		// 获得Tomcat文件夹名称
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

		// 解压
		try {
			UnZip.unZip("C:/softsource/" + TomcatZip, TomcatPath);
		} catch (Exception e) {
			e.printStackTrace();
			WAppUninstall un = new WAppUninstall();// 失败则删除文件
			un.uninstallTomcat(TomcatPath);
			return "0x0100001";
		}

		// 写脚本（配置环境变量）
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
		// 执行脚本
		String tomcatCommand = "cmd /c  C:/setupscripts/tomcat/tomcat.bat";
		System.out.println(tomcatCommand);
		WVMScriptExecute we = new WVMScriptExecute();
		we.executeVMScript(tomcatCommand);

		// 修改startup.bat
		UpdateAppBat.updateBatFile(TomcatPath + File.separator + TomcatDirName,
				JDKPath, "startup.bat");
		// 修改shutdown.bat
		UpdateAppBat.updateBatFile(TomcatPath + File.separator + TomcatDirName,
				JDKPath, "shutdown.bat");
		// 修改service.bat
		UpdateAppBat.updateBatFile(TomcatPath + File.separator + TomcatDirName,
				JDKPath, "service.bat");
		// 修改version.bat
		UpdateAppBat.updateBatFile(TomcatPath + File.separator + TomcatDirName,
				JDKPath, "version.bat");
		// 判断是否安装成功,同时安装服务
		StringBuffer strBuffer1 = new StringBuffer();
		strBuffer1.append("call cd " + TomcatPath + File.separator
				+ TomcatDirName + "/bin");
		strBuffer1.append(System.getProperty("line.separator"));
		strBuffer1.append("call version");// 查看版本信息
		strBuffer1.append(System.getProperty("line.separator"));
		strBuffer1.append("call service install");// 安装服务
		strBuffer1.append(System.getProperty("line.separator"));
		PrintWriter printWriter1 = new PrintWriter(
				"C:/setupscripts/tomcat/tomcat-version.bat");
		printWriter1.write(strBuffer1.toString().toCharArray());
		printWriter1.flush();
		printWriter1.close();
		// 执行脚本
		String tomcatCommand1 = "cmd /c C:/setupscripts/tomcat/tomcat-version.bat";
		Process process = Runtime.getRuntime().exec(tomcatCommand1);
		BufferedReader strCon = new BufferedReader(new InputStreamReader(
				process.getInputStream()));
		String line = "";
		// 等候
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
		// 失败则删除文件
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
			// 1.判断php路径是否存在
			File filepath1 = new File(phppath);
			if (!filepath1.isDirectory()) {
				return "0x0100502";
			}
			// 2.判断php配置文件是否存在
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

			// 检测
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
	// // 1.判断php路径是否存在
	// File filepath1 = new File(phppath);
	// if (!filepath1.isDirectory()) {
	// return "0x0100502";
	// }
	// // 2.判断php配置文件是否存在
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

	// 查询oracle版本号
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
		 * 参数说明： zipFileName——软件安装包的名字，不带路径 unzipDirectory——软件解压的路径，有路径，且必须以\结束
		 * hostname——示例：wenyanqi-PC inventorypath——示例：C:\Program
		 * Files\Oracle\Inventory oraclebase——C:\app\wenyanqi
		 * oraclehome——只传输名字，再与oraclebase拼接
		 * 。例如:C:\app\wenyanqi\product\11.2.0\dbhome_1,则oraclehome=dbhome1
		 * password——用户密码，不少于8个字符，并且必须有一个大写字母，一个小写字母
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
					System.out.println("安装中卸载oracle");
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
		 * 安装前检测环境 1.检测密码是否符合要求至少8位，且有数字，大写字母，小写字母 密码不符合规则，返回0x0100C01
		 * 2.检测是否有足够的硬盘空间 0x0100C02 3.检测是否有足够的内存空间 0x0100C03
		 */

		if (password.length() < 8) {
			return "0x0100C01";
		} else {
			boolean flag = false;
			// 判断是否包含字母和数字以外的符号
			flag = password.matches("[a-z0-9A-Z\u4e00-\u9fa5]+$");
			if (flag == false) {
				return "0x0100C01";
			} else {
				// 判断是否包含数字
				flag = password.matches(".*\\d+.*");
				if (flag == false) {
					return "0x0100C01";
				} else {
					// 判断是否包含小写字母
					flag = password.matches(".*[a-z].*");
					if (flag == false) {
						return "0x0100C01";
					} else {
						// 判断是否包含大写字母
						flag = password.matches(".*[A-Z].*");
						if (flag == false) {
							return "0x0100C01";
						}
					}
				}
			}

		}

		// 判断内存大小
		OperatingSystemMXBean osmb = (OperatingSystemMXBean) ManagementFactory
				.getOperatingSystemMXBean();
		System.out.println("系统物理内存总计：" + osmb.getTotalPhysicalMemorySize()
				/ 1024 / 1024 + "MB");

		if (osmb.getTotalPhysicalMemorySize() < 1024 * 1024 * 1024) {
			System.out.println("内存不足，系统物理内存总计："
					+ osmb.getTotalPhysicalMemorySize() / 1024 / 1024 + "MB");
			return "0x0100C02";
		}

		// 判断硬盘大小
		File[] roots = File.listRoots();// 获取磁盘分区列表
		for (File file : roots) {
			if (file.getPath().equals(hostname.substring(0, 3))) {
				if (file.getFreeSpace() / 1024 / 1024 / 1024 < 7) {
					System.out.println(file.getPath() + "信息如下:");
					System.out.println("所选硬盘空间不足，空闲未使用 = "
							+ file.getFreeSpace() / 1024 / 1024 / 1024 + "G");// 空闲空间
					return "0x0100C03";
				}

			}
		}
		// 检测系统残留项

		try {
			// 创建脚本的根目录
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
			// 新建解压脚本，解压oracle安装包
			batfile1.createNewFile();
			buff1.write("C: \r\n cd C:\\Program Files (x86)\\WinRAR\r\n"
					.getBytes());
			buff1.write(("winrar x -o+ C:\\softsource\\" + zipFileName1 + " C:\\softsource\\oracle_11g\r\n")
					.getBytes()); // 注释unzipDiretory末尾必须带\
			buff1.write(("winrar x -o+ C:\\softsource\\" + zipFileName2 + " C:\\softsource\\oracle_11g")
					.getBytes()); // 注释unzipDiretory末尾必须带\
			buff1.flush();
			buff1.close();
			outStr1.close();

			System.out.println("oracle安装文件开始解压");
			wexe.executeVMScript("C:\\setupscripts\\oracle\\oracleunzip.bat");
			Thread.sleep(900000);

			// 新建执行脚本,使用rsp文件通过静默安装oracle
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

			System.out.println("oracle安装文件解压完成，开始生成rsp文件");
			// 更改rsp文件，即安装时需要的参数
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

			// 执行脚本，完成安装。一般需要大概十分钟
			System.out.println("oracle安装脚本开始执行");
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
	 * @author ZXQ 安装Apache yixiugai
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

		// 删除原有软件
		System.out.println("emailAddress:" + emailAddress);
		File f = new File("C:/setupscripts/apache");
		if (!f.isDirectory()) {
			f.mkdirs();
		}
		// 写脚本
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
		// 执行脚本
		String jdkCommand = "cmd /c  C:/setupscripts/apache/apache.bat";
		System.out.println(jdkCommand);
		String[] ss = we.executeVMScript(jdkCommand);
		if (!(ss[0].equals("0x0500000"))) {
			WAppUninstall un = new WAppUninstall();
			un.uninstallApache(ApachePath);
			return "0x0100301";
		}

		// // 修改serveradmin配置
		boolean flag = AppConfig.judgeExist(ApachePath + "/conf/httpd.conf",
				"ServerAdmin");
		if (flag) {
			flag = AppConfig.replaceToFile(ApachePath + "/conf/httpd.conf",
					"ServerAdmin", "ServerAdmin " + emailAddress);
		} else {
			flag = AppConfig.appendToFile(ApachePath + "/conf/httpd.conf",
					"ServerAdmin " + emailAddress);
		}
		// 修改servername配置
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

		// 安装apache服务
		String serviceCommand = ApachePath + "/bin/httpd.exe -k install";
		System.out.println(serviceCommand);
		Runtime.getRuntime().exec(serviceCommand);
		// 判断是否成功
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
		// 解压
		try {
			UnZip.unZip("C:/softsource/" + ApacheFileName, ApachePath);
		} catch (Exception e) {
			e.printStackTrace();
			WAppUninstall un = new WAppUninstall();
			un.uninstallApache(ApachePath);
			return "0x0100301";
		}

		// 修改ServerAdmin配置
		boolean flag = AppConfig.judgeExist(ApachePath + "/conf/httpd.conf",
				"ServerAdmin");
		if (flag) {
			flag = AppConfig.replaceToFile(ApachePath + "/conf/httpd.conf",
					"ServerAdmin", "ServerAdmin " + emailAddress);
		} else {
			flag = AppConfig.appendToFile(ApachePath + "/conf/httpd.conf",
					"ServerAdmin " + emailAddress);
		}
		// 修改ServerName配置
		flag = AppConfig.judgeExist(ApachePath + "/conf/httpd.conf",
				"ServerName");
		if (flag) {
			flag = AppConfig.replaceToFile(ApachePath + "/conf/httpd.conf",
					"ServerName", "ServerName 127.0.0.1:80");
		} else {
			flag = AppConfig.appendToFile(ApachePath + "/conf/httpd.conf",
					"ServerName 127.0.0.1:80");
		}
		// 修改ServerRoot配置
		AppConfig.replaceToFile(ApachePath + "/conf/httpd.conf", "ServerRoot",
				"ServerRoot " + "\"" + ApachePath + "\"");

		// 修改DocumentRoot配置
		AppConfig.replaceToFile(ApachePath + "/conf/httpd.conf",
				"DocumentRoot", "DocumentRoot " + "\"" + ApachePath
						+ "/htdocs\"");

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 安装apache服务
		String serviceCommand = ApachePath + "/bin/httpd.exe -k install";
		System.out.println(serviceCommand);
		Runtime.getRuntime().exec(serviceCommand);
		// 判断安装是否成功
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
		// 失败则删除所有安装文件
		WAppUninstall un = new WAppUninstall();
		un.uninstallApache(ApachePath);
		return "0x0100301";
	}

	/**
	 * @author ZXQ 安装 Python yixiugai
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
		// 写脚本
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
		// 执行脚本
		String pythonCommand = "cmd /c  C:/setupscripts/python/python.bat";
		System.out.println(pythonCommand);
		WVMScriptExecute we = new WVMScriptExecute();
		String[] ss = we.executeVMScript(pythonCommand);

		// 检测安装是否成功
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
		// 执行脚本
		String tomcatCommand = "cmd /c C:/setupscripts/python/python-version.bat";
		Process process = Runtime.getRuntime().exec(tomcatCommand);
		BufferedReader strCon = new BufferedReader(new InputStreamReader(
				process.getInputStream()));
		String line = "";
		boolean flag = false;
		while ((line = strCon.readLine()) != null) {
			if (line.toLowerCase().contains("python")) {
				System.out.println("安装成功");
				flag = true;
			}
		}
		if (!flag) {
			System.out.println("安装失败");
			new WAppUninstall().uninstallPython(path);
		}

		if (!(ss[0].equals("0x0500000"))) {
			return "0x0100601";
		}
		return "0x0100600";
	}

	public String setupFTP(String FTPFileName, String FTPPath, String oldPath)
			throws IOException {
		// 如果旧路径不为空，删除
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
		// 执行脚本
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
		// 执行脚本
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
	 * @author hp 安装iisRewrite
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
		// 写脚本
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
		// 执行脚本
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
	 *            传进来的是压缩文件的名字
	 * @param installPath
	 *            传进来的安装路径 如"c:\\zhangke"
	 * @param password
	 *            默认的是混合登录方式，需要用户传进密码
	 * @param hostName
	 *            启动服务和登录账户的主机名
	 * @param userName
	 *            用户名
	 * @return
	 * @throws IOException
	 *             无界面安装SqlServer2008R2的方法中写了卸载脚本、删除卸载软件后的文件、解压脚本和安装脚本。
	 *             其中解压脚本和安装脚本是在安装中用的，卸载脚本和删除文件的脚本是在卸载文件的时候用的
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
		// String info = "已安装的SqlServer2008R2   " + line;
		// System.out.println(info);// 如果已安装SqlServer2008R2，则输入osql
		// // /?则输出的第二行为已安装的版本信息
		// return "0x0100A04";// 已安装过 无需再静默安装
		// }
		// i++;
		// }
		// } catch (IOException e) {
		//
		// }
		// 解压之后的安装包文件大小是5.7G
		// 但是安装过程中安装需要的安装空间要求有7G
		double needSpace = 8; // 如果本地已经有压缩包但是没有解压则需要13G的空间，
								// 如果本地压缩包已经解压好则只需要7G的空间
		double constm = 1024 * 1024 * 1024;// constm=1G
		String sdisk = installPath.substring(0, 2);
		File _file = new File(sdisk);
		double freeSpace = _file.getFreeSpace() / constm;
		if (freeSpace < needSpace) {
			System.out.println("freeSpace " + freeSpace);
			System.out.println("硬盘剩余空间不足，不可进行安装");
			return "0x0100A03";// 返回安装所需空间不足，还未进行安装
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

			File uninstallFile = new File(uninstallSqlScript); // 卸载脚本
																// 先在这个地方写好卸载脚本
			if (uninstallFile.exists())
				uninstallFile.delete();
			uninstallFile.createNewFile();
			PrintWriter printWriter = new PrintWriter(uninstallFile);
			// printWriter.println("c:\\softsource\\SqlServer2008R2\\setup.exe ^");
			printWriter.println(installPath + "\\SqlServer2008R2\\setup.exe ^");
			// printWriter.println(installPath+"setup.exe ^");//installPath
			// 传进来的安装路径 如"c:\\zhangke\\" 最后一定要加 \\ 20150618
			printWriter.println("/q ^"); // 注意在这个地方无界面的是/q，有界面的是/qs
			printWriter.println("/Action=Uninstall ^");
			printWriter.println("/INSTANCENAME=MSSQLSERVER  ^");
			printWriter
					.println("/FEATURES=SQLENGINE,REPLICATION,FULLTEXT,AS,RS,BIDS,CONN,IS,BC,SDK,BOL,SSMS,ADV_SSMS,SNAC_SDK,OCS   ^ ");
			printWriter.flush();
			printWriter.close();

			File deleteFileScript = new File(delInstallFileScript); // 卸载之后删除文件的脚本
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
					"C:\\setupscripts\\SqlServer2008R2\\unpack.bat"); // 解压文件的脚本
			if (batfile.exists())
				batfile.delete();
			batfile.createNewFile();
			FileOutputStream outStr = new FileOutputStream(batfile);
			BufferedOutputStream buff = new BufferedOutputStream(outStr);
			buff.write("C: \r\n cd C:\\Program Files (x86)\\WinRAR\r\n"
					.getBytes()); // 注意这个地方是C:\\Program Files还是C:\\Program
									// Files(X86)
			// buff.write(("winrar x -o+" + zipFilePathPre + zipFileName + " * "
			// + "C:\\softsource\\SqlServer2008R2\\")
			// .getBytes()); // 注释unzipDiretory末尾必须带斜线
			buff.write(("winrar x -o+ " + zipFilePathPre + zipFileName + " * "
					+ installPath + "\\SqlServer2008R2\\").getBytes()); // 注释unzipDiretory末尾必须带斜线
			// installPath末尾必须带斜线
			// 20150618
			buff.flush();
			buff.close();
			outStr.close();
			System.out.println("1开始解压缩文件");
			wexe.executeVMScript("C:\\setupscripts\\SqlServer2008R2\\unpack.bat");
			Thread.sleep(1000000);

			File setupScriptfile = new File(
					"C:\\setupscripts\\SqlServer2008R2\\setupSqlServer2008R2.bat"); // 写安装脚本
			if (setupScriptfile.exists())
				setupScriptfile.delete();
			setupScriptfile.createNewFile();
			PrintWriter printWriter2 = new PrintWriter(setupScriptfile);
			// printWriter2.println("C:\\softsource\\SqlServer2008R2\\setup.exe  ^");
			printWriter2.println(installPath + "\\SqlServer2008R2\\"
					+ "setup.exe ^");// installPath
			// 传进来的安装路径
			// 如"c:\\zhangke"
			printWriter2.println("/IACCEPTSQLSERVERLICENSETERMS ^");
			printWriter2.println("/q ^"); // 注意在这个地方无界面的是/q，有界面的是/qs
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
			printWriter2.println("/SECURITYMODE=\"SQL\"   ^"); // 设置用户登录模式是混合模式
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

			// System.out.println("2开始安装");
			String[] r = wexe.executeVMScript(appConfigFilePath
					+ "SqlServer2008R2\\setupSqlServer2008R2.bat");
			if (r[0].equals("0x0500000")) {
				return "0x0100A00"; // 脚本执行成功，返回安装成功
			} else {
				return "0x0100A01"; // 安装脚本执行不成功，返回安装失败
			}

		} catch (IOException e) {
			e.printStackTrace();
			return "0x0100A02";// 返回安装SqlServer2008R2出现IOException
		}
		// catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// return "0x0100A06"; //返回线程休眠时被打断的InterruptedException异常
		// }

	}

}
