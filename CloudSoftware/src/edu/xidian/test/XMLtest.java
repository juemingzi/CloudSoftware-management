package edu.xidian.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class XMLtest {
	public static void main(String[] args) {
		SAXReader reader = new SAXReader();
		File file = new File("D:/log.xml");
		String s=new String("1");
		Date date=new Date(System.currentTimeMillis());
		String d=date.toString();
		Document doc;
		try {
			doc = reader.read(file);
			//Element log=(Element)doc.selectSingleNode("//log[@userID='1'][@time='20141122'][@command='changePasswd']");
			String xpath=new String("//log[@userID='");
			xpath=xpath.concat(s);
			xpath=xpath.concat("'][@time='");
			xpath=xpath.concat(d);
			xpath=xpath.concat("'][@command='changePasswd']");
			System.out.println(xpath);
			Element log=(Element) doc.selectSingleNode(xpath);
			System.out.println(log.attributeValue("commandStatus"));
//			Element logs=(Element)doc.getRootElement(); 
			
//			Element author1 = logs.addElement("log")
//					.addAttribute("userID", "1")
//					.addAttribute("time", "20141122")
//					.addAttribute("command", "changePasswd")
//					.addAttribute("commandStatus", "success");
//			XMLWriter writer = new XMLWriter(new FileWriter("D:/log.xml"));
//			writer.write(doc);
//			writer.close();
//
//			// Pretty print the document to System.out
//			// 设置了打印的格式,将读出到控制台的格式进行美化
//			OutputFormat format = OutputFormat.createPrettyPrint();
//
//			writer = new XMLWriter(System.out, format);
//
//			writer.write(doc);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//			catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}
