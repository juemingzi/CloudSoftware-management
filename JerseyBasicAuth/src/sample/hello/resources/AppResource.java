package sample.hello.resources;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import sample.DBOP.*;
import sample.hello.bean.ApplicationBase;

@Path("/AppInstallation")
public class AppResource {
	@POST
	@Path("/MySql")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response installMySql(@QueryParam("ip") List<String> ip,
			@QueryParam("installPath") String installPath,
			@QueryParam("rootPswd") String rootPswd,
			@QueryParam("version") String version,
			@QueryParam("cover") String cover) throws SQLException {
		Response res = null;
		ApplicationBase a = new ApplicationBase();
		DBOperation dbop = new DBOperation();
		GetInstallPath getpath = new GetInstallPath();
		DBForSCS scs = new DBForSCS();
		String command = null;
		String[] scIPAddr = new String[2];
		if(installPath.equals("")){
			installPath = "C:/Mysql";
		}else{
			installPath = installPath+ "/Mysql";
		}
		try {
			JSONArray jarr = new JSONArray();
			for (int i = 0; i < ip.size(); i++) {
				boolean status = scs.getSetupStatus(ip.get(i), "mysql");
				JSONObject entity = new JSONObject();// 表示要给用户返回的json对象
				if (status) {
					String os = dbop.getHostOSByIP(ip.get(i));
					if (version.isEmpty()) {// 如果用户没有输入版本号，则程序需要自己选择一个版本号
						
						// 由程序选择数据库里最新的版本号
						version = dbop.queryAppNewestVersion("mysql", os);
					}
					int opID = 0;
					// 判断该软件有没有在该虚拟机上安装过
					String dbSoftVersion = dbop
							.queryHostappTableForSoftwareVersion(ip.get(i),
									"mysql");
					if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，正常安装
						scIPAddr = dbop.getRCAddrByIP("hp", ip.get(i), "mysql",
								version);
						/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
						int spaceRequirement = dbop
								.querySpaceRequirementForAppInstall("mysql",
										version,os);
						int zipSpace = dbop.queryZipSpaceForAppInstall("mysql",
								version,os);

						opID = a.sendSetupMySqlMsg(ip.get(i), scIPAddr,
								spaceRequirement, zipSpace, installPath,
								rootPswd);
						String uninstallPath = getpath.getpath(ip.get(i), "mysql");
						command = "uninstallPath="+uninstallPath+";"+"installPath="+installPath + ";rootPswd="+rootPswd  +";"+ scIPAddr[0] +";"+scIPAddr[1] ;
						// 可在此处添加setup数据库的添加信息
						scs.InsertToSetup(ip.get(i), "mysql", version,
								installPath, opID,command);
						// 构造要返回给用户的json对象
						entity.put("opID", opID);
						entity.put("status", "虚拟机已接收安装mysql请求可查询状态");//
					} else {
						// 已经安装过则考虑cover
						String oldpath = getpath.getpath(ip.get(i), "mysql");
						if (cover.toUpperCase().equals("YES")) {
							scIPAddr = dbop.getRCAddrByIP("hp", ip.get(i), "mysql",
									version);
							/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
							int spaceRequirement = dbop
									.querySpaceRequirementForAppInstall("mysql",
											version,os);
							int zipSpace = dbop.queryZipSpaceForAppInstall("mysql",
									version,os);

							opID = a.sendSetupMySqlMsg(ip.get(i), scIPAddr,
									spaceRequirement, zipSpace, installPath,
									rootPswd);
							String uninstallPath = getpath.getpath(ip.get(i), "mysql");
							command = "uninstallPath="+uninstallPath+";"+"installPath="+ installPath + ";rootPswd="+rootPswd  +";"+ scIPAddr[0] +";"+scIPAddr[1] ;
							// 可在此处添加setup数据库的添加信息
							scs.InsertToSetup(ip.get(i), "mysql", version,
									installPath, opID,command);
							// 构造要返回给用户的json对象
							entity.put("opID", opID);
							entity.put("status", "虚拟机已接收安装mysql请求可查询状态");//
							

						} else {
							// 软件已安装，用户又发请求再安装,返回错误代码0x0101000
							entity.put("code", "0x0101000  Path: " + oldpath);
						}
					}
				} else {
					entity.put("code", "0x1100001");
				}
				jarr.put(entity);

			}
			res = Response.ok(jarr).build();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbop.close();
		getpath.close();
		scs.close();
		return res;
	}

	@POST
	@Path("/MySql_Linux")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response installMySqlOnLinux(@QueryParam("ip") List<String> ip,
			@QueryParam("installPath") String installPath,
			@QueryParam("version") String version,
			@QueryParam("cover") String cover) throws SQLException {
		Response res = null;
		ApplicationBase a = new ApplicationBase();
		DBOperation dbop = new DBOperation();
		GetInstallPath getpath = new GetInstallPath();
		DBForSCS scs = new DBForSCS();
		String[] scIPAddr = new String[2];
		String command = null;
		if(installPath.equals(""))
			installPath = "/home/Mysql";
		else
			installPath = installPath + "/Mysql";
		try {
			JSONArray jarr = new JSONArray();
			for (int i = 0; i < ip.size(); i++) {

				boolean status = scs.getSetupStatus(ip.get(i), "tomcat");
				JSONObject entity = new JSONObject();// 表示要给用户返回的json对象
				if (status) {
					String os = dbop.getHostOSByIP(ip.get(i));
					if (version.isEmpty()) {// 如果用户没有输入版本号，则程序需要自己选择一个版本号
						
						// 由程序选择数据库里最新的版本号
						version = dbop.queryAppNewestVersion("mysql", os);
					}
					int opID = 0;
					// 判断该软件有没有在该虚拟机上安装过
					String dbSoftVersion = dbop
							.queryHostappTableForSoftwareVersion(ip.get(i),
									"mysql");
					if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，正常安装
						scIPAddr = dbop.getRCAddrByIP("hp", ip.get(i), "mysql",
								version);
						/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
						int spaceRequirement = dbop
								.querySpaceRequirementForAppInstall("mysql",
										version,os);

						opID = a.sendSetupMySqlOnLinuxMsg(ip.get(i), scIPAddr,
								spaceRequirement,installPath);
						String uninstallPath = getpath.getpath(ip.get(i), "mysql");
						command = "uninstallPath="+uninstallPath+";"+"installPath="+installPath+";"+scIPAddr[0] +";"+scIPAddr[1] ;
						scs.InsertToSetup(ip.get(i), "mysql", version,
								installPath, opID,command);
						// 构造要返回给用户的json对象
						entity.put("opID", opID);
						entity.put("status", "虚拟机已接收安装mysql请求可查询状态");//
					} else {
						String oldpath = getpath.getpath(ip.get(i), "mysql");
						if (cover.toUpperCase().equals("YES")) {
							scIPAddr = dbop.getRCAddrByIP("hp", ip.get(i),
									"mysql", version);
							/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
							int spaceRequirement = dbop
									.querySpaceRequirementForAppInstall(
											"mysql", version,os);

							opID = a.sendSetupMySqlOnLinuxMsg(ip.get(i),
									scIPAddr, spaceRequirement,installPath);
							String uninstallPath = getpath.getpath(ip.get(i), "mysql");
							command = "uninstallPath="+uninstallPath+";"+"installPath="+installPath+";"+scIPAddr[0] +";"+scIPAddr[1] ;
							scs.InsertToSetup(ip.get(i), "mysql", version,
									installPath, opID,command);
							// 构造要返回给用户的json对象
							entity.put("opID", opID);
							entity.put("status", "虚拟机已接收安装mysql请求可查询状态");//
						} else {
							// 软件已安装，用户又发请求再安装,返回错误代码0x0101000
							entity.put("code", "0x0101000   path : " + oldpath);
						}

					}
				} else {
					entity.put("code", "0x1100001");
				}
				jarr.put(entity);
			}
			res = Response.ok(jarr).build();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbop.close();
		getpath.close();
		scs.close();
		return res;
	}

	@POST
	@Path("/Tomcat")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response installTomcat(@QueryParam("ip") List<String> ip,
			@QueryParam("installPath") String installPath,
			@QueryParam("jdkPath") String jdkPath,
			@QueryParam("version") String version,
			@QueryParam("cover") String cover) throws SQLException {

		Response res = null;
		ApplicationBase a = new ApplicationBase();
		DBOperation dbop = new DBOperation();
		GetInstallPath getpath = new GetInstallPath();
		DBForSCS scs = new DBForSCS();
		String[] scIPAddr = new String[2];
		String command = null;
		if(installPath.equals("")){
			installPath = "C:/Tomcat";
		}else{
			installPath = installPath +"/Tomcat";
		}
		try {
			JSONArray jarr = new JSONArray();
			for (int i = 0; i < ip.size(); i++) {
				boolean status = scs.getSetupStatus(ip.get(i), "tomcat");
				JSONObject entity = new JSONObject();// 表示要给用户返回的json对象
				if (status) {
					String os = dbop.getHostOSByIP(ip.get(i));
					if (version.isEmpty()) {// 如果用户没有输入版本号，则程序需要自己选择一个版本号
						// System.out.println("用户输入version为空！");
						
						// 由程序选择数据库里最新的版本号
						version = dbop.queryAppNewestVersion("tomcat", os);
						// System.out.println("newest version in db = "+version);
					}
					int opID = 0;
					// 判断该软件有没有在该虚拟机上安装过
					String dbSoftVersion = dbop
							.queryHostappTableForSoftwareVersion(ip.get(i),
									"tomcat");
					if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，正常安装
						scIPAddr = dbop.getRCAddrByIP("hp", ip.get(i),
								"tomcat", version);

						/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
						int spaceRequirement = dbop
								.querySpaceRequirementForAppInstall("tomcat",
										version,os);
						int zipSpace = dbop.queryZipSpaceForAppInstall(
								"tomcat", version,os);

						opID = a.sendSetupTomcatMsg(ip.get(i), scIPAddr,
								spaceRequirement, zipSpace, installPath,
								jdkPath);
						String uninstallPath = getpath.getpath(ip.get(i), "tomcat");
						
						
						command = "uninstallPath="+uninstallPath+";"+"installPath="+installPath + ";jdkPath="+ jdkPath  +";"+ scIPAddr[0] +";"+scIPAddr[1] ;
						// 可在此处添加setup数据库的添加信息
						
						scs.InsertToSetup(ip.get(i), "tomcat", version,
								installPath, opID,command);
						// 构造要返回给用户的json对象
						entity.put("opID", opID);
						entity.put("status", "虚拟机已接收安装tomcat请求可查询状态");//
					} else {
						String oldpath = getpath.getpath(ip.get(i), "tomcat");
						if (cover.toUpperCase().equals("YES")) {
							// 已经安装过则考虑cover

							System.out.println("cover                 :"
									+ cover);

							scIPAddr = dbop.getRCAddrByIP("hp", ip.get(i),
									"tomcat", version);

							/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
							int spaceRequirement = dbop
									.querySpaceRequirementForAppInstall(
											"tomcat", version,os);
							int zipSpace = dbop.queryZipSpaceForAppInstall(
									"tomcat", version,os);

							opID = a.sendSetupTomcatMsg(ip.get(i), scIPAddr,
									spaceRequirement, zipSpace, installPath,
									jdkPath);
							String uninstallPath = getpath.getpath(ip.get(i), "tomcat");
							command = "uninstallPath="+uninstallPath+";"+ "installPath="+installPath + ";jdkPath="+jdkPath  +";"+ scIPAddr[0] +";"+scIPAddr[1] ;
							// 可在此处添加setup数据库的添加信息
						

							scs.InsertToSetup(ip.get(i), "tomcat", version,
									installPath, opID,command);
							// 构造要返回给用户的json对象
							entity.put("opID", opID);
							entity.put("status", "虚拟机已接收安装tomcat请求可查询状态");//
						} else {
							// 软件已安装，用户又发请求再安装,返回错误代码0x0101000
							entity.put("code", "0x0101000   path:" + oldpath);
						}
					}
				} else {
					entity.put("code", "0x1100001");
				}

				jarr.put(entity);
			}
			res = Response.ok(jarr).build();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbop.close();
		getpath.close();
		scs.close();
		return res;
	}
	
