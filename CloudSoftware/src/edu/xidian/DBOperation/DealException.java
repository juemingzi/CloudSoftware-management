package edu.xidian.DBOperation;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import com.sun.xml.internal.ws.api.config.management.policy.ManagementAssertion.Setting;

import edu.xidian.message.MsgType;
import edu.xidian.windows.*;
import edu.xidian.agent.FtpTransFile;
import edu.xidian.linux.*;

public class DealException {

	public String Setup(String[] str) throws Exception {
		String software = str[1];
		String version = str[3];
		String[] command = str[2].split(";");
		WAppSetup was = new WAppSetup();
		WAppUninstall wau = new WAppUninstall();
		String result=null,installPath = null,uninstallPath = null,rootPswd = null,jdkPath = null,emailAddress = null,hostName = null,userName = null;
		String inventorypath = null,oraclehome = null;
		/**
		 * 软件的下载
		 */
		int num = command.length;
		String softName = command[num - 1];
		String address = command[num - 2];
		String[] name = new String[2];

		
		/**
		 * 解析command并从中抽取参数
		 */
		int i = 0;
		for(;i<num;i++){
			if(command[i].toLowerCase().contains("uninstallpath")){
				uninstallPath = command[i].split("=")[1];
				System.out.println("uninstallPath  =    "+uninstallPath);
			}else if(command[i].toLowerCase().contains("installpath")){
				installPath = command[i].split("=")[1];
				System.out.println("installPath  =    "+installPath);
			}else if(command[i].toLowerCase().contains("rootpswd")){
				rootPswd = command[i].split("=")[1];
				System.out.println("rootPswd  =    "+rootPswd);
			}else if(command[i].toLowerCase().contains("jdkpath")){
				jdkPath = command[i].split("=")[1];
				System.out.println("jdkPath  =    "+jdkPath);
			}else if(command[i].toLowerCase().contains("emailaddress")){
				emailAddress = command[i].split("=")[1];
				System.out.println("emailAddress  =    "+emailAddress);
			}else if(command[i].toLowerCase().contains("hostname")){
				hostName = command[i].split("=")[1];
				System.out.println("hostName  =    "+hostName);
			}else if(command[i].toLowerCase().contains("username")){
				userName = command[i].split("=")[1];
				System.out.println("userName  =    "+userName);
			}else if(command[i].toLowerCase().contains("inventorypath")){
				inventorypath = command[i].split("=")[1];
				System.out.println("inventorypath  =    "+inventorypath);
			}else if(command[i].toLowerCase().contains("oraclehome")){
				oraclehome = command[i].split("=")[1];
				System.out.println("oraclehome  =    "+oraclehome);
			}		
			
		}
		
		
		//setup jiance
		GetTotallSpace getzip = new GetTotallSpace();
		double setupzip = getzip.searchSetupForZip(software) * 1.0 / 1024;
		// 检测下载空间是否足够
		double freeDownLoadSpace = getzip.getzipspace(softName, version, "W") * 1.0 / 1024;
		getzip.close();
		
		
		double currentDownLoadSpace = new File("C:\\").getFreeSpace() * 1.0 / 1024 / 1024 - setupzip;
		System.out
				.println("currentSpace  ###########   freespace :"
						+ currentDownLoadSpace
						+ "      " + freeDownLoadSpace);

		if (currentDownLoadSpace < freeDownLoadSpace) {
			return "0x1000001";
		}
		
		if (softName.contains(",")) {
			name = softName.split(",");
			FtpTransFile.fileDownload("windows", name[0], "C:\\softsource",
					address, "test", "123456");
			FtpTransFile.fileDownload("windows", name[1], "C:\\softsource",
					address, "test", "123456");
		} else {
			FtpTransFile.fileDownload("windows", softName, "C:\\softsource",
					address, "test", "123456");
		}
		
		
		

		// 截取安装路径
		
			String diskPath = installPath.split(":")[0];
					
			GetTotallSpace getspace = new GetTotallSpace();
			double setupspace = getspace.searchSetupForSpace(diskPath+":",software) * 1.0 / 1024;
			// 检测磁盘空间
			double freeSpace = getspace.getspace(softName, version, "W") * 1.0 / 1024;
			getspace.close();
			
			double currentSpace = new File(diskPath+ ":\\")
					.getFreeSpace() * 1.0 / 1024 / 1024 - setupspace;
		
			

		if (currentSpace < freeSpace) {
			return "0x1000002";
		}
		
		/**
		 * 安装Tomact
		 */
		if (software.toLowerCase().equals("tomcat")) {
			System.out.println("installing Tomact");
			wau.uninstallTomcat(installPath);
			result = was.setupTomcat(softName, installPath, jdkPath,uninstallPath);
		}
		/**
		 * 安装Mysql
		 */
		else if (software.toLowerCase().equals("mysql")) {
			System.out.println("installing MySql");
			wau.uninstallMySql(installPath);
			result = was.setupMySql(softName, installPath, rootPswd,
					uninstallPath);//zipFileName==Addes[1];

		}
		/**
		 * 安装Apache
		 */
		else if (software.toLowerCase().equals("apache")) {
			System.out.println("installing Apache");
			wau.uninstallApache(installPath);
			result = was.setupApache(softName, installPath,
			 emailAddress,uninstallPath);
		}
		/**
		 * 安装Nginx
		 */
		else if (software.toLowerCase().equals("nginx")) {
			System.out.println("installing Nginx");
			wau.uninstallNginx(installPath);
			result = was.setupNginx(softName, installPath,uninstallPath);
		}
		/**
		 * 安装ZendGuardLoader
		 */
		else if (software.toLowerCase().equals("zendguardloader")) {
			System.out.println("installing ZendGuardLoader");
			wau.uninstallZendGuardLoader(installPath);
			 result = was.setupZendGuardLoader(softName, installPath,uninstallPath);
		}
		/**
		 * 安装Python
		 */
		else if (software.toLowerCase().equals("python")) {
			System.out.println("installing Python");
			wau.uninstallPython(installPath);
			 result = was.setupPython(softName,installPath,uninstallPath);
		}
		/**
		 * 安装Memcached
		 */
		else if (software.toLowerCase().equals("memcached")) {
			System.out.println("installing Memcached");
			wau.uninstallMemcached(installPath);
			 result = was.setupMemcached(softName,installPath,uninstallPath);
		}
		/**
		 * 安装IISRewrite
		 */
		else if (software.toLowerCase().equals("iisrewrite")) {
			System.out.println("installing IISRewrite");
			wau.uninstallIISRewrite(installPath);
			 result = was.setupiisRewrite(softName,
			 installPath,uninstallPath);
		}
		/**
		 * 安装FTP
		 */
		else if (software.toLowerCase().equals("ftp")) {
			System.out.println("installing FTP");
			wau.uninstallFTP(installPath);
			 result = was.setupFTP(softName, installPath,uninstallPath);
		}
		/**
		 * 安装SQLServer2008R2
		 */
		else if (software.toLowerCase().equals("sqlserver2008r2")) {
			System.out.println("installing SQLServer2008R2");
			wau.uninstallSQLServer2008R2(installPath);
			 result = was.setupSQLServer2008R2(softName, installPath,
			 rootPswd, hostName, userName,uninstallPath);
		}

		/**
		 * 安装Oracle11g
		 */
		else if (software.toLowerCase().equals("oracle11g")) {
			System.out.println("installing Oracle11g");
			wau.uninstallOracle11g(installPath+"&"+oraclehome);
			result = was.setupOracle(name[0], name[1], hostName, inventorypath, installPath, oraclehome, rootPswd,uninstallPath);
		}
		/**
		 * 卸载360
		 */
		else if (software.toLowerCase().equals("360")) {
			System.out.println("uninstalling 360");
		//	wau.uninstall360(uninstallPath);
			result = "0x0100E01";
		}
		return result;
	}

