package edu.xidian.message;

import java.io.File;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.sun.org.apache.xml.internal.utils.URI;
import com.sun.org.apache.xml.internal.utils.URI.MalformedURIException;
/**
 * Xml工具类，读写XML文档
 * @author huangpeng
 *
 */
public class XmlUtils {
		public static String configFilePath;
		static{
			try {
				//filePath = new URI(XmlUtils.class.getClassLoader().getResource("curInfo.txt").toString()).getPath();
				configFilePath = new URI(XmlUtils.class.getClassLoader().getResource("conf.xml").toString()).getPath();
			} catch (MalformedURIException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		public static Document getDocument() throws DocumentException{
			SAXReader reader = new SAXReader();
		    Document document = reader.read(new File(configFilePath));
		    return document;
		}
		public static void main(String[] args) throws DocumentException{
			Document doc = XmlUtils.getDocument();
			Element root = doc.getRootElement();// 得到根节点
			Element pathEle = root.element("listen-port");
			System.out.println(pathEle.getText());
		}
}
