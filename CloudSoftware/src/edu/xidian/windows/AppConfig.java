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
	 * @author wzy fileName 为传入的文件路径，windows如：C:\\Program Files
	 *         (x86)\\MySQL\\MySQL Server 5.5\\my.ini； linux：/etc/my.cnf
	 *         这个方法为以行的形式显示整个文件
	 */
	public static String readFile(String fileName) {
		File file = new File(fileName);
		BufferedReader reader = null; 
		InputStreamReader isr = null;
		String temp = "";
		try {
			System.out.println("以行为单位读取文件内容，一次读一整行：");
			isr = new InputStreamReader(new FileInputStream(file),"UTF-8");
			reader = new BufferedReader(isr);
			String tempString = null;
			int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
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
	 * @author wzy fileName为输入的文件路径，keyword为搜索的关键字； 对每一行进行匹配，如果包含这个关键字，就将这一行输出
	 */
	public static String readFileByKey(String fileName, String keyword) {
//		System.out.println("keyword "+keyword);
		File file = new File(fileName);
		BufferedReader reader = null;
		InputStreamReader isr = null;
		String temp = "";
		try {
			System.out.println("以行为单位读取文件内容，一次读一整行：");
			 isr = new InputStreamReader(new FileInputStream(file),"UTF-8");
			reader = new BufferedReader(isr);
			String tempString = null;
			int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 判断是否包含关键字，如果包含并且不是注释文件则输出
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
	 * show 用来判断要写入的配置信息是否在配置文件中已经存在
	 * @return true表示已经存在，false表示不存在
	 * 判断这个配置文件中是否已经存在了这条配置信息，如果存在返回true，如果不存在，返回false
	 */
	public static boolean judgeExist(String fileName, String keyword) {
		File file = new File(fileName);
		BufferedReader reader = null;
		InputStreamReader isr = null;
		try {
			System.out.println("以行为单位读取文件内容，一次读一整行：");
			isr = new InputStreamReader(new FileInputStream(file),"UTF-8");
			reader = new BufferedReader(isr);
			String tempString = null;
			int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 判断是否包含关键字，如果包含并且不是注释的记录得到，然后进行替换
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
	 * 往配置文件后面追加新的设置记录 fileName是文件的路径，content是要往文件后面追加的内容
	 * flag为判断文件是否写入成功，写入失败会进入异常，返回false
	 */
	public static boolean appendToFile(String fileName, String content) {
		int flag = 1;//用来判断文件是否写入成功
		FileWriter writer = null;
		try {
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件			
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
	 * @author wzy 在配置文件中间插入一条配置信息 fileName
	 *         为文件路径，keyword是配置文件中的一个关键字，content是在关键字下一行插入的一条配置信息
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
			System.out.println("以行为单位读取文件内容，一次读一整行：");
			isr = new InputStreamReader(new FileInputStream(file),"UTF-8");
			reader = new BufferedReader(isr);
			String tempString = null;
			int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 判断是否包含关键字，如果包含并且不是注释的记录得到，然后进行替换
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
		// 替换后输出的文件位置
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
	 * @author wzy 在配置文件替换原有的配置信息 fileName
	 *         为文件路径，keyword是配置文件中的一个关键字，content为替换的配置信息
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
			System.out.println("以行为单位读取文件内容，一次读一整行：");
			isr = new InputStreamReader(new FileInputStream(file),"UTF-8");
			reader = new BufferedReader(isr);
			String tempString = null;
			int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 判断是否包含关键字，将包含并且不是注释的记录，进行替换
				if (tempString.contains(keyword) && !tempString.trim().startsWith("#")) {
					System.out.println("__________________find keyword:" + keyword);
					//System.out.println("line " + line + ": " + tempString);
					exit = true;
					tempString = tempString.replace(tempString, content);
				}
				// 将读取到的所有的记录写到缓冲区中，最后在写到源文件中即可
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
		// 替换后输出的文件位置
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

