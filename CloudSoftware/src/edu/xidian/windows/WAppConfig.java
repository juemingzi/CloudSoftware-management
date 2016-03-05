package edu.xidian.windows;

import java.io.File;
import java.io.IOException;

import edu.xidian.linux.ApacheConfig;

public class WAppConfig {

	/**
	 * @author ZXQ 闁板秶鐤員omcat
	 * @param filePath
	 * @param keyWord
	 * @param content
	 * @return
	 * @throws IOException
	 */
	public String configTomcat(String filePath, String keyWord, String content) {
		// if (keyWord == null || content == null || content == null)
		// return false;
		boolean flag = false;
		String configPath = "";

		if (keyWord.equalsIgnoreCase("maxMem")) {
			// 閺堬拷銇囬崘鍛摠
			configPath = filePath + "/bin/catalina.bat";
			// 娣囶喗鏁糱at閺傚洣娆�
			flag = TomcatConfig.updateBat(configPath, content);
		} else {
			configPath = filePath + "/conf/server.xml";
			// 娣囶喗鏁紉ml閺傚洣娆�
			flag = TomcatConfig.updateXML(configPath, keyWord, content);
		}

		if (flag)
			return "0x0300000";
		else
			return "0x0300001";

	}

	/**
	 * @author ZXQ 闁板秶鐤咥apache
	 * @param filePath
	 * @param keyWord
	 * @param content
	 * @return
	 */
	public static String configApache(String filePath, String keyWord,
			String content) throws IOException {

		boolean flag = false;
		String configPath = "";
//		flag = AppConfig.judgeExist(filePath + "/conf/httpd.conf","ServerAdmin");
//		if (!flag) {
//			AppConfig.appendToFile(filePath + "/conf/httpd.conf", "ServerAdmin 123@123.com");
//		}	
//		flag = AppConfig.judgeExist(filePath + "/conf/httpd.conf","ServerName");
//		if (!flag) {
//			AppConfig.appendToFile(filePath + "/conf/httpd.conf","ServerName 127.0.0.1:80");
//		}	
		
		if (keyWord.equalsIgnoreCase("MaxClients")) {
			// 閼汇儱鐡ㄩ崷銊ュ彠闁款喖鐡ч崚娆愭禌閹癸拷閸氾箑鍨晶鐐插閸掔増鏋冩禒鑸垫汞鐏忥拷
			configPath = filePath + "/conf/httpd.conf";
			AppConfig.appendToFile(
					configPath,
					"Include conf/extra/httpd-mpm.conf"
							+ System.getProperty("line.separator"));
			configPath = filePath + "/conf/extra/httpd-mpm.conf";
			flag = ApacheConfig.updateConf(configPath, keyWord);

		} else if(keyWord.equalsIgnoreCase("Listen")) {
			configPath = filePath + "/conf/httpd.conf";
		
			flag = AppConfig.replaceToFile(configPath, keyWord, keyWord + " "
					+ content);
			if (flag == false) {
				flag = AppConfig.appendToFile(configPath, keyWord + " "
						+ content);
			}
			flag = AppConfig.replaceToFile(configPath, "ServerName", "ServerName  127.0.0.1:"+content);
			if (flag == false) {
				flag = AppConfig.appendToFile(configPath, keyWord + " "
						+ content);
			}
		}
		else {
			
			configPath = filePath + "/conf/httpd.conf";
			// 閼汇儱鐡ㄩ崷銊ュ彠闁款喖鐡ч崚娆愭禌閹癸拷閸氾箑鍨晶鐐插閸掔増鏋冩禒鑸垫汞鐏忥拷
			flag = AppConfig.replaceToFile(configPath, keyWord, keyWord + " "
					+ content);
			if (flag == false) {
				flag = AppConfig.appendToFile(configPath, keyWord + " "
						+ content);
			}
		}

		if (flag)
			return "0x0300300";
		else
			return "0x0300301";
	}

