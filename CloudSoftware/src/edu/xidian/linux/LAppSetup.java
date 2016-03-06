package edu.xidian.linux;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import edu.xidian.linux.LVMScriptExecute;
import edu.xidian.windows.AppConfig;
import edu.xidian.linux.PreInstall;
import edu.xidian.windows.Utils;
import edu.xidian.windows.WVMScriptExecute;

public class LAppSetup {
	LVMScriptExecute lexe = new LVMScriptExecute();
	AppConfig app = new AppConfig();
	String appConfigFilePath = "/home/setupscripts/";
	String zipFilePathPre = "/home/softsource/";

	public String setupNginx(String zipFilename, String unzipDirectory,
			String oldpath) throws InterruptedException {
		try {
			if (oldpath != null) {
				if (!oldpath.equals("null"))
				// 判断是否进行卸载操作，如果oldpath存在，进行卸载再安装
				{
					LAppUninstall lau = new LAppUninstall();
					lau.uninstallNginx(oldpath);
				}
			}
			File filepath = new File("/home/setupscripts/nginx");
			if (!filepath.isDirectory()) {
				filepath.mkdir();
			}

			filepath = new File(unzipDirectory + "/nginx");
			if (!filepath.exists()) {
				filepath.mkdirs();
			}

			File batfile = new File("/home/setupscripts/nginx/nginx.sh");
			batfile.createNewFile();
			FileOutputStream outStr = new FileOutputStream(batfile);
			BufferedOutputStream buff = new BufferedOutputStream(outStr);
			buff.write("yum install gcc -y\n".getBytes());
			buff.write("yum install pcre pcre-devel -y\n".getBytes());
			buff.write("yum install zlib zlib-devel -y\n".getBytes());
			buff.write("yum install openssl openssl-devel -y\n".getBytes());
			buff.write(("tar -zxvf /home/softsource/" + zipFilename + " -C /home/softsource\n")
					.getBytes());
			buff.write(("cd /home/softsource/"
					+ zipFilename.substring(0, zipFilename.length() - 7) + "\n")
					.getBytes());
			buff.write(("./configure --prefix=" + unzipDirectory + "/nginx \n")
					.getBytes());
			buff.write("make\n".getBytes());
			buff.write("make install\n".getBytes());
			buff.write(("rm -fr /home/softsource/"
					+ zipFilename.substring(0, zipFilename.length() - 7) + "\n")
					.getBytes()); //写入脚本完毕
			buff.close();
			outStr.close();
			lexe.executeVMScript("/home/setupscripts/nginx/nginx.sh");
			// check
			boolean flags = false;
			File file = new File("/home/setupscripts/nginx/nginx-check.sh");
			if (!file.exists()) {
				file.createNewFile();
			}
			StringBuffer strBuffer2 = new StringBuffer();
			strBuffer2.append("cd " + unzipDirectory + "/" + "nginx/sbin");
			strBuffer2.append(System.getProperty("line.separator"));
			strBuffer2.append("./nginx");
			strBuffer2.append(System.getProperty("line.separator"));
			strBuffer2.append("ps -A | grep nginx");
			strBuffer2.append(System.getProperty("line.separator"));
			PrintWriter printWriter2 = new PrintWriter(
					"/home/setupscripts/nginx/nginx-check.sh");
			printWriter2.write(strBuffer2.toString().toCharArray());
			printWriter2.flush();
			printWriter2.close();
			Process process = Runtime.getRuntime().exec(
					"sh /home/setupscripts/nginx/nginx-check.sh");

			BufferedReader strCon = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			if (strCon.readLine().contains("nginx")) {
				flags = true;
				System.out.println("安装成功");
			} else {
				System.out.println("安装失败");
				// LAppUninstall().uninstallNginx(unzipDirectory);

			}

			if (flags == true)
				return "0x0100400";

			return "0x0100401";
		} catch (Exception e) {
			return "0x0100401";
		}
	}

	// yixiugai
	public String setupTomcat(String TomcatZip, String TomcatPath,
			String JdkPath, String oldpath) throws Exception {
		if (oldpath != null) {
			if (!oldpath.equals("null")) {
				LAppUninstall un = new LAppUninstall();
				un.uninstallTomcat(oldpath);
			}
		}
		try {
			File filepath = new File("/home/setupscripts/tomcat");
			if (!filepath.isDirectory()) {
				filepath.mkdirs();
			}
			filepath = new File(TomcatPath);
			if (!filepath.exists()) {
				filepath.mkdirs();
			}

			// 获得Tomcat文件夹名称
			String[] versions = TomcatZip.split("\\.");
			String TomcatDirName = versions[0] + "." + versions[1] + "."
					+ versions[2];
			System.out.println(TomcatDirName);
			// 写脚本（解压并配置环境变量）
			StringBuffer strBuffer = new StringBuffer();
			strBuffer.append("tar  -zxvf /home/softsource/" + TomcatZip
					+ "  -C  " + TomcatPath + "/;");
			strBuffer.append(System.getProperty("line.separator"));

			strBuffer.append("sed -i '/#!\\/bin\\/sh/a\\JAVA_HOME=" + JdkPath
					+ "' " + TomcatPath + "/" + TomcatDirName
					+ "/bin/catalina.sh;");
			strBuffer.append(System.getProperty("line.separator"));
			// 修改startup.sh
			strBuffer.append("echo \"#set java enviroment\" >> " + TomcatPath
					+ "/" + TomcatDirName + "/bin/startup.sh;");
			strBuffer.append(System.getProperty("line.separator"));
			strBuffer.append("echo \"export JAVA_HOME=" + JdkPath + "\" >> "
					+ TomcatPath + "/" + TomcatDirName + "/bin/startup.sh;");
			strBuffer.append(System.getProperty("line.separator"));
			strBuffer.append("echo \"export JRE_HOME=" + JdkPath + "/jre\" >> "
					+ TomcatPath + "/" + TomcatDirName + "/bin/startup.sh;");
			strBuffer.append(System.getProperty("line.separator"));
			strBuffer
					.append("echo 'export CLASSPATH=.:$JAVA_HOME/lib/dt.jar::$JAVA_HOME/lib/tools.jar:$CLASSPATH' >> "
							+ TomcatPath
							+ "/"
							+ TomcatDirName
							+ "/bin/startup.sh;");
			strBuffer.append(System.getProperty("line.separator"));
			strBuffer
					.append("echo 'export PATH=$JAVA_HOME/bin:$JRE_HOME/bin:$PATH' >> "
							+ TomcatPath
							+ "/"
							+ TomcatDirName
							+ "/bin/startup.sh;");
			strBuffer.append(System.getProperty("line.separator"));
			// 修改shutdown.sh
			strBuffer.append("echo \"#set java enviroment\" >> " + TomcatPath
					+ "/" + TomcatDirName + "/bin/shutdown.sh;");
			strBuffer.append(System.getProperty("line.separator"));
			strBuffer.append("echo \"export JAVA_HOME=" + JdkPath + "\" >> "
					+ TomcatPath + "/" + TomcatDirName + "/bin/shutdown.sh;");
			strBuffer.append(System.getProperty("line.separator"));
			strBuffer.append("echo \"export JRE_HOME=" + JdkPath + "/jre\" >> "
					+ TomcatPath + "/" + TomcatDirName + "/bin/shutdown.sh;");
			strBuffer.append(System.getProperty("line.separator"));
			strBuffer
					.append("echo 'export CLASSPATH=.:$JAVA_HOME/lib/dt.jar::$JAVA_HOME/lib/tools.jar:$CLASSPATH' >> "
							+ TomcatPath
							+ "/"
							+ TomcatDirName
							+ "/bin/shutdown.sh;");
			strBuffer.append(System.getProperty("line.separator"));
			strBuffer
					.append("echo 'export PATH=$JAVA_HOME/bin:$JRE_HOME/bin:$PATH' >> "
							+ TomcatPath
							+ "/"
							+ TomcatDirName
							+ "/bin/shutdown.sh;");
			strBuffer.append(System.getProperty("line.separator"));
			// 修改version.sh
			strBuffer.append("echo \"#set java enviroment\" >> " + TomcatPath
					+ "/" + TomcatDirName + "/bin/version.sh;");
			strBuffer.append(System.getProperty("line.separator"));
			strBuffer.append("echo \"export JAVA_HOME=" + JdkPath + "\" >> "
					+ TomcatPath + "/" + TomcatDirName + "/bin/version.sh;");
			strBuffer.append(System.getProperty("line.separator"));
			strBuffer.append("echo \"export JRE_HOME=" + JdkPath + "/jre\" >> "
					+ TomcatPath + "/" + TomcatDirName + "/bin/version.sh;");
			strBuffer.append(System.getProperty("line.separator"));
			strBuffer
					.append("echo 'export CLASSPATH=.:$JAVA_HOME/lib/dt.jar::$JAVA_HOME/lib/tools.jar:$CLASSPATH' >> "
							+ TomcatPath
							+ "/"
							+ TomcatDirName
							+ "/bin/version.sh;");
			strBuffer.append(System.getProperty("line.separator"));
			strBuffer
					.append("echo 'export PATH=$JAVA_HOME/bin:$JRE_HOME/bin:$PATH' >> "
							+ TomcatPath
							+ "/"
							+ TomcatDirName
							+ "/bin/version.sh;");
			strBuffer.append(System.getProperty("line.separator"));
			// 写入脚本
			PrintWriter printWriter;

			printWriter = new PrintWriter("/home/setupscripts/tomcat/tomcat.sh");

			printWriter.write(strBuffer.toString().toCharArray());
			printWriter.flush();
			printWriter.close();
			// 执行脚本
			String strCmdPath = "/home/setupscripts/tomcat/tomcat.sh";
			LVMScriptExecute le = new LVMScriptExecute();
			le.executeVMScript(strCmdPath);
			File[] files = new File(TomcatPath + "/" + TomcatDirName)
					.listFiles();
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
				return "0x0100000";

		} catch (FileNotFoundException e) {
			LAppUninstall un = new LAppUninstall();
			un.uninstallTomcat(TomcatPath);
			return "0x0100001";
		}
		LAppUninstall un = new LAppUninstall();
		un.uninstallTomcat(TomcatPath);
		return "0x0100001";

	}

