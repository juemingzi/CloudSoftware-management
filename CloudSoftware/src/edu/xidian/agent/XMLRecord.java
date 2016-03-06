package edu.xidian.agent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * @ClassName: XMLRecord
 * @Description: 将命令执行状态记录到XML文件
 * @author: wangyannan
 * @date: 2014-11-24 下午9:47:19
 */
public class XMLRecord {
	/*
	 * 获取系统类型
	 */
	private static int os = -1;
	static{
		Properties prop = System.getProperties();
		String osName = prop.getProperty("os.name");
		if (osName.toLowerCase().contains("windows")) {
			os = 0;
		} else {
			os = 1;
		}
	}
	/**
	 * @Title: write
	 * @Description: 写入XML文件的方法
	 * @param userID
	 * @param time
	 * @param command
	 * @param status
	 * @return: boolean
	 */
	public static String write(String opID, String time, String command,
			String status) {
		SAXReader reader = new SAXReader();
		File file = null;
		if(os == 0){
			file = new File("C:/log.xml");
		}else if(os == 1){
			file = new File("/home/log.xml");
		}
		
//		
		Document doc;
		try {
			doc = reader.read(file);
			Element logs = (Element) doc.getRootElement();
			Element log = logs.addElement("log").addAttribute("opID", opID)
					.addAttribute("time", time)
					.addAttribute("command", command)
					.addAttribute("status", status);
			
			XMLWriter writer = null;
			
			
			if(os == 0){
				writer = new XMLWriter(new FileWriter("C:/log.xml"));
			}else if(os == 1){
				writer = new XMLWriter(new FileWriter("/home/log.xml"));
			}
//		XMLWriter writer = new XMLWriter(new FileWriter("C:/log.xml"));
////		XMLWriter writer = new XMLWriter(new FileWriter("/home/log.xml"));
			writer.write(doc);
			writer.close();
			return "success";
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "failed";
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "failed";
		}
		
		
	}
}