	/**
	 * @author wyq 闁板秶鐤唍nginx
	 * @param filePath
	 * @param keyWord
	 * @param content
	 * @return
	 * @throws IOException
	 */
	public String configNginx(String filePath, String keyWord, String content)
			throws IOException {
		boolean flag = AppConfig.judgeExist(filePath + "\\conf\\nginx.conf",
				keyWord);
		boolean f = false;
		if (flag) {
			f = AppConfig.replaceToFile(filePath + "\\conf\\nginx.conf",
					keyWord, keyWord + " " + content);
		} else {
			f = AppConfig
					.appendToFile(filePath + "\\conf\\nginx.conf", content);
		}
		if (f) {
			System.out.println("success");
		} else {
			System.out.println("fail");
		}

		if (f)
			return "0x0300400";
		else
			return "0x0300401";

	}

	/**
	 * @author wyq 闁板秶鐤哯endGuardLoader
	 * @param phpPath
	 * @param keyWord
	 * @param content
	 * @return
	 * @throws IOException
	 */
	public String configZendGuardLoader(String phpPath, String keyWord,
			String content) throws IOException {
		// 3.鍒ゆ柇鏄惁鏈夊凡瀹夎鐗堟湰
		phpPath = phpPath.replace("/", "\\");
		System.out.println("change");
		File filepath1 = new File(phpPath + "\\ext\\ZendLoader.dll");
		if (!filepath1.isFile()) {
			return "0x0300502";
		}
		//boolean flag = AppConfig.judgeExist(phpPath, keyWord);
		boolean flag = AppConfig.judgeExist("C:\\windows\\php.ini", keyWord);
		boolean f = false;
		if (flag) {
			System.out.println("input:::::"+keyWord + "=" + content);
			f = AppConfig.replaceToFile("C:\\windows\\php.ini", keyWord,
					keyWord + "=" + content);
		} else {
			System.out.println("C:\\windows\\php.ini "+content);
			f = AppConfig.appendToFile("C:\\windows\\php.ini", content);
		}
		if (f) {
			System.out.println("success");
		} else {
			System.out.println("fail");
		}
		if (f)
			return "0x0300500";
		else
			return "0x0300501";
	}

	/**
	 * @author wzy show mysql瀵归厤缃枃浠剁殑鎿嶄綔 filepath 鏄渶澶栭潰鐨勬暣浣撹矾寰勶紜鏂囦欢鍖呭悕
	 * @param flag琛ㄧず瑕佹彃鍏ョ殑閰嶇疆淇℃伅
	 *            锛屾槸鍚﹀湪閰嶇疆鏂囦欢涓凡瀛樺湪锛宼rue琛ㄧず瀛樺湪锛宖alse琛ㄧず涓嶅瓨鍦�
	 * @param f琛ㄧず鏄惁瀵归厤缃枃浠惰繘琛屾伅鍐欏叆鎴愬姛
	 *            锛宼rue琛ㄧず鎴愬姛锛宖alse琛ㄧず澶辫触
	 */
	public String configMySQL(String filepath, String keyWord, String content) {
		String filePath = filepath + "\\my-default.ini";
		boolean flag = AppConfig.judgeExist(filePath, keyWord);
		boolean f = false;
		if (flag) {
			f = AppConfig.replaceToFile(filePath, keyWord, keyWord + " = "
					+ content);
		} else {
			f = AppConfig.appendToFile(filePath, keyWord + " = " + content);
		}
		if (f) {
			System.out.println("success");
			return "0x0300200";
		} else {
			System.out.println("fail");
			return "0x0300201";
		}
	}

