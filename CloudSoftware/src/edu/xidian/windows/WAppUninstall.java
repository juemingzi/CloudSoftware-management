package edu.xidian.windows;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class WAppUninstall {
	WVMScriptExecute wexe = new WVMScriptExecute();
	
	DeletePath de = new DeletePath();
	
	/**
	 * @author wzy
	 *show 卸载mysql
	 *@param mysqlPath 旧版本的msyql的安装路径
	 *@return //0x0100201 mysql存在注册表
	 *		  0x0200200 mysql卸载成功
	 *		  0x0200201 mysql卸载失败，注册表没有删除
	 *		  //0x0100202 msyql已经存在
	 *		  0x0200204 mysql卸载失败，没有进行任何删除操作
	 */
public String uninstallMySql(String mysqlPath) throws Exception{
		
		String code = "";
		//卸载脚本存放的目录
		File filepath = new File("C:\\uninstallscripts\\MySQL\\");
		if (!filepath.isDirectory()) {
			filepath.mkdir();
		}
		/**
		 * mySQL卸载建立四个执行的脚本
		 */
		String filePath = "C:\\uninstallscripts\\MySQL\\";
		
		File stop = new File(filePath + "stop.bat ");
		stop.createNewFile();
		
		File remove = new File(filePath + "remove.bat");
		remove.createNewFile();
		
		File regedit = new File(filePath + "regedit.bat");
		regedit.createNewFile();
		
		File scDelete = new File(filePath + "scdelete.bat");
		scDelete.createNewFile();
		
		
		FileOutputStream outStr1 = new FileOutputStream(stop);
		BufferedOutputStream buff1 = new BufferedOutputStream(outStr1);
		buff1.write("net stop mysql".getBytes());
		buff1.flush();
		buff1.close();
		outStr1.close();
		
		FileOutputStream outStr2 = new FileOutputStream(remove);
		BufferedOutputStream buff2 = new BufferedOutputStream(outStr2);
		buff2.write("mysqld remove".getBytes());
		buff2.flush();
		buff2.close();
		outStr2.close();
		
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("reg delete HKEY_LOCAL_MACHINE\\SYSTEM\\ControlSet001\\services\\eventlog\\Application\\MySQL /va /f ");
		strBuffer.append(System.getProperty("line.separator"));
		strBuffer.append("reg delete HKEY_LOCAL_MACHINE\\SYSTEM\\ControlSet002\\services\\eventlog\\Application\\MySQL /va /f");
		strBuffer.append(System.getProperty("line.separator"));
		strBuffer.append("reg delete HKEY_LOCAL_MACHINE\\SYSTEM\\CurrentControlSet\\services\\eventlog\\Application\\MySQL /va /f");
		strBuffer.append(System.getProperty("line.separator"));
		strBuffer.append("reg delete HKEY_LOCAL_MACHINE\\SYSTEM\\ControlSet001\\services\\MySQL /va /f ");
		strBuffer.append(System.getProperty("line.separator"));
		strBuffer.append("reg delete HKEY_LOCAL_MACHINE\\SYSTEM\\ControlSet002\\services\\MySQL /va /f");
		strBuffer.append(System.getProperty("line.separator"));
		strBuffer.append("reg delete HKEY_LOCAL_MACHINE\\SYSTEM\\CurrentControlSet\\services\\MySQL /va /f");
		strBuffer.append(System.getProperty("line.separator"));
		
		PrintWriter printWriter = new PrintWriter(filePath + "regedit.bat");
		printWriter.write(strBuffer.toString().toCharArray());
		printWriter.flush();
		printWriter.close();
		
		FileOutputStream outStr3 = new FileOutputStream(scDelete);
		BufferedOutputStream buff3 = new BufferedOutputStream(outStr3);
		buff3.write("sc delete mysql".getBytes());
		buff3.flush();
		buff3.close();
		outStr3.close();
		
		WVMScriptExecute wexe = new WVMScriptExecute();
		
		System.out.println("step1:停止服务");
		String mySQLPath = filePath + "stop.bat";
		System.out.println(mySQLPath);
		wexe.executeVMScript(mySQLPath);// 关闭mysql服务
		Thread.sleep(5000);
		
		System.out.println("step2:移出服务");
		String removePath = filePath + "remove.bat";
		System.out.println(removePath);
		wexe.executeVMScript(removePath);// 删除mysql服务
		Thread.sleep(5000);
		
		System.out.println("step3:清除注册表");
		String regeditRemove = filePath + "regedit.bat";
		System.out.println(regeditRemove);
		wexe.executeVMScript(regeditRemove);// 删除注册表
		Thread.sleep(5000);
		
		//删除mysql所有文件
		System.out.println("step4:删除mysql文件");
//		File dir = new File(mysqlPath + "/");
//    	String mysqlFilePath = "";
//    	File[] fs = dir.listFiles();
//    	for(int i=0;i<fs.length;i++){
//    		if(fs[i].toString().toLowerCase().contains("mysql")){
//    			mysqlFilePath = fs[i].getAbsolutePath();
//    			System.out.println(mysqlFilePath+"=============");
//    			break;
//    		}
//    	}
    	
    	de.deletePath(WAppSetup.mysqlPath);
		deleteDirectory(mysqlPath);
		
		System.out.println("step5:清除服务表中的服务");
		String scdelete = filePath + "scdelete.bat";
		System.out.println(scdelete);
		wexe.executeVMScript(scdelete);// 删除服务中的服务
		Thread.sleep(5000);
		
		PreInstall pre = new PreInstall();
		String preCode = pre.checkMySQL();
		if(preCode == "0x0100201"){
			code = "0x0200201";
			System.out.println("卸载失败，注册表没有删除");
		}else if(preCode == "0x0100202"){
			code = "0x0200204";
			System.out.println("卸载失败，根本就没有执行卸载过程");
		}else{
			code = "0x0200200";
			System.out.println("卸载成功");
		}
		System.out.println("end");
		return code;
		
	}
	
	/**
	 * 删除目录（文件夹）以及目录下的文件
	 * 
	 * @param sPath
	 *            被删除目录的文件路径
	 * @return 目录删除成功返回true，否则返回false
	 */
	public static boolean deleteDirectory(String sPath) {
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		boolean flag = false;
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} // 删除子目录
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 删除单个文件
	 * 
	 * @param sPath
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String sPath) {
		boolean flag = false;
		flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}
	
	
	//卸载nginx
	public String ss(String nginxpath){
		try {
			//更改输入的路径情况：
			String nginxpath_temp = null;
			String nginxpaths[] = nginxpath.split("/");
			for(int i = 0 ; i < nginxpaths.length ; i++)
			{
				nginxpath_temp = nginxpath_temp + nginxpaths[i] + "\\";
			}
			
			File batfile = new File("C:\\setupscripts\\nginx\\uninstall.bat");
			batfile.createNewFile();
			FileOutputStream outStr = new FileOutputStream(batfile);
			BufferedOutputStream buff = new BufferedOutputStream(outStr);
			buff.write(("taskkill /F /IM nginx*\r\n").getBytes());
			buff.write(("rd /s/q "+nginxpath).getBytes());
			buff.flush();
			buff.close();
			outStr.close();

			wexe.executeVMScript("C:\\setupscripts\\nginx\\uninstall.bat");
			
			return "0x0200400";
		} catch (IOException e) {
			return "0x0200401";
		}
	}
	
		//卸载zendguardloader
