package edu.xidian.linux;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class LVMScriptExecute {
	/**
	 * @author repace 
	 * linux��ִ�нű��ķ��� ��������Ǵ�ִ�еĽű�·��,
	 *         ����ֵ���ַ������飬��һ��Ԫ�ش���Ǳ��룬�ڶ��������ִ�нű�ʱ����̨�������Ϣ
	 * @throws InterruptedException 
	 */
	public String[] executeVMScript(String strCmdPath) {
		String[] result=new String[2];//���ؽ�����ַ���
		result[0]="";
		result[1]="";
		try {
			Process process = Runtime.getRuntime().exec("chmod 777 " + strCmdPath);// �޸��ļ�Ȩ�޳ɿ�ִ�е�
			try {
				process.waitFor();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			process = Runtime.getRuntime().exec(strCmdPath);
			BufferedReader strCon = new BufferedReader(new InputStreamReader(
					process.getInputStream())); // �ڿ���̨������
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
			result[1]="Found IOException";//���ַǿ�ִ��������쳣
			return result;
		}
	}
}

