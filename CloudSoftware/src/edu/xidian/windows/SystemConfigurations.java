package edu.xidian.windows;

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
//		System.out.println(sc.getSystemServiceState());
//		System.out.println(sc.getSystemSecRule());
//		System.out.println("获取得到的IP为：    "+sc.getSystemIP());
//		System.out.println("获取得到的affiIP为：    "+sc.getSystemaffiIP());
	}
	
	/**
	 * 获取系统所有服务,文件输出到sysServiceState.txt
	 */
	public String getSystemServiceState(){
		excuteCommand("cmd /c sc query state= all>C:\\sysServiceState.txt");

		String s = readFileByLines("C:\\sysServiceState.txt");
		return s.substring(4);
	}
	
	/**
	 * 获取系统安全规则,文件输出到sysSecurity.txt
	 */
	public String getSystemSecRule(){
		excuteCommand("cmd /c netsh ipsec static show all>C:\\sysSecurity.txt");
		String s = readFileByLines("C:\\sysSecurity.txt");
		return s.substring(4);
	}
	/**
	 * 获取系统IP,文件输出到sysIP.txt
	 */
	public String getSystemIP(){
		boolean flag = true;
		excuteCommand("cmd /c ipconfig /all>C:\\sysIP.txt");
		String s = readFileByLines("C:\\sysIP.txt","IPv4",flag);
		return s.substring(4);
	}
	/**
	 * 获取系统附属IP,文件输出到sysIP.txt
	 */
	public String getSystemaffiIP(){
		boolean flag = false;
		excuteCommand("cmd /c ipconfig /all>C:\\sysIP.txt");
		String s = readFileByLines("C:\\sysIP.txt","IPv4",flag);
		return s;
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
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                System.out.println("line " + line + ": " + tempString);
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
    public static String readFileByLines(String fileName,String s,boolean f) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null,IPStr = null;
            String affiIP = "";
            int line = 1,num = 0;
            boolean flag = false;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                System.out.println("line " + line + ": " + tempString);
                line++;
                if(tempString.contains(s)){
                	if(f){
                		String[] str = tempString.split(":");
                		IPStr = str[1];
                		System.out.println("The String is : "+str[1]);
                		return IPStr;
                	}else{
                		if(flag == false){
                			flag = true;
                			continue;
                		}else{
                			if(tempString.contains(s)){
                				String[] st = tempString.split(":");
                				affiIP += st[1]+"\n";
                			}
                		}
                	}	
                }
            }
            reader.close();
            //return affiIP.split("null")[0];
            return affiIP.replace("(首选)", "");
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
        return null;
    }	
}