	/**
	 * Linux脧脗掳虏脳掳Apache yixiugai
	 * */

	public  String setupApache(String ZIPFileName,String path,String emailAddress,String oldpath)  {
		if(oldpath!=null)
		{
			LAppUninstall un=new LAppUninstall();
			un.uninstallApache(oldpath);
		}
		try{
			
		File filepath = new File("/home/setupscripts/httpd");
		if (!filepath.isDirectory()) {
			filepath.mkdirs();
		}
		
		filepath = new File("/home/setupscripts/httpd/httpd.sh");
		if (!filepath.exists()) {
			filepath.createNewFile();
		}
		
		filepath = new File(path);
		if (!filepath.exists()) {
			filepath.mkdirs();
		}
		//获得Apache目录名称	
		String[] versions = ZIPFileName.split("\\.");
		String ApacheDirName = versions[0]+"."+versions[1]+"."+versions[2];
		System.out.println(ApacheDirName);
		//编写Apache安装脚本	
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("echo enterscript;");
		strBuffer.append(System.getProperty("line.separator"));
		strBuffer.append("tar  -zxvf  /home/softsource/"+ZIPFileName+"  -C  " + path + "/;");//解压Apache文件
		strBuffer.append(System.getProperty("line.separator"));
		strBuffer.append("cd  " + path +"/;");
		strBuffer.append(System.getProperty("line.separator"));
		strBuffer.append("mkdir -p  " + path + "/httpd"+";");//新建Apache安装目录
		strBuffer.append(System.getProperty("line.separator"));
		
		/*
		strBuffer.append("tar -zxvf /home/softsource/apr-1.5.1.tar.gz  -C  " + path + "/"+ApacheDirName+"/srclib/;");
		strBuffer.append(System.getProperty("line.separator"));
		strBuffer.append("tar -zxvf /home/softsource/apr-util-1.5.4.tar.gz  -C  " + path + "/"+ApacheDirName+"/srclib/;");
		strBuffer.append(System.getProperty("line.separator"));
		
		strBuffer.append("cd "+ path + "/"+ApacheDirName+"/srclib/;");
		strBuffer.append(System.getProperty("line.separator"));
		strBuffer.append("mv ./apr-1.5.1 ./apr;");
		strBuffer.append(System.getProperty("line.separator"));
		strBuffer.append("mv ./apr-util-1.5.4 ./apr-util;");
		strBuffer.append(System.getProperty("line.separator"));
		*/
		strBuffer.append("cd  " + path + "/"+ApacheDirName+"/;");
		strBuffer.append(System.getProperty("line.separator"));
		//分情况编写安装语句
		if(ApacheDirName.equals("httpd-2.4.10"))
		{
			strBuffer.append("./configure --prefix=" + path + "/httpd --with-pcre=/usr/local/pcre --with-included-apr;");
			strBuffer.append(System.getProperty("line.separator"));
		}//2.4.10
		else
		{
			strBuffer.append("./configure --prefix=" + path + "/httpd --with-included-apr;");
			strBuffer.append(System.getProperty("line.separator"));
		}//2.2.10
		
		strBuffer.append("make;");//编译
		strBuffer.append(System.getProperty("line.separator"));
		strBuffer.append("make install;");//安装
		strBuffer.append(System.getProperty("line.separator"));
		
		PrintWriter printWriter = new PrintWriter("/home/setupscripts/httpd/httpd.sh");
		printWriter.write(strBuffer.toString().toCharArray());
		printWriter.flush();
		printWriter.close();
		//准备执行语句
		String strCmdPath = "/home/setupscripts/httpd/httpd.sh";
		File file_http = new File(path+"/httpd");
		File file_httpd = new File(path+"/"+ApacheDirName);
	
		//脚本执行判断
		
		int n = 1;
		boolean flag_file = false;
		while(n > 0){
			Process process = Runtime.getRuntime().exec("chmod 777 " + strCmdPath);// 修改文件权限成可执行的
			BufferedReader strCon = new BufferedReader(new InputStreamReader(
					process.getInputStream())); // 在控制台输出结果
			 String line="";
			 while ((line = strCon.readLine()) != null) {
			 System.out.println("xiugaiquanxian!:"+line);
			 }
			System.out.println("xiugaiquanxiansuccess!");
			process = Runtime.getRuntime().exec(strCmdPath);
			strCon = new BufferedReader(new InputStreamReader(
					process.getInputStream())); // 在控制台输出结果
			 while ((line = strCon.readLine()) != null) {
			 System.out.println(line);
			 }
			System.out.println("n  :  " + n);
			if(file_http.exists() && file_httpd.exists()){
				flag_file = true;
				break;
			}
			n--;
		}
		
		if(flag_file == false)
		{
//			LAppUninstall un=new LAppUninstall();
//			un.uninstallApache(path);
			return "0x0100301";//若没有安装成功，返回错误代码
		}
		/*Process process = Runtime.getRuntime().exec("chmod 777 " + strCmdPath);// 淇敼鏂囦欢鏉冮檺鎴愬彲鎵ц鐨�		
		BufferedReader strCon = new BufferedReader(new InputStreamReader(
				process.getInputStream())); // 鍦ㄦ帶鍒跺彴杈撳嚭缁撴灉
		 String line="";
		 while ((line = strCon.readLine()) != null) {
		 System.out.println("xiugaiquanxian!:"+line);
		 }
		System.out.println("xiugaiquanxiansuccess!");
		process = Runtime.getRuntime().exec(strCmdPath);
		strCon = new BufferedReader(new InputStreamReader(
				process.getInputStream())); // 鍦ㄦ帶鍒跺彴杈撳嚭缁撴灉
		 while ((line = strCon.readLine()) != null) {
		 System.out.println(line);
		 }
		 */
		
		
		
			//改写httpd.conf配置文件，设置 ServerAdmin 和ServerName		
		 boolean flag = AppConfig.judgeExist(path + "/httpd/conf/httpd.conf", "ServerAdmin");
			if (flag) {
					flag = AppConfig.replaceToFile(path + "/httpd/conf/httpd.conf",
							"ServerAdmin", "ServerAdmin " + emailAddress);
			} else {
				flag = AppConfig.appendToFile(path + "/httpd/conf/httpd.conf",
								"ServerAdmin " + emailAddress);
			}

			flag = AppConfig.judgeExist(path + "/httpd/conf/httpd.conf", "ServerName");
			if (flag) {
				flag = AppConfig.replaceToFile(path + "/httpd/conf/httpd.conf",
							"ServerName", "ServerName 127.0.0.1:80");
			} else {
				flag = AppConfig.appendToFile(path + "/httpd/conf/httpd.conf",
							"ServerName 127.0.0.1:80");
			}
			Thread.sleep(5000);//等待5秒钟
			//在此判断安装是否完成，目录是否完整
			File[] files = new File(path+"/httpd").listFiles();
			boolean flag1 = false;
			boolean flag2 = false;
			boolean flag3 = false;
			for(File file:files)  {
				String fileName = file.getName().toLowerCase();
				if(fileName.equals("bin")&&file.isDirectory()) {
					flag1 = true;
				}else if(fileName.equals("conf")&&file.isDirectory()) {
					flag2 = true;
				}else if(fileName.equals("logs")&&file.isDirectory()) {
					flag3 = true;
				}
			}
			if(flag1&&flag2&&flag3) 
				return "0x0100300";//若目录均存在，返回成功代码
//		LAppUninstall un=new LAppUninstall();
//		un.uninstallApache(path);
		return "0x0100301";//安装失败，返回失败代码
		} catch(Exception e) {
			LAppUninstall un=new LAppUninstall();
			un.uninstallApache(path);
			return "0x0100301";//安装失败，返回失败代码
		}
	}
	
	

