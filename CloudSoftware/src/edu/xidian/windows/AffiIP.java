package edu.xidian.windows;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import edu.xidian.windows.WOSConfig;

public class AffiIP {

	/**
	 * ���������ַ��ȡip���б�
	 * 
	 * @param name
	 * @return
	 * @throws IOException
	 */
	private static List<String> searchIP(String physicalAddress)
			throws IOException {
		Process process = null;
		BufferedReader br = null;
		List<String> list = new ArrayList<String>();

		process = Runtime.getRuntime().exec("ipconfig /all");
		br = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line = "";
		boolean flag = false;
		while ((line = br.readLine()) != null) {
			if (line.toLowerCase().contains("Ĭ������")) {// ��ȡ����
				break;
			}
			if (!flag) {
				if (line.toLowerCase().indexOf(physicalAddress.toLowerCase()) >= 0) {// �����ַ��ʼ��ȡ

					flag = true;

				}
			}
			if (flag) {
				if (line.toLowerCase().indexOf("ipv4 ��ַ") >= 0) {
					int start = line.indexOf(":");
					int end = line.indexOf("(");
					list.add(line.substring(start + 1, end).trim());
				}
			}
		}

		return list;

	}
	
	
	private static String getMacName(String physicalAddress) throws IOException {
		Process process = null;
		BufferedReader br = null;
		process = Runtime.getRuntime().exec("ipconfig /all");
		br = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line = "";
		String macName = "";

		while ((line = br.readLine()) != null) {
			int start = -1;
			int end = -1;
			if (!line.contains(". :") && (start = line.indexOf(" ")) > 0
					&& (end = line.indexOf(":")) > 0) {

				macName = line.substring((start + 1), end);

			}

			if (line.contains("�����ַ") && line.contains(physicalAddress)) {

				return macName;

			}

		}
		return null;
	}

	/**
	 * �жϸ������ַ���Ƿ���ڸ�ip
	 * 
	 * @param name
	 * @param ip
	 * @return
	 */
	public static boolean checkIP(String physicalAddress, String ip) {
		List<String> list = null;

		try {
			list = searchIP(physicalAddress);

			if (list != null) {
				return list.contains(ip);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;

	}

	/**
	 * �ж��Ƿ�Ϊ��IP
	 * @param physicalAddress
	 * @param oldIP
	 * @return ����IP����true������Ϊfalse
	 */
	private static boolean checkMainIP(String physicalAddress,String oldIP)
	{
		List<String> list = null;
		try {
			list = searchIP(physicalAddress);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(list != null && list.size() > 0 && list.get(0).equals(oldIP))
		{
			return true;
		}
		
		return false;
	}
	
	
	
	
	
	/**
	 * �޸�IP
	 * 
	 * @param physicalAddress�����ַ
	 * @param oldIPԭʼIP
	 * @param newIP
	 * @param mask��������
	 * @return
	 */

	public static String changeIP(String physicalAddress, String oldIP,
			String newIP, String mask) {
		try {
			
			if(checkMainIP(physicalAddress, oldIP))
				return "0x0000604";
			
			if (deleteIP(physicalAddress, oldIP).equals("0x0001100")) {
				if (addIP(physicalAddress, newIP, mask).equals("0x0001000")) {
					
					
					
					
					return "0x0000600";
				} else {
					return "0x0000601";
				}
			} else {
				return "0x0000602";
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "0x0000601";
	}

	/**
	 * ɾ������IP
	 * 
	 * @param physicalAddress
	 *            �����ַ
	 * @param IP
	 *            ����ip
	 * @return
	 * @throws IOException
	 */
	public static String deleteIP(String physicalAddress, String IP)
			throws IOException {
		boolean f = false;
		if (checkIP(physicalAddress, IP))// �ж�IP�Ƿ����
		{
			
			String macName = getMacName(physicalAddress);
			if(macName == null)
			{
				return "0x0001102";
			}
			
			
			if(checkMainIP(physicalAddress, IP))
			{
				return "0x0001103";
			}
			String addIPStr = "netsh interface ip del address \""
					+ macName + "\" " + IP + "\r\n";// ���IP
			File filepath = new File("C:\\setupscripts\\");
			if (!filepath.isDirectory()) {
				filepath.mkdirs();
			}
			File batfile = new File("C:\\setupscripts\\delAffiIP.bat");
			batfile.createNewFile();
			FileOutputStream outStr = new FileOutputStream(batfile);
			BufferedOutputStream buff = new BufferedOutputStream(outStr);
			buff.write(addIPStr.getBytes());
			buff.flush();
			buff.close();
			outStr.close();
			f = WOSConfig
					.excuteCommand("cmd /c C:\\setupscripts\\delAffiIP.bat");
			if (f) {
				return "0x0001100";
			}
			return "0x0001101";
		} else {
			System.out.println(" IP doesn't exist ");// ������û�д�IP
			return "0x0001102";
		}

	}

	// public static String addIP(String physicalAddress, String IP) throws
	// IOException
	// {
	// return addIP(physicalAddress, IP, "255.255.255.0");
	// }

	/**
	 * ��Ӹ���IP
	 * 
	 * @param physicalAddress
	 *            �����ַ
	 * @param IP
	 *            ����ip
	 * @param mask
	 *            ����
	 * @return
	 * @throws IOException
	 */
	public static String addIP(String physicalAddress, String IP, String mask)
			throws IOException {
		boolean f = false;
		
		String macName = getMacName(physicalAddress);
		if(macName == null)
		{
			return "0x0001002";
		}
		
		String addIPStr = "netsh interface ip add address \"" + macName
				+ "\" " + IP + " " + mask + "\r\n";// ���ipָ��
		File filepath = new File("C:\\setupscripts\\");
		if (!filepath.isDirectory()) {
			filepath.mkdir();
		}
		File batfile = new File("C:\\setupscripts\\addIP.bat");// �����������ļ�
		batfile.createNewFile();
		FileOutputStream outStr = new FileOutputStream(batfile);
		BufferedOutputStream buff = new BufferedOutputStream(outStr);
		buff.write(addIPStr.getBytes());
		buff.flush();
		buff.close();
		outStr.close();
		f = WOSConfig.excuteCommand("cmd /c C:\\setupscripts\\addIP.bat");// ִ���������ļ�
		if (f) {
			return "0x0001000";
		}
		return "0x0001001";
	}

	public static String getIPs(String physicalAddress) {
		List<String> list = null;
		String iPString = "";
		try {
			list = searchIP(physicalAddress);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(list != null && list.size() > 0)
		{
			list.remove(0);
		}
		
		iPString = list.toString();
		String ipStrings = iPString.substring(1, iPString.length() - 1).replaceAll(" ", "");
		
		return ipStrings.length() > 0 ? ipStrings : "0x0001202";

	}

}
