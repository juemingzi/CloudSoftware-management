package edu.xidian.windows;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.SequenceInputStream;

public class PreInstall {

	/**
	 * @author wzy
	 * show  检测mysql是否安装
	 * @return 0x0100201 mysql没有安装，存在注册表
	 * 		   0x0100202:系统装有mysql
	 * 		   0x0100203 注册表不存在，现在可以装mysql
	 * @throws Exception 
	 */
	public static String checkMySQL() throws Exception{
		System.out.println("==========mysql安装前检测开始==========");
		String filePath = "C:\\preinstall\\MySQL\\";//检测mysql是否安装的配置文件存放位置

		//检测脚本存放的目录
		File filepath = new File(filePath);
		if (!filepath.isDirectory()) {
			filepath.mkdir();
		}
		
		
		File regedit = new File(filePath + "regedit.bat ");
		regedit.createNewFile();
		
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("@echo off");
		strBuffer.append(System.getProperty("line.separator"));
		strBuffer.append("reg query HKEY_LOCAL_MACHINE\\SYSTEM\\ControlSet001\\services\\eventlog\\Application\\MySQL /v EventMessageFile>nul 2>nul&&echo 存在MySQL||echo 不存在MySQL");
		strBuffer.append(System.getProperty("line.separator"));
		strBuffer.append("pause>nul");
		strBuffer.append(System.getProperty("line.separator"));
		PrintWriter printWriter = new PrintWriter(filePath + "regedit.bat");
		printWriter.write(strBuffer.toString().toCharArray());
		printWriter.flush();
		printWriter.close();
		
		
		String mySQLPath = filePath + "regedit.bat";//检测mysql是否安装的配置文件路径
		System.out.println(mySQLPath);
		String s1 = executeVMScriptMysql(mySQLPath);//执行脚本，察看mysql的注册表是否存在
		
		System.out.println("检测中................."+s1);
		
		String flag ="";
		if(s1.equals("存在MySQL")){//判断是存在mysql还是存在注册表，无论哪种情况都不可安装mysql
			System.out.println("MySQL的注册表存在");				
			//检测mysql是否安装成功
			 Process process = Runtime.getRuntime ().exec ("cmd");
	         SequenceInputStream sis = new SequenceInputStream (process.getInputStream (), process.getErrorStream ());
	         InputStreamReader isr = new InputStreamReader (sis, "gbk");
	         BufferedReader br = new BufferedReader (isr);
	         // next command
	         OutputStreamWriter osw = new OutputStreamWriter (process.getOutputStream ());
	         BufferedWriter bw = new BufferedWriter (osw);
	         bw.write ("net start mysql");
	         bw.newLine();//不能删掉
	         bw.flush ();
	         bw.close ();
	         osw.close ();
	         // read
	         String line = null;
	         String tem = "";
	         while (null != ( line = br.readLine () ))
	         {
	             tem += line;
	         }
	         process.destroy ();
	         br.close ();
	         isr.close ();
	     	System.out.println("-----");
			System.out.println(tem);
			System.out.println("-----");
			if(tem.contains("不是内部或外部命令")){
				flag ="0x0100201";//mysql没有安装，存在注册表
				System.out.println("mysql没有卸载干净");
			}else{
				flag = "0x0100202";//安装有mysql
				System.out.println("系统装有mysql");
			}
		}else{
			flag = "0x0100203";//系统可以装mysql
			System.out.println("注册表不存在，现在可以装mysql");
		}
		System.out.println("==========mysql安装前检测结束=========="+flag);
		return flag;
	}
	
	/**
	 * @author wzy
	 * @param fileName 要安装的memcached的名字
	 * show 检测memcached是否安装
	 * @return 0x0100702 已经装有memcached
	 * 		   0x0100703:没有装memcached，可以进行安装
	 * @throws Exception 
	 */
//	public String checkMemcached(String fileName) throws Exception{
//		String code = "";
//				
//		//检测memcached是否安装成功
//		Process process = Runtime.getRuntime ().exec ("cmd");
//        SequenceInputStream sis = new SequenceInputStream (process.getInputStream (), process.getErrorStream ());
//        InputStreamReader isr = new InputStreamReader (sis, "gbk");
//        BufferedReader br = new BufferedReader (isr);
//        // next command
//        OutputStreamWriter osw = new OutputStreamWriter (process.getOutputStream ());
//        BufferedWriter bw = new BufferedWriter (osw);
//        bw.write ("C:\\softsource\\" + fileName + " -d install");
//        bw.newLine();//不能删掉
//        bw.flush ();
//        bw.close ();
//        osw.close ();
//        // read
//        String line = null;
//        String tem = "";
//        while (null != ( line = br.readLine () ))
//        {
//            tem += line;
//        }
//        process.destroy ();
//        br.close ();
//        isr.close ();
//		
//		System.out.println(tem);
//		if(tem.contains("already installed")){
//			code = "0x0100702";//已经装有memcached
//			System.out.println("已经装有memcached");
//		}else{
//			code = "0x0100703";//没有装memcached，可以进行安装
//			System.out.println("没有装memcached，可以进行安装");
//		}
//		return code;
//	}
	//执行脚本
	public static String executeVMScript(String strCmdPath) throws InterruptedException {
		Process process = null;
		try {
			 process = Runtime.getRuntime().exec(strCmdPath);
			 BufferedReader strCon = new BufferedReader(new
			 InputStreamReader(process.getInputStream()));
			 System.out.println(strCon.readLine());
			 String line = null;
			 while ((line = strCon.readLine()) != null) {
				 if(line.toLowerCase().contains("memcached"))
				 {
					 return "OK";
				 }
			 }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "NOK";
	}
	
	public static String executeVMScriptMysql(String strCmdPath) throws InterruptedException {
		Process process = null;
		try {
			 process = Runtime.getRuntime().exec(strCmdPath);
			 BufferedReader strCon = new BufferedReader(new
			 InputStreamReader(process.getInputStream()));
			// System.out.println(strCon.readLine());
			 String line = null;
			 while ((line = strCon.readLine()) != null) {
				 System.out.println(line);
				 return line;
			 }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		//checkMySQL();
	//	checkMemcached("memcached64.exe");
	}

}
