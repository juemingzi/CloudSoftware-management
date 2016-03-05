package test;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import test.WVMScriptExecute;
//C:\app\wenyanqi
//dbhome1
//C:\app\wenyanqi\oradata
//orcl
//123456QWq
public class oracleset {
	/**
	 * @author repace
	 * @param zipFileName
	 *            传进来的是压缩文件的名字
	 * @param installPath
	 *            传进来的安装路径 如"c:\\zhangke\\" 最后一定要加 \\
	 * @param password
	 *            默认的是混合登录方式，需要用户传进密码
	 * @param hostName
	 *            启动服务和登录账户的主机名
	 * @param userName
	 *            用户名
	 * @return
	 * @throws IOException
	 *             无界面安装SqlServer2008R2的方法中写了1 卸载脚本、2 删除卸载软件后的文件、3 解压脚本和4 安装脚本。
	 *             其中解压脚本和安装脚本是在安装中用的，卸载脚本和删除文件的脚本是在卸载文件的时候用的
	 * @throws InterruptedException 
	 */

	public static String setupSQLServer2008R2Interface(String zipFileName, String installPath,
			String password, String hostName, String userName)
			throws IOException, InterruptedException {
		installPath = installPath.replace("/", "\\");
		String result;
		String appConfigFilePath = "C:\\setupscripts\\";
		String zipFilePathPre = "C:\\softsource\\";
		WVMScriptExecute wexe = new WVMScriptExecute();
	
		
		//double needSpace = 12;
		double needSpace = 5;
		double constm = 1024 * 1024 * 1024;// 单位是M
		File _file = new File(installPath.substring(0, 2));
		double freeSpace = _file.getFreeSpace() / constm;
		try{
			 Process process = Runtime.getRuntime().exec("osql /?");
			 BufferedReader strCon = new BufferedReader(new InputStreamReader(process.getInputStream()));
			 String line;
			 int i=1;
			 while ((line = strCon.readLine()) != null) {
				 if(i==2){
					 String info="已安装的SqlServer2008R2   "+line;
					 System.out.println(info);//如果已安装SqlServer2008R2，则输入osql /?则输出的第二行为已安装的版本信息
					 return "0x0100A04";//已安装过  无需再安装
				 }
				 i++;
				 }		 
			 }catch (IOException e){
				 
			 }
		
		if (freeSpace < needSpace) {
			System.out.println("硬盘剩余空间不足，未进行安装");
			return "0x0100A03";//
		}

		
		String UIsetupSqlScript = "c:\\setupscripts\\"+"SqlServer2008R2\\UIsetupSqlServer2008R2.bat";
		String UIuninstallSqlScript = "c:\\setupscripts\\"+ "SqlServer2008R2\\UIuninstallSqlServer2008R2.bat";
		String UIdelInstallFileScript = "c:\\setupscripts\\"+ "SqlServer2008R2\\UIdelInstallFileScript.bat";
		try {

			File f1 = new File("c:\\setupscripts\\");
			if (!f1.exists())
				f1.mkdir();
			File f2 = new File("c:\\setupscripts\\" + "SqlServer2008R2");
			if (!f2.exists())
				f2.mkdir();
			
			File f3 = new File(installPath+"\\SqlServer2008R2");
			if (!f3.exists())
				f3.mkdir();

			File UIuninstallFile = new File(UIuninstallSqlScript); // 1 卸载脚本
																	// 先在这个地方写好卸载脚本
			if (UIuninstallFile.exists())
				UIuninstallFile.delete();
			UIuninstallFile.createNewFile();
			PrintWriter printWriter = new PrintWriter(UIuninstallFile);
			printWriter.println(installPath+"\\SqlServer2008R2\\setup.exe ^");
			printWriter.println("/qs ^"); // 注意在这个地方无界面的是/q，有界面的是/qs
			printWriter.println("/Action=Uninstall ^");
			printWriter.println("/INSTANCENAME=MSSQLSERVER  ^");
			printWriter
					.println("/FEATURES=SQLENGINE,REPLICATION,FULLTEXT,AS,RS,BIDS,CONN,IS,BC,SDK,BOL,SSMS,ADV_SSMS,SNAC_SDK,OCS   ^ ");
			printWriter.flush();
			printWriter.close();

			File UIdeleteFileScript = new File(UIdelInstallFileScript); // 2
																		// 卸载之后删除文件的脚本
			if (UIdeleteFileScript.exists())
				UIdeleteFileScript.delete();
			UIdeleteFileScript.createNewFile();
			PrintWriter printWriter1 = new PrintWriter(UIdelInstallFileScript);
			printWriter1
					.println("rd/s/q \"C:\\Program Files\\Microsoft SQL Server\"");
			printWriter1
					.println("rd/s/q \"C:\\Program Files (x86)\\Microsoft SQL Server\"");
			printWriter1
					.println("rd/s/q \"C:\\Program Files (x86)\\Microsoft SQL Server Compact Edition\"");
			printWriter1.println("rd/s/q \"" + installPath + "\"");

			printWriter1.flush();
			printWriter1.close();

			File batfile = new File(
					"C:\\setupscripts\\SqlServer2008R2\\unpack.bat"); // 3
																		// 解压文件的脚本
			if (batfile.exists())
				batfile.delete();
			batfile.createNewFile();
			FileOutputStream outStr = new FileOutputStream(batfile);
			BufferedOutputStream buff = new BufferedOutputStream(outStr);
			buff.write("C: \r\n cd C:\\Program Files (x86)\\WinRAR\r\n"
					.getBytes());
		//	buff.write(("winrar x -o+ " +zipFilePathPre+zipFileName+ " * "+"C:\\softsource\\SqlServer2008R2\\").getBytes());//注释unzipDiretory末尾必须带\
			buff.write(("winrar x -o+ " +zipFilePathPre+zipFileName+ " * "+installPath+"\\SqlServer2008R2\\").getBytes());//注释unzipDiretory末尾必须带\
			buff.flush();
			buff.close();
			outStr.close();
			wexe.executeVMScript("C:\\setupscripts\\SqlServer2008R2\\unpack.bat");
			Thread.sleep(1000000);

			File UIsetupScriptfile = new File(
					"C:\\setupscripts\\SqlServer2008R2\\UIsetupSqlServer2008R2.bat"); // 4
																						// 写安装脚本
			if (UIsetupScriptfile.exists())
				UIsetupScriptfile.delete();
			UIsetupScriptfile.createNewFile();
			PrintWriter printWriter2 = new PrintWriter(UIsetupScriptfile);
			printWriter2
					.println(installPath+"\\SqlServer2008R2\\setup.exe  ^");
			printWriter2.println("/IACCEPTSQLSERVERLICENSETERMS ^");
			printWriter2.println("/qs ^"); // 注意在这个地方无界面的是/q，有界面的是/qs
			printWriter2.println("/ACTION=Install ^");
			printWriter2
					.println("/FEATURES=SQLENGINE,REPLICATION,FULLTEXT,AS,RS,BIDS,CONN,IS,BC,SDK,BOL,SSMS,ADV_SSMS,SNAC_SDK,OCS  ^");
			printWriter2.println("/INSTANCENAME=\"MSSQLSERVER\" ^");
			printWriter2
					.println("/AGTSVCACCOUNT=\"NT AUTHORITY\\NETWORK SERVICE\" ^");
			printWriter2
					.println("/ASSVCACCOUNT=\"NT AUTHORITY\\NETWORK SERVICE\"  ^");
			printWriter2
					.println("/SQLSVCACCOUNT=\"NT AUTHORITY\\NETWORK SERVICE\" ^");
			printWriter2
					.println("/ISSVCACCOUNT=\"NT AUTHORITY\\NETWORK SERVICE\" ^");
			printWriter2
					.println("/RSSVCACCOUNT=\"NT AUTHORITY\\NETWORK SERVICE\" ^");

			printWriter2.println("/SAPWD=" + "\"" + password + "\"  ^");
			printWriter2.println("/SECURITYMODE=\"SQL\"   ^"); //设置用户登录模式是混合模式
			printWriter2.println("/INSTALLSHAREDDIR=" + "\"" + installPath+ "\\ProFiles" + "\"  ^");
			printWriter2.println("/INSTALLSHAREDWOWDIR=" + "\"" + installPath+ "\\ProFiles(x86)" + "\"  ^");
			printWriter2.println("/INSTANCEDIR=" + "\"" + installPath+ "\\ProFiles" + "\"  ^");
			printWriter2.println("/ASDATADIR=" + "\"" + installPath+ "\\ProFiles\\MSAS10_50.MSSQLSERVER\\OLAP\\Data" + "\"  ^");
			printWriter2.println("/ASLOGDIR=" + "\"" + installPath+ "\\ProFiles\\MSAS10_50.MSSQLSERVER\\OLAP\\Log" + "\"  ^");
			printWriter2.println("/ASBACKUPDIR=" + "\"" + installPath+ "\\ProFiles\\MSAS10_50.MSSQLSERVER\\OLAP\\Backup"+ "\"  ^");
			printWriter2.println("/ASTEMPDIR=" + "\"" + installPath+ "\\ProFiles\\MSAS10_50.MSSQLSERVER\\OLAP\\Temp" + "\"  ^");
			printWriter2.println("/ASCONFIGDIR=" + "\"" + installPath+ "\\ProFiles\\MSAS10_50.MSSQLSERVER\\OLAP\\Config"+ "\"  ^");
			printWriter2.println("/ASSYSADMINACCOUNTS=" + "\"" + hostName+ "\\" + userName + "\"   ^");
			printWriter2.println("/SQLSYSADMINACCOUNTS=" + "\"" + hostName+ "\\" + userName + "\"   ^");

			printWriter2.println("/AGTSVCSTARTUPTYPE=\"Manual\"   ^");
			printWriter2.println("/ISSVCSTARTUPTYPE=\"Manual\"   ^");
			printWriter2.println("/ASSVCSTARTUPTYPE=\"Manual\"   ^");
			printWriter2.println("/SQLSVCSTARTUPTYPE=\"Manual\"   ^");
			printWriter2.println("/BROWSERSVCSTARTUPTYPE=\"Manual\"   ^");
			printWriter2.println("/RSSVCSTARTUPTYPE=\"Manual\"   ^");

			printWriter2.println("/IACCEPTSQLSERVERLICENSETERMS");
			printWriter2.flush();
			printWriter2.close();

			 String[] r=wexe.executeVMScript(appConfigFilePath+"SqlServer2008R2\\UIsetupSqlServer2008R2.bat");
			 if(r[0].equals("0x0500000"))
				 return "0x0100A00"; //脚本执行成功，返回安装成功
			 else
			return "0x0100A01"; // 安装脚本执行不成功，返回安装失败
		} catch (IOException e) {
			return "0x0100A02";// 返回安装SqlServer2008R2出现IOException
		} 
//		catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return "0x0100A02";
//		}

	}
	public static String setupOracle(String zipFileName1, String zipFileName2, String oraclebase,String oraclehome,String databasefilepath,String databasename,String password) throws InterruptedException{
		
		String result = "";
		databasefilepath = databasefilepath.replace("/", "\\");
		oraclebase = oraclebase.replace("/", "\\");
		
		WVMScriptExecute wexe = new WVMScriptExecute();
		try  {
			// 创建脚本的根目录
			File filepath;
			if (!(filepath = new File("C:\\setupscripts\\oracle"))
					.isDirectory()) {
				filepath.mkdir();
			}
			if (!(filepath = new File("C:\\softsource\\oracle_11g"))
					.isDirectory()) {
				filepath.mkdir();
			}

			//判断是否有已安装版本
			File filepath_old = new File(oraclebase);
			if (filepath_old.isDirectory()) {
				return "0x0100C09";	
			}
			
			File batfile1 = new File("C:\\setupscripts\\oracle\\oracleunzip.bat");
			FileOutputStream outStr1 = new FileOutputStream(batfile1);
			BufferedOutputStream buff1 = new BufferedOutputStream(outStr1);
			// 新建解压脚本，解压oracle安装包
			batfile1.createNewFile();
			buff1.write("C: \r\n cd C:\\Program Files (x86)\\WinRAR\r\n"
					.getBytes());
			buff1.write(("winrar x -o+ C:\\softsource\\" + zipFileName1 + " C:\\softsource\\oracle_11g\r\n")
					.getBytes()); // 注释unzipDiretory末尾必须带\
			buff1.write(("winrar x -o+ C:\\softsource\\" + zipFileName2 + " C:\\softsource\\oracle_11g")
					.getBytes()); // 注释unzipDiretory末尾必须带\
			buff1.flush();
			buff1.close();
			outStr1.close();
			
			System.out.println("oracle安装文件开始解压");
			wexe.executeVMScript("C:\\setupscripts\\oracle\\oracleunzip.bat");
			Thread.sleep(900000); 
//			
			//根据用户所传参数编写au3脚本
			File au3file = new File("C:\\setupscripts\\oracle\\autosetup.au3");
			FileOutputStream outStr2;
			outStr2 = new FileOutputStream(au3file);
			BufferedOutputStream buff2 = new BufferedOutputStream(outStr2);
			au3file.createNewFile();

			buff2.write("run(\"C:\\softsource\\oracle_11g\\database\\setup.exe\")\r\n".getBytes());
			
			buff2.write("WinWaitActive(\"Oracle Database 11g 发行版 2 安装程序 - 安装数据库 - 步骤 1/9\")\r\n".getBytes());
			buff2.write("Send(\"!n\")\r\n".getBytes());
			buff2.write("WinWaitActive(\"未指定电子邮件地址\")\r\n".getBytes());
			buff2.write("Send(\"!y\")\r\n".getBytes());
			buff2.write("WinWaitActive(\"Oracle Database 11g 发行版 2 安装程序 - 安装数据库 - 步骤 2/9\")\r\n".getBytes());
			buff2.write("Send(\"!n\")\r\n".getBytes());
			buff2.write("Sleep(3000)\r\n".getBytes());
			buff2.write("WinWaitActive(\"Oracle Database 11g 发行版 2 安装程序 - 安装数据库 - 步骤 3/8\")\r\n".getBytes());
			buff2.write("Send(\"!s\")\r\n".getBytes());
			buff2.write("Sleep(3000)\r\n".getBytes());
			buff2.write("Send(\"!n\")\r\n".getBytes());
			buff2.write("WinWaitActive(\"Oracle Database 11g 发行版 2 安装程序 - 安装数据库 - 步骤 4/10\")\r\n".getBytes());
			buff2.write("Send(\"!n\")\r\n".getBytes());
			buff2.write("Sleep(3000)\r\n".getBytes());
			buff2.write("WinWaitActive(\"Oracle Database 11g 发行版 2 安装程序 - 安装数据库 - 步骤 5/10\")\r\n".getBytes());
			buff2.write("Send(\"!n\")\r\n".getBytes());
			buff2.write("Sleep(3000)\r\n".getBytes());
			buff2.write("WinWaitActive(\"Oracle Database 11g 发行版 2 安装程序 - 安装数据库 - 步骤 6/10\")\r\n".getBytes());
			buff2.write("Sleep(1000)\r\n".getBytes());
			buff2.write("Send(\"{TAB}{TAB}{TAB}\")\r\n".getBytes());
			buff2.write("Sleep(2000)\r\n".getBytes());
			buff2.write(("Send(\""+oraclebase+"\")\r\n").getBytes());
			buff2.write("Sleep(2000)\r\n".getBytes());
			buff2.write("Send(\"{TAB}{TAB}\")\r\n".getBytes());
			buff2.write("Sleep(2000)\r\n".getBytes());
			buff2.write(("send(\""+oraclebase+"\\product\\11.2.0\\"+oraclehome+"\")\r\n").getBytes());
			buff2.write("Sleep(2000)\r\n".getBytes());
			buff2.write("Send(\"{TAB}{TAB}{TAB}{TAB}\")\r\n".getBytes());
			buff2.write("Sleep(2000)\r\n".getBytes());
			buff2.write(("send(\""+databasefilepath+"\")\r\n").getBytes());
			buff2.write("Sleep(2000)\r\n".getBytes());
			buff2.write("Send(\"{TAB}{TAB}{TAB}\")\r\n".getBytes());
			buff2.write("Sleep(2000)\r\n".getBytes());
			buff2.write(("send(\""+databasename+"\")\r\n").getBytes());
			buff2.write("Sleep(2000)\r\n".getBytes());
			buff2.write("Send(\"{TAB}\")\r\n".getBytes());
			buff2.write("Sleep(2000)\r\n".getBytes());
			buff2.write(("send(\""+password+"\")\r\n").getBytes());
			buff2.write("Sleep(2000)\r\n".getBytes());
			buff2.write("Send(\"{TAB}\")\r\n".getBytes());
			buff2.write("Sleep(2000)\r\n".getBytes());
			buff2.write(("send(\""+password+"\")\r\n").getBytes());
			buff2.write("Send(\"!n\")\r\n".getBytes());
			buff2.write("Sleep(3000)\r\n".getBytes());
			
			buff2.write("WinWaitActive(\"Oracle Database 11g 发行版 2 安装程序 - 安装数据库 - 步骤 8/10\")\r\n".getBytes());
			buff2.write("Send(\"!f\")\r\n".getBytes());
			buff2.write("Sleep(3000)\r\n".getBytes());
			
			buff2.write("WinWaitActive(\"[CLASS:SunAwtDialog]\",\"\")\r\n".getBytes());
			buff2.write("Send(\"{ENTER}\")\r\n".getBytes());
			buff2.write("WinWaitActive(\"[CLASS:SunAwtDialog]\",\"\")\r\n".getBytes());
			buff2.write("Send(\"{ENTER}\")\r\n".getBytes());
			buff2.write("WinWaitActive(\"[CLASS:SunAwtDialog]\",\"\")\r\n".getBytes());
			buff2.write("Send(\"{TAB}{TAB}{ENTER}\")\r\n".getBytes());
			
			buff2.write("WinWaitActive(\"Oracle Database 11g 发行版 2 安装程序 - 安装数据库 - 步骤 10/10\")\r\n".getBytes());
			buff2.write("Send(\"!c\");\r\n".getBytes());
		
			buff2.close();
			outStr2.close();
			
			//执行au3脚本，进行界面安装
			File autosetup = new File("C:\\setupscripts\\oracle\\startsetup.bat");
			FileOutputStream outStr3 = new FileOutputStream(autosetup);
			BufferedOutputStream buff3 = new BufferedOutputStream(outStr3);
			autosetup.createNewFile();
			
			buff3.write("cd C:\\AutoIt3\r\n".getBytes());
			buff3.write("AutoIt3_x64.exe C:\\setupscripts\\oracle\\autosetup.au3\r\n".getBytes());
			
			buff3.close();
			outStr3.close();
			// 执行脚本，完成安装。一般需要大概十分钟
			System.out.println("oracle安装脚本开始执行");
			String[] r=wexe.executeVMScript("C:\\setupscripts\\oracle\\startsetup.bat");
			 
		   if(r[0].equals("0x0500000"))
		   {
			   return "0x0100C00"; //脚本执行成功，返回安装成功
		   }

			return "0x0100C01";
		} catch (IOException e) {
			return "0x0100C05";
		}
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return "0x0100C05";
//		}

		
	}
	public static void main(String[] args) throws InterruptedException {
		try {
			ServerSocket server = new ServerSocket(9900);
			while (true) {
				Socket socket = server.accept();
				ObjectInputStream ois = null;
				ObjectOutputStream oos = null;

				/**
				 * 获取输入消息
				 */
				ois = new ObjectInputStream(socket.getInputStream());
				String[] msg = (String[]) ois.readObject();
				for(int i=0;i<msg.length;i++){
					System.out.println(msg[i]);
				}
				System.out.println("start install "+msg[0]);
				/**
				 * 安装
				 */
//				System.out.println("start install oracle");
//				String oraclesetexe = "C:\\Users\\wenyanqi\\Desktop\\test\\ZendGuard-6_0_0.exe";
//				Runtime runtime = Runtime.getRuntime();
//				Process process = runtime.exec(oraclesetexe);
//				BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
//	            String inline;
//	            while ((inline = br.readLine()) != null) {
//	                System.out.println(inline);
//	               
//	            }
//	            br.close();
				String outMsg = "";
				if(msg[0].equals("oracle")){
					outMsg = setupOracle(msg[1],msg[2],msg[3],msg[4],msg[5],msg[6],msg[7]);
				}else{
					outMsg = setupSQLServer2008R2Interface(msg[1],msg[2],msg[3],msg[4],msg[5]);
				}
				
				/**
				 * 输出安装消息
				 */
				System.out.println("install "+msg[0]+" finished");
				oos = new ObjectOutputStream(socket.getOutputStream());
				//String outMsg = "success";
				oos.writeObject(outMsg);
				oos.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