	public String Setup_Linux(String[] str) throws Exception {
		String software = str[1];
		String version = str[3];
		String[] command = str[2].split(";");
		String result = null,installPath= null,uninstallPath= null,rootPswd= null,jdkPath= null,emailAddress= null,oraclehome= null,userName= null;
		LAppSetup las = new LAppSetup();
		LAppUninstall lau = new LAppUninstall();
		String oracleinventory = null,oracle_sid = null,oradata = null;
		/**
		 * 软件的下载
		 */
		int num = command.length;
		String softName = command[num - 1];
		String address = command[num - 2];
		String[] name = new String[2];

		Runtime rt = Runtime.getRuntime();
		Process p = rt.exec("df -hl /");// df -hl 查看硬盘空间

		BufferedReader in = new BufferedReader(new InputStreamReader(p
				.getInputStream()));
		String string = null;
		double currentSpace = 0;
		while ((string = in.readLine()) != null) {
			String[] temp = string.split("\\s+");
			if (temp.length >= 3 && temp[3].contains("G")) {
				currentSpace = Double.valueOf(temp[3]
						.trim().replace("G", "")) * 1024 * 1024;
			}
		}
		
		GetTotallSpace getspace = new GetTotallSpace();
		double zip = getspace.searchSetupForSpace("/",software) * 1.0 / 1024;
		double setup = getspace.searchSetupForZip(software) * 1.0 / 1024;
		double space = zip+setup;
		int freeSpace = getspace.getspace(softName, version, "L");
		getspace.close();
		
		
		if (currentSpace-space < freeSpace) {
			Thread.sleep(2000);
			result = "0x1000002";
		}
		
		/**
		 * 解析command并从中抽取参数
		 */
		for(int i=0;i<num;i++){
			if(command[i].toLowerCase().contains("uninstallpath")){
				uninstallPath = command[i].split("=")[1];
			}else if(command[i].toLowerCase().contains("installpath")){
				installPath = command[i].split("=")[1];
			}else if(command[i].toLowerCase().contains("jdkpath")){
				jdkPath = command[i].split("=")[1];
			}else if(command[i].toLowerCase().contains("emailaddress")){
				emailAddress = command[i].split("=")[1];
			}else if(command[i].toLowerCase().contains("username")){
				userName = command[i].split("=")[1];
			}else if(command[i].toLowerCase().contains("oracleinventory")){
				oracleinventory = command[i].split("=")[1];
			}else if(command[i].toLowerCase().contains("oraclehome")){
				oraclehome = command[i].split("=")[1];
			}else if(command[i].toLowerCase().contains("oracle_sid")){
				oracle_sid = command[i].split("=")[1];
			}else if(command[i].toLowerCase().contains("rootpswd")){
				rootPswd = command[i].split("=")[1];
			}else if(command[i].toLowerCase().contains("oradata")){
				oradata = command[i].split("=")[1];
			}
		}
		
		
		if (softName.contains(",")) {
			name = softName.split(",");
			FtpTransFile.fileDownload("linux", name[0], "/home/softsource",
					address, "test", "123456");
			FtpTransFile.fileDownload("linux", name[0], "/home/softsource",
					address, "test", "123456");
		} else {
			FtpTransFile.fileDownload("linux", softName, "/home/softsource",
					address, "test", "123456");
		}
		
		/**
		 * 安装Tomact
		 */
		if (software.toLowerCase().equals("tomcat")) {
			System.out.println("installing Tomact");
			lau.uninstallTomcat(installPath);
			result = las.setupTomcat(softName, installPath, jdkPath,uninstallPath);
		}
		/**
		 * 安装Mysql
		 */
		else if (software.toLowerCase().equals("mysql")) {
			System.out.println("installing MySql");
			lau.uninstallMySql(installPath);
			result = las.setupMySQL(name[0], name[1],installPath,uninstallPath);

		}
		/**
		 * 安装Apache
		 */
		else if (software.toLowerCase().equals("apache")) {
			System.out.println("installing Apache");
			lau.uninstallApache(installPath);
			 result = las.setupApache(softName, installPath, emailAddress,uninstallPath);
		}
		/**
		 * 安装Nginx
		 */
		else if (software.toLowerCase().equals("nginx")) {
			System.out.println("installing Nginx");
			lau.uninstallNginx(installPath);
			result = las.setupNginx(softName, installPath,uninstallPath);
		}
		/**
		 * 安装ZendGuardLoader
		 */
		else if (software.toLowerCase().equals("zendguardloader")) {
			System.out.println("installing ZendGuardLoader");
			lau.uninstallZendGuardLoader(installPath);
			result = las.setupZendGuardLoader(softName, installPath,uninstallPath);
		}
		/**
		 * 安装Python
		 */
		else if (software.toLowerCase().equals("python")) {
			System.out.println("installing Python");
			lau.uninstallPython(installPath);
			result = las.setupPython(softName, installPath,uninstallPath);
		}
		/**
		 * 安装Memcached
		 */
		 else if (software.toLowerCase().equals("memcached")) {
		 System.out.println("installing Memcached");
		 lau.uninstallMemcached(installPath);
		 result = las.setupMemcached(softName, installPath,uninstallPath);
		 }
		/**
		 * 安装FTP
		 */
		else if (software.toLowerCase().equals("ftp")) {
			System.out.println("installing FTP");
			lau.uninstallFTP(installPath);
			result = las.setupFTP(softName,installPath,uninstallPath);
		}
		/**
		 * 安装Oracle11g
		 */
		 else if (software.toLowerCase().equals("oracle11g")) {
		 System.out.println("installing Oracle11g");
		 lau.uninstallOracle11g(installPath+"&"+userName);
		 result = las.setupOracle(name[0], name[1], userName, installPath, oracleinventory, oraclehome, oracle_sid, rootPswd, oradata, uninstallPath);
		 }

		return result;
	}

