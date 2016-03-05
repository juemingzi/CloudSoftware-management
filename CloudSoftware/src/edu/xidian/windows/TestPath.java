package edu.xidian.windows;

import java.io.InputStream;
import java.util.HashSet;

/**
 * @author Administrator
 *
 */
public class TestPath {
		
		public String getPath() {
		    Runtime ObjRunTime = Runtime.getRuntime();
		    byte[] env = new byte[1000];
		    try{
		      Process ObjPrcess = ObjRunTime.exec("cmd /c echo %Path%");
		      InputStream in = ObjPrcess.getInputStream();
		      in.read(env);
		    }catch(Exception e) { e.printStackTrace();}
		    	return new String(env).trim();
		  }
		
//		public HashSet<String> deletePath(String str){
//			String[] sp = str.split(";");
////			for (int i=0;i<sp.length;i++){
////				System.out.println(sp[i]);
////			}
//			HashSet<String> pathSet = new HashSet<String>();
//			for (int i=0;i<sp.length;i++){
//				pathSet.add(sp[i]);
//			}
//			System.out.println(pathSet);
//			return pathSet;
//		}
		
		public boolean charge(String path,String s){

			String[] str = s.split(";");
			
			for (int i=0;i<str.length;i++){
				System.out.println(str[i]);
			}
			
			for(int i=0;i<str.length;i++)
			{
				if(path.equals(str[i])||path.equals(str[i]+";"))//if(path.contains(str[i]))
					return true;//若存在此变量返回true，否则返回false
			}
			return false;
		}
		
//		public static void main(String[] args){
//
//			TestPath tp = new TestPath();
//			String str = tp.getPath();
//			boolean b = tp.charge("C:\\Windows",str);
//			System.out.println(b);
//		}
		

	}