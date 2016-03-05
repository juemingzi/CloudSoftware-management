package edu.xidian.test;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class test1 {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Runtime rt = Runtime.getRuntime();
		File f=new File("/");
		Process proc=rt.exec("ls", null, f);
		BufferedReader in = null;

		in = new BufferedReader(new InputStreamReader(proc
				.getInputStream()));
		String s = null;
		boolean flage = false;
		while ((s = in.readLine()) != null) {
			System.out.println(s);

		}
//		System.out.println()
//		Process proc = rt.exec("ls","");
//		proc = rt.exec("ls");
//		proc = rt.exec("ls");
	}

}
