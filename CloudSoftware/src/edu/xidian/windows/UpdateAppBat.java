package edu.xidian.windows;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

public class UpdateAppBat {
	static boolean  updateBatFile(String TomcatPath,String JDKPath,String FileName) {
		StringBuffer strBuffer = new StringBuffer();
		String configPath = TomcatPath + File.separator + "bin"+File.separator+FileName;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(configPath));
			String tempString = null;
			int line = 0;
			while ((tempString = reader.readLine()) != null) {
				if(line == 0) {
					strBuffer.append("set JAVA_HOME="+JDKPath);
					strBuffer.append(System.getProperty("line.separator"));
					strBuffer.append("set JRE_HOME="+JDKPath+"/jre");
					strBuffer.append(System.getProperty("line.separator"));
					strBuffer.append("set CATALINA_HOME="+TomcatPath);
					strBuffer.append(System.getProperty("line.separator"));
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
	
	
	

}
