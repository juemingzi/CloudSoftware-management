package edu.xidian.windows;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

/**@author Administrator
 *��ѹ�ļ�
 * @author Administrator
 *
 */

public class UnZip {

	/**@author Administrator
	 * ��ѹ�ļ�
	 * @param zipFilePath Ҫ��ѹ���ļ�·��
	 * @param unzipDirectory Ҫ��ѹ����Ŀ¼
	 * @throws Exception
	 */
	public static void unZip(String zipFilePath, String unzipDirectory)throws Exception {
		File file = new File(zipFilePath);// �����ļ�����
		ZipFile zipFile = new ZipFile(file);// ����zip�ļ�����
		File unzipFile = new File(unzipDirectory);// ������zip�ļ���ѹĿ¼
		if(unzipFile.exists()) unzipFile.delete();
		unzipFile.mkdir();
		Enumeration zipEnum = zipFile.getEntries(); // �õ�zip�ļ���Ŀö�ٶ���
		InputStream input = null;// ��������������
		OutputStream output = null;// �������������
		ZipEntry entry = null;// �������
		// ѭ����ȡ��Ŀ
		while (zipEnum.hasMoreElements()) {
			// �õ���ǰ��Ŀ
			entry = (ZipEntry) zipEnum.nextElement();
			String entryName = new String(entry.getName());
			// ��/�ָ���Ŀ����
			String names[] = entryName.split("\\/");
			int length = names.length;
			String path = unzipFile.getAbsolutePath();
			for (int v = 0; v < length; v++) {
				if (v < length - 1)  {
					 // ���һ��Ŀ¼֮ǰ��Ŀ¼
					path += "/" + names[v] + "/";
					createDir(path);
				}else {
					if (entryName.endsWith("/"))  {
						// ΪĿ¼,�򴴽��ļ���
						createDir(unzipFile.getAbsolutePath() + "/" + entryName);
					}else {
						// Ϊ�ļ�,��������ļ�
						input = zipFile.getInputStream(entry);
						output = new FileOutputStream(new File(
								unzipFile.getAbsolutePath() + "/" + entryName));
						byte[] buffer = new byte[1024 * 8];
						int readLen = 0;
						while ((readLen = input.read(buffer, 0, 1024 * 8)) != -1)
							output.write(buffer, 0, readLen);
						// �ر���
						input.close();
						output.flush();
						output.close();
					}
					
					
				}
			}
			
		}
	}
	
	/**@author Administrator
	 * ����Ŀ¼
	 * @param path Ҫ����dir��·��
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