	@POST
	@Path("/Tomcat_Linux")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response installTomcatOnLinux(@QueryParam("ip") List<String> ip,
			@QueryParam("installPath") String installPath,
			@QueryParam("jdkPath") String jdkPath,
			@QueryParam("version") String version,
			@QueryParam("cover") String cover) throws SQLException {

		Response res = null;
		ApplicationBase a = new ApplicationBase();
		DBOperation dbop = new DBOperation();
		GetInstallPath getpath = new GetInstallPath();
		DBForSCS scs = new DBForSCS();
		String[] scIPAddr = new String[2];
		String command = null;
		if(installPath.equals("")){
			installPath = "/home/Tomcat";
		}else{
			installPath = installPath +"/Tomcat";
		}
		try {
			JSONArray jarr = new JSONArray();
			for (int i = 0; i < ip.size(); i++) {
				boolean status = scs.getSetupStatus(ip.get(i), "tomcat");
				JSONObject entity = new JSONObject();// 表示要给用户返回的json对象
				if (status) {
					String os = dbop.getHostOSByIP(ip.get(i));
					if (version.isEmpty()) {// 如果用户没有输入版本号，则程序需要自己选择一个版本号
						// System.out.println("用户输入version为空！");
						
						// 由程序选择数据库里最新的版本号
						version = dbop.queryAppNewestVersion("tomcat", os);
						// System.out.println("newest version in db = "+version);
					}
					int opID = 0;
					// 判断该软件有没有在该虚拟机上安装过
					String dbSoftVersion = dbop
							.queryHostappTableForSoftwareVersion(ip.get(i),
									"tomcat");
					if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，正常安装
						scIPAddr = dbop.getRCAddrByIP("hp", ip.get(i),
								"tomcat", version);

						/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
						int spaceRequirement = dbop
								.querySpaceRequirementForAppInstall("tomcat",
										version,os);
						int zipSpace = dbop.queryZipSpaceForAppInstall(
								"tomcat", version,os);

						opID = a.sendSetupTomcatMsg(ip.get(i), scIPAddr,
								spaceRequirement, zipSpace, installPath,
								jdkPath);
						String uninstallPath = getpath.getpath(ip.get(i), "tomcat");
						command = "uninstallPath="+uninstallPath+";"+ "installPath="+installPath + ";jdkPath="+ jdkPath  +";"+ scIPAddr[0] +";"+scIPAddr[1] ;
						// 可在此处添加setup数据库的添加信息
						
						scs.InsertToSetup(ip.get(i), "tomcat", version,
								installPath, opID,command);
						// 构造要返回给用户的json对象
						entity.put("opID", opID);
						entity.put("status", "虚拟机已接收安装tomcat请求可查询状态");//
					} else {
						String oldpath = getpath.getpath(ip.get(i), "tomcat");
						if (cover.toUpperCase().equals("YES")) {
							// 已经安装过则考虑cover

							System.out.println("cover                 :"
									+ cover);

							scIPAddr = dbop.getRCAddrByIP("hp", ip.get(i),
									"tomcat", version);

							/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
							int spaceRequirement = dbop
									.querySpaceRequirementForAppInstall(
											"tomcat", version,os);
							int zipSpace = dbop.queryZipSpaceForAppInstall(
									"tomcat", version,os);

							opID = a.sendSetupTomcatMsg(ip.get(i), scIPAddr,
									spaceRequirement, zipSpace, installPath,
									jdkPath);
							String uninstallPath = getpath.getpath(ip.get(i), "tomcat");
							command = "uninstallPath="+uninstallPath+";"+"installPath="+installPath + ";jdkPath="+jdkPath  +";"+ scIPAddr[0] +";"+scIPAddr[1] ;
							// 可在此处添加setup数据库的添加信息
							scs.InsertToSetup(ip.get(i), "tomcat", version,
									installPath, opID,command);
							// 构造要返回给用户的json对象
							entity.put("opID", opID);
							entity.put("status", "虚拟机已接收安装tomcat请求可查询状态");//
						} else {
							// 软件已安装，用户又发请求再安装,返回错误代码0x0101000
							entity.put("code", "0x0101000   path:" + oldpath);
						}
					}
				} else {
					entity.put("code", "0x1100001");
				}

				jarr.put(entity);
			}
			res = Response.ok(jarr).build();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbop.close();
		getpath.close();
		scs.close();
		return res;
	}

	@POST
	@Path("/Apache")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response installApache(@QueryParam("ip") List<String> ip,
			@QueryParam("installPath") String installPath,
			@QueryParam("emailAddress") String emailAddress,
			@QueryParam("version") String version,
			@QueryParam("cover") String cover) throws SQLException {
		Response res = null;
		ApplicationBase a = new ApplicationBase();
		DBOperation dbop = new DBOperation();
		GetInstallPath getpath = new GetInstallPath();
		DBForSCS scs = new DBForSCS();
		String[] scIPAddr = new String[2];
		String command = null;
		if(installPath.equals("")){
			installPath = "C:/Apache";
		}else{
			installPath = installPath +"/Apache";
		}
		try {
			JSONArray jarr = new JSONArray();
			for (int i = 0; i < ip.size(); i++) {
				JSONObject entity = new JSONObject();// 表示要给用户返回的json对象
				boolean status = scs.getSetupStatus(ip.get(i), "apache");
				String os = dbop.getHostOSByIP(ip.get(i));
				if (status) {
					if (version.isEmpty()) {// 如果用户没有输入版本号，则程序需要自己选择一个版本号
						
						// 由程序选择数据库里最新的版本号
						version = dbop.queryAppNewestVersion("apache", os);
					}
					int opID = 0;
					// 判断该软件有没有在该虚拟机上安装过
					String dbSoftVersion = dbop
							.queryHostappTableForSoftwareVersion(ip.get(i),
									"apache");
					if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，正常安装
						scIPAddr = dbop.getRCAddrByIP("hp", ip.get(i),
								"apache", version);
						/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
						int spaceRequirement = dbop
								.querySpaceRequirementForAppInstall("apache",
										version,os);
						int zipSpace = dbop.queryZipSpaceForAppInstall(
								"apache", version,os);

						opID = a.sendSetupApacheMsg(ip.get(i), scIPAddr,
								spaceRequirement, zipSpace, installPath,
								emailAddress);
						String uninstallPath = getpath.getpath(ip.get(i), "apache");
						command = "uninstallPath="+uninstallPath+";"+"installPath="+ installPath + ";emailAddress="+ emailAddress  +";"+ scIPAddr[0] +";"+scIPAddr[1] ;
						// 可在此处添加setup数据库的添加信息
						
						scs.InsertToSetup(ip.get(i), "apache", version,
								installPath, opID,command);

						// 构造要返回给用户的json对象
						entity.put("opID", opID);
						entity.put("status", "虚拟机已接收安装apache请求可查询状态");//
					} else {
						// 已经安装过则考虑cover
						String oldpath = getpath.getpath(ip.get(i), "apache");
						if (cover.toUpperCase().equals("YES")) {
							scIPAddr = dbop.getRCAddrByIP("hp", ip.get(i),
									"apache", version);
							/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
							int spaceRequirement = dbop
									.querySpaceRequirementForAppInstall(
											"apache", version,os);
							int zipSpace = dbop.queryZipSpaceForAppInstall(
									"apache", version,os);

							opID = a.sendSetupApacheMsg(ip.get(i), scIPAddr,
									spaceRequirement, zipSpace, installPath,
									emailAddress);
							String uninstallPath = getpath.getpath(ip.get(i), "apache");
							command = "uninstallPath="+uninstallPath+";"+"installPath="+ installPath + ";emailAddress="+ emailAddress  +";"+ scIPAddr[0] +";"+scIPAddr[1] ;
							// 可在此处添加setup数据库的添加信息
							scs.InsertToSetup(ip.get(i), "apache", version,
									installPath, opID,command);
							// 构造要返回给用户的json对象
							entity.put("opID", opID);
							entity.put("status", "虚拟机已接收安装apache请求可查询状态");//
						} else {
							// 软件已安装，用户又发请求再安装,返回错误代码0x0101000
							entity.put("code", "0x0101000  path : " + oldpath);
						}

					}
				} else {
					entity.put("code", "0x1100001");
				}
				jarr.put(entity);
			}
			res = Response.ok(jarr).build();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbop.close();
		getpath.close();
		scs.close();
		return res;
	}
	
