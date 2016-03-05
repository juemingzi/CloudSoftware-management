package edu.xidian.linux;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class LVMScriptExecute {
	/**
	 * @author repace 
	 * linux下执行脚本的方法 ：传入的是待执行的脚本路径,
	 *         返回值是字符串数组，第一个元素存的是编码，第二个存的是执行脚本时控制台输出的信息
	 * @throws InterruptedException 
	 */
	public String[] executeVMScript(String strCmdPath) {
		String[] result=new String[2];//返回结果的字符串
		result[0]="";
		result[1]="";
		try {
			Process process = Runtime.getRuntime().exec("chmod 777 " + strCmdPath);// 修改文件权限成可执行的
			try {
				process.waitFor();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			process = Runtime.getRuntime().exec(strCmdPath);
			BufferedReader strCon = new BufferedReader(new InputStreamReader(
					process.getInputStream())); // 在控制台输出结果
			 String line;
			 
			 while ((line = strCon.readLine()) != null) {
				 System.out.println(line);
			 result[1]=result[1]+line+"\n";
			 }
			 result[0]="0x0500000";
			return result;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result[0]="0x0500001";
			result[1]="Found IOException";//出现非可执行命令的异常
			return result;
		}
	}
}

