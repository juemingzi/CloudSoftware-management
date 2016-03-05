package edu.xidian.linux;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class test {
	public static void a(){
		try{
		b();}
		catch(IOException e){
			e.printStackTrace();
		}
	}
public static void  b()throws IOException{
	 File[] fileList = new File("G:\\").listFiles();
	 if(fileList != null){
	 for(File f:fileList) {}
	 try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		test.a();
	}

}