	/**
	 * Linux脧脗掳虏脳掳Python yixiugai
	 * */
	public String setupPython(String ZIPFileName, String path, String oldPath) {

		// 如果旧路径不为空,即原来安装过该软件,则进行卸载
		if (oldPath != null) {
			if (!oldPath.equals("null")) {
				new LAppUninstall().uninstallPython(oldPath);
			}
		}
		try {
			File filepath = new File("/home/setupscripts/python");
			if (!filepath.isDirectory()) {
				filepath.mkdirs();
			}

			filepath = new File(path);
			if (!filepath.exists()) {
				filepath.mkdirs();
			}
			// 获取版本号信息,将其作为文件名
			String[] versions = ZIPFileName.split("\\.");
			String PythonDirName = versions[0] + "." + versions[1] + "."
					+ versions[2];
			System.out.println(PythonDirName);
			// 编写安装命令的脚本
			StringBuffer strBuffer = new StringBuffer();
			strBuffer.append("tar  -zxvf  /home/softsource/" + ZIPFileName
					+ "  -C  " + path + "/;");
			strBuffer.append(System.getProperty("line.separator"));

			strBuffer.append("cd  " + path + "/" + PythonDirName + ";");
			strBuffer.append(System.getProperty("line.separator"));
			strBuffer.append("sudo ./configure;");
			strBuffer.append(System.getProperty("line.separator"));
			strBuffer.append("sudo make;");
			strBuffer.append(System.getProperty("line.separator"));
			strBuffer.append("sudo make install;");
			strBuffer.append(System.getProperty("line.separator"));

			PrintWriter printWriter = new PrintWriter(
					"/home/setupscripts/python/python.sh");
			printWriter.write(strBuffer.toString().toCharArray());
			printWriter.flush();
			printWriter.close();
			// 执行python.sh脚本文件
			String strCmdPath = "/home/setupscripts/python/python.sh";
			LVMScriptExecute le = new LVMScriptExecute();
			le.executeVMScript(strCmdPath);

			// 使用Python，为下来检测Python安装是否成功
			File file = new File("/home/setupscripts/python/python-version.sh");
			if (!file.exists()) {
				file.createNewFile();
			}
			StringBuffer strBuffer2 = new StringBuffer();
			strBuffer2.append("cd " + path);
			strBuffer2.append(System.getProperty("line.separator"));
			strBuffer2.append("python");
			strBuffer2.append(System.getProperty("line.separator"));
			strBuffer2.append(System.getProperty("line.separator"));
			PrintWriter printWriter2 = new PrintWriter(
					"/home/setupscripts/python/python-version.sh");
			printWriter2.write(strBuffer2.toString().toCharArray());
			printWriter2.flush();
			printWriter2.close();
			new LVMScriptExecute().executeVMScript("/home/setupscripts/python/python-version.sh");
//			Runtime.getRuntime().exec(
//					" chmod 777 /home/setupscripts/python/python-version.sh");
			String[] cmds = { "/bin/sh", "-c", "ps -A|grep python" };
			Process process = Runtime.getRuntime().exec(cmds);
			process.waitFor();
			BufferedReader strCon = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			String checklower = strCon.readLine().toLowerCase();
			if (checklower == null || !checklower.contains("python")) {
				System.out.println("安装失败");
				// 鍒犻櫎宸插畨瑁呮枃浠� new LAppUninstall().uninstallPython(path);
			} else {
				System.out.println("安装成功");
			}
			return "0x0100600";
		} catch (Exception e) {
			System.out.println(e);
			return "0x0100601";
		}

	}

	/**
	 * Linux安装FTP 已修改
	 * FTPZIP 软件安装包
	 * installPath 安装路径
	 * oldPath 旧路径(可能为NULL)
	 * */
	
