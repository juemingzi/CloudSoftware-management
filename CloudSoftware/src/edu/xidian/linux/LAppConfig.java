package edu.xidian.linux;

import java.io.File;
import java.io.IOException;

import edu.xidian.windows.AppConfig;
import edu.xidian.windows.TomcatConfig;

public class LAppConfig {
	
	/**@author ZXQ
	 * 闁板秶鐤員omcat
	 * @param filePath
	 * @param keyWord
	 * @param content
	 * @return
	 * @throws IOException
	 */
	public String configTomcat(String filePath, String keyWord, String content) {
//		if (keyWord == null || content == null || content == null)
//			return false;
		boolean flag = false;
		String configPath = "";

		if (keyWord.equalsIgnoreCase("maxMem")) {
			
			configPath = filePath + "/bin/catalina.sh";
			
			flag = TomcatConfig.updateBat(configPath,content);
		} else {
			configPath = filePath + "/conf/server.xml";
			
			flag = TomcatConfig.updateXML(configPath, keyWord, content);
		}
		
		if(flag)
			return "0x0300000";
		else
			return "0x0300001";
		
	}
	
	/**@author ZXQ
	 * 闁板秶鐤咥pache
	 * @param filePath
	 * @param keyWord
	 * @param content
	 * @return
	 */
	public String configApache(String filePath, String keyWord, String content) throws IOException {
		
//		if (keyWord == null || content == null || filePath == null)
//			return false;
		boolean flag = true;
		String configPath = "";
		if(keyWord.equalsIgnoreCase("MaxClients")) {
			
			configPath = filePath + "/conf/httpd.conf";
			AppConfig.appendToFile(configPath,"Include conf/extra/httpd-mpm.conf"+System.getProperty("line.separator"));
			configPath = filePath + "/conf/extra/httpd-mpm.conf";
			flag = ApacheConfig.updateConf(configPath, keyWord);
			
		} else {
			configPath = filePath + "/conf/httpd.conf";
			
			if(flag){
				flag =AppConfig.replaceToFile(configPath,keyWord, keyWord+" "+content);
			}else{
			flag = AppConfig.appendToFile(configPath, keyWord+" "+content); 
			}
		}
		if(flag)
			return "0x0300300";
		else
			return "0x0300301";
	}
	
	/**@author ZXQ
	 * 闁板秶鐤咶TP
	 * @param filePath
	 * @param keyWord
	 * @param content
	 * @return
	 * @throws IOException
	 */
	public String configFTP(String filePath, String keyWord, String content) throws IOException {
		String configPath = filePath + "/vsftpd.conf";
		
		boolean flag = AppConfig.judgeExist(configPath, keyWord);
		boolean f = false;
		if(flag){
			f = AppConfig.replaceToFile(configPath, keyWord, keyWord+"="+content);
		}else {
			f = AppConfig.appendToFile(configPath, keyWord+"="+content);
		}
		if(f){
			return "0x0300900";
			
		}else {
			return "0x0300901";
		}
		
	}
	/**@author wyq
	 * 闁板秶鐤唍ginx
	 * @param filePath
	 * @param keyWord
	 * @param content
	 * @return
	 * @throws IOException
	 */
	public String configNginx(String filePath, String keyWord, String content) throws IOException {
		boolean flag = AppConfig.judgeExist(filePath+"/conf/nginx.conf", keyWord);
		boolean f = false;
		if(flag){
			f = AppConfig.replaceToFile(filePath+"/conf/nginx.conf", keyWord, keyWord+" "+content+";");
		}else {
			f = AppConfig.appendToFile(filePath+"/conf/nginx.conf", content);
		}
		if(f){
			return "0x0300400";
		}else {
			return "0x0300401";
		}
		
	}
	
