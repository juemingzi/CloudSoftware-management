package edu.xidian.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Administrator
 *获取基础环境配置参数
 */
public class SystemConfigurations {
	
	public static void main(String[] args){
		SystemConfigurations sc = new SystemConfigurations();
		System.out.println(sc.getSystemServiceState());
//		System.out.println(sc.getSystemSecRule());
//		System.out.println(sc.getSystemIP());
	}
	
	/**
	 * 获取系统所有服务,文件输出到sysServiceState.txt
	 */
	public String getSystemServiceState(){
		excuteCommand("cmd /c sc query state= all>D:\\sysServiceState.txt");

		String s = readFileByLines("D:\\sysServiceState.txt");
		return s.substring(4);
	}
	
	/**
	 * 获取系统安全规则,文件输出到sysSecurity.txt
	 */
	public String getSystemSecRule(){
		excuteCommand("cmd /c netsh ipsec static show all>D:\\sysSecurity.txt");
		String s = readFileByLines("D:\\sysSecurity.txt");
		return s.substring(4);
	}
	/**
	 * 获取系统IP,文件输出到sysIP.txt
	 */
	public String getSystemIP(){
		excuteCommand("cmd /c ipconfig /all>D:\\sysIP.txt");
		String s = readFileByLines("D:\\sysIP.txt");
		return s.substring(4);
	}
	
	/**
	 * 执行windows命令
	 * @param command
	 */
	public static boolean  excuteCommand(String command)
	{
	    Runtime r = Runtime.getRuntime();
	    Process p = null;
        try {
            p = r.exec(command);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String inline;
            while ((inline = br.readLine()) != null) {
                System.out.println(inline);
            }
            br.close();
            int res = p.waitFor();
            if(res==0){
            	return true;
            }
            p.destroy();
            p=null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return false;
	}
	
	 /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public static String readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        String returnStr = null;
        try {
//            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
//                System.out.println("line " + line + ": " + tempString);
                line++;
                returnStr += tempString+"\n"; 
            }
            reader.close();
            return returnStr;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return returnStr;
    }	
}

