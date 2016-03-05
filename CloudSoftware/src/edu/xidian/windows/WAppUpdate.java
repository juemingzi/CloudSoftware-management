package edu.xidian.windows;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import edu.xidian.agent.FtpTransFile;

public class WAppUpdate {
	WAppUninstall wau = new WAppUninstall();
	WAppSetup was = new WAppSetup();
	WAppConfig wac = new WAppConfig();
	/**
	 * @author wzy
	 * show ����mysql
	 * @param zipFileName ��װ��������
	 * @param unzipDirectory ��ѹ���ŵ�λ��
	 * @param mysqlPath ԭ��mysql�İ�װλ��
	 * @param password ���µ����ݿ�����
	 * @return 0x0200200 ж�سɹ�
	 * 		   0x0100200 ��װ�ɹ�
	 * 		   0x0400200 ���³ɹ�
	 * 		   0x0400201 mysql����ʧ�ܣ���Ϊ�ɰ汾��mysql����ʧ��
	 *         0x0400202 mysql����ʧ�ܣ���Ϊ�°汾��mysql��װʧ��
	 */
public static String updateMySql( String zipFileName, String mysqlPath,String unzipDirectory,String password) throws Exception{
		
		WAppUninstall wu = new WAppUninstall();
		WAppSetup ws = new WAppSetup();
		String funinstall = wu.uninstallMySql(mysqlPath);	
		String setup = "";
		String update = "";
		 if(funinstall == "0x0200200"){
			 mysqlPath = null;
			setup = ws.setupMySql(zipFileName, unzipDirectory, password, mysqlPath);
			if(setup == "0x0100200"){
				update = "0x0400200";
				System.out.println("������³ɹ�");
			}else {
				update = "0x0400202";
				System.out.println("�����װʧ�ܵ��µ��������ʧ��");//ֻ�Ƿ����˱��룬û�н��������Ĳ���
			}
		}else{
			update = "0x0400201";
			System.out.println("���ж��ʧ�ܵ��µĸ���ʧ��");
		}
		System.out.println("end");
		return update;
	}


	// ����nginx
public String updateNginx(String zipFileName, String path, String oldnginxpath) {
	String result = was.setupNginx(zipFileName, path, oldnginxpath);
	if(result.equals("0x0100400"))
	{
		return "0x0400400";
	}
	return "0x0400401";
}
	

		// ����zendguardloader
public String updateZendGuardLoader(String newzip,String phppath ,String uninstallPath) throws FileNotFoundException, InterruptedException {
		
		// 1.ж��zendguardloader
		String result1 = wau.uninstallZendGuardLoader(uninstallPath);
		if (result1 == "0x0200504") {	
			return "0x0400503";
		}else if(result1 != "0x0200500") {
			return "0x0400501";
		}
		
	
		// 2.��װ�°汾��zendguardloader
		String result2 = was.setupZendGuardLoader(newzip, phppath,uninstallPath);

		if (result2 != "0x0100500") {
			return "0x0400502";
		}
		return "0x0400500";
	}

	
public String updateTomcat(String TomcatZip, String TomcatPath,
		String JDKPath,String unistallPath) throws IOException {
	//tomcatzip��װ�����ƣ�tomcatpath��װ·��,jdkpath jdk·����uninstallpath�ɰ汾·��
	
		WAppSetup ws = new WAppSetup();
		if (ws.setupTomcat(TomcatZip, TomcatPath, JDKPath,unistallPath).equals("0x0100000")) {
			return "0x0400000";
		} else {
			return "0x0400001";
		}
	
	
}

	public String updateApache(String ApacheFileName, String ApachePath,
			String emailAddress,String uninstallpath) throws IOException {
		//ApacheFileName��װ�����ƣ�ApachePath��װ·��,emailAddress �������䣬uninstallpath�ɰ汾·��
		
		
			WAppSetup ws = new WAppSetup();
			if (ws.setupApache(ApacheFileName, ApachePath, emailAddress,uninstallpath).equals(
				"0x0100300")) {
				return "0x0400300";
			} else {
				return "0x0400301";
			}
		
	}

	public String updatePython(String PythonFileName, String path, String oldPath) throws IOException
	{
		// ��������������ɾ���ٰ�װ
		// Utils.findDirs("C:/", "python");
		// wau.uninstallPython();
		WAppSetup ws = new WAppSetup();
		if (ws.setupPython(PythonFileName, path, oldPath).equals("0x0100600"))
		{
			return "0x0400600";
		} else
		{
			return "0x0400601";
		}
	}

	public String updateFTP(String FTPFileName, String installPath,String uninstallPath)
			throws IOException {
	
		WAppSetup ws = new WAppSetup();
		if (ws.setupFTP(FTPFileName, installPath,uninstallPath).equals("0x0100900")) {
			return "0x0400900";
		} else {
			return "0x0400901";
		}
	}


	public String update360(String FileName, String Path) throws IOException {
		// ��������������ɾ���ٰ�װ
		Utils.findDirs(Path, "360");
		//wau.uninstall360();
		WAppSetup ws = new WAppSetup();
		if (ws.setup360(FileName, Path,null).equals("0x0100E00")) {
			return "0x0400E00";
		} else {
			return "0x0400E01";
		}
	}
	


	/**
	 * @author wzy
	 * show ����memcached
	 * @param fileName1 Ҫж�ص����� 
	 * @param fileName2 Ҫ���µ�����
	 * @return 0x0200700 memcachedж�سɹ�
	 * 		   0x0400701����ʧ�ܣ���Ϊж�ؾɰ汾��memcachedʧ��
	 *		   0x0100700 memcached�°汾�İ�װ�ɹ� 
	 * 		   0x0400700 ���³ɹ�
	 * 		   0x0400702 ����ʧ�ܣ���Ϊ�°��memcached��װʧ��
	 */
	public String updateMemcached(String filename,String newpath,String oldpath) throws Exception{
		String result = was.setupMemcached(filename, newpath, oldpath);
		if(result.equals("0x0100700"))
		{
			return "0x0400700";
		}		
		return "0x0400701";
}
}
