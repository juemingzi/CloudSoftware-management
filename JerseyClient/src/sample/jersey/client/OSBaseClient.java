package sample.jersey.client;
import java.io.File;
import javax.ws.rs.core.MediaType;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;

public class OSBaseClient {
		
	public static void main(String[] args){
//		getToken("hp","123456");
		Client c = Client.create();
		WebResource r = c.resource("http://119.90.140.60:8080/JerseyBasicAuth/rest/");
		putChangePasswd(r,"119.90.140.59","administrator","123456","aHA6MTIzNDU2OjMz");
//		putChangePasswd_json(r,"119.90.140.59","administrator","123456","aHA6MTIzNDU2OjE3");
//		postUpload(r,"E:/script.bat","119.90.140.59","aHA6MTIzNDU2OjE3");
	}
	/**
	 * 用户获取认证
	 * @param uid 数据库cloudhost的userinfo表的userID字段
	 * @param pwd 数据库cloudhost的userinfo表的pwd字段
	 */
	public static void getToken(String uid,String pwd){
		Client c = Client.create();
		WebResource r = c.resource("http://localhost:8080/JerseyBasicAuth/Authentication");
		String response = r
                .queryParam("username", uid)
                .queryParam("pwd", pwd)
                .type(MediaType.APPLICATION_JSON)
                .get(String.class);
		System.out.println("token:"+response); 
	}
	/**
	 * 使用queryParam发送数据执行修改密码操作
	 * @param r Client WebResource
	 * @param ip 要修改的虚拟机的IP
	 * @param userName 要修改密码的虚拟机用户名
	 * @param passwd 修改后的密码
	 * @param auth getToken获取的认证码
	 */
	public static void putChangePasswd(WebResource r,String ip,String userName,String passwd,String auth){
		String response = r.path("OSBase/vmSysPwd")
                .queryParam("ip", ip)
                .queryParam("userName", userName)
                .queryParam("passwd", passwd)
                .header("Authentication", auth)
                .type(MediaType.APPLICATION_JSON)
                .put(String.class);
		System.out.println(response);
	}
	/**
	 * 使用json对象发送数据执行修改密码操作
	 * @param r Client WebResource
	 * @param ip 要修改的虚拟机的IP
	 * @param userName 要修改密码的虚拟机用户名
	 * @param passwd 修改后的密码
	 * @param auth getToken获取的认证码
	 */
	public static void putChangePasswd_json(WebResource r,String ip,String userName,String passwd,String auth){
		JSONObject jo = new JSONObject();
		try {
			jo.put("ip", ip);
			jo.put("userName", userName);
			jo.put("passwd", passwd);
			String response = r.path("OSBase/vmSysPwd_json")
					.entity(jo)
	                .header("Authentication", auth)
	                .type(MediaType.APPLICATION_JSON)
	                .put(String.class);
			System.out.println(response);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 发送虚拟机执行脚本请求
	 * @param r Client WebResource
	 * @param scriptFilePath 要执行的脚本文件在本机的绝对路径
	 * @param ip 要执行脚本的虚拟机的IP
	 * @param auth getToken获取的认证码
	 */
	public static void postUpload(WebResource r,String scriptFilePath,String ip,String auth){
		final File fileToUpload = new File(scriptFilePath);
		final FormDataMultiPart multiPart = new FormDataMultiPart();
		if (fileToUpload != null) 
		{
			((FormDataMultiPart) multiPart.bodyPart(new FileDataBodyPart("upload", fileToUpload,
					MediaType.APPLICATION_OCTET_STREAM_TYPE)))
					.field("ip",ip, MediaType.TEXT_PLAIN_TYPE);
		}
		final ClientResponse clientResp = r.path("vmScript/upload")
				.type(MediaType.MULTIPART_FORM_DATA_TYPE)
				.header("Authentication", auth)
				.post(ClientResponse.class,multiPart);
		System.out.println("Response: " + clientResp.getClientResponseStatus());
	}	
}
