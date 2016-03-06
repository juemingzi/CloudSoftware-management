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

import sample.DBOP.*;
import sample.hello.bean.AppParamConfiguration;

@Path("/AppParamConfiguration")
public class AppParamResource {
	@POST
	@Path("/MySql")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response configMySql(@QueryParam("ip") String ip,
			@QueryParam("cfgFilePath") String cfgFilePath,
			@QueryParam("paramName") String paramName,
			@QueryParam("paramValue") String paramValue) throws JSONException,
			SQLException {
		Response res = null;
		JSONObject entity = new JSONObject();
		DBOperation dbop = new DBOperation();
		DBForSCS scs = new DBForSCS();
		AppParamConfiguration a = new AppParamConfiguration();
		// 判断该软件有没有在该虚拟机上安装过
		String dbSoftVersion;

		try {
			dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
					"mysql");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要配置，提示错误码0x0301000
				entity.put("code", "0x0301000");
			} else {// 软件已经安装，正常配置,发消息给代理软件
				boolean status = scs.getConfigStatus(ip, "mysql", paramName);
				if (status) {
					scs.InsertToConfig(ip, "mysql", cfgFilePath, paramName,
							paramValue);
					String result = a.sendConfigMySqlMsg(ip, cfgFilePath,
							paramName, paramValue);
					entity.put("response", result);
				} else {
					entity.put("response", "0x1100001");
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbop.close();
		scs.close();
		res = Response.ok(entity).build();
		return res;
	}

	@POST
	@Path("/Tomcat")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response configTomcat(@QueryParam("ip") String ip,
			@QueryParam("cfgFilePath") String cfgFilePath,
			@QueryParam("paramName") String paramName,
			@QueryParam("paramValue") String paramValue) throws JSONException, SQLException {
		Response res = null;
		JSONObject entity = new JSONObject();
		DBOperation dbop = new DBOperation();
		DBForSCS scs = new DBForSCS();
		AppParamConfiguration a = new AppParamConfiguration();

		// 判断该软件有没有在该虚拟机上安装过
		String dbSoftVersion;
		try {
			dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
					"tomcat");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要配置，提示错误码0x0301000
				entity.put("code", "0x0301000");
			} else {
				boolean status = scs.getConfigStatus(ip, "tomcat", paramName);
				
				System.out.println("status~~~~~~~~~~~~~~~~~"+status);
				
				if (status) {
					// 软件已经安装，正常配置,发消息给代理软件
					scs.InsertToConfig(ip, "tomcat", cfgFilePath, paramName,
							paramValue);
					String result = a.sendConfigTomcatMsg(ip, cfgFilePath,
							paramName, paramValue);
					entity.put("response", result);
				} else {
					entity.put("response", "0x1100001");
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbop.close();
		scs.close();
		res = Response.ok(entity).build();
		return res;
	}

	@POST
	@Path("/Apache")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response configApache(@QueryParam("ip") String ip,
			@QueryParam("cfgFilePath") String cfgFilePath,
			@QueryParam("paramName") String paramName,
			@QueryParam("paramValue") String paramValue) throws JSONException, SQLException {
		Response res = null;
		JSONObject entity = new JSONObject();
		DBOperation dbop = new DBOperation();
		DBForSCS scs = new DBForSCS();
		AppParamConfiguration a = new AppParamConfiguration();
		// 判断该软件有没有在该虚拟机上安装过
		String dbSoftVersion;
		try {
			dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
					"apache");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要配置，提示错误码0x0301000
				entity.put("code", "0x0301000");
			} else {// 软件已经安装，正常配置,发消息给代理软件
				boolean status = scs.getConfigStatus(ip, "apache", paramName);
				if (status) {
					scs.InsertToConfig(ip, "apache", cfgFilePath, paramName,
							paramValue);
					String result = a.sendConfigApacheMsg(ip, cfgFilePath,
							paramName, paramValue);
					entity.put("response", result);
				} else {
					entity.put("response", "0x1100001");
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbop.close();
		scs.close();
		res = Response.ok(entity).build();
		return res;
	}

	@POST
	@Path("/Nginx")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response configNginx(@QueryParam("ip") String ip,
			@QueryParam("cfgFilePath") String cfgFilePath,
			@QueryParam("paramName") String paramName,
			@QueryParam("paramValue") String paramValue) throws JSONException, SQLException {
		Response res = null;
		JSONObject entity = new JSONObject();
		DBOperation dbop = new DBOperation();
		DBForSCS scs = new DBForSCS();
		AppParamConfiguration a = new AppParamConfiguration();

		// 判断该软件有没有在该虚拟机上安装过
		String dbSoftVersion;
		try {
			dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
					"nginx");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要配置，提示错误码0x0301000
				entity.put("code", "0x0301000");
			} else {// 软件已经安装，正常配置,发消息给代理软件
				boolean status = scs.getConfigStatus(ip, "nginx", paramName);
				if (status) {
					scs.InsertToConfig(ip, "nginx", cfgFilePath, paramName,
							paramValue);
					String result = a.sendConfigNginxMsg(ip, cfgFilePath,
							paramName, paramValue);
					entity.put("response", result);
				} else {
					entity.put("response", "0x1100001");
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbop.close();
		scs.close();
		res = Response.ok(entity).build();
		return res;
	}

	@POST
	@Path("/ZendGuardLoader")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response configZendGuardLoader(@QueryParam("ip") String ip,
			@QueryParam("cfgFilePath") String cfgFilePath,
			@QueryParam("paramName") String paramName,
			@QueryParam("paramValue") String paramValue) throws JSONException, SQLException {
		Response res = null;
		JSONObject entity = new JSONObject();
		DBOperation dbop = new DBOperation();
		DBForSCS scs = new DBForSCS();
		AppParamConfiguration a = new AppParamConfiguration();

		// 判断该软件有没有在该虚拟机上安装过
		String dbSoftVersion;
		try {
			dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
					"ZendGuardLoader");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要配置，提示错误码0x0301000
				entity.put("code", "0x0301000");
			} else {// 软件已经安装，正常配置,发消息给代理软件
				boolean status = scs.getConfigStatus(ip, "zendguardloader",
						paramName);
				if (status) {
					scs.InsertToConfig(ip, "zendguardloader", cfgFilePath,
							paramName, paramValue);
					String result = a.sendConfigZendGuardLoaderMsg(ip,
							cfgFilePath, paramName, paramValue);
					entity.put("response", result);
				} else {
					entity.put("response", "0x1100001");
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbop.close();
		scs.close();
		res = Response.ok(entity).build();
		return res;
	}
	@POST
	@Path("/FTP")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response configFTP(@QueryParam("ip") String ip,
			@QueryParam("cfgFilePath") String cfgFilePath,
			@QueryParam("paramName") String paramName,
			@QueryParam("paramValue") String paramValue) throws JSONException, SQLException {

		Response res = null;
		JSONObject entity = new JSONObject();
		DBOperation dbop = new DBOperation();
		DBForSCS scs = new DBForSCS();
		AppParamConfiguration a = new AppParamConfiguration();

		// 判断该软件有没有在该虚拟机上安装过
		String dbSoftVersion;
		try {
			dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip, "ftp");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要配置，提示错误码0x0301000
				entity.put("code", "0x0301000");
			} else {// 软件已经安装，正常配置,发消息给代理软件
				boolean status = scs.getConfigStatus(ip, "ftp", paramName);
				if (status) {
					scs.InsertToConfig(ip, "ftp", cfgFilePath, paramName,
							paramValue);
					String result = a.sendConfigFTPMsg(ip, cfgFilePath,
							paramName, paramValue);
					entity.put("response", result);
				} else {
					entity.put("response", "0x1100001");
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbop.close();
		scs.close();
		res = Response.ok(entity).build();
		return res;
	}
}
