package edu.xidian.windows;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;



public class AppConfig {

	public AppConfig() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @author wzy fileName Ϊ������ļ�·����windows�磺C:\\Program Files
	 *         (x86)\\MySQL\\MySQL Server 5.5\\my.ini�� linux��/etc/my.cnf
	 *         �������Ϊ���е���ʽ��ʾ�����ļ�
	 */
	public static String readFile(String fileName) {
		File file = new File(fileName);
		BufferedReader reader = null; 
		InputStreamReader isr = null;
		String temp = "";
		try {
			System.out.println("����Ϊ��λ��ȡ�ļ����ݣ�һ�ζ�һ���У�");
			isr = new InputStreamReader(new FileInputStream(file),"UTF-8");
			reader = new BufferedReader(isr);
			String tempString = null;
			int line = 1;
			// һ�ζ���һ�У�ֱ������nullΪ�ļ�����
			while ((tempString = reader.readLine()) != null) {
				// ��ʾ�к�
				System.out.println("line " + line + ": " + tempString);
				temp = temp + tempString + ";";
				line++;
			}
			reader.close();
			isr.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (isr != null) {
				try {
					isr.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
		}
		return temp;
	}

	/**
	 * @author wzy fileNameΪ������ļ�·����keywordΪ�����Ĺؼ��֣� ��ÿһ�н���ƥ�䣬�����������ؼ��֣��ͽ���һ�����
	 */
	public static String readFileByKey(String fileName, String keyword) {
//		System.out.println("keyword "+keyword);
		File file = new File(fileName);
		BufferedReader reader = null;
		InputStreamReader isr = null;
		String temp = "";
		try {
			System.out.println("����Ϊ��λ��ȡ�ļ����ݣ�һ�ζ�һ���У�");
			 isr = new InputStreamReader(new FileInputStream(file),"UTF-8");
			reader = new BufferedReader(isr);
			String tempString = null;
			int line = 1;
			// һ�ζ���һ�У�ֱ������nullΪ�ļ�����
			while ((tempString = reader.readLine()) != null) {
				// �ж��Ƿ�����ؼ��֣�����������Ҳ���ע���ļ������
				if (tempString.contains(keyword) && !tempString.trim().startsWith("#")) {
					System.out.println("line " + line + ": " + tempString);
					temp = temp + tempString + ";";
				}
				line++;
			}
			reader.close();
			isr.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
			if (isr != null) {
				try {
					isr.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
		}
		return temp;
	}

	/**
	 * @author wzy
	 * show �����ж�Ҫд���������Ϣ�Ƿ��������ļ����Ѿ�����
	 * @return true��ʾ�Ѿ����ڣ�false��ʾ������
	 * �ж���������ļ����Ƿ��Ѿ�����������������Ϣ��������ڷ���true����������ڣ�����false
	 */
	public static boolean judgeExist(String fileName, String keyword) {
		File file = new File(fileName);
		BufferedReader reader = null;
		InputStreamReader isr = null;
		try {
			System.out.println("����Ϊ��λ��ȡ�ļ����ݣ�һ�ζ�һ���У�");
			isr = new InputStreamReader(new FileInputStream(file),"UTF-8");
			reader = new BufferedReader(isr);
			String tempString = null;
			int line = 1;
			// һ�ζ���һ�У�ֱ������nullΪ�ļ�����
			while ((tempString = reader.readLine()) != null) {
				// �ж��Ƿ�����ؼ��֣�����������Ҳ���ע�͵ļ�¼�õ���Ȼ������滻
				if (tempString.contains(keyword) && !tempString.trim().startsWith("#")) {
					System.out.println(tempString);
					return true;
				}
				line++;
			}
			reader.close();
			isr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		if (isr != null) {
			try {
				isr.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
		return false;
	}
	/**
	 * @author wzy 
	 * @version 1
	 * �������ļ�����׷���µ����ü�¼ fileName���ļ���·����content��Ҫ���ļ�����׷�ӵ�����
	 * flagΪ�ж��ļ��Ƿ�д��ɹ���д��ʧ�ܻ�����쳣������false
	 */
	public static boolean appendToFile(String fileName, String content) {
		int flag = 1;//�����ж��ļ��Ƿ�д��ɹ�
		FileWriter writer = null;
		try {
			// ��һ��д�ļ��������캯���еĵڶ�������true��ʾ��׷����ʽд�ļ�			
			writer = new FileWriter(fileName, true);
			writer.write(content);
			writer.append(System.getProperty("line.separator"));
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			flag = 0;
		}finally {
			if(null!=writer) {
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if(flag == 1){
			return true;
		}else {
			return false;
		}
	}

	/**
	 * @author wzy �������ļ��м����һ��������Ϣ fileName
	 *         Ϊ�ļ�·����keyword�������ļ��е�һ���ؼ��֣�content���ڹؼ�����һ�в����һ��������Ϣ
	 */
	public static boolean insertToFile(String fileName, String keyword,
			String content) {
		int flag = 1;
		File file = new File(fileName);
		BufferedReader reader = null;
		InputStreamReader isr = null;
		StringBuffer strBuffer = new StringBuffer();
		PrintWriter printWriter = null;
		try {
			System.out.println("����Ϊ��λ��ȡ�ļ����ݣ�һ�ζ�һ���У�");
			isr = new InputStreamReader(new FileInputStream(file),"UTF-8");
			reader = new BufferedReader(isr);
			String tempString = null;
			int line = 1;
			// һ�ζ���һ�У�ֱ������nullΪ�ļ�����
			while ((tempString = reader.readLine()) != null) {
				// �ж��Ƿ�����ؼ��֣�����������Ҳ���ע�͵ļ�¼�õ���Ȼ������滻
				if (tempString.contains(keyword) && !tempString.trim().startsWith("#")) {
					System.out.println("line " + line + ": " + tempString);
					tempString += System.getProperty("line.separator")
							+ content;
				}
				strBuffer.append(tempString);
				strBuffer.append(System.getProperty("line.separator"));
				line++;
			}
			reader.close();
			isr.close();
		} catch (IOException e) {
			e.printStackTrace();
			flag = 0;
		}finally {
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e1) {
				e1.printStackTrace();
				flag = 0;
			}
		}
	
		if (isr != null) {
			try {
				isr.close();
			} catch (IOException e1) {
				e1.printStackTrace();
				flag = 0;
			}
		}
		
		}
		// �滻��������ļ�λ��
		try {
			printWriter = new PrintWriter(fileName);
			printWriter.write(strBuffer.toString().toCharArray());
			printWriter.flush();
			printWriter.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			flag = 0;
		}finally {
			if(printWriter!=null) {
				printWriter.close();
			}
		}
		if(flag == 1){
			return true;
		}else {
			return false;
		}
	}

	/**
	 * @author wzy �������ļ��滻ԭ�е�������Ϣ fileName
	 *         Ϊ�ļ�·����keyword�������ļ��е�һ���ؼ��֣�contentΪ�滻��������Ϣ
	 */
	public static boolean replaceToFile(String fileName, String keyword,
			String content) {
		int flag = 1;
		boolean exit = false;
		File file = new File(fileName);
		BufferedReader reader = null;
		InputStreamReader isr = null;
		StringBuffer strBuffer = new StringBuffer();
		PrintWriter printWriter=null;
		try {
			System.out.println("����Ϊ��λ��ȡ�ļ����ݣ�һ�ζ�һ���У�");
			isr = new InputStreamReader(new FileInputStream(file),"UTF-8");
			reader = new BufferedReader(isr);
			String tempString = null;
			int line = 1;
			// һ�ζ���һ�У�ֱ������nullΪ�ļ�����
			while ((tempString = reader.readLine()) != null) {
				// �ж��Ƿ�����ؼ��֣����������Ҳ���ע�͵ļ�¼�������滻
				if (tempString.contains(keyword) && !tempString.trim().startsWith("#")) {
					System.out.println("__________________find keyword:" + keyword);
					//System.out.println("line " + line + ": " + tempString);
					exit = true;
					tempString = tempString.replace(tempString, content);
				}
				// ����ȡ�������еļ�¼д���������У������д��Դ�ļ��м���
				strBuffer.append(tempString);
				strBuffer.append(System.getProperty("line.separator"));
				line++;
			}
			reader.close();
			isr.close();
		} catch (IOException e) {
			e.printStackTrace();
			flag = 0;
		}finally{
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e1) {
				e1.printStackTrace();
				flag = 0 ;
			}
		}
		if (isr != null) {
			try {
				isr.close();
			} catch (IOException e1) {
				e1.printStackTrace();
				flag = 0 ;
			}
		}
		}
		// �滻��������ļ�λ��
		try {
			printWriter = new PrintWriter(fileName);
			printWriter.write(strBuffer.toString().toCharArray());
			printWriter.flush();
			printWriter.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			flag = 0;
		}finally{
			if (printWriter != null) {
			
				printWriter.close();
			
		}
		}
	
		if(flag == 1 && exit == true){
			return true;
		}else {
			return false;
		}
	}

	

}