	@POST
	@Path("/Apache_Linux")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response installApacheOnLinux(@QueryParam("ip") List<String> ip,
			@QueryParam("installPath") String installPath,
			@QueryParam("emailAddress") String emailAddress,
			@QueryParam("version") String version,
			@QueryParam("cover") String cover) throws SQLException {
		Response res = null;
		ApplicationBase a = new ApplicationBase();
		DBOperation dbop = new DBOperation();
		GetInstallPath getpath = new GetInstallPath();
		DBForSCS scs = new DBForSCS();
		String[] scIPAddr = new String[2];
		String command = null;
		if(installPath.equals("")){
			installPath = "/home/Apache";
		}else{
			installPath = installPath +"/Apache";
		}
		try {
			JSONArray jarr = new JSONArray();
			for (int i = 0; i < ip.size(); i++) {
				JSONObject entity = new JSONObject();// 表示要给用户返回的json对象
				boolean status = scs.getSetupStatus(ip.get(i), "apache");
				
				if (status) {
					String os = dbop.getHostOSByIP(ip.get(i));
					if (version.isEmpty()) {// 如果用户没有输入版本号，则程序需要自己选择一个版本号
						
						// 由程序选择数据库里最新的版本号
						version = dbop.queryAppNewestVersion("apache", os);
					}
					int opID = 0;
					// 判断该软件有没有在该虚拟机上安装过
					String dbSoftVersion = dbop
							.queryHostappTableForSoftwareVersion(ip.get(i),
									"apache");
					if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，正常安装
						scIPAddr = dbop.getRCAddrByIP("hp", ip.get(i),
								"apache", version);
						/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
						int spaceRequirement = dbop
								.querySpaceRequirementForAppInstall("apache",
										version,os);
						int zipSpace = dbop.queryZipSpaceForAppInstall(
								"apache", version,os);

						opID = a.sendSetupApacheMsg(ip.get(i), scIPAddr,
								spaceRequirement, zipSpace, installPath,
								emailAddress);
						String uninstallPath = getpath.getpath(ip.get(i), "apache");
						command = "uninstallPath="+uninstallPath+";"+"installPath="+ installPath + ";emailAddress="+ emailAddress  +";"+ scIPAddr[0] +";"+scIPAddr[1] ;
						// 可在此处添加setup数据库的添加信息
						scs.InsertToSetup(ip.get(i), "apache", version,
								installPath, opID,command);

						// 构造要返回给用户的json对象
						entity.put("opID", opID);
						entity.put("status", "虚拟机已接收安装apache请求可查询状态");//
					} else {
						// 已经安装过则考虑cover
						String oldpath = getpath.getpath(ip.get(i), "apache");
						if (cover.toUpperCase().equals("YES")) {
							scIPAddr = dbop.getRCAddrByIP("hp", ip.get(i),
									"apache", version);
							/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
							int spaceRequirement = dbop
									.querySpaceRequirementForAppInstall(
											"apache", version,os);
							int zipSpace = dbop.queryZipSpaceForAppInstall(
									"apache", version,os);

							opID = a.sendSetupApacheMsg(ip.get(i), scIPAddr,
									spaceRequirement, zipSpace, installPath,
									emailAddress);
							String uninstallPath = getpath.getpath(ip.get(i), "apache");
							command = "uninstallPath="+uninstallPath+";"+"installPath="+ installPath + ";emailAddress="+ emailAddress  +";"+ scIPAddr[0] +";"+scIPAddr[1] ;
							// 可在此处添加setup数据库的添加信息
							scs.InsertToSetup(ip.get(i), "apache", version,
									installPath, opID,command);
							// 构造要返回给用户的json对象
							entity.put("opID", opID);
							entity.put("status", "虚拟机已接收安装apache请求可查询状态");//
						} else {
							// 软件已安装，用户又发请求再安装,返回错误代码0x0101000
							entity.put("code", "0x0101000  path : " + oldpath);
						}

					}
				} else {
					entity.put("code", "0x1100001");
				}
				jarr.put(entity);
			}
			res = Response.ok(jarr).build();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbop.close();
		getpath.close();
		scs.close();
		return res;
	}

	@POST
	@Path("/Nginx")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response installNginx(@QueryParam("ip") List<String> ip,
			@QueryParam("installPath") String installPath,
			@QueryParam("version") String version,
			@QueryParam("cover") String cover) throws SQLException {
		Response res = null;
		ApplicationBase a = new ApplicationBase();
		DBOperation dbop = new DBOperation();
		GetInstallPath getpath = new GetInstallPath();
		DBForSCS scs = new DBForSCS();
		String[] scIPAddr = new String[2];
		String command = null;
		if(installPath.equals("")){
			installPath = "C:/Nginx";
		}else{
			installPath = installPath +"/Nginx";
		}
		try {

			JSONArray jarr = new JSONArray();
			for (int i = 0; i < ip.size(); i++) {
				JSONObject entity = new JSONObject();// 表示要给用户返回的json对象
				boolean status = scs.getSetupStatus(ip.get(i), "nginx");
				if (status) {
					String os = dbop.getHostOSByIP(ip.get(i));
					if (version.isEmpty()) {// 如果用户没有输入版本号，则程序需要自己选择一个版本号
						
						// 由程序选择数据库里最新的版本号
						version = dbop.queryAppNewestVersion("nginx", os);
					}
					int opID = 0;
					// 判断该软件有没有在该虚拟机上安装过
					String dbSoftVersion = dbop
							.queryHostappTableForSoftwareVersion(ip.get(i),
									"nginx");
					if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，正常安装
						scIPAddr = dbop.getRCAddrByIP("hp", ip.get(i), "nginx",
								version);
						/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
						int spaceRequirement = dbop
								.querySpaceRequirementForAppInstall("nginx",
										version,os);
						int zipSpace = dbop.queryZipSpaceForAppInstall("nginx",
								version,os);

						opID = a.sendSetupNginxMsg(ip.get(i), scIPAddr,
								spaceRequirement, zipSpace, installPath);
						String uninstallPath = getpath.getpath(ip.get(i), "nginx");
						command = "uninstallPath="+uninstallPath+";"+ "installPath="+installPath + ";"+ scIPAddr[0] +";"+scIPAddr[1] ;
						// 可在此处添加setup数据库的添加信息
						scs.InsertToSetup(ip.get(i), "nginx", version,
								installPath, opID,command);
						// 构造要返回给用户的json对象
						entity.put("opID", opID);
						entity.put("status", "虚拟机已接收安装nginx请求可查询状态");//
					} else {
						// 已经安装过则考虑cover
						String oldpath = getpath.getpath(ip.get(i), "nginx");
						if (cover.toUpperCase().equals("YES")) {
							scIPAddr = dbop.getRCAddrByIP("hp", ip.get(i),
									"nginx", version);
							/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
							int spaceRequirement = dbop
									.querySpaceRequirementForAppInstall(
											"nginx", version,os);
							int zipSpace = dbop.queryZipSpaceForAppInstall(
									"nginx", version,os);

							opID = a.sendSetupNginxMsg(ip.get(i), scIPAddr,
									spaceRequirement, zipSpace, installPath);
							String uninstallPath = getpath.getpath(ip.get(i), "nginx");
							command = "uninstallPath="+uninstallPath+";"+"installPath="+ installPath + ";"+  scIPAddr[0] +";"+scIPAddr[1] ;
							// 可在此处添加setup数据库的添加信息
							scs.InsertToSetup(ip.get(i), "nginx", version,
									installPath, opID,command);
							// 构造要返回给用户的json对象
							entity.put("opID", opID);
							entity.put("status", "虚拟机已接收安装nginx请求可查询状态");//
						} else {
							// 软件已安装，用户又发请求再安装,返回错误代码0x0101000
							entity.put("code", "0x0101000  path : " + oldpath);
						}

					}
				} else {
					entity.put("code", "0x1100001");
				}
				jarr.put(entity);

			}
			res = Response.ok(jarr).build();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbop.close();
		getpath.close();
		scs.close();
		return res;
	}
	
	@POST
	@Path("/Nginx_Linux")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response installNginxOnLinux(@QueryParam("ip") List<String> ip,
			@QueryParam("installPath") String installPath,
			@QueryParam("version") String version,
			@QueryParam("cover") String cover) throws SQLException {
		Response res = null;
		ApplicationBase a = new ApplicationBase();
		DBOperation dbop = new DBOperation();
		GetInstallPath getpath = new GetInstallPath();
		DBForSCS scs = new DBForSCS();
		String[] scIPAddr = new String[2];
		String command = null;
		if(installPath.equals("")){
			installPath = "/home/Nginx";
		}else{
			installPath = installPath +"/Nginx";
		}
		try {

			JSONArray jarr = new JSONArray();
			for (int i = 0; i < ip.size(); i++) {
				JSONObject entity = new JSONObject();// 表示要给用户返回的json对象
				boolean status = scs.getSetupStatus(ip.get(i), "nginx");
				if (status) {
					String os = dbop.getHostOSByIP(ip.get(i));
					if (version.isEmpty()) {// 如果用户没有输入版本号，则程序需要自己选择一个版本号
						
						// 由程序选择数据库里最新的版本号
						version = dbop.queryAppNewestVersion("nginx", os);
					}
					int opID = 0;
					// 判断该软件有没有在该虚拟机上安装过
					String dbSoftVersion = dbop
							.queryHostappTableForSoftwareVersion(ip.get(i),
									"nginx");
					if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，正常安装
						scIPAddr = dbop.getRCAddrByIP("hp", ip.get(i), "nginx",
								version);
						/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
						int spaceRequirement = dbop
								.querySpaceRequirementForAppInstall("nginx",
										version,os);
						int zipSpace = dbop.queryZipSpaceForAppInstall("nginx",
								version,os);

						opID = a.sendSetupNginxMsg(ip.get(i), scIPAddr,
								spaceRequirement, zipSpace, installPath);
						String uninstallPath = getpath.getpath(ip.get(i), "nginx");
						command = "uninstallPath="+uninstallPath+";"+ "installPath="+installPath + ";"+ scIPAddr[0] +";"+scIPAddr[1] ;
						// 可在此处添加setup数据库的添加信息
						scs.InsertToSetup(ip.get(i), "nginx", version,
								installPath, opID,command);
						// 构造要返回给用户的json对象
						entity.put("opID", opID);
						entity.put("status", "虚拟机已接收安装nginx请求可查询状态");//
					} else {
						// 已经安装过则考虑cover
						String oldpath = getpath.getpath(ip.get(i), "nginx");
						if (cover.toUpperCase().equals("YES")) {
							scIPAddr = dbop.getRCAddrByIP("hp", ip.get(i),
									"nginx", version);
							/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
							int spaceRequirement = dbop
									.querySpaceRequirementForAppInstall(
											"nginx", version,os);
							int zipSpace = dbop.queryZipSpaceForAppInstall(
									"nginx", version,os);

							opID = a.sendSetupNginxMsg(ip.get(i), scIPAddr,
									spaceRequirement, zipSpace, installPath);
							String uninstallPath = getpath.getpath(ip.get(i), "nginx");
							command = "uninstallPath="+uninstallPath+";"+ "installPath="+installPath + ";"+  scIPAddr[0] +";"+scIPAddr[1] ;
							// 可在此处添加setup数据库的添加信息
							scs.InsertToSetup(ip.get(i), "nginx", version,
									installPath, opID,command);
							// 构造要返回给用户的json对象
							entity.put("opID", opID);
							entity.put("status", "虚拟机已接收安装nginx请求可查询状态");//
						} else {
							// 软件已安装，用户又发请求再安装,返回错误代码0x0101000
							entity.put("code", "0x0101000  path : " + oldpath);
						}

					}
				} else {
					entity.put("code", "0x1100001");
				}
				jarr.put(entity);

			}
			res = Response.ok(jarr).build();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbop.close();
		getpath.close();
		scs.close();
		return res;
	}