	public String setupFTP(String FTPZip, String installPath, String oldPath)
			throws Exception {
		try {
			File filepath = new File("/home/setupscripts/ftp");
			if (!filepath.isDirectory()) {
				filepath.mkdirs();
			}
			filepath = new File("/home/setupscripts/ftp/ftp.sh");
			if (!filepath.exists()) {
				filepath.createNewFile();
			}
			
			//如果存在已安装FTP，删除
			if (oldPath != null) {
				if (!oldPath.equals("null")) {
					String error = new LAppUninstall().uninstallFTP(oldPath);
					if (error.equals("0x0200901")) {
						return "0x0100901";
					}
				}
			}
			
			
			File file1 = new File(installPath);
			if (!file1.exists()) {
				file1.mkdirs();
			}
			
			//编写安装脚本文件
			String ftpDir = FTPZip.substring(0, FTPZip.length() - 7);
			System.out.println("ftpDir:" + ftpDir);
			StringBuffer strBuffer = new StringBuffer();
			strBuffer.append("yum -y install gcc;");
			strBuffer.append(System.getProperty("line.separator"));
			strBuffer.append("yum -y install libcap-devel;");
			strBuffer.append(System.getProperty("line.separator"));
			strBuffer.append("tar -zxvf /home/softsource/" + FTPZip + " -C "
					+ installPath + "/;");
			strBuffer.append(System.getProperty("line.separator"));
			strBuffer.append("cd " + installPath + "/" + ftpDir + ";");
			strBuffer.append(System.getProperty("line.separator"));
			strBuffer.append("make;");
			strBuffer.append(System.getProperty("line.separator"));
			strBuffer.append("make install;");
			strBuffer.append(System.getProperty("line.separator"));
			strBuffer.append("cp " + installPath + "/" + ftpDir
					+ "/vsftpd.conf /etc;");
			strBuffer.append(System.getProperty("line.separator"));
			PrintWriter printWriter = new PrintWriter(
					"/home/setupscripts/ftp/ftp.sh");
			printWriter.write(strBuffer.toString().toCharArray());
			printWriter.flush();
			printWriter.close();
			
			//执行安装脚本
			LVMScriptExecute le = new LVMScriptExecute();
			le.executeVMScript("/home/setupscripts/ftp/ftp.sh");
			AppConfig.replaceToFile("/etc/xinetd.d/vsftpd", "disable",
					"disable=YES");

			// 检查是否安装成功
			File[] files = new File(installPath).listFiles();
			for (File file : files) {
				if (file.getName().contains("vsftpd")) {
					System.out.println("true");
					return "0x0100900";
				}
			}
			//安装失败，删除已安装文件
			new LAppUninstall().uninstallFTP(installPath);
			return "0x0100901";
		} catch (IOException e) {
			e.printStackTrace();
			new LAppUninstall().uninstallFTP(installPath);
			return "0x0100901";
		}

	}

	public String setupZendGuardLoader(String zipFilename, String phppath,
			String oldPath) {
		LAppUninstall lau = new LAppUninstall();
		if (oldPath != null) {
			if (!oldPath.equals("null")) {
				lau.uninstallZendGuardLoader(oldPath);
			}
		}

		try {

			// 1.判断php路径是否存在
			File filepath1 = new File(phppath);
			if (!filepath1.isDirectory()) {
				return "0x0100502";
			}
			// 2.判断php配置文件是否存在
			filepath1 = new File(phppath + "/lib/php.ini");
			if (!filepath1.isFile()) {
				return "0x0100503";
			}

			File filepath = new File("/home/setupscripts/ZendGuardLoader");
			if (!filepath.isDirectory()) {
				filepath.mkdir();
			}

			File unzip = new File(
					"/home/setupscripts/ZendGuardLoader/unzipzend.sh");
			unzip.createNewFile();
			FileOutputStream outStr1 = new FileOutputStream(unzip);
			BufferedOutputStream buff1 = new BufferedOutputStream(outStr1);
			buff1.write(("tar -zxvf /home/softsource/" + zipFilename
					+ " -C /home/softsource/" + ";\n").getBytes());
			buff1.flush();
			buff1.close();
			outStr1.close();
			lexe.executeVMScript("/home/setupscripts/ZendGuardLoader/unzipzend.sh");
			
			
			
			
			String zend = zipFilename.replace(".tar.gz", "");
			Runtime rt = Runtime.getRuntime();
			
			
			File fi = new File("/home/softsource/" + zend);
			Process proc = rt.exec("ls",null,fi);
			BufferedReader in = null;
			in = new BufferedReader(
					new InputStreamReader(proc.getInputStream()));
			String s = null;
			String zendname = null;
			while ((s = in.readLine()) != null) {
				String[] stest = s.split(" ");
				for (int i = 0; i < stest.length; i++) {
					System.out.println(stest[i]);
					if (stest[i].contains("php")) {
						zendname = stest[i];
					}
				}
			}

			System.out.println("++++++++++++++++++++++++++++");
			File batfile = new File(
					"/home/setupscripts/ZendGuardLoader/ZendGuardLoader.sh");
			batfile.createNewFile();
			FileOutputStream outStr = new FileOutputStream(batfile);
			BufferedOutputStream buff = new BufferedOutputStream(outStr);
			// int phpname=zipFilename.indexOf("p");
			// if(phpname <= 0){
			// phpname = zipFilename.indexOf("P");
			// }
			// buff.write(("mv /home/softsource/"+zipFilename.substring(0,zipFilename.length()-7)+"/php"+zipFilename.substring(phpname+3,phpname+7)+".x/ZendGuardLoader.so "
			// + phppath + "/ext;\n").getBytes());

			buff.write(("cp /home/softsource/" + zend + "/" + zendname
					+ "/ZendGuardLoader.so " + phppath + "/ext;\n").getBytes());
			buff.flush();
			buff.close();
			outStr.close();
			lexe.executeVMScript("/home/setupscripts/ZendGuardLoader/ZendGuardLoader.sh");

			AppConfig.appendToFile(phppath + "/lib/php.ini", "[Zend.loader]\n");
			AppConfig.appendToFile(phppath + "/lib/php.ini", "zend_extension="
					+ phppath + "/ext/ZendLoader.so");
			AppConfig.appendToFile(phppath + "/lib/php.ini",
					"zend_loader.enable=1");
			AppConfig.appendToFile(phppath + "/lib/php.ini",
					"zend_loader.disable_licensing=1");
			AppConfig.appendToFile(phppath + "/lib/php.ini",
					"zend_loader.obfuscation_level_support=4");

			
			Runtime test = Runtime.getRuntime();
			File filetest = new File(phppath+"/ext");
			Process p = test.exec("ls", null, filetest);
			BufferedReader buf = null;
			buf = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String ss = null;
			boolean flage = false;
			while ((ss = buf.readLine()) != null) {
				String[] stest = ss.split(" ");
				for (int i = 0; i < stest.length; i++) {
					System.out.println(stest[i]);
					if (stest[i].equals("ZendGuardLoader.so")) {
						flage = true;
					}
				}
			}
			if (flage) {
				return "0x0100500";
			} else {
				lau.uninstallZendGuardLoader(phppath);
				return "0x0100501";
			}
		} catch (IOException e) {
			lau.uninstallZendGuardLoader(phppath);
			return "0x0100501";
		}
	}

