/**
 * @brief 
 * @author huangpeng
 * @version 
 * @date 2015-4-20
 */
package sample.hello.client;

import java.io.InputStream;
import java.util.HashSet;

/**
 * @author Administrator
 *
 */
public class EnvPathDelete {
	
	
	public void deletePath(String str){
		String[] sp = str.split(";");
//		for (int i=0;i<sp.length;i++){
//			System.out.println(sp[i]);
//		}
		HashSet<String> pathSet = new HashSet<String>();
		for (int i=0;i<sp.length;i++){
			pathSet.add(sp[i]);
		}
		System.out.println(pathSet);	
	}
	public String getPath() {
	    Runtime ObjRunTime = Runtime.getRuntime();
	    byte[] env = new byte[1000];
	    try{
	      Process ObjPrcess = ObjRunTime.exec("cmd /c echo %Path%");
	      InputStream in = ObjPrcess.getInputStream();
	      in.read(env);
	    }catch(Exception e) { e.printStackTrace();}
//	    	System.out.println(new String(env).trim());
	    	return new String(env).trim();
	  }
	
	public static void main(String[] args){
		EnvPathDelete epd = new EnvPathDelete();
		String str = epd.getPath();
		epd.deletePath(str);
	}
	

}
