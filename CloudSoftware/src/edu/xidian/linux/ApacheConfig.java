package edu.xidian.linux;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ApacheConfig {
	public static boolean updateConf(String path,String value) {
		File file = new File(path);
		BufferedReader reader = null;
		StringBuffer strBuffer = new StringBuffer();
		PrintWriter printWriter;
		try {
			
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			
			boolean flag = false;
			while ((tempString = reader.readLine()) != null) {
				
				if (tempString.contains("<IfModule")&&tempString.contains("mpm_prefork_module>")&&!tempString.trim().startsWith("#")) {
					flag = true;
				}else if(tempString.contains("</IfModule>")) {
					flag = false;
				}
				if(flag) {
					if(tempString.contains("MaxClients")) {
						System.out.println("before:"+ tempString);
						 String regex = "[0-9]+";
						 Pattern p = Pattern.compile(regex);
						 Matcher m = p.matcher(tempString);
						 while(m.find()) {
							 tempString = m.replaceAll(value);
						}
						System.out.println("after:" + tempString);
						
					}
				}
				
				// 鐏忓棜顕伴崣鏍у煂閻ㄥ嫭澧嶉張澶屾畱鐠佹澘缍嶉崘娆忓煂缂傛挸鍟块崠杞拌厬閿涘本娓堕崥搴℃躬閸愭瑥鍩屽┃鎰瀮娴犳湹鑵戦崡鍐插讲
				strBuffer.append(tempString);
				strBuffer.append(System.getProperty("line.separator"));
			
				}
			
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		// 閺囨寧宕查崥搴ょ翻閸戣櫣娈戦弬鍥︽娴ｅ秶鐤�
		try {
			printWriter = new PrintWriter(path);
			printWriter.write(strBuffer.toString().toCharArray());
			printWriter.flush();
			printWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	return true;
	}

	
	public static String getConf(String path,String value) {
		File file = new File(path);
		BufferedReader reader = null;
		try {
			
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			boolean flag = false;
			while ((tempString = reader.readLine()) != null) {
				if (tempString.contains("<IfModule")&&tempString.contains("mpm_prefork_module>")&&!tempString.trim().startsWith("#")) {
					flag = true;
				}else if(tempString.contains("</IfModule>")) {
					flag = false;
				}
				if(flag) {
					if(tempString.contains("MaxClients")) {
						 String regex = "[0-9]+";
						 Pattern p = Pattern.compile(regex);
						 Matcher m = p.matcher(tempString);
						 while(m.find()) {
							 System.out.println(m.group());
							 return m.group();
						}
					}
				}
				}
			
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		
		}
		
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		return "";
		
	}
	

}