	/**
	 * @author wzy yixiugai show linux下安装mysql
	 * @param serverName
	 *            表示服务器端的mysql的rpm
	 * @param clientName
	 *            表示客户器端的mysql的rpm
	 * @return //0x0100202 mysql已经存在 0x0100200 mysql安装成功 0x0100204 mysql安装失败
	 */
	public String setupMySQL(String serverName, String clientName,
			String installPath, String oldPath) throws Exception {

		System.out.println("mysql: " + serverName + "," + clientName);
		PreInstall pre = new PreInstall();
		AppConfig la = new AppConfig();
		
		String flag = "";

		LAppUninstall un = new LAppUninstall();
		if (oldPath != null) {
			if (!oldPath.equals("null")) {
				un.uninstallMySql(oldPath);
			}
		}
		
		
		String preinstall = pre.checkMysql();// maybe one of the client has been
		// installed
		if (preinstall != "") {
			System.out.println("mysql is equiped");
			flag = "0x0100202";
		} else {
			try {
				Process process = null;
				String filePath = "/home/softsource/";
				System.out.println("start----------------");

				process = Runtime.getRuntime().exec(
						"chmod 777 /home/softsource -R");
				process.waitFor();
				System.out.println("one-----");

				process = Runtime.getRuntime().exec(
						"rpm -ivh " + filePath + serverName + "\r\n");
				process.waitFor();
				Thread.sleep(15000);
				System.out.println("two-----");

				process = Runtime.getRuntime().exec(
						"rpm -ivh " + filePath + clientName + "\r\n");
				process.waitFor();
				Thread.sleep(15000);
				System.out.println("three-----");

				/*
				 * process =
				 * Runtime.getRuntime().exec("/usr/bin/mysqladmin -u root password "
				 * +password); process.waitFor(); Thread.sleep(10000);
				 * System.out.println("five-----");
				 */

				process = Runtime.getRuntime().exec(
						"cp /usr/share/mysql/my-default.cnf  /etc/my.cnf");
				process.waitFor();
				Thread.sleep(10000);
				System.out.println("four-----");

				process = Runtime.getRuntime().exec(
						"cp /usr/share/mysql/my-medium.cnf  /etc/my.cnf");
				process.waitFor();
				Thread.sleep(10000);
				System.out.println("five-----");

				la.insertToFile("/etc/my.cnf", "[mysqld]", "skip-grant-tables");
				System.out.println("six-----");

				process = Runtime.getRuntime().exec("service mysql start");
				process.waitFor();
				Thread.sleep(5000);
				System.out.println("seven-----");

				String is = pre.isMySQLExist();
				if (is.contains("SUCCESS! MySQL running")) {
					System.out.println("success-----");
					flag = "0x0100200";
				} else {
					System.out.println("fail-----");
					flag = "0x0100204";
					un.uninstallMySql(installPath);
				}

			} catch (Exception e) {
				e.getStackTrace();
			}
		}
		return flag;
	}

	/**
	 * @Title: setupMemcached
	 * @Description: TODO
	 * @param //fileName1 libevent-2.0.21-stable.tar.gz
	 * @param fileName2
	 *            memcached-1.4.15.tar.gz
	 * @param path
	 *            memcached的安装路径
	 * @return: 0x0100700 安装成功 0x0100704 安装失败
	 * @throws Exception
	 */
	public String setupMemcached(String fileName2, String path, String oldpath)
			throws Exception {
		String code = "";
		String fileName1 = "";
		// "libevent-2.0.21-stable.tar.gz";
		// 判断是否需要进行卸载操作
		if (oldpath != null) {
			if (!oldpath.equals("null")) {
				LAppUninstall LAS = new LAppUninstall();
				// 得到安装的文件名称，作为第一个参数调用卸载函数
				String uninstall_file_name = "";
				File uninstall_path = new File(oldpath);
				File uninstall_names[] = uninstall_path.listFiles();
				for (File fm : uninstall_names) {
					if (fm.getName().startsWith("memcached"))
						uninstall_file_name = fm.getName();
				}
				LAS.uninstallMemcached(oldpath + "/" + uninstall_file_name);
			}
		}

		// 获取当前libevent包名称
		File source_path = new File("/home/softsource");
		File Names[] = source_path.listFiles();
		for (File fm : Names) {
			if (fm.getName().startsWith("libevent")) {
				fileName1 = fm.getName();
			}
		}

		LAppUninstall un = new LAppUninstall();
		String s1 = fileName1.substring(0, fileName1.length() - 7);
		System.out.println("first: " + s1);
		String s2 = fileName2.substring(0, fileName2.length() - 7);
		System.out.println("second: " + s2);
		File filepath = new File("/home/setupscripts/memcached");
		if (!filepath.isDirectory()) {
			filepath.mkdir();
		}

		filepath = new File(path);
		if (!filepath.exists()) {
			filepath.mkdirs();
		}

		PreInstall pre = new PreInstall();
		boolean preCode = pre.checkMemcached();

		StringBuffer mem = new StringBuffer();
		mem.append("#!/bin/sh");
		mem.append(System.getProperty("line.separator"));

		if (preCode == false) {
			StringBuffer strBuffer = new StringBuffer();
			strBuffer.append("#!/bin/sh");
			strBuffer.append(System.getProperty("line.separator"));
			strBuffer.append("cd /home/softsource");
			strBuffer.append(System.getProperty("line.separator"));
			strBuffer.append("tar  -zxvf  /home/softsource/" + fileName1
					+ " -C " + "/home/softsource"); // 解压到指定的目录
			strBuffer.append(System.getProperty("line.separator"));
			strBuffer.append("cd /home/softsource" + "/" + s1);
			strBuffer.append(System.getProperty("line.separator"));
			strBuffer.append("./configure --prefix=/home/libevent");
			strBuffer.append(System.getProperty("line.separator"));
			strBuffer.append("make");
			strBuffer.append(System.getProperty("line.separator"));
			strBuffer.append("make install");
			strBuffer.append(System.getProperty("line.separator"));

			PrintWriter printWriter = new PrintWriter(
					"/home/setupscripts/memcached/libmem.sh");
			printWriter.write(strBuffer.toString().toCharArray());
			printWriter.flush();
			printWriter.close();

			System.out.println("third: ");
			String strCmdPath = "/home/setupscripts/memcached/libmem.sh";
			lexe.executeVMScript(strCmdPath);

			Thread.sleep(2000);

			mem.append("cd /home/softsource");
			mem.append(System.getProperty("line.separator"));
			mem.append("tar  -zxvf  /home/softsource/" + fileName2 + " -C "
					+ "/home/softsource"); // 解压到指定目录
			mem.append(System.getProperty("line.separator"));
			mem.append("cd /home/softsource" + "/" + s2); // 进入解压目录
			mem.append(System.getProperty("line.separator"));
			// mem.append("./configure --prefix="+path);
			mem.append("./configure --prefix=" + path + "/" + s2
					+ " --with-libevent=/home/libevent");
			mem.append(System.getProperty("line.separator"));
			mem.append("make");
			mem.append(System.getProperty("line.separator"));
			mem.append("make install");
			mem.append(System.getProperty("line.separator"));
			mem.append("./memcached -d -u root");
			mem.append(System.getProperty("line.separator"));
			mem.append("rm -rf /home/softsource/" + s2);
			mem.append(System.getProperty("line.separator"));
			mem.append("rm -rf /home/softsource/" + s1);
			mem.append(System.getProperty("line.separator"));
		} else {
			mem.append("cd /home/softsource");
			mem.append(System.getProperty("line.separator"));
			mem.append("tar  -zxvf  /home/softsource/" + fileName2 + " -C "
					+ "/home/softsource");
			mem.append(System.getProperty("line.separator"));
			mem.append("cd /home/softsource" + "/" + s2);
			mem.append(System.getProperty("line.separator"));
			mem.append("./configure --prefix=" + path + "/" + s2
					+ " --with-libevent=/home/libevent");
			// mem.append("./configure --prefix="+path);
			mem.append(System.getProperty("line.separator"));
			mem.append("make");
			mem.append(System.getProperty("line.separator"));
			mem.append("make install");
			mem.append(System.getProperty("line.separator"));
			mem.append("./memcached -d -u root");
			mem.append(System.getProperty("line.separator"));
			mem.append("rm -rf /home/softsource/" + s2);
			mem.append(System.getProperty("line.separator"));
			mem.append("rm -rf /home/softsource/" + s1);
			mem.append(System.getProperty("line.separator"));
		}

		PrintWriter printWriter1 = new PrintWriter(
				"/home/setupscripts/memcached/mem.sh");
		printWriter1.write(mem.toString().toCharArray());
		printWriter1.flush();
		printWriter1.close();

		System.out.println("four: ");
		String memPath = "/home/setupscripts/memcached/mem.sh";
		lexe.executeVMScript(memPath);

		String is = pre.isMemcachedSuccess(path);

		if (is.equals("")) {
			System.out.println("fail to install memcached");
			code = "0x0100704";
			un.uninstallMemcached(path + "/" + s2);
		} else {
			System.out.println("success to install memcached");
			code = "0x0100700";
		}

		return code;
	}

