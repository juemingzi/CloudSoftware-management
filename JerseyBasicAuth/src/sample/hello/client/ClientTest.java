package sample.hello.client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
		
//		getASystemServiceState(r,time);
//		getSystemSecRule(r,time);
//		getSystemIP(r,time);
		
		
		
		
//		installTomcat(r,time);
//		uninstallTomcat(r,time);
//		updateTomcat(r,time);
		
		//install360(r, time);
		
		
		//installMysql(r,time);
		//updateMySql(r,time);
		//uninstallMysql(r,time);
		
		
		//installNginx(r,time);
		//uninstallLNginx(r,time);
		//uninstallHNginx(r,time);
		//updateNginx(r,time);
		
		//installZendGuardLoader(r,time);
		//ZendGuardLoaderParam(r,time);
		//uninstallZendGuardLoader(r,time);
		//updateZendGuardLoader(r,time);		
		
		
		//installMemcached(r,time);
		//uninstallMemcached(r,time);
		//updateMemcached(r,time);
		
		//installApache(r,time);
		//ApacheParam(r,time);
		//uninstallApache(r,time);
//		updateApache(r,time);
		
	
		
		//installPython(r, time);
		//updatePython(r,time);
		//uninstallPython(r,time);
		
		//uninstallIISRewrite(r, time);
		//installIISRewrite(r, time);
		
		
		//QueryProgress(r,time,"C:\\1.jpg","15","false");
