package edu.xidian.linux;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class PreInstall {

	/**
	 * show mysql is equiped or not
	 * @author wzy
	 * @return if temp is "",there is no mysql,else there is mysql already
	 */
public static String checkMysql() throws Exception{
		
		String dir = "/home/preinstall/preMySQL";
		String prefile = "/home/preinstall/preMySQL/preMySQL.sh";
		
		File filepath = new File(dir);
		if (!filepath.isDirectory()) {
			filepath.mkdirs();
		}
		
		File batfile = new File(prefile);
		batfile.createNewFile();
		
		Process process = null;
		process = Runtime.getRuntime().exec("chmod 777 /home/preinstall -R");
		
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("rpm -qa |grep -i mysql");
		strBuffer.append(System.getProperty("line.separator"));

		PrintWriter printWriter = new PrintWriter(prefile);
		printWriter.write(strBuffer.toString().toCharArray());
		printWriter.flush();
		printWriter.close();
		
		
		process = Runtime.getRuntime().exec("/home/preinstall/preMySQL/preMySQL.sh");
		BufferedReader strCon = new BufferedReader(new InputStreamReader(
				process.getInputStream())); 
		String line ;
		String temp = "";
		while ((line = strCon.readLine()) != null) {
			temp =temp +line + ";";
			//System.out.println(temp);
		}
		process.waitFor();
		return temp;
	}
	
	/**
	 * @author wzy
	 * @return the number of the whether mysql exist or not
	 * @throws Exception 
	 */
	/*public static String IsMySQL() throws Exception{
		String s = checkMysql();
		System.out.println(s);
		String flag = null;
		if(s==""){
			System.out.println("there is no mysql");
			flag =  "00";
		}else {
			System.out.println("mysql is equiped");
			flag = "01";
		}
		return flag;
	}*/
	
	/**
	 * @author wzy
	 * show check whether libevent is equipted
	 * return 0x0100701 libevent is equipted
	 * 		  0x0100702 libevent isn't equipted
	 */
public static boolean checkMemcached() throws Exception{
	
	boolean flag;
	String libeventPath = "/home/";
	String dir = "/home/preinstall/preMemcached";
	String prefile = "/home/preinstall/preMemcached/preMemcached.sh";
	
	File filepath = new File(dir);
	if (!filepath.isDirectory()) {
		filepath.mkdir();
	}
	
	File batfile = new File(prefile);
		batfile.createNewFile();
	
	Process process = null;
	process = Runtime.getRuntime().exec("chmod 777 /home/preinstall -R");
	
	StringBuffer strBuffer = new StringBuffer();
	strBuffer.append("ls -al /home/ |grep libevent");
	strBuffer.append(System.getProperty("line.separator"));

	PrintWriter printWriter = new PrintWriter(prefile);
	printWriter.write(strBuffer.toString().toCharArray());
	printWriter.flush();
	printWriter.close();
	
	process = Runtime.getRuntime().exec(prefile);
	BufferedReader strCon = new BufferedReader(new InputStreamReader(
			process.getInputStream())); 
	String line ;
	String temp = "";
	while ((line = strCon.readLine()) != null) {
		temp =temp +line + ";";
		System.out.println(temp);
	}
	process.waitFor();
	if(temp.equals("")){
		
		flag = false;
		System.out.println("there is no libevent!!!");
	}else{
		
		flag = true;
		System.out.println("there is already libevent");
	}
	return flag;
}

	
	
	public static String isMySQLExist() throws Exception{
		String dir = "/home/preinstall/preMySQL";
		String isfile = "/home/preinstall/preMySQL/isMySQL.sh";
		
		File filepath = new File(dir);
		if (!filepath.isDirectory()) {
			filepath.mkdir();
		}
		
		File batfile = new File(isfile);
		batfile.createNewFile();
		
		Process process = null;
		process = Runtime.getRuntime().exec("chmod 777 /home/preinstall -R");
		
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("service mysql status");
		strBuffer.append(System.getProperty("line.separator"));

		PrintWriter printWriter = new PrintWriter(isfile);
		printWriter.write(strBuffer.toString().toCharArray());
		printWriter.flush();
		printWriter.close();
		
		
		process = Runtime.getRuntime().exec("/home/preinstall/preMySQL/isMySQL.sh");
		BufferedReader strCon = new BufferedReader(new InputStreamReader(
				process.getInputStream())); 
		String line ;
		String temp = "";
		while ((line = strCon.readLine()) != null) {
			temp =temp +line + ";";
			System.out.println(temp);
		}
		System.out.println(temp+"-----------");
		process.waitFor();
		return temp;
	}
	
	/**
	 * @author wzy
	 * show memcached is installed or uninstalled success 
	 * @throws Exception 
	 */
	public static String isMemcachedSuccess(String path) throws Exception{
		String dir = "/home/preinstall/preMemcached";
		String isfile = "/home/preinstall/preMemcached/isMemcached.sh";
		
		File filepath = new File(dir);
		if (!filepath.isDirectory()) {
			filepath.mkdir();
		}
		
		File batfile = new File(isfile);
		batfile.createNewFile();
		
		Process process = null;
		process = Runtime.getRuntime().exec("chmod 777 /home/preinstall -R");
		
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("ls -al "+ path + "/mem*");
		strBuffer.append(System.getProperty("line.separator"));

		PrintWriter printWriter = new PrintWriter(isfile);
		printWriter.write(strBuffer.toString().toCharArray());
		printWriter.flush();
		printWriter.close();
		
		
		process = Runtime.getRuntime().exec("/home/preinstall/preMemcached/isMemcached.sh");
		BufferedReader strCon = new BufferedReader(new InputStreamReader(
				process.getInputStream())); 
		String line ;
		String temp = "";
		while ((line = strCon.readLine()) != null) {
			temp =temp +line + ";";
			System.out.println(temp);
		}
		System.out.println(temp+"-----------");
		process.waitFor();
		return temp;
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		checkMemcached();
	}

}
