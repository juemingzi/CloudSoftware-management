package edu.xidian.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.net.Socket;
import java.net.UnknownHostException;

import com.sun.management.OperatingSystemMXBean;

import edu.xidian.message.Message;
import edu.xidian.message.MsgType;
import edu.xidian.windows.UnZip;
import edu.xidian.windows.Utils;

public class Test {
	public static void main(String[] args) {
		
//		try {
//			UnZip.unZip("C:/softsource/apache-tomcat-7.0.57-windows-x64.zip", "C:/software");
//		} catch (Exception e) {
//			e.printStackTrace();
//			
//		}
		double d = Double.valueOf("6.3");
		System.out.println(d);
		
//		int currentSpace = (int) new File("C:\\")
//		.getFreeSpace() / 1024;
//		System.out.println(currentSpace);
//		try {
//			Socket socket = new Socket("127.0.0.1", 9400);
//			Message msg = new Message(MsgType.executeVMScript, "123", null);
//			ObjectOutputStream oos = new ObjectOutputStream(
//					socket.getOutputStream());
//			oos.writeObject(msg);
//			
//			PrintWriter pw = new PrintWriter(socket.getOutputStream(),true);            
//                       
//            
//            
//            File file = new File("D:/script.bat");
//            BufferedReader reader = null;
//            try {
//                //System.out.println("����Ϊ��λ��ȡ�ļ����ݣ�һ�ζ�һ���У�");
//                reader = new BufferedReader(new FileReader(file));
//                String tempString = null;
//                pw.println(file.getName());//���е�һ�д��ļ����ֺͺ�׺
//                int i = 1;            
//                // һ�ζ���һ�У�ֱ������nullΪ�ļ�����
//                while ((tempString = reader.readLine()) != null) {
//                    // ��ʾ�к�
//                   // System.out.println(tempString);
//                    pw.println(tempString); //���ļ�����д��socket���У����͵���������
//                    i++;
//                }
//                reader.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                if (reader != null) {
//                    try {
//                        reader.close();
//                    } catch (IOException e1) {
//                    }
//                }
//            }
//            //pw.close();
//            
//            
//			ObjectInputStream ois = new ObjectInputStream(
//					socket.getInputStream());
//			msg = (Message) ois.readObject();
//			if (msg.getType().equals(MsgType.executeVMScript)) {
//				String ret = (String) (msg.getValues());
//				System.out.println(ret);
//			}
//			socket.close();
			//String aa = "ZendGuardLoader-php-5.3-linux-glibc23-x86_64.tar.gz";ZendGuardLoader-php-5.3-Windows.zip
//			String aa = "ZendGuardLoader-70429-PHP-5.4-Windows-x86.zip";
//			int a = aa.indexOf("PHP");
//			System.out.println(aa.substring(a+4, a+7));
//			
//			OperatingSystemMXBean osmb = (OperatingSystemMXBean) ManagementFactory
//					.getOperatingSystemMXBean();
//			System.out.println("ϵͳ�����ڴ��ܼƣ�" + osmb.getTotalPhysicalMemorySize()
//					/ 1024 / 1024 + "MB");
//
//			if (2043*1024*1024 < 1024 * 1024 * 1024) {
//				System.out.println("�ڴ治�㣬ϵͳ�����ڴ��ܼƣ�"
//						+ osmb.getTotalPhysicalMemorySize() / 1024 / 1024 + "MB");
//				
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//			try {
//				Process process = Runtime.getRuntime().exec("osql /?");
//				BufferedReader strCon = new BufferedReader(new InputStreamReader(
//						process.getInputStream()));
//				String line;
//				int i = 1;
//				while ((line = strCon.readLine()) != null) {
//					if (i == 2) {
//						String info = "�Ѱ�װ��SqlServer2008R2   " + line;
//						System.out.println(info);// ����Ѱ�װSqlServer2008R2��������osql
//													// /?������ĵڶ���Ϊ�Ѱ�װ�İ汾��Ϣ
////						return "0x0100A04" + " " + info;// �Ѱ�װ�� �����ٰ�װ
//					}
//					i++;
//				}
////				if(i!=2)
////					System.out.println("i!=2  0x0200A02");   //��������ڣ�����ж��
//			} catch (IOException e) {
//				System.out.println("0x0200A02");   //��������ڣ�����ж��
////			}
//		String[] strs = ("C:\\Apache1").split("\\\\");
//		//StringBuilder sb = new StringBuilder();
////		for(int i=0;i<strs.length-1;i++) {
////			//sb.append(strs[i]+"\\");
////		}
//		System.out.println(strs[strs.length-1]);
//		//System.out.println(sb.toString());
		
//		File[] fileList = new File("C://softsource").listFiles();
//		for (File f : fileList) {
//			if (f.getName().contains("ZendGuardLoader")) {
//				if(f.isFile())
//					f.delete();
//				else
//					Utils.delFolder(f.getAbsolutePath());
//			}
//		}
	}

}
