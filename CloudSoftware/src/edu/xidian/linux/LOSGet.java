package edu.xidian.linux;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;



/** 
 * @ClassName: LOSGet 
 * @Description: TODO
 * @author: wangyannan
 * @date: 2015-4-14 下午5:44:06  
 */
public class LOSGet {

	
	/** 
	 * @Title: getSecRule 
	 * @Description: 获取安全规则
	 * @return
	 * @return: String
	 */
	public  String getSecRule() {
		String result = new String();
		try {
			String[] cmds = new String[] { "/bin/sh", "-c",
					"iptables -nL --line-number" };
			
			result="";
			Process process = Runtime.getRuntime().exec(cmds);
			BufferedReader strCon = new BufferedReader(new InputStreamReader(
					process.getInputStream())); 
			String line;

			while ((line = strCon.readLine()) != null) {
				result = result + line + "\n";
			}
			
			
			return result;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = "0x0900101";
			return result;
		}
	}

	
	/** 
	 * @Title: getServiceStatus 
	 * @Description: 获取服务状态
	 * @param serviceName
	 * @return
	 * @return: String
	 */
	public  String getServiceStatus(String serviceName) {//获取一个服务
		String result = new String();
		try {
			String[] cmds = new String[] { "/bin/sh", "-c",
					"service " + serviceName + " status" };

			result="";
			Process process;

			process = Runtime.getRuntime().exec(cmds);

			BufferedReader strCon = new BufferedReader(new InputStreamReader(
					process.getInputStream())); 
			String line;

			while ((line = strCon.readLine()) != null) {
				result = result + line + "\n";
			}
			if (result.contains("running"))
				result = "running";
			else
				result = "stoped";
			
			return result;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = "0x0900201";
			return result;
		}

	}
	
	
	
	
	/** 
	 * @Title: getServiceStatus 
	 * @Description: 获取服务
	 * @param serviceName
	 * @return
	 * @return: String
	 */
	public  String getService() {//获取全部
		String result = new String();
		try {
			String[] cmds = new String[] { "/bin/sh", "-c",
					"chkconfig" };

			result="";
			Process process;

			process = Runtime.getRuntime().exec(cmds);

			BufferedReader strCon = new BufferedReader(new InputStreamReader(
					process.getInputStream())); 
			String line;

			while ((line = strCon.readLine()) != null) {
				System.out.println(line.trim());
				
				result = result + line + " \r\n  ";
			}
			return result;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = "0x0000802";
			return result;
		}

	}

	

	/** 
	 * @Title: getIP 
	 * @Description: 获取IP
	 * @return
	 * @return: String
	 */
	public  String getIP() {
		String result = new String();
		try {
			String[] cmds = new String[] { "/bin/sh", "-c",
					"ifconfig"  };

			result = "";
			result = "";
			Process process;

			process = Runtime.getRuntime().exec(cmds);

			BufferedReader strCon = new BufferedReader(new InputStreamReader(
					process.getInputStream())); 
			String line;
			boolean flag=false;
			while ((line = strCon.readLine()) != null) {
				if(line.contains("eth0"))
				{
					flag=true;
					continue;
				}
				if(flag)
				{
					int k=line.indexOf("inet addr:");
					result=line.substring(k+10, k+24);
					break;
					
				}
				
				
			}
			
			
			return result;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = "0x0900301";
			return result;
		}
	}

	
	/** 
	 * @Title: getAffiIP 
	 * @Description: 获取附属IP
	 * @return
	 * @return: String
	 */
	public String getAffiIP() {
		String result = new String();
		try {
			String[] cmds = new String[] { "/bin/sh", "-c",
					"ifconfig"  };

			result = "";
			Process process;

			process = Runtime.getRuntime().exec(cmds);

			BufferedReader strCon = new BufferedReader(new InputStreamReader(
					process.getInputStream())); 
			String line;
			boolean flag=false;
			while ((line = strCon.readLine()) != null) {
				if(line.contains("eth0:1"))
				{
					flag=true;
					continue;
				}
				if(flag)
				{
					int k=line.indexOf("inet addr:");
					result=line.substring(k+10, k+24);
					break;
					
				}
				
				
			}
			
			return result;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = "0x0900401";
			return result;
		}
	}

	
	/** 
	 * @Title: getUlimit 
	 * @Description: 获取ulimit
	 * @return
	 * @return: String
	 */
	public String getUlimit() {
		String result = new String();
		try {
			String[] cmds = new String[] { "/bin/sh", "-c",
					"ulimit -n" };
			
			result = "";
			Process process = Runtime.getRuntime().exec(cmds);
			BufferedReader strCon = new BufferedReader(new InputStreamReader(
					process.getInputStream())); 
			String line;

			while ((line = strCon.readLine()) != null) {
				result = result + line + "\n";
			}
			
			return result;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = "0x0900501";
			return result;
		}

	}
}
