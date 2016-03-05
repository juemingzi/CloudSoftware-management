package edu.xidian.linux;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import edu.xidian.windows.AppConfig;

public class LAppUninstall {
	/**
	 * @author wzy show 卸载mysql
	 * @return 0x0200200 mysql卸载成功 0x0200204 mysql卸载失败
	 */
	public static String uninstallMySql(String uninstallPath) throws Exception {
		PreInstall pre = new PreInstall();
		String str = pre.checkMysql();
		String flag = "";
		if (str != "") {
			String s[] = str.split(";");

			Process process = null;
			process = Runtime.getRuntime().exec("service mysql stop");
			process.waitFor();
			Thread.sleep(3000);

			for (int i = 0; i < s.length; i++) {
				process = Runtime.getRuntime().exec("rpm -ev --nodeps " + s[i]);
				System.out.println(s[i] + "------" + i);
				process.waitFor();
				Thread.sleep(3000);
			}

			process = Runtime.getRuntime().exec("rm -rf /etc/my.cnf -R");
			process.waitFor();
			Thread.sleep(3000);

			// delete file
			process = Runtime.getRuntime().exec("rm -rf /var/lib/mysql -R");
			process.waitFor();
			Thread.sleep(3000);
			System.out.println("delete file success");

			String is = pre.checkMysql();
			if (is != "") {
				flag = "0x0200204";
				System.out.println("fail to uninstall mysql");
			} else {
				flag = "0x0200200";
				System.out.println("success to uninstall mysql");
			}
		}
		return flag;

	}