	public String configZendGuardLoader(String phppath, String keyWord, String content)throws IOException {
		File filepath1 = new File(phppath+"/ext/ZendGuardLoader.so");
		if (!filepath1.isFile()) {
			return "0x0300502";
		}
		boolean flag = AppConfig.judgeExist(phppath+"/lib/php.ini", keyWord);
		boolean f = false;
		if(flag){
			f = AppConfig.replaceToFile(phppath+"/lib/php.ini", keyWord, keyWord+"="+content);
		}else {
			f = AppConfig.appendToFile(phppath+"/lib/php.ini", content);
		}
		if(f){
			return "0x0300500";
		}else {
			return "0x0300501";
		}
		
	}


	
	/**
	 * @author wzy
	 * show mysql瀵归厤缃枃浠剁殑鎿嶄綔
	 * @param flag琛ㄧず瑕佹彃鍏ョ殑閰嶇疆淇℃伅锛屾槸鍚﹀湪閰嶇疆鏂囦欢涓凡瀛樺湪锛宼rue琛ㄧず瀛樺湪锛宖alse琛ㄧず涓嶅瓨鍦�
	 * @param f琛ㄧず鏄惁瀵归厤缃枃浠惰繘琛屾伅鍐欏叆鎴愬姛锛宼rue琛ㄧず鎴愬姛锛宖alse琛ㄧず澶辫触
	 */
	public String configMySQL(String keyWord, String content){
		String filePath = "/etc/my.cnf";
		boolean flag = AppConfig.judgeExist(filePath, keyWord);
		boolean f = false;
		if(flag){
			f = AppConfig.replaceToFile(filePath, keyWord, keyWord+" = "+content);
		}else {
			f = AppConfig.appendToFile(filePath, keyWord+" = "+content);
		}
		if(f){
			
			return "0x0300200";
		}else {
			
			return "0x0300201";
		}
	}

	public static String getMySqlConfig(String key){
//		String filePath = "/etc/my.cnf";
//		AppConfig app = new AppConfig();
//		String result = app.readFileByKey(filePath, key);
//		return result;
		
		String filePath = "/etc/my.cnf";
		AppConfig app = new AppConfig();
		String result = app.readFileByKey(filePath, key);
		String rs[] = result.split(";");
		return rs[0].replaceAll("\t\t", "");	
	}

	//读取nginx配置文件信息
	public String getNginxConfig(String filePath, String keyWord){
		boolean flag = AppConfig.judgeExist(filePath+"/conf/nginx.conf", keyWord);
		String f = "";
		if(flag){
			f = AppConfig.readFileByKey(filePath+"/conf/nginx.conf", keyWord);
			
			//处理字符串，得到端口号
			String f_handle[] = f.split(keyWord);
			String f_temp = f_handle[1].trim();  //得到"80;"
			
			return keyWord+" = "+f_temp.substring(0,f_temp.length()-1); //得到"80"
		}else {
			return "0x0800401";
		}
	}
	//读取zendguardloader的配置信息
		public String getZendGuardLoaderConfig(String phppath,String keyword){
			boolean flag = AppConfig.judgeExist(phppath + "/lib/php.ini",keyword);
			String f = "";
			if(flag){
				f = AppConfig.readFileByKey(phppath + "/lib/php.ini", keyword);
				//String cf = f.replaceAll("/", "\\\\");
				return f;
			}else {
				return "0x0800501";
			}
		}
		public String  getTomcatConfig(String filePath, String keyWord) {
			if (keyWord == null || filePath == null )
				return "";
			String configPath = "";

			if (keyWord.equalsIgnoreCase("maxMem")) {
				configPath = filePath + "/bin/catalina.sh";
				return TomcatConfig.getBat(configPath,keyWord);	
			} else {
				configPath = filePath + "/conf/server.xml";
				return keyWord+" = "+TomcatConfig.getXML(configPath,keyWord);
			}
			
		}
		public String  getApacheConfig(String filePath, String keyWord){
			if (keyWord == null || filePath == null)
				return "";
			String configPath = "";
			String s = "";
			String[] str;
			
			if(keyWord.equalsIgnoreCase("MaxClients")) {
				configPath = filePath + "/conf/extra/httpd-mpm.conf";
				s = ApacheConfig.getConf(configPath, keyWord);
				str = s.split(" ");
				return  keyWord+" = "+str[1];
			} else {
				configPath = filePath + "/conf/httpd.conf";
				s = AppConfig.readFileByKey(configPath,keyWord);
				str = s.split(" ");
				return  keyWord+" = "+str[1]; 
			}
			
		}
		
		public String  getFTPConfig(String filePath, String keyWord){
			String configPath = filePath + "/vsftpd.conf";
			return AppConfig.readFileByKey(configPath,keyWord);
		}
}