//		getCodeInfo(r,time,"0x0201000");
	
		//installFTP(r, time);		
		//updateFTP(r,time);
		//uninstallFTP(r,time);		
		
		
		//installOracle11g(r,time);
		//uninstallOracle11g(r,time);
		//interfaceInstallOracle11g(r,time);
		
		//installSQLServer2008R2(r,time);
		//interfaceInstallSQLServer2008R2(r,time);
		//uninstallSQLServer2008R2(r,time);
			
		//reboot(r,time);
		// 调用代码
		changePWD(r, time);	
	
		//changeAffiIP(r, time);
		
		//installOracle11gLinux(r,time);
		//uninstallTomcatLinux(r,time);
		//installOracle11gLinux(r,time);
		//ulimit(r,time);
		//changeSecRulesLinux(r,time);
		//changeAffiIPLinux(r,time);
		//changePWDLinux(r,time);
		//startOrStopServiceLinux(r,time);
		//getParamTomcatLinux(r,time);
		//getParamMySqlLinux(r,time);
		//installMysqlLinux(r,time);
		//installTomcatLinux(r,time);
		// getParamApacheLinux(r,time);
		// getParamNginxLinux(r,time);
		// installNginxLinux(r,time);
		// getParamZendGuardLoaderLinux(r,time);
		// getParamFTP(r,time);
		 //vmScriptLinux(r,time,"C:\\Users\\Administrator\\Desktop\\script.sh");
		// updateApacheLinux(r,time);
		// updateFTPLinux(r,time);
		// uninstallFTPLinux(r,time);
		// installFTPLinux(r,time);
		// updatePythonLinux(r,time);
		// installPythonLinux(r,time);
		// uninstallPythonLinux(r,time);
		// updateMemcachedLinux(r,time);
		// installMemcachedLinux(r,time);
		// uninstallMemcachedLinux(r,time);
		// getCodeInfo(r,time,"0x0100202");
		// for(int i=0;i<15;i++) {
		// getCodeInfo(r,time,"0x0100300");
		// try {
		// Thread.sleep(2000);
		// } catch (InterruptedException e) {
		//
		// e.printStackTrace();
		// }
		// }
		// updateZendGuardLoaderLinux(r,time);
		// installZendGuardLoaderLinux(r, time);
		// uninstallZendGuardLoaderLinux(r,time);
		// getCodeInfo(r,time,"0x0100300");
		// installTomcat(r,time);
		// unistallTomcat(r, time);
		// QueryProgress(r,time,"C:\\1.jpg","51","false");
		 //vmScript(r,time,"C:\\script.bat");
		// TomcatParam(r, time);
		// getParamTomcat(r, time);
		// startOrStopService(r, time);
		// installMemcachedLinux(r,time);
	}

	/**
	 * 获取当前时间
	 */
	private static String CurrentTime() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		String time = df.format(now);
		return time;
	}

	/**
	 * @author Administrator 安装tomcat
	 * @param r
	 * @param time
	 */
	private static void installTomcat(WebResource r, String time) {
		String response = r.path("AppInstallation/Tomcat")
				.queryParam("ip", "119.90.140.59")
				.queryParam("installPath", "C:\\software")
				.queryParam("jdkPath", "C:\\Program Files\\Java\\jdk1.7.0_45")
				.queryParam("version", "")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator 安装Mysql
	 * @param r
	 * @param time
	 */
	private static void installMysql(WebResource r, String time) {
		String response = r.path("AppInstallation/MySql")
				.queryParam("ip", "119.90.140.59")
				.queryParam("installPath", "C:")
				.queryParam("rootPswd", "repace")
				.queryParam("version", "")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator 安装nginx
	 * @param r
	 * @param time
	 */
	private static void installNginx(WebResource r, String time) {
		String response = r.path("AppInstallation/Nginx")
				.queryParam("ip", "119.90.140.59")
				.queryParam("installPath", "C:\\")
				.queryParam("version", "")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator 安装ZendGuardLoader
	 * @param r
	 * @param time
	 */
	private static void installZendGuardLoader(WebResource r, String time) {
		String response = r.path("AppInstallation/ZendGuardLoader")
				.queryParam("ip", "119.90.140.59")
				.queryParam("phpPath", "C:\\php")
				.queryParam("version", "")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator 安装Memcached
	 * @param r
	 * @param time
	 */
	private static void installMemcached(WebResource r, String time) {
		String response = r.path("AppInstallation/Memcached")
				.queryParam("ip", "119.90.140.59")
				.queryParam("version", "")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator 安装Apache
	 * @param r
	 * @param time
	 */
	private static void installApache(WebResource r, String time) {
		String response = r.path("AppInstallation/Apache")
				.queryParam("ip", "119.90.140.59")
				.queryParam("installPath", "C:\\Apache1\\")
				.queryParam("emailAddress", "123@123.com")
				.queryParam("version", "")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator 安装Python
	 * @param r
	 * @param time
	 */
	private static void installPython(WebResource r, String time) {
		String response = r.path("AppInstallation/Python")
				.queryParam("ip", "119.90.140.59")
				.queryParam("version", "")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator 安装IISRewrite
	 * @param r
	 * @param time
	 */
	private static void installIISRewrite(WebResource r, String time) {
		String response = r.path("AppInstallation/IISRewrite")
				.queryParam("ip", "119.90.140.59")
				.queryParam("installPath", "C:\\iisRewrite\\")
				.queryParam("version", "")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator 安装FTP
	 * @param r
	 * @param time
	 */
	private static void installFTP(WebResource r, String time) {
		String response = r.path("AppInstallation/FTP")
				.queryParam("ip", "119.90.140.59")
				.queryParam("installPath", "C:\\FileZilla_Server-0_9_45\\")
				.queryParam("version", "")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator 安装SQLServer2008R2
	 * @param r
	 * @param time
	 */
	private static void installSQLServer2008R2(WebResource r, String time) {
		String response = r.path("AppInstallation/SQLServer2008R2")
				.queryParam("ip", "119.90.140.59")
				.queryParam("installPath", "C:\\Sql\\")
				.queryParam("rootPswd", "repace")
				.queryParam("hostName", "CLJAZITTFGVDGF8")
				.queryParam("userName", "Administrator")
				.queryParam("version", "")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator 安装Oracle11g
	 * @param r
	 * @param time
	 */
	private static void installOracle11g(WebResource r, String time) {
		String response = r
				.path("AppInstallation/Oracle11g")
				.queryParam("ip", "119.90.140.59")
				.queryParam("hostname", "CLJAZITTFGVDGF8")
				.queryParam("inventorypath","C:\\Program Files\\Oracle\\Inventory")
				.queryParam("oraclebase", "C:\\app\\wenyanqi")
				.queryParam("oraclehome", "dbhome1")
				.queryParam("rootPswd", "123456QWq")
				.queryParam("version", "")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator 界面安装SQLServer2008R2
	 * @param r
	 * @param time
	 */
	private static void interfaceInstallSQLServer2008R2(WebResource r,
			String time) {
		String response = r.path("AppInstallation/InterfaceSQLServer2008R2")
				.queryParam("ip", "119.90.140.59")
				.queryParam("installPath", "C:\\Sql\\")
				.queryParam("rootPswd", "repace")
				.queryParam("hostName", "CLJAZITTFGVDGF8")
				.queryParam("userName", "Administrator")
				.queryParam("version", "")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator 界面安装Oracle11g
	 * @param r
	 * @param time
	 */
	private static void interfaceInstallOracle11g(WebResource r, String time) {
		String response = r.path("AppInstallation/InterfaceOracle11g")
				.queryParam("ip", "119.90.140.59")
				.queryParam("oraclebase", "C:\\app\\wenyanqi")
				.queryParam("oraclehome", "dbhome1")
				.queryParam("inventorypath", "C:\\app\\wenyanqi\\oradata")
				.queryParam("databasename", "orcl")
				.queryParam("rootPswd", "123456QWq")
				.queryParam("version", "")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator 安装360
	 * @param r
	 * @param time
	 */
	private static void install360(WebResource r, String time) {
		String response = r.path("AppInstallation/360")
				.queryParam("ip", "119.90.140.59")
				.queryParam("installPath", "C:\\360\\")
				.queryParam("version", "")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator 卸载tomcat
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
	 * @author Administrator 卸载mysql
	 * @param r
	 * @param time
	 */
	private static void uninstallMysql(WebResource r, String time) {
		String response = r.path("AppUninstall/MySql")
				.queryParam("ip", "119.90.140.59")
				.queryParam("path", "C:")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator 卸载低版本nginx
	 * @param r
	 * @param time
	 */
	private static void uninstallLNginx(WebResource r, String time) {
		String response = r.path("AppUninstall/Nginx")
				.queryParam("ip", "119.90.140.59")
				.queryParam("path", "C:\\nginx-1.2.8")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}
	
	/**
	 * @author Administrator 卸载高版本nginx
	 * @param r
	 * @param time
	 */
	private static void uninstallHNginx(WebResource r, String time) {
		String response = r.path("AppUninstall/Nginx")
				.queryParam("ip", "119.90.140.59")
				.queryParam("path", "C:\\nginx-1.6.2")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator 卸载ZendGuardLoader
	 * @param r
	 * @param time
	 */
	private static void uninstallZendGuardLoader(WebResource r, String time) {
		String response = r.path("AppUninstall/ZendGuardLoader")
				.queryParam("ip", "119.90.140.59")
				.queryParam("path", "C:\\php")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator 卸载memcached
	 * @param r
	 * @param time
	 */
	private static void uninstallMemcached(WebResource r, String time) {
		String response = r.path("AppUninstall/Memcached")
				.queryParam("ip", "119.90.140.59")
				.queryParam("softName", "memcached64.exe")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator 卸载apace
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
	 * @author Administrator 卸载python
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
	 * @author Administrator 卸载IISRewrite
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
	 * @author Administrator 卸载FTP
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
	 * @author Administrator 卸载SQLServer2008R2
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
	 * @author Administrator 卸载Oracle11g
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
	 * @author Administrator 更新tomcat
	 * @param r
	 * @param time
	 */
	private static void updateTomcat(WebResource r, String time) {
		String response = r.path("AppUpdate/Tomcat")
				.queryParam("ip", "119.90.140.59")
				.queryParam("updatePath", "C:\\software")
				.queryParam("jdkPath", "C:\\Program Files\\Java\\jdk1.8.0_25")
				.queryParam("version", "")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator 更新mysql
	 * @param r
	 * @param time
	 */
	private static void updateMySql(WebResource r, String time) {
		String response = r.path("AppUpdate/MySql")
				.queryParam("ip", "119.90.140.59")
				.queryParam("uninstallPath", "C:")
				.queryParam("updatePath", "C:")
				.queryParam("rootPswd", "repace")
				.queryParam("version", "")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator 更新nginx
	 * @param r
	 * @param time
	 */
	private static void updateNginx(WebResource r, String time) {
		String response = r.path("AppUpdate/Nginx")
				.queryParam("ip", "119.90.140.59")
				.queryParam("updatePath", "C:\\")
				.queryParam("unistallPath", "C:\\nginx-1.2.8")
				.queryParam("version", "")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator 更新ZendGuardLoader
	 * @param r
	 * @param time
	 */
	private static void updateZendGuardLoader(WebResource r, String time) {
		String response = r.path("AppUpdate/ZendGuardLoader")
				.queryParam("ip", "119.90.140.59")
				.queryParam("phpPath", "C:\\php")
				.queryParam("version", "")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator 更新memcached
	 * @param r
	 * @param time
	 */
	private static void updateMemcached(WebResource r, String time) {
		String response = r.path("AppUpdate/Memcached")
				.queryParam("ip", "119.90.140.59")
				.queryParam("unistallName", "memcached64.exe")
				.queryParam("version", "")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator 更新apache
	 * @param r
	 * @param time
	 */
	private static void updateApache(WebResource r, String time) {
		String response = r.path("AppUpdate/Apache")
				.queryParam("ip", "119.90.140.59")
				.queryParam("updatePath", "C:\\Apache24")
				.queryParam("emailAddress", "123@123.com")
				.queryParam("version", "")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator 更新python
	 * @param r
	 * @param time
	 */
	private static void updatePython(WebResource r, String time) {
		String response = r.path("AppUpdate/Python")
				.queryParam("ip", "119.90.140.59")
				.queryParam("version", "")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator 更新FTP
	 * @param r
	 * @param time
	 */
	private static void updateFTP(WebResource r, String time) {
		String response = r.path("AppUpdate/FTP")
				.queryParam("ip", "119.90.140.59")
				.queryParam("updatePath", "C:\\FileZilla_Server-0_9_48")
				.queryParam("version", "")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator tomcat参数配置
	 * @param r
	 * @param time
	 */
	private static void TomcatParam(WebResource r, String time) {
		String response = r.path("AppParamConfiguration/Tomcat")
				.queryParam("ip", "119.90.140.59")
				.queryParam("paramName", "Port")
				.queryParam("paramValue", "8080")
				.queryParam("cfgFilePath", "C:/apache-tomcat-6.0.41")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator apache参数配置
	 * @param r
	 * @param time
	 */
	private static void ApacheParam(WebResource r, String time) {
		String response = r.path("AppParamConfiguration/Apache")
				.queryParam("ip", "119.90.140.59")
				.queryParam("paramName", "Listen")
				.queryParam("paramValue", "90")
				.queryParam("cfgFilePath", "C:/Apache1")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator mysql参数配置
	 * @param r
	 * @param time
	 */
	private static void MysqlParam(WebResource r, String time) {
		String response = r.path("AppParamConfiguration/MySql")
				.queryParam("ip", "119.90.140.59")
				.queryParam("paramName", "port")
				.queryParam("paramValue", "5413")
				.queryParam("cfgFilePath", "C:/mysql5.6.14win64")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator nginx参数配置
	 * @param r
	 * @param time
	 */
	private static void NginxParam(WebResource r, String time) {
		String response = r.path("AppParamConfiguration/Nginx")
				.queryParam("ip", "119.90.140.59")
				.queryParam("paramName", "listen")
				.queryParam("paramValue", "5555")
				.queryParam("cfgFilePath", "C:\\nginx-1.2.8")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator ZendGuardLoader参数配置
	 * @param r
	 * @param time
	 */
	private static void ZendGuardLoaderParam(WebResource r, String time) {
		String response = r.path("AppParamConfiguration/ZendGuardLoader")
				.queryParam("ip", "119.90.140.59")
				.queryParam("paramName", "zend_loader.enable")
				.queryParam("paramValue", "0")
				.queryParam("cfgFilePath", "C:\\php")
				//.queryParam("cfgFilePath", "C:\\windows\\php.ini")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator 获得tomcat配置参数
	 * @param r
	 * @param time
	 */
	private static void getParamTomcat(WebResource r, String time) {
		String response = r.path("GetAppParamConfig/Tomcat")
				.queryParam("ip", "119.90.140.59")
				.queryParam("cfgFilePath", "C://apache-tomcat-6.0.41")
				.queryParam("paramName", "Port")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator 获得mysql配置参数
	 * @param r
	 * @param time
	 */
	private static void getParamMySql(WebResource r, String time) {
		String response = r.path("GetAppParamConfig/MySql")
				.queryParam("ip", "119.90.140.59")
				.queryParam("cfgFilePath", "C://mysql5.6.14win64")
				.queryParam("paramName", "port")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator 获得nginx配置参数
	 * @param r
	 * @param time
	 */
	private static void getParamNginx(WebResource r, String time) {
		String response = r.path("GetAppParamConfig/Nginx")
				.queryParam("ip", "119.90.140.59")
				.queryParam("cfgFilePath", "C://nginx-1.2.8")
				.queryParam("paramName", "listen")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator 获得ZendGuardLoader配置参数
	 * @param r
	 * @param time
	 */
	private static void getParamZendGuardLoader(WebResource r, String time) {
		String response = r.path("GetAppParamConfig/ZendGuardLoader")
				.queryParam("ip", "119.90.140.59")
				.queryParam("paramName", "zend_extension")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator 获得apache配置参数
	 * @param r
	 * @param time
	 */
	private static void getParamApache(WebResource r, String time) {
		String response = r.path("GetAppParamConfig/Apache")
				.queryParam("ip", "119.90.140.59")
				.queryParam("cfgFilePath", "C://httpd")
				.queryParam("paramName", "Listen")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.post(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator 虚拟机执行脚本
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
//		final ClientResponse clientResp = r.path("vmScript/upload")
//				.queryParam("ip", ip)
//				.type(MediaType.MULTIPART_FORM_DATA_TYPE)
//				.header("password", "EDGM@MAMABDACFDLLG")
//				.header("time", time)
//				.post(ClientResponse.class, multiPart);
		ClientResponse response = r.path("vmScript/upload")
				.queryParam("ip", ip)
				.type(MediaType.MULTIPART_FORM_DATA_TYPE)
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time)
				.post(ClientResponse.class, multiPart);
		System.out.println(response.getEntity(JSONObject.class));		
	}

	/**
	 * @author Administrator 进度查询
	 * @param r
	 * @param FilePath
	 * @param time
	 * @param opID
	 * @param isUpdate
	 */
	public static void QueryProgress(WebResource r, String time,
			String FilePath, String opID, String isUpdate) {
		JSONObject response = r.path("ProgressQuery/progress")
				.queryParam("opID", opID)
				.queryParam("isUpdate", isUpdate)
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.get(JSONObject.class);
		try {
			System.out.println(response.get("status"));
			int category = (Integer) response.get("category");
			if (category == 1) {
				try {
					writeString((String) response.get("detailInfo"), FilePath);
				} catch (IOException e) {
					e.printStackTrace();
				}

			} else {
				System.out.println(response.get("detailInfo"));
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
	 * @author Administrator 虚拟机重启
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
	 * @author Administrator 基础环境配置--修改密码
	 * @param r
	 * @param time
	 */
	public static void changePWD(WebResource r, String time) {
		String response = r.path("OSBase/vmSysPwd")
				.queryParam("ip", "119.90.140.59")
				.queryParam("userName", "administrator")
				.queryParam("passwd", "5413")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.put(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator 基础环境配置--启动/关闭服务
	 * @param r
	 * @param time
	 */
	public static void startOrStopService(WebResource r, String time) {
		String response = r.path("OSBase/sysServiceConfig")
				.queryParam("ip", "119.90.140.59")
				.queryParam("serviceName", "TapiSrv")
				.queryParam("operation", "stop")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.put(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator 基础环境配置--修改安全规则
	 * @param r
	 * @param time
	 */
	public static void changeSecRules(WebResource r, String time) {
		String response = r.path("OSBase/secRule")
				.queryParam("ip", "119.90.140.59")
				.queryParam("policyName", "hpPolicy")
				.queryParam("protocol", "TCP").queryParam("port", "1121")
				.queryParam("addSecIP", "119.90.140.60")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.put(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator 基础环境配置--修改IP地址
	 * @param r
	 * @param time
	 */
	public static void changeIP(WebResource r, String time) {
		String response = r.path("OSBase/IPAdd")
				.queryParam("ip", "119.90.140.59")
				.queryParam("mac", "A0E420B0E3D1")
				.queryParam("changeToIP", "119.90.140.59")
				.queryParam("mask", "255.255.254.0")
				.queryParam("gateway", "119.90.140.1")
				.queryParam("dns", "219.141.140.10")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.put(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator 基础环境配置--修改affiIP
	 * @param r
	 * @param time
	 */
	public static void changeAffiIP(WebResource r, String time) {
		String response = r.path("OSBase/affiIPAdd")
				.queryParam("ip", "119.90.140.59")
				.queryParam("mac", "A0E420B0E3D1")
				.queryParam("affiIp", "192.168.0.96")
				.queryParam("affiMask", "255.255.254.0")
				.queryParam("affiGateway", "192.168.0.1")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.put(String.class);
		System.out.println(response);
	}

	/**
	 * @author Administrator 获得编码信息
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
	
	
	

//	public static void installZendGuardLoaderLinux(WebResource r, String time) {
//		String response = r.path("AppInstallation/ZendGuardLoader")
//				.queryParam("ip", "119.90.140.253")
//				.queryParam("phpPath", "/home/php")
//				.header("password", "EDGM@MAMABDACFDLLG")
//				.header("time", time).type(MediaType.APPLICATION_JSON)
//				.post(String.class);
//		System.out.println(response);
//
//	}
//
//	public static void uninstallZendGuardLoaderLinux(WebResource r, String time) {
//		String response = r.path("AppUninstall/ZendGuardLoader")
//				.queryParam("ip", "119.90.140.253")
//				.queryParam("path", "/home/php")
//				.header("password", "EDGM@MAMABDACFDLLG")
//				.header("time", time).type(MediaType.APPLICATION_JSON)
//				.post(String.class);
//		System.out.println(response);
//
//	}
//
//	public static void updateZendGuardLoaderLinux(WebResource r, String time) {
//		String response = r.path("AppUpdate/ZendGuardLoader")
//				.queryParam("ip", "119.90.140.253")
//				.queryParam("phpPath", "/home/php")
//				.header("password", "EDGM@MAMABDACFDLLG")
//				.header("time", time).type(MediaType.APPLICATION_JSON)
//				.post(String.class);
//		System.out.println(response);
//
//	}
//
//	public static void installMemcachedLinux(WebResource r, String time) {
//		String response = r.path("AppInstallation/Memcached_Linux")
//				.queryParam("ip", "119.90.140.253")
//				.queryParam("installPath", "/home")
//				.header("password", "EDGM@MAMABDACFDLLG")
//				.header("time", time).type(MediaType.APPLICATION_JSON)
//				.post(String.class);
//		System.out.println(response);
//
//	}
//
//	public static void uninstallMemcachedLinux(WebResource r, String time) {
//		String response = r.path("AppUninstall/Memcached")
//				.queryParam("ip", "119.90.140.253")
//				.queryParam("softName", "libevent-2.0.21-stable")
//				.header("password", "EDGM@MAMABDACFDLLG")
//				.header("time", time).type(MediaType.APPLICATION_JSON)
//				.post(String.class);
//		System.out.println(response);
//
//	}
//
//	public static void updateMemcachedLinux(WebResource r, String time) {
//		String response = r.path("AppUpdate/Memcached")
//				.queryParam("ip", "119.90.140.253")
//				.queryParam("unistallName", "/home")
//				.header("password", "EDGM@MAMABDACFDLLG")
//				.header("time", time).type(MediaType.APPLICATION_JSON)
//				.post(String.class);
//		System.out.println(response);
//
//	}
//
//	public static void installPythonLinux(WebResource r, String time) {
//		String response = r.path("AppInstallation/Python_Linux")
//				.queryParam("ip", "119.90.140.253")
//				.queryParam("installPath", "/home")
//				.header("password", "EDGM@MAMABDACFDLLG")
//				.header("time", time).type(MediaType.APPLICATION_JSON)
//				.post(String.class);
//		System.out.println(response);
//
//	}
//
//	public static void uninstallPythonLinux(WebResource r, String time) {
//		String response = r.path("AppUninstall/Python_Linux")
//				.queryParam("ip", "119.90.140.253")
//				.queryParam("path", "/home/Python-2.7.3")
//				.header("password", "EDGM@MAMABDACFDLLG")
//				.header("time", time).type(MediaType.APPLICATION_JSON)
//				.post(String.class);
//		System.out.println(response);
//
//	}
//
//	public static void updatePythonLinux(WebResource r, String time) {
//		String response = r.path("AppUpdate/Python_Linux")
//				.queryParam("ip", "119.90.140.253")
//				.queryParam("unistallPath", "/home/Python-2.7.3")
//				.queryParam("updatePath", "/home")
//				.header("password", "EDGM@MAMABDACFDLLG")
//				.header("time", time).type(MediaType.APPLICATION_JSON)
//				.post(String.class);
//		System.out.println(response);
//
//	}
//
//	public static void installFTPLinux(WebResource r, String time) {
//		String response = r.path("AppInstallation/FTP_Linux")
//				.queryParam("ip", "119.90.140.253")
//				.header("password", "EDGM@MAMABDACFDLLG")
//				.header("time", time).type(MediaType.APPLICATION_JSON)
//				.post(String.class);
//		System.out.println(response);
//
//	}
//
//	public static void uninstallFTPLinux(WebResource r, String time) {
//		String response = r.path("AppUninstall/FTP_Linux")
//				.queryParam("ip", "119.90.140.253")
//				.header("password", "EDGM@MAMABDACFDLLG")
//				.header("time", time).type(MediaType.APPLICATION_JSON)
//				.post(String.class);
//		System.out.println(response);
//
//	}
//
//	public static void updateFTPLinux(WebResource r, String time) {
//		String response = r.path("AppUpdate/FTP_Linux")
//				.queryParam("ip", "119.90.140.253")
//				.header("password", "EDGM@MAMABDACFDLLG")
//				.header("time", time).type(MediaType.APPLICATION_JSON)
//				.post(String.class);
//		System.out.println(response);
//
//	}
//
//	public static void updateApacheLinux(WebResource r, String time) {
//		String response = r.path("AppUpdate/Apache_Linux")
//				.queryParam("ip", "119.90.140.253")
//				.queryParam("unistallPath", "/home")
//				.queryParam("updatePath", "/home")
//				.queryParam("emailAddress", "123@123.com")
//				.header("password", "EDGM@MAMABDACFDLLG")
//				.header("time", time).type(MediaType.APPLICATION_JSON)
//				.post(String.class);
//		System.out.println(response);
//
//	}
//
	public static void vmScriptLinux(WebResource r, String time, String FilePath) {
		final File fileToUpload = new File(FilePath);
		FormDataMultiPart multiPart = new FormDataMultiPart();
		String ip = "119.90.140.253";
		if (fileToUpload != null) {
			multiPart = ((FormDataMultiPart) multiPart
					.bodyPart(new FileDataBodyPart("upload", fileToUpload,
							MediaType.APPLICATION_OCTET_STREAM_TYPE)));
		}
		final ClientResponse clientResp = r.path("vmScript/upload")
				.queryParam("ip", ip).type(MediaType.MULTIPART_FORM_DATA_TYPE)
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).post(ClientResponse.class, multiPart);
		System.out.println(clientResp.getEntity(JSONObject.class));
	}
//
//	private static void getParamFTP(WebResource r, String time) {
//		String response = r.path("GetAppParamConfig/FTP")
//				.queryParam("ip", "119.90.140.253")
//				.queryParam("cfgFilePath", "/etc/vsftpd")
//				.queryParam("paramName", "listen")
//				.header("password", "EDGM@MAMABDACFDLLG")
//				.header("time", time).type(MediaType.APPLICATION_JSON)
//				.post(String.class);
//		System.out.println(response);
//	}
//
//	private static void getParamZendGuardLoaderLinux(WebResource r, String time) {
//		String response = r.path("GetAppParamConfig/ZendGuardLoader_Linux")
//				.queryParam("ip", "119.90.140.253")
//				.queryParam("path", "/home/php")
//				.queryParam("paramName", "zend_extension")
//				.header("password", "EDGM@MAMABDACFDLLG")
//				.header("time", time).type(MediaType.APPLICATION_JSON)
//				.post(String.class);
//		System.out.println(response);
//	}
//
//	private static void installNginxLinux(WebResource r, String time) {
//		String response = r.path("AppInstallation/Nginx")
//				.queryParam("ip", "119.90.140.253")
//				.queryParam("installPath", "/home/nginx")
//				.header("password", "EDGM@MAMABDACFDLLG")
//				.header("time", time).type(MediaType.APPLICATION_JSON)
//				.post(String.class);
//		System.out.println(response);
//
//	}
//
//	private static void getParamNginxLinux(WebResource r, String time) {
//		String response = r.path("GetAppParamConfig/Nginx")
//				.queryParam("ip", "119.90.140.253")
//				.queryParam("cfgFilePath", "/home/nginx")
//				.queryParam("paramName", "listen")
//				.header("password", "EDGM@MAMABDACFDLLG")
//				.header("time", time).type(MediaType.APPLICATION_JSON)
//				.post(String.class);
//		System.out.println(response);
//	}
//
//	private static void getParamApacheLinux(WebResource r, String time) {
//		String response = r.path("GetAppParamConfig/Apache")
//				.queryParam("ip", "119.90.140.253")
//				.queryParam("cfgFilePath", "/home/httpd")
//				.queryParam("paramName", "Listen")
//				.header("password", "EDGM@MAMABDACFDLLG")
//				.header("time", time).type(MediaType.APPLICATION_JSON)
//				.post(String.class);
//		System.out.println(response);
//	}
//
//	private static void installTomcatLinux(WebResource r, String time) {
//		String response = r.path("AppInstallation/Tomcat")
//				.queryParam("ip", "119.90.140.253")
//				.queryParam("installPath", "/home")
//				.queryParam("jdkPath", "/usr/java/jdk1.7.0_75")
//				.header("password", "EDGM@MAMABDACFDLLG")
//				.header("time",time)
//				.type(MediaType.APPLICATION_JSON).post(String.class);
//		System.out.println(response);
//
//	}
//	
//	private static void uninstallTomcatLinux(WebResource r, String time) {
//		String response = r.path("AppUninstall/Tomcat_Linux")
//				.queryParam("ip", "119.90.140.253")
//				.queryParam("path", "/home/apache-tomcat-6.0.41")
//				.header("password", "EDGM@MAMABDACFDLLG")
//				.header("time", time).type(MediaType.APPLICATION_JSON)
//				.post(String.class);
//		System.out.println(response);
//	}
//	
//	
//	
//	private static void installMysqlLinux(WebResource r, String time) {
//		String  response = r.path("AppInstallation/MySql_Linux")
//		.queryParam("ip", "119.90.140.253")
//		.queryParam("rootPswd", "repace")
//        .header("password", "EDGM@MAMABDACFDLLG")
//        .header("time", time)
//       .type(MediaType.APPLICATION_JSON)
//       .post(String.class);
//		System.out.println(response);
//	}
//	
//
//	
//	
//	private static void getParamTomcatLinux(WebResource r, String time) {
//		String response = r.path("GetAppParamConfig/Tomcat")
//				.queryParam("ip", "119.90.140.253")
//				.queryParam("cfgFilePath", "/home/apache-tomcat-6.0.41")
//				.queryParam("paramName", "Port")
//				.header("password", "EDGM@MAMABDACFDLLG")
//				.header("time", time).type(MediaType.APPLICATION_JSON)
//				.post(String.class);
//		System.out.println(response);
//	}
//	
//	
//	private static void getParamMySqlLinux(WebResource r, String time) {
//		String response = r.path("GetAppParamConfig/MySql_Linux")
//				.queryParam("ip", "119.90.140.253")
//				.queryParam("paramName", "user")
//				.header("password", "EDGM@MAMABDACFDLLG")
//				.header("time", time).type(MediaType.APPLICATION_JSON)
//				.post(String.class);
//		System.out.println(response);
//	}
//
//	
//	private static void installOracle11gLinux(WebResource r, String time) {
//		String response = r.path("AppInstallation/Oracle11g_Linux")
//				.queryParam("ip", "119.90.140.253")
//				.queryParam("username", "wenyanqi")
//				//		String oraclebase="/u01/app/oracle";
////		String oracleinventory="/u01/app/oraInventory";
////		String oraclehome="/u01/app/oracle/product";
////		String oracle_sid = "orcl";
//				.queryParam("oraclebase", "/u01/app/oracle")
//				.queryParam("oracleinventory", "/u01/app/oraInventory")
//				.queryParam("oraclehome", "/u01/app/oracle/produc")
//				.queryParam("oracle_sid", "orcl")
//				.queryParam("rootPswd", "123456QWq").
//				queryParam("oradata", "/u01/app/oradata")
//				.header("password", "EDGM@MAMABDACFDLLG")
//				.header("time", time).type(MediaType.APPLICATION_JSON)
//				.post(String.class);
//		System.out.println(response);
//	}
//	
//	/**
//	 * 基础环境配置Linux
//	 */
//	private static void changePWDLinux(WebResource r, String time) {
//		
//		String response = r.path("OSBase/vmSysPwd")
//				.queryParam("ip", "119.90.140.253")
//				.queryParam("userName", "root")
//				.queryParam("passwd", "5413")
//				.header("password", "EDGM@MAMABDACFDLLG")
//				.header("time", time).type(MediaType.APPLICATION_JSON)
//				.put(String.class);
//		System.out.println(response);
//	}
//	
//	
//	public static void startOrStopServiceLinux(WebResource r, String time) {
//		String response = r.path("OSBase/sysServiceConfig")
//				.queryParam("ip", "119.90.140.253")
//				.queryParam("serviceName", "auditd")
//				.queryParam("operation", "start")
//				.header("password", "EDGM@MAMABDACFDLLG")
//				.header("time", time).type(MediaType.APPLICATION_JSON)
//				.put(String.class);
//		System.out.println(response);
//	}
//
//	
//	
//	public static void changeSecRulesLinux(WebResource r, String time) {
//		String response = r.path("OSBase/secRule_Linux")
//				.queryParam("ip", "119.90.140.253")
//				.queryParam("protocol", "TCP")
//				.queryParam("sourceIP", "119.90.140.60")
//				.queryParam("port", "8888")
//				.header("password", "EDGM@MAMABDACFDLLG")
//				.header("time", time).type(MediaType.APPLICATION_JSON)
//				.put(String.class);
//		System.out.println(response);
//	}
//	
//	
//	
////	public static void changeIPLinux(WebResource r, String time) {
////		String response = r.path("OSBase/IPAdd_Linux")
////				.queryParam("ip", "119.90.140.253")
////				.queryParam("deviceName", "xxx")
////				.queryParam("mask", "255.255.254.0")
////				.queryParam("changeToIP", "119.90.140.253")
////				.header("password", "EDGM@MAMABDACFDLLG")
////				.header("time", time).type(MediaType.APPLICATION_JSON)
////				.post(String.class);
////		System.out.println(response);
////	}
//	
//	public static void changeAffiIPLinux(WebResource r, String time) {
//		String response = r.path("OSBase/affiIPAdd_Linux")
//				.queryParam("ip", "119.90.140.253")
//				.queryParam("deviceName", "eth0:1")
//				.queryParam("mask", "255.255.254.0")
//				.queryParam("changeToIP", "119.90.140.254")
//				.header("password", "EDGM@MAMABDACFDLLG")
//				.header("time", time).type(MediaType.APPLICATION_JSON)
//				.put(String.class);
//		System.out.println(response);
//	}
//	
	public static void ulimit(WebResource r, String time) {
		String response = r.path("OSBase/ulimit_Linux")
				.queryParam("ip", "119.90.140.253")
				.queryParam("ulimit", "65535")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.put(String.class);
		System.out.println(response);
	}
//	
//	
//
//	
//	// public static void installMemcachedLinux(WebResource r, String time) {
//	// String response = r.path("AppInstallation/Memcached_Linux")
//	// .queryParam("ip", "119.90.140.253")
//	// .queryParam("installPath", "/home")
//	// .header("password", "EDGM@MAMABDACFDLLG")
//	// .header("time", time).type(MediaType.APPLICATION_JSON)
//	// .post(String.class);
//	// System.out.println(response);
//	//
//	// }
	
	
	private static void installOracle11gLinux(WebResource r, String time) {
		// String oraclebase="/u01/app/oracle";
		// String oracleinventory="/u01/app/oraInventory";
		// String oraclehome="/u01/app/oracle/product";
		// String oracle_sid = "orcl";
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
	 * @author Administrator 获取系统当前某个服务状态
	 * @param r
	 * @param time
	 */
	private static void getSystemServiceState(WebResource r, String time) {
		String response = r.path("OSBase/linuxSystemServiceState")
				.queryParam("ip", "119.90.140.59")
				.queryParam("serviceName","auditd")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.get(String.class);
		System.out.println(response);
	}
	/**
	 * @author Administrator 获取系统当前所有服务状态
	 * @param r
	 * @param time
	 */
	private static void getASystemServiceState(WebResource r, String time) {
		String response = r.path("OSBase/allSystemServiceState")
				.queryParam("ip", "119.90.140.59")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.get(String.class);
		System.out.println(response);
	}
	/**
	 * @author Administrator 获取系统当前安全规则
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
	 * @author Administrator 获取系统当前IP信息
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
	
	/**
	 * @author Administrator 获取系统当前ulimit
	 * @param r
	 * @param time
	 */
	private static void getSystemUlimit(WebResource r, String time) {
		String response = r.path("OSBase/systemUlimit")
				.queryParam("ip", "119.90.140.59")
				.header("password", "EDGM@MAMABDACFDLLG")
				.header("time", time).type(MediaType.APPLICATION_JSON)
				.get(String.class);
		System.out.println(response);
	}
	
}
