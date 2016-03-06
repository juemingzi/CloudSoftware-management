package edu.xidian.agent;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

/**
 * ������汾��ϣ������������Ķ��߳�����
 * ����Ҳ���˶ϵ����� ���Ƿ�ϵ����ص�����ΪĿ���ļ��Ƿ��Ѿ����� �������Ƿ�Ӷϵ���������أ��ֵ��߳����ص��ļ����Ǵ�ָ����λ�ÿ�ʼ���ص�
 * 
 */
public class FtpTransFile {

	private static String fileName; // Ҫ�ϴ������ص��ļ�������
	private static String path;// ��ʱ�ļ��е�Ŀ¼,���ڴ�Ŷ���߳����ص��ļ�
	static long threadBlock = 100 * 1024 * 1024L; //��ʾ�߳̿��С
	//static long threadBlock = 5100 * 1024 * 1024L;
	/**
	 * @author zhangke
	 * 
	 * @param path
	 *            Ҫ�ϴ��ı����ļ�·�� ��"C:/Users/repace/Desktop/zhangke1.txt";
	 * @param server
	 *            ftp������ip��ַ 192.168.242.133
	 * @param userName
	 *            ��¼ftp���û��� test
	 * @param password
	 *            ��¼ftp�û�����Ӧ������ 123456
	 */
	public static void fileUpload(String OStype, String path, String server,
			String userName, String password) { // Ҫ�ϴ����ļ��ı���·��·��
		// Ŀǰ����ɵ����ļ����ϴ�

		if (!(OStype.equalsIgnoreCase("windows") || OStype
				.equalsIgnoreCase("linux"))) {
			System.out.println("����ϵͳ�����������ӦΪwindows��linux");
			return;
		}

		FTPClient ftpClient = new FTPClient();
		ftpClient.enterLocalPassiveMode(); // ��һ�仰һ��Ҫ�ǵü���
		FileInputStream fis = null;
		try {
			ftpClient.connect(server);
			ftpClient.login(userName, password);

			File srcFile = new File(path);// Ҫ�ϴ��ı����ļ�·��
			fis = new FileInputStream(srcFile);
			String storeName = srcFile.getName();// Ҫ�洢���ļ�������
			String remoteFilename = "/mnt/data/ftp/www/" + OStype.toLowerCase() + "/"
					+ storeName;
			ftpClient.changeWorkingDirectory("/mnt/data/ftp/www/" + OStype.toLowerCase()
					+ "/"); // �����ϴ����ļ���centos�ϵ�Ŀ¼���ļ��ϴ����ɹ���Ҫ�鿴ָ��Ŀ¼��Ȩ��
			ftpClient.setBufferSize(1024);
			ftpClient.setControlEncoding("UTF-8");
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);// �����ļ����ͣ������ƣ�
			FTPFile[] files = ftpClient.listFiles(remoteFilename);// �ж���������Ƿ��������ļ�
			if (files.length == 1) {// ������İ������ļ�
				long remoteSize = files[0].getSize();// ������ĵ��ļ���С
				long localSize = srcFile.length();// ����Ҫ�ϴ����ļ���С
				if (remoteSize == localSize) { // �������������ļ������Һʹ���Ҫ�ϴ����ļ���Сһ������˵Ҫ�ϴ����ļ��Ѵ���
					System.out.println("Ҫ�ϴ����ļ��Ѵ���");
					ftpClient.disconnect();
					return;
				} else if (remoteSize > localSize) {// ������ĵ��ļ���Ҫ�ϴ��Ĵ󣬿������ϴ����ļ����޸��ˣ�Ȼ���ٴ��ϴ���
					System.out.println("������ĵ�����ȼ����ϴ���Ҫ�������ϴ�����������Ҫ�ϴ����ļ���");
					ftpClient.disconnect();
					return;
				}
				// ������ĵĴ���ļ���Ҫ�ϴ����ļ�С�������ƶ��ļ��ڶ�ȡָ��,ʵ�ֶϵ����� **************
				if (fis.skip(remoteSize) == remoteSize) {
					ftpClient.setRestartOffset(remoteSize);
					boolean i = ftpClient.storeFile(
							new String(storeName.getBytes("UTF-8"),
									"iso-8859-1"), fis);
					if (i) {
						System.out.println("�ļ��ϵ������ɹ�");
						ftpClient.disconnect();
						return;
					}
				}
			} else { // ������Ĳ�����Ҫ�ϴ����ļ��������������ɹ������ϴ�ȫ�µ��ļ�����
				boolean i = ftpClient.storeFile(
						new String(storeName.getBytes("UTF-8"), "iso-8859-1"),
						fis);
				System.out.println("�ļ��ϴ�" + i);
			}

		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("FTP�ͻ��˳���", e);
		} finally {
			try {
				fis.close();
				ftpClient.disconnect();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new RuntimeException("�ر�FTP���ӷ����쳣��", e);
			}
		}
	}

	/**
	 * *
	 * 
	 * @param OStype
	 *            ����ϵͳ������ windows������linux
	 * @param fileName
	 *            ָ��Ҫ���ص��ļ����� �Ӻ�׺��
	 * @param storePath
	 *            ����֮����Ҫ�ڱ��صĴ洢·������windowϵͳ��֧�������ļ�·��\\ ����/
	 * @param server
	 *            ftp������IP��ַ
	 * @param userName
	 *            ftp����ĵ�¼�� test
	 * @param password
	 *            ���¼����Ӧ�ĵ�¼���� 123456
	 * @throws FileNotFoundException
	 * @throws InterruptedException
	 */
	public static void fileDownload(String OStype, String fileNames,
			String storePath, String server, String userName, String password)
			throws FileNotFoundException, InterruptedException { // �����Ǵ���׺���ļ����ֺ�����֮��Ҫ�洢�ı���·��
		// ����ɵ����ļ������� ,

		if (!(OStype.equalsIgnoreCase("windows") || OStype
				.equalsIgnoreCase("linux"))) {
			System.out.println("����ϵͳ�����������ӦΪwindows��linux");
			return;
		}
		fileName = fileNames;

		File file = new File(storePath);
		if (!file.exists()) {// �ж��ļ����Ƿ����,����������򴴽��ļ���
			file.mkdir();
		}

		FTPClient ftpClient = new FTPClient();
		ftpClient.enterLocalPassiveMode(); // ��һ�仰һ��Ҫ�ǵü���
		String remoteFileName = "/mnt/data/ftp/www/" + OStype.toLowerCase() + "/"
				+ fileName; // �������ϵ��ļ���ǰ�����ļ��е����֣���������ļ�������
		String localFileName = "";// ����Ҫ�洢���ļ�����·�� �ļ��м����ļ���
		
		try {
			ftpClient.connect(server);
			ftpClient.login(userName, password);
			ftpClient.setBufferSize(1024);
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE); // �����ļ����ͣ������ƣ�
			FTPFile[] files = ftpClient.listFiles(remoteFileName);
			if (files.length == 0) { // �ж���������Ƿ���Ҫ���ص����
				System.out.println("�������û���ҵ�Ҫ���ص����");
				ftpClient.disconnect();
				return;
			} else { // ������İ����������ص��ļ�

				long localSize = 0L; // ��¼�����ļ��Ĵ�С
				if (storePath.endsWith("\\") || storePath.endsWith("/"))// �洢·��ֱ����ĳ�����µĸ�Ŀ¼�����û�����������б��
				{
					localFileName = storePath + fileName;
					path = storePath
							+ fileName.substring(0, fileName.indexOf("."))
							+ "Temp/";
				} else {
					localFileName = storePath + "/" + fileName;
					path = storePath + "/"
							+ fileName.substring(0, fileName.indexOf("."))
							+ "Temp/";
				}
				
				File localFile = new File(localFileName);
				long remoteSize = files[0].getSize();// ������ĵ��ļ���С
				if (localFile.exists()) {// ָ�����ص��ļ��ڱ����ļ������Ѿ�����
					localSize = localFile.length();// �Ѵ��ڵ��ļ���С
					if (remoteSize == localSize) {
						System.out.println("�ļ������ع�������������");
						return;
					} else if (remoteSize > localSize) { // ֮ǰ����δ��ɣ�ʵ�ֶϵ�����
						System.out.println("�ϵ����ء�����");
					}
					if (remoteSize < localSize) {// ������ص��ļ���������ĵ��ļ�����˵�����ص��ļ������д�ɾ����Ȼ���ͷ��ʼ����
						localFile.delete();
						System.out.println("������¿�ʼ����");
						localSize = 0L;
					}
				} else {// ָ�����ص��ļ��ڱ����ļ����ڲ�����,��ͷ�����ļ�
					localSize = 0L;
					System.out.println("�����ͷ����");
				}	
				
				File tempfile = new File(path);
				if (tempfile.exists()) {// �ж��ļ����Ƿ����,����Ѿ����ڣ���ɾ�����ļ��м������е����ļ���������������߳�Ӱ���������ع���
					System.out.println("delete ֮ǰ����ʱ�ļ���");
					deleteTempFile(path);
				}
				tempfile.mkdir();// �½������ʱ�ļ��е�Ŀ¼
				
				ExecutorService exec = Executors.newCachedThreadPool(); // ��ʼ�������߳������ļ�
				int threadNum = (int) ((remoteSize - localSize) / threadBlock + 1);// ÿ50M��һ���߳�����
																					// �����߳�����
				System.out.println("�ֳɵ��̸߳���" + threadNum);
				CountDownLatch latch = new CountDownLatch(threadNum);
				System.out.println(fileNames + "���������ص��ļ���С"
						+ (remoteSize - localSize));
				long[] startPos = new long[threadNum];
				ChildThread[] childThreads = new ChildThread[threadNum];// ChildThread
																			// ���ChildThread1����4���޸�
				for (int i = 0; i < threadNum; i++) {
					startPos[i] = localSize + i * threadBlock; // ����ÿ���߳̿�ʼ�����ļ�����ʼλ��
					
					childThreads[i] = new ChildThread(OStype, fileName,
							storePath, server, userName, password, startPos[i],
							i, latch); // �����߳� �̱߳�Ŵ�0��ʼ
					exec.execute(childThreads[i]);// ��ʼִ���߳�
				}

				latch.await(); // �ȴ����е��̶߳����н���
				exec.shutdown();
				tempFileToTargetFile(localFileName, childThreads, threadNum);// ����ʱ�õ����ļ����ڵ��ļ��ϲ���Ŀ���ļ�

			}

		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("FTP�ͻ��˳���", e);
		} finally {
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("�ر�FTP���ӷ����쳣��", e);
			}
		}
	}

	/**
	 * @author repace ����ʱ�ļ����ڵ��ļ���д��Ŀ���ļ��� ���������߳����µ��ļ����кϲ�
	 * @param target
	 *            Ŀ���ļ� ��֮ǰ�û����͵�����Ҫ���������ص��ļ���ŵľ���·����Ŀ¼
	 *            ����Ҫ���ص���test.txt�ļ��������c:\\123\\�ļ����� ��Ŀ���ļ�target
	 *            ָ�ĵľ���c:\\123\\test.txt
	 * @param tempFile
	 *            ��ʱ�ļ��е�Ŀ¼���� c:\\123\\testTemp\\
	 * @param threadNum
	 * @return
	 * @throws IOException
	 */

	public static boolean tempFileToTargetFile(String target,
			ChildThread[] childThreads, int threadNum) throws IOException { // ��ɰ���ʱ�ļ����ڵ���־��д��Ŀ���ļ���
		
		System.out.println("KAISHI HEBING");
		boolean result = true;

		FileInputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(target, true); // ׷������
			for (int i = 0; i < threadNum; i++) { // �����������̴߳�������ʱ�ļ�����˳�����������д��Ŀ���ļ���
				inputStream = new FileInputStream(
						childThreads[i].localTempFileName);
				int len = 0;
				byte[] b = new byte[1024];
				int count = 0;
				while ((len = inputStream.read(b)) != -1) {
					outputStream.write(b, 0, len);
					outputStream.flush();
					count += len;
				}
				inputStream.close();
				
			}
			outputStream.flush();
			outputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (outputStream != null) {
			outputStream.close();
		}
		File file = new File(target);
		System.out.print(target + "���صõ����ļ���С�� " + file.length());
		deleteTempFile(path);// ɾ����ʱ�ļ���
		return result;
	}

	public static void deleteTempFile(String Path) {//ɾ����ʱ�ļ���

		File file = new File(Path);
		if (file.isFile()) {// ��ʾ���ļ������ļ���
			file.delete();
		} else {
			// ���ȵõ���ǰ��·��
			String[] childFilePaths = file.list();
			for (String childFilePath : childFilePaths) {
				File childFile = new File(file.getAbsolutePath() + "/"
						+ childFilePath);
				String s = childFile.getAbsolutePath();
				deleteTempFile(s);
			}
			file.delete();
		}
	}

}