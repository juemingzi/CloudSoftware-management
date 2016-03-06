package edu.xidian.windows;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class DeletePath {

	/**
	 * @param args
	 */
	WVMScriptExecute wexe = new WVMScriptExecute();
	
	public void deletePath(String s){
		
		File f = new File("C:/deletepath");
		if (!f.isDirectory()) {
			f.mkdir();
		}
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("@echo off");
		strBuffer.append(System.getProperty("line.separator"));
		strBuffer.append("path=%path:"+s+";=;%");
		strBuffer.append(System.getProperty("line.separator"));
		strBuffer.append("path=%path:;"+s+";=;%");
		strBuffer.append(System.getProperty("line.separator"));
		strBuffer.append("wmic environment where \"name='path' and username='<system>'\" set VariableValue=\"%path:"+s+";=;%\"");
		strBuffer.append(System.getProperty("line.separator"));
		strBuffer.append("wmic environment where \"name='path' and username='<system>'\" set VariableValue=\"%path:;"+s+";=;%\"");
		strBuffer.append(System.getProperty("line.separator"));
		
		PrintWriter printWriter;
		try {
			printWriter = new PrintWriter(
					"C:/deletepath/deletepath.bat");
			printWriter.write(strBuffer.toString().toCharArray());
			printWriter.flush();
			printWriter.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//Ö´ÐÐ½Å±¾
		String jdkCommand = "cmd /c  C:/deletepath/deletepath.bat";
		wexe.executeVMScript(jdkCommand);
	}
	
	
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//
//		DeletePath de = new DeletePath();
//		de.deletePath("C:\\");
//	}
//
}
