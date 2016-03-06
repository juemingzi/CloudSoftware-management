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
	 * show  ���mysql�Ƿ�װ
	 * @return 0x0100201 mysqlû�а�װ������ע���
	 * 		   0x0100202:ϵͳװ��mysql
	 * 		   0x0100203 ע������ڣ����ڿ���װmysql
	 * @throws Exception 
	 */
	public static String checkMySQL() throws Exception{
		System.out.println("==========mysql��װǰ��⿪ʼ==========");
		String filePath = "C:\\preinstall\\MySQL\\";//���mysql�Ƿ�װ�������ļ����λ��

		//���ű���ŵ�Ŀ¼
		File filepath = new File(filePath);
		if (!filepath.isDirectory()) {
			filepath.mkdir();
		}
		
		
		File regedit = new File(filePath + "regedit.bat ");
		regedit.createNewFile();
		
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("@echo off");
		strBuffer.append(System.getProperty("line.separator"));
		strBuffer.append("reg query HKEY_LOCAL_MACHINE\\SYSTEM\\ControlSet001\\services\\eventlog\\Application\\MySQL /v EventMessageFile>nul 2>nul&&echo ����MySQL||echo ������MySQL");
		strBuffer.append(System.getProperty("line.separator"));
		strBuffer.append("pause>nul");
		strBuffer.append(System.getProperty("line.separator"));
		PrintWriter printWriter = new PrintWriter(filePath + "regedit.bat");
		printWriter.write(strBuffer.toString().toCharArray());
		printWriter.flush();
		printWriter.close();
		
		
		String mySQLPath = filePath + "regedit.bat";//���mysql�Ƿ�װ�������ļ�·��
		System.out.println(mySQLPath);
		String s1 = executeVMScriptMysql(mySQLPath);//ִ�нű����쿴mysql��ע����Ƿ����
		
		System.out.println("�����................."+s1);
		
		String flag ="";
		if(s1.equals("����MySQL")){//�ж��Ǵ���mysql���Ǵ���ע�������������������ɰ�װmysql
			System.out.println("MySQL��ע������");				
			//���mysql�Ƿ�װ�ɹ�
			 Process process = Runtime.getRuntime ().exec ("cmd");
	         SequenceInputStream sis = new SequenceInputStream (process.getInputStream (), process.getErrorStream ());
	         InputStreamReader isr = new InputStreamReader (sis, "gbk");
	         BufferedReader br = new BufferedReader (isr);
	         // next command
	         OutputStreamWriter osw = new OutputStreamWriter (process.getOutputStream ());
	         BufferedWriter bw = new BufferedWriter (osw);
	         bw.write ("net start mysql");
	         bw.newLine();//����ɾ��
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
			if(tem.contains("�����ڲ����ⲿ����")){
				flag ="0x0100201";//mysqlû�а�װ������ע���
				System.out.println("mysqlû��ж�ظɾ�");
			}else{
				flag = "0x0100202";//��װ��mysql
				System.out.println("ϵͳװ��mysql");
			}
		}else{
			flag = "0x0100203";//ϵͳ����װmysql
			System.out.println("ע������ڣ����ڿ���װmysql");
		}
		System.out.println("==========mysql��װǰ������=========="+flag);
		return flag;
	}
	
	/**
	 * @author wzy
	 * @param fileName Ҫ��װ��memcached������
	 * show ���memcached�Ƿ�װ
	 * @return 0x0100702 �Ѿ�װ��memcached
	 * 		   0x0100703:û��װmemcached�����Խ��а�װ
	 * @throws Exception 
	 */
//	public String checkMemcached(String fileName) throws Exception{
//		String code = "";
//				
//		//���memcached�Ƿ�װ�ɹ�
//		Process process = Runtime.getRuntime ().exec ("cmd");
//        SequenceInputStream sis = new SequenceInputStream (process.getInputStream (), process.getErrorStream ());
//        InputStreamReader isr = new InputStreamReader (sis, "gbk");
//        BufferedReader br = new BufferedReader (isr);
//        // next command
//        OutputStreamWriter osw = new OutputStreamWriter (process.getOutputStream ());
//        BufferedWriter bw = new BufferedWriter (osw);
//        bw.write ("C:\\softsource\\" + fileName + " -d install");
//        bw.newLine();//����ɾ��
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
//			code = "0x0100702";//�Ѿ�װ��memcached
//			System.out.println("�Ѿ�װ��memcached");
//		}else{
//			code = "0x0100703";//û��װmemcached�����Խ��а�װ
//			System.out.println("û��װmemcached�����Խ��а�װ");
//		}
//		return code;
//	}
	//ִ�нű�
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
