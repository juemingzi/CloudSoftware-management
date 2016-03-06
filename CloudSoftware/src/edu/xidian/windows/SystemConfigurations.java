package edu.xidian.windows;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Administrator
 *��ȡ�����������ò���
 */
public class SystemConfigurations {
	
	public static void main(String[] args){
		SystemConfigurations sc = new SystemConfigurations();
//		System.out.println(sc.getSystemServiceState());
//		System.out.println(sc.getSystemSecRule());
//		System.out.println("��ȡ�õ���IPΪ��    "+sc.getSystemIP());
//		System.out.println("��ȡ�õ���affiIPΪ��    "+sc.getSystemaffiIP());
	}
	
	/**
	 * ��ȡϵͳ���з���,�ļ������sysServiceState.txt
	 */
	public String getSystemServiceState(){
		excuteCommand("cmd /c sc query state= all>C:\\sysServiceState.txt");

		String s = readFileByLines("C:\\sysServiceState.txt");
		return s.substring(4);
	}
	
	/**
	 * ��ȡϵͳ��ȫ����,�ļ������sysSecurity.txt
	 */
	public String getSystemSecRule(){
		excuteCommand("cmd /c netsh ipsec static show all>C:\\sysSecurity.txt");
		String s = readFileByLines("C:\\sysSecurity.txt");
		return s.substring(4);
	}
	/**
	 * ��ȡϵͳIP,�ļ������sysIP.txt
	 */
	public String getSystemIP(){
		boolean flag = true;
		excuteCommand("cmd /c ipconfig /all>C:\\sysIP.txt");
		String s = readFileByLines("C:\\sysIP.txt","IPv4",flag);
		return s.substring(4);
	}
	/**
	 * ��ȡϵͳ����IP,�ļ������sysIP.txt
	 */
	public String getSystemaffiIP(){
		boolean flag = false;
		excuteCommand("cmd /c ipconfig /all>C:\\sysIP.txt");
		String s = readFileByLines("C:\\sysIP.txt","IPv4",flag);
		return s;
	}
	
	/**
	 * ִ��windows����
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
     * ����Ϊ��λ��ȡ�ļ��������ڶ������еĸ�ʽ���ļ�
     */
    public static String readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        String returnStr = null;
        try {
            System.out.println("����Ϊ��λ��ȡ�ļ����ݣ�һ�ζ�һ���У�");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // һ�ζ���һ�У�ֱ������nullΪ�ļ�����
            while ((tempString = reader.readLine()) != null) {
                // ��ʾ�к�
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
            System.out.println("����Ϊ��λ��ȡ�ļ����ݣ�һ�ζ�һ���У�");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null,IPStr = null;
            String affiIP = "";
            int line = 1,num = 0;
            boolean flag = false;
            // һ�ζ���һ�У�ֱ������nullΪ�ļ�����
            while ((tempString = reader.readLine()) != null) {
                // ��ʾ�к�
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
            return affiIP.replace("(��ѡ)", "");
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
