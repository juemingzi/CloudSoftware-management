package edu.xidian.windows;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

/**@author Administrator
 *解压文件
 * @author Administrator
 *
 */

public class UnZip {

	/**@author Administrator
	 * 解压文件
	 * @param zipFilePath 要解压的文件路径
	 * @param unzipDirectory 要解压到的目录
	 * @throws Exception
	 */
	public static void unZip(String zipFilePath, String unzipDirectory)throws Exception {
		File file = new File(zipFilePath);// 创建文件对象
		ZipFile zipFile = new ZipFile(file);// 创建zip文件对象
		File unzipFile = new File(unzipDirectory);// 创建本zip文件解压目录
		if(unzipFile.exists()) unzipFile.delete();
		unzipFile.mkdir();
		Enumeration zipEnum = zipFile.getEntries(); // 得到zip文件条目枚举对象
		InputStream input = null;// 定义输入流对象
		OutputStream output = null;// 定义输出流对象
		ZipEntry entry = null;// 定义对象
		// 循环读取条目
		while (zipEnum.hasMoreElements()) {
			// 得到当前条目
			entry = (ZipEntry) zipEnum.nextElement();
			String entryName = new String(entry.getName());
			// 用/分隔条目名称
			String names[] = entryName.split("\\/");
			int length = names.length;
			String path = unzipFile.getAbsolutePath();
			for (int v = 0; v < length; v++) {
				if (v < length - 1)  {
					 // 最后一个目录之前的目录
					path += "/" + names[v] + "/";
					createDir(path);
				}else {
					if (entryName.endsWith("/"))  {
						// 为目录,则创建文件夹
						createDir(unzipFile.getAbsolutePath() + "/" + entryName);
					}else {
						// 为文件,则输出到文件
						input = zipFile.getInputStream(entry);
						output = new FileOutputStream(new File(
								unzipFile.getAbsolutePath() + "/" + entryName));
						byte[] buffer = new byte[1024 * 8];
						int readLen = 0;
						while ((readLen = input.read(buffer, 0, 1024 * 8)) != -1)
							output.write(buffer, 0, readLen);
						// 关闭流
						input.close();
						output.flush();
						output.close();
					}
					
					
				}
			}
			
		}
	}
	
	/**@author Administrator
	 * 创建目录
	 * @param path 要创建dir的路径
	 */
	static void createDir(String path) {
		File dir = new File(path);
		if (!dir.exists()) dir.mkdir();
	}
	
	static String getSuffixName(String name) {
		return name.substring(0, name.lastIndexOf("."));
	}
	
	public static void main(String[] args) {
		try {
			UnZip.unZip("D:/Apache24.zip", "D:/");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