	@POST
	@Path("/ZendGuardLoader")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response installZendGuardLoader(@QueryParam("ip") List<String> ip,
			@QueryParam("phpPath") String phpPath,
			@QueryParam("version") String version,
			@QueryParam("cover") String cover) throws SQLException {
		Response res = null;
		ApplicationBase a = new ApplicationBase();
		DBOperation dbop = new DBOperation();
		GetInstallPath getpath = new GetInstallPath();
		DBForSCS scs = new DBForSCS();
		String[] scIPAddr = new String[2];
		String command = null;
		try {

			JSONArray jarr = new JSONArray();
			for (int i = 0; i < ip.size(); i++) {
				JSONObject entity = new JSONObject();// 表示要给用户返回的json对象
				boolean status = scs.getSetupStatus(ip.get(i),
						"zendguardloader");
				if (status) {
					String os = dbop.getHostOSByIP(ip.get(i));
					if (version.isEmpty()) {// 如果用户没有输入版本号，则程序需要自己选择一个版本号
						
						// 由程序选择数据库里最新的版本号
						version = dbop.queryAppNewestVersion("zendguardloader",
								os);
					}
					int opID = 0;
					// 判断该软件有没有在该虚拟机上安装过
					String dbSoftVersion = dbop
							.queryHostappTableForSoftwareVersion(ip.get(i),
									"zendguardloader");
					if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，正常安装
						scIPAddr = dbop.getRCAddrByIP("hp", ip.get(i),
								"zendguardloader", version);
						/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
						int spaceRequirement = dbop
								.querySpaceRequirementForAppInstall(
										"zendguardloader", version,os);
						int zipSpace = dbop.queryZipSpaceForAppInstall(
								"zendguardloader", version,os);

						opID = a.sendSetupZendGuardLoaderMsg(ip.get(i),
								scIPAddr, spaceRequirement, zipSpace, phpPath);
						String uninstallPath = getpath.getpath(ip.get(i), "zendguardloader");
						command = "uninstallPath="+uninstallPath+";"+"installPath="+ phpPath + ";"+  scIPAddr[0] +";"+scIPAddr[1] ;
						// 可在此处添加setup数据库的添加信息
						scs.InsertToSetup(ip.get(i), "zendguardloader",
								version, phpPath, opID,command);
						// 构造要返回给用户的json对象
						entity.put("opID", opID);
						entity.put("status", "虚拟机已接收安装zendGuardLoader请求可查询状态");//
					} else {
						// 已经安装过则考虑cover
						String oldpath = getpath.getpath(ip.get(i),
								"zendguardloader");
						if (cover.toUpperCase().equals("YES")) {
							scIPAddr = dbop.getRCAddrByIP("hp", ip.get(i),
									"zendguardloader", version);
							/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
							int spaceRequirement = dbop
									.querySpaceRequirementForAppInstall(
											"zendguardloader", version,os);
							int zipSpace = dbop.queryZipSpaceForAppInstall(
									"zendguardloader", version,os);

							opID = a.sendSetupZendGuardLoaderMsg(ip.get(i),
									scIPAddr, spaceRequirement, zipSpace,
									phpPath);
							String uninstallPath = getpath.getpath(ip.get(i), "zendguardloader");
							command = "uninstallPath="+uninstallPath+";"+"installPath="+phpPath + ";"+ scIPAddr[0] +";"+scIPAddr[1] ;
							// 可在此处添加setup数据库的添加信息
							scs.InsertToSetup(ip.get(i), "zendguardloader",
									version, phpPath, opID,command);
							// 构造要返回给用户的json对象
							entity.put("opID", opID);
							entity.put("status",
									"虚拟机已接收安装zendGuardLoader请求可查询状态");//
						} else {
							// 软件已安装，用户又发请求再安装,返回错误代码0x0101000
							entity.put("code", "0x0101000  path : " + oldpath);
						}

					}
				} else {
					entity.put("code", "0x1100001");
				}
				jarr.put(entity);
			}
			res = Response.ok(jarr).build();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbop.close();
		getpath.close();
		scs.close();
		return res;
	}

	@POST
	@Path("/Python")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response installPython(@QueryParam("ip") List<String> ip,
			@QueryParam("installPath") String installPath,
			@QueryParam("version") String version,
			@QueryParam("cover") String cover) throws SQLException {
		Response res = null;
		ApplicationBase a = new ApplicationBase();
		DBOperation dbop = new DBOperation();
		GetInstallPath getpath = new GetInstallPath();
		DBForSCS scs = new DBForSCS();
		String[] scIPAddr = new String[2];
		String command = null;
		if(installPath.equals("")){
			installPath = "C:/Python";
		}else{
			installPath = installPath +"/Python";
		}
		try {
			JSONArray jarr = new JSONArray();
			for (int i = 0; i < ip.size(); i++) {
				JSONObject entity = new JSONObject();// 表示要给用户返回的json对象
				boolean status = scs.getSetupStatus(ip.get(i), "python");
				if (status) {
					String os = dbop.getHostOSByIP(ip.get(i));
					if (version.isEmpty()) {// 如果用户没有输入版本号，则程序需要自己选择一个版本号
						
						// 由程序选择数据库里最新的版本号
						version = dbop.queryAppNewestVersion("python", os);
					}
					int opID = 0;
					// 判断该软件有没有在该虚拟机上安装过
					String dbSoftVersion = dbop
							.queryHostappTableForSoftwareVersion(ip.get(i),
									"python");
					if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，正常安装
						scIPAddr = dbop.getRCAddrByIP("hp", ip.get(i),
								"python", version);
						/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
						int spaceRequirement = dbop
								.querySpaceRequirementForAppInstall("python",
										version,os);
						int zipSpace = dbop.queryZipSpaceForAppInstall(
								"python", version,os);

						opID = a.sendSetupPythonMsg(ip.get(i), scIPAddr,
								spaceRequirement, zipSpace,installPath);
						String uninstallPath = getpath.getpath(ip.get(i), "python");
						command = "uninstallPath="+uninstallPath+";"+ "installPath="+installPath+";"+scIPAddr[0] +";"+scIPAddr[1] ;
						// 插入setup表
						scs.InsertToSetup(ip.get(i), "python", version,
								installPath, opID,command);
						// 构造要返回给用户的json对象
						entity.put("opID", opID);
						entity.put("status", "虚拟机已接收安装python请求可查询状态");//
					} else {
						String oldpath = getpath.getpath(ip.get(i), "python");
						if (cover.toUpperCase().equals("YES")) {
							scIPAddr = dbop.getRCAddrByIP("hp", ip.get(i),
									"python", version);
							/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
							int spaceRequirement = dbop
									.querySpaceRequirementForAppInstall(
											"python", version,os);
							int zipSpace = dbop.queryZipSpaceForAppInstall(
									"python", version,os);

							opID = a.sendSetupPythonMsg(ip.get(i), scIPAddr,
									spaceRequirement, zipSpace,installPath);
							String uninstallPath = getpath.getpath(ip.get(i), "python");
							command = "uninstallPath="+uninstallPath+";"+ "installPath="+installPath+";"+ scIPAddr[0] +";"+scIPAddr[1] ;
							// 插入setup表
							scs.InsertToSetup(ip.get(i), "python", version,
									installPath, opID,command);
							// 构造要返回给用户的json对象
							entity.put("opID", opID);
							entity.put("status", "虚拟机已接收安装python请求可查询状态");//
						} else {
							// 软件已安装，用户又发请求再安装,返回错误代码0x0101000
							entity.put("code", "0x0101000   path : " + oldpath);
						}

					}
				} else {
					entity.put("code", "0x1100001");
				}
				jarr.put(entity);
			}
			res = Response.ok(jarr).build();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbop.close();
		getpath.close();
		scs.close();
		return res;
	}

	@POST
	@Path("/Python_Linux")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response installPythonOnLinux(@QueryParam("ip") List<String> ip,
			@QueryParam("installPath") String installPath,
			@QueryParam("version") String version,
			@QueryParam("cover") String cover) throws SQLException {
		Response res = null;
		ApplicationBase a = new ApplicationBase();
		DBOperation dbop = new DBOperation();
		DBForSCS scs = new DBForSCS();
		GetInstallPath getpath = new GetInstallPath();
		String[] scIPAddr = new String[2];
		String command = null;
		if(installPath.equals("")){
			installPath = "/home/Python";
		}else{
			installPath = installPath +"/Python";
		}
		try {
			JSONArray jarr = new JSONArray();
			for (int i = 0; i < ip.size(); i++) {
				JSONObject entity = new JSONObject();// 表示要给用户返回的json对象
				boolean status = scs.getSetupStatus(ip.get(i), "python");
				if (status) {
					String os = dbop.getHostOSByIP(ip.get(i));
					if (version.isEmpty()) {// 如果用户没有输入版本号，则程序需要自己选择一个版本号
						
						// 由程序选择数据库里最新的版本号
						version = dbop.queryAppNewestVersion("python", os);
					}
					int opID = 0;
					// 判断该软件有没有在该虚拟机上安装过
					String dbSoftVersion = dbop
							.queryHostappTableForSoftwareVersion(ip.get(i),
									"python");
					if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，正常安装
						scIPAddr = dbop.getRCAddrByIP("hp", ip.get(i),
								"python", version);
						/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
						int spaceRequirement = dbop
								.querySpaceRequirementForAppInstall("python",
										version,os);

						opID = a.sendSetupPythonMsgOnLinux(ip.get(i), scIPAddr,
								spaceRequirement, installPath);
						String uninstallPath = getpath.getpath(ip.get(i), "python");
						command = "uninstallPath="+uninstallPath+";"+"installPath="+installPath + ";"+ scIPAddr[0] +";"+scIPAddr[1] ;
						// 插入到setup表
						scs.InsertToSetup(ip.get(i), "python", version,
								installPath, opID,command);
						// 构造要返回给用户的json对象
						entity.put("opID", opID);
						entity.put("status", "虚拟机已接收安装python请求可查询状态");//
					} else {

						String oldpath = getpath.getpath(ip.get(i), "python");
						if (cover.toUpperCase().equals("YES")) {
							scIPAddr = dbop.getRCAddrByIP("hp", ip.get(i),
									"python", version);
							/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
							int spaceRequirement = dbop
									.querySpaceRequirementForAppInstall(
											"python", version,os);

							opID = a.sendSetupPythonMsgOnLinux(ip.get(i),
									scIPAddr, spaceRequirement, installPath);
							String uninstallPath = getpath.getpath(ip.get(i), "python");
							command = "uninstallPath="+uninstallPath+";"+"installPath="+installPath + ";"+ scIPAddr[0] +";"+scIPAddr[1] ;
							// 插入到setup表
							scs.InsertToSetup(ip.get(i), "python", version,
									installPath, opID,command);
							// 构造要返回给用户的json对象
							entity.put("opID", opID);
							entity.put("status", "虚拟机已接收安装python请求可查询状态");//
						} else {
							// 软件已安装，用户又发请求再安装,返回错误代码0x0101000
							entity.put("code", "0x0101000   path : " + oldpath);
						}

					}
				} else {
					entity.put("code", "0x1100001");
				}
				jarr.put(entity);
			}
			res = Response.ok(jarr).build();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbop.close();
		getpath.close();
		scs.close();
		return res;
	}

