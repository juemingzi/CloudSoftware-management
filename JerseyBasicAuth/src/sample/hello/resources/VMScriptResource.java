package sample.hello.resources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import sample.hello.bean.VMScript;
import sample.DBOP.*;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

@Path("/vmScript")
public class VMScriptResource {
	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile(@QueryParam("ip") List<String> ip,
			@FormDataParam("upload") InputStream is,
			@FormDataParam("upload") FormDataContentDisposition formData) throws SQLException {
		// 脚本下载到服务器端的存储位置
		String fileLocation = "c:/" + formData.getFileName();
		System.out.println(fileLocation);
		DBForSCS scs = new DBForSCS();
		String uploadpath = "C:/scripts/" + formData.getFileName();
		boolean status;

		Response res = null;
		try {
			File f = saveFile(is, fileLocation);
			int opID = 0;
			String result = "Successfully File Uploaded on the path "
					+ fileLocation;
			System.out.println(result);
			VMScript vs = new VMScript();// ////////////////////////////////////////////////////////////
			if (ip.size() == 1) {
				status = scs.getScriptStatus(ip.get(0), uploadpath);
				JSONObject json = new JSONObject();

				if (status) {

					// 如果发送给agent成功，返回操作id
					opID = vs.sendExeVmScriptMsg(ip.get(0), f,
							formData.getFileName(),uploadpath);
					json.put("opID", opID);// 操作id
					json.put("status", "发送的脚本文件正在被执行");// 操作状态
				} else {
					json.put("status", "相同脚本正在执行，请稍后重试");
				}

				res = Response.ok(json).build();
			} else if (ip.size() > 1) {
				JSONArray jarr = new JSONArray();
				for (int i = 0; i < ip.size(); i++) {
					JSONObject json = new JSONObject();
					status = scs.getScriptStatus(ip.get(i), uploadpath);
					if (status) {
						opID = vs.sendExeVmScriptMsg(ip.get(i), f,
								formData.getFileName(),uploadpath);
						json.put("opID", opID);// 操作id
						json.put("status", "发送的脚本文件正在被执行");// 操作状态
					} else {
						json.put("status", "相同脚本正在执行，请稍后重试");
					}

					jarr.put(json);
				}
				res = Response.ok(jarr).build();
			}
			// 删除临时脚本
			// if (f.isFile() && f.exists()) {
			// f.delete();
			// }
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		scs.close();
		return res;
	}

	@POST
	@Path("/upload_Linux")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFileOnLinux(@QueryParam("ip") List<String> ip,
			@FormDataParam("upload") InputStream is,
			@FormDataParam("upload") FormDataContentDisposition formData) throws SQLException {
		// 脚本下载到服务器端的存储位置
		String fileLocation = "c:/" + formData.getFileName();
		System.out.println(fileLocation);
		DBForSCS scs = new DBForSCS();
		String uploadpath = "/home/scripts" + formData.getFileName();
		boolean status;

		Response res = null;
		try {
			File f = saveFile(is, fileLocation);
			int opID = 0;
			String result = "Successfully File Uploaded on the path "
					+ fileLocation;
			System.out.println(result);
			VMScript vs = new VMScript();// ////////////////////////////////////////////////////////////
			if (ip.size() == 1) {
				status = scs.getScriptStatus(ip.get(0), uploadpath);
				JSONObject json = new JSONObject();

				if (status) {

					// 如果发送给agent成功，返回操作id
					opID = vs.sendExeVmScriptMsg(ip.get(0), f,
							formData.getFileName(),uploadpath);
					json.put("opID", opID);// 操作id
					json.put("status", "发送的脚本文件正在被执行");// 操作状态
				} else {
					json.put("status", "相同脚本正在执行，请稍后重试");
				}

				res = Response.ok(json).build();
			} else if (ip.size() > 1) {
				JSONArray jarr = new JSONArray();
				for (int i = 0; i < ip.size(); i++) {
					JSONObject json = new JSONObject();
					status = scs.getScriptStatus(ip.get(i), uploadpath);
					if (status) {
						opID = vs.sendExeVmScriptMsg(ip.get(i), f,
								formData.getFileName(),uploadpath);
						json.put("opID", opID);// 操作id
						json.put("status", "发送的脚本文件正在被执行");// 操作状态
					} else {
						json.put("status", "相同脚本正在执行，请稍后重试");
					}

					jarr.put(json);
				}
				res = Response.ok(jarr).build();
			}
			// 删除临时脚本
			// if (f.isFile() && f.exists()) {
			// f.delete();
			// }
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		scs.close();
		return res;
	}
	private File saveFile(InputStream is, String fileLocation)
			throws IOException {
		File f = new File(fileLocation);
		OutputStream os = new FileOutputStream(f);
		byte[] buffer = new byte[256];
		int bytes = 0;
		while ((bytes = is.read(buffer)) != -1) {
			os.write(buffer, 0, bytes);
		}
		return f;
	}

}