	public String uninstallTomcat(String uninstallPath) throws Exception {
		//创建卸载脚本所在目录
		String[] flag = null;
		File filepath = new File("/home/setupscripts/tomcat");
		if (!filepath.isDirectory()) {
			filepath.mkdirs();
		}
		//创建卸载脚本文件
		File batfile = new File("/home/setupscripts/tomcat/untomcat.sh");
		batfile.createNewFile();
		LVMScriptExecute le = new LVMScriptExecute();
		String apachedirname = "";
		System.out.println("uninstallPath  ==    " + uninstallPath);
		//获取Tomcat文件名，编写执行卸载的语句，停止tomcat服务
		File[] files = new File(uninstallPath).listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.getName().startsWith("apache") && file.isDirectory()) {
					apachedirname = file.getName();
				}
			}
			String strCmdPath = uninstallPath + "/" + apachedirname
					+ "/bin/shutdown.sh";
			System.out.println(strCmdPath);
			le.executeVMScript(strCmdPath);
		}

		StringBuffer strBuffer = new StringBuffer();
		// strBuffer.append("find " + uninstallPath
		// + " -name \"apache-tomcat*\" -exec rm -rf {} \\;");
		
		//删除文件夹下所有文件
		strBuffer.append("rm -rf " + uninstallPath + ";");
		strBuffer.append(System.getProperty("line.separator"));
		PrintWriter printWriter = new PrintWriter(
				"/home/setupscripts/tomcat/untomcat.sh");
		printWriter.write(strBuffer.toString().toCharArray());
		printWriter.flush();
		printWriter.close();

		String strCmdPath1 = "/home/setupscripts/tomcat/untomcat.sh";
		// LVMScriptExecute le = new LVMScriptExecute();
		flag = le.executeVMScript(strCmdPath1);//执行删除脚本

		if (flag[0].equals("0x0500000"))
			return "0x0200000";
		else
			return "0x0200001";
	}

	public String uninstallApache(String uninstallPath) {
		try {

			String apachePath = uninstallPath;
			System.out.println(apachePath);
			//创建删除脚本所在目录
			File filepath = new File("/home/setupscripts/apache");
			if (!filepath.isDirectory()) {
				filepath.mkdirs();
			}
			//创建删除脚本
			File batfile = new File("/home/setupscripts/apache/unapache.sh");
			batfile.createNewFile();
			//编写删除脚本，停止apache服务
			try {
				String strCmdPath = uninstallPath
						+ "/httpd/bin/apachectl  stop";
				System.out.println(strCmdPath);
				Runtime.getRuntime().exec(strCmdPath);

			} catch (Exception e) {
				e.printStackTrace();
			}

			StringBuffer strBuffer = new StringBuffer();
			// strBuffer.append("find " + apachePath
			// + " -type d  -name \"httpd*\" -exec rm -rf {} \\;");
			//删除文件夹下所有文件
			strBuffer.append("rm -rf " + uninstallPath + ";");
			strBuffer.append(System.getProperty("line.separator"));
			PrintWriter printWriter = new PrintWriter(
					"/home/setupscripts/apache/unapache.sh");
			printWriter.write(strBuffer.toString().toCharArray());
			printWriter.flush();
			printWriter.close();
			//执行卸载脚本
			String strCmdPath1 = "/home/setupscripts/apache/unapache.sh";
			LVMScriptExecute le = new LVMScriptExecute();
			le.executeVMScript(strCmdPath1);
			return "0x0200300";
		} catch (Exception e) {
			return "0x0200301";
		}

	}

	public String uninstallPython(String FilePath) {
		try {
			File filepath = new File("/home/setupscripts/python");
			if (!filepath.isDirectory()) {
				filepath.mkdirs();
			}
			File batfile = new File("/home/setupscripts/python/unpython.sh");
			batfile.createNewFile();
			StringBuffer strBuffer = new StringBuffer();
			strBuffer.append("find " + FilePath
					+ "/ -name \"*Python*\" -exec rm -rf {} \\;");
			strBuffer.append(System.getProperty("line.separator"));
			PrintWriter printWriter = new PrintWriter(
					"/home/setupscripts/python/unpython.sh");
			printWriter.write(strBuffer.toString().toCharArray());
			printWriter.flush();
			printWriter.close();
			String strCmdPath = "/home/setupscripts/python/unpython.sh";
			LVMScriptExecute le = new LVMScriptExecute();
			le.executeVMScript(strCmdPath);
			return "0x0200600";
		} catch (Exception e) {
			return "0x0200601";
		}
	}
	
	//uninstallPath：卸载路径
	public String uninstallFTP(String uninstallPath) throws Exception {
		try {
			File filepath = new File("/home/uninstallscripts/vsftpd");
			if (!filepath.isDirectory()) {
				filepath.mkdirs();
			}
			
			//编写卸载脚本
			File batfile = new File("/home/uninstallscripts/vsftpd/unvsftpd.sh");
			batfile.createNewFile();
			StringBuffer strBuffer = new StringBuffer();

			strBuffer.append("rm -rf /usr/local/sbin/vsftpd;");
			strBuffer.append(System.getProperty("line.separator"));

			strBuffer.append("rm -rf /etc/vsftpd.conf;");
			strBuffer.append(System.getProperty("line.separator"));

			strBuffer.append("rm -rf /etc/xinetd.d/vsftpd;");
			strBuffer.append(System.getProperty("line.separator"));

			strBuffer.append("rm -rf " + uninstallPath);
			strBuffer.append(System.getProperty("line.separator"));

			PrintWriter printWriter = new PrintWriter(
					"/home/uninstallscripts/vsftpd/unvsftpd.sh");
			printWriter.write(strBuffer.toString().toCharArray());
			printWriter.flush();
			printWriter.close();
			
			//执行卸载脚本
			String strCmdPath = "/home/uninstallscripts/vsftpd/unvsftpd.sh";
			LVMScriptExecute le = new LVMScriptExecute();
			le.executeVMScript(strCmdPath);
			return "0x0200900";
		} catch (Exception e) {
			return "0x0200901";
		}
	}

	// uninstall zendguardloader

	public String uninstallZendGuardLoader(String phppath) {
		LVMScriptExecute lexe = new LVMScriptExecute();
		try {
			// 1.判断php路径是否存在
			File filepath1 = new File(phppath);
			if (!filepath1.isDirectory()) {
				return "0x0200502";
			}
			// 2.判断php配置文件是否存在
			filepath1 = new File(phppath + "/lib/php.ini");
			if (!filepath1.isFile()) {
				return "0x0200503";
			}

			File filepath2 = new File("/home/uninstallscripts/zendguardloader");
			if (!filepath2.isDirectory()) {
				filepath2.mkdir();
			}
			// 1.删除文件
			File uninstall = new File(
					"/home/uninstallscripts/zendguardloader/uninstall.sh");
			FileOutputStream outStr;
			outStr = new FileOutputStream(uninstall);
			BufferedOutputStream buff = new BufferedOutputStream(outStr);

			uninstall.createNewFile();
			buff.write(("rm -f " + phppath + "/ext/ZendGuardLoader.so\n")
					.getBytes());
			buff.flush();
			buff.close();
			outStr.close();
			lexe.executeVMScript("/home/uninstallscripts/zendguardloader/uninstall.sh");
			// 2.删除配置信息
			AppConfig.replaceToFile(phppath + "/lib/php.ini", "[Zend.loader]",
					"");
			AppConfig.replaceToFile(phppath + "/lib/php.ini",
					"zend_extension=", "");
			AppConfig.replaceToFile(phppath + "/lib/php.ini",
					"zend_loader.enable", "");
			AppConfig.replaceToFile(phppath + "/lib/php.ini",
					"zend_loader.disable_licensing", "");
			AppConfig.replaceToFile(phppath + "/lib/php.ini",
					"zend_loader.obfuscation_level_support", "");

			return "0x0200500";
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "0x0200501";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "0x0200501";
		}

	}

	// uninstall nginx
	public String uninstallNginx(String nginxpath) {
		try {
			LVMScriptExecute lexe = new LVMScriptExecute();

			File filepath1 = new File("/home/uninstallscripts/nginx");
			if (!filepath1.isDirectory()) {
				filepath1.mkdir();
			}

			File batfile = new File("/home/uninstallscripts/nginx/nginx.sh");
			batfile.createNewFile();
			FileOutputStream outStr = new FileOutputStream(batfile);
			BufferedOutputStream buff = new BufferedOutputStream(outStr);
			buff.write("killall nginx\n".getBytes());
			buff.write(("rm -rf " + nginxpath + "\n").getBytes());

			buff.flush();
			buff.close();
			outStr.close();
			lexe.executeVMScript("/home/uninstallscripts/nginx/nginx.sh");
			return "0x0200400";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "0x0200401";
		}

	}

	/**
	 * @author wzy show 卸载memcached
	 * @param fileName
	 *            要卸载的memcached的名字
	 * @param path
	 *            要卸载的memcached的安装路径
	 * @return 0x0200700 memcached卸载成功 0x0200704 memcached卸载失败
	 */
	public static String uninstallMemcached(String path) throws Exception {

		String code = "";
		Process process = null;
		process = Runtime.getRuntime().exec("killall memcached");
		process = Runtime.getRuntime().exec("rm -rf " + path + " -R");
		process.waitFor();

		PreInstall pre = new PreInstall();
		String is = pre.isMemcachedSuccess(path);
		if (is.equals("")) {
			System.out.println("success to uninstall memcached");
			code = "0x0200700";
		} else {
			System.out.println("fail to uninstall memcached");
			code = "0x0200704";
		}
		return code;
	}

	public String uninstallOracle11g(String oldpath) {

		String[] oldpath_a = oldpath.split("&");
		String oraclepath = oldpath_a[0];
		String username = oldpath_a[1];

		System.out.println("oraclepath              username  "+oraclepath+"                "+username);
		FileOutputStream outStr;
		BufferedOutputStream buff;
		BufferedReader strCon;
		String line;
		Process process;
		// String oraclepath = "/u01/app";
		// String user = "wenyanqi";
		try {
			File filepath = new File("/home/uninstallscripts/oracle");

			if (!filepath.isDirectory()) {
				filepath.mkdir();
			}

			filepath = new File(oraclepath);
			if (!filepath.isDirectory()) {
				System.out.println("the oracle path is not exist");
				return "0x0200C01";
			}

			filepath = new File("/home/" + username + "/.bash_profile");
			if (!filepath.isFile()) {
				System.out.println("the username is not exist");
				return "0x0200C02";
			}
			// 1.停止Listener lsnrctl stop
			String cmd = "su - " + username + " -c \"lsnrctl stop\"";
			process = Runtime.getRuntime().exec(cmd);
			strCon = new BufferedReader(new InputStreamReader(
					process.getInputStream()));

			// 2.将安装目录删除rm -rf /u01/app
			// 3.将/usr/bin下的文件删除
			// rm /usr/local/bin/dbhome
			// rm /usr/local/bin/oraenv
			// rm /usr/local/bin/coraenv
			// 4.将/etc/oratab删除rm /etc/oratab
			// 5.将/etc/oraInst.loc删除 rm /etc/oraInst.loc
			File deldir = new File("/home/uninstallscripts/oracle/deldir.sh");
			deldir.createNewFile();
			outStr = new FileOutputStream(deldir);
			buff = new BufferedOutputStream(outStr);

			buff.write(("rm -rf " + oraclepath + "\n").getBytes());
			buff.write("rm /usr/local/bin/dbhome -f\n".getBytes());
			buff.write("rm /usr/local/bin/oraenv -f\n".getBytes());
			buff.write("rm /usr/local/bin/coraenv -f\n".getBytes());
			buff.write("rm /etc/oratab -f\n".getBytes());
			buff.write("rm /etc/oraInst.loc -f\n".getBytes());

			buff.flush();
			buff.close();
			outStr.close();
			LVMScriptExecute lexe = new LVMScriptExecute();
			lexe.executeVMScript("/home/uninstallscripts/oracle/deldir.sh");
			return "0x0200C00";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "0x0200C03";
		}

	}
}
