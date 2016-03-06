package sample.hello.resources;

import java.sql.SQLException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import sample.DBOP.DBOperation;
import sample.hello.bean.AppParamConfiguration;
import sample.hello.bean.GetAppParamConfig;

@Path("/GetAppParamConfig")
public class GetAppParamConfigResource {
	@POST
	@Path("/MySql")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response configMySql(@QueryParam("ip") String ip,
			@QueryParam("cfgFilePath") String cfgFilePath,
			@QueryParam("paramName") String paramName) throws JSONException {
		Response res = null;
		JSONObject entity = new JSONObject();
		DBOperation dbop = new DBOperation();
		GetAppParamConfig a = new GetAppParamConfig();

		// 判断该软件有没有在该虚拟机上安装过
		String dbSoftVersion;
		try {
			dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
					"mysql");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要获取配置信息，提示错误码0x0801000
				entity.put("code", "0x0801000");
			} else {// 软件已经安装，正常获取配置信息,发消息给代理软件
				String result = a.sendGetConfigMySqlMsg(ip, cfgFilePath,
						paramName);
				entity.put("response", result);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbop.close();
		res = Response.ok(entity).build();
		return res;
	}

	@POST
	@Path("/MySql_Linux")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response configMySqlLinux(@QueryParam("ip") String ip,
			@QueryParam("paramName") String paramName) throws JSONException {
		Response res = null;
		JSONObject entity = new JSONObject();
		DBOperation dbop = new DBOperation();
		GetAppParamConfig a = new GetAppParamConfig();
		// 判断该软件有没有在该虚拟机上安装过
		String dbSoftVersion;
		try {
			dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
					"mysql");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要获取配置信息，提示错误码0x0801000
				entity.put("code", "0x0801000");
			} else {// 软件已经安装，正常获取配置信息,发消息给代理软件
				String result = a.sendGetConfigMySqlLinuxMsg(ip, paramName);
				entity.put("response", result);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbop.close();
		res = Response.ok(entity).build();
		return res;
	}

	@POST
	@Path("/Tomcat")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response configTomcat(@QueryParam("ip") String ip,
			@QueryParam("cfgFilePath") String cfgFilePath,
			@QueryParam("paramName") String paramName) throws JSONException {
		Response res = null;
		JSONObject entity = new JSONObject();
		DBOperation dbop = new DBOperation();
		GetAppParamConfig a = new GetAppParamConfig();

		// 判断该软件有没有在该虚拟机上安装过
		String dbSoftVersion;
		try {
			dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
					"tomcat");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要获取配置信息，提示错误码0x0801000
				entity.put("code", "0x0801000");
			} else {// 软件已经安装，正常获取配置信息,发消息给代理软件
				String result = a.sendGetConfigTomcatMsg(ip, cfgFilePath,
						paramName);
				entity.put("response", result);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbop.close();
		res = Response.ok(entity).build();
		return res;
	}

	@POST
	@Path("/Apache")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response configApache(@QueryParam("ip") String ip,
			@QueryParam("cfgFilePath") String cfgFilePath,
			@QueryParam("paramName") String paramName) throws JSONException {
		Response res = null;
		JSONObject entity = new JSONObject();
		DBOperation dbop = new DBOperation();
		GetAppParamConfig a = new GetAppParamConfig();

		// 判断该软件有没有在该虚拟机上安装过
		String dbSoftVersion;
		try {
			dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
					"apache");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要获取配置信息，提示错误码0x0801000
				entity.put("code", "0x0801000");
			} else {// 软件已经安装，正常获取配置信息,发消息给代理软件
				String result = a.sendGetConfigApacheMsg(ip, cfgFilePath,
						paramName);
				entity.put("response", result);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbop.close();
		res = Response.ok(entity).build();
		return res;
	}
	@POST
	@Path("/Nginx")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response configNginx(@QueryParam("ip") String ip,
			@QueryParam("cfgFilePath") String cfgFilePath,
			@QueryParam("paramName") String paramName) throws JSONException {
		Response res = null;
		JSONObject entity = new JSONObject();
		GetAppParamConfig a = new GetAppParamConfig();
		DBOperation dbop = new DBOperation();

		// 判断该软件有没有在该虚拟机上安装过
		String dbSoftVersion;
		try {
			dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
					"nginx");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要获取配置信息，提示错误码0x0801000
				entity.put("code", "0x0801000");
			} else {// 软件已经安装，正常获取配置信息,发消息给代理软件
				String result = a.sendGetConfigNginxMsg(ip, cfgFilePath,
						paramName);
				entity.put("response", result);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbop.close();
		res = Response.ok(entity).build();
		return res;
	}