public String uninstallZendGuardLoader(String phppath){
	phppath = phppath.replace("/", "\\");
		try {
			
			File filepath ;
			// 卸载脚本存放的目录
			filepath = new File("C:\\uninstallscripts\\zendguardloader\\");
			if (!filepath.isDirectory()) {
				filepath.mkdir();
			}
			//1.删除文件
			File uninstall = new File("C:\\uninstallscripts\\zendguardloader\\uninstall.bat");
			FileOutputStream outStr;
			outStr = new FileOutputStream(uninstall);
			BufferedOutputStream buff = new BufferedOutputStream(outStr);
			
			uninstall.createNewFile();
			buff.write(("del "+phppath+"\\ext\\ZendLoader.dll\r").getBytes());
			buff.flush();
			buff.close();
			outStr.close();
			wexe.executeVMScript("C:\\uninstallscripts\\zendguardloader\\uninstall.bat");
			//2.删除配置信息
			AppConfig.replaceToFile("C:\\windows\\php.ini", "[Zend.loader]", "");
			AppConfig.replaceToFile("C:\\windows\\php.ini", "zend_extension=", "");
			AppConfig.replaceToFile("C:\\windows\\php.ini", "zend_loader.enable", "");
			AppConfig.replaceToFile("C:\\windows\\php.ini", "zend_loader.disable_licensing", "");
			AppConfig.replaceToFile("C:\\windows\\php.ini", "zend_loader.obfuscation_level_support", "");
			
			 return "0x0200500";
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "0x0200501";
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "0x0200502";
		}
		
	}

	//卸载oracle
