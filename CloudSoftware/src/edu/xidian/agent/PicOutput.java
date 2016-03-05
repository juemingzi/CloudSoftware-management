package edu.xidian.agent;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/** 
 * @ClassName: PicOutput 
 * @Description: socketÍ¼Æ¬´«ÊäÀà
 * @author: wangyannan
 * @date: 2015-1-20 ÏÂÎç4:49:19  
 */
public class PicOutput {
	/** 
	 * @Title: output 
	 * @Description: socket´«ÊäÍ¼Æ¬
	 * @param path
	 * @return: void
	 */
	public static void output(String path,String ip) {
		int length = 0;
		byte[] sendBytes = null;
		Socket socket = null;
		DataOutputStream dos = null;
		FileInputStream fis = null;

		try {
			try {
				socket = new Socket();
//				socket.connect(new InetSocketAddress("119.90.140.60", 7001),
//						10 * 1000);
				socket.connect(new InetSocketAddress(ip, 7001),
						10 * 1000);
				dos = new DataOutputStream(socket.getOutputStream());
				File file = new File(path);
				fis = new FileInputStream(file);
				sendBytes = new byte[1024];
				while ((length = fis.read(sendBytes, 0, sendBytes.length)) > 0) {
					dos.write(sendBytes, 0, length);
					dos.flush();
				}
			} finally {
				if (dos != null)
					dos.close();
				if (fis != null)
					fis.close();
				if (socket != null)
					socket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
