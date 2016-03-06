package edu.xidian.windows;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import edu.xidian.agent.FtpTransFile;

public class WAppUpdate {
	WAppUninstall wau = new WAppUninstall();
	WAppSetup was = new WAppSetup();
	WAppConfig wac = new WAppConfig();
	/**
	 * @author wzy
	 * show 更新mysql
	 * @param zipFileName 安装包的名字
	 * @param unzipDirectory 解压后存放的位置
	 * @param mysqlPath 原来mysql的安装位置
	 * @param password 更新的数据库密码
	 * @return 0x0200200 卸载成功
	 * 		   0x0100200 安装成功
	 * 		   0x0400200 更新成功
	 * 		   0x0400201 mysql更新失败，因为旧版本的mysql下载失败
	 *         0x0400202 mysql更新失败，因为新版本的mysql安装失败
	 */
public static String updateMySql( String zipFileName, String mysqlPath,String unzipDirectory,String password) throws Exception{
		
		WAppUninstall wu = new WAppUninstall();
		WAppSetup ws = new WAppSetup();
		String funinstall = wu.uninstallMySql(mysqlPath);	
		String setup = "";
		String update = "";
		 if(funinstall == "0x0200200"){
			 mysqlPath = null;
			setup = ws.setupMySql(zipFileName, unzipDirectory, password, mysqlPath);
			if(setup == "0x0100200"){
				update = "0x0400200";
				System.out.println("软件更新成功");
			}else {
				update = "0x0400202";
				System.out.println("软件安装失败导致的软件更新失败");//只是返回了编码，没有进行其它的操作
			}
		}else{
			update = "0x0400201";
			System.out.println("软件卸载失败导致的更新失败");
		}
		System.out.println("end");
		return update;
	}


	// 升级nginx
public String updateNginx(String zipFileName, String path, String oldnginxpath) {
	String result = was.setupNginx(zipFileName, path, oldnginxpath);
	if(result.equals("0x0100400"))
	{
		return "0x0400400";
	}
	return "0x0400401";
}
	

		// 升级zendguardloader
public String updateZendGuardLoader(String newzip,String phppath ,String uninstallPath) throws FileNotFoundException, InterruptedException {
		
		// 1.卸载zendguardloader
		String result1 = wau.uninstallZendGuardLoader(uninstallPath);
		if (result1 == "0x0200504") {	
			return "0x0400503";
		}else if(result1 != "0x0200500") {
			return "0x0400501";
		}
		
	
		// 2.安装新版本的zendguardloader
		String result2 = was.setupZendGuardLoader(newzip, phppath,uninstallPath);

		if (result2 != "0x0100500") {
			return "0x0400502";
		}
		return "0x0400500";
	}

	
public String updateTomcat(String TomcatZip, String TomcatPath,
		String JDKPath,String unistallPath) throws IOException {
	//tomcatzip安装包名称，tomcatpath安装路径,jdkpath jdk路径，uninstallpath旧版本路径
	
		WAppSetup ws = new WAppSetup();
		if (ws.setupTomcat(TomcatZip, TomcatPath, JDKPath,unistallPath).equals("0x0100000")) {
			return "0x0400000";
		} else {
			return "0x0400001";
		}
	
	
}

	public String updateApache(String ApacheFileName, String ApachePath,
			String emailAddress,String uninstallpath) throws IOException {
		//ApacheFileName安装包名称，ApachePath安装路径,emailAddress 电子邮箱，uninstallpath旧版本路径
		
		
			WAppSetup ws = new WAppSetup();
			if (ws.setupApache(ApacheFileName, ApachePath, emailAddress,uninstallpath).equals(
				"0x0100300")) {
				return "0x0400300";
			} else {
				return "0x0400301";
			}
		
	}

	public String updatePython(String PythonFileName, String path, String oldPath) throws IOException
	{
		// 该盘下搜索，先删除再安装
		// Utils.findDirs("C:/", "python");
		// wau.uninstallPython();
		WAppSetup ws = new WAppSetup();
		if (ws.setupPython(PythonFileName, path, oldPath).equals("0x0100600"))
		{
			return "0x0400600";
		} else
		{
			return "0x0400601";
		}
	}

	public String updateFTP(String FTPFileName, String installPath,String uninstallPath)
			throws IOException {
	
		WAppSetup ws = new WAppSetup();
		if (ws.setupFTP(FTPFileName, installPath,uninstallPath).equals("0x0100900")) {
			return "0x0400900";
		} else {
			return "0x0400901";
		}
	}


	public String update360(String FileName, String Path) throws IOException {
		// 该盘下搜索，先删除再安装
		Utils.findDirs(Path, "360");
		//wau.uninstall360();
		WAppSetup ws = new WAppSetup();
		if (ws.setup360(FileName, Path,null).equals("0x0100E00")) {
			return "0x0400E00";
		} else {
			return "0x0400E01";
		}
	}
	


	/**
	 * @author wzy
	 * show 更新memcached
	 * @param fileName1 要卸载的名字 
	 * @param fileName2 要更新的名字
	 * @return 0x0200700 memcached卸载成功
	 * 		   0x0400701更新失败，因为卸载旧版本的memcached失败
	 *		   0x0100700 memcached新版本的安装成功 
	 * 		   0x0400700 更新成功
	 * 		   0x0400702 更新失败，因为新版的memcached安装失败
	 */
	public String updateMemcached(String filename,String newpath,String oldpath) throws Exception{
		String result = was.setupMemcached(filename, newpath, oldpath);
		if(result.equals("0x0100700"))
		{
			return "0x0400700";
		}		
		return "0x0400701";
}
}
