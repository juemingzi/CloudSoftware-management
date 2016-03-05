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
	 *            ����������ѹ���ļ�������
	 * @param installPath
	 *            �������İ�װ·�� ��"c:\\zhangke\\" ���һ��Ҫ�� \\
	 * @param password
	 *            Ĭ�ϵ��ǻ�ϵ�¼��ʽ����Ҫ�û���������
	 * @param hostName
	 *            ��������͵�¼�˻���������
	 * @param userName
	 *            �û���
	 * @return
	 * @throws IOException
	 *             �޽��氲װSqlServer2008R2�ķ�����д��1 ж�ؽű���2 ɾ��ж���������ļ���3 ��ѹ�ű���4 ��װ�ű���
	 *             ���н�ѹ�ű��Ͱ�װ�ű����ڰ�װ���õģ�ж�ؽű���ɾ���ļ��Ľű�����ж���ļ���ʱ���õ�
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
		double constm = 1024 * 1024 * 1024;// ��λ��M
		File _file = new File(installPath.substring(0, 2));
		double freeSpace = _file.getFreeSpace() / constm;
		try{
			 Process process = Runtime.getRuntime().exec("osql /?");
			 BufferedReader strCon = new BufferedReader(new InputStreamReader(process.getInputStream()));
			 String line;
			 int i=1;
			 while ((line = strCon.readLine()) != null) {
				 if(i==2){
					 String info="�Ѱ�װ��SqlServer2008R2   "+line;
					 System.out.println(info);//����Ѱ�װSqlServer2008R2��������osql /?������ĵڶ���Ϊ�Ѱ�װ�İ汾��Ϣ
					 return "0x0100A04";//�Ѱ�װ��  �����ٰ�װ
				 }
				 i++;
				 }		 
			 }catch (IOException e){
				 
			 }
		
		if (freeSpace < needSpace) {
			System.out.println("Ӳ��ʣ��ռ䲻�㣬δ���а�װ");
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

			File UIuninstallFile = new File(UIuninstallSqlScript); // 1 ж�ؽű�
																	// ��������ط�д��ж�ؽű�
			if (UIuninstallFile.exists())
				UIuninstallFile.delete();
			UIuninstallFile.createNewFile();
			PrintWriter printWriter = new PrintWriter(UIuninstallFile);
			printWriter.println(installPath+"\\SqlServer2008R2\\setup.exe ^");
			printWriter.println("/qs ^"); // ע��������ط��޽������/q���н������/qs
			printWriter.println("/Action=Uninstall ^");
			printWriter.println("/INSTANCENAME=MSSQLSERVER  ^");
			printWriter
					.println("/FEATURES=SQLENGINE,REPLICATION,FULLTEXT,AS,RS,BIDS,CONN,IS,BC,SDK,BOL,SSMS,ADV_SSMS,SNAC_SDK,OCS   ^ ");
			printWriter.flush();
			printWriter.close();

			File UIdeleteFileScript = new File(UIdelInstallFileScript); // 2
																		// ж��֮��ɾ���ļ��Ľű�
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
																		// ��ѹ�ļ��Ľű�
			if (batfile.exists())
				batfile.delete();
			batfile.createNewFile();
			FileOutputStream outStr = new FileOutputStream(batfile);
			BufferedOutputStream buff = new BufferedOutputStream(outStr);
			buff.write("C: \r\n cd C:\\Program Files (x86)\\WinRAR\r\n"
					.getBytes());
		//	buff.write(("winrar x -o+ " +zipFilePathPre+zipFileName+ " * "+"C:\\softsource\\SqlServer2008R2\\").getBytes());//ע��unzipDiretoryĩβ�����\
			buff.write(("winrar x -o+ " +zipFilePathPre+zipFileName+ " * "+installPath+"\\SqlServer2008R2\\").getBytes());//ע��unzipDiretoryĩβ�����\
			buff.flush();
			buff.close();
			outStr.close();
			wexe.executeVMScript("C:\\setupscripts\\SqlServer2008R2\\unpack.bat");
			Thread.sleep(1000000);

			File UIsetupScriptfile = new File(
					"C:\\setupscripts\\SqlServer2008R2\\UIsetupSqlServer2008R2.bat"); // 4
																						// д��װ�ű�
			if (UIsetupScriptfile.exists())
				UIsetupScriptfile.delete();
			UIsetupScriptfile.createNewFile();
			PrintWriter printWriter2 = new PrintWriter(UIsetupScriptfile);
			printWriter2
					.println(installPath+"\\SqlServer2008R2\\setup.exe  ^");
			printWriter2.println("/IACCEPTSQLSERVERLICENSETERMS ^");
			printWriter2.println("/qs ^"); // ע��������ط��޽������/q���н������/qs
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
			printWriter2.println("/SECURITYMODE=\"SQL\"   ^"); //�����û���¼ģʽ�ǻ��ģʽ
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
				 return "0x0100A00"; //�ű�ִ�гɹ������ذ�װ�ɹ�
			 else
			return "0x0100A01"; // ��װ�ű�ִ�в��ɹ������ذ�װʧ��
		} catch (IOException e) {
			return "0x0100A02";// ���ذ�װSqlServer2008R2����IOException
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
			// �����ű��ĸ�Ŀ¼
			File filepath;
			if (!(filepath = new File("C:\\setupscripts\\oracle"))
					.isDirectory()) {
				filepath.mkdir();
			}
			if (!(filepath = new File("C:\\softsource\\oracle_11g"))
					.isDirectory()) {
				filepath.mkdir();
			}

			//�ж��Ƿ����Ѱ�װ�汾
			File filepath_old = new File(oraclebase);
			if (filepath_old.isDirectory()) {
				return "0x0100C09";	
			}
			
			File batfile1 = new File("C:\\setupscripts\\oracle\\oracleunzip.bat");
			FileOutputStream outStr1 = new FileOutputStream(batfile1);
			BufferedOutputStream buff1 = new BufferedOutputStream(outStr1);
			// �½���ѹ�ű�����ѹoracle��װ��
			batfile1.createNewFile();
			buff1.write("C: \r\n cd C:\\Program Files (x86)\\WinRAR\r\n"
					.getBytes());
			buff1.write(("winrar x -o+ C:\\softsource\\" + zipFileName1 + " C:\\softsource\\oracle_11g\r\n")
					.getBytes()); // ע��unzipDiretoryĩβ�����\
			buff1.write(("winrar x -o+ C:\\softsource\\" + zipFileName2 + " C:\\softsource\\oracle_11g")
					.getBytes()); // ע��unzipDiretoryĩβ�����\
			buff1.flush();
			buff1.close();
			outStr1.close();
			
			System.out.println("oracle��װ�ļ���ʼ��ѹ");
			wexe.executeVMScript("C:\\setupscripts\\oracle\\oracleunzip.bat");
			Thread.sleep(900000); 
//			
			//�����û�����������дau3�ű�
			File au3file = new File("C:\\setupscripts\\oracle\\autosetup.au3");
			FileOutputStream outStr2;
			outStr2 = new FileOutputStream(au3file);
			BufferedOutputStream buff2 = new BufferedOutputStream(outStr2);
			au3file.createNewFile();

			buff2.write("run(\"C:\\softsource\\oracle_11g\\database\\setup.exe\")\r\n".getBytes());
			
			buff2.write("WinWaitActive(\"Oracle Database 11g ���а� 2 ��װ���� - ��װ���ݿ� - ���� 1/9\")\r\n".getBytes());
			buff2.write("Send(\"!n\")\r\n".getBytes());
			buff2.write("WinWaitActive(\"δָ�������ʼ���ַ\")\r\n".getBytes());
			buff2.write("Send(\"!y\")\r\n".getBytes());
			buff2.write("WinWaitActive(\"Oracle Database 11g ���а� 2 ��װ���� - ��װ���ݿ� - ���� 2/9\")\r\n".getBytes());
			buff2.write("Send(\"!n\")\r\n".getBytes());
			buff2.write("Sleep(3000)\r\n".getBytes());
			buff2.write("WinWaitActive(\"Oracle Database 11g ���а� 2 ��װ���� - ��װ���ݿ� - ���� 3/8\")\r\n".getBytes());
			buff2.write("Send(\"!s\")\r\n".getBytes());
			buff2.write("Sleep(3000)\r\n".getBytes());
			buff2.write("Send(\"!n\")\r\n".getBytes());
			buff2.write("WinWaitActive(\"Oracle Database 11g ���а� 2 ��װ���� - ��װ���ݿ� - ���� 4/10\")\r\n".getBytes());
			buff2.write("Send(\"!n\")\r\n".getBytes());
			buff2.write("Sleep(3000)\r\n".getBytes());
			buff2.write("WinWaitActive(\"Oracle Database 11g ���а� 2 ��װ���� - ��װ���ݿ� - ���� 5/10\")\r\n".getBytes());
			buff2.write("Send(\"!n\")\r\n".getBytes());
			buff2.write("Sleep(3000)\r\n".getBytes());
			buff2.write("WinWaitActive(\"Oracle Database 11g ���а� 2 ��װ���� - ��װ���ݿ� - ���� 6/10\")\r\n".getBytes());
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
			
			buff2.write("WinWaitActive(\"Oracle Database 11g ���а� 2 ��װ���� - ��װ���ݿ� - ���� 8/10\")\r\n".getBytes());
			buff2.write("Send(\"!f\")\r\n".getBytes());
			buff2.write("Sleep(3000)\r\n".getBytes());
			
			buff2.write("WinWaitActive(\"[CLASS:SunAwtDialog]\",\"\")\r\n".getBytes());
			buff2.write("Send(\"{ENTER}\")\r\n".getBytes());
			buff2.write("WinWaitActive(\"[CLASS:SunAwtDialog]\",\"\")\r\n".getBytes());
			buff2.write("Send(\"{ENTER}\")\r\n".getBytes());
			buff2.write("WinWaitActive(\"[CLASS:SunAwtDialog]\",\"\")\r\n".getBytes());
			buff2.write("Send(\"{TAB}{TAB}{ENTER}\")\r\n".getBytes());
			
			buff2.write("WinWaitActive(\"Oracle Database 11g ���а� 2 ��װ���� - ��װ���ݿ� - ���� 10/10\")\r\n".getBytes());
			buff2.write("Send(\"!c\");\r\n".getBytes());
		
			buff2.close();
			outStr2.close();
			
			//ִ��au3�ű������н��氲װ
			File autosetup = new File("C:\\setupscripts\\oracle\\startsetup.bat");
			FileOutputStream outStr3 = new FileOutputStream(autosetup);
			BufferedOutputStream buff3 = new BufferedOutputStream(outStr3);
			autosetup.createNewFile();
			
			buff3.write("cd C:\\AutoIt3\r\n".getBytes());
			buff3.write("AutoIt3_x64.exe C:\\setupscripts\\oracle\\autosetup.au3\r\n".getBytes());
			
			buff3.close();
			outStr3.close();
			// ִ�нű�����ɰ�װ��һ����Ҫ���ʮ����
			System.out.println("oracle��װ�ű���ʼִ��");
			String[] r=wexe.executeVMScript("C:\\setupscripts\\oracle\\startsetup.bat");
			 
		   if(r[0].equals("0x0500000"))
		   {
			   return "0x0100C00"; //�ű�ִ�гɹ������ذ�װ�ɹ�
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
				 * ��ȡ������Ϣ
				 */
				ois = new ObjectInputStream(socket.getInputStream());
				String[] msg = (String[]) ois.readObject();
				for(int i=0;i<msg.length;i++){
					System.out.println(msg[i]);
				}
				System.out.println("start install "+msg[0]);
				/**
				 * ��װ
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
				 * �����װ��Ϣ
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