	public void config(String[] values) throws IOException {
		String software = values[1];
		String result = null;
		WAppConfig wac = new WAppConfig();
		/**
		 * 配置Tomact
		 */
		if (software.toLowerCase().equals("tomcat")) {
			System.out.println("configuring Tomcat " + values[3] + " "
					+ values[4]);
			result = wac.configTomcat(values[2], values[3], values[4]);
		}

		/**
		 * 配置Mysql
		 */
		else if (software.toLowerCase().equals("mysql")) {
			System.out.println("configuring MySql " + values[3] + " "
					+ values[4]);
			result = wac.configMySQL(values[2], values[3], values[4]);

		}
		/**
		 * 配置Apache
		 */
		else if (software.toLowerCase().equals("apache")) {
			System.out.println("configuring Apache " + values[3] + " "
					+ values[4]);
			result = wac.configApache(values[2], values[3], values[4]);
		}
		/**
		 * 配置Nginx
		 */
		else if (software.toLowerCase().equals("nginx")) {
			System.out.println("configuring Nginx " + values[3] + " "
					+ values[4]);
			result = wac.configNginx(values[2], values[3], values[4]);
		}
		/**
		 * 配置ZendGuardLoader
		 */
		else if (software.toLowerCase().equals("zendguardloader")) {
			System.out.println("configuring ZendGuardLoader " + values[3] + " "
					+ values[4]);
			result = wac.configZendGuardLoader(values[2], values[3], values[4]);
		}
	}

