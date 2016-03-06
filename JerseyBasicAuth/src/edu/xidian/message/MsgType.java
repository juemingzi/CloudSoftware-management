package edu.xidian.message;

/**
 * @ClassName: MsgType
 * @Description: 不同的消息类型，表示需要不同的处理
 * @author: wangyannan
 * @date: 2014-11-12 下午3:40:19
 */
public enum MsgType {
	/**
	 * 基础环境配置-9000
	 */
	changePasswd, changeSecRule, startService, stopService, viewErrLog, diskFormat, changeIP,  changeUlimit,readkey,
	
	getSecRule,  getIP, getAffiIP, getUlimit, getServiceState,getOneServiceState,addAffiIP,deleteAffiIP,changeAffiIP,searchAffiIP,
	/**
	 * 应用软件部署-9100
	 */
	setupTomcat, setupJdk, setupMySql, setupApache, setupNginx, setupZendGuardLoader, setupPython, setupMemcached, setupIISRewrite, setupFTP,  setupSQLServer2008R2, setupSQLServer2000, setupOracle10g, setupOracle11g, setup360,setupSQLServer2008R2Interface, setupSQLServer2000Interface, setupOracle10gInterface, setupOracle11gInterface,
	/**
	 * 应用软件卸载-9600
	 */
	uninstallTomcat, uninstallJdk, uninstallMySql, uninstallApache, uninstallNginx, uninstallZendGuardLoader, uninstallPython, uninstallMemcached, uninstallIISRewrite,  uninstallFTP,  uninstallSQLServer2008R2, uninstallSQLServer2000, uninstallOracle10g, uninstallOracle11g, uninstall360,	
	/**
	 * 应用软件配置-9200
	 */
	configTomcat, configJdk, configMySql, configApache, configNginx, configZendGuardLoader, configPython, configMemcached, configIISRewrite, configFTP, configSQLServer2008R2, configSQLServer2000, configOracle10g, configOracle11g, config360,
	/**
	 * 获得应用软件的配置项
	 */
	getTomcatConfig,getJdkConfig,getMySqlConfig,getApacheConfig,getNginxConfig,getZendGuardLoaderConfig,getPythonConfig,getMemcachedConfig,getIISRewriteConfig,getFTPConfig,getSQLServer2008R2Config,getSQLServer2000Config,getOracle10gConfig,getOracle11gConfig,get360Config,
	/**
	 * 应用软件更新-9300
	 */
	updateTomcat, updateJdk, updateMySql, updateApache, updateNginx, updateZendGuardLoader, updatePython, updateMemcached, updateIISRewrite,  updateFTP,  updateSQLServer2008R2, updateSQLServer2000, updateOracle10g, updateOracle11g, update360,
	/**
	 * 虚拟机脚本执行-9400
	 */
	executeVMScript,
	/**
	 * 操作状态获取-9500
	 */
	getDownloadStatus,
	/**
	 * 错误密钥加密
	 */
	errorKey,
	/**
	 * 重启-9700
	 */
	reboot
	
	
	
}