	@POST
	@Path("/ZendGuardLoader")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response configZendGuardLoader(@QueryParam("ip") String ip,
			@QueryParam("paramName") String paramName) throws JSONException {
		Response res = null;
		JSONObject entity = new JSONObject();
		GetAppParamConfig a = new GetAppParamConfig();
		DBOperation dbop = new DBOperation();

		// 判断该软件有没有在该虚拟机上安装过
		String dbSoftVersion;
		try {
			dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
					"zendguardloader");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要获取配置信息，提示错误码0x0801000
				entity.put("code", "0x0801000");
			} else {// 软件已经安装，正常获取配置信息,发消息给代理软件
				String result = a
						.sendGetConfigZendGuardLoaderMsg(ip, paramName);
				entity.put("response", result);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbop.close();

		res = Response.ok(entity).build();
		return res;
	}

	@POST
	@Path("/ZendGuardLoader_Linux")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response configZendGuardLoader(@QueryParam("ip") String ip,
			@QueryParam("path") String path,
			@QueryParam("paramName") String paramName) throws JSONException {
		Response res = null;
		JSONObject entity = new JSONObject();
		GetAppParamConfig a = new GetAppParamConfig();
		DBOperation dbop = new DBOperation();

		// 判断该软件有没有在该虚拟机上安装过
		String dbSoftVersion;
		try {
			dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
					"zendguardloader");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要获取配置信息，提示错误码0x0801000
				entity.put("code", "0x0801000");
			} else {// 软件已经安装，正常获取配置信息,发消息给代理软件
				String result = a.sendGetConfigZendGuardLoaderLinuxMsg(ip,
						path, paramName);
				entity.put("response", result);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbop.close();

		res = Response.ok(entity).build();
		return res;
	}
//	@POST
//	@Path("/Memcached")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response configMemcached(@QueryParam("ip") String ip,
//			@QueryParam("cfgFilePath") String cfgFilePath,
//			@QueryParam("paramName") String paramName) throws JSONException {
//		Response res = null;
//		JSONObject entity = new JSONObject();
//		DBOperation dbop = new DBOperation();
//		GetAppParamConfig a = new GetAppParamConfig();
//		// 判断该软件有没有在该虚拟机上安装过
//		String dbSoftVersion;
//		try {
//			dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
//					"memcached");
//			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要获取配置信息，提示错误码0x0801000
//				entity.put("code", "0x0801000");
//			} else {// 软件已经安装，正常获取配置信息,发消息给代理软件
//				String result = a.sendGetConfigMemcachedMsg(ip, cfgFilePath,
//						paramName);
//				entity.put("response", result);
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		dbop.close();
//		res = Response.ok(entity).build();
//		return res;
//	}

//	@POST
//	@Path("/IISRewrite")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response configIISRewrite(@QueryParam("ip") String ip,
//			@QueryParam("cfgFilePath") String cfgFilePath,
//			@QueryParam("paramName") String paramName) throws JSONException {
////		Response res = null;
////		JSONObject entity = new JSONObject();
////		GetAppParamConfig a = new GetAppParamConfig();
////		String result = a
////				.sendGetConfigIISRewriteMsg(ip, cfgFilePath, paramName);
////		entity.put("response", result);
////		res = Response.ok(entity).build();
////		return res;
//		Response res = null;
//		JSONObject entity = new JSONObject();
//		DBOperation dbop = new DBOperation();
//		GetAppParamConfig a = new GetAppParamConfig();
//		// 判断该软件有没有在该虚拟机上安装过
//		String dbSoftVersion;
//		try {
//			dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
//					"iisrewrite");
//			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要获取配置信息，提示错误码0x0801000
//				entity.put("code", "0x0801000");
//			} else {// 软件已经安装，正常获取配置信息,发消息给代理软件
//				String result = a.sendGetConfigIISRewriteMsg(ip, cfgFilePath, paramName);
//				entity.put("response", result);
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		dbop.close();
//		res = Response.ok(entity).build();
//		return res;
//	}