public String uninstallOracle11g(String oraclepath){
	String result="";
	System.out.println("oraclepath%%%%%%%%%%%%%"+oraclepath);
	String[] oraclepath_a = oraclepath.split("&");
	String oraclebase = oraclepath_a[0];
	String oraclehome = oraclepath_a[1];
	oraclebase = oraclebase.replaceAll("/", "\\\\");
	oraclehome = oraclehome.replaceAll("/", "\\\\");
	System.out.println("oraclebase              oraclehome  "+oraclebase+"                "+oraclehome);
	
	if(!new File(oraclebase).exists())
		return "";
	
	
	try {

		// 卸载脚本存放的目录
		File filepath = new File("C:\\uninstallscripts\\oracle\\");
		if (!filepath.isDirectory()) {
			filepath.mkdir();
		}
		
		//1.停止现有的所有oracle服务
		File batfile1 = new File("C:\\uninstallscripts\\oracle\\stoporacleservice.bat");
		FileOutputStream outStr1 = new FileOutputStream(batfile1);
		BufferedOutputStream buff1 = new BufferedOutputStream(outStr1);
		
		batfile1.createNewFile();
		buff1.write("net stop Oracle ORCL VSS Writer Sevice\r\n".getBytes());
		buff1.write("net stop OracleDBConsoleorcl\r\n".getBytes());
		buff1.write("net stop OracleJobSchedulerORCL\r\n".getBytes());
		buff1.write("net stop OracleMTSRecoveryService\r\n".getBytes());
		buff1.write("net stop OracleOraDb11g_home1ClrAgent\r\n".getBytes());
		buff1.write("net stop OracleOraDb11g_home1TNSListener\r\n".getBytes());
		buff1.write("net stop OracleRemExecService\r\n".getBytes());
		buff1.write("net stop OracleServiceORCL\r\n".getBytes());
		buff1.flush();
		buff1.close();
		outStr1.close();
		
		wexe.executeVMScript("C:\\uninstallscripts\\oracle\\stoporacleservice.bat");
	
		Thread.sleep(5000);
		
		//2.执行oracle卸载脚本deinstall.bat
		String s = oraclebase+"\\product\\11.2.0\\"+oraclehome+"\\deinstall\\deinstall.bat";
		
		
		if(!new File(s).exists())
			return "";
		
		
		wexe.executeVMScript(oraclebase+"\\product\\11.2.0\\"+oraclehome+"\\deinstall\\deinstall.bat -silent");
		
		//3.删除相关的注册表
		File batfile2 = new File("C:\\uninstallscripts\\oracle\\delregedit.bat");
		FileOutputStream outStr2 = new FileOutputStream(batfile2);
		BufferedOutputStream buff2 = new BufferedOutputStream(outStr2);
		
		batfile2.createNewFile();
		buff2.write("reg delete HKCR\\OracleConfig.OracleConfig /f \r\n".getBytes());
		buff2.write("reg delete HKCR\\OracleConfig.OracleConfig.1 /f\r\n".getBytes());
		buff2.write("reg delete HKCR\\OracleDatabase.OracleDatabase /f\r\n".getBytes());
		buff2.write("reg delete HKCR\\OracleDatabase.OracleDatabase.1 /f\r\n".getBytes());
		buff2.write("reg delete HKCR\\OracleHome.OracleHome /f\r\n".getBytes());
		buff2.write("reg delete HKCR\\OracleHome.OracleHome.1 /f\r\n".getBytes());
		buff2.write("reg delete HKCR\\OracleProcess.OracleProcess /f\r\n".getBytes());
		buff2.write("reg delete HKCR\\OracleProcess.OracleProcess.1 /f\r\n".getBytes());
		buff2.write("reg delete HKCR\\ORAMMCCFG11.ComponentData /f\r\n".getBytes());
		buff2.write("reg delete HKCR\\ORAMMCCFG11.ComponentData.1 /f\r\n".getBytes());
		buff2.write("reg delete HKCR\\ORAMMCPMON11.ComponentData.1 /f\r\n".getBytes());
		buff2.write("reg delete HKCR\\ORAMMCPMON11.ComponentData.1 /f\r\n".getBytes());
		buff2.write("reg delete HKCR\\OraOLEDB.ErrorLookup /f\r\n".getBytes());
		buff2.write("reg delete HKCR\\OraOLEDB.ErrorLookup.1 /f\r\n".getBytes());
		buff2.write("reg delete HKCR\\OraOLEDB.Oracle /f\r\n".getBytes());
		buff2.write("reg delete HKCR\\OraOLEDB.Oracle.1 /f\r\n".getBytes());
		buff2.write("reg delete HKCR\\OraPerfMon.OraPerfMon /f\r\n".getBytes());
		buff2.write("reg delete HKCR\\OraPerfMon.OraPerfMon.1 /f\r\n".getBytes());
		buff2.write("reg delete HKCR\\ORCLMMC.About /f\r\n".getBytes());
		buff2.write("reg delete HKCR\\ORCLMMC.About.1 /f\r\n".getBytes());
		buff2.write("reg delete HKCR\\ORCLSSO.ComponentData /f\r\n".getBytes());
		buff2.write("reg delete HKCR\\ORCLSSO.ComponentData.1 /f\r\n".getBytes());
		
		buff2.write("reg delete \"HKLM\\SYSTEM\\CurrentControlSet\\services\\eventlog\\Application\\Oracle Services for MTS\" /f\r\n".getBytes());
		buff2.write("reg delete HKLM\\SYSTEM\\CurrentControlSet\\services\\eventlog\\Application\\Oracle.orcl /f\r\n".getBytes());
		buff2.write("reg delete HKLM\\SYSTEM\\CurrentControlSet\\services\\eventlog\\Application\\Oracle.VSSWriter.ORCL /f\r\n".getBytes());
		buff2.write("reg delete HKLM\\SYSTEM\\CurrentControlSet\\services\\eventlog\\Application\\OracleDBConsoleorcl /f\r\n".getBytes());
		
		buff2.write("reg delete HKLM\\SYSTEM\\CurrentControlSet\\services\\Oracle11 /f\r\n".getBytes());
		buff2.write("reg delete HKLM\\SOFTWARE\\ORACLE /f\r\n".getBytes());
		
		buff2.write("reg delete HKLM\\SYSTEM\\CurrentControlSet\\services\\OracleDBConsoleorcl /f\r\n".getBytes());
		buff2.write("reg delete HKLM\\SYSTEM\\CurrentControlSet\\services\\OracleJobSchedulerORCL /f\r\n".getBytes());
		buff2.write("reg delete HKLM\\SYSTEM\\CurrentControlSet\\services\\OracleMTSRecoveryService /f\r\n".getBytes());
		buff2.write("reg delete HKLM\\SYSTEM\\CurrentControlSet\\services\\OracleOraDb11g_home1ClrAgent /f\r\n".getBytes());
		buff2.write("reg delete HKLM\\SYSTEM\\CurrentControlSet\\services\\OracleOraDb11g_home1TNSListener /f\r\n".getBytes());
		buff2.write("reg delete HKLM\\SYSTEM\\CurrentControlSet\\services\\OracleServiceORCL /f\r\n".getBytes());
		buff2.write("reg delete HKLM\\SYSTEM\\CurrentControlSet\\services\\OracleVssWriterORCL /f\r\n".getBytes());
		
		buff2.flush();
		buff2.close();
		outStr2.close();
		
		wexe.executeVMScript("C:\\uninstallscripts\\oracle\\delregedit.bat");
		
		new DeletePath().deletePath(oraclebase + "\\produc\\11.2.0\\dbhome1\\bin");
		
		//4.写好删除程序文件夹的脚本
		File batfile3 = new File("C:\\uninstallscripts\\oracle\\delfile.bat");
		FileOutputStream outStr3 = new FileOutputStream(batfile3);
		BufferedOutputStream buff3 = new BufferedOutputStream(outStr3);
		
		batfile3.createNewFile();
		buff3.write(("rd /s/q "+oraclebase+"\r\n").getBytes());
		buff3.write("rd /s/q \"C:\\Program Files\\Oracle\"\r\n".getBytes());
		
		buff3.flush();
		buff3.close();
		outStr3.close();
		
		
		//5.将重启后需要执行的脚本写入文件
		File rebootfile = new File("C:/rebootscript.txt");
		FileOutputStream outStr4 = new FileOutputStream(rebootfile);
		BufferedOutputStream buff4 = new BufferedOutputStream(outStr4);
		
		buff4.write("C:\\uninstallscripts\\oracle\\delfile.bat\r".getBytes());
		buff4.flush();
		buff4.close();
		outStr4.close();
		return "0x0200C00";
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return "0x0200C01";
	}catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return "0x0200C02";
	}
}
	
