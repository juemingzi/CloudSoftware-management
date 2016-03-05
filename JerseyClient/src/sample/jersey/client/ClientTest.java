package sample.jersey.client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;

public class ClientTest {

	public static void main(String[] args) {

		String time = CurrentTime();
		System.out.println(time);
		Client c = Client.create();
		WebResource r = c
				.resource("http://119.90.140.60:8080/JerseyBasicAuth/rest/");
		
		
	    /*
	     * Windows���Դ���
	     */
//		installTomcat(r,time);
//		uninstallTomcat(r,time);
		//updateTomcat(r,time);
		//getParamTomcat(r,time);//�����޸İ汾�ţ��Ͱ汾6.0.41���߰汾7.0.57
		//TomcatParam(r,time);//�����޸İ汾�ţ��Ͱ汾6.0.41���߰汾7.0.57
		
		
		//installMysql(r,time);
		//updateMySql(r,time);
		//uninstallMysql(r,time);
		//MysqlParam(r,time);//�����޸İ汾�ţ��Ͱ汾mysql-5.7.1-m11-winx64���߰汾mysql-5.7.4-m14-winx64
		//getParamMySql(r,time);//�����޸İ汾�ţ��Ͱ汾mysql-5.7.1-m11-winx64���߰汾mysql-5.7.4-m14-winx64
		
		//installNginx(r,time);
		//uninstallLNginx(r,time);
		//uninstallHNginx(r,time);
		//updateNginx(r,time);
		//NginxParam(r,time);//���д˴����޸�����İ汾�ţ��Ͱ汾1.2.8���߰汾1.6.2
		//getParamNginx(r,time);//���д˴����޸�����İ汾�ţ��Ͱ汾1.2.8���߰汾1.6.2
		
		
//		QueryProgress(r,time,"C:\\1.jpg","1051","false");
		//getCodeInfo(r,time,"0x0100000");
		
		//installZendGuardLoader(r,time);
		//ZendGuardLoaderParam(r,time);
		//uninstallZendGuardLoader(r,time);
		//updateZendGuardLoader(r,time);		
		//getParamZendGuardLoader(r,time);
		
		//installMemcached(r,time);
		//uninstallMemcached(r,time);
		//updateMemcached(r,time);
		
		
		//QueryProgress(r,time,"C:\\1.jpg","376","false");
		//getCodeInfo(r,time,"0x0100500");
		
		//installApache(r,time);
		//ApacheParam(r,time);
		//uninstallApache(r,time);
		//updateApache(r,time);
		//getParamApache(r,time);
		
	
		
		//installPython(r, time);
		//updatePython(r,time);
		//uninstallPython(r,time);
		
		
		//QueryProgress(r,time,"C:\\1.jpg","1470","false");
		//getCodeInfo(r,time,"0x0300502");
		
		//uninstallIISRewrite(r, time);
		//installIISRewrite(r, time);
		
		
		//QueryProgress(r,time,"C:\\1.jpg","2111","false");
		//getCodeInfo(r,time,"0x0101000");
	
		//installFTP(r, time);		
		//updateFTP(r,time);
		//uninstallFTP(r,time);		
		
		
		//install360(r, time);
		
//		installOracle11g(r,time);
		uninstallOracle11g(r,time);
//		interfaceInstallOracle11g(r,time);
		
		
//		installSQLServer2008R2(r,time);//�ڰ�װ��ʱ���ֶ���ѹ����Ӧλ�ã��ļ����Ѿ�����������ã�ע�����İ�װ�ռ�����ҪԤ��12����13G
										//��װ�ɹ�������������������osql /?����֤�Ƿ�װ�ɹ���
//		interfaceInstallSQLServer2008R2(r,time);//ͬ��
//		uninstallSQLServer2008R2(r,time);
			
//		QueryProgress(r,time,"C:\\1.jpg","1210","false");
//		getCodeInfo(r,time,"0x0201000");
		//reboot(r,time);
		// ���ô���
		//changePWD(r, time);	
		//changeIP(r, time);
//		vmScript(r, time, "C:\\Users\\abc\\Desktop\\script.bat");
		
		//QueryProgress(r,time,"C:\\1.jpg","753","false");
//		getCodeInfo(r,time,"0x0000800");
		//changeSecRules(r,time);
		//startOrStopService(r,time);
		
//		getSystemServiceState(r, time);//��ȡȫ������
//		getServiceState(r,time);//��ȡ��������
		//getSystemSecRule(r, time);
		//getSystemIP(r, time);

		
		//addSystemaffiIP(r,time);
		//deletesystemAffiIP(r,time);
//		searchsystemAffiIP(r,time);
		//changesystemAffiIP(r,time);

/*
 * ���в��Ե�Linux������ClientLinuxTest��
 */
		

	}

