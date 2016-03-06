package sample.hello.resources;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import sample.DBOP.DBOperation;

/**
 * 用户查询事件执行状态的api，例如查询安装软件进度，脚本执行进度等
 * 
 * @author huangpeng
 * 
 */
@Path("/ProgressQuery")
public class ProgressQuery {
	@GET
	@Path("/progress")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response changePasswd(@QueryParam("opID") String opID,
			@QueryParam("isUpdate") String  flag)
			throws JSONException, SQLException {
		Response res;
		DBOperation dbop = new DBOperation();
		JSONObject entity = new JSONObject();
		int eid = -1;
		// 判断用户输入的opID是否正确
		try {
			eid = Integer.parseInt(opID);
		} catch (Exception e) {
			entity.put("exception", "event id error!");
			res = Response.ok(entity).build();
		}
		String status = dbop.getOpStatus(eid,flag);
		System.out.println("status "+ status);
		entity.put("status", status);
		int category = 0;
		/**
		 * 2015-1-21新增
		 */
		String detailInfo = dbop.getOpDetailInfo(eid);
		if(detailInfo==null){
			entity.put("category", category);
			entity.put("detailInfo", detailInfo);
			dbop.close();
			res = Response.ok(entity).build();
			return res;
		}
		// 若是图片则转为字符串
		String regex = "([a-z]|[A-Z]):(/(\\S)+)+\\.jpg";
		if ((!detailInfo.isEmpty())&&(detailInfo.matches(regex))) {
			category = 1;
			try {
				detailInfo = readFile(detailInfo);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		entity.put("category", category);
		entity.put("detailInfo", detailInfo);
		dbop.close();
		res = Response.ok(entity).build();
		return res;
	}

	/**
	 * 2015-1-21新增 将图片转为字符串
	 * 
	 * @param FilePath
	 * @return
	 * @throws IOException
	 */
	public String readFile(String FilePath) throws IOException {
		FileInputStream fis = new FileInputStream(FilePath);
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = fis.read(buffer)) != -1) {

			outStream.write(buffer, 0, len);
		}
		outStream.close();
		fis.close();
		return new String(outStream.toByteArray(), "ISO-8859-1");
	}
	
	
	
//	/**@2015-1-21新增 
//	 * 用于测试
//	 * @param opID
//	 * @return
//	 * @throws JSONException
//	 * @throws SQLException
//	 */
//	@GET
//	@Path("/progress")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response changePasswd(@QueryParam("opID") String opID)
//			throws JSONException, SQLException {
//		Response res;
//		JSONObject entity = new JSONObject();
//		String status = "success";
//		entity.put("status", status);
//		int category = 0;
//		String detailInfo ="E:/MyEclpse10/workspace/JerseyBasicAuth/pictures/1/1.jpg";
//		String regex = "([a-z]|[A-Z]):(/(\\S)+)+\\.jpg";
//		if (detailInfo.matches(regex)) {
//		category = 1;
//		try {
//			detailInfo = readFile(detailInfo);
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	entity.put("category", category);
//	entity.put("detailInfo", detailInfo);
//	res = Response.ok(entity).build();
//	entity.put("detailInfo", detailInfo);
//	res = Response.ok(entity).build();
//	return res;
//	}
}