public String uninstallTomcat(String uninstallPath) {
	boolean flag = false;
	//停止tomcat服务
	StopTomcat st = new StopTomcat();
	try {
		st.stop(uninstallPath);
		System.out.println("stop");
	} catch (IOException e) {
		e.printStackTrace();
		System.out.println("stop2");
	}
	System.out.println("~~~~~~~~~~~~~~~~~delete path");
	de.deletePath(WAppSetup.tomcatPath);
	// 删除uninstallpath下包含tomcat的目录
	Utils.delFolder(uninstallPath);
	flag=true;
	if (!flag)
		return "0x0200001";
	else
		return "0x0200000";

}


public String uninstallApache(String uninstallPath) throws FileNotFoundException {
	boolean flag = false;
	String unFilePath = "C:\\uninstallscripts\\apache";
	File f = new File(unFilePath);
	if (!f.isDirectory()) {
		f.mkdirs();
	}
	
	StringBuffer strBuffer = new StringBuffer();
	strBuffer.append("reg delete \"HKEY_LOCAL_MACHINE\\SOFTWARE\\Wow6432Node\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\{85262A06-2D8C-4BC1-B6ED-5A705D09CFFC}\" /va /f ");
	strBuffer.append(System.getProperty("line.separator"));		
	PrintWriter printWriter = new PrintWriter("C:\\uninstallscripts\\apache\\regedit.bat");
	printWriter.write(strBuffer.toString().toCharArray());
	printWriter.flush();
	printWriter.close();
	wexe.executeVMScript("C:\\uninstallscripts\\apache\\regedit.bat");
	
	
	File batfile = new File("C:\\uninstallscripts\\apache\\uninstall.bat");
	File apache1File = new File("C:\\Apache1");
	File apache2File = new File("C:\\Apache24");
	try {
		batfile.createNewFile();
		FileOutputStream outStr = new FileOutputStream(batfile);
		BufferedOutputStream buff = new BufferedOutputStream(outStr);
	
		buff.write(("cd "+uninstallPath+"/bin\r\n").getBytes());
		buff.write(("httpd.exe -k stop\r\n").getBytes());
		buff.write(("httpd.exe -k uninstall\r\n").getBytes());
		buff.write(("taskkill /F /IM Apache*\r\n").getBytes());
		buff.write(("taskkill /F /IM httpd*\r\n").getBytes());
		
		buff.flush();
		buff.close();
		outStr.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	wexe.executeVMScript("C:\\uninstallscripts\\apache\\uninstall.bat");
	try {
		Thread.sleep(3000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	//Utils.delFolder(uninstallPath);

	flag =deleteDirectory(uninstallPath);
	
	if (!flag)
	{
		return "0x0200301";
	}
	else
		return "0x0200300";
}


//	public String uninstallApache(String uninstallPath) throws FileNotFoundException {
//		boolean flag = false;
//		String unFilePath = "C:\\uninstallscripts\\apache";
//		File f = new File(unFilePath);
//		if (!f.isDirectory()) {
//			f.mkdirs();
//		}
//		
//		StringBuffer strBuffer = new StringBuffer();
//		strBuffer.append("reg delete \"HKEY_LOCAL_MACHINE\\SOFTWARE\\Wow6432Node\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\{85262A06-2D8C-4BC1-B6ED-5A705D09CFFC}\" /va /f ");
//		strBuffer.append(System.getProperty("line.separator"));		
//		PrintWriter printWriter = new PrintWriter("C:\\uninstallscripts\\apache\\regedit.bat");
//		printWriter.write(strBuffer.toString().toCharArray());
//		printWriter.flush();
//		printWriter.close();
//		wexe.executeVMScript("C:\\uninstallscripts\\apache\\regedit.bat");
//		
//		
//		File batfile = new File("C:\\uninstallscripts\\apache\\uninstall.bat");
//		File apache1File = new File("C:\\Apache1");
//		File apache2File = new File("C:\\Apache24");
//		try {
//			batfile.createNewFile();
//			FileOutputStream outStr = new FileOutputStream(batfile);
//			BufferedOutputStream buff = new BufferedOutputStream(outStr);
//			if (apache1File.exists()) {
//				buff.write(("cd C:/Apache1/bin\r\n").getBytes());
//				buff.write(("httpd.exe -k stop\r\n").getBytes());
//				buff.write(("httpd.exe -k uninstall\r\n").getBytes());
//			}
//			if (apache2File.exists()) {
//				buff.write(("cd C:/Apache24/bin\r\n").getBytes());
//				buff.write(("httpd.exe -k stop\r\n").getBytes());
//				buff.write(("httpd.exe -k uninstall\r\n").getBytes());
//			}
//			
//			buff.write(("cd "+uninstallPath+"/bin\r\n").getBytes());
//			buff.write(("httpd.exe -k stop\r\n").getBytes());
//			buff.write(("httpd.exe -k uninstall\r\n").getBytes());
//			buff.write(("taskkill /F /IM Apache*\r\n").getBytes());
//			buff.write(("taskkill /F /IM httpd*\r\n").getBytes());
//			
//			buff.flush();
//			buff.close();
//			outStr.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		wexe.executeVMScript("C:\\uninstallscripts\\apache\\uninstall.bat");
//		try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		if (apache1File.exists())  Utils.delFolder("C:\\Apache1");
//		if(apache2File.exists()) Utils.delFolder("C:\\Apache24");
////		
//		
//		Utils.delFolder(uninstallPath);//删除目录下所有文件
//		flag=true;
//		if (!flag)
//		{
//			return "0x0200301";
//		}
//		else
//			return "0x0200300";
//	}

	public String uninstallPython(String uninstallPath) throws FileNotFoundException
	{
		boolean flag = false;
		de.deletePath(WAppSetup.pythonPath);
		flag = deleteDirectory(uninstallPath);
		if (!flag)
			return "0x0200601";
		else
			return "0x0200600";
	}
	public String uninstallFTP(String uninstallPath) throws FileNotFoundException {
		boolean flag = false;
		uninstallPath=uninstallPath.replaceAll("/", "\\\\");
		String unFilePath = "C:\\uninstallscripts\\FileZilla";
		File f = new File(unFilePath);
		if (!f.isDirectory()) {
			f.mkdirs();
		}
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("reg delete HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\FileZilla Server /va /f ");
		strBuffer.append(System.getProperty("line.separator"));
		strBuffer.append("reg delete \"HKEY_LOCAL_MACHINE\\SOFTWARE\\Wow6432Node\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\FileZilla Server\" /va /f ");
		strBuffer.append(System.getProperty("line.separator"));
		
		PrintWriter printWriter = new PrintWriter("C:\\uninstallscripts\\FileZilla\\regedit.bat");
		printWriter.write(strBuffer.toString().toCharArray());
		printWriter.flush();
		printWriter.close();
		wexe.executeVMScript("C:\\uninstallscripts\\FileZilla\\regedit.bat");
		
		File batfile = new File(
				"C:\\uninstallscripts\\FileZilla\\uninstall.bat");
		try {
			batfile.createNewFile();
			FileOutputStream outStr = new FileOutputStream(batfile);
			BufferedOutputStream buff = new BufferedOutputStream(outStr);
			buff.write(("taskkill /F /IM FileZilla*\r\n").getBytes());
			buff.flush();
			buff.close();
			outStr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		wexe.executeVMScript("C:\\uninstallscripts\\FileZilla\\uninstall.bat");
	
	
		if(uninstallPath!=null&&new File(uninstallPath).exists())
			flag=deleteDirectory(uninstallPath);				
		//删除桌面快捷方式
		File[] fileList1 = new File("C:\\Users\\Public\\Desktop").listFiles();
		for (File file : fileList1) {
			if (file.getName().contains("FileZilla")) {
				file.delete();
				break;
			}
		}
		//删除开始菜单快捷方式
		File startMenuFile =new File("C:\\ProgramData\\Microsoft\\Windows\\Start Menu\\Programs\\FileZilla Server");
		if(startMenuFile.exists()){
			deleteDirectory(startMenuFile.getAbsolutePath());
		}
		if (!flag)
			return "0x0200901";
		else
			return "0x0200900";
	}
	public String uninstallIISRewrite(String iisRewritePath)
			throws FileNotFoundException {
		boolean flag = false;
		iisRewritePath = iisRewritePath.replaceAll("/", "\\\\");
	
		if (new File(iisRewritePath).exists()) {
			flag = true;
		}
		if (!flag)
			return "0x0200802";
		flag = deleteDirectory(iisRewritePath);
		if (!flag)
			return "0x0200801";
		else
			return "0x0200800";

	}
	
	/**
     * @author wzy
     * show 卸载memcached
     * @param FileName 是要卸载的软件的名称
     * @return 0x0200700 卸载成功
     */
public static String uninstallMemcached(String uninstallpath) throws Exception{
		
		//ת���û��ṩ��·��
		String uninstallpath_temp = uninstallpath.replaceAll("/", "\\\\");
		
    	String code = "";
    	File f1 = new File("C:\\uninstallscripts");
    	if(!f1.isDirectory())
    	{
    		f1.mkdir();
    	}
    	String unFilePath = "C:\\uninstallscripts\\unmemecached";
    	File f2 = new File(unFilePath);
		if (!f2.isDirectory()) {
			f2.mkdir();
		}
		File f3 = new File("C:\\uninstallscripts\\unmemecached\\unmemecached.bat");
		if(!f3.isFile())
		{
			f3.createNewFile();
		}
		
		File file = new File(uninstallpath);
		if(!file.exists())
		{
			return "0x0200700";
		}
		File files[] = file.listFiles();
		String file_name = "";
		//����nginx���ڵ��ļ���
		for(File fm : files)
		{
			if(fm.getName().trim().startsWith("memcached"));
			{
				file_name = file_name + fm.getName();
				break;
			}
		}
		String exe_filename = uninstallpath + "\\" + file_name + "\\" + file_name + ".exe";
		
		// д�ű�
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("@echo off");
		strBuffer.append(System.getProperty("line.separator"));
		strBuffer.append(exe_filename + " -d stop");
		strBuffer.append(System.getProperty("line.separator"));
		strBuffer.append(exe_filename + " -d uninstall");
		strBuffer.append(System.getProperty("line.separator"));
		strBuffer.append("rd /s/q "+ uninstallpath_temp);
		strBuffer.append(System.getProperty("line.separator"));
		strBuffer.append("exit");
		strBuffer.append(System.getProperty("line.separator"));
		
		PrintWriter printWriter = new PrintWriter(unFilePath + "\\unmemecached.bat");
		printWriter.write(strBuffer.toString().toCharArray());
		printWriter.flush();
		printWriter.close();
		// ִ�нű�
		String jdkCommand = "cmd /c  C:\\uninstallscripts\\unmemecached\\unmemecached.bat";
		System.out.println("second"+jdkCommand);
		WVMScriptExecute we = new WVMScriptExecute();
		we.executeVMScript(jdkCommand);
		
		
		code = "0x0200700"; 
		
    	return code;
    }
    
    /**@author zk
	 *   卸载SqlServer时执行三个脚本 1 卸载脚本 2 删除相关文件的脚本 3删除注册表信息的脚本
	 * @return
	 * @throws IOException 
	 */
public String  uninstallSQLServer2008R2(String oldPath) throws IOException{//执行卸载脚本的接口
		
//		try {
//			Process process = Runtime.getRuntime().exec("osql /?");
//			BufferedReader strCon = new BufferedReader(new InputStreamReader(
//					process.getInputStream()));
//			String line;
//			int i = 1;
//			while ((line = strCon.readLine()) != null) {
//				if (i == 2) {
//					String info = "已安装的SqlServer2008R2   " + line;
//					System.out.println(info);// 如果已安装SqlServer2008R2，则输入osql
//												// /?则输出的第二行为已安装的版本信息
//				}
//				i++;
//			}
//		} catch (IOException e) {
//			return "0x0200A02";   //软件不存在，无需卸载
//		}
		
		
	String appConfigFilePath = "C:\\setupscripts\\";
		//System.out.println("1 kaishixiezai");
		String[] scriptUninstallResult=wexe.executeVMScript(appConfigFilePath+"SqlServer2008R2\\uninstallSqlServer2008R2.bat");//脚本执行结果返回的是一个字符串数组
		
		if(!scriptUninstallResult[0].equalsIgnoreCase("0x0500000")){
			if(scriptUninstallResult[0].equalsIgnoreCase("0x0500001"))
				return "0x0200A03";//执行SqlServer2008R2卸载脚本失败,出现非可执行命令的异常,还没有删除相关文件夹
			else {
				return "0x0200A04";//执行SqlServer2008R2卸载脚本失败,出现命令被InterruptedException异常,还没有删除相关文件夹
			}
		}		
		//System.out.println("2 kaishishanchuwenjian");
		String[] scriptDelFileResult=wexe.executeVMScript(appConfigFilePath+"SqlServer2008R2\\delInstallFileScript.bat");

		if(scriptDelFileResult[0].equalsIgnoreCase("0x0500000"))
			return "0x0200A00";//返回卸载SqlServer2008R2成功
		else
			return "0x0200A01";//彻底卸载SqlServer2008R2失败
	}
  


public String uninstallNginx(String nginxpath){
	try {
		//锟斤拷锟斤拷锟斤拷锟斤拷路锟斤拷锟斤拷锟斤拷锟?
		String nginxpath_temp = nginxpath.replaceAll("/", "\\\\");
		
		File batfile = new File("C:\\setupscripts\\nginx\\uninstall.bat");
		batfile.createNewFile();
		FileOutputStream outStr = new FileOutputStream(batfile);
		BufferedOutputStream buff = new BufferedOutputStream(outStr);
		buff.write(("taskkill /F /IM nginx*\r\n").getBytes());
		buff.write(("rd /s/q "+ nginxpath_temp).getBytes());
		buff.flush();
		buff.close();
		outStr.close();

		wexe.executeVMScript("C:\\setupscripts\\nginx\\uninstall.bat");
		
		return "0x0200400";
	} catch (IOException e) {
		return "0x0200401";
	}
}
}