	/**
	 * ��ȡ��ǰʱ��
	 */
	private static String CurrentTime() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		String time = df.format(now);
		return time;
	}

	/**
	 * @author Administrator ��װtomcat
	 * @param r
	 * @param time
	 */
	private static void installTomcat(WebResource r, String time) {
		String response = r.path("AppInstallation/Tomcat")
				.queryParam("ip", "119.90.140.59")
				.queryParam("installPath", "C:")
				.queryParam("jdkPath", "C:/Program Files/Java/jdk1.7.0_45")
				.queryParam("version", "6.0.41")
				.queryParam("cover", "YES")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator ��װMysql
	 * @param r
	 * @param time
	 */
	private static void installMysql(WebResource r, String time) {
		String response = r.path("AppInstallation/MySql")
				.queryParam("ip", "119.90.140.59")
				.queryParam("installPath", "C:")
				.queryParam("rootPswd", "repace")
				.queryParam("version", "5.7.1")
				.queryParam("cover", "YES")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator ��װnginx
	 * @param r
	 * @param time
	 */
	private static void installNginx(WebResource r, String time) {
		String response = r.path("AppInstallation/Nginx")
				.queryParam("ip", "119.90.140.59")
				.queryParam("installPath", "C:")
				.queryParam("version", "1.2.8")
				.queryParam("cover", "YES")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator ��װZendGuardLoader
	 * @param r
	 * @param time
	 */
	private static void installZendGuardLoader(WebResource r, String time) {
		String response = r.path("AppInstallation/ZendGuardLoader")
				.queryParam("ip", "119.90.140.59")
				.queryParam("phpPath", "C:/php")
				.queryParam("version", "5.3")
				.queryParam("cover", "YES")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator ��װMemcached
	 * @param r
	 * @param time
	 */
	private static void installMemcached(WebResource r, String time) {
		String response = r.path("AppInstallation/Memcached")
				.queryParam("ip", "119.90.140.59")
				.queryParam("installPath", "C:")
				.queryParam("version", "1.4.13")
				.queryParam("cover", "YES")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator ��װApache
	 * @param r
	 * @param time
	 */
	private static void installApache(WebResource r, String time) {
		String response = r.path("AppInstallation/Apache")
				.queryParam("ip", "119.90.140.59")
				.queryParam("installPath", "C:")
				.queryParam("emailAddress", "123@123.com")
				.queryParam("version", "2.2.31")
				.queryParam("cover", "YES")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator ��װPython
	 * @param r
	 * @param time
	 */
	private static void installPython(WebResource r, String time) {
		String response = r.path("AppInstallation/Python")
				.queryParam("ip", "119.90.140.59")
				.queryParam("installPath", "C:")
				.queryParam("version", "2.7.4")//�˴��汾��
				.queryParam("cover", "YES")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator ��װIISRewrite
	 * @param r
	 * @param time
	 */
	private static void installIISRewrite(WebResource r, String time) {
		String response = r.path("AppInstallation/IISRewrite")
				.queryParam("ip", "119.90.140.59")
				.queryParam("installPath", "C:")
				.queryParam("version", "3.0104")
				.queryParam("cover", "YES")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator ��װFTP
	 * @param r
	 * @param time
	 */
	private static void installFTP(WebResource r, String time) {
		String response = r.path("AppInstallation/FTP")
				.queryParam("ip", "119.90.140.59")
				.queryParam("installPath", "C:")
				.queryParam("version", "0.9.45")
				.queryParam("cover", "YES")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator ��װSQLServer2008R2
	 * @param r
	 * @param time
	 */
	private static void installSQLServer2008R2(WebResource r, String time) {
		String response = r.path("AppInstallation/SQLServer2008R2")
				.queryParam("ip", "119.90.140.59")
				.queryParam("installPath", "E:")
				.queryParam("rootPswd", "repace")
				.queryParam("hostName", "CLJAZITTFGVDGF8")
				.queryParam("userName", "Administrator")
				.queryParam("version", "2008.2")
				.queryParam("cover", "YES")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator ��װOracle11g
	 * @param r
	 * @param time
	 */
	private static void installOracle11g(WebResource r, String time) {
		String response = r
				.path("AppInstallation/Oracle11g")
				.queryParam("ip", "119.90.140.59")
				.queryParam("hostname", "CLJAZITTFGVDGF8")
				.queryParam("inventorypath","C:/Program Files/Oracle/Inventory")
				.queryParam("oraclebase", "E:/app/wenyanqi")
				.queryParam("oraclehome", "dbhome1")
				.queryParam("rootPswd", "123456QWq")
				.queryParam("version", "11.0")
				.queryParam("cover", "YES")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator ���氲װSQLServer2008R2
	 * @param r
	 * @param time
	 */
	private static void interfaceInstallSQLServer2008R2(WebResource r,
			String time) {
		String response = r.path("AppInstallation/InterfaceSQLServer2008R2")
				.queryParam("ip", "119.90.140.59")
				.queryParam("installPath", "E:")
				.queryParam("rootPswd", "repace")
				.queryParam("hostName", "CLJAZITTFGVDGF8")
				.queryParam("userName", "Administrator")
				.queryParam("version", "2008.2")
				.queryParam("cover", "YES")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator ���氲װOracle11g
	 * @param r
	 * @param time
	 */
	private static void interfaceInstallOracle11g(WebResource r, String time) {
		String response = r.path("AppInstallation/InterfaceOracle11g")
				.queryParam("ip", "119.90.140.59")
				.queryParam("oraclebase", "E:/app/wenyanqi")
				.queryParam("oraclehome", "dbhome1")
				.queryParam("inventorypath", "C:/app/wenyanqi/oradata")
				.queryParam("databasename", "testoracl")
				.queryParam("rootPswd", "123456QWq")
				.queryParam("version", "11.0")
				.queryParam("cover", "YES")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator ��װ360
	 * @param r
	 * @param time
	 */
	private static void install360(WebResource r, String time) {//360�����ڸ��ǲ���
		String response = r.path("AppInstallation/360")
				.queryParam("ip", "119.90.140.59")
				.queryParam("installPath", "C:")
				.queryParam("version", "")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator ж��tomcat
	 * @param r
	 * @param time
	 */
	private static void uninstallTomcat(WebResource r, String time) {
		String response = r.path("AppUninstall/Tomcat")
				.queryParam("ip", "119.90.140.59")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator ж��mysql
	 * @param r
	 * @param time
	 */
	private static void uninstallMysql(WebResource r, String time) {
		String response = r.path("AppUninstall/MySql")
				.queryParam("ip", "119.90.140.59")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator ж�صͰ汾nginx
	 * @param r
	 * @param time
	 */
	private static void uninstallLNginx(WebResource r, String time) {
		String response = r.path("AppUninstall/Nginx")
				.queryParam("ip", "119.90.140.59")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}
	
	/**
	 * @author Administrator ж�ظ߰汾nginx
	 * @param r
	 * @param time
	 */
	private static void uninstallHNginx(WebResource r, String time) {
		String response = r.path("AppUninstall/Nginx")
				.queryParam("ip", "119.90.140.59")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator ж��ZendGuardLoader
	 * @param r
	 * @param time
	 */
	private static void uninstallZendGuardLoader(WebResource r, String time) {
		String response = r.path("AppUninstall/ZendGuardLoader")
				.queryParam("ip", "119.90.140.59")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator ж��memcached
	 * @param r
	 * @param time
	 */
	private static void uninstallMemcached(WebResource r, String time) {
		String response = r.path("AppUninstall/Memcached")
				.queryParam("ip", "119.90.140.59")
//				.queryParam("softName", "memcached.exe")//����ǰ��memcached64.exe�����º���memcached.exe
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator ж��apace
	 * @param r
	 * @param time
	 */
	private static void uninstallApache(WebResource r, String time) {
		String response = r.path("AppUninstall/Apache")
				.queryParam("ip", "119.90.140.59")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator ж��python
	 * @param r
	 * @param time
	 */
	private static void uninstallPython(WebResource r, String time) {
		String response = r.path("AppUninstall/Python")
				.queryParam("ip", "119.90.140.59")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator ж��IISRewrite
	 * @param r
	 * @param time
	 */
	private static void uninstallIISRewrite(WebResource r, String time) {
		String response = r.path("AppUninstall/IISRewrite")
				.queryParam("ip", "119.90.140.59")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator ж��FTP
	 * @param r
	 * @param time
	 */
	private static void uninstallFTP(WebResource r, String time) {
		String response = r.path("AppUninstall/FTP")
				.queryParam("ip", "119.90.140.59")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator ж��SQLServer2008R2
	 * @param r
	 * @param time
	 */
	private static void uninstallSQLServer2008R2(WebResource r, String time) {
		String response = r.path("AppUninstall/SQLServer2008R2")
				.queryParam("ip", "119.90.140.59")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator ж��Oracle11g
	 * @param r
	 * @param time
	 */
	private static void uninstallOracle11g(WebResource r, String time) {
		String response = r.path("AppUninstall/Oracle11g")
				.queryParam("ip", "119.90.140.59")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator ����tomcat
	 * @param r
	 * @param time
	 */
	private static void updateTomcat(WebResource r, String time) {
		String response = r.path("AppUpdate/Tomcat")
				.queryParam("ip", "119.90.140.59")
				.queryParam("updatePath", "C:")
				.queryParam("jdkPath", "C:/Program Files/Java/jdk1.7.0_45")
				.queryParam("version", "")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator ����mysql
	 * @param r
	 * @param time
	 */
	private static void updateMySql(WebResource r, String time) {
		String response = r.path("AppUpdate/MySql")
				.queryParam("ip", "119.90.140.59")
				.queryParam("updatePath", "C:")
				.queryParam("rootPswd", "repace")
				.queryParam("version", "")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator ����nginx
	 * @param r
	 * @param time
	 */
	private static void updateNginx(WebResource r, String time) {
		String response = r.path("AppUpdate/Nginx")
				.queryParam("ip", "119.90.140.59")
				.queryParam("updatePath", "C:")
				.queryParam("version", "")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator ����ZendGuardLoader
	 * @param r
	 * @param time
	 */
	private static void updateZendGuardLoader(WebResource r, String time) {
		String response = r.path("AppUpdate/ZendGuardLoader")
				.queryParam("ip", "119.90.140.59")
				.queryParam("phpPath", "C:/php")
				.queryParam("version", "")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator ����memcached
	 * @param r
	 * @param time
	 */
	private static void updateMemcached(WebResource r, String time) {
		String response = r.path("AppUpdate/Memcached")
				.queryParam("ip", "119.90.140.59")
				.queryParam("updatePath", "C:")
//				.queryParam("unistallName", "memcached64.exe")
				.queryParam("version", "")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator ����apache
	 * @param r
	 * @param time
	 */
	private static void updateApache(WebResource r, String time) {
		String response = r.path("AppUpdate/Apache")
				.queryParam("ip", "119.90.140.59")
				.queryParam("updatePath", "C:")
				.queryParam("emailAddress", "123@123.com")
				.queryParam("version", "")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator ����python
	 * @param r
	 * @param time
	 */
	private static void updatePython(WebResource r, String time) {
		String response = r.path("AppUpdate/Python")
				.queryParam("ip", "119.90.140.59")
				.queryParam("updatePath","C:") 
				.queryParam("version", "")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator ����FTP
	 * @param r
	 * @param time
	 */
	private static void updateFTP(WebResource r, String time) {
		String response = r.path("AppUpdate/FTP")
				.queryParam("ip", "119.90.140.59")
				.queryParam("updatePath", "C:")
				.queryParam("version", "")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator tomcat��������
	 * @param r
	 * @param time
	 */
	private static void TomcatParam(WebResource r, String time) {
		String response = r.path("AppParamConfiguration/Tomcat")
				.queryParam("ip", "119.90.140.59")
				.queryParam("paramName", "Port")
				.queryParam("paramValue", "8080")
				.queryParam("cfgFilePath", "C:/Tomcat/apache-tomcat-6.0.41")//�˴��汾��
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator apache��������
	 * @param r
	 * @param time
	 */
	private static void ApacheParam(WebResource r, String time) {
		String response = r.path("AppParamConfiguration/Apache")
				.queryParam("ip", "119.90.140.59")
				.queryParam("paramName", "Listen")
				.queryParam("paramValue", "90")
				.queryParam("cfgFilePath", "C:/Apache")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator mysql��������
	 * @param r
	 * @param time
	 */
	private static void MysqlParam(WebResource r, String time) {
		String response = r.path("AppParamConfiguration/MySql")
				.queryParam("ip", "119.90.140.59")
				.queryParam("paramName", "port")
				.queryParam("paramValue", "5413")
				.queryParam("cfgFilePath", "C:/Mysql/mysql-5.7.4-m14-winx64")//�˴��汾��mysql-5.7.4-m11-winx64
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator nginx��������
	 * @param r
	 * @param time
	 */
	private static void NginxParam(WebResource r, String time) {
		String response = r.path("AppParamConfiguration/Nginx")
				.queryParam("ip", "119.90.140.59")
				.queryParam("paramName", "listen")
				.queryParam("paramValue", "5555")
				.queryParam("cfgFilePath", "C:/Nginx/nginx-1.2.8")//�˴��汾�Ű����޸�
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator ZendGuardLoader��������
	 * @param r
	 * @param time
	 */
	private static void ZendGuardLoaderParam(WebResource r, String time) {
		String response = r.path("AppParamConfiguration/ZendGuardLoader")
				.queryParam("ip", "119.90.140.59")
				.queryParam("paramName", "zend_loader.enable")
				.queryParam("paramValue", "0")
				.queryParam("cfgFilePath", "C:/php")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator ���tomcat���ò���
	 * @param r
	 * @param time
	 */
	private static void getParamTomcat(WebResource r, String time) {
		String response = r.path("GetAppParamConfig/Tomcat")
				.queryParam("ip", "119.90.140.59")
				.queryParam("cfgFilePath", "C:/Tomcat/apache-tomcat-7.0.57")
				.queryParam("paramName", "Port")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator ���mysql���ò���
	 * @param r
	 * @param time
	 */
	private static void getParamMySql(WebResource r, String time) {
		String response = r.path("GetAppParamConfig/MySql")
				.queryParam("ip", "119.90.140.59")
				.queryParam("cfgFilePath", "C:/Mysql/mysql-5.7.4-m14-winx64")//�˴��汾��mysql-5.7.1-m11-winx64
				.queryParam("paramName", "port")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator ���nginx���ò���
	 * @param r
	 * @param time
	 */
	private static void getParamNginx(WebResource r, String time) {
		String response = r.path("GetAppParamConfig/Nginx")
				.queryParam("ip", "119.90.140.59")
				.queryParam("cfgFilePath", "C:/Nginx/nginx-1.2.8")//�˴��汾�Ű����޸�
				.queryParam("paramName", "listen")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator ���ZendGuardLoader���ò���
	 * @param r
	 * @param time
	 */
	private static void getParamZendGuardLoader(WebResource r, String time) {
		String response = r.path("GetAppParamConfig/ZendGuardLoader")
				.queryParam("ip", "119.90.140.59")
				.queryParam("paramName", "zend_loader.enable")//zend_extension
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator ���apache���ò���
	 * @param r
	 * @param time
	 */
	private static void getParamApache(WebResource r, String time) {
		String response = r.path("GetAppParamConfig/Apache")
				.queryParam("ip", "119.90.140.59")
				.queryParam("cfgFilePath", "C://Apache")
				.queryParam("paramName", "Listen")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator �����ִ�нű�
	 * @param r
	 * @param FilePath
	 * @param time
	 */
	public static void vmScript(WebResource r, String time, String FilePath) {
		final File fileToUpload = new File(FilePath);
		FormDataMultiPart multiPart = new FormDataMultiPart();
		String ip = "119.90.140.59";
		if (fileToUpload != null) {
			multiPart = ((FormDataMultiPart) multiPart
					.bodyPart(new FileDataBodyPart("upload", fileToUpload,
							MediaType.APPLICATION_OCTET_STREAM_TYPE)));
		}

		ClientResponse response = r.path("vmScript/upload")
				.queryParam("ip", ip)
				.type(MediaType.MULTIPART_FORM_DATA_TYPE)
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time)
				.post(ClientResponse.class, multiPart);
		System.out.println(response.getEntity(JSONObject.class));		
	}


	/**
	 * @author Administrator ���Ȳ�ѯ
	 * @param r
	 * @param FilePath
	 * @param time
	 * @param opID
	 * @param isUpdate
	 */
//	public static void QueryProgress(WebResource r, String time,
//			String FilePath, String opID, String isUpdate) {
//		JSONObject response = r.path("ProgressQuery/progress")
//				.queryParam("opID", opID)
//				.queryParam("isUpdate", isUpdate)
//				.header("password", "EDGM@MAMABDACFDLLG")
//				.header("time", time).type(MediaType.APPLICATION_JSON)
//				.get(JSONObject.class);
//		try {
//			System.out.println(response.get("status"));
//			int category = (Integer) response.get("category");
//			if (category == 1) {
//				try {
//					writeString((String) response.get("detailInfo"), FilePath);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//
//			} else {
//				System.out.println(response.get("detailInfo"));
//			}
//
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//	}
	public static void QueryProgress(WebResource r, String time,
			String FilePath, String opID, String isUpdate) {
		JSONObject response = r.path("ProgressQuery/progress")
				.queryParam("opID", opID).queryParam("isUpdate", isUpdate)
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.get(JSONObject.class);
		try {
//			System.out.println(response.get("status"));
			int category = (Integer) response.get("category");
			if (category == 1) {
				try {
					writeString((String) response.get("detailInfo"), FilePath);
				} catch (IOException e) {
					e.printStackTrace();
				}

			} else {
				// System.out.println(response.get("detailInfo"));
				System.out.println(response);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}


	public static void writeString(String data, String filePath)
			throws IOException {
		FileOutputStream outStream = new FileOutputStream(filePath);
		outStream.write(data.getBytes("ISO-8859-1"));
		outStream.close();
	}

	/**
	 * @author Administrator ���������
	 * @param r
	 * @param time
	 */
	public static void reboot(WebResource r, String time) {
		String response = r.path("VMReboot/reboot")
				.queryParam("ip", "119.90.140.59")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator ������������--�޸�����
	 * @param r
	 * @param time
	 */
	public static void changePWD(WebResource r, String time) {
		String response = r.path("OSBase/vmSysPwd")
				.queryParam("ip", "119.90.140.59")
				.queryParam("userName", "administrator")
				.queryParam("passwd", "5414")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.put(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator ������������--����/�رշ���
	 * @param r
	 * @param time
	 */
	public static void startOrStopService(WebResource r, String time) {
		String response = r.path("OSBase/sysServiceConfig")
				.queryParam("ip", "119.90.140.59")
				.queryParam("serviceName", "TapiSrv")
				.queryParam("operation", "start")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.put(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator ������������--�޸İ�ȫ����
	 * @param r
	 * @param time
	 */
	public static void changeSecRules(WebResource r, String time) {
		String response = r.path("OSBase/secRule")
				.queryParam("ip", "119.90.140.59")
				.queryParam("policyName", "hpPolicy")
				.queryParam("protocol", "TCP")
				.queryParam("port", "1121")
				.queryParam("addSecIP", "119.90.140.60")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.put(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator ������������--�޸�IP��ַ
	 * @param r
	 * @param time
	 */
	public static void changeIP(WebResource r, String time) {
		String response = r.path("OSBase/IPAdd")
				.queryParam("ip", "119.90.140.59")
				.queryParam("mac", "A0E420B0E3D1")
				.queryParam("changeToIP", "119.90.140.59")
				.queryParam("mask", "255.255.254.0")
				.queryParam("gateway", "119.90.140.1")//����
				.queryParam("dns", "219.141.140.10")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.put(String.class);
		System.out.println(response);
	}

	

	/**
	 * @author Administrator ��ñ�����Ϣ
	 * @param r
	 * @param time
	 */
	public static void getCodeInfo(WebResource r, String time, String codeID) {
		String response = r.path("Code/GetCodeInfo")
				.queryParam("codeID", codeID)
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);

	}
	
	
	/**
	 * @author Administrator ��ȡϵͳ��ǰ���з���״̬
	 * @param r
	 * @param time
	 */
	private static void getSystemServiceState(WebResource r, String time) {
		String response = r.path("OSBase/allSystemServiceState")
				.queryParam("ip", "119.90.140.59")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.get(String.class);
		System.out.println(response);
	}
	/**
	 * @author Administrator ��ȡϵͳ����״̬
	 * @param r
	 * @param time
	 */
	private static void getServiceState(WebResource r, String time) {
		String response = r.path("OSBase/aSystemServiceState")
				.queryParam("ip", "119.90.140.59")
				.queryParam("serviceName", "IP Helper")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.get(String.class);
		System.out.println(response);
	}
	/**
	 * @author Administrator ��ȡϵͳ��ǰ��ȫ����
	 * @param r
	 * @param time
	 */
	private static void getSystemSecRule(WebResource r, String time) {
		String response = r.path("OSBase/allSystemSecRule")
				.queryParam("ip", "119.90.140.59")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.get(String.class);
		System.out.println(response);
	}
	/**
	 * @author Administrator ��ȡϵͳ��ǰIP��Ϣ
	 * @param r
	 * @param time
	 */
	private static void getSystemIP(WebResource r, String time) {
		String response = r.path("OSBase/allSystemIP")
				.queryParam("ip", "119.90.140.59")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.get(String.class);
		System.out.println(response);
	}

	
	private static void installOracle11gLinux(WebResource r, String time) {
		String response = r
				.path("AppInstallation/Oracle11g_Linux")
				.queryParam("ip", "119.90.140.253")
				.queryParam("username", "wenyanqi")
				.queryParam("oraclebase", "/u01/app/oracle")
				.queryParam("oracleinventory", "/u01/app/oraInventory")
				.queryParam("oraclehome", "/u01/app/oracle/produc")
				.queryParam("oracle_sid", "orcl")
				.queryParam("rootPswd", "123456QWq")
				.queryParam("oradata", "/u01/app/oradata")
				.queryParam("version", "")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}
	
	
	
	
	
	
	
	
	
	
	/**
	 * ����IP����
	 */
	/**
	 * @author Administrator ���Ӹ���IP��Ϣ
	 * @param r
	 * @param time
	 */
	private static void addSystemaffiIP(WebResource r, String time) {
		String response = r.path("OSBase/addsystemAffiIP")
				.queryParam("ip", "119.90.140.59")
				.queryParam("mac", "00-16-3E-30-8E-67")
				.queryParam("AffiIP", "192.168.0.100")
				.queryParam("mask", "255.255.254.0")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.get(String.class);
		System.out.println(response);
	}
	/**
	 * @author Administrator ������������--ɾ��affiIP
	 * @param r
	 * @param time
	 */
	public static void deletesystemAffiIP(WebResource r, String time) {
		String response = r.path("OSBase/deletesystemAffiIP")
				.queryParam("ip", "119.90.140.59")
				.queryParam("mac", "00-16-3E-30-8E-67")//A0E420B0E3D1
				.queryParam("AffiIP", "192.168.0.100")/////////////////////////////
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.get(String.class);
		System.out.println(response);
	}
	/**
	 * @author Administrator ������������--�鿴affiIP
	 * @param r
	 * @param time
	 */
	public static void searchsystemAffiIP(WebResource r, String time) {
		String response = r.path("OSBase/searchsystemAffiIP")
				.queryParam("ip", "119.90.140.59")
				.queryParam("mac", "00-16-3E-30-8E-67")//A0E420B0E3D1
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.get(String.class);
		System.out.println(response);
	}
	/**
	 * @author Administrator ������������--�޸�affiIP
	 * @param r
	 * @param time
	 */
	public static void changesystemAffiIP(WebResource r, String time) {
		String response = r.path("OSBase/changesystemAffiIP")
				.queryParam("ip", "119.90.140.59")
				.queryParam("mac", "00-16-3E-30-8E-67")//A0E420B0E3D1
				.queryParam("AffiIP", "192.168.0.93")/////////////////////////////
				.queryParam("NewAffiIP", "192.168.0.101")
				.queryParam("mask", "255.255.254.0")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.get(String.class);
		System.out.println(response);
	}
	
	
	
	
	

}
