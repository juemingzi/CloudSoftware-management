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

import sample.DBOP.AppVersionMng;
import sample.DBOP.DBForSCS;
import sample.DBOP.DBOperation;
import sample.DBOP.GetInstallPath;
import sample.hello.bean.ApplicationUpdateBase;

@Path("/AppUpdate")
public class AppUpdateResource {

	@POST
	@Path("/MySql")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateMySql(@QueryParam("ip") String ip,
			@QueryParam("updatePath") String updatePath,
			@QueryParam("rootPswd") String rootPswd,
			@QueryParam("version") String version) throws SQLException {
		Response res = null;
		ApplicationUpdateBase a = new ApplicationUpdateBase();
		JSONObject entity = new JSONObject();// 表示要给用户返回的json对象
		DBOperation dbop = new DBOperation();
		DBForSCS scs = new DBForSCS();
		GetInstallPath getpath = new GetInstallPath();
		String[] scIPAddr = new String[2];
		AppVersionMng avm = new AppVersionMng();
		if(updatePath.equals("")){
			updatePath = getpath.getpath(ip, "mysql");
		}else{
			updatePath = updatePath + "/Mysql";
		}
		try {
			String os = dbop.getHostOSByIP(ip);
			// 由程序选择数据库里最新的版本号
			String newestV = dbop.queryAppNewestVersion("mysql", os);
			if (version.isEmpty()) {
				version = newestV;
			}
			// 判断该软件有没有在该虚拟机上安装过
			String dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
					"mysql");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要更新，提示错误码0x0401000
				entity.put("code", "0x0401000");
			} else if (dbSoftVersion.equals(newestV)) {// 表示该软件在该虚拟机上安装了最新的版本，无需更新
				entity.put("code", "0x0401003");
			} else if (dbSoftVersion.equals(version)) {// 表示用户输入的版本和已经安装的版本相同，无需更新,提示错误码
				entity.put("code", "0x0401002");
			} else {
				String tempNewVersion = avm.whichIsNew(dbSoftVersion, version);
				if (tempNewVersion.equals(dbSoftVersion)) {// 表示现在已将安装的版本较新，无需更新,提示错误码
					entity.put("code", "0x0401001");
				} else {// 用户输入的版本比较新，可以正常更新
					boolean status = scs.getSetupStatus(ip, "mysql");
					if (status) {

						scIPAddr = dbop.getRCAddrByIP("hp", ip, "mysql",
								version);

						/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
						int spaceRequirement = dbop
								.querySpaceRequirementForAppInstall("mysql",
										version,os);
						int zipSpace = dbop.queryZipSpaceForAppInstall("mysql",
								version,os);
						String uninstallPath = getpath.getpath(ip, "mysql");
						// 将消息发送给Agent成功以后，返回一个代表操作id的代号eid，同时这也是数据库中存储的代表该操作的eid
						int eid = a.sendUpdateMySqlMsg(ip, scIPAddr,
								spaceRequirement, zipSpace, uninstallPath,
								updatePath, rootPswd);
						String command = "uninstallPath=" + uninstallPath + ";"
								+ "installPath=" + updatePath + ";"
								+ "rootPswd=" + rootPswd + ";" + scIPAddr[0]
								+ ";" + scIPAddr[1];
						scs.InsertToSetup(ip, "mysql", version, updatePath,
								eid, command);
						// 构造要返回给用户的json对象
						entity.put("eid", eid);
						entity.put("status", "虚拟机已接收更新mysql请求可查询状态");//
					} else {
						entity.put("code", "0x1100001");
					}
				}
			}
			dbop.close();
			getpath.close();
			scs.close();
			res = Response.ok(entity).build();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	@POST
	@Path("/MySql_Linux")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateMySqlOnLinux(@QueryParam("ip") String ip,
			@QueryParam("updatePath") String updatePath,
			@QueryParam("version") String version) throws SQLException {
		Response res = null;
		ApplicationUpdateBase a = new ApplicationUpdateBase();
		JSONObject entity = new JSONObject();// 表示要给用户返回的json对象
		DBOperation dbop = new DBOperation();
		DBForSCS scs = new DBForSCS();
		GetInstallPath getpath = new GetInstallPath();
		String[] scIPAddr = new String[2];
		AppVersionMng avm = new AppVersionMng();
		if(updatePath.equals("")){
			updatePath = getpath.getpath(ip, "mysql");
		}else{
			updatePath = updatePath + "/Mysql";
		}
		try {
			String os = dbop.getHostOSByIP(ip);
			// 由程序选择数据库里最新的版本号
			String newestV = dbop.queryAppNewestVersion("mysql", os);
			if (version.isEmpty()) {
				version = newestV;
			}
			// 判断该软件有没有在该虚拟机上安装过
			String dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
					"mysql");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要更新，提示错误码0x0401000
				entity.put("code", "0x0401000");
			} else if (dbSoftVersion.equals(newestV)) {// 表示该软件在该虚拟机上安装了最新的版本，无需更新
				entity.put("code", "0x0401003");
			} else if (dbSoftVersion.equals(version)) {// 表示用户输入的版本和已经安装的版本相同，无需更新,提示错误码
				entity.put("code", "0x0401002");
			} else {
				String tempNewVersion = avm.whichIsNew(dbSoftVersion, version);
				if (tempNewVersion.equals(dbSoftVersion)) {// 表示现在已将安装的版本较新，无需更新,提示错误码
					entity.put("code", "0x0401001");
				} else {
					boolean status = scs.getSetupStatus(ip, "mysql");
					if (status) {
						// 用户输入的版本比较新，可以正常更新
						scIPAddr = dbop.getRCAddrByIP("hp", ip, "mysql",
								version);
						String uninstallPath = getpath.getpath(ip, "mysql");
						/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
						int spaceRequirement = dbop
								.querySpaceRequirementForAppInstall("mysql",
										version,os);

						int eid = a.sendUpdateMySqlOnLinuxMsg(ip, scIPAddr,
								spaceRequirement,updatePath,uninstallPath);
						String command = "installPath="+updatePath+";"+"uninstallPath="+uninstallPath+";"+scIPAddr[0] + ";" + scIPAddr[1];
						scs.InsertToSetup(ip, "mysql", version,
								updatePath, eid, command);
						entity.put("eid", eid);
						entity.put("status", "虚拟机已接收更新mysql请求可查询状态");
					} else {
						entity.put("code", "0x1100001");
					}
				}
			}
			dbop.close();
			getpath.close();
			scs.close();
			res = Response.ok(entity).build();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	@POST
	@Path("/Tomcat")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateTomcat(@QueryParam("ip") String ip,
			@QueryParam("updatePath") String updatePath,
			@QueryParam("jdkPath") String jdkPath,
			@QueryParam("version") String version) throws SQLException {
		Response res = null;
		ApplicationUpdateBase a = new ApplicationUpdateBase();
		JSONObject entity = new JSONObject();// 表示要给用户返回的json对象
		DBOperation dbop = new DBOperation();
		DBForSCS scs = new DBForSCS();
		GetInstallPath getpath = new GetInstallPath();
		String[] scIPAddr = new String[2];
		AppVersionMng avm = new AppVersionMng();
		String uninstallPath = getpath.getpath(ip, "tomcat");
		if(updatePath.equals("")){
			updatePath = uninstallPath;
		}else{
			updatePath = updatePath + "/Tomcat";
		}
		try {
			String os = dbop.getHostOSByIP(ip);
			// 由程序选择数据库里最新的版本号
			String newestV = dbop.queryAppNewestVersion("tomcat", os);
			if (version.isEmpty()) {
				version = newestV;
			}
			// 判断该软件有没有在该虚拟机上安装过
			String dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
					"tomcat");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要更新，提示错误码0x0401000
				entity.put("code", "0x0401000");
			} else if (dbSoftVersion.equals(newestV)) {// 表示该软件在该虚拟机上安装了最新的版本，无需更新
				entity.put("code", "0x0401003");
			} else if (dbSoftVersion.equals(version)) {// 表示用户输入的版本和已经安装的版本相同，无需更新,提示错误码
				entity.put("code", "0x0401002");
			} else {
				String tempNewVersion = avm.whichIsNew(dbSoftVersion, version);
				if (tempNewVersion.equals(dbSoftVersion)) {// 表示现在已将安装的版本较新，无需更新,提示错误码
					entity.put("code", "0x0401001");
				} else {// 用户输入的版本比较新，可以正常更新
					boolean status = scs.getSetupStatus(ip, "tomcat");
					if (status) {
						scIPAddr = dbop.getRCAddrByIP("hp", ip, "tomcat",
								version);

						/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
						int spaceRequirement = dbop
								.querySpaceRequirementForAppInstall("tomcat",
										version,os);
						int zipSpace = dbop.queryZipSpaceForAppInstall(
								"tomcat", version,os);

						// 将消息发送给Agent成功以后，返回一个代表操作id的代号eid，同时这也是数据库中存储的代表该操作的eid
						int eid = a
								.sendUpdateTomcatMsg(ip, scIPAddr,
										spaceRequirement, zipSpace, updatePath,
										jdkPath,uninstallPath);
						String command = "uninstallpath=" + uninstallPath + ";"+"installPath=" + updatePath + ";"
								+ "jdkpath=" + jdkPath + ";" + scIPAddr[0] + ";"
								+ scIPAddr[1];
						scs.InsertToSetup(ip, "tomcat", version, updatePath,
								eid, command);
						// 构造要返回给用户的json对象
						entity.put("eid", eid);
						entity.put("status", "虚拟机已接收更新tomcat请求可查询状态");//
					} else {
						entity.put("status", "0x1100001");
					}
				}
			}
			dbop.close();
			getpath.close();
			scs.close();
			res = Response.ok(entity).build();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	@POST
	@Path("/Tomcat_Linux")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateTomcatOnLinux(@QueryParam("ip") String ip,
			@QueryParam("updatePath") String updatePath,
			@QueryParam("jdkPath") String jdkPath,
			@QueryParam("version") String version) throws SQLException {
		Response res = null;
		ApplicationUpdateBase a = new ApplicationUpdateBase();
		JSONObject entity = new JSONObject();// 表示要给用户返回的json对象
		DBOperation dbop = new DBOperation();
		DBForSCS scs = new DBForSCS();
		GetInstallPath getpath = new GetInstallPath();
		String[] scIPAddr = new String[2];
		AppVersionMng avm = new AppVersionMng();
		String uninstallPath = getpath.getpath(ip, "tomcat");
		if(updatePath.equals("")){
			updatePath = uninstallPath;
		}else{
			updatePath = updatePath + "/Tomcat";
		}
		try {
			String os = dbop.getHostOSByIP(ip);
			// 由程序选择数据库里最新的版本号
			String newestV = dbop.queryAppNewestVersion("tomcat", os);
			if (version.isEmpty()) {
				version = newestV;
			}
			// 判断该软件有没有在该虚拟机上安装过
			String dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
					"tomcat");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要更新，提示错误码0x0401000
				entity.put("code", "0x0401000");
			} else if (dbSoftVersion.equals(newestV)) {// 表示该软件在该虚拟机上安装了最新的版本，无需更新
				entity.put("code", "0x0401003");
			} else if (dbSoftVersion.equals(version)) {// 表示用户输入的版本和已经安装的版本相同，无需更新,提示错误码
				entity.put("code", "0x0401002");
			} else {
				String tempNewVersion = avm.whichIsNew(dbSoftVersion, version);
				if (tempNewVersion.equals(dbSoftVersion)) {// 表示现在已将安装的版本较新，无需更新,提示错误码
					entity.put("code", "0x0401001");
				} else {// 用户输入的版本比较新，可以正常更新
					boolean status = scs.getSetupStatus(ip, "tomcat");
					if (status) {
						scIPAddr = dbop.getRCAddrByIP("hp", ip, "tomcat",
								version);

						/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
						int spaceRequirement = dbop
								.querySpaceRequirementForAppInstall("tomcat",
										version,os);
						// 将消息发送给Agent成功以后，返回一个代表操作id的代号eid，同时这也是数据库中存储的代表该操作的eid
						int eid = a.sendUpdateTomcatLinuxMsg(ip, scIPAddr,
								spaceRequirement,uninstallPath, updatePath
								, jdkPath);
						String command = "uninstallPath=" + uninstallPath + ";"
								+ "installPath=" + updatePath + ";"+"jdkPath="+jdkPath
								+ ";"+scIPAddr[0] + ";" + scIPAddr[1];
						
						scs.InsertToSetup(ip, "tomcat", version, updatePath,
								eid, command);
						// 构造要返回给用户的json对象
						entity.put("eid", eid);
						entity.put("status", "虚拟机已接收更新tomcat请求可查询状态");//
					} else {
						entity.put("status", "0x1100001");//
					}
				}
			}
			dbop.close();
			getpath.close();
			scs.close();
			res = Response.ok(entity).build();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	@POST
	@Path("/Apache")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateApache(@QueryParam("ip") String ip,
			@QueryParam("updatePath") String updatePath,
			@QueryParam("emailAddress") String emailAddress,
			@QueryParam("version") String version) throws SQLException {

		Response res = null;
		ApplicationUpdateBase a = new ApplicationUpdateBase();
		JSONObject entity = new JSONObject();// 表示要给用户返回的json对象
		DBOperation dbop = new DBOperation();
		DBForSCS scs = new DBForSCS();
		GetInstallPath getpath = new GetInstallPath();
		String[] scIPAddr = new String[2];
		AppVersionMng avm = new AppVersionMng();
		String uninstallPath = getpath.getpath(ip, "apache");
		if(updatePath.equals("")){
			updatePath = uninstallPath;
		}else{
			updatePath = updatePath + "/Apache";
		}
		try {
			String os = dbop.getHostOSByIP(ip);
			// 由程序选择数据库里最新的版本号
			String newestV = dbop.queryAppNewestVersion("apache", os);
			if (version.isEmpty()) {
				version = newestV;
			}
			// 判断该软件有没有在该虚拟机上安装过
			String dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
					"apache");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要更新，提示错误码0x0401000
				entity.put("code", "0x0401000");
			} else if (dbSoftVersion.equals(newestV)) {// 表示该软件在该虚拟机上安装了最新的版本，无需更新
				entity.put("code", "0x0401003");
			} else if (dbSoftVersion.equals(version)) {// 表示用户输入的版本和已经安装的版本相同，无需更新,提示错误码
				entity.put("code", "0x0401002");
			} else {
				String tempNewVersion = avm.whichIsNew(dbSoftVersion, version);
				if (tempNewVersion.equals(dbSoftVersion)) {// 表示现在已将安装的版本较新，无需更新,提示错误码
					entity.put("code", "0x0401001");
				} else {// 用户输入的版本比较新，可以正常更新
					boolean status = scs.getSetupStatus(ip, "apache");
					if (status) {
						scIPAddr = dbop.getRCAddrByIP("hp", ip, "apache",
								version);

						/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
						int spaceRequirement = dbop
								.querySpaceRequirementForAppInstall("apache",
										version,os);
						int zipSpace = dbop.queryZipSpaceForAppInstall(
								"apache", version,os);
						// 将消息发送给Agent成功以后，返回一个代表操作id的代号eid，同时这也是数据库中存储的代表该操作的eid
						int eid = a.sendUpdateApacheMsg(ip, scIPAddr,
								spaceRequirement, zipSpace, updatePath,
								emailAddress,uninstallPath);
						String command ="uninstallPath="+uninstallPath+";"+ "installPath=" + updatePath + ";"
								+ "emailAddress=" + emailAddress + ";"
								+ scIPAddr[0] + ";" + scIPAddr[1];
						scs.InsertToSetup(ip, "apache", version, updatePath,
								eid, command);
						// 构造要返回给用户的json对象
						entity.put("eid", eid);
						entity.put("status", "虚拟机已接收更新apache请求可查询状态");//
					} else {
						entity.put("status", "0x1100001");//
					}
				}
			}
			dbop.close();
			getpath.close();
			scs.close();
			res = Response.ok(entity).build();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	@POST
	@Path("/Apache_Linux")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateApacheOnLinux(@QueryParam("ip") String ip,
			@QueryParam("updatePath") String updatePath,
			@QueryParam("emailAddress") String emailAddress,
			@QueryParam("version") String version) throws SQLException {
		Response res = null;
		ApplicationUpdateBase a = new ApplicationUpdateBase();
		JSONObject entity = new JSONObject();// 表示要给用户返回的json对象
		DBOperation dbop = new DBOperation();
		DBForSCS scs = new DBForSCS();
		GetInstallPath getpath = new GetInstallPath();
		String[] scIPAddr = new String[2];
		AppVersionMng avm = new AppVersionMng();
		String uninstallPath = getpath.getpath(ip, "apache");
		if(updatePath.equals("")){
			updatePath = uninstallPath;
		}else{
			updatePath = updatePath + "/Apache";
		}
		try {
			String os = dbop.getHostOSByIP(ip);
			// 由程序选择数据库里最新的版本号
			String newestV = dbop.queryAppNewestVersion("apache", os);
			if (version.isEmpty()) {
				version = newestV;
			}
			// 判断该软件有没有在该虚拟机上安装过
			String dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
					"apache");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要更新，提示错误码0x0401000
				entity.put("code", "0x0401000");
			} else if (dbSoftVersion.equals(newestV)) {// 表示该软件在该虚拟机上安装了最新的版本，无需更新
				entity.put("code", "0x0401003");
			} else if (dbSoftVersion.equals(version)) {// 表示用户输入的版本和已经安装的版本相同，无需更新,提示错误码
				entity.put("code", "0x0401002");
			} else {
				String tempNewVersion = avm.whichIsNew(dbSoftVersion, version);
				if (tempNewVersion.equals(dbSoftVersion)) {// 表示现在已将安装的版本较新，无需更新,提示错误码
					entity.put("code", "0x0401001");
				} else {// 用户输入的版本比较新，可以正常更新
					boolean status = scs.getSetupStatus(ip, "apache");
					if (status) {
						scIPAddr = dbop.getRCAddrByIP("hp", ip, "apache",
								version);

						/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
						int spaceRequirement = dbop
								.querySpaceRequirementForAppInstall("apache",
										version,os);

						// 将消息发送给Agent成功以后，返回一个代表操作id的代号eid，同时这也是数据库中存储的代表该操作的eid
						int eid = a.sendUpdateApacheLinuxMsg(ip, scIPAddr,
								spaceRequirement, uninstallPath, updatePath,
								emailAddress);
						String command = "uninstallPath=" + uninstallPath + ";"
								+ "installPath=" + updatePath + ";"
								+ "emailAddress=" + emailAddress + ";"
								+ scIPAddr[0] + ";" + scIPAddr[1];
						scs.InsertToSetup(ip, "apache", version, updatePath,
								eid, command);

						// 构造要返回给用户的json对象
						entity.put("eid", eid);
						entity.put("status", "虚拟机已接收更新apache请求可查询状态");//
					} else {
						entity.put("status", "0x1100001");//
					}
				}

			}
			dbop.close();
			getpath.close();
			scs.close();
			res = Response.ok(entity).build();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	@POST
	@Path("/Nginx")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateNginx(@QueryParam("ip") String ip,
			@QueryParam("updatePath") String updatePath,
			@QueryParam("version") String version) throws SQLException {
		Response res = null;
		ApplicationUpdateBase a = new ApplicationUpdateBase();
		JSONObject entity = new JSONObject();
		DBOperation dbop = new DBOperation();
		DBForSCS scs = new DBForSCS();
		GetInstallPath getpath = new GetInstallPath();
		String[] scIPAddr = new String[2];
		AppVersionMng avm = new AppVersionMng();
		String uninstallPath = getpath.getpath(ip, "nginx");
		if(updatePath.equals("")){
			updatePath = uninstallPath;
		}else{
			updatePath = updatePath + "/Nginx";
		}
		try {
			String os = dbop.getHostOSByIP(ip);
			// 由程序选择数据库里最新的版本号
			String newestV = dbop.queryAppNewestVersion("nginx", os);
			if (version.isEmpty()) {
				version = newestV;
			}
			// 判断该软件有没有在该虚拟机上安装过
			String dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
					"nginx");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要更新，提示错误码0x0401000
				entity.put("code", "0x0401000");
			} else if (dbSoftVersion.equals(newestV)) {// 表示该软件在该虚拟机上安装了最新的版本，无需更新
				entity.put("code", "0x0401003");
			} else if (dbSoftVersion.equals(version)) {// 表示用户输入的版本和已经安装的版本相同，无需更新,提示错误码
				entity.put("code", "0x0401002");
			} else {
				String tempNewVersion = avm.whichIsNew(dbSoftVersion, version);
				if (tempNewVersion.equals(dbSoftVersion)) {// 表示现在已将安装的版本较新，无需更新,提示错误码
					entity.put("code", "0x0401001");
				} else {// 用户输入的版本比较新，可以正常更新
					boolean status = scs.getSetupStatus(ip, "nginx");
					if (status) {
						scIPAddr = dbop.getRCAddrByIP("hp", ip, "nginx",
								version);

						/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
						int spaceRequirement = dbop
								.querySpaceRequirementForAppInstall("nginx",
										version,os);
						int zipSpace = dbop.queryZipSpaceForAppInstall("nginx",
								version,os);
						// 将消息发送给Agent成功以后，返回一个代表操作id的代号eid，同时这也是数据库中存储的代表该操作的eid
						int eid = a.sendUpdateNginxMsg(ip, scIPAddr,
								spaceRequirement, zipSpace, updatePath,
								uninstallPath);
						String command = "uninstallPath=" + uninstallPath + ";"
								+ "installPath=" + updatePath + ";"
								+ scIPAddr[0] + ";" + scIPAddr[1];
						scs.InsertToSetup(ip, "nginx", version, updatePath,
								eid, command);
						// 构造要返回给用户的json对象
						entity.put("eid", eid);
						entity.put("status", "虚拟机已接收更新nginx请求可查询状态");//
					} else {
						entity.put("status", "0x1100001");//
					}
				}
			}
			dbop.close();
			getpath.close();
			scs.close();
			res = Response.ok(entity).build();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	@POST
	@Path("/Nginx_Linux")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateNginxLinux(@QueryParam("ip") String ip,
			@QueryParam("updatePath") String updatePath,
			@QueryParam("version") String version) throws SQLException {
		Response res = null;
		ApplicationUpdateBase a = new ApplicationUpdateBase();
		JSONObject entity = new JSONObject();
		DBOperation dbop = new DBOperation();
		DBForSCS scs = new DBForSCS();
		GetInstallPath getpath = new GetInstallPath();
		String[] scIPAddr = new String[2];
		AppVersionMng avm = new AppVersionMng();
		String uninstallPath = getpath.getpath(ip, "nginx");
		if(updatePath.equals("")){
			updatePath = uninstallPath;
		}else{
			updatePath = updatePath + "/Nginx";
		}
		try {
			String os = dbop.getHostOSByIP(ip);
			// 由程序选择数据库里最新的版本号
			String newestV = dbop.queryAppNewestVersion("nginx", os);
			if (version.isEmpty()) {
				version = newestV;
			}
			// 判断该软件有没有在该虚拟机上安装过
			String dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
					"nginx");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要更新，提示错误码0x0401000
				entity.put("code", "0x0401000");
			} else if (dbSoftVersion.equals(newestV)) {// 表示该软件在该虚拟机上安装了最新的版本，无需更新
				entity.put("code", "0x0401003");
			} else if (dbSoftVersion.equals(version)) {// 表示用户输入的版本和已经安装的版本相同，无需更新,提示错误码
				entity.put("code", "0x0401002");
			} else {
				String tempNewVersion = avm.whichIsNew(dbSoftVersion, version);
				if (tempNewVersion.equals(dbSoftVersion)) {// 表示现在已将安装的版本较新，无需更新,提示错误码
					entity.put("code", "0x0401001");
				} else {// 用户输入的版本比较新，可以正常更新
					boolean status = scs.getSetupStatus(ip, "nginx");
					if (status) {
						scIPAddr = dbop.getRCAddrByIP("hp", ip, "nginx",
								version);

						/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
						int spaceRequirement = dbop
								.querySpaceRequirementForAppInstall("nginx",
										version,os);

						// 将消息发送给Agent成功以后，返回一个代表操作id的代号eid，同时这也是数据库中存储的代表该操作的eid
						int eid = a.sendUpdateNginxLinuxMsg(ip, scIPAddr,
								spaceRequirement, updatePath,uninstallPath);
						String command = "uninstallPath="+uninstallPath+";"+"installPath=" + updatePath + ";"
								+ scIPAddr[0] + ";" + scIPAddr[1];
						scs.InsertToSetup(ip, "nginx", version, updatePath,
								eid, command);
						// 构造要返回给用户的json对象
						entity.put("eid", eid);
						entity.put("status", "虚拟机已接收更新nginx请求可查询状态");//
					} else {
						entity.put("status", "0x1100001");//
					}
				}
			}
			dbop.close();
			getpath.close();
			scs.close();
			res = Response.ok(entity).build();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	@POST
	@Path("/ZendGuardLoader")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateZendGuardLoader(@QueryParam("ip") String ip,
			@QueryParam("phpPath") String phpPath,
			@QueryParam("version") String version) throws SQLException {
		Response res = null;
		ApplicationUpdateBase a = new ApplicationUpdateBase();
		JSONObject entity = new JSONObject();
		DBOperation dbop = new DBOperation();
		DBForSCS scs = new DBForSCS();
		GetInstallPath getpath = new GetInstallPath();
		String[] scIPAddr = new String[2];
		AppVersionMng avm = new AppVersionMng();
		String uninstallPath = getpath.getpath(ip, "zendguardloader");
		if(phpPath.equals("")){
			phpPath = uninstallPath;
		}
		try {
			String os = dbop.getHostOSByIP(ip);
			// 由程序选择数据库里最新的版本号
			String newestV = dbop.queryAppNewestVersion("zendguardloader", os);
			if (version.isEmpty()) {
				version = newestV;
			}
			// 判断该软件有没有在该虚拟机上安装过
			String dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
					"zendguardloader");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要更新，提示错误码0x0401000
				entity.put("code", "0x0401000");
			} else if (dbSoftVersion.equals(newestV)) {// 表示该软件在该虚拟机上安装了最新的版本，无需更新
				entity.put("code", "0x0401003");
			} else if (dbSoftVersion.equals(version)) {// 表示用户输入的版本和已经安装的版本相同，无需更新,提示错误码
				entity.put("code", "0x0401002");
			} else {
				String tempNewVersion = avm.whichIsNew(dbSoftVersion, version);
				if (tempNewVersion.equals(dbSoftVersion)) {// 表示现在已将安装的版本较新，无需更新,提示错误码
					entity.put("code", "0x0401001");
				} else {// 用户输入的版本比较新，可以正常更新
					boolean status = scs.getSetupStatus(ip, "zendguardloader");
					if (status) {
						scIPAddr = dbop.getRCAddrByIP("hp", ip,
								"zendguardloader", version);

						/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
						int spaceRequirement = dbop
								.querySpaceRequirementForAppInstall(
										"zendguardloader", version,os);
						int zipSpace = dbop.queryZipSpaceForAppInstall(
								"zendguardloader", version,os);

						// 将消息发送给Agent成功以后，返回一个代表操作id的代号eid，同时这也是数据库中存储的代表该操作的eid
						int eid = a.sendUpdateZendGuardLoaderMsg(ip, scIPAddr,
								spaceRequirement, zipSpace, phpPath,uninstallPath);

						String command = "uninstallPath="+uninstallPath+";"+"installPath=" + phpPath + ";"
								+ scIPAddr[0] + ";" + scIPAddr[1];
						scs.InsertToSetup(ip, "zendguardloader", version,
								phpPath, eid, command);

						// 构造要返回给用户的json对象
						entity.put("eid", eid);
						entity.put("status", "虚拟机已接收更新zendGuardLoader请求可查询状态");//
					} else {
						entity.put("status", "0x1100001");//
					}
				}
			}
			dbop.close();
			getpath.close();
			scs.close();
			res = Response.ok(entity).build();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	@POST
	@Path("/Python")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updatePython(@QueryParam("ip") String ip,
			@QueryParam("updatePath") String updatePath,
			@QueryParam("version") String version) throws SQLException {
		Response res = null;
		ApplicationUpdateBase a = new ApplicationUpdateBase();
		JSONObject entity = new JSONObject();// 表示要给用户返回的json对象
		DBOperation dbop = new DBOperation();
		DBForSCS scs = new DBForSCS();
		GetInstallPath getpath = new GetInstallPath();
		String[] scIPAddr = new String[2];
		AppVersionMng avm = new AppVersionMng();
		String uninstallPath = getpath.getpath(ip, "python");
		if(updatePath.equals("")){
			updatePath = uninstallPath;
		}else{
			updatePath = updatePath + "/Python";
		}
		try {
			String os = dbop.getHostOSByIP(ip);
			// 由程序选择数据库里最新的版本号
			String newestV = dbop.queryAppNewestVersion("python", os);
			if (version.isEmpty()) {
				version = newestV;
			}
			// 判断该软件有没有在该虚拟机上安装过
			String dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
					"python");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要更新，提示错误码0x0401000
				entity.put("code", "0x0401000");
			} else if (dbSoftVersion.equals(newestV)) {// 表示该软件在该虚拟机上安装了最新的版本，无需更新
				entity.put("code", "0x0401003");
			} else if (dbSoftVersion.equals(version)) {// 表示用户输入的版本和已经安装的版本相同，无需更新,提示错误码
				entity.put("code", "0x0401002");
			} else {
				String tempNewVersion = avm.whichIsNew(dbSoftVersion, version);
				if (tempNewVersion.equals(dbSoftVersion)) {// 表示现在已将安装的版本较新，无需更新,提示错误码
					entity.put("code", "0x0401001");
				} else {// 用户输入的版本比较新，可以正常更新

					boolean status = scs.getSetupStatus(ip, "python");
					if (status) {
						scIPAddr = dbop.getRCAddrByIP("hp", ip, "python",
								version);

						/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
						int spaceRequirement = dbop
								.querySpaceRequirementForAppInstall("python",
										version,os);
						int zipSpace = dbop.queryZipSpaceForAppInstall(
								"python", version,os);

						// 将消息发送给Agent成功以后，返回一个代表操作id的代号eid，同时这也是数据库中存储的代表该操作的eid
						int eid = a.sendUpdatePythonMsg(ip, scIPAddr,
								spaceRequirement, zipSpace,updatePath,uninstallPath);
						String command = "uninstallPath="+uninstallPath+";"+"installPath="+updatePath+";"+scIPAddr[0] + ";" + scIPAddr[1];
						scs.InsertToSetup(ip, "python", version,
								updatePath, eid, command);
						// 构造要返回给用户的json对象
						entity.put("eid", eid);
						entity.put("status", "虚拟机已接收更新python请求可查询状态");//
					} else {
						entity.put("status", "0x1100001");//
					}
				}
			}
			dbop.close();
			getpath.close();
			scs.close();
			res = Response.ok(entity).build();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	@POST
	@Path("/Python_Linux")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updatePythonOnLinux(@QueryParam("ip") String ip,
			@QueryParam("updatePath") String updatePath,
			@QueryParam("version") String version) throws SQLException {
		Response res = null;
		ApplicationUpdateBase a = new ApplicationUpdateBase();
		JSONObject entity = new JSONObject();// 表示要给用户返回的json对象
		DBOperation dbop = new DBOperation();
		DBForSCS scs = new DBForSCS();
		GetInstallPath getpath = new GetInstallPath();
		String[] scIPAddr = new String[2];
		AppVersionMng avm = new AppVersionMng();
		String uninstallPath = getpath.getpath(ip, "python");
		if(updatePath.equals("")){
			updatePath = uninstallPath;
		}else{
			updatePath = updatePath + "/Python";
		}
		try {
			String os = dbop.getHostOSByIP(ip);
			// 由程序选择数据库里最新的版本号
			String newestV = dbop.queryAppNewestVersion("python", os);
			if (version.isEmpty()) {
				version = newestV;
			}
			// 判断该软件有没有在该虚拟机上安装过
			String dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
					"python");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要更新，提示错误码0x0401000
				entity.put("code", "0x0401000");
			} else if (dbSoftVersion.equals(newestV)) {// 表示该软件在该虚拟机上安装了最新的版本，无需更新
				entity.put("code", "0x0401003");
			} else if (dbSoftVersion.equals(version)) {// 表示用户输入的版本和已经安装的版本相同，无需更新,提示错误码
				entity.put("code", "0x0401002");
			} else {
				String tempNewVersion = avm.whichIsNew(dbSoftVersion, version);
				if (tempNewVersion.equals(dbSoftVersion)) {// 表示现在已将安装的版本较新，无需更新,提示错误码
					entity.put("code", "0x0401001");
				} else {// 用户输入的版本比较新，可以正常更新
					boolean status = scs.getSetupStatus(ip, "python");
					if (status) {
						scIPAddr = dbop.getRCAddrByIP("hp", ip, "python",
								version);

						/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
						int spaceRequirement = dbop
								.querySpaceRequirementForAppInstall("python",
										version,os);
						// 将消息发送给Agent成功以后，返回一个代表操作id的代号eid，同时这也是数据库中存储的代表该操作的eid
						int eid = a.sendUpdatePythonMsgOnLinux(ip, scIPAddr,
								spaceRequirement, uninstallPath, updatePath);

						String command = "uninstallPath=" + uninstallPath + ";"
								+ "installPath=" + updatePath + ";"
								+ scIPAddr[0] + ";" + scIPAddr[1];
						
						scs.InsertToSetup(ip, "python", version, updatePath,
								eid, command);
						// 构造要返回给用户的json对象
						entity.put("eid", eid);
						entity.put("status", "虚拟机已接收更新python请求可查询状态");//

					} else {
						entity.put("status", "0x1100001");//
					}
				}
			}
			dbop.close();
			getpath.close();
			scs.close();
			res = Response.ok(entity).build();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	@POST
	@Path("/Memcached")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateMemcached(@QueryParam("ip") String ip,
			@QueryParam("updatePath") String updatePath,
			@QueryParam("version") String version) throws SQLException {
		Response res = null;
		ApplicationUpdateBase a = new ApplicationUpdateBase();
		JSONObject entity = new JSONObject();
		DBOperation dbop = new DBOperation();
		DBForSCS scs = new DBForSCS();
		GetInstallPath getpath = new GetInstallPath();
		String[] scIPAddr = new String[2];
		AppVersionMng avm = new AppVersionMng();
		String uninstallPath = getpath.getpath(ip, "memcached");
		if(updatePath.equals("")){
			updatePath = uninstallPath;
		}else{
			updatePath = updatePath + "/Memcached";
		}
		try {
			String os = dbop.getHostOSByIP(ip);
			// 由程序选择数据库里最新的版本号
			String newestV = dbop.queryAppNewestVersion("memcached", os);
			if (version.isEmpty()) {
				version = newestV;
			}
			// 判断该软件有没有在该虚拟机上安装过
			String dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
					"memcached");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要更新，提示错误码0x0401000
				entity.put("code", "0x0401000");
			} else if (dbSoftVersion.equals(newestV)) {// 表示该软件在该虚拟机上安装了最新的版本，无需更新
				entity.put("code", "0x0401003");
			} else if (dbSoftVersion.equals(version)) {// 表示用户输入的版本和已经安装的版本相同，无需更新,提示错误码
				entity.put("code", "0x0401002");
			} else {
				String tempNewVersion = avm.whichIsNew(dbSoftVersion, version);
				if (tempNewVersion.equals(dbSoftVersion)) {// 表示现在已将安装的版本较新，无需更新,提示错误码
					entity.put("code", "0x0401001");
				} else {// 用户输入的版本比较新，可以正常更新
					boolean status = scs.getSetupStatus(ip, "memcached");
					if (status) {
						scIPAddr = dbop.getRCAddrByIP("hp", ip, "memcached",
								version);

//						String vs = getpath.getVersion(ip, "memcached");
//						String[] sc = dbop.getRCAddrByIP("hp", ip, "memcached",
//								vs);
//						String uninstallName = sc[1];

						/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
						int spaceRequirement = dbop
								.querySpaceRequirementForAppInstall(
										"memcached", version,os);
						int zipSpace = dbop.queryZipSpaceForAppInstall(
								"memcached", version,os);

						int eid = a.sendUpdateMemcachedMsg(ip, scIPAddr,
								spaceRequirement, zipSpace, updatePath,uninstallPath);
						String command = "installPath="+updatePath+";"+"uninstallPath=" + uninstallPath + ";"+ scIPAddr[0] + ";" + scIPAddr[1];
						scs.InsertToSetup(ip, "memcached", version,
								updatePath, eid, command);
						entity.put("eid", eid);
						entity.put("status", "虚拟机已接收更新memcached请求可查询状态");
					} else {
						entity.put("status", "0x1100001");//
					}
				}
			}
			dbop.close();
			getpath.close();
			scs.close();
			res = Response.ok(entity).build();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	@POST
	@Path("/Memcached_Linux")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateMemcachedOnLinux(@QueryParam("ip") String ip,
			@QueryParam("path") String Path,
			@QueryParam("version") String version) throws SQLException {
		Response res = null;
		ApplicationUpdateBase a = new ApplicationUpdateBase();
		JSONObject entity = new JSONObject();
		DBOperation dbop = new DBOperation();
		DBForSCS scs = new DBForSCS();
		GetInstallPath getpath = new GetInstallPath();
		String[] scIPAddr = new String[2];
		AppVersionMng avm = new AppVersionMng();
		String uninstallPath = getpath.getpath(ip, "memcached");
		if(Path.equals("")){
			Path = uninstallPath;
		}else{
			Path = Path + "/Memcached";
		}
		try {
			String os = dbop.getHostOSByIP(ip);
			// 由程序选择数据库里最新的版本号
			String newestV = dbop.queryAppNewestVersion("memcached", os);
			if (version.isEmpty()) {
				version = newestV;
			}
			// 判断该软件有没有在该虚拟机上安装过
			String dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
					"memcached");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要更新，提示错误码0x0401000
				entity.put("code", "0x0401000");
			} else if (dbSoftVersion.equals(newestV)) {// 表示该软件在该虚拟机上安装了最新的版本，无需更新
				entity.put("code", "0x0401003");
			} else if (dbSoftVersion.equals(version)) {// 表示用户输入的版本和已经安装的版本相同，无需更新,提示错误码
				entity.put("code", "0x0401002");
			} else {
				String tempNewVersion = avm.whichIsNew(dbSoftVersion, version);
				if (tempNewVersion.equals(dbSoftVersion)) {// 表示现在已将安装的版本较新，无需更新,提示错误码
					entity.put("code", "0x0401001");
				} else {// 用户输入的版本比较新，可以正常更新
					boolean status = scs.getSetupStatus(ip, "memcached");
					if (status) {
						scIPAddr = dbop.getRCAddrByIP("hp", ip, "memcached",
								version);

						/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
						int spaceRequirement = dbop
								.querySpaceRequirementForAppInstall(
										"memcached", version,os);
						int eid = a.sendUpdateMemcachedLinuxMsg(ip, scIPAddr,
								spaceRequirement, uninstallPath, Path);

						String command = "uninsatallPath=" + uninstallPath+";"+"installPath="+Path
								+ ";" + scIPAddr[0] + ";" + scIPAddr[1];
						scs.InsertToSetup(ip, "memcached", version, Path, eid,
								command);
						entity.put("eid", eid);
						entity.put("status", "虚拟机已接收更新memcached请求可查询状态");
					} else {
						entity.put("status", "0x1100001");//
					}
				}
			}
			dbop.close();
			getpath.close();
			scs.close();
			res = Response.ok(entity).build();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	@POST
	@Path("/IISRewrite")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateIISRewrite(@QueryParam("ip") String ip,
			@QueryParam("updatePath") String updatePath,
			@QueryParam("version") String version) throws SQLException {
		Response res = null;
		ApplicationUpdateBase a = new ApplicationUpdateBase();
		JSONObject entity = new JSONObject();// 表示要给用户返回的json对象
		DBOperation dbop = new DBOperation();
		DBForSCS scs = new DBForSCS();
		GetInstallPath getpath = new GetInstallPath();
		String[] scIPAddr = new String[2];
		AppVersionMng avm = new AppVersionMng();
		String uninstallPath = getpath.getpath(ip, "iisrewrite");
		if(updatePath.equals("")){
			updatePath = uninstallPath;
		}else{
			updatePath = updatePath + "/IISRewrite";
		}
		try {
			String os = dbop.getHostOSByIP(ip);
			// 由程序选择数据库里最新的版本号
			String newestV = dbop.queryAppNewestVersion("iisrewrite", os);
			if (version.isEmpty()) {
				version = newestV;
			}
			// 判断该软件有没有在该虚拟机上安装过
			String dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
					"iisrewrite");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要更新，提示错误码0x0401000
				entity.put("code", "0x0401000");
			} else if (dbSoftVersion.equals(newestV)) {// 表示该软件在该虚拟机上安装了最新的版本，无需更新
				entity.put("code", "0x0401003");
			} else if (dbSoftVersion.equals(version)) {// 表示用户输入的版本和已经安装的版本相同，无需更新,提示错误码
				entity.put("code", "0x0401002");
			} else {
				String tempNewVersion = avm.whichIsNew(dbSoftVersion, version);
				if (tempNewVersion.equals(dbSoftVersion)) {// 表示现在已将安装的版本较新，无需更新,提示错误码
					entity.put("code", "0x0401001");
				} else {// 用户输入的版本比较新，可以正常更新
					boolean status = scs.getSetupStatus(ip, "iisrewrite");
					if (status) {
						scIPAddr = dbop.getRCAddrByIP("hp", ip, "iisrewrite",
								version);

						/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
						int spaceRequirement = dbop
								.querySpaceRequirementForAppInstall(
										"iisrewrite", version,os);
						int zipSpace = dbop.queryZipSpaceForAppInstall(
								"iisrewrite", version,os);

						// 将消息发送给Agent成功以后，返回一个代表操作id的代号eid，同时这也是数据库中存储的代表该操作的eid
						int eid = a.sendUpdateIISRewriteMsg(ip, scIPAddr,
								spaceRequirement, zipSpace, updatePath,uninstallPath);
						String command ="uninstallPath="+uninstallPath+";"+ "installPath=" + updatePath + ";"
								+ scIPAddr[0] + ";" + scIPAddr[1];
						String update = updatePath.substring(0,updatePath.length()-1);
						scs.InsertToSetup(ip, "iisrewrite", version,
								update, eid, command);
						// 构造要返回给用户的json对象
						entity.put("eid", eid);
						entity.put("status", "虚拟机已接收更新iisrewrite请求可查询状态");//
					} else {
						entity.put("status", "0x1100001");//
					}
				}
			}
			dbop.close();
			getpath.close();
			scs.close();
			res = Response.ok(entity).build();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	@POST
	@Path("/FTP")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateFTP(@QueryParam("ip") String ip,
			@QueryParam("updatePath") String updatePath,
			@QueryParam("version") String version) throws SQLException {

		Response res = null;
		ApplicationUpdateBase a = new ApplicationUpdateBase();
		JSONObject entity = new JSONObject();// 表示要给用户返回的json对象
		DBOperation dbop = new DBOperation();
		DBForSCS scs = new DBForSCS();
		GetInstallPath getpath = new GetInstallPath();
		String[] scIPAddr = new String[2];
		AppVersionMng avm = new AppVersionMng();
		String uninstallPath = getpath.getpath(ip, "ftp");
		if(updatePath.equals("")){
			updatePath = uninstallPath;
		}else{
			updatePath = updatePath + "/FTP";
		}
		try {
			String os = dbop.getHostOSByIP(ip);
			// 由程序选择数据库里最新的版本号
			String newestV = dbop.queryAppNewestVersion("ftp", os);
			if (version.isEmpty()) {
				version = newestV;
			}
			// 判断该软件有没有在该虚拟机上安装过
			String dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
					"ftp");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要更新，提示错误码0x0401000
				entity.put("code", "0x0401000");
			} else if (dbSoftVersion.equals(newestV)) {// 表示该软件在该虚拟机上安装了最新的版本，无需更新
				entity.put("code", "0x0401003");
			} else if (dbSoftVersion.equals(version)) {// 表示用户输入的版本和已经安装的版本相同，无需更新,提示错误码
				entity.put("code", "0x0401002");
			} else {
				String tempNewVersion = avm.whichIsNew(dbSoftVersion, version);
				if (tempNewVersion.equals(dbSoftVersion)) {// 表示现在已将安装的版本较新，无需更新,提示错误码
					entity.put("code", "0x0401001");
				} else {// 用户输入的版本比较新，可以正常更新
					boolean status = scs.getSetupStatus(ip, "ftp");
					if (status) {
						scIPAddr = dbop.getRCAddrByIP("hp", ip, "ftp", version);

						/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
						int spaceRequirement = dbop
								.querySpaceRequirementForAppInstall("ftp",
										version,os);
						int zipSpace = dbop.queryZipSpaceForAppInstall("ftp",
								version,os);

						// 将消息发送给Agent成功以后，返回一个代表操作id的代号eid，同时这也是数据库中存储的代表该操作的eid
						int eid = a.sendUpdateFTPMsg(ip, scIPAddr,
								spaceRequirement, zipSpace, updatePath,uninstallPath);
						String command = "uninstallPath="+uninstallPath+";"+"installPath=" + updatePath + ";"
								+ scIPAddr[0] + ";" + scIPAddr[1];
						
						scs.InsertToSetup(ip, "ftp", version, updatePath, eid,
								command);
						// 构造要返回给用户的json对象
						entity.put("eid", eid);
						entity.put("status", "虚拟机已接收更新ftp请求可查询状态");//
					} else {
						entity.put("status", "0x1100001");//
					}
				}
			}
			dbop.close();
			getpath.close();
			scs.close();
			res = Response.ok(entity).build();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	@POST
	@Path("/FTP_Linux")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateFTPOnLinux(@QueryParam("ip") String ip,
			@QueryParam("updatePath") String updatePath,
			@QueryParam("version") String version) throws SQLException {
		Response res = null;
		ApplicationUpdateBase a = new ApplicationUpdateBase();
		JSONObject entity = new JSONObject();// 表示要给用户返回的json对象
		DBOperation dbop = new DBOperation();
		DBForSCS scs = new DBForSCS();
		GetInstallPath getpath = new GetInstallPath();
		String[] scIPAddr = new String[2];
		AppVersionMng avm = new AppVersionMng();
		String uninstallPath = getpath.getpath(ip, "ftp");
		if(updatePath.equals("")){
			updatePath = uninstallPath;
		}else{
			updatePath = updatePath + "/FTP";
		}
		try {
			String os = dbop.getHostOSByIP(ip);
			// 由程序选择数据库里最新的版本号
			String newestV = dbop.queryAppNewestVersion("ftp", os);
			if (version.isEmpty()) {
				version = newestV;
			}
			// 判断该软件有没有在该虚拟机上安装过
			String dbSoftVersion = dbop.queryHostappTableForSoftwareVersion(ip,
					"ftp");
			if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，却要更新，提示错误码0x0401000
				entity.put("code", "0x0401000");
			} else if (dbSoftVersion.equals(newestV)) {// 表示该软件在该虚拟机上安装了最新的版本，无需更新
				entity.put("code", "0x0401003");
			} else if (dbSoftVersion.equals(version)) {// 表示用户输入的版本和已经安装的版本相同，无需更新,提示错误码
				entity.put("code", "0x0401002");
			} else {
				String tempNewVersion = avm.whichIsNew(dbSoftVersion, version);
				if (tempNewVersion.equals(dbSoftVersion)) {// 表示现在已将安装的版本较新，无需更新,提示错误码
					entity.put("code", "0x0401001");
				} else {// 用户输入的版本比较新，可以正常更新
					boolean status = scs.getSetupStatus(ip, "ftp");
					if (status) {

						scIPAddr = dbop.getRCAddrByIP("hp", ip, "ftp", version);

						/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
						int spaceRequirement = dbop
								.querySpaceRequirementForAppInstall("ftp",
										version,os);

						// 将消息发送给Agent成功以后，返回一个代表操作id的代号eid，同时这也是数据库中存储的代表该操作的eid
						int eid = a.sendUpdateFTPMsgOnLinux(ip, scIPAddr,
								spaceRequirement,updatePath,uninstallPath);
						String command = "uninstallPath="+uninstallPath+";"+"installPath="+updatePath+";"+scIPAddr[0] + ";" + scIPAddr[1];
						scs.InsertToSetup(ip, "ftp", version, updatePath,
								eid, command);
						// 构造要返回给用户的json对象
						entity.put("eid", eid);
						entity.put("status", "虚拟机已接收更新ftp请求可查询状态");//
					} else {
						entity.put("status", "0x1100001");//
					}
				}
			}
			dbop.close();
			getpath.close();
			scs.close();
			res = Response.ok(entity).build();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
}