	// 0x0100C05 界面安装oracle出错，出现异常
	// 0x0100C06 安装oracle出现异常 linux swap空间不足
	// 0x0100C07 安装oracle出现异常 指定用户不存在
	// 0x0100C08 安装oracle出现异常 指定用户不能为root用户

	public String setupOracle(String zipname1, String zipname2,
			String username, String oraclebase, String oracleinventory,
			String oraclehome, String oracle_sid, String password,
			String oradata,String oldpath) {
		
		if (oldpath != null) {
			if (!oldpath.equals("null"))
			
			{
				LAppUninstall lau = new LAppUninstall();
				lau.uninstallOracle11g(oldpath);
			}
		}
		
		String result = "";
		FileOutputStream outStr;
		BufferedOutputStream buff;
		BufferedReader strCon;
		String line;
		// String oraclebase="/u01/app/oracle";
		// String oracleinventory="/u01/app/oraInventory";
		// String oraclehome="/u01/app/oracle/product";
		// String oracle_sid = "orcl";
		oracleinventory = oraclebase+"/"+ oracleinventory;
		oraclehome = oraclebase+"/"+ oraclehome;
		oradata=oraclebase+"/"+oradata;
		boolean f = false;

		try {
			File filepath = new File("/home/setupscripts/oracle");
			if (!filepath.isDirectory()) {
				filepath.mkdir();
			}

			// //判断密码是否符合要求
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

			// 判断所给的用户名是否存在，是否为root
			if (username.equals("root")) {
				return "0x0100C08";
			} else {
				filepath = new File("/home/" + username + "/.bash_profile");
				if (!filepath.isFile()) {
					return "0x0100C07";
				}
			}

			// 1.检测内存大小
			int meminfo;
			String cmd = "grep MemTotal /proc/meminfo";

			Process process = Runtime.getRuntime().exec(cmd);

			strCon = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			line = strCon.readLine();
			// System.out.println(line.substring(17,line.length()-3));
			meminfo = Integer.parseInt(line.substring(17, line.length() - 3));
			if (meminfo <= 1024 * 1024) {
				System.out.println("mem is not enough");
				return "0x0100C02";
			}
			System.out.println("oracle presetup-------------------------5%");

			// 2.检测swap大小
			int swap;
			cmd = "grep SwapTotal /proc/meminfo";
			process = Runtime.getRuntime().exec(cmd);
			strCon = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			line = strCon.readLine();
			// System.out.println(line.substring(17,line.length()-3));
			swap = Integer.parseInt(line.substring(17, line.length() - 3));
			if (meminfo <= 1024 * 1024 * 2) {
				if (swap <= meminfo * 1.5) {
					return "0x0100C06";
				}
			} else if (meminfo <= 1024 * 1024 * 16) {
				if (swap <= meminfo) {
					return "0x0100C06";
				}
			} else {
				if (swap <= 16 * 1024 * 1024) {
					return "0x0100C06";
				}
			}
			System.out.println("oracle presetup-------------------------10%");

			// 3.检测硬盘/tmp 大于6G
			int tmpsize = 0;
			cmd = "df -h /tmp";
			process = Runtime.getRuntime().exec(cmd);
			strCon = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			line = strCon.readLine();
			line = strCon.readLine();
			line = strCon.readLine();
			// System.out.println(line.substring(35,37));
			tmpsize = Integer.parseInt(line.substring(34, 35));
			if (tmpsize < 6) {
				return "0x0100C03";
			}

			// yum install rpm
			File getrpm = new File("/home/setupscripts/oracle/setrpm.sh");
			getrpm.createNewFile();
			outStr = new FileOutputStream(getrpm);
			buff = new BufferedOutputStream(outStr);

			buff.write("yum install binutils compat-libstdc++-33.x86_64 compat-libstdc++-33.i686 elfutils-libelf elfutils-libelf-devel expat gcc gcc-c++ glibc.x86_64 glibc.i686 glibc-common glibc-devel glibc-headers libaio.x86_64 libaio.i686 libaio-devel.x86_64 libaio-devel.i686 libgcc.i686 libstdc++.x86_64 libstdc++.i686 libstdc++-devel make sysstat unixODBC.x86_64 unixODBC.i686 unixODBC-devel.x86_64 unixODBC-devel.i686 -y"
					.getBytes());
			buff.flush();
			outStr.close();
			buff.close();

			lexe.executeVMScript("/home/setupscripts/oracle/setrpm.sh");
			// Thread.sleep(1000 * 60 * 5);

			File getrpm1 = new File("/home/setupscripts/oracle/setrpm1.sh");
			getrpm1.createNewFile();
			outStr = new FileOutputStream(getrpm1);
			buff = new BufferedOutputStream(outStr);

			buff.write("wget http://mirror.centos.org/centos/5/os/x86_64/CentOS/pdksh-5.2.14-37.el5_8.1.x86_64.rpm\n"
					.getBytes());
			buff.write("rpm -ivh pdksh-5.2.14-37.el5_8.1.x86_64.rpm\n"
					.getBytes());
			buff.flush();
			outStr.close();
			buff.close();
			lexe.executeVMScript("/home/setupscripts/oracle/setrpm1.sh");
			// Thread.sleep(1000 * 60 * 1);
			System.out.println("oracle presetup-------------------------30%");

			// 4.create group oinstall
			cmd = "/usr/sbin/groupadd oinstall";
			process = Runtime.getRuntime().exec(cmd);
			strCon = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			System.out.println("oracle presetup-------------------------35%");

			// 5.配置oracle inventory 组
			File oraInst = new File("/etc/oraInst.loc");
			oraInst.createNewFile();
			outStr = new FileOutputStream(oraInst);
			buff = new BufferedOutputStream(outStr);

			buff.write(("inventory_loc=" + oracleinventory + "\n").getBytes());
			buff.write("inst_group=oinstall\n".getBytes());
			buff.flush();
			outStr.close();
			buff.close();
			System.out.println("oracle presetup-------------------------40%");

			// 6.添加OSDBA Group
			cmd = "/usr/sbin/groupadd -g 502 dba";
			process = Runtime.getRuntime().exec(cmd);
			strCon = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			System.out.println("oracle presetup-------------------------45%");

			// 7.添加已有用户到用户组
			cmd = "/usr/sbin/usermod -g oinstall -G dba " + username;
			process = Runtime.getRuntime().exec(cmd);
			strCon = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			line = strCon.readLine();
			if (line != null) {
				return "0x0100C07";
			}
			System.out.println("oracle presetup-------------------------50%");

			// 8.配置orale安装用户的脚本权限/etc/security/limits.conf
			f = AppConfig.judgeExist("/etc/security/limits.conf", username
					+ " soft nproc 2047");
			if (f == false) {
				AppConfig.appendToFile("/etc/security/limits.conf", username
						+ " soft nproc 2047");
				AppConfig.appendToFile("/etc/security/limits.conf", username
						+ " hard nproc 16384");
				AppConfig.appendToFile("/etc/security/limits.conf", username
						+ " soft nofile 1024");
				AppConfig.appendToFile("/etc/security/limits.conf", username
						+ " hard nofile 65536");

			}
			System.out.println("oracle presetup-------------------------55%");

			// 9.修改/etc/pam.d/login
			f = AppConfig.judgeExist("/etc/pam.d/login",
					"session required pam_limits.so");
			if (f == false) {
				AppConfig.appendToFile("/etc/pam.d/login",
						"session required pam_limits.so");
				Thread.sleep(1000 * 5);
			}
			System.out.println("oracle presetup-------------------------60%");

			// 10.修改/etc/profile.local
			f = AppConfig.judgeExist("/etc/profile", "if [ $USER = \""
					+ username + "\" ]; then");
			if (f == false) {
				AppConfig.appendToFile("/etc/profile", "if [ $USER = \""
						+ username + "\" ]; then");
				AppConfig.appendToFile("/etc/profile",
						"if [ $SHELL = \"/bin/ksh\" ]; then");
				AppConfig.appendToFile("/etc/profile", "ulimit -p 16384");
				AppConfig.appendToFile("/etc/profile", "ulimit -n 65536");
				AppConfig.appendToFile("/etc/profile", "else");
				AppConfig.appendToFile("/etc/profile",
						"ulimit -u 16384 -n 65536");
				AppConfig.appendToFile("/etc/profile", "fi");
				AppConfig.appendToFile("/etc/profile", "umask 022");
				AppConfig.appendToFile("/etc/profile", "fi");
				Thread.sleep(1000 * 5);
			}
			System.out.println("oracle presetup-------------------------62%");

			// 11.Configuring Kernel Parameters
			f = AppConfig.judgeExist("/etc/sysctl.conf",
					"fs.aio-max-nr = 1048576");
			if (f == false) {
				AppConfig.appendToFile("/etc/sysctl.conf",
						"fs.aio-max-nr = 1048576");
				AppConfig.appendToFile("/etc/sysctl.conf",
						"fs.file-max = 6815744");
				AppConfig.appendToFile("/etc/sysctl.conf",
						"kernel.sem = 250 32000 100 128");
				AppConfig.appendToFile("/etc/sysctl.conf",
						"net.ipv4.ip_local_port_range = 9000 65500");
				AppConfig.appendToFile("/etc/sysctl.conf",
						"net.core.rmem_default = 262144");
				AppConfig.appendToFile("/etc/sysctl.conf",
						"net.core.rmem_max = 4194304");
				AppConfig.appendToFile("/etc/sysctl.conf",
						"net.core.wmem_default = 262144");
				AppConfig.appendToFile("/etc/sysctl.conf",
						"net.core.wmem_max = 1048586");

				Thread.sleep(1000 * 20);
			}
			f = AppConfig.judgeExist("/etc/sysctl.conf", "kernel.shmall");
			if (f == false) {
				AppConfig.appendToFile("/etc/sysctl.conf",
						"kernel.shmall = 2097152");
			} else {
				AppConfig.replaceToFile("/etc/sysctl.conf", "kernel.shmall",
						"kernel.shmall = 2097152");
			}
			f = AppConfig.judgeExist("/etc/sysctl.conf", "kernel.shmmax");
			if (f == false) {
				AppConfig.appendToFile("/etc/sysctl.conf",
						"kernel.shmmax = 536870912");
			} else {
				AppConfig.replaceToFile("/etc/sysctl.conf", "kernel.shmmax",
						"kernel.shmmax = 536870912");
			}
			f = AppConfig.judgeExist("/etc/sysctl.conf", "kernel.shmmni");
			if (f == false) {
				AppConfig.appendToFile("/etc/sysctl.conf",
						"kernel.shmmni = 4096");
			} else {
				AppConfig.replaceToFile("/etc/sysctl.conf", "kernel.shmmni",
						"kernel.shmmni = 4096");
			}
			cmd = "/sbin/sysctl -p";
			process = Runtime.getRuntime().exec(cmd);
			strCon = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			System.out.println("oracle presetup-------------------------65%");

			// 12.Identifying Required Software Directories

			File makedir = new File("/home/setupscripts/oracle/makedir.sh");
			makedir.createNewFile();
			outStr = new FileOutputStream(makedir);
			buff = new BufferedOutputStream(outStr);

			buff.write(("mkdir -p " + oraclebase + "\n").getBytes());
			buff.write(("mkdir -p " + oracleinventory + "\n").getBytes());
			buff.write(("mkdir -p " + oraclehome + "\n").getBytes());
			buff.write(("mkdir -p " + oradata    +"\n").getBytes());

			buff.flush();
			outStr.close();
			buff.close();
			lexe.executeVMScript("/home/setupscripts/oracle/makedir.sh");
			Thread.sleep(1000 * 5);
			System.out.println("oracle presetup-------------------------67%");

			File givePrivi = new File("/home/setupscripts/oracle/givepri.sh");
			givePrivi.createNewFile();
			outStr = new FileOutputStream(givePrivi);
			buff = new BufferedOutputStream(outStr);

			String path[] = oraclebase.split("/");
			buff.write(("chown -R " + username + ":oinstall /" + path[1] + "\n")
					.getBytes());
			// buff.write(("chown -R "+username+":oinstall " + oracleinventory +
			// "\n").getBytes());
			// buff.write(("chmod -R 775 " + oraclebase + "\n").getBytes());
			buff.write(("chmod -R 775 /" + path[1] + "\n").getBytes());
			buff.flush();
			outStr.close();
			buff.close();
			lexe.executeVMScript("/home/setupscripts/oracle/givepri.sh");
			Thread.sleep(1000 * 5);
			System.out.println("oracle presetup-------------------------68%");

			// 13.配置用户的环境变量
			// 修改.bash_profile
			// ORACLE_BASE=/u01/app/oracle
			// ORACLE_SID=orcl
			// export ORACLE_BASE ORACLE_SID
			String bash = "/home/" + username + "/.bash_profile";
			f = AppConfig.judgeExist(bash, "ORACLE_BASE");
			if (f == false) {
				AppConfig.appendToFile(bash, "ORACLE_BASE=" + oraclebase);
			} else {
				AppConfig.replaceToFile(bash, "ORACLE_BASE", "ORACLE_BASE="
						+ oraclebase);
			}

			f = AppConfig.judgeExist(bash, "ORACLE_SID");
			if (f == false) {
				AppConfig.appendToFile(bash, "ORACLE_SID=" + oracle_sid);
			} else {
				AppConfig.replaceToFile(bash, "ORACLE_SID", "ORACLE_SID="
						+ oracle_sid);
			}

			f = AppConfig.judgeExist(bash, "ORACLE_HOME");
			if (f == false) {
				AppConfig.appendToFile(bash, "ORACLE_HOME=" + oraclehome
						+ "/11.2.0/dbhome_1");
			} else {
				AppConfig.replaceToFile(bash, "ORACLE_HOME", "ORACLE_HOME="
						+ oraclehome + "/11.2.0/dbhome_1");
			}

			f = AppConfig.judgeExist(bash, "export ORACLE_BASE ORACLE_SID");
			if (f == false) {
				AppConfig.appendToFile(bash,
						"export ORACLE_BASE ORACLE_SID ORACLE_HOME");
			}
			AppConfig.appendToFile(bash, "export PATH=$PATH:$ORACLE_HOME/bin");
			AppConfig.appendToFile(bash, "umask 022");
			// 执行指令
			// unset ORACLE_HOME
			// unset TNS_ADMIN
			// cmd = "unset ORACLE_HOME;unset TNS_ADMIN";
			// process = Runtime.getRuntime().exec(cmd);
			// strCon = new BufferedReader(new InputStreamReader(
			// process.getInputStream()));
			System.out.println("oracle presetup-------------------------70%");

			// 14.解压安装包
			File unzipfile = new File("/home/setupscripts/oracle/unzipfile.sh");
			unzipfile.createNewFile();
			outStr = new FileOutputStream(unzipfile);
			buff = new BufferedOutputStream(outStr);

			buff.write(("unzip -o /home/softsource/" + zipname1 + " -d /home/softsource/\n")
					.getBytes());
			buff.write(("unzip -o /home/softsource/" + zipname2 + " -d /home/softsource/")
					.getBytes());
			buff.flush();
			outStr.close();
			System.out.println("oracle start unzip");
			buff.close();
			lexe.executeVMScript("/home/setupscripts/oracle/unzipfile.sh");
			System.out.println("oracle stop unzip");
			Thread.sleep(1000 * 5);
			System.out.println("oracle presetup-------------------------80%");

			// 15.修改rsp文件
			AppConfig.replaceToFile("/home/setupscripts/oracle/db_install.rsp",
					"INVENTORY_LOCATION", "INVENTORY_LOCATION="
							+ oracleinventory);
			AppConfig.replaceToFile("/home/setupscripts/oracle/db_install.rsp",
					"ORACLE_HOME", "ORACLE_HOME=" + oraclehome
							+ "/11.2.0/dbhome_1");
			AppConfig.replaceToFile("/home/setupscripts/oracle/db_install.rsp",
					"ORACLE_BASE", "ORACLE_BASE=" + oraclebase);
			AppConfig.replaceToFile("/home/setupscripts/oracle/db_install.rsp",
					"oracle.install.db.config.starterdb.globalDBName",
					"oracle.install.db.config.starterdb.globalDBName="
							+ oracle_sid);
			AppConfig.replaceToFile("/home/setupscripts/oracle/db_install.rsp",
					"oracle.install.db.config.starterdb.SID",
					"oracle.install.db.config.starterdb.SID=" + oracle_sid);
			AppConfig.replaceToFile("/home/setupscripts/oracle/db_install.rsp",
					"oracle.install.db.config.starterdb.password.ALL",
					"oracle.install.db.config.starterdb.password.ALL="
							+ password);
			AppConfig.replaceToFile("/home/setupscripts/oracle/db_install.rsp",
					"oracle.install.db.config.starterdb.password.SYS",
					"oracle.install.db.config.starterdb.password.SYS="
							+ password);
			AppConfig.replaceToFile("/home/setupscripts/oracle/db_install.rsp",
					"oracle.install.db.config.starterdb.password.SYSTEM",
					"oracle.install.db.config.starterdb.password.SYSTEM="
							+ password);
			AppConfig.replaceToFile("/home/setupscripts/oracle/db_install.rsp",
					"oracle.install.db.config.starterdb.password.SYSMAN",
					"oracle.install.db.config.starterdb.password.SYSMAN="
							+ password);
			AppConfig.replaceToFile("/home/setupscripts/oracle/db_install.rsp",
					"oracle.install.db.config.starterdb.password.DBSNMP",
					"oracle.install.db.config.starterdb.password.DBSNMP="
							+ password);
			AppConfig
					.replaceToFile(
							"/home/setupscripts/oracle/db_install.rsp",
							"oracle.install.db.config.starterdb.fileSystemStorage.dataLocation",
							"oracle.install.db.config.starterdb.fileSystemStorage.dataLocation="
									+ oradata);
			
//			AppConfig.replaceToFile("/home/softsource/database/response/db_install.rsp",
//					"INVENTORY_LOCATION", "INVENTORY_LOCATION="
//							+ oracleinventory);
//			AppConfig.replaceToFile("/home/softsource/database/response/db_install.rsp",
//					"ORACLE_HOME", "ORACLE_HOME=" + oraclehome
//							+ "/11.2.0/dbhome_1");
//			AppConfig.replaceToFile("/home/softsource/database/response/db_install.rsp",
//					"ORACLE_BASE", "ORACLE_BASE=" + oraclebase);
//			AppConfig.replaceToFile("/home/softsource/database/response/db_install.rsp",
//					"oracle.install.db.config.starterdb.globalDBName",
//					"oracle.install.db.config.starterdb.globalDBName="
//							+ oracle_sid);
//			AppConfig.replaceToFile("/home/softsource/database/response/db_install.rsp",
//					"oracle.install.db.config.starterdb.SID",
//					"oracle.install.db.config.starterdb.SID=" + oracle_sid);
//			AppConfig.replaceToFile("/home/softsource/database/response/db_install.rsp",
//					"oracle.install.db.config.starterdb.password.ALL",
//					"oracle.install.db.config.starterdb.password.ALL="
//							+ password);
//			AppConfig.replaceToFile("/home/softsource/database/response/db_install.rsp",
//					"oracle.install.db.config.starterdb.password.SYS",
//					"oracle.install.db.config.starterdb.password.SYS="
//							+ password);
//			AppConfig.replaceToFile("/home/softsource/database/response/db_install.rsp",
//					"oracle.install.db.config.starterdb.password.SYSTEM",
//					"oracle.install.db.config.starterdb.password.SYSTEM="
//							+ password);
//			AppConfig.replaceToFile("/home/softsource/database/response/db_install.rsp",
//					"oracle.install.db.config.starterdb.password.SYSMAN",
//					"oracle.install.db.config.starterdb.password.SYSMAN="
//							+ password);
//			AppConfig.replaceToFile("/home/softsource/database/response/db_install.rsp",
//					"oracle.install.db.config.starterdb.password.DBSNMP",
//					"oracle.install.db.config.starterdb.password.DBSNMP="
//							+ password);
//			AppConfig
//					.replaceToFile(
//							"/home/softsource/database/response/db_install.rsp",
//							"oracle.install.db.config.starterdb.fileSystemStorage.dataLocation",
//							"oracle.install.db.config.starterdb.fileSystemStorage.dataLocation="
//									+ oradata);
			System.out.println("oracle presetup-------------------------85%");

			System.out.println("oracle presetup-------------------------100%");
			return "0x0100C10";
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}

	}

}