	@POST
	@Path("/FTP")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response configFTP(@QueryParam("ip") String ip,
			@QueryParam("cfgFilePath") String cfgFilePath,
			@QueryParam("paramName") String paramName) throws JSONException {
		Response res = null;
		JSONObject entity = new JSONObject();
		DBOperation dbop = new DBOperation();
		GetAppParamConfig a = new GetAppParamConfig();

		// 判断该软件有没有在该虚拟机上安装过
		String dbSoftVersion;
		try {
			dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip, "ftp");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要获取配置信息，提示错误码0x0801000
				entity.put("code", "0x0801000");
			} else {// 软件已经安装，正常获取配置信息,发消息给代理软件
				String result = a.sendGetConfigFTPMsg(ip, cfgFilePath,
						paramName);
				entity.put("response", result);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbop.close();
		res = Response.ok(entity).build();
		return res;
	}

//	@POST
//	@Path("/SQLServer2008R2")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response configSQLServer2008R2(@QueryParam("ip") String ip,
//			@QueryParam("cfgFilePath") String cfgFilePath,
//			@QueryParam("paramName") String paramName) throws JSONException {
//
//		Response res = null;
//		JSONObject entity = new JSONObject();
//		DBOperation dbop = new DBOperation();
//		GetAppParamConfig a = new GetAppParamConfig();
//
//		// 判断该软件有没有在该虚拟机上安装过
//		String dbSoftVersion;
//		try {
//			dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
//					"sqlserver2008r2");
//			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要获取配置信息，提示错误码0x0801000
//				entity.put("code", "0x0801000");
//			} else {// 软件已经安装，正常获取配置信息,发消息给代理软件
//				String result = a.sendGetConfigSQLServer2008R2Msg(ip,
//						cfgFilePath, paramName);
//				entity.put("response", result);
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		dbop.close();
//		res = Response.ok(entity).build();
//		return res;
//	}
//
//	@POST
//	@Path("/SQLServer2000")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response configSQLServer2000(@QueryParam("ip") String ip,
//			@QueryParam("cfgFilePath") String cfgFilePath,
//			@QueryParam("paramName") String paramName) throws JSONException {
//		Response res = null;
//		JSONObject entity = new JSONObject();
//		DBOperation dbop = new DBOperation();
//		GetAppParamConfig a = new GetAppParamConfig();
//
//		// 判断该软件有没有在该虚拟机上安装过
//		String dbSoftVersion;
//		try {
//			dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
//					"sqlserver2000");
//			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要获取配置信息，提示错误码0x0801000
//				entity.put("code", "0x0801000");
//			} else {// 软件已经安装，正常获取配置信息,发消息给代理软件
//				String result = a.sendGetConfigSQLServer2000Msg(ip,
//						cfgFilePath, paramName);
//				entity.put("response", result);
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		dbop.close();
//		res = Response.ok(entity).build();
//		return res;
//	}
//
//	@POST
//	@Path("/Oracle10g")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response configOracle10g(@QueryParam("ip") String ip,
//			@QueryParam("cfgFilePath") String cfgFilePath,
//			@QueryParam("paramName") String paramName) throws JSONException {
//		Response res = null;
//		JSONObject entity = new JSONObject();
//		GetAppParamConfig a = new GetAppParamConfig();
//		String result = a.sendGetConfigOracle10gMsg(ip, cfgFilePath, paramName);
//		entity.put("response", result);
//		res = Response.ok(entity).build();
//		return res;
//	}
//
//	@POST
//	@Path("/Oracle11g")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response configOracle11g(@QueryParam("ip") String ip,
//			@QueryParam("cfgFilePath") String cfgFilePath,
//			@QueryParam("paramName") String paramName) throws JSONException {
//		Response res = null;
//		JSONObject entity = new JSONObject();
//		GetAppParamConfig a = new GetAppParamConfig();
//		DBOperation dbop = new DBOperation();
//
//		// 判断该软件有没有在该虚拟机上安装过
//		String dbSoftVersion;
//		try {
//			dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
//					"oacle11g");
//			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要获取配置信息，提示错误码0x0801000
//				entity.put("code", "0x0801000");
//			} else {// 软件已经安装，正常获取配置信息,发消息给代理软件
//				String result = a.sendGetConfigOracle11gMsg(ip, cfgFilePath,
//						paramName);
//				entity.put("response", result);
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		dbop.close();
//
//		res = Response.ok(entity).build();
//		return res;
//	}
//
//	@POST
//	@Path("/360")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response config360(@QueryParam("ip") String ip,
//			@QueryParam("cfgFilePath") String cfgFilePath,
//			@QueryParam("paramName") String paramName) throws JSONException {
//		Response res = null;
//		JSONObject entity = new JSONObject();
//		DBOperation dbop = new DBOperation();
//		GetAppParamConfig a = new GetAppParamConfig();
//
//		// 判断该软件有没有在该虚拟机上安装过
//		String dbSoftVersion;
//		try {
//			dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
//					"mysql");
//			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要获取配置信息，提示错误码0x0801000
//				entity.put("code", "0x0801000");
//			} else {// 软件已经安装，正常获取配置信息,发消息给代理软件
//				String result = a.sendGetConfig360Msg(ip, cfgFilePath,
//						paramName);
//				entity.put("response", result);
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		dbop.close();
//		res = Response.ok(entity).build();
//		return res;
//	}

}
