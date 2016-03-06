package sample.hello.client;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
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

public class OSBaseClient1 {
		
	public static void main(String[] args){
		
		
		System.out.println(CurrentTime());
		
		Client c = Client.create();
		WebResource r = c.resource("http://119.90.140.165:8080/JerseyBasicAuth/rest/");
		
		//update(r);
		//postUpload(r);
		//uninstall(r);
		//reboot(r);
		install(r);
	 // getParam(r);
		
	//postQueryProgress_json(r,"E:\\MyEclpse10\\workspace\\JerseyBasicAuth\\pictures\\2\\1.jpg");
		
		
		
		
		
		
		
	}
	/**
	 * 用户获取认证
	 * @param uid 数据库cloudhost的userinfo表的userID字段
	 * @param pwd 数据库cloudhost的userinfo表的pwd字段
	 */
	/*public static void getToken(String uid,String pwd){
		Client c = Client.create();
		WebResource r = c.resource("http://localhost:8080/JerseyBasicAuth/Authentication");
		String response = r
                .queryParam("username", uid)
                .queryParam("pwd", pwd)
                .type(MediaType.APPLICATION_JSON)
                .get(String.class);
		System.out.println("token:"+response);
	}*/
	/**
	 * 使用queryParam发送数据执行修改密码操作
	 * @param r Client WebResource
	 */
	public static void putChangePasswd(WebResource r){
		String password = "123456";
		String time = "";
		password = convertMD5(string2MD5(password));//发送前加密
		time = CurrentTime();//时间戳
		System.out.println("password="+password);
		System.out.println("time = "+time);
		String response = r.path("OSBase/vmSysPwd")
                .queryParam("ip", "192.168.0.202")
                .queryParam("userName", "Administrator")
                .queryParam("passwd", "5413")
                .header("password", password)
                .header("time", time)
                .type(MediaType.APPLICATION_JSON)
                .put(String.class);
		System.out.println(response);
	}
	/**
	 * 使用json对象发送数据执行修改密码操作
	 * @param r Client WebResource
	 */
	public static void putChangePasswd_json(WebResource r){
		JSONObject jo = new JSONObject();
		try {
			jo.put("ip", "192.168.0.202");
			jo.put("userName", "Administrator");
			jo.put("passwd", "5413");
			String response = r.path("OSBase/vmSysPwd_json")
					.entity(jo)
	                .header("Authentication", "aHA6MTIzNDU2OjEz")
	                .type(MediaType.APPLICATION_JSON)
	                .put(String.class);
			System.out.println(response);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void reboot(WebResource r) {
		
		
		String  response = r.path("VMReboot/reboot")
		.queryParam("ip", "119.90.140.59")
        .header("password", "EDGM@MAMABDACFDLLG")
        .header("time", "2015-01-25 11:35:23")
       .type(MediaType.APPLICATION_JSON)
       .post(String.class);
		System.out.println(response);
		
		
	}
	
	
	public static void install(WebResource r) {
		/**
		 * windows
		 */
		
		
		
//		String  response = r.path("AppInstallation/Memcached")
//				.queryParam("ip", "119.90.140.59")
//		        .header("password", "EDGM@MAMABDACFDLLG")
//		        .header("time", "2015-01-24 09:26:27")
//		       .type(MediaType.APPLICATION_JSON)
//		       .post(String.class);
//		System.out.println(response);
		
		
//		String  response = r.path("AppInstallation/InterfaceOracle11g")
//		.queryParam("ip", "119.90.140.59")
//		.queryParam("oraclebase", "C:\\app\\wenyanqi")
//		.queryParam("oraclehome", "dbhome1")
//		.queryParam("inventorypath", "C:\\app\\wenyanqi\\oradata")
//		.queryParam("databasename", "orcl")
//		.queryParam("rootPswd", "123456QWq")
//        .header("password", "EDGM@MAMABDACFDLLG")
//        .header("time", "2015-01-25 20:27:56")
//       .type(MediaType.APPLICATION_JSON)
//       .post(String.class);
		
//		String response = r.path("AppInstallation/Tomcat")
//				.queryParam("ip", "119.90.140.59")
//		        .header("password", "EDGM@MAMABDACFDLLG")
//		        .header("time", "2015-01-24 09:26:27")
//		       .type(MediaType.APPLICATION_JSON)
//		       .post(String.class);
		
//		String  response = r.path("AppInstallation/Memcached")
//		.queryParam("ip", "119.90.140.59")
//        .header("password", "EDGM@MAMABDACFDLLG")
//        .header("time", "2015-01-24 09:26:27")
//       .type(MediaType.APPLICATION_JSON)
//       .post(String.class);
		
//		String  response = r.path("AppInstallation/InterfaceSQLServer2008R2")
//		.queryParam("ip", "119.90.140.59")
//		.queryParam("installPath", "C:\\Sql\\")
//		.queryParam("rootPswd", "repace")
//		.queryParam("hostName", "CLJAZITTFGVDGF8")
//		.queryParam("userName", "Administrator")
//        .header("password", "EDGM@MAMABDACFDLLG")
//        .header("time", "2015-01-26 16:37:07")
//       .type(MediaType.APPLICATION_JSON)
//       .post(String.class);
	
//		System.out.println(response);
		
//		
		/**
		 *linux
		 */
		
//		String  response = r.path("AppInstallation/Tomcat")
//		.queryParam("ip", "119.90.140.68")
//		.queryParam("installPath", "/home")
//		.queryParam("jdkPath", "/usr/java/jdk1.7.0_75")
//        .header("password", "EDGM@MAMABDACFDLLG")
//        .header("time", "2015-01-27 12:22:31")
//       .type(MediaType.APPLICATION_JSON)
//       .post(String.class);	
//		System.out.println(response);
		
//		String  response = r.path("AppInstallation/Apache")
//		.queryParam("ip", "119.90.140.68")
//		.queryParam("installPath", "/home")
//		.queryParam("emailAddress", "123@123.com")
//        .header("password", "EDGM@MAMABDACFDLLG")
//        .header("time", "2015-01-27 21:43:03")
//       .type(MediaType.APPLICATION_JSON)
//       .post(String.class);	
//		System.out.println(response);
		
		
		
//		String  response = r.path("AppInstallation/MySql_Linux")
//		.queryParam("ip", "119.90.140.68")
//		.queryParam("rootPswd", "repace")
//        .header("password", "EDGM@MAMABDACFDLLG")
//        .header("time", "2015-01-26 09:49:22")
//       .type(MediaType.APPLICATION_JSON)
//       .post(String.class);
		
//		String  response = r.path("AppInstallation/Nginx")
//		.queryParam("ip", "119.90.140.68")
//		.queryParam("installPath", "/home/nginx")
//        .header("password", "EDGM@MAMABDACFDLLG")
//        .header("time", "2015-01-27 11:47:51")
//       .type(MediaType.APPLICATION_JSON)
//       .post(String.class);	
//		System.out.println(response);
		
		String  response = r.path("AppInstallation/Nginx")
		.queryParam("ip", "119.90.140.68")
		.queryParam("installPath", "/home/nginx")
        .header("password", "EDGM@MAMABDACFDLLG")
        .header("time", "2015-01-27 11:47:51")
       .type(MediaType.APPLICATION_JSON)
       .post(String.class);	
		System.out.println(response);
		
	//	System.out.println(response);
		

	
	}
	
	
	
	public static void uninstall(WebResource r) {
		
//		String  response = r.path("AppUninstall/Apache")
//		.queryParam("ip", "119.90.140.59")
//        .header("password", "EDGM@MAMABDACFDLLG")
//        .header("time", "2015-01-25 12:05:32")
//       .type(MediaType.APPLICATION_JSON)
//       .post(String.class);
//		System.out.println(response);
		
		
//		String  response = r.path("AppUninstall/Python")
//		.queryParam("ip", "119.90.140.59")
//        .header("password", "EDGM@MAMABDACFDLLG")
//        .header("time", "2015-01-25 11:40:17")
//       .type(MediaType.APPLICATION_JSON)
//       .post(String.class);
//		System.out.println(response);
		
//			String  response = r.path("AppUninstall/ZendGuardLoader")
//					.queryParam("ip", "119.90.140.59")
//					.queryParam("path", "C:\\php")
//	                .header("password", "EDGM@MAMABDACFDLLG")
//	                .header("time", "2015-01-23 17:57:15")
//	               .type(MediaType.APPLICATION_JSON)
//	               .post(String.class);
		
//		String  response = r.path("AppUninstall/Memcached")
//		.queryParam("ip", "119.90.140.59")
//		.queryParam("softName", "memcached64.exe")
//        .header("password", "EDGM@MAMABDACFDLLG")
//        .header("time", "2015-01-23 21:47:36")
//       .type(MediaType.APPLICATION_JSON)
//       .post(String.class);
		
//		String  response = r.path("AppUninstall/IISRewrite")
//		.queryParam("ip", "119.90.140.59")
//        .header("password", "EDGM@MAMABDACFDLLG")
//        .header("time", "2015-01-23 22:26:01")
//       .type(MediaType.APPLICATION_JSON)
//       .post(String.class);
		
		
//		String  response = r.path("AppUninstall/Oracle11g")
//		.queryParam("ip", "119.90.140.59")
//        .header("password", "EDGM@MAMABDACFDLLG")
//        .header("time", "2015-01-25 15:51:07")
//       .type(MediaType.APPLICATION_JSON)
//       .post(String.class);
//		System.out.println(response);
//		
//		String  response = r.path("AppUninstall/SQLServer2008R2")
//		.queryParam("ip", "119.90.140.59")
//        .header("password", "EDGM@MAMABDACFDLLG")
//        .header("time", "2015-01-26 15:07:22")
//       .type(MediaType.APPLICATION_JSON)
//       .post(String.class);
		
//		String  response = r.path("AppUninstall/MySql_Linux")
//		.queryParam("ip", "119.90.140.68")
//        .header("password", "EDGM@MAMABDACFDLLG")
//        .header("time", "2015-01-26 09:58:51")
//       .type(MediaType.APPLICATION_JSON)
//       .post(String.class);
		
		
		/**
		 * Linux
		 */
//		String  response = r.path("AppUninstall/Tomcat")
//		.queryParam("ip", "119.90.140.68")
//        .header("password", "EDGM@MAMABDACFDLLG")
//        .header("time", "2015-01-26 17:29:19")
//       .type(MediaType.APPLICATION_JSON)
//       .post(String.class);
		
		
		String  response = r.path("AppUninstall/Apache_Linux")
		.queryParam("ip", "119.90.140.68")
		.queryParam("path", "/home/httpd")
        .header("password", "EDGM@MAMABDACFDLLG")
        .header("time", "2015-01-27 20:40:41")
       .type(MediaType.APPLICATION_JSON)
       .post(String.class);
		
		
//		String  response = r.path("AppUninstall/Nginx")
//		.queryParam("ip", "119.90.140.68")
//		.queryParam("path", "/home/nginx")
//        .header("password", "EDGM@MAMABDACFDLLG")
//        .header("time", "2015-01-27 11:17:13")
//       .type(MediaType.APPLICATION_JSON)
//       .post(String.class);
		
		System.out.println(response);
	
	}
	
	
	public static void getParam(WebResource r) {
		
		String  response = r.path("GetAppParamConfig/Tomcat")
				.queryParam("ip", "119.90.140.59")
				.queryParam("cfgFilePath", "C://apache-tomcat-6.0.41")
				.queryParam("paramName", "Port")
                .header("password", "EDGM@MAMABDACFDLLG")
                .header("time", "2015-01-25 21:24:56")
               .type(MediaType.APPLICATION_JSON)
               .post(String.class);
		
//		String  response = r.path("GetAppParamConfig/MySql")
//		.queryParam("ip", "119.90.140.59")
//		.queryParam("cfgFilePath", "C://mysql5.6.14win64")
//		.queryParam("paramName", "port")
//        .header("password", "EDGM@MAMABDACFDLLG")
//        .header("time", "2015-01-23 15:24:39")
//       .type(MediaType.APPLICATION_JSON)
//       .post(String.class);
		
//		String  response = r.path("GetAppParamConfig/Nginx")
//		.queryParam("ip", "119.90.140.59")
//		.queryParam("cfgFilePath", "C://nginx-1.2.8")
//		.queryParam("paramName", "listen")
//        .header("password", "EDGM@MAMABDACFDLLG")
//        .header("time", "2015-01-23 15:24:39")
//       .type(MediaType.APPLICATION_JSON)
//       .post(String.class);
		
//		String  response = r.path("GetAppParamConfig/ZendGuardLoader")
//		.queryParam("ip", "119.90.140.59")
//		.queryParam("paramName", "zend_extension")
//        .header("password", "EDGM@MAMABDACFDLLG")
//        .header("time", "2015-01-23 15:24:39")
//       .type(MediaType.APPLICATION_JSON)
//       .post(String.class);
		
//		String  response = r.path("GetAppParamConfig/Apache")
//		.queryParam("ip", "119.90.140.59")
//		.queryParam("cfgFilePath", "C://httpd")
//		.queryParam("paramName", "Listen")
//        .header("password", "EDGM@MAMABDACFDLLG")
//        .header("time", "2015-01-23 15:24:39")
//       .type(MediaType.APPLICATION_JSON)
//       .post(String.class);
		
		
		System.out.println(response);
		
	}
	
	public static void update(WebResource r) {
//		String  response = r.path("AppUpdate/Tomcat")
//				.queryParam("ip", "119.90.140.59")
//				.queryParam("updatePath", "C:\\")
//				.queryParam("jdkPath", "C:\\Program Files\\Java\\jdk1.8.0_25")
//		        .header("password", "EDGM@MAMABDACFDLLG")
//		        .header("time", "2015-01-23 17:34:13")
//		       .type(MediaType.APPLICATION_JSON)
//		       .post(String.class);
		
//		String  response = r.path("AppUpdate/MySql")
//		.queryParam("ip", "119.90.140.59")
//		.queryParam("updatePath", "C:")
//		.queryParam("rootPswd", "repace")
//        .header("password", "EDGM@MAMABDACFDLLG")
//        .header("time", "2015-01-23 17:34:13")
//       .type(MediaType.APPLICATION_JSON)
//       .post(String.class);
		
//		String  response = r.path("AppUpdate/Nginx")
//		.queryParam("ip", "119.90.140.59")
//		.queryParam("updatePath", "C:\\")
//		.queryParam("unistallPath", "C:\\nginx-1.2.8")
//        .header("password", "EDGM@MAMABDACFDLLG")
//        .header("time", "2015-01-23 20:26:10")
//       .type(MediaType.APPLICATION_JSON)
//       .post(String.class);
		
//		String  response = r.path("AppUpdate/ZendGuardLoader")
//		.queryParam("ip", "119.90.140.59")
//		.queryParam("phpPath", "C:\\php")
//        .header("password", "EDGM@MAMABDACFDLLG")
//        .header("time", "2015-01-23 20:39:31")
//       .type(MediaType.APPLICATION_JSON)
//       .post(String.class);
		
//		String  response = r.path("AppUpdate/Apache")
//		.queryParam("ip", "119.90.140.59")
//		.queryParam("updatePath", "C:\\httpd")
//        .header("password", "EDGM@MAMABDACFDLLG")
//        .header("time", "2015-01-23 20:39:31")
//       .type(MediaType.APPLICATION_JSON)
//       .post(String.class);
		
//		String  response = r.path("AppUpdate/Python")
//		.queryParam("ip", "119.90.140.59")
//        .header("password", "EDGM@MAMABDACFDLLG")
//        .header("time", "2015-01-23 21:08:31")
//       .type(MediaType.APPLICATION_JSON)
//       .post(String.class);
		
//		String  response = r.path("AppUpdate/FTP")
//		.queryParam("ip", "119.90.140.59")
//		.queryParam("updatePath", "C:\\FileZilla_Server\\")
//        .header("password", "EDGM@MAMABDACFDLLG")
//        .header("time", "2015-01-23 21:08:31")
//       .type(MediaType.APPLICATION_JSON)
//       .post(String.class);
		
//		String  response = r.path("AppUpdate/Memcached")
//		.queryParam("ip", "119.90.140.59")
//		.queryParam("unistallName", "memcached64.exe")
//        .header("password", "EDGM@MAMABDACFDLLG")
//        .header("time", "2015-01-24 09:29:16")
//       .type(MediaType.APPLICATION_JSON)
//       .post(String.class);
		
		
//		String  response = r.path("AppUpdate/MySql_Linux")
//		.queryParam("ip", "119.90.140.68")
//        .header("password", "EDGM@MAMABDACFDLLG")
//        .header("time", "2015-01-26 16:27:24")
//       .type(MediaType.APPLICATION_JSON)
//       .post(String.class);
		
//		String  response = r.path("AppUpdate/Tomcat")
//		.queryParam("ip", "119.90.140.68")
//		.queryParam("unistallPath", "/home/apache-tomcat-6.0.41")
//		.queryParam("updatePath", "/home")
//		.queryParam("jdkPath", "/usr/java/jdk1.7.0_75")
//        .header("password", "EDGM@MAMABDACFDLLG")
//        .header("time", "2015-01-27 19:50:51")
//       .type(MediaType.APPLICATION_JSON)
//       .post(String.class);
//		
//		String  response = r.path("AppUpdate/Nginx_Linux")
//		.queryParam("ip", "119.90.140.68")
//		.queryParam("updatePath", "/home/nginx")
//        .header("password", "EDGM@MAMABDACFDLLG")
//        .header("time", "2015-01-27 11:44:02")
//       .type(MediaType.APPLICATION_JSON)
//       .post(String.class);
		
		String  response = r.path("AppUpdate/Apache_Linux")
		.queryParam("ip", "119.90.140.68")
		.queryParam("unistallPath", "/home")
		.queryParam("updatePath", "/home")
		.queryParam("emailAddress", "123@123.com")
        .header("password", "EDGM@MAMABDACFDLLG")
        .header("time", "2015-01-27 21:52:48")
       .type(MediaType.APPLICATION_JSON)
       .post(String.class);
		
		System.out.println(response);	
	}
	
	
	
	/**@author Administrator
	 * 2015-1-21新增 查询进度，若是图片则写入FilePath
	 * @param r
	 */
	public static void postQueryProgress_json(WebResource r,String FilePath){
		//String time = CurrentTime();
		JSONObject  response = r.path("ProgressQuery/progress")
					 .queryParam("opID", "126")
					  .queryParam("isUpdate", "false")
	                 .header("password", "EDGM@MAMABDACFDLLG")
	                 .header("time", "2015-01-25 09:55:00")
	                .type(MediaType.APPLICATION_JSON)
	                .get(JSONObject.class);
			try {
				System.out.println(response.get("status"));
				int category = (Integer)response.get("category");
				if(category==1) {
					//是图片
					try {
						writeString((String)response.get("detailInfo"),FilePath);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}else {
					System.out.println(response.get("detailInfo"));
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println(response.get("status"));
		//	System.out.println(response.get("detailInfo"));
			//System.out.println(response);
		
	}
	
	/**2015-1-21新增
	 * 将图片字符串写入文件中
	 * @param data
	 * @param filePath
	 * @throws IOException
	 */
	public static void writeString(String data,String filePath) throws IOException
	{
		FileOutputStream outStream = new FileOutputStream(filePath); 
		outStream.write(data.getBytes("ISO-8859-1"));
		outStream.close();	
	}
	
	/**
	 * 发送虚拟机执行脚本请求
	 * @param r Client WebResource
	 */
	public static void postUpload(WebResource r){
		
		final File fileToUpload = new File("C:/Users/Administrator/Desktop/script.bat");
		FormDataMultiPart multiPart = new FormDataMultiPart();
		String ip = "119.90.140.59";
		if (fileToUpload != null) 
		{
			multiPart =((FormDataMultiPart) multiPart.bodyPart(new FileDataBodyPart("upload", fileToUpload,
					MediaType.APPLICATION_OCTET_STREAM_TYPE)));
		}
		final ClientResponse clientResp = r.path("vmScript/upload")
				.queryParam("ip", ip)
				.type(MediaType.MULTIPART_FORM_DATA_TYPE)
				.header("password", "EDGM@MAMABDACFDLLG")
      			.header("time", "2015-01-26 16:14:07")
				.post(ClientResponse.class,multiPart);
		System.out.println("Response: " + clientResp.getClientResponseStatus()+"\n");
	
		
//		String ip = "127.0.0.1";
//		final File file = new File("C:/Users/Administrator/Desktop/script.bat");
//		FileDataBodyPart fdp = new FileDataBodyPart("file", file, MediaType.APPLICATION_OCTET_STREAM_TYPE);
//		FormDataMultiPart formDataMultiPart = new FormDataMultiPart();
//        formDataMultiPart.bodyPart(fdp);
//        formDataMultiPart.field("ip", ip, MediaType.TEXT_PLAIN_TYPE);
//        ClientResponse clientResp = r.path("vmScript/upload")
//        			.type(MediaType.MULTIPART_FORM_DATA)
//        			.header("password", "EDGM@MAMABDACFDLLG")
//        			.header("time", "2015-01-26 10:49:08")
//        			.post(ClientResponse.class, formDataMultiPart);
//        System.out.println("Response: " + clientResp.getClientResponseStatus()+"\n"); 
	
	}	
	
	/**
	 * MD5加码 生成32位md5码
	 */
	private static String string2MD5(String inStr) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();

	}

	/**
	 * 加密解密算法 执行一次加密，两次解密
	 */
	private static String convertMD5(String inStr) {

		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 't');
		}
		String s = new String(a);
		return s;

	}
	
	/**
	 *获取当前时间
	 */
	private static String CurrentTime(){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		String time = df.format(now);
		return time;
	}
	
}
