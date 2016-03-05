package edu.xidian.linux;

import java.io.*;

/**
 * @ClassName: StreamGobbler
 * @Description: 读输入流线程类
 * @author: wangyannan
 * @date: 2014-11-17 下午5:26:44
 */
class StreamGobbler extends Thread {
	InputStream is;
	String type;

	/**
	 * @Title:StreamGobbler
	 * @Description:构造方法
	 * @param is
	 * @param type
	 */
	StreamGobbler(InputStream is, String type) {
		this.is = is;
		this.type = type;
	}

	public void run() {
		try {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null)
				System.out.println(type + ">" + line);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
