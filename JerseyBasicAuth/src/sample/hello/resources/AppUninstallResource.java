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
import sample.DBOP.GetInstallPath;
import sample.hello.bean.ApplicationUninstallBase;

@Path("/AppUninstall")
public class AppUninstallResource {

	@POST
	@Path("/MySql")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uninstallMySql(@QueryParam("ip") String ip) throws SQLException {
		Response res = null;
		ApplicationUninstallBase a = new ApplicationUninstallBase();
		JSONObject entity = new JSONObject();// 表示要给用户返回的json对象
		DBOperation dbop = new DBOperation();
		GetInstallPath getpath = new GetInstallPath();
		String path = getpath.getpath(ip, "mysql");
		try {
			// 判断该软件有没有在该虚拟机上安装过
			String dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
					"mysql");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要卸载，返回错误码0x0201000
				entity.put("code", "0x0201000");
			} else {// 软件已安装，正常卸载
					// 将消息发送给Agent成功以后，返回一个代表操作id的代号eid，同时这也是数据库中存储的代表该操作的eid
				int eid = a.sendUninstallMySqlMsg(ip, path);
				// 构造要返回给用户的json对象
				entity.put("eid", eid);
				entity.put("status", "虚拟机已接收卸载mysql请求可查询状态");//
			}
			dbop.close();
			getpath.close();
			res = Response.ok(entity).build();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	/*
	 * @POST
	 * 
	 * @Path("/MySql_Linux")
	 * 
	 * @Consumes(MediaType.APPLICATION_JSON)
	 * 
	 * @Produces(MediaType.APPLICATION_JSON) public Response
	 * uninstallMySqlOnLinux(
	 * 
	 * @QueryParam("ip") String ip ) { Response res = null;
	 * ApplicationUninstallBase a = new ApplicationUninstallBase(); JSONObject
	 * entity = new JSONObject(); try { int eid =
	 * a.sendUninstallMySqlOnLinuxMsg( ip); entity.put("eid", eid);
	 * entity.put("status", "虚拟机已接收卸载mysql请求可查询状态"); res =
	 * Response.ok(entity).build(); } catch (JSONException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } return res; }
	 */
	@POST
	@Path("/MySql_Linux")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uninstallMySqlOnLinux(@QueryParam("ip") String ip) throws SQLException {
		Response res = null;
		ApplicationUninstallBase a = new ApplicationUninstallBase();
		JSONObject entity = new JSONObject();
		DBOperation dbop = new DBOperation();
		GetInstallPath getpath = new GetInstallPath();
		String path = getpath.getpath(ip, "mysql");
		try {
			// 判断该软件有没有在该虚拟机上安装过
			String dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
					"mysql");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要卸载，返回错误码0x0201000
				entity.put("code", "0x0201000");
			} else {// 软件已安装，正常卸载
					// 将消息发送给Agent成功以后，返回一个代表操作id的代号eid，同时这也是数据库中存储的代表该操作的eid
				int eid = a.sendUninstallMySqlOnLinuxMsg(ip,path);
				// 构造要返回给用户的json对象
				entity.put("eid", eid);
				entity.put("status", "虚拟机已接收卸载mysql请求可查询状态");
			}
			dbop.close();
			getpath.close();
			res = Response.ok(entity).build();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	@POST
	@Path("/Tomcat")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uninstallTomcat(@QueryParam("ip") String ip) throws SQLException {
		Response res = null;
		ApplicationUninstallBase a = new ApplicationUninstallBase();
		JSONObject entity = new JSONObject();// 表示要给用户返回的json对象
		DBOperation dbop = new DBOperation();
		GetInstallPath getpath = new GetInstallPath();
		String path = getpath.getpath(ip, "tomcat");
		System.out.println("getPath is path    ########"+path);
		try {
			// 判断该软件有没有在该虚拟机上安装过
			String dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
					"tomcat");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要卸载，返回错误码0x0201000
				entity.put("code", "0x0201000");
			} else {// 软件已安装，正常卸载
					// 将消息发送给Agent成功以后，返回一个代表操作id的代号eid，同时这也是数据库中存储的代表该操作的eid
				int eid = a.sendUninstallTomcatMsg(ip,path);
				// 构造要返回给用户的json对象
				entity.put("eid", eid);
				entity.put("status", "虚拟机已接收卸载tomcat请求可查询状态");//
			}
			dbop.close();
			getpath.close();
			res = Response.ok(entity).build();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	@POST
	@Path("/Tomcat_Linux")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uninstallTomcatOnLinux(@QueryParam("ip") String ip) throws SQLException {
		Response res = null;
		ApplicationUninstallBase a = new ApplicationUninstallBase();
		JSONObject entity = new JSONObject();// 表示要给用户返回的json对象
		DBOperation dbop = new DBOperation();
		GetInstallPath getpath = new GetInstallPath();
		String path = getpath.getpath(ip, "tomcat");
		try {
			// 判断该软件有没有在该虚拟机上安装过
			String dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
					"tomcat");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要卸载，返回错误码0x0201000
				entity.put("code", "0x0201000");
			} else {// 软件已安装，正常卸载
					// 将消息发送给Agent成功以后，返回一个代表操作id的代号eid，同时这也是数据库中存储的代表该操作的eid
				int eid = a.sendUninstallTomcatLinuxMsg(ip, path);
				// 构造要返回给用户的json对象
				entity.put("eid", eid);
				entity.put("status", "虚拟机已接收卸载tomcat请求可查询状态");//
			}
			dbop.close();
			getpath.close();
			res = Response.ok(entity).build();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}



	@POST
	@Path("/Apache")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uninstallApache(@QueryParam("ip") String ip) throws SQLException {
		Response res = null;
		ApplicationUninstallBase a = new ApplicationUninstallBase();
		JSONObject entity = new JSONObject();// 表示要给用户返回的json对象
		DBOperation dbop = new DBOperation();
		GetInstallPath getpath = new GetInstallPath();
		String path = getpath.getpath(ip, "apache");
		try {
			// 判断该软件有没有在该虚拟机上安装过
			String dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
					"apache");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要卸载，返回错误码0x0201000
				entity.put("code", "0x0201000");
			} else {// 软件已安装，正常卸载
					// 将消息发送给Agent成功以后，返回一个代表操作id的代号eid，同时这也是数据库中存储的代表该操作的eid
				int eid = a.sendUninstallApacheMsg(ip,path);
				// 构造要返回给用户的json对象
				entity.put("eid", eid);
				entity.put("status", "虚拟机已接收卸载apache请求可查询状态");//
			}
			dbop.close();
			getpath.close();
			res = Response.ok(entity).build();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	@POST
	@Path("/Apache_Linux")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uninstallApacheOnLinux(@QueryParam("ip") String ip) throws SQLException {
		Response res = null;
		ApplicationUninstallBase a = new ApplicationUninstallBase();
		JSONObject entity = new JSONObject();// 表示要给用户返回的json对象
		DBOperation dbop = new DBOperation();
		GetInstallPath getpath = new GetInstallPath();
		String path = getpath.getpath(ip, "apache");
		try {
			// 判断该软件有没有在该虚拟机上安装过
			String dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
					"apache");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要卸载，返回错误码0x0201000
				entity.put("code", "0x0201000");
			} else {// 软件已安装，正常卸载
					// 将消息发送给Agent成功以后，返回一个代表操作id的代号eid，同时这也是数据库中存储的代表该操作的eid
				int eid = a.sendUninstallApacheLinuxMsg(ip, path);
				// 构造要返回给用户的json对象
				entity.put("eid", eid);
				entity.put("status", "虚拟机已接收卸载apache请求可查询状态");//
			}
			dbop.close();
			getpath.close();
			res = Response.ok(entity).build();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}


	@POST
	@Path("/Nginx")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uninstallNginx(@QueryParam("ip") String ip) throws SQLException {
		Response res = null;
		ApplicationUninstallBase a = new ApplicationUninstallBase();
		JSONObject entity = new JSONObject();
		DBOperation dbop = new DBOperation();
		GetInstallPath getpath = new GetInstallPath();
		String path = getpath.getpath(ip, "nginx");
		try {
			// 判断该软件有没有在该虚拟机上安装过
			String dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
					"nginx");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要卸载，返回错误码0x0201000
				entity.put("code", "0x0201000");
			} else {// 软件已安装，正常卸载
					// 将消息发送给Agent成功以后，返回一个代表操作id的代号eid，同时这也是数据库中存储的代表该操作的eid
				int eid = a.sendUninstallNginxMsg(ip, path);
				// 构造要返回给用户的json对象
				entity.put("eid", eid);
				entity.put("status", "虚拟机已接收卸载nginx请求可查询状态");//
			}
			dbop.close();
			getpath.close();
			res = Response.ok(entity).build();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	/*
	 * @POST
	 * 
	 * @Path("/ZendGuardLoader")
	 * 
	 * @Consumes(MediaType.APPLICATION_JSON)
	 * 
	 * @Produces(MediaType.APPLICATION_JSON) public Response
	 * uninstallZendGuardLoader(
	 * 
	 * @QueryParam("ip") String ip,
	 * 
	 * @QueryParam("path") String Path ) { Response res = null;
	 * ApplicationUninstallBase a = new ApplicationUninstallBase();JSONObject
	 * entity = new JSONObject();
	 * 
	 * 
	 * try {
	 * 
	 * 
	 * int eid = a.sendUninstallZendGuardLoaderMsg( ip,Path); entity.put("eid",
	 * eid); entity.put("status", "虚拟机已接收卸载zendGuardLoader请求可查询状态 "); res =
	 * Response.ok(entity).build(); } catch (JSONException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } return res; }
	 */
	@POST
	@Path("/ZendGuardLoader")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uninstallZendGuardLoader(@QueryParam("ip") String ip) throws SQLException {
		Response res = null;
		ApplicationUninstallBase a = new ApplicationUninstallBase();
		JSONObject entity = new JSONObject();
		DBOperation dbop = new DBOperation();
		GetInstallPath getpath = new GetInstallPath();
		String path = getpath.getpath(ip, "zendguardloader");

		try {
			// 判断该软件有没有在该虚拟机上安装过
			String dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
					"zendguardloader");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要卸载，返回错误码0x0201000
				entity.put("code", "0x0201000");
			} else {// 软件已安装，正常卸载
					// 将消息发送给Agent成功以后，返回一个代表操作id的代号eid，同时这也是数据库中存储的代表该操作的eid
				int eid = a.sendUninstallZendGuardLoaderMsg(ip, path);
				// 构造要返回给用户的json对象
				entity.put("eid", eid);
				entity.put("status", "虚拟机已接收卸载zendGuardLoader请求可查询状态");//
			}
			dbop.close();
			getpath.close();
			res = Response.ok(entity).build();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}


	@POST
	@Path("/Python")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uninstallPython(@QueryParam("ip") String ip) throws SQLException {
		Response res = null;
		ApplicationUninstallBase a = new ApplicationUninstallBase();
		JSONObject entity = new JSONObject();// 表示要给用户返回的json对象
		DBOperation dbop = new DBOperation();
		GetInstallPath getpath = new GetInstallPath();
		String path = getpath.getpath(ip, "python");
		try {
			// 判断该软件有没有在该虚拟机上安装过
			String dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
					"python");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要卸载，返回错误码0x0201000
				entity.put("code", "0x0201000");
			} else {// 软件已安装，正常卸载
					// 将消息发送给Agent成功以后，返回一个代表操作id的代号eid，同时这也是数据库中存储的代表该操作的eid
				int eid = a.sendUninstallPythonMsg(ip,path);
				// 构造要返回给用户的json对象
				entity.put("eid", eid);
				entity.put("status", "虚拟机已接收卸载python请求可查询状态");//
			}
			dbop.close();
			getpath.close();
			res = Response.ok(entity).build();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	@POST
	@Path("/Python_Linux")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uninstallPythonLinux(@QueryParam("ip") String ip) throws SQLException {

		Response res = null;
		ApplicationUninstallBase a = new ApplicationUninstallBase();
		JSONObject entity = new JSONObject();// 表示要给用户返回的json对象
		DBOperation dbop = new DBOperation();
		GetInstallPath getpath = new GetInstallPath();
		String path = getpath.getpath(ip, "python");
		try {
			// 判断该软件有没有在该虚拟机上安装过
			String dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
					"python");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要卸载，返回错误码0x0201000
				entity.put("code", "0x0201000");
			} else {// 软件已安装，正常卸载
					// 将消息发送给Agent成功以后，返回一个代表操作id的代号eid，同时这也是数据库中存储的代表该操作的eid
				int eid = a.sendUninstallPythonLinuxMsg(ip, path);
				// 构造要返回给用户的json对象
				entity.put("eid", eid);
				entity.put("status", "虚拟机已接收卸载python请求可查询状态");//
			}
			dbop.close();
			getpath.close();
			res = Response.ok(entity).build();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	/*
	 * @POST
	 * 
	 * @Path("/Memcached")
	 * 
	 * @Consumes(MediaType.APPLICATION_JSON)
	 * 
	 * @Produces(MediaType.APPLICATION_JSON) public Response uninstallMemcached(
	 * 
	 * @QueryParam("ip") String ip,
	 * 
	 * @QueryParam("softName") String softName ) { Response res = null;
	 * ApplicationUninstallBase a = new ApplicationUninstallBase();JSONObject
	 * entity = new JSONObject();
	 * 
	 * 
	 * try {
	 * 
	 * 
	 * int eid = a.sendUninstallMemcachedMsg( ip,softName); entity.put("eid",
	 * eid); entity.put("status", "虚拟机已接收卸载memcached请求可查询状态"); res =
	 * Response.ok(entity).build(); } catch (JSONException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } return res; }
	 */
	@POST
	@Path("/Memcached")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uninstallMemcached(@QueryParam("ip") String ip
			/*@QueryParam("softName") String softName*/) throws SQLException {
		Response res = null;
		ApplicationUninstallBase a = new ApplicationUninstallBase();
		JSONObject entity = new JSONObject();// 表示要给用户返回的json对象
		DBOperation dbop = new DBOperation();
		GetInstallPath getpath = new GetInstallPath();
		String path = getpath.getpath(ip, "memcached");
		try {
			// 判断该软件有没有在该虚拟机上安装过
			String dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
					"memcached");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要卸载，返回错误码0x0201000
				entity.put("code", "0x0201000");
			} else {// 软件已安装，正常卸载
					// 将消息发送给Agent成功以后，返回一个代表操作id的代号eid，同时这也是数据库中存储的代表该操作的eid
				int eid = a.sendUninstallMemcachedMsg(ip, path);
				// 构造要返回给用户的json对象
				entity.put("eid", eid);
				entity.put("status", "虚拟机已接收卸载memcached请求可查询状态");
			}
			dbop.close();
			getpath.close();
			res = Response.ok(entity).build();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	/*
	 * @POST
	 * 
	 * @Path("/Memcached_Linux")
	 * 
	 * @Consumes(MediaType.APPLICATION_JSON)
	 * 
	 * @Produces(MediaType.APPLICATION_JSON) public Response uninstallMemcached(
	 * 
	 * @QueryParam("ip") String ip,
	 * 
	 * @QueryParam("softName") String softName,
	 * 
	 * @QueryParam("path") String path ) { Response res = null;
	 * ApplicationUninstallBase a = new ApplicationUninstallBase();JSONObject
	 * entity = new JSONObject();
	 * 
	 * 
	 * try {
	 * 
	 * 
	 * int eid = a.sendUninstallMemcachedLinuxMsg( ip,softName,path);
	 * entity.put("eid", eid); entity.put("status", "虚拟机已接收卸载memcached请求可查询状态");
	 * res = Response.ok(entity).build(); } catch (JSONException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } return res; }
	 */
	@POST
	@Path("/Memcached_Linux")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uninstallMemcachedOnLinux(@QueryParam("ip") String ip) throws SQLException {
		Response res = null;
		ApplicationUninstallBase a = new ApplicationUninstallBase();
		JSONObject entity = new JSONObject();
		DBOperation dbop = new DBOperation();
		GetInstallPath getpath = new GetInstallPath();
		String path = getpath.getpath(ip, "memcached");

		try {
			// 判断该软件有没有在该虚拟机上安装过
			String dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
					"memcached");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要卸载，返回错误码0x0201000
				entity.put("code", "0x0201000");
			} else {// 软件已安装，正常卸载
					// 将消息发送给Agent成功以后，返回一个代表操作id的代号eid，同时这也是数据库中存储的代表该操作的eid
				int eid = a.sendUninstallMemcachedLinuxMsg(ip,path);
				entity.put("eid", eid);
				entity.put("status", "虚拟机已接收卸载memcached请求可查询状态");
			}
			dbop.close();
			getpath.close();
			res = Response.ok(entity).build();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	// @POST
	// @Path("/Memcached_Linux")
	// @Consumes(MediaType.APPLICATION_JSON)
	// @Produces(MediaType.APPLICATION_JSON)
	// public Response uninstallMemcachedL(
	// @QueryParam("ip") String ip
	// ) {
	// Response res = null;
	// ApplicationUninstallBase a = new ApplicationUninstallBase();JSONObject
	// entity = new JSONObject();
	//
	//
	// try {
	// scIPAddr = dbop.getRCAddrByIP("hp", ip,"memcached");
	//
	// int eid = a.sendUninstallMemcachedMsgOnLinux( ip);
	// entity.put("eid", eid);
	// entity.put("status", "uninstall memcached has already been executing ");
	// res = Response.ok(entity).build();
	// } catch (SQLException e) {
	// // TODO Auto-generated catch block0
	// e.printStackTrace();
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return res;
	// }
	@POST
	@Path("/IISRewrite")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uninstallIISRewrite(@QueryParam("ip") String ip) throws SQLException {
		Response res = null;
		ApplicationUninstallBase a = new ApplicationUninstallBase();
		JSONObject entity = new JSONObject();
		DBOperation dbop = new DBOperation();
		GetInstallPath getpath = new GetInstallPath();
		String path = getpath.getpath(ip,"iisrewrite");

		try {
			// 判断该软件有没有在该虚拟机上安装过
			String dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
					"iisrewrite");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要卸载，返回错误码0x0201000
				entity.put("code", "0x0201000");
			} else {// 软件已安装，正常卸载
					// 将消息发送给Agent成功以后，返回一个代表操作id的代号eid，同时这也是数据库中存储的代表该操作的eid
				int eid = a.sendUninstallIISRewriteMsg(ip,path);
				entity.put("eid", eid);
				entity.put("status", "虚拟机已接收卸载iisrewrite请求可查询状态");
			}
			dbop.close();
			getpath.close();
			res = Response.ok(entity).build();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	@POST
	@Path("/FTP")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uninstallFTP(@QueryParam("ip") String ip) throws SQLException {
		Response res = null;
		ApplicationUninstallBase a = new ApplicationUninstallBase();
		JSONObject entity = new JSONObject();// 表示要给用户返回的json对象
		DBOperation dbop = new DBOperation();
		GetInstallPath getpath = new GetInstallPath();
		String path = getpath.getpath(ip, "ftp");

		try {
			// 判断该软件有没有在该虚拟机上安装过
			String dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
					"ftp");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要卸载，返回错误码0x0201000
				entity.put("code", "0x0201000");
			} else {// 软件已安装，正常卸载
					// 将消息发送给Agent成功以后，返回一个代表操作id的代号eid，同时这也是数据库中存储的代表该操作的eid
				int eid = a.sendUninstallFTPMsg(ip,path);
				// 构造要返回给用户的json对象
				entity.put("eid", eid);
				entity.put("status", "虚拟机已接收卸载ftp请求可查询状态");//
			}
			dbop.close();
			getpath.close();
			res = Response.ok(entity).build();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	@POST
	@Path("/FTP_Linux")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uninstallFTPL(@QueryParam("ip") String ip) throws SQLException {
		Response res = null;
		ApplicationUninstallBase a = new ApplicationUninstallBase();
		JSONObject entity = new JSONObject();// 表示要给用户返回的json对象
		DBOperation dbop = new DBOperation();
		GetInstallPath getpath = new GetInstallPath();
		String path = getpath.getpath(ip, "ftp");
		try {
			// 判断该软件有没有在该虚拟机上安装过
			String dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
					"ftp");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要卸载，返回错误码0x0201000
				entity.put("code", "0x0201000");
			} else {// 软件已安装，正常卸载
					// 将消息发送给Agent成功以后，返回一个代表操作id的代号eid，同时这也是数据库中存储的代表该操作的eid
				int eid = a.sendUninstallFTPMsgOnLinux(ip,path);
				// 构造要返回给用户的json对象
				entity.put("eid", eid);
				entity.put("status", "虚拟机已接收卸载ftp请求可查询状态");//
			}
			dbop.close();
			getpath.close();
			res = Response.ok(entity).build();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}


	/*
	 * @POST
	 * 
	 * @Path("/SQLServer2008R2")
	 * 
	 * @Consumes(MediaType.APPLICATION_JSON)
	 * 
	 * @Produces(MediaType.APPLICATION_JSON) public Response
	 * uninstallSQLServer2008R2(
	 * 
	 * @QueryParam("ip") String ip ) { Response res = null;
	 * ApplicationUninstallBase a = new ApplicationUninstallBase();JSONObject
	 * entity = new JSONObject();
	 * 
	 * 
	 * try {
	 * 
	 * 
	 * int eid = a.sendUninstallSQLServer2008R2Msg( ip); entity.put("eid", eid);
	 * entity.put("status", "虚拟机已接收卸载SQLServer2008R2请求可查询状态"); res =
	 * Response.ok(entity).build(); } catch (JSONException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } return res; }
	 */
	@POST
	@Path("/SQLServer2008R2")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uninstallSQLServer2008R2(@QueryParam("ip") String ip) throws SQLException {

		Response res = null;
		ApplicationUninstallBase a = new ApplicationUninstallBase();
		JSONObject entity = new JSONObject();// 表示要给用户返回的json对象
		DBOperation dbop = new DBOperation();
		GetInstallPath getpath = new GetInstallPath();
		String path = getpath.getpath(ip, "sqlserver2008r2");

		try {
			// 判断该软件有没有在该虚拟机上安装过
			String dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
					"sqlserver2008r2");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要卸载，返回错误码0x0201000
				entity.put("code", "0x0201000");
			} else {// 软件已安装，正常卸载
					// 将消息发送给Agent成功以后，返回一个代表操作id的代号eid，同时这也是数据库中存储的代表该操作的eid
				int eid = a.sendUninstallSQLServer2008R2Msg(ip,path);
				// 构造要返回给用户的json对象
				entity.put("eid", eid);
				entity.put("status", "虚拟机已接收卸载SQLServer2008R2请求可查询状态");//
			}
			dbop.close();
			getpath.close();
			res = Response.ok(entity).build();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}


	/*
	 * @POST
	 * 
	 * @Path("/Oracle11g")
	 * 
	 * @Consumes(MediaType.APPLICATION_JSON)
	 * 
	 * @Produces(MediaType.APPLICATION_JSON) public Response uninstallOracle11g(
	 * 
	 * @QueryParam("ip") String ip,
	 * 
	 * @QueryParam("ip") String path ) { Response res = null;
	 * ApplicationUninstallBase a = new ApplicationUninstallBase();JSONObject
	 * entity = new JSONObject();
	 * 
	 * 
	 * try {
	 * 
	 * int eid = a.sendUninstallOracle11gMsg( ip,path); entity.put("eid", eid);
	 * entity.put("status", "虚拟机已接收卸载Oracle11g请求可查询状态"); res =
	 * Response.ok(entity).build(); } catch (JSONException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } return res; }
	 */
	@POST
	@Path("/Oracle11g")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uninstallOracle11g(@QueryParam("ip") String ip) throws SQLException{
		Response res = null;
		ApplicationUninstallBase a = new ApplicationUninstallBase();
		JSONObject entity = new JSONObject();

		DBOperation dbop = new DBOperation();
		GetInstallPath getpath = new GetInstallPath();
		String path = getpath.getpath(ip, "oracle11g");
		try {
			// 判断该软件有没有在该虚拟机上安装过
			String dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
					"oracle11g");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要卸载，返回错误码0x0201000
				entity.put("code", "0x0201000");
			} else {// 软件已安装，正常卸载
					// 将消息发送给Agent成功以后，返回一个代表操作id的代号eid，同时这也是数据库中存储的代表该操作的eid
				int eid = a.sendUninstallOracle11gMsg(ip, path);
				// 构造要返回给用户的json对象
				entity.put("eid", eid);
				entity.put("status", "虚拟机已接收卸载Oracle11g请求可查询状态");//
			}
			dbop.close();
			getpath.close();
			res = Response.ok(entity).build();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	/*
	 * @POST
	 * 
	 * @Path("/Oracle11g_Linux")
	 * 
	 * @Consumes(MediaType.APPLICATION_JSON)
	 * 
	 * @Produces(MediaType.APPLICATION_JSON) public Response uninstallOracle11g(
	 * 
	 * @QueryParam("ip") String ip,
	 * 
	 * @QueryParam("oraclepath") String oraclepath,
	 * 
	 * @QueryParam("user") String user ) { Response res = null;
	 * ApplicationUninstallBase a = new ApplicationUninstallBase(); JSONObject
	 * entity = new JSONObject();
	 * 
	 * 
	 * try {
	 * 
	 * int eid = a.sendUninstallOracle11gLinuxMsg( ip,oraclepath,user);
	 * entity.put("eid", eid); entity.put("status", "虚拟机已接收卸载Oracle11g请求可查询状态");
	 * res = Response.ok(entity).build(); } catch (JSONException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } return res; }
	 */
	@POST
	@Path("/Oracle11g_Linux")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uninstallOracle11gOnLinux(@QueryParam("ip") String ip) throws SQLException {
		Response res = null;
		ApplicationUninstallBase a = new ApplicationUninstallBase();
		JSONObject entity = new JSONObject();
		DBOperation dbop = new DBOperation();
		GetInstallPath getpath = new GetInstallPath();
		String oraclepath = getpath.getpath(ip, "oracle11g");

		try {
			// 判断该软件有没有在该虚拟机上安装过
			String dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
					"oracle11g");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要卸载，返回错误码0x0201000
				entity.put("code", "0x0201000");
			} else {// 软件已安装，正常卸载
					// 将消息发送给Agent成功以后，返回一个代表操作id的代号eid，同时这也是数据库中存储的代表该操作的eid
				int eid = a
						.sendUninstallOracle11gLinuxMsg(ip, oraclepath);
				// 构造要返回给用户的json对象
				entity.put("eid", eid);
				entity.put("status", "虚拟机已接收卸载Oracle11g请求可查询状态");//
			}
			dbop.close();
			getpath.close();
			res = Response.ok(entity).build();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	@POST
	@Path("/360")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uninstall360(@QueryParam("ip") String ip) throws SQLException {
		Response res = null;
		ApplicationUninstallBase a = new ApplicationUninstallBase();
		JSONObject entity = new JSONObject();// 表示要给用户返回的json对象
		DBOperation dbop = new DBOperation();
		GetInstallPath getpath = new GetInstallPath();
		String path = getpath.getpath(ip, "360");
		try {
			// 判断该软件有没有在该虚拟机上安装过
			String dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
					"360");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要卸载，返回错误码0x0201000
				entity.put("code", "0x0201000");
			} else {// 软件已安装，正常卸载
					// 将消息发送给Agent成功以后，返回一个代表操作id的代号eid，同时这也是数据库中存储的代表该操作的eid
				int eid = a.sendUninstall360Msg(ip,path);
				// 构造要返回给用户的json对象
				entity.put("eid", eid);
				entity.put("status", "虚拟机已接收卸载360请求可查询状态");//
			}
			dbop.close();
			getpath.close();
			res = Response.ok(entity).build();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

}
