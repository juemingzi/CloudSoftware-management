package edu.xidian.linux;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



import edu.xidian.windows.AppConfig;

/**
 * @ClassName: LOSConfig
 * @Description: linux上的基础环境配置
 * @author: wangyannan
 * @date: 2014-11-17 下午5:12:44
 */
public class LOSConfig {
	/**
	 * @Title: changePasswd
	 * @Description: 修改密码
	 * @param user
	 * @param password
	 * @return: String
	 */
	public LOSGet lg = new LOSGet();
	
	public String changePasswd(String user, String password) {
		try {
			Runtime rt = Runtime.getRuntime();
			String[] cmds = new String[] { "/bin/sh", "-c", "passwd " + user };
			Process proc = rt.exec(cmds);

			OutputStream out = proc.getOutputStream();
			OutputStreamWriter outsw = new OutputStreamWriter(out);

			/**
			 * 需要提前输入要输入的字符
			 */
			/**
			 * New password:后面需要写新密码
			 */
			outsw.write(password + "\n");

			/**
			 * Retype password:后面需要重新键入新密码
			 */
			outsw.write(password + "\n");
			outsw.close();

			/**
			 * 读错误输入流的线程
			 */
			StreamGobbler errorGobbler = new StreamGobbler(
					proc.getErrorStream(), "ERROR");

			/**
			 * 读标准输入流的线程
			 */
			StreamGobbler outputGobbler = new StreamGobbler(
					proc.getInputStream(), "OUTPUT");

			/**
			 * 启动读错误输入流线程和读标准输入流线程
			 */
			errorGobbler.start();
			outputGobbler.start();

			int exitVal;
			/**
			 * 返回0表示正常
			 */
			exitVal = proc.waitFor();
			System.out.println("ExitValue: " + exitVal);
			if (exitVal == 0)
				return "0x0000000";
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "0x0000001";
	}

	/** 
	 * @Title: changeSecRule 
	 * @Description: 修改安全规则
	 * @param protocol
	 * @param IP
	 * @param port
	 * @return: String
	 * @throws InterruptedException 
	 */
	public String changeSecRule(String protocol, String IP, String port) {
		String fileName = "/home/scripts/changeSecRule.sh";
		File f=new File("/home/scripts");
		if(!f.exists())
				f.mkdirs();
			
		try {
			// 使用这个构造函数时，如果存在changeSecRule.sh文件，
			// 则先把这个文件给删除掉，然后创建新的changeSecRule.sh
			FileWriter writer = new FileWriter(fileName);
			writer.write("iptables -I INPUT -p " + protocol + " --dport " + port
					+ " -j DROP\n");
			writer.write("iptables -I INPUT -s " + IP + " -p " + protocol
					+ " --dport " + port + " -j ACCEPT\n");
			writer.write(" service iptables save\n");
			writer.write(" service iptables restart\n");
			writer.flush();
			writer.close();
			LVMScriptExecute lvmse = new LVMScriptExecute();
			lvmse.executeVMScript(fileName);
			return "0x0000400";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "0x0000401";
	}

	/**
	 * @Title: startService
	 * @Description: 开启服务
	 * @param serviceName
	 * @return: String
	 */
	public String startService(String serviceName) {
		String[] cmds = new String[] { "/bin/sh", "-c",
				"/etc/init.d/" + serviceName + " start" };
		boolean f=false;
		f=execute(cmds);
		if(f)
			return "0x0000100";
		else
			return "0x0000101";
	}

	/**
	 * @Title: stopService
	 * @Description: 关闭服务
	 * @param serviceName
	 * @return: String
	 */
	public String stopService(String serviceName) {
		String[] cmds = new String[] { "/bin/sh", "-c",
				"/etc/init.d/" + serviceName + " stop" };
		boolean f=false;
		f=execute(cmds);
		if(f)
			return "0x0000200";
		else
			return "0x0000201";
	}

	/**
	 * @Title: viewErrLog
	 * @Description: 查看操作系统错误日志
	 * @return: void
	 */
	// public boolean viewErrLog() {
	// String[] cmds = new String[] { "/bin/sh", "-c", "cat /var/log/messages"
	// };
	// return execute(cmds);
	// }

	/**
	 * @Title: diskFormat
	 * @Description: 格式化硬盘
	 * @param diskName
	 * @return: void
	 */
	// public boolean diskFormat(String diskName) {
	// String fileName = "/home/scripts/diskFormat.sh";
	// try {
	// // 使用这个构造函数时，如果存在diskFormat.sh文件，
	// // 则先把这个文件给删除掉，然后创建新的diskFormat.sh
	// FileWriter writer = new FileWriter(fileName);
	// writer.write("iptables –I INPUT –p " + protocol + " –dport " + port
	// + " –j DROP\n");
	// writer.write("iptables –I INPUT –s " + IP + " –p " + protocol
	// + " –dport " + port + " –j ACCEPT\n");
	// writer.write(" service iptables save\n");
	// writer.write(" service iptables restart\n");
	// writer.flush();
	// writer.close();
	// LVMScriptExecute lvmse = new LVMScriptExecute();
	// return lvmse.executeVMScript(fileName);
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// return false;
	// }

	
	/** 
	 * @Title: changeIP 
	 * @Description: 修改IP
	 * @param name
	 * @param netMask
	 * @param IP
	 * @return: String
	 */
	public String changeIP(String name, String netMask, String IP) {
		String[] cmds = new String[] { "/bin/sh", "-c",
				"ifconfig " + name + " " + IP + " netmask " + netMask };
		boolean f=false;
		f=execute(cmds);
		if(f)
			return "0x0000500";
		else
			return "0x0000501";
	}

	/** 
	 * @Title: changeAffiIP 
	 * @Description: 修改附属IP
	 * @param name
	 * @param netMask
	 * @param IP
	 * @return: String
	 */
	public String changeAffiIP(String name, String netMask, String IP) {
		String[] cmds = new String[] { "/bin/sh", "-c",
				"ifconfig " + name + " " + IP + " netmask " + netMask };
		boolean f=false;
		f=execute(cmds);
		if(f)
			return "0x0000600";
		else
			return "0x0000601";
	}
	
	//添加指定网卡上的IP 	ncName：网卡名字 IP：IP地址 netMask：子网掩码
		public String addFSIP(String ncName,String IP,String netMask) throws IOException{
				if(!checkNcName(ncName)){
					System.out.println("没有此网卡");
					return "0x0001003";
				}
				ArrayList<String> records=find();
				boolean[] usedPort=new boolean[256];
				Arrays.fill(usedPort, false);
				for(String record:records){
					if(record.contains(ncName)){
						usedPort[Integer.parseInt(record.split(" ")[1])]=true;
						if(record.contains(IP))
							return "0x0001004";
					}
					
				}
				
				int firstUnusedPort=0;
				for(firstUnusedPort=0;firstUnusedPort<256;firstUnusedPort++){
					if(usedPort[firstUnusedPort]==false){
						break;
					}
				}
				if(firstUnusedPort==256){
					System.out.println("添加失败：此网卡端口已用完");
					 return "0x0001005";
				}
					
					
				String cmd="ifconfig " + ncName + ":"+firstUnusedPort + " " + IP +" netmask " + netMask +" up ";
				String[] cmds = new String[] { "/bin/sh", "-c",
						cmd};
				boolean f=false;
				f=execute(cmds);
				if(f){
					records.add(ncName+" "+firstUnusedPort+" "+IP+" "+netMask);
					write(records);
					return "0x0001000";
					
				}
				else{
					System.out.println("添加失败:执行命令失败");
					return "0x0001001";
				}
		}



			//删除指定网卡上的IP ncName：网卡名字 IP：IP地址
			public String deleteFSIP(String ncName,String IP) throws IOException{
				ArrayList<String> records=find();
				String port="";
				boolean isMinPort=true;
				int index=0;
				int i=0;
				for( i=0;i<records.size();i++){
					String record=records.get(i);
					if(record.contains(ncName)){
						if(record.contains(IP)){
							port=record.split(" ")[1];
							index=i;
							break;
						}
						else{
							isMinPort=false;
						}
					}
						
				}
				if(i==records.size()){
					System.out.println("删除失败：无此IP");
					return "0x0001102";
				}
				String cmd="ifconfig "+ncName+":"+port+" down";
				String[] cmds = new String[] { "/bin/sh", "-c",
						cmd};
				if(execute(cmds)){
					
					records.remove(index);
					
					if(isMinPort){
						for(String record:records){
							if(record.contains(ncName)){
								String[] words=record.split(" ");
								String c="ifconfig " + ncName + ":"+words[1] + " " + words[2] +" netmask " + words[3] +" up ";
								String[] cs=new String[] { "/bin/sh", "-c", c};
								execute(cs);
							}
						}
					}
					
					write(records);
					return "0x0001100";
					
				}
				else{
					System.out.println("删除失败：执行命令失败");
					return "0x0001101";
				}
				
				
				
				
		}


			//修改IP ncName：网卡名字 oIP: 要修改的IP  nIP:修改之后的IP
			public String changeFSIP(String ncName,String oIP,String nIP) throws IOException{
				if(!checkNcName(ncName)){
					System.out.println("没有此网卡");
					return "0x0000603";
				}
				ArrayList<String> records=find();
				String port="";
				int index=0;
				int i=0;
				for( i=0;i<records.size();i++){
					String record=records.get(i);
					if(record.contains(ncName)&&record.contains(oIP)){
						port=record.split(" ")[1];
						index=i;
						break;
					}
						
				}
				if(i==records.size()){
					System.out.println("修改失败，无此IP");
					return "0x0000602";
				}
				//鍏堝垹闄わ紝鍐嶆坊鍔?		
				
				String[] words=records.get(index).split(" ");
				String cmd="ifconfig " + ncName + ":"+words[1] + " " + nIP +" netmask " + words[3] +" up ";
				String[] cmds = new String[] { "/bin/sh", "-c",
						cmd};
				
				if(execute(cmds)){
					
					records.remove(index);
					records.add(ncName+" "+words[1]+" "+nIP+" "+words[3]);
					write(records);
					return "0x0000600";
					
				}
				else{
					System.out.println("修改失败:执行命令失败");
					return "0x0000601";
				}
		}



			//返回网卡信息列表ArrayList<String> 格式：  网卡名字 端口号 IP 子网掩码

			public ArrayList<String> find() throws IOException{
				Process process=Runtime.getRuntime().exec("ifconfig");
				BufferedReader strCon = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line = "";
				ArrayList<String> result=new ArrayList<String>();
				
				while ((line = strCon.readLine()) != null) {
					
				
					if((line.split(" ")[0].contains(":"))){
					String record="";
					record+=line.split(" ")[0].split(":")[0]+" "+line.split(" ")[0].split(":")[1]+" ";
					String IP="";
					String netMask="";
					
					while(!((line = strCon.readLine()).length()==0)){
						Pattern IPPattern = Pattern
								.compile("inet addr:[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}");
						Matcher IPMatcher = IPPattern.matcher(line);
						Pattern NMPattern = Pattern
								.compile("Mask:[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}");
						Matcher NMMatcher = NMPattern.matcher(line);
						if(IPMatcher.find()){
							IP=IPMatcher.group().split(":")[1];
						}
						if(NMMatcher.find()){
							netMask=NMMatcher.group().split(":")[1];
						}
							
					}
					record+=IP+" "+netMask;
					result.add(record);
					//System.out.println(record);
				}
			}
				return result;
		}
			
			
				
				//查找一个网卡上的所有IP 格式 IP1,IP2,IP3,

				public String findALLIPinNc(String ncName) throws IOException{
					ArrayList<String> records=find();
					StringBuffer buffer=new StringBuffer("");
					
					for(String record:records){
						if(record.contains(ncName)){
							buffer.append(record.split(" ")[2]).append(System.getProperty("line.separator"));
						}
					}
					if(buffer.toString().length()==0){
						return "0x0001202";
					}
					return buffer.toString();
					
				}

				//添加，修改，删除成功后 写入开机文件  其他函数调用的方法
			public void write(ArrayList<String> records) throws IOException{
				BufferedReader br=new BufferedReader(new FileReader(new File("/etc/rc.d/rc.local")));
				
				
				StringBuffer buffer=new StringBuffer();
				String line=null;
				while((line=br.readLine())!=null){
					if(line.startsWith("ifconfig")){
						continue;
					}
					
					buffer.append(line).append(System.getProperty("line.separator"));
				}
				
				br.close();
				
				BufferedWriter bw=new BufferedWriter(new FileWriter(new File("/etc/rc.d/rc.local")));
				for(String record:records){
					String[] words=record.split(" ");
					String cmd="ifconfig " + words[0] + ":"+words[1] + " " + words[2] +" netmask " + words[3] +" up ";
					buffer.append(cmd).append(System.getProperty("line.separator"));
				}
				bw.write(buffer.toString());
				bw.flush();
				bw.close();
			}
			
			//判断网卡是否存在
			public boolean checkNcName(String ncName) throws IOException{
				Process process=Runtime.getRuntime().exec("ifconfig");
				BufferedReader strCon = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line = "";
				
				while((line=strCon.readLine())!=null){
					String word=line.split("\\s+")[0];
					if(word.length()==0)
						continue;
			
					
					if(word.equals(ncName)){
							return true;
					}
				}
				
				
				return false;
			}


	
	
	
	

	/**
	 * @Title: changeUlimit
	 * @Description: 修改Ulimit
	 * @param ulimit
	 * @return: String
	 */
	public String changeUlimit(String ulimit) {
		String configPath = "/etc/security/limits.conf";
		boolean flag = AppConfig.judgeExist(configPath, "* soft nofile");
		boolean f1 = false;
		boolean f2=false;
		if(flag){
			f1 = AppConfig.replaceToFile(configPath, "* soft nofile", "* soft nofile "+ulimit);
			f2 = AppConfig.replaceToFile(configPath, "* hard nofile", "* hard nofile "+ulimit);
		}else {
			f1 = AppConfig.appendToFile(configPath, "* soft nofile "+ulimit);
			f2 = AppConfig.appendToFile(configPath, "* hard nofile "+ulimit);
		}
		if(f1&f2){
			
			return "0x0000700";
		}else {
			return "0x0000701";
		}
		
	}

	/**
	 * @Title: execute
	 * @Description: 执行命令的方法
	 * @param cmds
	 * @return: boolean
	 */
	public boolean execute(String[] cmds) {
		try {
			System.out.println("2 "+cmds[2]);
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(cmds);

			/**
			 * 关闭输出流
			 */
			OutputStream out = proc.getOutputStream();
			out.close();

			/**
			 * 读错误输入流的线程
			 */
			StreamGobbler errorGobbler = new StreamGobbler(
					proc.getErrorStream(), "ERROR");

			/**
			 * 读标准输入流的线程
			 */
			StreamGobbler outputGobbler = new StreamGobbler(
					proc.getInputStream(), "OUTPUT");

			/**
			 * 启动读错误输入流线程和读标准输入流线程
			 */
			errorGobbler.start();
			outputGobbler.start();

			int exitVal=0;
			/**
			 * 返回0表示正常
			 */
			exitVal = proc.waitFor();
			System.out.println("ExitValue: " + exitVal);
			if (exitVal == 0)
				return true;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 获取linux密钥
	 * @return
	 * @throws IOException
	 */
	
	public static String[] createKeyPairs() throws IOException{  
		String[] result = new String[3];
		
		String nullstr ="";
		String path = "/root/.ssh/rsa";
		String[] cmd ={ "ssh-keygen", "-t", "rsa", "-f", path , "-P", nullstr};
		try {
			Process process = Runtime.getRuntime().exec(cmd);
//			System.out.println(cmd);
			System.out.println(loadStream(process.getInputStream()));
			System.err.println(loadStream(process.getErrorStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return null;
		}
		result[0]="0x0500000";
        result[1]="";
        result[2]="";
		File privatekey = new File(path);
		if(privatekey.isFile() && privatekey.exists()){
			InputStreamReader read = new InputStreamReader(new FileInputStream(privatekey));

            BufferedReader bufferedReader = new BufferedReader(read);
            
            String lineTxt = null;
            
            while((lineTxt = bufferedReader.readLine()) != null){
            	result[1]+=lineTxt;
            }
		}    
        File publickey = new File(path+".pub");
        if(publickey.isFile() && publickey.exists()){
			InputStreamReader read = new InputStreamReader(new FileInputStream(publickey));

            BufferedReader bufferedReader = new BufferedReader(read);
            
            String lineTxt = null;
            
            while((lineTxt = bufferedReader.readLine()) != null){
            	result[2]+=lineTxt;
            }
		}    
       
        
        result[1]=result[1].split("-----")[2];
        result[2]=result[2].split(" ")[1];
        String pri = result[1];
        System.out.println("private:"+result[1]);
        System.out.println("public:"+result[2]);
		return result;
	}
	private static String loadStream(InputStream in) throws IOException {
		// TODO Auto-generated method stub
		int ptr=0;
		in=new BufferedInputStream(in);
		StringBuffer buffer = new StringBuffer();
		while((ptr=in.read())!=-1){
			buffer.append((char)ptr);
			
		}
		return buffer.toString();
	}
}
