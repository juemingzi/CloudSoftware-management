package edu.xidian.windows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;




/**
 * @author repace
 * windows下执行脚本的方法：传入的参数是待执行的脚本路径
 *  返回值是字符串数组，第一个元素存的是编码，第二个存的是执行脚本时控制台输出的信息
 */



public class WVMScriptExecute {

	public  String[] executeVMScript(String strCmd) {
		 Process process = null;
		 String[] result=new String[2];//返回结果的字符串
		 result[0]="";
		 result[1]="";
		try {
			 process = Runtime.getRuntime().exec(strCmd);
			 BufferedReader strCon = new BufferedReader(new
			 InputStreamReader(process.getInputStream()));
			 String line;			
			 
			 while ((line = strCon.readLine()) != null) {
			 result[1]=result[1]+line+"\n";
			 System.out.println(line);
			 }
			process.waitFor();
			result[0]="0x0500000";
			return result;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result[0]="0x0500001";
			result[1]="Found IOException"; //出现非可执行命令的异常
			return result;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result[0]="0x0500002";
			result[1]="InterruptedException";  //出现命令被InterruptedException异常
			return result;
		}
	}
}