	@POST
	@Path("/Memcached")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response installMemcached(@QueryParam("ip") List<String> ip,
			@QueryParam("installPath") String installPath,
			@QueryParam("version") String version,
			@QueryParam("cover") String cover) throws SQLException {
		Response res = null;
		ApplicationBase a = new ApplicationBase();
		DBOperation dbop = new DBOperation();
		DBForSCS scs = new DBForSCS();
		GetInstallPath getpath = new GetInstallPath();
		String[] scIPAddr = new String[2];
		String command = null;
		if(installPath.equals("")){
			installPath = "C:/Memcached";
		}else{
			installPath = installPath+"/Memcached";
		}
		try {
			JSONArray jarr = new JSONArray();
			for (int i = 0; i < ip.size(); i++) {
				JSONObject entity = new JSONObject();// 表示要给用户返回的json对象
				boolean status = scs.getSetupStatus(ip.get(i), "memcached");
				if (status) {
					String os = dbop.getHostOSByIP(ip.get(i));
					if (version.isEmpty()) {// 如果用户没有输入版本号，则程序需要自己选择一个版本号
						
						// 由程序选择数据库里最新的版本号
						version = dbop.queryAppNewestVersion("memcached", os);
					}
					int opID = 0;
					// 判断该软件有没有在该虚拟机上安装过
					String dbSoftVersion = dbop
							.queryHostappTableForSoftwareVersion(ip.get(i),
									"memcached");
					if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，正常安装
						scIPAddr = dbop.getRCAddrByIP("hp", ip.get(i),
								"memcached", version);
						/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
						int spaceRequirement = dbop
								.querySpaceRequirementForAppInstall(
										"memcached", version,os);
						int zipSpace = dbop.queryZipSpaceForAppInstall(
								"memcached", version,os);

						opID = a.sendSetupMemcachedMsg(ip.get(i), scIPAddr,
								spaceRequirement, zipSpace,installPath);
						String uninstallPath = getpath.getpath(ip.get(i), "memcached");
						command = "uninstallPath="+uninstallPath+";"+ "installPath="+installPath+";"+scIPAddr[0] +";"+scIPAddr[1] ;
						// 插入到setup表
						scs.InsertToSetup(ip.get(i), "memcached", version,
								installPath, opID,command);
						// 构造要返回给用户的json对象
						entity.put("opID", opID);
						entity.put("status", "虚拟机已接收安装memcached请求可查询状态");//
					} else {
						String oldpath = getpath
								.getpath(ip.get(i), "memcached");
						if (cover.toUpperCase().equals("YES")) {
							scIPAddr = dbop.getRCAddrByIP("hp", ip.get(i),
									"memcached", version);
							/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
							int spaceRequirement = dbop
									.querySpaceRequirementForAppInstall(
											"memcached", version,os);
							int zipSpace = dbop.queryZipSpaceForAppInstall(
									"memcached", version,os);

							opID = a.sendSetupMemcachedMsg(ip.get(i), scIPAddr,
									spaceRequirement, zipSpace,installPath);
							String uninstallPath = getpath.getpath(ip.get(i), "memcached");
							command = "uninstallPath="+uninstallPath+";"+"installPath="+installPath+";"+scIPAddr[0] +";"+scIPAddr[1] ;
							// 插入到setup表
							scs.InsertToSetup(ip.get(i), "memcached", version,
									installPath, opID,command);
							// 构造要返回给用户的json对象
							entity.put("opID", opID);
							entity.put("status", "虚拟机已接收安装memcached请求可查询状态");//
						} else {
							// 软件已安装，用户又发请求再安装,返回错误代码0x0101000
							entity.put("code", "0x0101000   path : " + oldpath);
						}

					}
				} else {
					entity.put("code", "0x1100001");
				}
				jarr.put(entity);
			}
			res = Response.ok(jarr).build();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbop.close();
		getpath.close();
		scs.close();
		return res;
	}

	@POST
	@Path("/Memcached_Linux")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response installMemcachedOnLinux(@QueryParam("ip") List<String> ip,
			@QueryParam("installPath") String installPath,
			@QueryParam("version") String version,
			@QueryParam("cover") String cover) throws SQLException {

		Response res = null;
		ApplicationBase a = new ApplicationBase();
		DBOperation dbop = new DBOperation();
		GetInstallPath getpath = new GetInstallPath();
		DBForSCS scs = new DBForSCS();
		String[] scIPAddr = new String[2];
		String command = null;
		if(installPath.equals("")){
			installPath = "/home/Memcached";
		}else{
			installPath = installPath + "/Memcached";
		}
		try {
			JSONArray jarr = new JSONArray();
			for (int i = 0; i < ip.size(); i++) {
				JSONObject entity = new JSONObject();// 表示要给用户返回的json对象
				boolean status = scs.getSetupStatus(ip.get(i), "memcached");
				if (status) {
					String os = dbop.getHostOSByIP(ip.get(i));
					if (version.isEmpty()) {// 如果用户没有输入版本号，则程序需要自己选择一个版本号
						
						// 由程序选择数据库里最新的版本号
						version = dbop.queryAppNewestVersion("memcached", os);
					}
					int opID = 0;
					// 判断该软件有没有在该虚拟机上安装过
					String dbSoftVersion = dbop
							.queryHostappTableForSoftwareVersion(ip.get(i),
									"memcached");
					if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，正常安装
						scIPAddr = dbop.getRCAddrByIP("hp", ip.get(i),
								"memcached", version);
						/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
						int spaceRequirement = dbop
								.querySpaceRequirementForAppInstall(
										"memcached", version,os);

						opID = a.sendSetupMemcachedMsgOnLinux(ip.get(i),
								scIPAddr, spaceRequirement, installPath);
						String uninstallPath = getpath.getpath(ip.get(i), "memcached");
						command = "uninstallPath="+uninstallPath+";"+ "installPath="+installPath + ";" + scIPAddr[0] +";"+scIPAddr[1] ;
						// 可在此处添加setup数据库的添加信息
						scs.InsertToSetup(ip.get(i), "memcached", version,
								installPath, opID,command);
						// 构造要返回给用户的json对象
						entity.put("opID", opID);
						entity.put("status", "虚拟机已接收安装memcached请求可查询状态");//
					} else {
						// 已经安装过则考虑cover
						String oldpath = getpath
								.getpath(ip.get(i), "memcached");
						if (cover.toUpperCase().equals("YES")) {
							scIPAddr = dbop.getRCAddrByIP("hp", ip.get(i),
									"memcached", version);
							/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
							int spaceRequirement = dbop
									.querySpaceRequirementForAppInstall(
											"memcached", version,os);

							opID = a.sendSetupMemcachedMsgOnLinux(ip.get(i),
									scIPAddr, spaceRequirement, installPath);
							String uninstallPath = getpath.getpath(ip.get(i), "memcached");
							command = "uninstallPath="+uninstallPath+";"+ "installPath="+installPath + ";"+ scIPAddr[0] +";"+scIPAddr[1] ;
							// 可在此处添加setup数据库的添加信息
							scs.InsertToSetup(ip.get(i), "memcached", version,
									installPath, opID,command);
							// 构造要返回给用户的json对象
							entity.put("opID", opID);
							entity.put("status", "虚拟机已接收安装memcached请求可查询状态");//
						} else {
							// 软件已安装，用户又发请求再安装,返回错误代码0x0101000
							entity.put("code", "0x0101000  path : " + oldpath);
						}
					}
				} else {
					entity.put("code", "0x1100001");
				}
				jarr.put(entity);
			}
			res = Response.ok(jarr).build();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbop.close();
		getpath.close();
		scs.close();
		return res;
	}

	@POST
	@Path("/IISRewrite")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response installIISRewrite(@QueryParam("ip") List<String> ip,
			@QueryParam("installPath") String installPath,
			@QueryParam("version") String version,
			@QueryParam("cover") String cover) throws SQLException {
		Response res = null;
		ApplicationBase a = new ApplicationBase();
		DBOperation dbop = new DBOperation();
		GetInstallPath getpath = new GetInstallPath();
		DBForSCS scs = new DBForSCS();
		String[] scIPAddr = new String[2];
		String command = null;
		if(installPath.equals("")){
			installPath = "C:/IISRewrite";
		}else{
			installPath = installPath + "/IISRewrite";
		}
		try {
			JSONArray jarr = new JSONArray();
			for (int i = 0; i < ip.size(); i++) {
				JSONObject entity = new JSONObject();// 表示要给用户返回的json对象
				boolean status = scs.getSetupStatus(ip.get(i), "iisrewrite");
				if (status) {
					String os = dbop.getHostOSByIP(ip.get(i));
					if (version.isEmpty()) {// 如果用户没有输入版本号，则程序需要自己选择一个版本号
						
						// 由程序选择数据库里最新的版本号
						version = dbop.queryAppNewestVersion("iisrewrite", os);
					}
					int opID = 0;
					// 判断该软件有没有在该虚拟机上安装过
					String dbSoftVersion = dbop
							.queryHostappTableForSoftwareVersion(ip.get(i),
									"iisrewrite");
					if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，正常安装
						scIPAddr = dbop.getRCAddrByIP("hp", ip.get(i),
								"iisrewrite", version);
						/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
						int spaceRequirement = dbop
								.querySpaceRequirementForAppInstall(
										"iisrewrite", version,os);
						int zipSpace = dbop.queryZipSpaceForAppInstall(
								"iisrewrite", version,os);

						opID = a.sendSetupIISRewriteMsg(ip.get(i), scIPAddr,
								spaceRequirement, zipSpace, installPath);
						String uninstallPath = getpath.getpath(ip.get(i), "iisrewrite");
						command = "uninstallPath="+uninstallPath+";"+"installPath="+installPath + ";"+scIPAddr[0] +";"+scIPAddr[1] ;
						// 可在此处添加setup数据库的添加信息
						scs.InsertToSetup(ip.get(i), "iisrewrite", version,
								installPath, opID,command);
						// 构造要返回给用户的json对象
						entity.put("opID", opID);
						entity.put("status", "虚拟机已接收安装iisrewrite请求可查询状态");//
					} else {
						// 已经安装过则考虑cover
						String oldpath = getpath.getpath(ip.get(i),
								"iisrewrite");
						if (cover.toUpperCase().equals("YES")) {
							scIPAddr = dbop.getRCAddrByIP("hp", ip.get(i),
									"iisrewrite", version);
							/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
							int spaceRequirement = dbop
									.querySpaceRequirementForAppInstall(
											"iisrewrite", version,os);
							int zipSpace = dbop.queryZipSpaceForAppInstall(
									"iisrewrite", version,os);

							opID = a.sendSetupIISRewriteMsg(ip.get(i),
									scIPAddr, spaceRequirement, zipSpace,
									installPath);
							String uninstallPath = getpath.getpath(ip.get(i), "iisrewrite");
							command = "uninstallPath="+uninstallPath+";"+ "installPath="+installPath + ";"+ scIPAddr[0] +";"+scIPAddr[1] ;
							// 可在此处添加setup数据库的添加信息

							scs.InsertToSetup(ip.get(i), "iisrewrite", version,
									installPath, opID,command);
							// 构造要返回给用户的json对象
							entity.put("opID", opID);
							entity.put("status", "虚拟机已接收安装iisrewrite请求可查询状态");//
						} else {
							// 软件已安装，用户又发请求再安装,返回错误代码0x0101000
							entity.put("code", "0x0101000  path : " + oldpath);
						}

					}
				} else {
					entity.put("code", "0x1100001");
				}
				jarr.put(entity);
			}
			res = Response.ok(jarr).build();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbop.close();
		getpath.close();
		scs.close();
		return res;
	}

