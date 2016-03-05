package edu.xidian.linux;

import java.io.File;
import java.io.IOException;

import edu.xidian.windows.AppConfig;

public class LAppUpdate {

/**
 * @author wzy
 * show mysql更新
 * @param serverName 服务端rpm包
 * 				  clientName  客户端rpm包
 * @return 0x0200200 旧版本的mysql卸载成功
 * 		   0x0400200 mysql更新成功
 * 		   0x0400201 mysql更新失败，因为旧版本的msyql下载失败
 * 		   0x0400202 mysql更新失败，因为新版本的msyql安装失败
 */
//	public static String updateMysql(String serverName, String clientName,String installpath,String uninstallpath) throws Exception{
//		System.out.println("server: "+serverName+" client: "+clientName);
//	LAppUninstall un = new LAppUninstall();
//	LAppSetup las = new LAppSetup();
//	String flag1 = "";
//	String lsm = null;
//	 if(flag == "0x0200200"){
//	try{
//			lsm = las.setupMySQL(serverName, clientName, installpath, uninstallpath);
//			if(lsm.equals("0x0100200"))
//				flag1 = "0x0400200";
//			else
//				flag1 = "0x0400204";
//		} catch (Exception  e) {
//			flag1 = "0x0400202";//fail to update mysql
//		}
//	}else{
//		flag1 = "0x0400201";//fail to uninstall mysql
//	}
//	return flag1;
//}
	public static String updateMysql(String serverName, String clientName,String installpath,String uninstallpath) throws Exception{
			System.out.println("server: "+serverName+" client: "+clientName);
		LAppUninstall un = new LAppUninstall();
		AppConfig la = new AppConfig();
		String flag = un.uninstallMySql(uninstallpath);
		String flag1 = "";
		 if(flag == "0x0200200"){
			try {	
				Process process = null;
				String filePath = "/home/softsource/";
				System.out.println("start----------------");				
				
				process = Runtime.getRuntime().exec("chmod 777 /home/softsource -R");
				process.waitFor();
				System.out.println("one-----");
				
				process = Runtime.getRuntime().exec("rpm -ivh "+filePath + serverName + "\r\n");
				process.waitFor();
				Thread.sleep(15000);
				System.out.println("two-----");
				
				process = Runtime.getRuntime().exec("rpm -ivh "+filePath + clientName + "\r\n");
				process.waitFor();
				Thread.sleep(15000);
				System.out.println("three-----");
				
				process = Runtime.getRuntime().exec("cp /usr/share/mysql/my-default.cnf /etc/my.cnf");
				process.waitFor();
				Thread.sleep(1000);
				System.out.println("four-----");
				
				process = Runtime.getRuntime().exec("cp /usr/share/mysql/my-medium.cnf /etc/my.cnf");
				process.waitFor();
				Thread.sleep(1000);
				System.out.println("five-----");

				la.insertToFile("/etc/my.cnf", "[mysqld]", "skip-grant-tables");
				System.out.println("six-----");
				
				process = Runtime.getRuntime().exec("service mysql restart");
				process.waitFor();
				Thread.sleep(5000);
				
				System.out.println("success-----");
				flag1 = "0x0400200";
			} catch (Exception  e) {
				flag1 = "0x0400202";//fail to update mysql
			}
		}else{
			flag1 = "0x0400201";//fail to uninstall mysql
		}
		return flag1;
	}
	
	public String updateTomcat(String TomcatZip,String uninstallPath,String TomcatPath, String JdkPath) throws IOException, Exception {
		LAppSetup ls = new LAppSetup();
		//若安装成功，返回更新成功编码
		if (ls.setupTomcat(TomcatZip, TomcatPath, JdkPath,uninstallPath).equals(
				"0x0100000")) {
			return "0x0400000";

		} else {
			return "0x0400001";
		}
	
}
	public String updateApache(String ZIPFileName,String uninstallPath,String Apachepath, String emailAddress) throws IOException, Exception {

		System.out.println("uninstallPath:"+uninstallPath);
		System.out.println("updatePath:"+Apachepath);
		//若安装成功，返回更新成功编码
			LAppSetup ls = new LAppSetup();
			if (ls.setupApache(ZIPFileName, Apachepath, emailAddress,uninstallPath).equals(
					"0x0100300")) {
				System.out.println("apache one");
				return "0x0400300";

			} else {
				System.out.println("apache two");
				return "0x0400301";
			}
		
	}
	public String updatePython(String ZIPFileName, String path, String uninstallPath) throws IOException, Exception {
		LAppUninstall lu = new LAppUninstall();
		if (lu.uninstallPython(uninstallPath).equals("0x0200600")) {
			LAppSetup ls = new LAppSetup();
			if (ls.setupPython(ZIPFileName, path, uninstallPath).equals("0x0100600")) {
				return "0x0400600";

			} else {
				return "0x0400601";
			}
		} else {
			return "0x0400601";
		}
	}
	public String updateFTP(String FileName,String installPath,String uninstalllPath) throws IOException, Exception {

		LAppSetup setup=new LAppSetup();
		if("0x0100900".equals(setup.setupFTP(FileName, installPath, uninstalllPath))){
			return "0x0400900";								//调用安装函数，如果返回安装成功编码，则返回更新成功编码
		}else{
			return "0x0400901";								//安装失败，返回更新失败编码
		}
	}
	public String updateNginx(String zipFilename,String path,String oldpath) throws InterruptedException{
		LAppSetup las = new LAppSetup();
		String result = las.setupNginx(zipFilename, path, oldpath);
		if(result.equals("0x0100400"))
		{
			return "0x0400400";
		}
		return "0x0400401";
	}


	public String updateZendGuardLoader(String newzip,String phppath,String uninstallPath){
		LAppUninstall un = new LAppUninstall();
		LAppSetup ls = new LAppSetup();
		
		//1.卸载zendguardloader
		String result1 = un.uninstallZendGuardLoader(uninstallPath);
		if(result1.equals("0x0200504")){
			return "0x0400503";
		}
		else if(!result1.equals("0x0200500")){
			return "0x0400501"; 
		}
		//2.安装新版本的zendguardloader
		String result2 = ls.setupZendGuardLoader(newzip,phppath,uninstallPath);
		if(result2 != "0x0100500"){
			return "0x0400502"; 
		}
		return "0x0400500";
	}
	
	
	
	/**
	 * show update memcached
	 * @author wzy
	 * @param fileName memcached的压缩包
	 * @param path 安装路径
	 * @param firstPath 要卸载的memcached安装路径
	 * @return 
	 * 		   0x0100700 memcached卸载成功
	 * 		   0x0400700 更新成功
	 * 		   0x0400701 更新失败，卸载旧版本的失败
	 * 		   0x0400702 更新失败，安装新版本的memcached失败
	 */
	public  String updateMemcached( String fileName, String path,String firstPath) throws Exception{
		LAppSetup ls = new LAppSetup();
		String result = ls.setupMemcached(fileName, path,firstPath);
		if(result.equals("0x0100700"))
		{
			return "0x0400700";
		}
		return "0x0400701";
	}
	
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
	
	//	updateMysql("MySQL-server-5.7.4_m14-1.el6.x86_64.rpm", "MySQL-client-5.7.4_m14-1.el6.x86_64.rpm");
	}

}