	public void config_Linux(String[] values) throws IOException {
		String software = values[1];
		String result = null;
		LAppConfig lac = new LAppConfig();
		/**
		 * 配置Tomact
		 */
		if (software.toLowerCase().equals("tomcat")) {
			System.out.println("configuring Tomcat " + values[3] + " "
					+ values[4]);
			result = lac.configTomcat(values[2], values[3], values[4]);
		}
		/**
		 * 配置Mysql 第一个路径默认不用，从第二个参数名开始。
		 */
		else if (software.toLowerCase().equals("mysql")) {
			System.out.println("configuring MySql " + values[3] + " "
					+ values[4]);
			result = lac.configMySQL(values[3], values[4]);
		}
		/**
		 * 配置Apache
		 */
		else if (software.toLowerCase().equals("apache")) {
			System.out.println("configuring Apache " + values[3] + " "
					+ values[4]);
			result = lac.configApache(values[2], values[3], values[4]);
		}
		/**
		 * 配置Nginx
		 */
		else if (software.toLowerCase().equals("nginx")) {
			System.out.println("configuring Nginx " + values[3] + " "
					+ values[4]);
			result = lac.configNginx(values[2], values[3], values[4]);
		}
		/**
		 * 配置ZendGuardLoader
		 */
		else if (software.toLowerCase().equals("zendguardloader")) {
			System.out.println("configuring ZendGuardLoader " + values[3] + " "
					+ values[4]);
			result = lac.configZendGuardLoader(values[2], values[3], values[4]);
		}

		/**
		 * 配置FTP
		 */
		else if (software.toLowerCase().equals("ftp")) {
			System.out
					.println("configuring FTP " + values[3] + " " + values[4]);
			result = lac.configFTP(values[2], values[3], values[4]);
		}
	}

	public void script(String[] str) {
		String[] result = new String[2];
		/**
		 * 执行脚本
		 */
		WVMScriptExecute wvmse = new WVMScriptExecute();
		System.out.println("executing script " + str[1]);

		result = wvmse.executeVMScript(str[1]);

	}

	public void script_Linux(String[] str) {
		String[] result = new String[2];
		/**
		 * 执行脚本
		 */
		LVMScriptExecute lvmse = new LVMScriptExecute();
		System.out.println("executing script " + str[1]);
		result = lvmse.executeVMScript(str[1]);

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