	@POST
	@Path("/FTP")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response installFTP(@QueryParam("ip") List<String> ip,
			@QueryParam("installPath") String installPath,
			@QueryParam("version") String version,
			@QueryParam("cover") String cover) throws SQLException {
		Response res = null;
		ApplicationBase a = new ApplicationBase();
		DBOperation dbop = new DBOperation();
		GetInstallPath getpath = new GetInstallPath();
		DBForSCS scs = new DBForSCS();
		String[] scIPAddr = new String[2];
		String command = null;
		if(installPath.equals("")){
			installPath = "C:/FTP";
		}else{
			installPath = installPath + "/FTP";
		}
		try {
			JSONArray jarr = new JSONArray();
			for (int i = 0; i < ip.size(); i++) {
				JSONObject entity = new JSONObject();// 表示要给用户返回的json对象
				boolean status = scs.getSetupStatus(ip.get(i), "ftp");
				if (status) {
					String os = dbop.getHostOSByIP(ip.get(i));
					if (version.isEmpty()) {// 如果用户没有输入版本号，则程序需要自己选择一个版本号
						
						// 由程序选择数据库里最新的版本号
						version = dbop.queryAppNewestVersion("ftp", os);
					}
					int opID = 0;
					// 判断该软件有没有在该虚拟机上安装过
					String dbSoftVersion = dbop
							.queryHostappTableForSoftwareVersion(ip.get(i),
									"ftp");
					if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，正常安装
						scIPAddr = dbop.getRCAddrByIP("hp", ip.get(i), "ftp",
								version);
						/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
						int spaceRequirement = dbop
								.querySpaceRequirementForAppInstall("ftp",
										version,os);
						int zipSpace = dbop.queryZipSpaceForAppInstall("ftp",
								version,os);

						opID = a.sendSetupFTPMsg(ip.get(i), scIPAddr,
								spaceRequirement, zipSpace, installPath);
						String uninstallPath = getpath.getpath(ip.get(i), "ftp");
						command = "uninstallPath="+uninstallPath+";"+ "installPath="+installPath + ";"+  scIPAddr[0] +";"+scIPAddr[1] ;
						// 可在此处添加setup数据库的添加信息
						scs.InsertToSetup(ip.get(i), "ftp", version,
								installPath, opID,command);
						// 构造要返回给用户的json对象
						entity.put("opID", opID);
						entity.put("status", "虚拟机已接收安装ftp请求可查询状态");//
					} else {
						// 已经安装过则考虑cover
						String oldpath = getpath.getpath(ip.get(i), "ftp");
						if (cover.toUpperCase().equals("YES")) {
							scIPAddr = dbop.getRCAddrByIP("hp", ip.get(i),
									"ftp", version);
							/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
							int spaceRequirement = dbop
									.querySpaceRequirementForAppInstall("ftp",
											version,os);
							int zipSpace = dbop.queryZipSpaceForAppInstall(
									"ftp", version,os);

							opID = a.sendSetupFTPMsg(ip.get(i), scIPAddr,
									spaceRequirement, zipSpace, installPath);
							String uninstallPath = getpath.getpath(ip.get(i), "ftp");
							command = "uninstallPath="+uninstallPath+";"+"installPath="+installPath + ";"+ scIPAddr[0] +";"+scIPAddr[1] ;
							// 可在此处添加setup数据库的添加信息
							scs.InsertToSetup(ip.get(i), "ftp", version,
									installPath, opID,command);
							// 构造要返回给用户的json对象
							entity.put("opID", opID);
							entity.put("status", "虚拟机已接收安装ftp请求可查询状态");//
						} else {
							// 软件已安装，用户又发请求再安装,返回错误代码0x0101000
							entity.put("code", "0x0101000  path : " + oldpath);
						}
					}
				} else {
					entity.put("code", "0x1100001");
				}
				jarr.put(entity);
			}
			res = Response.ok(jarr).build();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbop.close();
		getpath.close();
		scs.close();
		return res;
	}

	@POST
	@Path("/FTP_Linux")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response installFTPOnLinux(@QueryParam("ip") List<String> ip,
			@QueryParam("installPath") String installPath,
			@QueryParam("version") String version,
			@QueryParam("cover") String cover) throws SQLException {
		Response res = null;
		ApplicationBase a = new ApplicationBase();
		DBOperation dbop = new DBOperation();
		DBForSCS scs = new DBForSCS();
		GetInstallPath getpath = new GetInstallPath();
		String[] scIPAddr = new String[2];
		String command = null;
		if(installPath.equals("")){
			installPath = "/home/FTP";
		}else{
			installPath = installPath + "/FTP";
		}
		try {
			JSONArray jarr = new JSONArray();
			for (int i = 0; i < ip.size(); i++) {
				JSONObject entity = new JSONObject();// 表示要给用户返回的json对象
				boolean status = scs.getSetupStatus(ip.get(i), "ftp");
				if (status) {
					String os = dbop.getHostOSByIP(ip.get(i));
					if (version.isEmpty()) {// 如果用户没有输入版本号，则程序需要自己选择一个版本号
						
						// 由程序选择数据库里最新的版本号
						version = dbop.queryAppNewestVersion("ftp", os);
					}
					int opID = 0;
					// 判断该软件有没有在该虚拟机上安装过
					String dbSoftVersion = dbop
							.queryHostappTableForSoftwareVersion(ip.get(i),
									"ftp");
					if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，正常安装
						scIPAddr = dbop.getRCAddrByIP("hp", ip.get(i), "ftp",
								version);
						/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
						int spaceRequirement = dbop
								.querySpaceRequirementForAppInstall("ftp",
										version,os);
						opID = a.sendSetupFTPMsgOnLinux(ip.get(i), scIPAddr,
								spaceRequirement,installPath);
						String uninstallPath = getpath.getpath(ip.get(i), "ftp");
						command = "uninstallPath="+uninstallPath+";"+"installPath="+installPath+";"+scIPAddr[0] +";"+scIPAddr[1] ;
						scs.InsertToSetup(ip.get(i), "ftp", version, installPath, opID,command);
						// 构造要返回给用户的json对象
						entity.put("opID", opID);
						entity.put("status", "虚拟机已接收安装ftp请求可查询状态");//
					} else {
						String oldpath = getpath.getpath(ip.get(i), "ftp");
						if(cover.toUpperCase().equals("YES")){
							scIPAddr = dbop.getRCAddrByIP("hp", ip.get(i), "ftp",
									version);
							/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
							int spaceRequirement = dbop
									.querySpaceRequirementForAppInstall("ftp",
											version,os);
							opID = a.sendSetupFTPMsgOnLinux(ip.get(i), scIPAddr,
									spaceRequirement,installPath);
							String uninstallPath = getpath.getpath(ip.get(i), "ftp");
							command = "uninstallPath="+uninstallPath+";"+ "installPath="+installPath+";"+scIPAddr[0] +";"+scIPAddr[1] ;
							scs.InsertToSetup(ip.get(i), "ftp", version, installPath, opID,command);
							// 构造要返回给用户的json对象
							entity.put("opID", opID);
							entity.put("status", "虚拟机已接收安装ftp请求可查询状态");//
						}else{
							// 软件已安装，用户又发请求再安装,返回错误代码0x0101000
							entity.put("code", "0x0101000  path : "+oldpath);
						}
						
					}
				} else {
					entity.put("code", "0x1100001");
				}
				jarr.put(entity);
			}
			res = Response.ok(jarr).build();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbop.close();
		getpath.close();
		scs.close();
		return res;
	}

	@POST
	@Path("/SQLServer2008R2")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response installSQLServer2008R2(@QueryParam("ip") List<String> ip,
			@QueryParam("installPath") String installPath,
			@QueryParam("rootPswd") String rootPswd,
			@QueryParam("hostName") String hostName,
			@QueryParam("userName") String userName,
			@QueryParam("version") String version,
			@QueryParam("cover") String cover) throws SQLException {
		Response res = null;
		ApplicationBase a = new ApplicationBase();
		DBOperation dbop = new DBOperation();
		DBForSCS scs = new DBForSCS();
		GetInstallPath getpath = new GetInstallPath();
		String[] scIPAddr = new String[2];
		String command = null;
		if(installPath.equals("")){
			installPath = "C:/SQLServer2008R2";
		}else{
			installPath = installPath+"/SQLServer2008R2";
		}
		
		try {
			JSONArray jarr = new JSONArray();
			for (int i = 0; i < ip.size(); i++) {
				JSONObject entity = new JSONObject();// 表示要给用户返回的json对象
				boolean status = scs.getSetupStatus(ip.get(i),
						"sqlserver2008r2");
				if (status) {
					String os = dbop.getHostOSByIP(ip.get(i));
					if (version.isEmpty()) {// 如果用户没有输入版本号，则程序需要自己选择一个版本号
						
						// 由程序选择数据库里最新的版本号
						version = dbop.queryAppNewestVersion("sqlserver2008r2",
								os);
					}
					int opID = 0;
					// 判断该软件有没有在该虚拟机上安装过
					String dbSoftVersion = dbop
							.queryHostappTableForSoftwareVersion(ip.get(i),
									"sqlserver2008r2");
					if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，正常安装
						scIPAddr = dbop.getRCAddrByIP("hp", ip.get(i),
								"sqlserver2008r2", version);
						/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
						int spaceRequirement = dbop
								.querySpaceRequirementForAppInstall(
										"sqlserver2008r2", version,os);
						int zipSpace = dbop.queryZipSpaceForAppInstall(
								"sqlserver2008r2", version,os);

						opID = a.sendSetupSQLServer2008R2Msg(ip.get(i),
								scIPAddr, spaceRequirement, zipSpace,
								installPath, rootPswd, hostName, userName);
						String uninstallPath = getpath.getpath(ip.get(i), "sqlserver2008r2");
						command = "uninstallPath="+uninstallPath+";"+ "installPath="+installPath + ";rootPswd="+ rootPswd  +";hostName="+ hostName  +";userName="+userName  +";"+scIPAddr[0] +";"+scIPAddr[1] ;
						// 可在此处添加setup数据库的添加信息
						scs.InsertToSetup(ip.get(i), "sqlserver2008r2",
								version, installPath, opID,command);
						// 构造要返回给用户的json对象
						entity.put("opID", opID);
						entity.put("status", "虚拟机已接收安装SQLServer2008R2请求可查询状态");//
					} else {
						if(cover.toUpperCase().equals("YES")){
							scIPAddr = dbop.getRCAddrByIP("hp", ip.get(i),
									"sqlserver2008r2", version);
							/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
							int spaceRequirement = dbop
									.querySpaceRequirementForAppInstall(
											"sqlserver2008r2", version,os);
							int zipSpace = dbop.queryZipSpaceForAppInstall(
									"sqlserver2008r2", version,os);

							opID = a.sendSetupSQLServer2008R2Msg(ip.get(i),
									scIPAddr, spaceRequirement, zipSpace,
									installPath, rootPswd, hostName, userName);
							String uninstallPath = getpath.getpath(ip.get(i), "sqlserver2008r2");
							command = "uninstallPath="+uninstallPath+";"+ "installPath="+installPath + ";rootPswd="+ rootPswd  +";hostName="+ hostName  +";userName="+userName  +";"+scIPAddr[0] +";"+scIPAddr[1] ;
							// 可在此处添加setup数据库的添加信息
							scs.InsertToSetup(ip.get(i), "sqlserver2008r2",
									version, installPath, opID,command);
							// 构造要返回给用户的json对象
							entity.put("opID", opID);
							entity.put("status", "虚拟机已接收安装SQLServer2008R2请求可查询状态");//
						}else{
						String oldpath = getpath.getpath(ip.get(i),
								"sqlserver2008r2");
						// 软件已安装，用户又发请求再安装,返回错误代码0x0101000
						entity.put("code", "0x0101000  path : " + oldpath);
						}
					}
				} else {
					entity.put("code", "0x1100001");
				}
				jarr.put(entity);
			}
			res = Response.ok(jarr).build();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbop.close();
		getpath.close();
		scs.close();
		return res;
	}