	// /**@author ZXQ
	// * 閰嶇疆Apache
	// * @param filePath
	// * @param keyWord
	// * @param content
	// * @return
	// */
	// public static boolean configApache(String filePath, String keyWord,
	// String content) throws IOException {
	//
	// if (keyWord == null || content == null || filePath == null)
	// return false;
	// boolean flag = false;
	// String configPath = "";
	// if(keyWord.equalsIgnoreCase("MaxClients")) {
	// //鑻ュ瓨鍦ㄥ叧閿瓧鍒欐浛鎹�鍚﹀垯澧炲姞鍒版枃浠舵湯灏�
	// configPath = filePath + "/conf/conftpd.conf";
	// AppConfig.appendToFile(configPath,"Include conf/extratpd-mpm.conf"+System.getProperty("line.separator"));
	// configPath = filePath + "/conf/extratpd-mpm.conf";
	// flag = ApacheConfig.updateConf(configPath, keyWord);
	//
	// } else {
	// configPath = filePath + "/conf/conftpd.conf";
	// //鑻ュ瓨鍦ㄥ叧閿瓧鍒欐浛鎹�鍚﹀垯澧炲姞鍒版枃浠舵湯灏�
	// if(!AppConfig.replaceToFile(configPath,keyWord, keyWord+" "+content)) {
	// flag = AppConfig.appendToFile(configPath, keyWord+" "+content);
	// }
	// }
	// return flag;
	// }

	public static String getMySqlConfig(String filePath, String key) {//此处存在缺陷，应如下面
		String file = filePath + "\\my-default.ini";
		AppConfig app = new AppConfig();
		String result = app.readFileByKey(file, key);
		System.out.println("result::" + result);
		return result;
	}

	// 璇诲彇zendguardloader鐨勯厤缃俊鎭�
	public String getZendGuardLoaderConfig(String keyword) {
		System.out.println("keyword "+keyword);
		boolean flag = AppConfig.judgeExist("C:\\Windows\\php.ini", keyword);
		String f = "";
		if (flag) {
			f = AppConfig.readFileByKey("C:\\Windows\\php.ini", keyword);
			return f;
		} else {
			return "0x0800501";
		}
	}

	// 璇诲彇nginx閰嶇疆鏂囦欢淇℃伅
	public String getNginxConfig(String filePath, String keyWord) {
		boolean flag = AppConfig.judgeExist(filePath + "\\conf\\nginx.conf",
				keyWord);
		String f = "";
		if (flag) {
			f = AppConfig.readFileByKey(filePath + "\\conf\\nginx.conf",
					keyWord);
			
			//处理字符串，得到端口号
			String f_handle[] = f.split(keyWord);
			String f_temp = f_handle[1].trim();  //得到"80;"
			//这里可以试试直接返回f即return f
			return keyWord+"="+f_temp.substring(0,f_temp.length()-1); //得到"80"
			
		} else {
			return "0x0800401";
		}
	}
	
	public String  getTomcatConfig(String filePath, String keyWord) {
		if (keyWord == null || filePath == null )
			return "";
		String configPath = "";

		if (keyWord.equalsIgnoreCase("maxMem")) {
			configPath = filePath + "/bin/catalina.bat";
			String [] s=TomcatConfig.getBat(configPath,keyWord).split(" ");
			int num = s.length - 1;
			return keyWord + " = " +s[num];
		} else {
			configPath = filePath + "/conf/server.xml";
			String [] s=TomcatConfig.getXML(configPath,keyWord).split(" ");
			int num = s.length - 1;
			return keyWord + " = " +s[num];
		}
		
	}
	public String  getApacheConfig(String filePath, String keyWord){
		if (keyWord == null || filePath == null)
			return "";
		String configPath = "";
		
		if(keyWord.equalsIgnoreCase("MaxClients")) {
			configPath = filePath + "/conf/extra/httpd-mpm.conf";
			String [] s=ApacheConfig.getConf(configPath, keyWord).split(" ");
			int num = s.length - 1;
			return keyWord + " = " +s[num];
		} else {
			configPath = filePath + "/conf/httpd.conf";
			String [] s=AppConfig.readFileByKey(configPath,keyWord).split(" ");
			int num = s.length - 1;
			return keyWord + " = " +s[num];
		}
		
	}
}
