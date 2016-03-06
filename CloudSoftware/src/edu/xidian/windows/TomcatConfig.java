package edu.xidian.windows;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class TomcatConfig {

	/**
	 * @author XQ
	 * 
	 * @param path
	 * @param keyword
	 * @param value
	 */
	public static boolean updateXML(String path, String keyword, String value) {
		SAXReader saxReader = new SAXReader();
		Document doc = null;
		try {
			doc = saxReader.read(new File(path));
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		if (keyword.equalsIgnoreCase("Port")
				|| keyword.equalsIgnoreCase("URIEncoding")
				|| keyword.equalsIgnoreCase("maxProcessors")) {

			Element updateElement = null;
			for (Iterator iter = doc.getRootElement().element("Service")
					.elementIterator("Connector"); iter.hasNext();) {
				Element element = (Element) iter.next();
				for (Iterator iterator = element.attributeIterator(); iterator
						.hasNext();) {
					Attribute attr = (Attribute) iterator.next();
					if (attr.getName().equalsIgnoreCase("protocol")
							&& attr.getValue().equalsIgnoreCase("HTTP/1.1")) {
						updateElement = element;
						break;
					}
				}
			}

			boolean flag = true;
			if (updateElement != null) {
				for (Iterator iterator = updateElement.attributeIterator(); iterator
						.hasNext();) {
					Attribute attr = (Attribute) iterator.next();
					if (attr.getName().equalsIgnoreCase(keyword)) {
						attr.setValue(value);
						flag = false;
					}
				}

				if (flag) {
					updateElement.addAttribute(keyword, value);
				}
			}

		} else if (keyword.equalsIgnoreCase("Host")) {
			String[] valueList = value.split(",");
			Element engineElement = doc.getRootElement().element("Service")
					.element("Engine");
			Element hostElement = engineElement.addElement("Host");
			hostElement.addAttribute("name", valueList[0]);
			Element contElement = hostElement.addElement("Context");
			contElement.addAttribute("docBase", valueList[1]);

		} else if (keyword.equalsIgnoreCase("Logger")) {
			String[] valueList = value.split(",");
			for (Iterator iter = doc.getRootElement().element("Service")
					.elementIterator("Engine"); iter.hasNext();) {
				Element element = (Element) iter.next();
				if (element.element("Logger") != null) {
					for (Iterator iterator = element.element("Logger").attributeIterator(); iterator.hasNext();) {
						Attribute attr = (Attribute) iterator.next();
						if (attr.getName().equalsIgnoreCase("className")) {
							attr.setValue(valueList[0]);
						} else if (attr.getName().equalsIgnoreCase("prefix")) {
							attr.setValue(valueList[1]);
						} else if (attr.getName().equalsIgnoreCase("suffix")) {
							attr.setValue(valueList[2]);
						} else if (attr.getName().equalsIgnoreCase("timestamp")) {
							attr.setValue(valueList[3]);
						}
					}
				} else {
					Element logElement = element.addElement("Logger");
					logElement.addAttribute("className", valueList[0]);
					logElement.addAttribute("prefix", valueList[1]);
					logElement.addAttribute("suffix", valueList[2]);
					logElement.addAttribute("timestamp", valueList[3]);
				}

			}

		}
		try {
			OutputFormat format = new OutputFormat("    ", true);
			format.setEncoding("gb2312");
			XMLWriter xmlWriter = new XMLWriter(new PrintWriter(new File(path)), format);
			xmlWriter.write(doc);
			xmlWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}
	
	public static String getXML(String path, String keyword) {
		SAXReader saxReader = new SAXReader();
		Document doc = null;
		try {
			doc = saxReader.read(new File(path));
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		if (keyword.equalsIgnoreCase("Port")
				|| keyword.equalsIgnoreCase("URIEncoding")
				|| keyword.equalsIgnoreCase("maxProcessors")) {
			Element updateElement = null;
			for (Iterator iter = doc.getRootElement().element("Service")
					.elementIterator("Connector"); iter.hasNext();) {
				Element element = (Element) iter.next();
				for (Iterator iterator = element.attributeIterator(); iterator
						.hasNext();) {
					Attribute attr = (Attribute) iterator.next();
					if (attr.getName().equalsIgnoreCase("protocol")
							&& attr.getValue().equalsIgnoreCase("HTTP/1.1")) {
						updateElement = element;
						break;
					}
				}
			}
			if (updateElement != null) {
				for (Iterator iterator = updateElement.attributeIterator(); iterator
						.hasNext();) {
					Attribute attr = (Attribute) iterator.next();
					if (attr.getName().equalsIgnoreCase(keyword)) {
						System.out.println(attr.getValue().toString());
						return attr.getValue().toString();
					}
				}
	
			}

		} else if (keyword.equalsIgnoreCase("Host")) {
			Element hostElement = doc.getRootElement().element("Service").element("Engine").element("Host");
			StringBuilder temp = new StringBuilder("");
			for (Iterator iterator = hostElement.attributeIterator(); iterator.hasNext();) {
				Attribute attr = (Attribute) iterator.next();
				System.out.println(attr.getName()+":"+attr.getValue()+" ");
				temp.append(attr.getName()+":"+attr.getValue()+" ");
			}
			return temp.toString();

		} else if (keyword.equalsIgnoreCase("Logger")) {
			for (Iterator iter = doc.getRootElement().element("Service").elementIterator("Engine"); iter.hasNext();) {
				Element element = (Element) iter.next();
				if (element.element("Logger") != null) {
					StringBuilder temp = new StringBuilder("");
					for (Iterator iterator = element.element("Logger").attributeIterator(); iterator.hasNext();) {
						Attribute attr = (Attribute) iterator.next();
						System.out.println(attr.getName()+":"+attr.getValue()+" ");
						temp.append(attr.getName()+":"+attr.getValue()+" ");
					}
					return temp.toString();
				}

			}

		}
		return "";
		
	}
	
	public static boolean  updateBat(String configPath,String value) {
		StringBuffer strBuffer = new StringBuffer();
		// 修改bat文件
		// 先读取文件是否有如果没有则在文件开头添加，若有则更改
		// set JAVA_OPTS=–Xmxkm
		try {
			BufferedReader reader = new BufferedReader(new FileReader(
					configPath));
			String tempString = null;
			int line = 0;
			while ((tempString = reader.readLine()) != null) {
				if(line == 0) {
				   if(tempString.contains("set")&&tempString.contains("JAVA_OPTS")) {
					    String regex = "Xmx[0-9]+";
						 Pattern p = Pattern.compile(regex);
						 Matcher m = p.matcher(tempString);
						 while(m.find()) {
							 tempString = m.replaceAll("Xmx200");
						}
				   }
				   else {
					   strBuffer.append("set JAVA_OPTS=-CXmx"+value);
					   strBuffer.append(System.getProperty("line.separator"));
				   }
				}
				// 将读取到的所有的记录写到缓冲区中，最后在写到源文件中即可
				strBuffer.append(tempString);
				strBuffer.append(System.getProperty("line.separator"));
				line++;
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		// 替换后输出的文件位置
		try {
			PrintWriter printWriter = new PrintWriter(configPath);
			printWriter.write(strBuffer.toString().toCharArray());
			printWriter.flush();
			printWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static String  getBat(String configPath,String value) {
		StringBuffer strBuffer = new StringBuffer();
		// 修改bat文件
		// 先读取文件是否有如果没有则在文件开头添加，若有则更改
		// set JAVA_OPTS=–Xmxkm
		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(configPath),"UTF-8");
			BufferedReader reader = new BufferedReader(isr);
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				 if(tempString.contains("set")&&tempString.contains("JAVA_OPTS")) {
					 System.out.println(tempString);
					    String regex = "Xmx[0-9]+";
					    tempString = tempString.split("=")[1].replace("=", "").replace("-", "");
						 Pattern p = Pattern.compile(regex);
						 Matcher m = p.matcher(tempString);
						 while(m.find()) {
							return  m.group().replace("Xmx","").toString();
					}
				   }
				 }
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
		
	}
	

	public static void main(String[] args) {
		updateXML(
				"C:/software/servertest.xml",
				"Logger",
				"org.apache.catalina.logger.FileLogger,catalina_log_test.,.log,false");
		System.out.println("end");
	}

}