	@POST
	@Path("/InterfaceSQLServer2008R2")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response interfaceInstallSQLServer2008R2(
			@QueryParam("ip") List<String> ip,
			@QueryParam("installPath") String installPath,
			@QueryParam("rootPswd") String rootPswd,
			@QueryParam("hostName") String hostName,
			@QueryParam("userName") String userName,
			@QueryParam("version") String version,
			@QueryParam("cover") String cover) throws SQLException {
		Response res = null;
		ApplicationBase a = new ApplicationBase();
		DBOperation dbop = new DBOperation();
		DBForSCS scs = new DBForSCS();
		GetInstallPath getpath = new GetInstallPath();
		String[] scIPAddr = new String[2];
		String command = null;
		if(installPath.equals("")){
			installPath = "C:/SQLServer2008R2";
		}else{
			installPath = installPath+"/SQLServer2008R2";
		}
		try {
			JSONArray jarr = new JSONArray();
			for (int i = 0; i < ip.size(); i++) {
				JSONObject entity = new JSONObject();// 表示要给用户返回的json对象
				boolean status = scs.getSetupStatus(ip.get(i),
						"sqlserver2008r2");
				if (status) {
					String os = dbop.getHostOSByIP(ip.get(i));
					if (version.isEmpty()) {// 如果用户没有输入版本号，则程序需要自己选择一个版本号
						
						// 由程序选择数据库里最新的版本号
						version = dbop.queryAppNewestVersion("sqlserver2008r2",
								os);
					}
					int opID = 0;
					// 判断该软件有没有在该虚拟机上安装过
					String dbSoftVersion = dbop
							.queryHostappTableForSoftwareVersion(ip.get(i),
									"sqlserver2008r2");
					if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，正常安装
						scIPAddr = dbop.getRCAddrByIP("hp", ip.get(i),
								"sqlserver2008r2", version);
						/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
						int spaceRequirement = dbop
								.querySpaceRequirementForAppInstall(
										"sqlserver2008r2", version,os);
						int zipSpace = dbop.queryZipSpaceForAppInstall(
								"sqlserver2008r2", version,os);

						opID = a.sendSetupSQLServer2008R2InterfaceMsg(
								ip.get(i), scIPAddr, spaceRequirement,
								zipSpace, installPath, rootPswd, hostName,
								userName);
						String uninstallPath = getpath.getpath(ip.get(i), "sqlserver2008r2");
						command = "uninstallPath="+uninstallPath+";"+"installPath="+installPath + ";rootPwd="+ rootPswd  +";hostName="+ hostName  +";userName="+userName  +";"+scIPAddr[0] +";"+scIPAddr[1] ;
						// 可在此处添加setup数据库的添加信息
						scs.InsertToSetup(ip.get(i), "sqlserver2008r2", version,
								installPath, opID,command);
						// 构造要返回给用户的json对象
						entity.put("opID", opID);
						entity.put("status", "虚拟机已接收安装SQLServer2008R2请求可查询状态");//
					} else {
						if(cover.toUpperCase().equals("YES")){
							scIPAddr = dbop.getRCAddrByIP("hp", ip.get(i),
									"sqlserver2008r2", version);
							/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
							int spaceRequirement = dbop
									.querySpaceRequirementForAppInstall(
											"sqlserver2008r2", version,os);
							int zipSpace = dbop.queryZipSpaceForAppInstall(
									"sqlserver2008r2", version,os);

							opID = a.sendSetupSQLServer2008R2InterfaceMsg(
									ip.get(i), scIPAddr, spaceRequirement,
									zipSpace, installPath, rootPswd, hostName,
									userName);
							String uninstallPath = getpath.getpath(ip.get(i), "sqlserver2008r2");
							command = "uninstallPath="+uninstallPath+";"+"installPath="+installPath + ";rootPwd="+ rootPswd  +";hostName="+ hostName  +";userName="+userName  +";"+scIPAddr[0] +";"+scIPAddr[1] ;
							// 可在此处添加setup数据库的添加信息
							scs.InsertToSetup(ip.get(i), "sqlserver2008r2", version,
									installPath, opID,command);
							// 构造要返回给用户的json对象
							entity.put("opID", opID);
							entity.put("status", "虚拟机已接收安装SQLServer2008R2请求可查询状态");//
						}else{
						String oldpath = getpath.getpath(ip.get(i),
								"sqlserver2008r2");
						// 软件已安装，用户又发请求再安装,返回错误代码0x0101000
						entity.put("code", "0x0101000  path :" + oldpath);
						}
					}
				} else {
					entity.put("code", "0x1100001");
				}
				jarr.put(entity);
			}
			res = Response.ok(jarr).build();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbop.close();
		getpath.close();
		scs.close();
		return res;
	}

	@POST
	@Path("/Oracle11g_Linux")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response installOracle11gLinux(@QueryParam("ip") List<String> ip,
			@QueryParam("username") String username,
			@QueryParam("oraclebase") String oraclebase,
			@QueryParam("oracleinventory") String oracleinventory,
			@QueryParam("oraclehome") String oraclehome,
			@QueryParam("oracle_sid") String oracle_sid,
			@QueryParam("rootPswd") String rootPswd,
			@QueryParam("oradata") String oradata,
			@QueryParam("version") String version,
			@QueryParam("cover") String cover) throws SQLException {
		Response res = null;
		ApplicationBase a = new ApplicationBase();
		DBOperation dbop = new DBOperation();
		DBForSCS scs = new DBForSCS();
		GetInstallPath getpath = new GetInstallPath();
		String[] scIPAddr = new String[2];
		String command =null;
		if(oraclebase.equals("")){
			oraclebase = "/home/Oracle1g";
		}else{
			oraclebase = oraclebase+"/Oracle11g";
		}
		try {

			JSONArray jarr = new JSONArray();
			for (int i = 0; i < ip.size(); i++) {
				JSONObject entity = new JSONObject();// 表示要给用户返回的json对象
				boolean status = scs.getSetupStatus(ip.get(i), "oracle11g");
				if (status) {
					String os = dbop.getHostOSByIP(ip.get(i));
					if (version.isEmpty()) {// 如果用户没有输入版本号，则程序需要自己选择一个版本号
						
						// 由程序选择数据库里最新的版本号
						version = dbop.queryAppNewestVersion("oracle11g", os);
					}
					int opID = 0;

					// 判断该软件有没有在该虚拟机上安装过
					String dbSoftVersion = dbop
							.queryHostappTableForSoftwareVersion(ip.get(i),
									"oracle11g");
					if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，正常安装
						scIPAddr = dbop.getRCAddrByIP("hp", ip.get(i),
								"oracle11g", version);
						/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
						int spaceRequirement = dbop
								.querySpaceRequirementForAppInstall(
										"oracle11g", version,os);

						opID = a.sendSetupOracle11gLinuxMsg(ip.get(i),
								scIPAddr, spaceRequirement, username,
								oraclebase, oracleinventory, oraclehome,
								oracle_sid, rootPswd, oradata);
						String uninstallPath = getpath.getpath(ip.get(i), "oracle11g");
						command = "uninstallPath="+uninstallPath+";"+"username="+ username + ";installPath="+ oraclebase  +";oracleinventory="+ oracleinventory  +";oraclehome="+oraclehome  +";oracle_sid="+oracle_sid  +";rootPswd="+rootPswd  +";oradata="+oradata  +";"+scIPAddr[0] +";"+scIPAddr[1] ;
						scs.InsertToSetup(ip.get(i), "oracle11g", version,
								oraclebase+"&"+username, opID,command);
						// 构造要返回给用户的json对象
						entity.put("opID", opID);
						entity.put("status", "虚拟机已接收安装Oracle11g请求可查询状态");//
					} else {
						if(cover.toUpperCase().equals("YES")){
							scIPAddr = dbop.getRCAddrByIP("hp", ip.get(i),
									"oracle11g", version);
							/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
							int spaceRequirement = dbop
									.querySpaceRequirementForAppInstall(
											"oracle11g", version,os);

							opID = a.sendSetupOracle11gLinuxMsg(ip.get(i),
									scIPAddr, spaceRequirement, username,
									oraclebase, oracleinventory, oraclehome,
									oracle_sid, rootPswd, oradata);
							String uninstallPath = getpath.getpath(ip.get(i), "oracle11g");
							command = "uninstallPath="+uninstallPath+";"+"username="+ username + ";oraclebase="+ oraclebase  +";oracleinventory="+ oracleinventory  +";oraclehome="+oraclehome  +";oracle_sid="+oracle_sid  +";rootPswd="+rootPswd  +";oradata="+oradata  +";"+scIPAddr[0] +";"+scIPAddr[1] ;
							scs.InsertToSetup(ip.get(i), "oracle11g", version,
									oraclebase+"&"+username, opID,command);
							// 构造要返回给用户的json对象
							entity.put("opID", opID);
							entity.put("status", "虚拟机已接收安装Oracle11g请求可查询状态");//
						}else{
						String oldpath = getpath
								.getpath(ip.get(i), "oracle11g");
						// 软件已安装，用户又发请求再安装,返回错误代码0x0101000
						entity.put("code", "0x0101000  path : " + oldpath);
						}
					}
				} else {
					entity.put("code", "0x1100001");
				}
				jarr.put(entity);

			}

			res = Response.ok(jarr).build();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbop.close();
		getpath.close();
		scs.close();
		return res;
	}

