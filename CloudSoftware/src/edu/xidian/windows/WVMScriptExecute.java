package edu.xidian.windows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;




/**
 * @author repace
 * windows��ִ�нű��ķ���������Ĳ����Ǵ�ִ�еĽű�·��
 *  ����ֵ���ַ������飬��һ��Ԫ�ش���Ǳ��룬�ڶ��������ִ�нű�ʱ����̨�������Ϣ
 */



public class WVMScriptExecute {

	public  String[] executeVMScript(String strCmd) {
		 Process process = null;
		 String[] result=new String[2];//���ؽ�����ַ���
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
			result[1]="Found IOException"; //���ַǿ�ִ��������쳣
			return result;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result[0]="0x0500002";
			result[1]="InterruptedException";  //�������InterruptedException�쳣
			return result;
		}
	}
}


