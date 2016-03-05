package edu.xidian.message;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Properties;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;


public class WRFile {
//	private final  static String filePath = "C:/key.txt";
////	private final  static String filePath = "/home/key.txt";
	private static String filePath = null;

	/*
	 * 获取系统类型
	 */
	private static int os = -1;

	public WRFile(){
		Properties prop = System.getProperties();
		String osName = prop.getProperty("os.name");
		if (osName.toLowerCase().contains("windows")) {
			os = 0;
			filePath = "C:/key.txt";
		} else {
			os = 1;
			filePath = "/home/key.txt";
		}
	}
	
	/**
	 * @author wzy
	 * @param filePath 文件路径
	 * @param hostIP AgentIP
	 */
	public static String readKey(String filePath){
		String password = null;//明文秘钥
		File file = new File(filePath);
		BufferedReader reader = null; 
		try {
			System.out.println("以行为单位读取文件内容，一次读一整行：");
			InputStreamReader isr = new InputStreamReader(new FileInputStream(file),"UTF-8");
			reader = new BufferedReader(isr);
			String tempString = null;
			int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
				System.out.println("line " + line + ": " + tempString);
				//用base64解密
				try {
					password  = new String(new BASE64Decoder().decodeBuffer(tempString));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				line++;
			}
			reader.close();
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
		
		return password;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//writeKey("127.0.0.1", "12345678");
		String password = "12345678";
		String pwd;
		pwd = new BASE64Encoder().encode(password.getBytes());
		System.out.println(pwd);
		System.out.println(readKey(filePath));
	}

}