	@POST
	@Path("/Oracle11g")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response installOracle11g(@QueryParam("ip") List<String> ip,
			@QueryParam("hostname") String hostname,
			@QueryParam("inventorypath") String inventorypath,
			@QueryParam("oraclebase") String oraclebase,
			@QueryParam("oraclehome") String oraclehome,
			@QueryParam("rootPswd") String rootPswd,
			@QueryParam("version") String version,
			@QueryParam("cover") String cover) throws SQLException {
		Response res = null;
		ApplicationBase a = new ApplicationBase();
		DBOperation dbop = new DBOperation();
		DBForSCS scs = new DBForSCS();
		GetInstallPath getpath = new GetInstallPath();
		String[] scIPAddr = new String[2];
		String command = null;
		if(oraclebase.equals("")){
			oraclebase = "C:/Oracle11g";
		}else{
			oraclebase = oraclebase+"/Oracle11g";
		}
		try {

			JSONArray jarr = new JSONArray();
			for (int i = 0; i < ip.size(); i++) {
				JSONObject entity = new JSONObject();// 表示要给用户返回的json对象
				boolean status = scs.getSetupStatus(ip.get(i), "oracle11g");
				if (status) {
					String os = dbop.getHostOSByIP(ip.get(i));
					if (version.isEmpty()) {// 如果用户没有输入版本号，则程序需要自己选择一个版本号
						
						// 由程序选择数据库里最新的版本号
						version = dbop.queryAppNewestVersion("oracle11g", os);
					}
					int opID = 0;
					// 判断该软件有没有在该虚拟机上安装过
					String dbSoftVersion = dbop
							.queryHostappTableForSoftwareVersion(ip.get(i),
									"oracle11g");
					if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，正常安装
						scIPAddr = dbop.getRCAddrByIP("hp", ip.get(i),
								"oracle11g", version);
						/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
						int spaceRequirement = dbop
								.querySpaceRequirementForAppInstall(
										"oracle11g", version,os);
						int zipSpace = dbop.queryZipSpaceForAppInstall(
								"oracle11g", version,os);

						opID = a.sendSetupOracle11gMsg(ip.get(i), scIPAddr,
								spaceRequirement, zipSpace, hostname,
								inventorypath, oraclebase, oraclehome, rootPswd);
						String uninstallPath = getpath.getpath(ip.get(i), "oracle11g");
						command = "uninstallPath="+uninstallPath+";"+"installPath="+oraclebase+";hostname="+ hostname + ";inventorypath="+ inventorypath  +";oraclehome="+ oraclehome  +";rootPswd="+rootPswd +";"+scIPAddr[0] +";"+scIPAddr[1] ;
						// 可在此处添加setup数据库的添加信息
						scs.InsertToSetup(ip.get(i), "oracle11g", version,
								oraclebase+"&"+oraclehome, opID,command);
						// 构造要返回给用户的json对象
						entity.put("opID", opID);
						entity.put("status", "虚拟机已接收安装Oracle11g请求可查询状态");//
					} else {
						if(cover.toUpperCase().equals("YES")){
							scIPAddr = dbop.getRCAddrByIP("hp", ip.get(i),
									"oracle11g", version);
							/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
							int spaceRequirement = dbop
									.querySpaceRequirementForAppInstall(
											"oracle11g", version,os);
							int zipSpace = dbop.queryZipSpaceForAppInstall(
									"oracle11g", version,os);

							opID = a.sendSetupOracle11gMsg(ip.get(i), scIPAddr,
									spaceRequirement, zipSpace, hostname,
									inventorypath, oraclebase, oraclehome, rootPswd);
							String uninstallPath = getpath.getpath(ip.get(i), "oracle11g");
							command = "uninstallPath="+uninstallPath+";"+"installPath="+oraclebase+";hostname="+ hostname + ";inventorypath="+ inventorypath  +";oraclehome="+ oraclehome  +";rootPswd="+rootPswd +";"+scIPAddr[0] +";"+scIPAddr[1] ;							// 可在此处添加setup数据库的添加信息
							scs.InsertToSetup(ip.get(i), "oracle11g", version,
									oraclebase+"&"+oraclehome, opID,command);
							// 构造要返回给用户的json对象
							entity.put("opID", opID);
							entity.put("status", "虚拟机已接收安装Oracle11g请求可查询状态");//
						}else{
						String oldpath = getpath
								.getpath(ip.get(i), "oracle11g");
						// 软件已安装，用户又发请求再安装,返回错误代码0x0101000
						entity.put("code", "0x0101000  path : " + oldpath);
						}
					}
				} else {
					entity.put("code", "0x1100001");
				}
				jarr.put(entity);

			}

			res = Response.ok(jarr).build();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbop.close();
		getpath.close();
		scs.close();
		return res;
	}

	@POST
	@Path("/InterfaceOracle11g")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response InterfaceinstallOracle11g(
			@QueryParam("ip") List<String> ip,
			@QueryParam("oraclebase") String oraclebase,
			@QueryParam("oraclehome") String oraclehome,
			@QueryParam("inventorypath") String inventorypath,
			@QueryParam("databasename") String databasename,
			@QueryParam("rootPswd") String rootPswd,
			@QueryParam("version") String version,
			@QueryParam("cover") String cover) throws SQLException {
		Response res = null;
		ApplicationBase a = new ApplicationBase();
		DBOperation dbop = new DBOperation();
		DBForSCS scs = new DBForSCS();
		GetInstallPath getpath = new GetInstallPath();
		String[] scIPAddr = new String[2];
		String command = null;
		if(oraclebase.equals("")){
			oraclebase = "C:/Oracle11g";
		}else{
			oraclebase = oraclebase+"/Oracle11g";
		}
		try {

			JSONArray jarr = new JSONArray();
			for (int i = 0; i < ip.size(); i++) {
				JSONObject entity = new JSONObject();// 表示要给用户返回的json对象
				boolean status = scs.getSetupStatus(ip.get(i), "oracle11g");
				if (status) {
					String os = dbop.getHostOSByIP(ip.get(i));
					if (version.isEmpty()) {// 如果用户没有输入版本号，则程序需要自己选择一个版本号
						
						// 由程序选择数据库里最新的版本号
						version = dbop.queryAppNewestVersion("oracle11g", os);
					}
					int opID = 0;
					// 判断该软件有没有在该虚拟机上安装过
					String dbSoftVersion = dbop
							.queryHostappTableForSoftwareVersion(ip.get(i),
									"oracle11g");
					if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，正常安装
						scIPAddr = dbop.getRCAddrByIP("hp", ip.get(i),
								"oracle11g", version);
						/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
						int spaceRequirement = dbop
								.querySpaceRequirementForAppInstall(
										"oracle11g", version,os);
						int zipSpace = dbop.queryZipSpaceForAppInstall(
								"oracle11g", version,os);

						opID = a.sendSetupOracle11gInterfaceMsg(ip.get(0),
								scIPAddr, spaceRequirement, zipSpace,
								oraclebase, oraclehome, inventorypath,
								databasename, rootPswd);
						String uninstallPath = getpath.getpath(ip.get(i), "oracle11g");
						command = "uninstallPath="+uninstallPath+";"+"installPath="+oraclebase + ";oraclehome="+ oraclehome  +";inventorypath="+ inventorypath  +";hostname="+databasename  +";rootPswd="+rootPswd  +";"+scIPAddr[0] +";"+scIPAddr[1] ;
						scs.InsertToSetup(ip.get(i), "oracle11g", version,
								oraclebase+"&"+oraclehome, opID,command);
						// 构造要返回给用户的json对象
						entity.put("opID", opID);
						entity.put("status", "虚拟机已接收安装Oracle11g请求可查询状态");//
					} else {
						if(cover.toUpperCase().equals("YES")){
							scIPAddr = dbop.getRCAddrByIP("hp", ip.get(i),
									"oracle11g", version);
							/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
							int spaceRequirement = dbop
									.querySpaceRequirementForAppInstall(
											"oracle11g", version,os);
							int zipSpace = dbop.queryZipSpaceForAppInstall(
									"oracle11g", version,os);

							opID = a.sendSetupOracle11gInterfaceMsg(ip.get(0),
									scIPAddr, spaceRequirement, zipSpace,
									oraclebase, oraclehome, inventorypath,
									databasename, rootPswd);
							String uninstallPath = getpath.getpath(ip.get(i), "oracle11g");
							command = "uninstallPath="+uninstallPath+";"+"installPath="+oraclebase + ";oraclehome="+ oraclehome  +";inventorypath="+ inventorypath  +";databasename="+databasename  +";rootPswd="+rootPswd  +";"+scIPAddr[0] +";"+scIPAddr[1] ;
							scs.InsertToSetup(ip.get(i), "oracle11g", version,
									oraclebase+"&"+oraclehome, opID,command);
							// 构造要返回给用户的json对象
							entity.put("opID", opID);
							entity.put("status", "虚拟机已接收安装Oracle11g请求可查询状态");//
						}else{
						String oldpath = getpath
								.getpath(ip.get(i), "oracle11g");
						// 软件已安装，用户又发请求再安装,返回错误代码0x0101000
						entity.put("code", "0x0101000  path : " + oldpath);
						}
					}
				} else {
					entity.put("code", "0x1100001");
				}
				jarr.put(entity);
			}

			res = Response.ok(jarr).build();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbop.close();
		getpath.close();
		scs.close();
		return res;
	}

	@POST
	@Path("/360")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response install360(@QueryParam("ip") List<String> ip,
			@QueryParam("installPath") String installPath,
			@QueryParam("version") String version) throws SQLException {
		Response res = null;
		ApplicationBase a = new ApplicationBase();
		DBOperation dbop = new DBOperation();
		GetInstallPath getpath = new GetInstallPath();
		DBForSCS scs = new DBForSCS();
		String[] scIPAddr = new String[2];
		String command = null;
		if(installPath.equals("")){
			installPath = "C:/360";
		}else{
			installPath = installPath+"/360";
		}
		try {
			JSONArray jarr = new JSONArray();
			for (int i = 0; i < ip.size(); i++) {
				JSONObject entity = new JSONObject();// 表示要给用户返回的json对象
				boolean status = scs.getSetupStatus(ip.get(i), "360");
				if (status) {
					String os = dbop.getHostOSByIP(ip.get(i));
					if (version.isEmpty()) {// 如果用户没有输入版本号，则程序需要自己选择一个版本号
						
						// 由程序选择数据库里最新的版本号
						version = dbop.queryAppNewestVersion("360", os);
					}
					int opID = 0;
					// 判断该软件有没有在该虚拟机上安装过
					String dbSoftVersion = dbop
							.queryHostappTableForSoftwareVersion(ip.get(i),
									"360");
					if (dbSoftVersion.isEmpty()) {// 表示该软件没有在该虚拟机上安装过，正常安装
						scIPAddr = dbop.getRCAddrByIP("hp", ip.get(i), "360",
								version);
						/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
						int spaceRequirement = dbop
								.querySpaceRequirementForAppInstall("360",
										version,os);
						int zipSpace = dbop.queryZipSpaceForAppInstall("360",
								version,os);

						opID = a.sendSetup360Msg(ip.get(i), scIPAddr,
								spaceRequirement, zipSpace, installPath);
						String uninstallPath = getpath.getpath(ip.get(i), "360");
						command = "uninstallPath="+uninstallPath+";"+"installPath="+installPath +";"+scIPAddr[0] +";"+scIPAddr[1] ;
						// 可在此处添加setup数据库的添加信息
						scs.InsertToSetup(ip.get(i), "360", version,
								installPath, opID,command);
						// 构造要返回给用户的json对象
						entity.put("opID", opID);
						entity.put("status", "虚拟机已接收安装360请求可查询状态");//
					} else {
//						if(cover.toUpperCase().equals("YES")){
//							scIPAddr = dbop.getRCAddrByIP("hp", ip.get(i), "360",
//									version);
//							/** 查询rcinfo表，获得软件解压后所需的硬盘空间的大小 */
//							int spaceRequirement = dbop
//									.querySpaceRequirementForAppInstall("360",
//											version,os);
//							int zipSpace = dbop.queryZipSpaceForAppInstall("360",
//									version,os);
//
//							opID = a.sendSetup360Msg(ip.get(i), scIPAddr,
//									spaceRequirement, zipSpace, installPath);
//							String uninstallPath = getpath.getpath(ip.get(i), "360");
//							command = "uninstallPath="+uninstallPath+";"+"installPath="+installPath +";"+scIPAddr[0] +";"+scIPAddr[1] ;
//							// 可在此处添加setup数据库的添加信息
//							scs.InsertToSetup(ip.get(i), "360", version,
//									installPath, opID,command);
//							// 构造要返回给用户的json对象
//							entity.put("opID", opID);
//							entity.put("status", "虚拟机已接收安装360请求可查询状态");//
//						}else{
						String oldpath = getpath.getpath(ip.get(i), "360");

						// 软件已安装，用户又发请求再安装,返回错误代码0x0101000
//						entity.put("code", "0x0101000   path : " + oldpath);
						entity.put("code", "0x0101000   path : " + oldpath);
//						}
					}
				} else {
					entity.put("code", "0x1100001");
				}
				jarr.put(entity);
			}
			res = Response.ok(jarr).build();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbop.close();
		getpath.close();
		scs.close();
		return res;
	}

}
